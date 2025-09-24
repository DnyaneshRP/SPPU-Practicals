import java.io.*;
import java.util.*;

public class Pass2Assembler {
    Map<String, Integer> symbols = new HashMap<>();
    Map<String, Integer> literals = new HashMap<>();
    List<String[]> ic = new ArrayList<>();
    List<String[]> mc = new ArrayList<>();
    
    public static void main(String[] args) {
        Pass2Assembler p2 = new Pass2Assembler();
        if (args.length == 4) {
            p2.process(args[0], args[1], args[2], args[3]);
        } else {
            p2.processDefault();
        }
    }
    
    void processDefault() {
        String[] files = {"intermediate_code.txt", "symbol_table.txt", "literal_table.txt", "pool_table.txt"};
        boolean allExist = true;
        for (String f : files) {
            if (!new File(f).exists()) {
                System.err.println("Required file not found: " + f);
                allExist = false;
            }
        }
        if (allExist) process(files[0], files[1], files[2], files[3]);
        else System.out.println("Please ensure all Pass1 output files are present");
    }
    
    void process(String icFile, String symFile, String litFile, String poolFile) {
        try {
            loadSymbols(symFile);
            loadLiterals(litFile);
            loadIC(icFile);
            generateMC();
            printMC();
            saveMC();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    void loadSymbols(String file) throws IOException {
        Scanner sc = new Scanner(new File(file));
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) symbols.put(parts[0], Integer.parseInt(parts[1]));
            }
        }
        sc.close();
    }
    
    void loadLiterals(String file) throws IOException {
        Scanner sc = new Scanner(new File(file));
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (!line.isEmpty() && !line.equals("NIL")) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) literals.put(parts[0], Integer.parseInt(parts[1]));
            }
        }
        sc.close();
    }
    
    void loadIC(String file) throws IOException {
        Scanner sc = new Scanner(new File(file));
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) {
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String[] code = new String[4];
                    code[0] = parts[0].isEmpty() ? "-1" : parts[0]; // LC
                    code[1] = parts[1]; // opcode
                    code[2] = parts.length > 2 ? parts[2] : ""; // operand1
                    code[3] = parts.length > 3 ? parts[3] : ""; // operand2
                    ic.add(code);
                }
            }
        }
        sc.close();
    }
    
    void generateMC() {
        for (String[] code : ic) {
            int lc = Integer.parseInt(code[0]);
            String opcode = code[1];
            
            if (shouldGenerate(lc, opcode)) {
                String[] machine = new String[4];
                machine[0] = code[0]; // LC
                machine[1] = getOpcodeCode(opcode);
                
                // Special case for DC (DL,01)
                if (opcode.equals("(DL,01)")) {
                    machine[2] = "0";
                    machine[3] = getOperandCode(code[2]);
                } else {
                    machine[2] = getOperandCode(code[2]);
                    machine[3] = getOperandCode(code[3]);
                }
                mc.add(machine);
            }
        }
    }
    
    boolean shouldGenerate(int lc, String opcode) {
        return lc != -1 && !opcode.startsWith("(AD,") && !opcode.equals("(DL,02)");
    }
    
    String getOpcodeCode(String opcode) {
        if (opcode.startsWith("(IS,") || opcode.startsWith("(DL,")) {
            String num = opcode.substring(4, opcode.length() - 1);
            return String.format("%02d", Integer.parseInt(num));
        }
        return "00";
    }
    
    String getOperandCode(String operand) {
        if (operand.isEmpty()) return "0";
        
        // Constants (C,value)
        if (operand.startsWith("(C,")) {
            String val = operand.substring(3, operand.length() - 1);
            return String.format("%03d", Integer.parseInt(val));
        }
        
        // Registers (1), (2), etc.
        if (operand.matches("\\(\\d+\\)")) {
            return operand.substring(1, operand.length() - 1);
        }
        
        // Symbols (S,XX)
        if (operand.startsWith("(S,")) {
            String ref = operand.substring(3, operand.length() - 1);
            int idx = Integer.parseInt(ref);
            List<String> keys = new ArrayList<>(symbols.keySet());
            if (idx > 0 && idx <= keys.size()) {
                return String.format("%03d", symbols.get(keys.get(idx - 1)));
            }
        }
        
        // Literals (L,XX)
        if (operand.startsWith("(L,")) {
            String ref = operand.substring(3, operand.length() - 1);
            int idx = Integer.parseInt(ref);
            List<String> keys = new ArrayList<>(literals.keySet());
            if (idx > 0 && idx <= keys.size()) {
                return String.format("%03d", literals.get(keys.get(idx - 1)));
            }
        }
        
        return "0";
    }
    
    void printMC() {
        System.out.println("\n============ MACHINE CODE GENERATION =============");
        System.out.println("LC\tOpcode\t\tOperand1\tOperand2");
        System.out.println("==================================================");
        
        int mcIdx = 0;
        for (String[] code : ic) {
            int lc = Integer.parseInt(code[0]);
            String opcode = code[1];
            
            if (shouldGenerate(lc, opcode)) {
                String[] machine = mc.get(mcIdx++);
                System.out.printf("%s\t%s\t\t%s\t\t%s%n", 
                    machine[0], machine[1], machine[2], machine[3]);
            } else {
                if (lc != -1) {
                    System.out.printf("%d\tNO \t\tMACHINE \tCODE%n", lc);
                } else {
                    System.out.printf("\tNO \t\tMACHINE \tCODE%n");
                }
            }
        }
    }
    
    void saveMC() throws IOException {
        PrintWriter pw = new PrintWriter("machine_code.txt");
        pw.println("LC|Opcode|Operand1|Operand2|Complete");
        for (String[] machine : mc) {
            String complete = machine[1] + " " + machine[2] + " " + machine[3];
            pw.println(machine[0] + "|" + machine[1] + "|" + machine[2] + "|" + 
                      machine[3] + "|" + complete);
        }
        pw.close();
    }
}
