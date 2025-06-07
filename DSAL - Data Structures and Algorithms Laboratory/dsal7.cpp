/*
You have a business with several offices; you want to lease phone lines to connect them up with each other; 
and the phone company charges different amounts of money to connect different pairs of cities. 
You want a set of lines that connects all your offices with a minimum total cost. 
Solve the problem by suggesting appropriate data structures.
*/ 

#include <iostream>
#include <climits> // For INT_MAX
using namespace std;

class Graph {
private:
    int **adjMatrix;
    int numVertices;

public:
    // Constructor
    Graph() {
        numVertices = 0;
        adjMatrix = nullptr;
    }

    // Create graph
    void createGraph(int vertices) {
        numVertices = vertices;
        adjMatrix = new int*[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adjMatrix[i] = new int[numVertices];
            for (int j = 0; j < numVertices; j++) {
                adjMatrix[i][j] = 0; // Initialize with 0 (no edge)
            }
        }
    }

    // Add edge (Undirected Graph)
    void addEdge(int u, int v, int weight) {
        adjMatrix[u][v] = weight;
        adjMatrix[v][u] = weight; // Since the graph is undirected
    }

    // Display the adjacency matrix
    void display() {
        cout << "\nAdjacency Matrix:\n";
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                cout << adjMatrix[i][j] << " ";
            }
            cout << endl;
        }
    }

    // Kruskal's algorithm
    void kruskal() {
        int **tempMatrix = new int*[numVertices];
        for (int i = 0; i < numVertices; i++) {
            tempMatrix[i] = new int[numVertices];
            for (int j = 0; j < numVertices; j++) {
                tempMatrix[i][j] = adjMatrix[i][j];
            }
        }

        int count = 0, min = INT_MAX, wt = 0;
        int *father = new int[numVertices];
        int **res = new int*[numVertices];

        for (int i = 0; i < numVertices; i++) {
            father[i] = -1;
            res[i] = new int[numVertices];
            for (int j = 0; j < numVertices; j++) {
                res[i][j] = 0;
            }
        }

        while (count < numVertices - 1) {
            min = INT_MAX;
            int t1 = -1, t2 = -1;

            for (int v1 = 0; v1 < numVertices; v1++) {
                for (int v2 = 0; v2 < numVertices; v2++) {
                    if (tempMatrix[v1][v2] != 0 && tempMatrix[v1][v2] < min) {
                        min = tempMatrix[v1][v2];
                        t1 = v1;
                        t2 = v2;
                    }
                }
            }

            if (t1 == -1 || t2 == -1) break;

            int temp1 = t1;
            int temp2 = t2;
            tempMatrix[t1][t2] = tempMatrix[t2][t1] = 0;

            int root_temp1 = t1;
            while (father[root_temp1] != -1) {
                root_temp1 = father[root_temp1];
            }

            int root_temp2 = t2;
            while (father[root_temp2] != -1) {
                root_temp2 = father[root_temp2];
            }

            if (root_temp1 != root_temp2) {
                res[temp1][temp2] = res[temp2][temp1] = min;
                wt += min;
                father[root_temp2] = root_temp1;
                count++;
            }
        }

        cout << "\nMinimum Spanning Tree (Kruskal's Algorithm):\n";

        cout << "\nAdjacency Matrix:\n";
        for (int i = 0; i < numVertices; i++) {
        	for (int j = 0; j < numVertices; j++) {
        		cout << res[i][j] << " ";
        	}
        	cout << endl;
        }

        cout << "\nEdge \tWeight\n";
        for (int i = 0; i < numVertices; i++) {
            for (int j = i; j < numVertices; j++) {
                if (res[i][j] != 0) {
                    cout << i << " - " << j << "\t" << res[i][j] << endl;
                }
            }
        }
        cout << "Total weight of MST: " << wt << endl;

        for (int i = 0; i < numVertices; i++) {
            delete[] res[i];
            delete[] tempMatrix[i];
        }
        delete[] res;
        delete[] tempMatrix;
        delete[] father;
    }

    // Prim's algorithm
    void prims(int startVertex) {
        int **tempMatrix = new int*[numVertices];
        for (int i = 0; i < numVertices; i++) {
            tempMatrix[i] = new int[numVertices];
            for (int j = 0; j < numVertices; j++) {
                tempMatrix[i][j] = adjMatrix[i][j];
            }
        }

        int count = 0, wt = 0;
        int *A = new int[numVertices];
        int *father = new int[numVertices];
        int *visited = new int[numVertices];
        int **res = new int*[numVertices];

        for (int i = 0; i < numVertices; i++) {
            father[i] = -1;
            visited[i] = 0;
            res[i] = new int[numVertices];
            for (int j = 0; j < numVertices; j++) {
                res[i][j] = 0;
            }
        }

        int count1 = 0;
        A[count1] = startVertex;
        count1++;
        visited[startVertex] = 1;

        while (count < numVertices - 1) {
            int min = INT_MAX;
            int t1 = -1, t2 = -1;

            for (int i = 0; i < count1; i++) {
                int v1 = A[i];
                for (int v2 = 0; v2 < numVertices; v2++) {
                    if (tempMatrix[v1][v2] != 0 && tempMatrix[v1][v2] < min && !visited[v2]) {
                        min = tempMatrix[v1][v2];
                        t1 = v1;
                        t2 = v2;
                    }
                }
            }

            if (t1 == -1 || t2 == -1) break;

            tempMatrix[t1][t2] = tempMatrix[t2][t1] = 0;

            res[t1][t2] = res[t2][t1] = min;
            wt += min;
            visited[t2] = 1;
            A[count1] = t2;
            count1++;
            count++;
        }

        cout << "\nMinimum Spanning Tree (Prim's Algorithm starting from vertex " << startVertex << "):\n";

        cout << "\nAdjacency Matrix:\n";
        for (int i = 0; i < numVertices; i++) {
        	for (int j = 0; j < numVertices; j++) {
        		cout << res[i][j] << " ";
        	}
        	cout << endl;
        }

        cout << "\nEdge \tWeight\n";
        for (int i = 0; i < numVertices; i++) {
            for (int j = i; j < numVertices; j++) {
                if (res[i][j] != 0) {
                    cout << i << " - " << j << "\t" << res[i][j] << endl;
                }
            }
        }
        cout << "Total weight of MST: " << wt << endl;

        delete[] A;
        delete[] father;
        delete[] visited;
        for (int i = 0; i < numVertices; i++) {
            delete[] res[i];
            delete[] tempMatrix[i];
        }
        delete[] res;
        delete[] tempMatrix;
    }

    // Destructor
    ~Graph() {
        if (adjMatrix != nullptr) {
            for (int i = 0; i < numVertices; i++) {
                delete[] adjMatrix[i];
            }
            delete[] adjMatrix;
        }
    }
};



int main() {
    Graph g;
    int choice, vertices, edges, startVertex;

    do {
        cout << "\n======== Graph Operations Menu =======\n";
        cout << "1. Create Graph \n";
        cout << "2. Find MST using Prim's Algorithm\n";
        cout << "3. Find MST using Kruskal's Algorithm\n";
        cout << "4. Exit\n";
        cout << "======================================\n";
        cout << "\nEnter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1: {
                cout << "Enter number of vertices: ";
                cin >> vertices;
                g.createGraph(vertices);

                cout << "Enter number of edges: ";
                cin >> edges;

                cout << "Enter edges (format: u v weight):\n";
                for (int i = 0; i < edges; i++) {
                    int u, v, weight;
                    cin >> u >> v >> weight;
                    g.addEdge(u, v, weight);
                }

                g.display();
                break;
            }
            case 2: {
                cout << "Enter starting vertex: ";
                cin >> startVertex;
                g.prims(startVertex);
                break;
            }
            case 3:
                g.kruskal();
                break;
            case 4:
                cout << "Exiting program...\n";
                break;
            default:
                cout << "Invalid choice! Please try again.\n";
        }
    } while (choice != 4);

    return 0;
}
