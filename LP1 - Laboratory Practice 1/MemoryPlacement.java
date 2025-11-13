import java.util.Scanner;

public class MemoryPlacement {

    static class Block {
        int size;
        boolean allocated;

        Block(int s) {
            size = s;
            allocated = false;
        }
    }

    static class Process {
        int size;
        int allocatedBlockIndex;

        Process(int s) {
            size = s;
            allocatedBlockIndex = -1;  // -1 means not allocated
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input blocks
        System.out.print("Enter number of memory blocks: ");
        int nb = sc.nextInt();
        int blockSizes[] = new int[nb];
        System.out.println("Enter sizes of each block:");
        for (int i = 0; i < nb; i++) {
            System.out.print("B" + (i + 1) + ": ");
            blockSizes[i] = sc.nextInt();
        }

        // Input processes
        System.out.print("\nEnter number of processes: ");
        int np = sc.nextInt();
        int processSizes[] = new int[np];
        System.out.println("Enter sizes of each process:");
        for (int i = 0; i < np; i++) {
            System.out.print("P" + (i + 1) + ": ");
            processSizes[i] = sc.nextInt();
        }

        System.out.println("\n--- MEMORY PLACEMENT STRATEGIES ---\n");
        runStrategy("First Fit", blockSizes, processSizes);
        runStrategy("Best Fit", blockSizes, processSizes);
        runStrategy("Worst Fit", blockSizes, processSizes);
        runStrategy("Next Fit", blockSizes, processSizes);

        sc.close();
    }

    static void runStrategy(String name, int blockSizes[], int processSizes[]) {
        Block blocks[] = new Block[blockSizes.length];
        Process processes[] = new Process[processSizes.length];

        for (int i = 0; i < blockSizes.length; i++)
            blocks[i] = new Block(blockSizes[i]);

        for (int i = 0; i < processSizes.length; i++)
            processes[i] = new Process(processSizes[i]);

        if (name.equals("First Fit"))
            firstFit(blocks, processes);
        else if (name.equals("Best Fit"))
            bestFit(blocks, processes);
        else if (name.equals("Worst Fit"))
            worstFit(blocks, processes);
        else if (name.equals("Next Fit"))
            nextFit(blocks, processes);

        // Output
        System.out.println("Strategy: " + name);
        System.out.println("+---------+------------+----------------+-------------+");
        System.out.printf("| %-7s | %-10s | %-14s | %-10s |\n",
                "Process", "Size", "Block Allocated", "Block Size");
        System.out.println("+---------+------------+----------------+-------------+");

        for (int i = 0; i < processes.length; i++) {
            String bname = (processes[i].allocatedBlockIndex == -1)
                    ? "Not Allocated"
                    : "B" + (processes[i].allocatedBlockIndex + 1);
            String bsize = (processes[i].allocatedBlockIndex == -1)
                    ? "-"
                    : String.valueOf(blocks[processes[i].allocatedBlockIndex].size);
            System.out.printf("| P%-7d | %-10d | %-14s | %-10s |\n",
                    i + 1, processes[i].size, bname, bsize);
        }
        System.out.println("+---------+------------+----------------+-------------+\n");
    }

    // First Fit
    static void firstFit(Block blocks[], Process processes[]) {
        for (int i = 0; i < processes.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (!blocks[j].allocated && blocks[j].size >= processes[i].size) {
                    processes[i].allocatedBlockIndex = j;
                    blocks[j].allocated = true;
                    break;
                }
            }
        }
    }

    // Best Fit
    static void bestFit(Block blocks[], Process processes[]) {
        for (int i = 0; i < processes.length; i++) {
            int best = -1;
            int bestSize = Integer.MAX_VALUE;
            for (int j = 0; j < blocks.length; j++) {
                if (!blocks[j].allocated && blocks[j].size >= processes[i].size && blocks[j].size < bestSize) {
                    bestSize = blocks[j].size;
                    best = j;
                }
            }
            if (best != -1) {
                processes[i].allocatedBlockIndex = best;
                blocks[best].allocated = true;
            }
        }
    }

    // Worst Fit
    static void worstFit(Block blocks[], Process processes[]) {
        for (int i = 0; i < processes.length; i++) {
            int worst = -1;
            int worstSize = -1;
            for (int j = 0; j < blocks.length; j++) {
                if (!blocks[j].allocated && blocks[j].size >= processes[i].size && blocks[j].size > worstSize) {
                    worstSize = blocks[j].size;
                    worst = j;
                }
            }
            if (worst != -1) {
                processes[i].allocatedBlockIndex = worst;
                blocks[worst].allocated = true;
            }
        }
    }

    // Next Fit
    static void nextFit(Block blocks[], Process processes[]) {
        int start = 0;
        for (int i = 0; i < processes.length; i++) {
            boolean allocated = false;
            int j = start;
            do {
                if (!blocks[j].allocated && blocks[j].size >= processes[i].size) {
                    processes[i].allocatedBlockIndex = j;
                    blocks[j].allocated = true;
                    start = j;
                    allocated = true;
                    break;
                }
                j = (j + 1) % blocks.length;
            } while (j != start);
            if (!allocated)
                processes[i].allocatedBlockIndex = -1;
        }
    }
}