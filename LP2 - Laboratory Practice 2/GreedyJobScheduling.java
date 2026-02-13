import java.util.*;

class Job {
    char id; // ID of job/task
    String taskName; //taskname
    int deadline; //deadline of task
    int profit; //profit of task

    //Initialize task
    Job(char id, String taskName, int deadline, int profit) {
        this.id = id;
        this.taskName = taskName;
        this.deadline = deadline;
        this.profit = profit;
    }
}

public class GreedyJobScheduling {

    public static void scheduleJobs(Job[] jobs, int n) {
          
        // Sort jobs in desc order of profit
        Arrays.sort(jobs, (a, b) -> b.profit - a.profit);

        // Print jobs after sorting
        System.out.println("\nAfter sorting tasks by descending profit:");
        System.out.print("ID:\t\t");
        for (Job job : jobs) {
            System.out.print(job.id + "\t");
        }

        System.out.print("\nProfit:\t\t");
        for (Job job : jobs) {
            System.out.print(job.profit + "\t");
        }

        System.out.print("\nDeadline:\t");
        for (Job job : jobs) {
            System.out.print(job.deadline + "\t");
        }
        System.out.println("\n");

        // Create new result array of objects  
        Job[] result = new Job[n];

        // Select jobs - Scheduling
        for (Job job : jobs) {
            for (int j = Math.min(n, job.deadline) - 1; j >= 0; j--) {
                if (!slot[j]) {
                    slot[j] = true;
                    result[j] = job;
                    break;
                }
            }
        }

        // Total profit
        int totalProfit = 0;
        System.out.println("Execution Order of Tasks (Greedy / Max Profit):");
        System.out.println("ID\tTask\tDeadline\tProfit");

        for (int i = 0; i < n; i++) {
            if (slot[i]) {
                System.out.println(
                        result[i].id + "\t" +
                        result[i].taskName + "\t" +
                        result[i].deadline + "\t\t" +
                        result[i].profit
                );
                totalProfit += result[i].profit;
            }
        }

        System.out.println("\nTotal Profit: " + totalProfit);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n===== Task Scheduler =====\n");
        System.out.print("Enter number of tasks: ");
        int n = sc.nextInt();
        sc.nextLine();

        Job[] jobs = new Job[n];

        for (int i = 0; i < n; i++) {
            char taskId = (char) ('A' + i);

            System.out.println("\nEnter details for Task " + taskId);
            System.out.print("Task Name: ");
            String taskName = sc.nextLine();
            System.out.print("Deadline: ");
            int deadline = sc.nextInt();
            System.out.print("Profit: ");
            int profit = sc.nextInt();
            sc.nextLine();

            jobs[i] = new Job(taskId, taskName, deadline, profit);
        }
        
        // Call schedule jobs function
        scheduleJobs(jobs, n);
        sc.close();
    }
}
