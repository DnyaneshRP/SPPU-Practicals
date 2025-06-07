/*
Given sequence k = k1 <k2 < … <kn of n sorted keys, with a successful and unsuccessful
search probability pi and qi for each key ki. Build the Binary search tree that
has the least search cost given the access probability for each key.
*/

#include <iostream>
using namespace std;

class Node {
public:
    int key;
    Node *left, *right;
    Node(int k) {
        key = k;
        left = right = NULL;
    }
};

class OBST {
    double* p;
    double* q;
    double** w;
    double** c;
    int** r;
    int n;
    int* keys;
    Node* root;

public:
    OBST() {
        root = NULL;
        p = q = nullptr;
        w = c = nullptr;
        r = nullptr;
        keys = nullptr;
    }

    ~OBST() {
        delete[] p;
        delete[] q;
        delete[] keys;
        for (int i = 0; i <= n; i++) {
            delete[] w[i];
            delete[] c[i];
            delete[] r[i];
        }
        delete[] w;
        delete[] c;
        delete[] r;
    }

    void input() {
        cout << "\nEnter number of keys: ";
        cin >> n;

        p = new double[n + 1];
        q = new double[n + 1];
        keys = new int[n + 1];

        w = new double*[n + 1];
        c = new double*[n + 1];
        r = new int*[n + 1];
        for (int i = 0; i <= n; i++) {
            w[i] = new double[n + 1]{};
            c[i] = new double[n + 1]{};
            r[i] = new int[n + 1]{};
        }

        cout << "Enter keys in sorted order:\n";
        for (int i = 1; i <= n; i++) {
            cout << "Key " << i << ": ";
            cin >> keys[i];
        }

        cout << "Enter successful search probabilities (p1 to pn):\n";
        for (int i = 1; i <= n; i++) {
            cout << "p" << i << ": ";
            cin >> p[i];
        }

        cout << "Enter unsuccessful search probabilities (q0 to q" << n << "):\n";
        for (int i = 0; i <= n; i++) {
            cout << "q" << i << ": ";
            cin >> q[i];
        }
    }

    void buildOBST() {
        for (int i = 0; i <= n; i++) {
            w[i][i] = q[i];
            c[i][i] = 0;
            r[i][i] = 0;
            if (i < n) {
                w[i][i + 1] = q[i] + q[i + 1] + p[i + 1];
                c[i][i + 1] = w[i][i + 1];
                r[i][i + 1] = i + 1;
            }
        }

        for (int m = 2; m <= n; m++) {
            for (int i = 0; i <= n - m; i++) {
                int j = i + m;
                w[i][j] = w[i][j - 1] + p[j] + q[j];
                double minCost = 99999;
                int k = 0;
                for (int k1 = i + 1; k1 <= j; k1++) {
                    double cost = c[i][k1 - 1] + c[k1][j];
                    if (cost < minCost) {
                        minCost = cost;
                        k = k1;
                    }
                }
                c[i][j] = w[i][j] + c[i][k - 1] + c[k][j];
                r[i][j] = k;
            }
        }

        root = createTree(0, n);
        cout << "\nTree built successfully." << endl;
    }

    Node* createTree(int i, int j) {
        if (i == j) return NULL;
        int k = r[i][j];
        Node* t = new Node(keys[k]);
        t->left = createTree(i, k - 1);
        t->right = createTree(k, j);
        return t;
    }

    void showInorder(Node* node) {
        if (!node) return;
        showInorder(node->left);
        cout << node->key << " ";
        showInorder(node->right);
    }

    void showPreorder(Node* node) {
        if (!node) return;
        cout << node->key << " ";
        showPreorder(node->left);
        showPreorder(node->right);
    }

    void showParentChild(Node* node, int parent = -1) {
        if (!node) return;

        cout << endl << "+---------------------------------------------------------+" << endl;

        // Left child
        cout << "| Left : ";
        if (node->left)
            cout << node->left->key << "      ";
        else
            cout << "NULL    ";

        // Node + Parent/Root
        cout << "| Node : " << node->key;
        if (parent == -1)
            cout << " (Root)        ";
        else {
            // Add spacing to align based on key width
            if (node->key < 10)
                cout << " (Parent: " << parent << ")    ";
            else
                cout << " (Parent: " << parent << ")  ";
        }

        // Right child
        cout << "| Right : ";
        if (node->right)
            cout << node->right->key << "   |";
        else
            cout << "NULL |";

        cout << endl << "+---------------------------------------------------------+" << endl;

        showParentChild(node->left, node->key);
        showParentChild(node->right, node->key);
    }

    void displayTreeDetails() {
        cout << "\nInorder Traversal  : ";
        showInorder(root);
        cout << "\nPreorder Traversal : ";
        showPreorder(root);
        cout << "\n\n";

        cout << "Parent-Child View:\n";
        showParentChild(root);

        cout << "\nLeast Search Cost = " << c[0][n] << endl;
    }

    void menu() {
        int choice;
        do {
            cout << "\n======== Menu =======\n";
            cout << "1. Input Data\n";
            cout << "2. Build OBST\n";
            cout << "3. Show Tree Details\n";
            cout << "4. Exit\n";
            cout << "\nEnter choice: ";
            cin >> choice;

            switch (choice) {
                case 1:
                    input();
                    break;
                case 2:
                    buildOBST();
                    break;
                case 3:
                    displayTreeDetails();
                    break;
                case 4:
                    cout << "Exiting...\n";
                    break;
                default:
                    cout << "Invalid choice. Try again.\n";
            }
        } while (choice != 0);
    }
};

int main() {
    OBST tree;
    tree.menu();
    return 0;
}
