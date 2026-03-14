import java.util.Scanner;

public class NQueens {
    private int N;
    private int[] board; 
    private boolean[] cols;    
    private boolean[] diag1;  
    private boolean[] diag2;   
    private boolean solutionFound = false;

    public NQueens(int N) {
        this.N = N;
        board = new int[N];
        cols = new boolean[N];
        diag1 = new boolean[2 * N];
        diag2 = new boolean[2 * N];
    }

    public void solve() {
        
        if (N <= 0) {
            System.out.println("Invalid input. N must be greater than 0.");
            return;
        }
        if (N == 2 || N == 3) {
            System.out.println("No solution exists for N = " + N);
            return;
        }

        placeQueen(0);

        if (!solutionFound) {
            System.out.println("No solution exists for N = " + N);
        }
    }

    private void placeQueen(int row) {
        if (row == N) {
            printSolution();
            solutionFound = true;
            return; 
        }

        for (int col = 0; col < N; col++) {
            if (isSafe(row, col)) {
                board[row] = col;
                cols[col] = diag1[row - col + N] = diag2[row + col] = true;

                placeQueen(row + 1);

                if (solutionFound) return; 

                
                cols[col] = diag1[row - col + N] = diag2[row + col] = false;
            }
        }
    }

    
    private boolean isSafe(int row, int col) {
        return !cols[col] && !diag1[row - col + N] && !diag2[row + col];
    }

    private void printSolution() {
        System.out.println("One possible solution for " + N + "-Queens:");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i] == j) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter value of N: ");
        int N = sc.nextInt();
        NQueens solver = new NQueens(N);
        solver.solve();
        sc.close();
    }
}
