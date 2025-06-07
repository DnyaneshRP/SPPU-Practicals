/*	Create an inordered threaded binary search tree.
 	Perform inorder, preorder traversals without recursion and deletion of a node.
 	Analyze time and space complexity of the algorithm.
*/

#include<iostream>
using namespace std;

class Node {
private:
    bool lth;
    Node* left;
    int data;
    Node* right;
    bool rth;

public:
    Node(int val) {
        lth = false;
        left = nullptr;
        data = val;
        right = nullptr;
        rth = false;
    }
    friend class TBST;
};

class TBST {
private:
    Node* head;

public:
    TBST() {
        head = nullptr;
    }

    Node* insertdepth(Node* t1, int x) {
        if (x < t1->data) {
            if (t1->lth == true) {
            	Node* t = new Node(x);
            	linsert(t1, t);
            } else {
                insertdepth(t1->left, x);
            }
        } else if (x > t1->data) {
            if (t1->rth == true) {
            	Node* t = new Node(x);
            	rinsert(t1, t);
            } else {
                insertdepth(t1->right, x);
            }
        }
        return t1;
    }

    void insert() {
        int x;
        if (head == nullptr) {
            Node *p, *q;
            p = new Node(-99999);
            head = p;
            p->left = p->right = p;
            p->lth = true;
            p->rth = false;
            cout << "\nEnter data for root to be inserted in TBST : ";
            cin >> x;
            q = new Node(x);
            linsert(head, q);
        } else {
            cout << "\nTree head is created\n";
            cout << "\nEnter data to be inserted in TBST : ";
            cin >> x;
            head->left = insertdepth(head->left, x);
        }
    }

    void linsert(Node* s, Node* t) {
        t->left = s->left;
        t->right = s;
        t->lth = s->lth;
        t->rth = true;
        s->left = t;
        s->lth = false;
    }

    void rinsert(Node* s, Node* t) {
        t->right = s->right;
        t->left = s;
        t->rth = s->rth;
        t->lth = true;
        s->right = t;
        s->rth = false;
    }

    void TBSTInorder() {
        if(head == nullptr){
        	cout << " ";
        	return;
        }
    	Node* T = head;
        while (true) {
            T = inSuccessor(T);
            if (T == head) {
                break;
            }
            cout << T->data << " ";
        }
    }

    Node* inSuccessor(Node* X) {
        Node* S;
        S = X->right;
        if (X->rth == false) {
            while (S->lth == false) {
                S = S->left;
            }
        }
        return S;
    }

    void TBSTPreorder() {
    	if(head == nullptr){
    		cout << " ";
    		return;
    	}
    	Node* T = head->left;
        while (true) {
            if (T == head) {
                break;
            }
            cout << T->data << " ";
            if (T->lth == false) {
                T = T->left;
            } else {
                if (T->rth == false) {
                    T = T->right;
                } else {
                    while (T->rth != false) {
                        T = T->right;
                    }
                    T = T->right;
                }
            }
        }
    }


    // Function to delete a node from the TBST
    //Complete code here for deleteNode
    void deleteNode(int key) {
        Node *parent = head;
        Node *curr = head->left;
        while(curr != head && curr->data != key){
        	parent = curr;
        	if(key < curr->data){
        		if(curr->lth == false){
        			curr = curr->left;
        		}else{
        			break;
        		}
        	}else{
        		if(curr->rth == false){
        			curr = curr->right;
        		}else{
        			break;
        		}
        	}
        }
        if(curr == head || curr->data != key){
        	cout << "\nData not found..!\n";
        	return;
        }

        // Case 1: Node has two children
        if (curr->lth == false && curr->rth == false) {
            Node* par = curr;
            Node* succ = curr->right;
            while (succ->lth == false) {
                par = succ;
                succ = succ->left;
            }
            curr->data = succ->data;
            curr = succ;
            parent = par;
        }

        // Case 2: Node has no children (both threads)
        if (curr->lth == true && curr->rth == true) {
            if(curr == head->left){
            	head = nullptr;
            	delete curr;
            	cout<< "\nNode deleted successfully...\n";
            	return;
            }
        	if (parent->right == curr) {
                parent->right = curr->right;
                parent->rth = true;
            } else {
                parent->left = curr->left;
                parent->lth = true;
            }
            delete curr;
            cout<< "\nNode deleted successfully...\n";
            return;
        }

        // Case 3: Node has only left child
        if (curr->lth == false && curr->rth == true) {
            Node* child = curr->left;
            if (parent->left == curr) {
                parent->left = child;
            } else {
                parent->right = child;
            }

            Node* pred = child;
            while (pred->rth == false)
                pred = pred->right;
            pred->right = curr->right;
            delete curr;
            cout<< "\nNode deleted successfully...\n";
            return;
        }

        // Case 4: Node has only right child
        if (curr->lth == true && curr->rth == false) {
            Node* child = curr->right;
            if (parent->left == curr) {
                parent->left = child;
            } else {
                parent->right = child;
            }

            Node* succ = child;
            while (succ->lth == false)
                succ = succ->left;
            succ->left = curr->left;
            delete curr;
            cout<< "\nNode deleted successfully...\n";
            return;
        }
    }


    void display() {
        cout << "\nTBST Inorder Traversal:  ";
        TBSTInorder();
        cout << "\n\nTBST Preorder Traversal: ";
        TBSTPreorder();
        cout << endl;
    }
};

int main() {
    int ch, key;
    TBST t;
    while (true) {
        cout << "\n\n";
        cout << "+------------------+\n";
        cout << "|      Menu        |\n";
        cout << "+------------------+\n";
        cout << "| 1. Insert        |\n";
        cout << "| 2. Display       |\n";
        cout << "| 3. Delete        |\n";
        cout << "| 4. Exit          |\n";
        cout << "+------------------+\n";
        cout << "\nEnter your choice : ";
        cin >> ch;

        switch (ch) {
            case 1:
                t.insert();
                break;

            case 2:
                t.display();
                break;

            case 3:
                cout << "\nEnter the key to delete: ";
                cin >> key;
                // Complete code here
                t.deleteNode(key);

                break;

            case 4:
                cout << "\nExiting...\n";
                return 0;

            default:
                cout << "\nInvalid choice!";
                break;
        }
    }

    return 0;
}

