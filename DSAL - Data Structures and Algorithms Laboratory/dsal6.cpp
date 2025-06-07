/*
Represent a given graph using adjacency list to perform DFS and BFS.
Use the map of the area around the college as the graph.
Identify the prominent land marks as nodes and perform DFS and BFS on that.
*/

#include<iostream>
using namespace std;

class Graph;  

class Node{
	private:
		int data;
		Node* next;
		friend class Graph;

	public:
		Node(int x){
			data = x;
			next = nullptr;
		}
};

class Graph{
	private:
		Node* G[20]; 
		int visited[20]; 

	public:
		Graph(int n){
			for(int i = 0; i < n; i++){
				G[i] = new Node(i);  
			}
		}

		void create(int n){
			Node* temp, *q;
			int s, d;
			for(int i = 0; i < n; i++){
				cout << "\nEnter source and destination vertex: \n";
				cout << "Source     : ";
				cin >> s;
				cout << "Destination: ";
				cin >> d;

				temp = G[s];
				while(temp->next != nullptr){  
					temp = temp->next;
				}
				q = new Node(d);
				temp->next = q;  

				temp = G[d];
				while(temp->next != nullptr){  
					temp = temp->next;
				}
				q = new Node(s);
				temp->next = q;  
			}
		}

		void BFS(int v1){
			Node* temp;
			int Q[20], f = 0, r = -1;
			for(int i = 0; i < 20; i++) visited[i] = 0; 
			Q[++r] = v1;  
			visited[v1] = 1;  

			while(f <= r){
				int x = Q[f++];  
				cout << "Visited: " << x << endl;

				temp = G[x];  
				while(temp != nullptr){
					if(visited[temp->data] == 0){  
						Q[++r] = temp->data;  
						visited[temp->data] = 1;  
					}
					temp = temp->next;  
				}
			}
		}

		void DFS(int v1){
			Node* temp;
			cout << "Visited: " << v1 << endl;
			visited[v1] = 1;  

			temp = G[v1];  
			while(temp != nullptr){
				if(visited[temp->data] == 0){  
					DFS(temp->data);  
				}
				temp = temp->next;  
			}
		}

		void resetVisited(){
			for(int i = 0; i < 20; i++) visited[i] = 0;
		}
};
/*
int main(){
	int ch, n, e;
	Graph g(0); 

	while(true){
		cout << "\nGraph Menu";
		cout << "\n1. Create";
		cout << "\n2. BFS Traversal";
		cout << "\n3. DFS Traversal";
		cout << "\n4. Exit";
		cout << "\n\nEnter choice : ";
		cin >> ch;

		switch(ch){
			case 1:{
				cout << "\nEnter no. of vertices : ";
				cin >> n;
				g = Graph(n); 
				cout << "\nEnter no. of edges : ";
				cin >> e;
				g.create(e); 
				break;
			}
			case 2:{
				cout << "\nBFS Traversal\n";
				int startVertex;
				cout << "\nEnter starting vertex for BFS: ";
				cin >> startVertex;
				g.resetVisited(); 
				g.BFS(startVertex); 
				break;
			}
			case 3:{
				cout << "\nDFS Traversal\n";
				int startVertex;
				cout << "\nEnter starting vertex for DFS: ";
				cin >> startVertex;
				g.resetVisited(); 
				g.DFS(startVertex); 
				break;
			}
			case 4:{
				cout << "Exiting ...";
				return 0;
			}
			default:
				cout << "Invalid Choice!";
		}
	}
}
*/
