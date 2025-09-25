import java.io.*;
import java.util.*;

class Pass2MacroProcessor {

    // Data Structures
    static class MNTEntry {
        String name;
        int pp, kp, mdtp, kpdtp;
        MNTEntry(String name, int pp, int kp, int mdtp, int kpdtp) {
            this.name = name; this.pp = pp; this.kp = kp; this.mdtp = mdtp; this.kpdtp = kpdtp;
        }
    }

    static class KPDTABEntry {
        String param, def;
        KPDTABEntry(String param, String def) {
            this.param = param; this.def = def;
        }
    }

    static class APTABEntry {
        String param, value;
        APTABEntry(String param, String value) {
            this.param = param; this.value = value;
        }
        public String toString() {
            return String.format("%-8s %-10s", param, value);
        }
    }

    static List<MNTEntry> MNT = new ArrayList<>();
    static List<KPDTABEntry> KPDTAB = new ArrayList<>();
    static List<String> MDT = new ArrayList<>();
    static Map<String, List<String>> PNTABs = new LinkedHashMap<>();
    static List<String> intermediate = new ArrayList<>();
    static List<String> output = new ArrayList<>();
    static List<APTABEntry> APTAB = new ArrayList<>();
    static List<String> aptabDisplays = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // Load data from Pass1 output files
        loadTables();
        
        // Process Pass2
        pass2();
        
        // Display results
        printOutput();
        
        // Save output file
        saveOutputFile();
    }

    static void loadTables() throws IOException {
        // Load MNT
        try (BufferedReader br = new BufferedReader(new FileReader("MNT.txt"))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 6) {
                    MNT.add(new MNTEntry(parts[1], Integer.parseInt(parts[2]), 
                                       Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), 
                                       Integer.parseInt(parts[5])));
                }
            }
        }

        // Load KPDTAB
        try (BufferedReader br = new BufferedReader(new FileReader("KPDTAB.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+", 3);
                if (parts.length >= 3) {
                    KPDTAB.add(new KPDTABEntry(parts[1], parts[2]));
                }
            }
        }

        // Load MDT
        try (BufferedReader br = new BufferedReader(new FileReader("MDT.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.trim().split("\\s+", 2);
                if (parts.length >= 2) {
                    MDT.add(parts[1]);
                }
            }
        }

        // Load PNTAB for each macro
        for (MNTEntry mnt : MNT) {
            String filename = "PNTAB_" + mnt.name + ".txt";
            List<String> pntab = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.trim().split("\\s+", 2);
                    if (parts.length >= 2) {
                        pntab.add("&" + parts[1]); // Add & prefix back
                    }
                }
            }
            PNTABs.put(mnt.name, pntab);
        }

        // Load Intermediate Code
        try (BufferedReader br = new BufferedReader(new FileReader("Intermediate.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                intermediate.add(line);
            }
        }
    }

    static void pass2() {
        for (String line : intermediate) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Check if this line is a macro call
            String[] parts = line.split("\\s+", 2);
            String opcode = parts[0];
            
            MNTEntry macroEntry = findMacro(opcode);
            if (macroEntry != null) {
                // This is a macro call - expand it
                expandMacro(macroEntry, parts.length > 1 ? parts[1] : "");
            } else {
                // Regular instruction - add to output as is
                output.add(line);
            }
        }
    }

    static MNTEntry findMacro(String name) {
        for (MNTEntry entry : MNT) {
            if (entry.name.equals(name)) {
                return entry;
            }
        }
        return null;
    }

    static void expandMacro(MNTEntry macroEntry, String actualParams) {
        // Save current APTAB (for nested macro calls)
        List<APTABEntry> savedAPTAB = new ArrayList<>(APTAB);
        
        // Clear APTAB for new macro call
        APTAB.clear();
        
        // Get PNTAB for this macro
        List<String> pntab = PNTABs.get(macroEntry.name);
        
        // Parse actual parameters
        List<String> actualParamList = new ArrayList<>();
        if (!actualParams.isEmpty()) {
            for (String param : actualParams.split(",")) {
                actualParamList.add(param.trim());
            }
        }

        // Build APTAB - handle positional parameters first
        for (int i = 0; i < macroEntry.pp && i < actualParamList.size(); i++) {
            String param = actualParamList.get(i);
            // Skip keyword parameters in positional section
            if (!param.contains("=")) {
                APTAB.add(new APTABEntry(pntab.get(i), param));
            }
        }

        // Handle keyword parameters - first set defaults from KPDTAB
        for (int i = macroEntry.pp; i < pntab.size(); i++) {
            String param = pntab.get(i);
            int kpdtIndex = macroEntry.kpdtp - 1 + (i - macroEntry.pp);
            String defaultValue = "-";
            if (kpdtIndex < KPDTAB.size()) {
                defaultValue = KPDTAB.get(kpdtIndex).def;
            }
            APTAB.add(new APTABEntry(param, defaultValue));
        }

        // Override with actual keyword parameters from anywhere in the parameter list
        for (String param : actualParamList) {
            if (param.contains("=")) {
                String[] kv = param.split("=", 2);
                String key = "&" + kv[0].trim();
                String value = kv.length > 1 ? kv[1].trim() : "-";
                
                // Find and update in APTAB
                for (APTABEntry entry : APTAB) {
                    if (entry.param.equals(key)) {
                        entry.value = value;
                        break;
                    }
                }
            }
        }

        // Store APTAB display for this macro call
        StringBuilder aptabDisplay = new StringBuilder();
        aptabDisplay.append("\n====== APTAB (").append(macroEntry.name).append(") =======\n");
        aptabDisplay.append(String.format("%-5s %-10s\n", "Idx", "Value"));
        aptabDisplay.append("------------\n");
        for (int i = 0; i < APTAB.size(); i++) {
            aptabDisplay.append(String.format("%-5d %-10s\n", i+1, APTAB.get(i).value));
        }
        aptabDisplays.add(aptabDisplay.toString());

        // Save current APTAB and PNTAB for parameter substitution
        List<APTABEntry> currentAPTAB = new ArrayList<>(APTAB);
        List<String> currentPNTAB = new ArrayList<>(pntab);

        // Expand macro body
        int mdtIndex = macroEntry.mdtp - 1;
        while (mdtIndex < MDT.size()) {
            String mdtLine = MDT.get(mdtIndex);
            if (mdtLine.equals("MEND")) {
                break;
            }

            // Replace parameters with actual values
            String expandedLine = mdtLine;
            for (APTABEntry entry : currentAPTAB) {
                String paramPattern = "\\(P," + (getParameterIndex(currentPNTAB, entry.param) + 1) + "\\)";
                expandedLine = expandedLine.replaceAll(paramPattern, entry.value);
            }

            // Check if the expanded line is a macro call (nested macro)
            String[] parts = expandedLine.split("\\s+", 2);
            String opcode = parts[0];
            MNTEntry nestedMacro = findMacro(opcode);
            
            if (nestedMacro != null) {
                // This is a nested macro call - expand it recursively
                expandMacro(nestedMacro, parts.length > 1 ? parts[1] : "");
            } else {
                // Regular instruction - add to output
                output.add("+" + expandedLine);
            }
            
            mdtIndex++;
        }

        // Restore parent macro's APTAB (for nested macro calls)
        APTAB = savedAPTAB;
    }

    static int getParameterIndex(List<String> pntab, String param) {
        for (int i = 0; i < pntab.size(); i++) {
            if (pntab.get(i).equals(param)) {
                return i;
            }
        }
        return -1;
    }

    static void printOutput() {
        // Display APTAB for each macro call
        for (String aptabDisplay : aptabDisplays) {
            System.out.print(aptabDisplay);
        }

        System.out.println("\n======= OUTPUT.ASM =======\n");
        for (String line : output) {
            System.out.println(line);
        }
    }

    static void saveOutputFile() throws IOException {
        try (PrintWriter pw = new PrintWriter("output.asm")) {
            for (String line : output) {
                // Remove the '+' prefix when saving to file
                if (line.startsWith("+")) {
                    pw.println(line.substring(1));
                } else {
                    pw.println(line);
                }
            }
        }
        System.out.println("\n✅ Output saved to output.asm");
    }
}
