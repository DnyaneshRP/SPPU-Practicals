import java.io.*;
import java.util.*;

class Pass1Assembler{
    private final Map<String, String[]> opTab = new HashMap<>();
    private final Map<String, Integer> regTab = Map.of("AREG", 1, "BREG", 2, "CREG", 3, "DREG", 4);
    private final  Map<String, Integer> condTab = Map.of("LT", 1, "LE", 2, "EQ", 3, "GT", 4, "GE", 5, "ANY", 6);
    private final ArrayList<IC> icList = new ArrayList<>();
    private final LinkedHashMap<String, Integer> symTab = new LinkedHashMap<>();
    private final ArrayList<String[]> litTab = new ArrayList<>();
    private final ArrayList<Integer> poolTab = new ArrayList<>();
    private int litCount = 1, poolStart = 1, LC = 0;
    
    public Pass1Assembler(){
        String[][] is = {{"STOP","00"}, {"ADD","01"}, {"SUB","02"}, {"MULT","03"}, {"MOVER","04"}, {"MOVEM","05"}, {"COMP","06"}, {"BC","07"}, {"DIV","08"}, {"READ","09"}, {"PRINT","10"}};
        for(String[] x : is){
            opTab.put(x[0], new String[]{"IS", x[1]});
        }
        String[][] ad = {{"START","01"}, {"END","02"}, {"ORIGIN","03"}, {"EQU","04"}, {"LTORG","05"}};
        for(String[] x : ad){
            opTab.put(x[0], new String[]{"AD", x[1]});
        }
        opTab.put("DC", new String[]{"DL", "01"});
        opTab.put("DS", new String[]{"DL", "02"});
    }

    static class IC{
        String src;
        int lc;
        String op;
        String o1;
        String o2;
        IC(String s, int l, String o, String a, String b){
            src = s;
            lc = l;
            op = o;
            o1 = a;
            o2 = b;
        }
    }

    public void processFile(String f){
        try(BufferedReader br = new BufferedReader(new FileReader(f))){
            String line;
            while((line = br.readLine()) != null){
                line = line.trim();
                if(!line.isEmpty() && !line.startsWith(";")){
                    process(line);
                }
            }
            processRemLit();
            saveFile();
            printConsole();
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
    }

    private void process(String line){
        String[] t = {"", "", "", ""};
        String[] p = line.split("\\s+");
        System.arraycopy(p, 0, t, 0, Math.min(p.length, 4));
        if(opTab.containsKey(t[0])){
            for(int i=3; i>0; i--){
                t[i] = t[i-1];
            }
            t[0] = "";
        }

        String lbl = t[0], op = t[1], o1 = t[2], o2 = t[3];
        
        if(!lbl.isEmpty() && !isLit(lbl) && !op.equals("EQU")){
            symTab.put(lbl, LC);
        }

        switch(op){
            case "START":{
                LC = Integer.parseInt(o1);
                icList.add(new IC(line, -1, "(AD,01)", "(C," + o1 + ")", ""));
                break;
            }
            case "END":{
                icList.add(new IC(line, -1, "(AD,02)","", ""));
                break;
            }
            case "ORIGIN": {
                LC = eval(o1);
                icList.add(new IC(line, -1, "(AD,03)", frtOrg(o1), ""));
                break;
            }
            case "EQU": {
                symTab.put(lbl, symTab.get(o1));
                icList.add(new IC(line, -1, "NO IC", "", ""));
                break;
            }
            case "LTORG": {
                ltorg(line);
                break;
            }
            case "DC": {
                icList.add(new IC(line, LC, "(DL,01)", "(C," + o1 + ")", ""));
                LC++;
                break;
            }
            case "DS": {
                icList.add(new IC(line, LC, "(DL,02)", "(C," + o1 + ")", ""));
                LC += Integer.parseInt(o1);
                break;
            }
            default:{
                if(opTab.containsKey(op) && opTab.get(op)[0].equals("IS")){
                    icList.add(new IC(line, LC, "(IS," + opTab.get(op)[1] + ")", oper(o1), oper(o2)));
                    LC++;
                }
                
            }
        }
    }

    private String oper(String o){
        if(o == null || o.isEmpty()){
            return "";
        }
        if(isLit(o)){
            return "(L," + String.format("%02d", addLit(o)) + ")";
        }
        if(regTab.containsKey(o)){
            return "(" + regTab.get(o) +")";
        }
        if(condTab.containsKey(o)){
            return "(" + condTab.get(o) +")";
        }
        if(!symTab.containsKey(o)){
            symTab.put(o, -1);
        }
        return "(S," + String.format("%02d", getSymIdx(o)) + ")";
    }

    private int addLit(String l){
        for(int i=poolStart-1; i< litTab.size(); i++){
            if(litTab.get(i)[0].equals(l)){
                return i+1;
            }
        }
        litTab.add(new String[]{l, "-1"});
        return litCount++;
    }

    private void ltorg(String line){
        icList.add(new IC(line, -1, "", "", ""));
        for(int i=poolStart-1; i<litTab.size(); i++){
            if(litTab.get(i)[1].equals("-1")){
                litTab.get(i)[1] = "" + LC;
                String v = litTab.get(i)[0];
                char p = v.charAt(2);
                icList.add(new IC("", LC, "(DL,01)", "(C,"  + p + ")", ""));
                LC++;
            }
        }
        poolTab.add(poolStart);
        poolStart = litCount;

    }

    private void processRemLit(){
        if(poolStart <= litTab.size()){        
            for(int i=poolStart-1; i<litTab.size(); i++){
                if(litTab.get(i)[1].equals("-1")){
                    litTab.get(i)[1] = "" + LC;
                    String v = litTab.get(i)[0];
                    char p = v.charAt(2);
                    icList.add(new IC("", LC, "(DL,01)", "(C,"  + p + ")", ""));
                    LC++;
                }
            }
            poolTab.add(poolStart);
        }
    }

    private void saveFile() throws Exception{
        saveIC("intermediate_code.txt");
        saveST("symbol_table.txt");
        saveLT("literal_table.txt");
        savePT("pool_table.txt");
    }

    private void saveIC(String f)throws Exception{
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        for(IC ic : icList){
            pw.println((ic.lc == -1 ? "" : ic.lc) + "|" + ic.op + "|" + ic.o1 + "|" + ic.o2);
        }
        pw.close();
    }

    private void saveST(String f)throws Exception{
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        for(Map.Entry<String, Integer> e : symTab.entrySet()){
            pw.println(e.getKey() + "|" + e.getValue());
        }
        pw.close();
    }

    private void saveLT(String f) throws Exception{
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        if(litTab.isEmpty()){
            pw.println("NIL");
        }else{
            for(String[] s : litTab){
                pw.println(s[0] + "|" + s[1]);
            }
        }
        pw.close();
    }

    private void savePT(String f) throws Exception{
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        if(poolTab.isEmpty()){
            pw.println("NIL");
        }else{
            for(int i : poolTab){
                pw.println(i);
            }
        }
        pw.close();
    }

    private void printConsole(){
        System.out.println("\n=== INTERMEDIATE CODE ===\nSOURCE CODE\t\t\tLC\tIC\n" + "=".repeat(50));
        for(IC ic: icList){
            String lcp = ic.lc == -1 ? "" : "" + ic.lc;
            String icp = ic.op + " " + ic.o1 + " " +  ic.o2;
            System.out.printf("%-25s\t%s\t%s%n", ic.src, lcp, icp);
        }
        System.out.println("\n=== SYMBOL TABLE ===\nSymbol\t\tAddress\n" + "=".repeat(25));
        for(Map.Entry<String, Integer> e : symTab.entrySet()){
            System.out.println(e.getKey() + "\t\t" + e.getValue());
        } 

        if(litTab.isEmpty()){
            System.out.println("\n=== LITERAL TABLE AND POOL TABLE ===\nNIL (No literals in source code)\n");
        }else{
            System.out.println("\n=== LITERAL TABLE ===\nLiteral\t\tAddress\n" + "=".repeat(20));
            for(String[] s : litTab){
                System.out.println(s[0] + "\t\t" + s[1]);
            }
            System.out.println("\n=== POOL TABLE ===\nLiteral no.\n" + "=".repeat(15));
            for(int p : poolTab){
                System.out.println("#" + p);
            }
        }
        }

    private int eval(String e){
        if(e.contains("+")){
            String[] p = e.split("\\+");
            return (val(p[0]) + Integer.parseInt(p[1]));
        }
        if(e.contains("-")){
            String[] p = e.split("\\-");
            return (val(p[0]) - Integer.parseInt(p[1]));
        }
        try{
            return Integer.parseInt(e);
        }catch(Exception ex){
            return val(e);
        }
    }

    private int val(String o){
        if(!symTab.containsKey(o)){
            symTab.put(o, 0);
        }
        return symTab.get(o);
    }

    private String frtOrg(String e){
        if(e.contains("+")){
            String[] p = e.split("\\+");
            return "(S," + getSymIdx(p[0]) + ")+" + p[1];
        }
        if(e.contains("-")){
            String[] p = e.split("\\-");
            return "(S," + getSymIdx(p[0]) + ")-" + p[1];
        }
        return "(S," + getSymIdx(e) + ")";   
    }

    private int getSymIdx(String s){
        return new ArrayList<>(symTab.keySet()).indexOf(s) + 1;
    }

    private boolean isLit(String o){
        return o.startsWith("=") && o.endsWith("'");
    }
    public static void main(String args[]){
        if(args.length == 0){
            System.out.println("Usage: java Pass1Assembler <filename>");
            return;
        }
        new Pass1Assembler().processFile(args[0]);
    }


}
