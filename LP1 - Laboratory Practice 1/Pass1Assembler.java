import java.io.*;
import java.util.*;

public class Pass1Assembler {
    private final Map<String, String[]> opTab = new HashMap<>();
    private final Map<String, Integer> regTab = Map.of("AREG", 1, "BREG", 2, "CREG", 3, "DREG", 4);
    private final Map<String, Integer> condTab = Map.of("LT", 1, "LE", 2, "EQ", 3, "GT", 4, "GE", 5, "ANY", 6);
    private final LinkedHashMap<String, Integer> symTab = new LinkedHashMap<>();
    private final ArrayList<String[]> litTab = new ArrayList<>();
    private final ArrayList<Integer> poolTab = new ArrayList<>();
    private final ArrayList<IC> icList = new ArrayList<>();
    private int LC = 0, litCount = 1, poolStart = 1;

    static class IC {
        String src;
        int lc;
        String op, o1, o2;

        IC(String s, int l, String o, String a, String b) {
            src = s;
            lc = l;
            op = o;
            o1 = a;
            o2 = b;
        }
    }

    public Pass1Assembler() {
        String[][] is = { { "STOP", "00" }, { "ADD", "01" }, { "SUB", "02" }, { "MULT", "03" }, { "MOVER", "04" },
                { "MOVEM", "05" }, { "COMP", "06" }, { "BC", "07" }, { "DIV", "08" }, { "READ", "09" },
                { "PRINT", "10" } };
        for (String[] x : is)
            opTab.put(x[0], new String[] { "IS", x[1] });
        String[][] ad = { { "START", "01" }, { "END", "02" }, { "ORIGIN", "03" }, { "EQU", "04" }, { "LTORG", "05" } };
        for (String[] x : ad)
            opTab.put(x[0], new String[] { "AD", x[1] });
        opTab.put("DC", new String[] { "DL", "01" });
        opTab.put("DS", new String[] { "DL", "02" });
    }

    public void processFile(String f) {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith(";"))
                    process(line);
            }
            processRemLit();
            saveFiles();
            printConsole();
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }

    private void process(String line) {
        String[] t = { "", "", "", "" };
        String[] p = line.split("\\s+");
        System.arraycopy(p, 0, t, 0, Math.min(p.length, 4));
        if (opTab.containsKey(t[0])) {
            for (int i = 3; i > 0; i--)
                t[i] = t[i - 1];
            t[0] = "";
        }
        String lbl = t[0], op = t[1], o1 = t[2], o2 = t[3];
        if (!lbl.isEmpty() && !op.equals("EQU") && !isLit(lbl))
            symTab.put(lbl, LC);

        switch (op) {
            case "START":
                LC = Integer.parseInt(o1);
                icList.add(new IC(line, -1, "(AD,01)", "(C," + o1 + ")", ""));
                break;
            case "END":
                icList.add(new IC(line, -1, "(AD,02)", operand(o1), ""));
                break;
            case "ORIGIN":
                LC = eval(o1);
                icList.add(new IC(line, -1, "(AD,03)", fmtOrg(o1), ""));
                break;
            case "EQU":
                if (!isLit(lbl))
                    symTab.put(lbl, eval(o1));
                icList.add(new IC(line, -1, "No IC, Reflect in SYMTAB", "", ""));
                break;
            case "LTORG":
                ltorg(line);
                break;
            case "DC":
                icList.add(new IC(line, LC, "(DL,01)", "(C," + o1.replace("'", "") + ")", ""));
                LC++;
                break;
            case "DS":
                icList.add(new IC(line, LC, "(DL,02)", "(C," + o1 + ")", ""));
                LC += Integer.parseInt(o1);
                break;
            default:
                if (opTab.containsKey(op) && opTab.get(op)[0].equals("IS")) {
                    icList.add(new IC(line, LC, "(IS," + opTab.get(op)[1] + ")", operand(o1), operand(o2)));
                    LC++;
                }
        }
    }

    private String operand(String o) {
        if (o == null || o.isEmpty())
            return "";
        if (isLit(o))
            return "(L," + String.format("%02d", addLit(o)) + ")";
        if (regTab.containsKey(o))
            return "(" + regTab.get(o) + ")";
        if (condTab.containsKey(o))
            return "(" + condTab.get(o) + ")";
        if (!symTab.containsKey(o))
            symTab.put(o, -1);
        return "(S," + String.format("%02d", symIdx(o)) + ")";
    }

    private boolean isLit(String s) {
        return s.startsWith("='") && s.endsWith("'");
    }

    private int addLit(String l) {
        for (int i = poolStart - 1; i < litTab.size(); i++)
            if (litTab.get(i)[0].equals(l))
                return i + 1;
        litTab.add(new String[] { l, "-1" });
        return litCount++;
    }

    private void ltorg(String line) {
        icList.add(new IC(line, -1, "", "", ""));
        for (int i = poolStart - 1; i < litTab.size(); i++)
            if (litTab.get(i)[1].equals("-1")) {
                litTab.get(i)[1] = "" + LC;
                String v = litTab.get(i)[0].substring(2, litTab.get(i)[0].length() - 1);
                icList.add(new IC("", LC, "(DL,01)", "(C," + v + ")", ""));
                LC++;
            }
        poolTab.add(poolStart);
        poolStart = litCount;
    }

    private void processRemLit() {
        if (poolStart <= litTab.size()) {
            for (int i = poolStart - 1; i < litTab.size(); i++)
                if (litTab.get(i)[1].equals("-1")) {
                    litTab.get(i)[1] = "" + LC;
                    String v = litTab.get(i)[0].substring(2, litTab.get(i)[0].length() - 1);
                    icList.add(new IC("", LC, "(DL,01)", "(C," + v + ")", ""));
                    LC++;
                }
            poolTab.add(poolStart);
        }
    }

    private int symIdx(String s) {
        return new ArrayList<>(symTab.keySet()).indexOf(s) + 1;
    }

    private int eval(String e) {
        if (e.contains("+")) {
            String[] p = e.split("\\+");
            return val(p[0]) + Integer.parseInt(p[1]);
        }
        if (e.contains("-")) {
            String[] p = e.split("-");
            return val(p[0]) - Integer.parseInt(p[1]);
        }
        try {
            return Integer.parseInt(e);
        } catch (Exception ex) {
            return val(e);
        }
    }

    private int val(String s) {
        if (!symTab.containsKey(s))
            symTab.put(s, 0);
        return symTab.get(s);
    }

    private String fmtOrg(String e) {
        if (e.contains("+")) {
            String[] p = e.split("\\+");
            return "(S," + symIdx(p[0]) + ")+" + p[1];
        }
        if (e.contains("-")) {
            String[] p = e.split("-");
            return "(S," + symIdx(p[0]) + ")-" + p[1];
        }
        return "(S," + symIdx(e) + ")";
    }

    private void saveFiles() throws Exception {
        saveIC("intermediate_code.txt");
        saveSym("symbol_table.txt");
        saveLit("literal_table.txt");
        savePool("pool_table.txt");
    }

    private void saveIC(String f) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        for (IC ic : icList)
            pw.println((ic.lc == -1 ? "" : ic.lc) + "|" + ic.op + "|" + ic.o1 + "|" + ic.o2);
        pw.close();
    }

    private void saveSym(String f) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        for (Map.Entry<String, Integer> e : symTab.entrySet())
            if (!isLit(e.getKey()))
                pw.println(e.getKey() + "|" + e.getValue());
        pw.close();
    }

    private void saveLit(String f) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        if (litTab.isEmpty())
            pw.println("NIL");
        else
            for (String[] l : litTab)
                pw.println(l[0] + "|" + l[1]);
        pw.close();
    }

    private void savePool(String f) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(f));
        if (poolTab.isEmpty())
            pw.println("NIL");
        else
            for (int p : poolTab)
                pw.println(p);
        pw.close();
    }

    private void printConsole() {
        System.out.println("\n=== INTERMEDIATE CODE ===\nSource Code\t\t\tLC\tIC\n" + "=".repeat(60));
        for (IC ic : icList) {
            String lcStr = ic.lc == -1 ? "" : "" + ic.lc;
            String icStr = ic.op + (!ic.o1.isEmpty() ? " " + ic.o1 : "") + (!ic.o2.isEmpty() ? " " + ic.o2 : "");
            System.out.printf("%-25s\t%s\t%s%n", ic.src, lcStr, icStr);
        }
        System.out.println("\n=== SYMBOL TABLE ===\nSymbol\t\tAddress\n" + "=".repeat(20));
        for (Map.Entry<String, Integer> e : symTab.entrySet())
            if (!isLit(e.getKey()))
                System.out.println(e.getKey() + "\t\t" + e.getValue());
        if (litTab.isEmpty())
            System.out.println("\n=== LITERAL TABLE & POOL TABLE ===\nNIL (No literals in source code)");
        else {
            System.out.println("\n=== LITERAL TABLE ===\nLiteral\t\tAddress\n" + "=".repeat(20));
            for (String[] l : litTab)
                System.out.println(l[0] + "\t\t" + l[1]);
            System.out.println("\n=== POOL TABLE ===\nLiteral No.\n" + "=".repeat(15));
            for (int p : poolTab)
                System.out.println("#" + p);
        }
    }

    public static void main(String[] a) {
        if (a.length != 1) {
            System.out.println("Usage: java Pass1Assembler <file>");
            return;
        }
        new Pass1Assembler().processFile(a[0]);
    }
}