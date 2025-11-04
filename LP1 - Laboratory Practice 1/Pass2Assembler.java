import java.io.*;
import java.util.*;

public class Pass2Assembler{
    Map<String, Integer> symbols = new LinkedHashMap<>();
    List<String[]> literals = new ArrayList<>();
    List<String[]> ic = new ArrayList<>();
    List<String[]> mc = new ArrayList<>();

    public static void main(String args[]){
        new Pass2Assembler().processDefault();
    }

    void processDefault(){
        String[] files = {"intermediate_code.txt", "symbol_table.txt", "literal_table.txt"};
        boolean allExists = true;
        for(String f : files){
            if(!new File(f).exists()){
                System.err.println("File not found: " + f);
                allExists = false;
            }
        }
        if(allExists)
            process(files[0], files[1], files[2]);
        else
            System.out.println("Error");
    }

    void process(String icFile, String symFile, String litFile){
        try{
            loadIC(icFile);
            loadSymbols(symFile);
            loadLiterals(litFile);
            generateMC();
            printMC();
            saveMC();

        }catch(Exception e){
            System.err.println("Error: " + e);
        }
    }

    void loadIC(String file)throws Exception{
        Scanner sc = new Scanner(new File(file));
        while(sc.hasNextLine()){
            String line = sc.nextLine().trim();
            if(!line.isEmpty()){
                String[] p = line.split("\\|");
                if(p.length >= 2){
                    String[] code = new String[4];
                    code[0] = p[0].isEmpty() ? "-1" : p[0];
                    code[1] = p[1];
                    code[2] = p.length > 2 ? p[2] : "";
                    code[3] = p.length > 3 ? p[3] : "";
                    ic.add(code);        
                }
            }
        }
        sc.close();
    }

    void loadSymbols(String file)throws Exception{
        Scanner sc = new Scanner(new File(file));
        while(sc.hasNextLine()){
            String line = sc.nextLine().trim();
            if(!line.isEmpty()){
                String[] p = line.split("\\|");
                if(p.length == 2){
                    symbols.put(p[0], Integer.parseInt(p[1]));
                }
            }
        }
        sc.close();
    }

    void loadLiterals(String file)throws Exception{
        Scanner sc = new Scanner(new File(file));
        while(sc.hasNextLine()){
            String line = sc.nextLine().trim();
            if(!line.isEmpty() && !line.equals("NIL")){
                String[] p = line.split("\\|");
                if(p.length == 2){
                    literals.add(new String[]{p[0], p[1]});
                }
            }
        }
        sc.close();
    }

    void generateMC(){
        for(String[] code : ic){
            int lc = Integer.parseInt(code[0]);
            String opcode = code[1];

            if(shouldGenerate(lc, opcode)){
                String[] machine = new String[4];
                machine[0] = code[0];
                machine[1] = getOpcodeCode(opcode);

                if(opcode.equals("(DL,01)")){
                    machine[2] = "0";
                    machine[3] = getOperandCode(code[2]);
                }else {
                    machine[2] = getOperandCode(code[2]);
                    machine[3] = getOperandCode(code[3]);
                }
                mc.add(machine);
            }
        }
    }

    boolean shouldGenerate(int lc, String opcode){
        return lc != -1 && !opcode.startsWith("(AD,") && !opcode.equals("(DL,02)");
    }

    String getOpcodeCode(String opcode){
        if(opcode.startsWith("(IS,")){
            String num = opcode.substring(4, opcode.length()-1);
            return  String.format("%02d", Integer.parseInt(num));
        }
        return "00";
    }

    String getOperandCode(String operand){
        if(operand.isEmpty()){
            return "0";
        }
        if(operand.startsWith("(C,")){
            String val = operand.substring(3, operand.length()-1);
            return String.format("%03d", Integer.parseInt(val));
        }
        if(operand.matches("\\(\\d+\\)")){
            return operand.substring(1, operand.length()-1);
        }
        if(operand.startsWith("(S,")){
            String ref = operand.substring(3, operand.length()-1);
            int idx = Integer.parseInt(ref);
            List<String> keys = new ArrayList<>(symbols.keySet());
            if(idx > 0 && idx <= keys.size()){
                return String.format("%03d", symbols.get(keys.get(idx-1)));
            }
        }
        if(operand.startsWith("(L,")){
            String ref = operand.substring(3, operand.length()-1);
            int idx = Integer.parseInt(ref);
            if(idx > 0 && idx <= literals.size()){
                return String.format("%03d", Integer.parseInt(literals.get(idx-1)[1]));
            }
        }
        return "0";
    }

    void printMC(){
        System.out.println("\n========== MACHINE CODE GENERATION ==========");
        System.out.println("LC\tOpcode\t\tOperand1\tOperand2");
        System.out.println("=".repeat(30));

        int mcIdx = 0;
        for(String[] code : ic){
            int lc = Integer.parseInt(code[0]);
            String opcode = code[1];

            if(shouldGenerate(lc, opcode)){
                String[] machine = mc.get(mcIdx++);
                System.out.printf("%s\t%s\t\t%s\t\t%s%n", machine[0], machine[1], machine[2], machine[3]);
            }else{
                if(lc != -1){
                    System.out.printf("%s\tNO \t\tMACHINE \tCODE%n", code[0]);
                }else{
                    System.out.printf("\tNO \t\tMACHINE \tCODE%n");
                }
            }
        }
    }


    void saveMC()throws Exception{
        PrintWriter pw = new PrintWriter("machine_code.txt");
        for(String[] machine : mc){
            String complete = machine[1] + " " + machine[2] + " " + machine[3];
            pw.println(complete);
        }
        pw.close();
    }
}