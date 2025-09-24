import java.util.Scanner;

public class MemoryPlacement {

    static class Block {
        int size;
        boolean allocated;

        Block(int size) {
            this.size = size;
            this.allocated = false;
        }
    }

    static class Process {
        int size;
        int allocatedBlockIndex;

        Process(int size) {
            this.size = size;
            this.allocatedBlockIndex = -1;  // -1 means not allocated
        }
    }

    interface AllocationStrategy {
        void allocate(Block[] blocks, Process[] processes);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of blocks
        System.out.print("Enter number of memory blocks: ");
        int blockCount = sc.nextInt();

        int[] blockSizes = new int[blockCount];
        System.out.println("Enter sizes of each block:");
        for (int i = 0; i < blockCount; i++) {
            System.out.printf("B%d: ", i + 1);
            blockSizes[i] = sc.nextInt();
        }

        // Input number of processes
        System.out.print("\nEnter number of processes: ");
        int processCount = sc.nextInt();

        int[] processSizes = new int[processCount];
        System.out.println("Enter sizes of each process:");
        for (int i = 0; i < processCount; i++) {
            System.out.printf("P%d: ", i + 1);
            processSizes[i] = sc.nextInt();
        }

        System.out.println("\nProcesses:");
        printProcesses(processSizes);

        System.out.println("Blocks:");
        printBlocks(blockSizes);

        while (true) {
            System.out.println("\nChoose Memory Placement Strategy:");
            System.out.println("1. First Fit");
            System.out.println("2. Best Fit");
            System.out.println("3. Worst Fit");
            System.out.println("4. Next Fit");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            AllocationStrategy strategy = null;
            String strategyName = "";

            switch (choice) {
                case 1:
                    strategy = MemoryPlacement::firstFit;
                    strategyName = "First Fit";
                    break;
                case 2:
                    strategy = MemoryPlacement::bestFit;
                    strategyName = "Best Fit";
                    break;
                case 3:
                    strategy = MemoryPlacement::worstFit;
                    strategyName = "Worst Fit";
                    break;
                case 4:
                    strategy = MemoryPlacement::nextFit;
                    strategyName = "Next Fit";
                    break;
                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
                    continue;
            }

            runStrategy(strategyName, blockSizes, processSizes, strategy);
        }
    }

    static void printProcesses(int[] processSizes) {
        System.out.println("+----------+------------+");
        System.out.printf("| %-7s  | %-10s |\n", "Process", "Size");
        System.out.println("+----------+------------+");
        for (int i = 0; i < processSizes.length; i++) {
            System.out.printf("| P%-7d | %-10d |\n", i + 1, processSizes[i]);
        }
        System.out.println("+----------+------------+\n");
    }

    static void printBlocks(int[] blockSizes) {
        System.out.println("+--------+------------+");
        System.out.printf("| %-5s  | %-10s |\n", "Block", "Size");
        System.out.println("+--------+------------+");
        for (int i = 0; i < blockSizes.length; i++) {
            System.out.printf("| B%-5d | %-10d |\n", i + 1, blockSizes[i]);
        }
        System.out.println("+--------+------------+\n");
    }

    static void runStrategy(String strategyName, int[] blockSizes, int[] processSizes, AllocationStrategy strategy) {
        // Initialize blocks and processes fresh for each run
        Block[] blocks = new Block[blockSizes.length];
        for (int i = 0; i < blockSizes.length; i++) {
            blocks[i] = new Block(blockSizes[i]);
        }

        Process[] processes = new Process[processSizes.length];
        for (int i = 0; i < processSizes.length; i++) {
            processes[i] = new Process(processSizes[i]);
        }

        strategy.allocate(blocks, processes);

        System.out.println("\nStrategy: " + strategyName);
        System.out.println("+---------+------------+----------------+-------------+");
        System.out.printf("| %-7s  | %-10s| %-14s | %-10s |\n", "Process", "Size", "Block Allocated", "Block Size");
        System.out.println("+---------+------------+----------------+-------------+");

        for (int i = 0; i < processes.length; i++) {
            String blockInfo = (processes[i].allocatedBlockIndex == -1) ? "Not Allocated" : "B" + (processes[i].allocatedBlockIndex + 1);
            String blockSizeInfo = (processes[i].allocatedBlockIndex == -1) ? "-" : String.valueOf(blocks[processes[i].allocatedBlockIndex].size);
            System.out.printf("| P%-7d | %-10d| %-14s | %-10s |\n", i + 1, processes[i].size, blockInfo, blockSizeInfo);
        }
        System.out.println("+---------+------------+----------------+-------------+\n");
    }

    // First Fit allocation
    static void firstFit(Block[] blocks, Process[] processes) {
        for (Process p : processes) {
            for (int i = 0; i < blocks.length; i++) {
                if (!blocks[i].allocated && blocks[i].size >= p.size) {
                    p.allocatedBlockIndex = i;
                    blocks[i].allocated = true;
                    break;
                }
            }
        }
    }

    // Best Fit allocation
    static void bestFit(Block[] blocks, Process[] processes) {
        for (Process p : processes) {
            int bestIdx = -1;
            int bestSize = Integer.MAX_VALUE;
            for (int i = 0; i < blocks.length; i++) {
                if (!blocks[i].allocated && blocks[i].size >= p.size && blocks[i].size < bestSize) {
                    bestSize = blocks[i].size;
                    bestIdx = i;
                }
            }
            if (bestIdx != -1) {
                p.allocatedBlockIndex = bestIdx;
                blocks[bestIdx].allocated = true;
            }
        }
    }

    // Worst Fit allocation
    static void worstFit(Block[] blocks, Process[] processes) {
        for (Process p : processes) {
            int worstIdx = -1;
            int worstSize = -1;
            for (int i = 0; i < blocks.length; i++) {
                if (!blocks[i].allocated && blocks[i].size >= p.size && blocks[i].size > worstSize) {
                    worstSize = blocks[i].size;
                    worstIdx = i;
                }
            }
            if (worstIdx != -1) {
                p.allocatedBlockIndex = worstIdx;
                blocks[worstIdx].allocated = true;
            }
        }
    }

    // Next Fit allocation
    static void nextFit(Block[] blocks, Process[] processes) {
        int startIndex = 0; // track where last allocation ended
        for (Process p : processes) {
            boolean allocated = false;
            int i = startIndex;
            do {
                if (!blocks[i].allocated && blocks[i].size >= p.size) {
                    p.allocatedBlockIndex = i;
                    blocks[i].allocated = true;
                    startIndex = i; // update startIndex to current allocation
                    allocated = true;
                    break;
                }
                i = (i + 1) % blocks.length;
            } while (i != startIndex);
            if (!allocated) {
                p.allocatedBlockIndex = -1;
            }
        }
    }
}
