import java.io.*;
import java.util.*;

public class Pass1Assembler {
    // Core data structures - easy to remember pattern
    Map<String, String[]> opTab = new HashMap<>();
    Map<String, Integer> regTab = Map.of("AREG",1, "BREG",2, "CREG",3, "DREG",4);
    Map<String, Integer> condTab = Map.of("LT",1, "LE",2, "EQ",3, "GT",4, "GE",5, "ANY",6);
    LinkedHashMap<String, Integer> symTab = new LinkedHashMap<>();
    List<String[]> litTab = new ArrayList<>();
    List<Integer> poolTab = new ArrayList<>();
    List<String> icList = new ArrayList<>();
    
    int lc = 0, litIndex = 1, poolStart = 1;
    
    public Pass1Assembler() {
        // Initialize opcodes - remember: IS, AD, DL pattern
        String[][] opcodes = {
            {"STOP","00","IS"}, {"ADD","01","IS"}, {"SUB","02","IS"}, {"MULT","03","IS"},
            {"MOVER","04","IS"}, {"MOVEM","05","IS"}, {"COMP","06","IS"}, {"BC","07","IS"},
            {"DIV","08","IS"}, {"READ","09","IS"}, {"PRINT","10","IS"},
            {"START","01","AD"}, {"END","02","AD"}, {"ORIGIN","03","AD"}, {"EQU","04","AD"}, {"LTORG","05","AD"},
            {"DC","01","DL"}, {"DS","02","DL"}
        };
        for(String[] op : opcodes) opTab.put(op[0], new String[]{op[1], op[2]});
    }
    
    public void process(String filename) {
        try(Scanner sc = new Scanner(new File(filename))) {
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if(!line.isEmpty() && !line.startsWith(";")) processLine(line);
            }
            handleRemainingLiterals();
            printOutput();
        } catch(Exception e) { System.out.println("Error: " + e.getMessage()); }
    }
    
    void processLine(String line) {
        String[] parts = line.split("\\s+");
        String label = "", opcode = "", op1 = "", op2 = "";
        
        // Parse tokens - remember: if first token is opcode, no label
        if(opTab.containsKey(parts[0])) {
            opcode = parts[0];
            if(parts.length > 1) op1 = parts[1];
            if(parts.length > 2) op2 = parts[2];
        } else {
            label = parts[0];
            if(parts.length > 1) opcode = parts[1];
            if(parts.length > 2) op1 = parts[2];
            if(parts.length > 3) op2 = parts[3];
        }
        
        // Add label to symbol table (except for EQU and literals)
        if(!label.isEmpty() && !opcode.equals("EQU") && !isLiteral(label)) {
            symTab.put(label, lc);
        }
        
        // Process by opcode - remember the main cases
        switch(opcode) {
            case "START":
                lc = Integer.parseInt(op1);
                addIC(line, -1, "(AD,01) (C," + op1 + ")");
                break;
            case "END":
                String endOp = op1.isEmpty() ? "" : processOperand(op1);
                addIC(line, -1, "(AD,02) " + endOp);
                break;
            case "ORIGIN":
                lc = evaluateExpr(op1);
                addIC(line, -1, "(AD,03) " + formatOrigin(op1));
                break;
            case "EQU":
                if(!label.isEmpty()) symTab.put(label, evaluateExpr(op1));
                addIC(line, -1, "No IC generated");
                break;
            case "LTORG":
                processLTORG(line);
                break;
            case "DC":
                addIC(line, lc, "(DL,01) (C," + op1.replace("'","") + ")");
                lc++;
                break;
            case "DS":
                int size = Integer.parseInt(op1);
                addIC(line, lc, "(DL,02) (C," + size + ")");
                lc += size;
                break;
            default:
                if(opTab.containsKey(opcode) && opTab.get(opcode)[1].equals("IS")) {
                    String ic = "(" + opTab.get(opcode)[1] + "," + opTab.get(opcode)[0] + ")";
                    ic += " " + processOperand(op1) + " " + processOperand(op2);
                    addIC(line, lc, ic.trim());
                    lc++;
                }
        }
    }
    
    String processOperand(String op) {
        if(op.isEmpty()) return "";
        if(isLiteral(op)) return "(L," + String.format("%02d", addLiteral(op)) + ")";
        if(regTab.containsKey(op)) return "(" + regTab.get(op) + ")";
        if(condTab.containsKey(op)) return "(" + condTab.get(op) + ")";
        
        // Symbol - add to symtab if not present
        if(!symTab.containsKey(op)) symTab.put(op, -1);
        return "(S," + String.format("%02d", getSymIndex(op)) + ")";
    }
    
    boolean isLiteral(String s) {
        return s != null && s.startsWith("='") && s.endsWith("'");
    }
    
    int addLiteral(String lit) {
        // Check if literal exists in current pool
        for(int i = poolStart-1; i < litTab.size(); i++) {
            if(litTab.get(i)[0].equals(lit)) return i + 1;
        }
        litTab.add(new String[]{lit, "-1"});
        return litIndex++;
    }
    
    void processLTORG(String line) {
        addIC(line, -1, "");
        // Assign addresses to pending literals
        for(int i = poolStart-1; i < litTab.size(); i++) {
            if(litTab.get(i)[1].equals("-1")) {
                litTab.get(i)[1] = String.valueOf(lc);
                String val = litTab.get(i)[0].substring(2, litTab.get(i)[0].length()-1);
                addIC("", lc, "(DL,01) (C," + val + ")");
                lc++;
            }
        }
        poolTab.add(poolStart);
        poolStart = litIndex;
    }
    
    void handleRemainingLiterals() {
        if(poolStart <= litTab.size()) {
            for(int i = poolStart-1; i < litTab.size(); i++) {
                if(litTab.get(i)[1].equals("-1")) {
                    litTab.get(i)[1] = String.valueOf(lc);
                    String val = litTab.get(i)[0].substring(2, litTab.get(i)[0].length()-1);
                    addIC("", lc, "(DL,01) (C," + val + ")");
                    lc++;
                }
            }
            if(!litTab.isEmpty()) poolTab.add(litTab.size());
        }
    }
    
    int getSymIndex(String sym) {
        List<String> syms = new ArrayList<>();
        for(String s : symTab.keySet()) if(!isLiteral(s)) syms.add(s);
        return syms.indexOf(sym) + 1;
    }
    
    int evaluateExpr(String expr) {
        if(expr.contains("+")) {
            String[] parts = expr.split("\\+");
            return getSymValue(parts[0].trim()) + Integer.parseInt(parts[1].trim());
        }
        if(expr.contains("-")) {
            String[] parts = expr.split("-");
            return getSymValue(parts[0].trim()) - Integer.parseInt(parts[1].trim());
        }
        try { return Integer.parseInt(expr); }
        catch(Exception e) { return getSymValue(expr); }
    }
    
    int getSymValue(String sym) {
        if(!symTab.containsKey(sym)) symTab.put(sym, 0);
        return symTab.get(sym);
    }
    
    String formatOrigin(String op) {
        if(op.contains("+")) {
            String[] parts = op.split("\\+");
            return "(S," + String.format("%02d", getSymIndex(parts[0].trim())) + ")+" + parts[1].trim();
        }
        if(op.contains("-")) {
            String[] parts = op.split("-");
            return "(S," + String.format("%02d", getSymIndex(parts[0].trim())) + ")-" + parts[1].trim();
        }
        return "(S," + String.format("%02d", getSymIndex(op)) + ")";
    }
    
    void addIC(String source, int loc, String ic) {
        String locStr = (loc == -1) ? "" : String.valueOf(loc);
        icList.add(source + "|" + locStr + "|" + ic);
    }
    
    void printOutput() {
        // Print Intermediate Code
        System.out.println("\n=== INTERMEDIATE CODE ===");
        System.out.println("Source\t\tLC\tIC");
        for(String ic : icList) {
            String[] parts = ic.split("\\|", 3);
            System.out.printf("%-15s\t%s\t%s%n", parts[0], parts[1], parts[2]);
        }
        
        // Print Symbol Table
        System.out.println("\n=== SYMBOL TABLE ===");
        System.out.println("Symbol\tAddress");
        for(Map.Entry<String, Integer> e : symTab.entrySet()) {
            if(!isLiteral(e.getKey())) {
                System.out.println(e.getKey() + "\t" + e.getValue());
            }
        }
        
        // Print Literal Table
        if(litTab.isEmpty()) {
            System.out.println("\n=== LITERAL TABLE ===\nNIL");
        } else {
            System.out.println("\n=== LITERAL TABLE ===");
            System.out.println("Literal\tAddress");
            for(String[] lit : litTab) System.out.println(lit[0] + "\t" + lit[1]);
        }
        
        // Print Pool Table
        if(poolTab.isEmpty()) {
            System.out.println("\n=== POOL TABLE ===\nNIL");
        } else {
            System.out.println("\n=== POOL TABLE ===");
            for(int pool : poolTab) System.out.println("#" + pool);
        }
    }
    
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: java Pass1Assembler <file>");
            return;
        }
        new Pass1Assembler().process(args[0]);
    }
}