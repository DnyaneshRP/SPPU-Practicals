import java.io.*;
import java.util.*;

class Pass1MacroProcessor {

    // Data Structures
    static class MNTEntry {
        String name; int pp, kp, mdtp, kpdtp;
        MNTEntry(String n, int pp, int kp, int mdtp, int kpdtp) {
            this.name=n; this.pp=pp; this.kp=kp; this.mdtp=mdtp; this.kpdtp=kpdtp;
        }
        public String toString(){return String.format("%-6s %-3d %-3d %-4d %-4d",name,pp,kp,mdtp,kpdtp);}
    }
    static class KPDTABEntry {
        String param, def;
        KPDTABEntry(String p,String d){param=p;def=d;}
        public String toString(){return String.format("%-5s %-6s",param,def);}
    }

    static List<MNTEntry> MNT=new ArrayList<>();
    static List<KPDTABEntry> KPDTAB=new ArrayList<>();
    static List<String> MDT=new ArrayList<>();
    static Map<String,List<String>> PNTABs=new LinkedHashMap<>();
    static List<String> intermediate=new ArrayList<>();

    public static void main(String[] args)throws Exception{
        if(args.length<1){System.out.println("Usage: java Pass1MacroProcessor <inputfile>");return;}
        List<String> src=readFile(args[0]);
        pass1(src);
        printOutput();
        saveOutputFiles();   // <-- save outputs for pass 2
    }

    static void pass1(List<String> src){
        boolean inMacro=false; String macro=null; List<String> PNTAB=null;
        int pp=0,kp=0,mdtp=0,kpdtp=0;
        for(String line:src){
            line=line.trim(); if(line.isEmpty()) continue;
            if(line.equalsIgnoreCase("MACRO")){inMacro=true; kpdtp=KPDTAB.size()+1; continue;}
            if(inMacro){
                if(macro==null){ // header
                    String[] parts=line.split("\\s+",2);
                    macro=parts[0]; PNTAB=new ArrayList<>();
                    if(parts.length>1){
                        for(String p:parts[1].split(",")){
                            if(p.contains("=")){
                                String[] kv=p.split("=");
                                PNTAB.add(kv[0]); // keep &
                                KPDTAB.add(new KPDTABEntry(kv[0], kv.length>1?kv[1]:"-")); // keep &
                                kp++;
                            }
                            else{
                                PNTAB.add(p); // keep &
                                pp++;
                            }
                        }
                    }
                    mdtp=MDT.size()+1; PNTABs.put(macro,PNTAB); MNT.add(new MNTEntry(macro,pp,kp,mdtp,kpdtp));
                } else if(line.equalsIgnoreCase("MEND")){
                    MDT.add("MEND"); inMacro=false; macro=null; pp=kp=0;
                }
                else{
                    for(int i=0;i<PNTAB.size();i++) line=line.replace(PNTAB.get(i),"(P,"+(i+1)+")");
                    MDT.add(line);
                }
            } else intermediate.add(line);
        }
    }

    static void printOutput(){
        System.out.println("============= MNT =============");
        System.out.printf("%-5s %-6s %-3s %-3s %-4s %-4s\n","Idx","Name","PP","KP","MDTP","KPDTP");
        for(int i=0;i<MNT.size();i++) System.out.printf("%-5d %s\n",i+1,MNT.get(i));

        System.out.println("\n===== KPDTAB =====");
        for(int i=0;i<KPDTAB.size();i++) System.out.printf("%-5d %s\n",i+1,KPDTAB.get(i));

        System.out.println("\n========= MDT =========");
        for(int i=0;i<MDT.size();i++) System.out.printf("%-5d %s\n",i+1,MDT.get(i));

        System.out.println("\n== PNTAB ==");
        for(Map.Entry<String,List<String>> e:PNTABs.entrySet()){
            System.out.println("\nMacro: "+e.getKey());
            for(int i=0;i<e.getValue().size();i++) 
                System.out.printf("%-5d %s\n",i+1,e.getValue().get(i));  // keep &
        }

        System.out.println("\n===== Intermediate Code =====");
        intermediate.forEach(System.out::println);
    }

    static void saveOutputFiles() throws IOException {
        // Save MNT
        try(PrintWriter pw=new PrintWriter("MNT.txt")){
            pw.printf("%-5s %-6s %-3s %-3s %-4s %-4s\n","Idx","Name","PP","KP","MDTP","KPDTP");
            for(int i=0;i<MNT.size();i++) pw.printf("%-5d %s\n",i+1,MNT.get(i));
        }

        // Save KPDTAB
        try(PrintWriter pw=new PrintWriter("KPDTAB.txt")){
            for(int i=0;i<KPDTAB.size();i++) pw.printf("%-5d %s\n",i+1,KPDTAB.get(i));
        }

        // Save MDT
        try(PrintWriter pw=new PrintWriter("MDT.txt")){
            for(int i=0;i<MDT.size();i++) pw.printf("%-5d %s\n",i+1,MDT.get(i));
        }

        // Save PNTAB (one per macro)
        for(Map.Entry<String,List<String>> e:PNTABs.entrySet()){
            try(PrintWriter pw=new PrintWriter("PNTAB_"+e.getKey()+".txt")){
                for(int i=0;i<e.getValue().size();i++) 
                    pw.printf("%-5d %s\n",i+1,e.getValue().get(i));  // keep &
            }
        }

        // Save Intermediate Code
        try(PrintWriter pw=new PrintWriter("Intermediate.txt")){
            intermediate.forEach(pw::println);
        }

        System.out.println("\n✅ All tables saved to files (MNT.txt, KPDTAB.txt, MDT.txt, PNTAB_*.txt, Intermediate.txt)");
    }

    static List<String> readFile(String f)throws IOException{
        List<String> l=new ArrayList<>(); 
        BufferedReader br=new BufferedReader(new FileReader(f));
        for(String s;(s=br.readLine())!=null;) 
            if(!s.trim().isEmpty()) l.add(s.trim()); 
        br.close(); 
        return l;
    }
}

