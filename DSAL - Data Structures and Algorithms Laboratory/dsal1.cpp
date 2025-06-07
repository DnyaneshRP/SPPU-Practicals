/*
Beginning with an empty binary tree, construct binary tree by inserting the values in the order given. After constructing a binary tree perform following operations on it-
- Perform inorder, preorder and post order traversal
- Change a tree so that the roles of the left and right pointers are swapped at every node
- Find the height of tree
- Copy this tree to another [operator=]
- Count number of leaves, number of internal nodes.
- Erase all nodes in a binary tree.
(Implement both recursive and non-recursive methods)
 */

#include<iostream>
using namespace std;

class Node{
	private:
		int data;
		Node *left, *right;
		Node(int value) : data(value), left(nullptr), right(nullptr) {}
		friend class BinaryTree;
};

class Stack{
	private:
		Node* arr[100];
		int top;
	public:
		Stack() : top(-1) {}

		void push(Node* node){
			if(top < 99)
				arr[++top] = node;
		}

		Node* pop(){
			return (top >= 0) ? arr[top--] : nullptr;
		}

		Node* peek(){
			return (top >= 0) ? arr[top] : nullptr;
		}

		bool isEmpty(){
			return top == -1;
		}
};

class BinaryTree{
	private:
		Node* root;

	public:
		BinaryTree() : root(nullptr) {}


		void insert(int value) {
		    Node* newNode = new Node(value);

		    if (!root) {
		        root = newNode;
		        cout << "Tree was empty. Node " << value << " has been set as the root." << endl;
		        return;
		    }

		    Node* current = root;
		    while (true) {
		        char choice;
		        cout << "Current Node: " << current->data << ". \nInsert as left or right child? (l/r): ";
		        cin >> choice;

		        if (choice == 'l' || choice == 'L') {
		            if (!current->left) {
		                current->left = newNode;
		                cout << "Node " << value << " inserted as left child of " << current->data << "." << endl;
		                break;
		            } else {
		                cout << "Left child of node " << current->data << " already exists. Moving to left child...\n" << endl;
		                current = current->left;
		            }
		        } else if (choice == 'r' || choice == 'R') {
		            if (!current->right) {
		                current->right = newNode;
		                cout << "Node " << value << " inserted as right child of " << current->data << "." << endl;
		                break;
		            } else {
		                cout << "Right child of node " << current->data << " already exists. Moving to right child...\n" << endl;
		                current = current->right;
		            }
		        } else {
		            cout << "Invalid choice. Use 'l' for left or 'r' for right." << endl;
		        }
		    }
		}

		void inorderRecursive(Node* node){
			if(node){
				inorderRecursive(node->left);
				cout << node->data << " ";
				inorderRecursive(node->right);
			}
		}

		void preorderRecursive(Node* node){
			if(node){
				cout << node->data << " ";
				preorderRecursive(node->left);
				preorderRecursive(node->right);
			}
		}

		void postorderRecursive(Node* node){
			if(node){
				postorderRecursive(node->left);
				postorderRecursive(node->right);
				cout << node->data << " ";
			}
		}

		Node* mirror(Node* node){
			if(node){
				Node* temp = node->left;
				node->left = mirror(node->right);
				node->right = mirror(temp);
			}
			return node;
		}

		int height(Node* node){
			if(!node)
				return -1;
			return 1 + max(height(node->left), height(node->right));
		}

	    BinaryTree& operator=(const BinaryTree &other) {
	        if (this == &other)
	        	return *this;
	        eraseTree(root);
	        copyTree(root, other.root);
	        return *this;
	    }

		void copyTree(Node* &newRoot, Node* originalNode){
			if(originalNode){
				newRoot = new Node(originalNode->data);
				copyTree(newRoot->left, originalNode->left);
				copyTree(newRoot->right, originalNode->right);
			}
		}

		int countLeaves(Node* node){
			if(!node)
				return 0;
			if(!node->left && !node->right)
				return 1;
			return countLeaves(node->left) + countLeaves(node->right);
		}

		int countInternalNodes(Node* node){
			if(!node || (!node->left && !node->right))
				return 0;
			return 1 + countInternalNodes(node->left) + countInternalNodes(node->right);
		}

		void eraseTree(Node* &node){
			if(node){
				eraseTree(node->left);
				eraseTree(node->right);
				delete node;
				node = nullptr;
			}
		}

		void inorderNonRecursive(Node* node){
			Stack st;
			while(node || !st.isEmpty()){
				while(node){
					st.push(node);
					node = node->left;
				}
				node = st.pop();
				cout << node->data << " ";
				node = node->right;
			}
		}

		void preorderNonRecursive(Node* node){
			Stack st;
			st.push(node);
			while(!st.isEmpty()){
				node = st.pop();
				cout << node->data << " ";
				if(node->right)
					st.push(node->right);
				if(node->left)
					st.push(node->left);
			}
		}

		void postorderNonRecursive(Node* node){
			Stack st1, st2;
			st1.push(node);
			while(!st1.isEmpty()){
				node = st1.pop();
				st2.push(node);
				if(node->left)
					st1.push(node->left);
				if(node->right)
					st1.push(node->right);
			}
			while(!st2.isEmpty()){
				cout << st2.pop()->data << " ";
			}
		}


		void performOperations(){
			int choice, value;
			while(true){
				cout << "\n\n=========================================\n"
					 << "		   Menu"
					 << "\n=========================================\n"
					 << "1. Insert Node\n"
					 << "2. Tree Traversal (Recursive)\n"
					 << "3. Tree Traversal (Non-Recursive)\n"
					 << "4. Swap Left and Right Child (Mirror)\n"
					 << "5. Find Height\n"
					 << "6. Copy Tree\n"
					 << "7. Count Leaves and Internal Nodes\n"
					 << "8. Erase Tree\n"
					 << "9. Exit\n"
					 << "=========================================\n"
					 << "\nEnter your choice : ";
				cin >> choice;

				switch(choice){
					case 1:
						cout << "\nEnter value to insert : ";
						cin >> value;
						insert(value);
						break;

					case 2:
						cout << "\nRecursive Traversal";
						cout << "\nInorder   : ";
						inorderRecursive(root);
						cout << "\nPreorder  : ";
						preorderRecursive(root);
						cout << "\nPostorder : ";
						postorderRecursive(root);
						break;

					case 3:
						cout << "\nNon-Recursive Traversal";
						cout << "\nInorder : ";
						inorderNonRecursive(root);
						cout << "\nPreorder : ";
						preorderNonRecursive(root);
						cout << "\nPostorder : ";
						postorderNonRecursive(root);
						break;

					case 4:
						root = mirror(root);
						cout << "Swapped/Mirrored tree successfully...\n";
						break;

					case 5:
						cout << "\nHeight : " << height(root) << endl;
						break;

					case 6:{
					    BinaryTree newTree;
					    newTree = *this;
					    cout << "\nTree copied successfully.";
					    cout << "\nInorder Traversal of Copied Tree: \n";
					    newTree.inorderRecursive(newTree.root);
					    break;
					}
					case 7:
						cout << "\nNumber of leaves : " << countLeaves(root) << endl;
						cout << "Number of internal nodes : " << countInternalNodes(root) << endl;
						break;

					case 8:
						eraseTree(root);
						cout << "\nTree erased.\n";
						break;

					case 9:
						cout << "\nExiting...\n";
						exit(1);

					default:
						cout << "\nInvalid Choice!";
						break;
				}
			}
		}
};

/*
int main(){
	BinaryTree bt;
	bt.performOperations();
	return 0;
}
*/
