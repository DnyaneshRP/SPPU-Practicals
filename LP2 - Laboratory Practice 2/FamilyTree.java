/*
Implement Depth First Search algorithm and Breadth First Search algorithm, 
Use an undirected graph and develop a recursive algorithm 
for searching all the vertices of a 
graph or tree data structure.  
*/


import java.util.*;

class FamilyTree {

    // To store family tree
    Map<String, List<String>> family = new HashMap<>();
    
    // To mark visited members
    Set<String> visited = new HashSet<>();
    
    // Queue for BFS
    Queue<String> queue = new LinkedList<>();

    // Add relationship (undirected)
    void addRelation(String p1, String p2) {
        family.putIfAbsent(p1, new ArrayList<>());
        family.putIfAbsent(p2, new ArrayList<>());

        family.get(p1).add(p2);
        family.get(p2).add(p1);
    }

    // ---------------- DFS (Recursive) ----------------
    void DFS(String member) {
        visited.add(member);
        System.out.println(member);

        for (String relative : family.get(member)) {
            if (!visited.contains(relative)) {
                DFS(relative);  // recursive call
            }
        }
    }


    // ---------------- BFS (Recursive) ----------------
    void BFS(String member) {

        // First time initialization
        if (queue.isEmpty() && visited.isEmpty()) {
            visited.add(member);
            queue.add(member);
        }

        if (queue.isEmpty())
            return;

        String current = queue.poll();
        System.out.println(current);

        for (String relative : family.get(current)) {
            if (!visited.contains(relative)) {
                visited.add(relative);
                queue.add(relative);
            }
        }

        BFS(member); // recursive call
    }


    public static void main(String[] args) {

        FamilyTree tree = new FamilyTree();

        
        // Test Case Input - 1
         
        tree.addRelation("Grandfather", "Father");
        tree.addRelation("Grandfather", "Uncle");
        tree.addRelation("Father", "Son");
        tree.addRelation("Father", "Daughter");
        
        // Test Case Input - 2
        /*
        tree.addRelation("Grandmother", "Father");
        tree.addRelation("Grandmother", "Aunt");
        tree.addRelation("Father", "Son");
        tree.addRelation("Father", "Daughter");
        */

        System.out.println("DFS Traversal:");
        tree.DFS("Grandfather");
        //tree.DFS("Grandmother");

        tree.visited.clear();

        System.out.println("\nBFS Traversal:");
        tree.BFS("Grandfather");
        //tree.BFS("Grandmother");
    }
}
