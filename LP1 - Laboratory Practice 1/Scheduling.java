import java.util.*;

class Process {
    int ID, AT, BT, CT, TAT, WT, RT, priority;
    Process(int id, int at, int bt, int pr) {
        ID = id;
        AT = at;
        BT = bt;
        RT = bt;
        priority = pr;
    }
}

public class Scheduling{
    static Scanner sc = new Scanner(System.in);
    static int fcfsWT, fcfsTAT, sjfWT, sjfTAT, prioWT, prioTAT, rrWT, rrTAT;

    public static void main(String[] args) {
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] fcfsArr = new Process[n];
        Process[] sjfArr = new Process[n];
        Process[] prioArr = new Process[n];
        Process[] rrArr = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details for process P" + (i + 1));
            System.out.print("Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Burst Time  : ");
            int bt = sc.nextInt();
            System.out.print("Priority    : ");
            int pr = sc.nextInt();
            fcfsArr[i] = new Process(i + 1, at, bt, pr);
            sjfArr[i] = new Process(i + 1, at, bt, pr);
            prioArr[i] = new Process(i + 1, at, bt, pr);
            rrArr[i] = new Process(i + 1, at, bt, pr);
        }

        System.out.print("\nEnter Time Quantum for Round Robin: ");
        int tq = sc.nextInt();
        System.out.println("\n");
        System.out.println("\n=============== FCFS Scheduling =================");
        fcfs(fcfsArr);

        System.out.println("\n=============== SJF Scheduling ==================");
        sjf(sjfArr);

        System.out.println("\n============= Priority Scheduling ===============");
        priority(prioArr);

        System.out.println("\n============ Round Robin Scheduling =============");
        roundRobin(rrArr, tq);

        System.out.println("\n======== Scheduling Algorithm Comparison ========\n");
        System.out.println("+-----------------------------------------------+");
        System.out.println("| Algorithm\t| Avg WT\t| Avg TAT \t|");
        System.out.println("+-----------------------------------------------+");
        System.out.printf("| FCFS     \t| %.2f\t\t| %.2f\t\t|\n", (float) fcfsWT / n, (float) fcfsTAT / n);
        System.out.printf("| SJF      \t| %.2f\t\t| %.2f\t\t|\n", (float) sjfWT / n, (float) sjfTAT / n);
        System.out.printf("| Priority \t| %.2f\t\t| %.2f\t\t|\n", (float) prioWT / n, (float) prioTAT / n);
        System.out.printf("| RoundRobin\t| %.2f\t\t| %.2f\t\t|\n", (float) rrWT / n, (float) rrTAT / n);
        System.out.println("+-----------------------------------------------+");

        int minWT = Math.min(Math.min(fcfsWT, sjfWT), Math.min(prioWT, rrWT));
        String best;
        if(minWT == sjfWT)
        	best = "SJF";
        else if(minWT == fcfsWT)
        	best = "FCFS";
        else if(minWT == rrWT)
        	best = "Round Robin";
        else
        	best = "Priority Scheduling";
        	
        System.out.println("\nBest Algorithm based on Waiting Time: \n" + best + "\n");
    }

    static void fcfs(Process[] p) {
        Arrays.sort(p, Comparator.comparingInt(a -> a.AT));
        int currTime = 0;
        for (Process pr : p) {
            currTime = Math.max(currTime, pr.AT);
            pr.CT = currTime + pr.BT;
            pr.TAT = pr.CT - pr.AT;
            pr.WT = pr.TAT - pr.BT;
            currTime = pr.CT;
        }
        printTable(p, "FCFS");
    }

    static void sjf(Process[] p) {
        int n = p.length, currTime = 0, completed = 0;
        while (completed < n) {
            int idx = -1, minRT = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (p[i].AT <= currTime && p[i].RT > 0 && p[i].RT < minRT) {
                    minRT = p[i].RT;
                    idx = i;
                }
            }
            if (idx == -1) { currTime++; continue; }
            p[idx].RT--;
            currTime++;
            if (p[idx].RT == 0) {
                p[idx].CT = currTime;
                p[idx].TAT = p[idx].CT - p[idx].AT;
                p[idx].WT = p[idx].TAT - p[idx].BT;
                completed++;
            }
        }
        printTable(p, "SJF");
    }

    static void priority(Process[] p) {
        int n = p.length, currTime = 0, completed = 0;
        boolean[] done = new boolean[n];
        while (completed < n) {
            int idx = -1, high = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!done[i] && p[i].AT <= currTime && p[i].priority < high) {
                    high = p[i].priority;
                    idx = i;
                }
            }
            if (idx == -1) { currTime++; continue; }
            currTime += p[idx].BT;
            p[idx].CT = currTime;
            p[idx].TAT = p[idx].CT - p[idx].AT;
            p[idx].WT = p[idx].TAT - p[idx].BT;
            done[idx] = true;
            completed++;
        }
        printTable(p, "Priority");
    }

    static void roundRobin(Process[] p, int tq) {
        int n = p.length, completed = 0, currTime = 0;
        Queue<Process> q = new LinkedList<>();
        boolean[] inQ = new boolean[n];

        Arrays.sort(p, Comparator.comparingInt(x -> x.AT));
        q.add(p[0]); inQ[0] = true; currTime = p[0].AT;

        while (completed < n) {
            Process curr = q.poll();
            int time = Math.min(tq, curr.RT);
            currTime += time;
            curr.RT -= time;

            for (int i = 0; i < n; i++) {
                if (!inQ[i] && p[i].AT <= currTime && p[i].RT > 0) {
                    q.add(p[i]); inQ[i] = true;
                }
            }
            if (curr.RT > 0) q.add(curr);
            else {
                curr.CT = currTime;
                curr.TAT = curr.CT - curr.AT;
                curr.WT = curr.TAT - curr.BT;
                completed++;
            }

            if (q.isEmpty()) {
                for (int i = 0; i < n; i++) {
                    if (p[i].RT > 0) {
                        q.add(p[i]); inQ[i] = true;
                        currTime = Math.max(currTime, p[i].AT);
                        break;
                    }
                }
            }
        }
        printTable(p, "RoundRobin");
    }

    static void printTable(Process[] p, String label) {
        int totalWT = 0, totalTAT = 0;
        System.out.println("\n+-----------------------------------------------+");
        System.out.println("| PID\tAT\tBT\tCT\tTAT\tWT\t|");
        System.out.println("+-----------------------------------------------+");
        for (Process pr : p) {
            totalWT += pr.WT;
            totalTAT += pr.TAT;
            System.out.printf("| P%d\t%d\t%d\t%d\t%d\t%d\t|\n", pr.ID, pr.AT, pr.BT, pr.CT, pr.TAT, pr.WT);
        }
        System.out.println("+-----------------------------------------------+");
        System.out.printf("\nAverage Waiting Time    : %.2f\n", (float) totalWT / p.length);        
        System.out.printf("\nAverage Turnaround Time : %.2f", (float) totalTAT / p.length);
        System.out.println("\n\n");

        switch (label) {
            case "FCFS" -> { fcfsWT = totalWT; fcfsTAT = totalTAT; }
            case "SJF" -> { sjfWT = totalWT; sjfTAT = totalTAT; }
            case "Priority" -> { prioWT = totalWT; prioTAT = totalTAT; }
            case "RoundRobin" -> { rrWT = totalWT; rrTAT = totalTAT; }
        }
    }
}

