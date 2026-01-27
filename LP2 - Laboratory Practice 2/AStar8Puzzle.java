import java.util.*;

class Node {
    int[][] state;
    int g; // cost from start
    int h; // heuristic value
    int f; // g + h
    Node parent;

    Node(int[][] state, int g, int h, Node parent) {
        this.state = state;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.parent = parent;
    }
}

public class AStar8Puzzle {

    static int[][] goal = new int[3][3];

    // Misplaced tiles heuristic
    static int calculateHeuristic(int[][] state) {
        int misplaced = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] != 0 && state[i][j] != goal[i][j]) {
                    misplaced++;
                }
            }
        }
        return misplaced;
    }

    // Check goal state
    static boolean isGoal(int[][] state) {
        return Arrays.deepEquals(state, goal);
    }

    // Find blank tile (0)
    static int[] findBlank(int[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0)
                    return new int[]{i, j};
            }
        }
        return null;
    }

    // Generate successors
    static List<Node> generateSuccessors(Node current) {
        List<Node> successors = new ArrayList<>();
        int[] blank = findBlank(current.state);
        int x = blank[0], y = blank[1];

        int[][] moves = {
                {x - 1, y}, // up
                {x + 1, y}, // down
                {x, y - 1}, // left
                {x, y + 1}  // right
        };

        for (int[] move : moves) {
            int nx = move[0];
            int ny = move[1];

            if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3) {
                int[][] newState = copyState(current.state);
                newState[x][y] = newState[nx][ny];
                newState[nx][ny] = 0;

                int h = calculateHeuristic(newState);
                successors.add(new Node(newState, current.g + 1, h, current));
            }
        }
        return successors;
    }

    static int[][] copyState(int[][] state) {
        int[][] copy = new int[3][3];
        for (int i = 0; i < 3; i++)
            copy[i] = state[i].clone();
        return copy;
    }

    static void printState(int[][] state) {
        for (int[] row : state) {
            for (int val : row)
                System.out.print(val + " ");
            System.out.println();
        }
    }

    // A* Algorithm
    static void aStar(int[][] start) {
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<String> closed = new HashSet<>();

        int h = calculateHeuristic(start);
        open.add(new Node(start, 0, h, null));

        while (!open.isEmpty()) {
            Node current = open.poll();

            System.out.println("Selected Node with MIN f-value:");
            printState(current.state);
            System.out.println("g = " + current.g + ", h = " + current.h + ", f = " + current.f);
            System.out.println("--------------------------------");

            if (isGoal(current.state)) {
                System.out.println("Goal State Reached!");
                return;   // STOP HERE
            }

            closed.add(Arrays.deepToString(current.state));

            for (Node successor : generateSuccessors(current)) {
                String key = Arrays.deepToString(successor.state);
                if (!closed.contains(key)) {
                    open.add(successor);
                }
            }
        }

        System.out.println("No solution found.");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int[][] start = new int[3][3];

        System.out.println("Enter Initial State (0 for blank):");
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                start[i][j] = sc.nextInt();

        System.out.println("Enter Goal State:");
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                goal[i][j] = sc.nextInt();

        aStar(start);
        sc.close();
    }
}
