/*
A Dictionary stores keywords and its meanings.
Provide facility for adding new keywords.
Provide facility to display whole data sorted in ascending/ Descending order.
Also find how many maximum comparisons may require for finding any keyword.
Use Height balance tree and find the complexity for finding a keyword.
 */


#include <iostream>
#include <string>
using namespace std;

class Node {
public:
    string keyword;
    string meaning;
    Node* left;
    Node* right;
    int height;

    Node(string key, string mean) {
        keyword = key;
        meaning = mean;
        left = right = NULL;
        height = 1;
    }
};

class AVL {
private:
    Node* root;

    int height(Node* n) {
        return n ? n->height : 0;
    }

    int getBalance(Node* n) {
        return n ? height(n->left) - height(n->right) : 0;
    }


    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    Node* rightRotate(Node* y) {
        Node* x = y->left;
        Node* T2 = x->right;

        x->right = y;
        y->left = T2;

        y->height = max(height(y->left), height(y->right)) + 1;
        x->height = max(height(x->left), height(x->right)) + 1;

        return x;
    }

    Node* leftRotate(Node* x) {
        Node* y = x->right;
        Node* T2 = y->left;

        y->left = x;
        x->right = T2;

        x->height = max(height(x->left), height(x->right)) + 1;
        y->height = max(height(y->left), height(y->right)) + 1;

        return y;
    }

    Node* insert(Node* node, string key, string meaning) {
        if (!node)
            return new Node(key, meaning);

        if (key < node->keyword)
            node->left = insert(node->left, key, meaning);
        else if (key > node->keyword)
            node->right = insert(node->right, key, meaning);
        else {
            cout << "Keyword already exists! Updating meaning.\n";
            node->meaning = meaning;
            return node;
        }

        node->height = 1 + max(height(node->left), height(node->right));
        int balance = getBalance(node);

        // Left Left
        if (balance > 1 && key < node->left->keyword)
            return rightRotate(node);

        // Right Right
        if (balance < -1 && key > node->right->keyword)
            return leftRotate(node);

        // Left Right
        if (balance > 1 && key > node->left->keyword) {
            node->left = leftRotate(node->left);
            return rightRotate(node);
        }

        // Right Left
        if (balance < -1 && key < node->right->keyword) {
            node->right = rightRotate(node->right);
            return leftRotate(node);
        }

        return node;
    }

    void inOrder(Node* root) {
        if (root) {
            inOrder(root->left);
            cout << root->keyword << " : " << root->meaning << endl;
            inOrder(root->right);
        }
    }

    void reverseInOrder(Node* root) {
        if (root) {
            reverseInOrder(root->right);
            cout << root->keyword << " : " << root->meaning << endl;
            reverseInOrder(root->left);
        }
    }

    int search(Node* node, string key, int& comparisons) {
        if (!node) return 0;

        comparisons++;
        if (key == node->keyword) {
            cout << "Found: " << node->meaning << endl;
            return comparisons;
        } else if (key < node->keyword) {
            return search(node->left, key, comparisons);
        } else {
            return search(node->right, key, comparisons);
        }
    }

public:
    AVL() {
        root = NULL;
    }

    void insert(string key, string meaning) {
        root = insert(root, key, meaning);
    }

    void displayBalanceFactors(Node* node) {
        if (node) {
            displayBalanceFactors(node->left);
            int balance = getBalance(node);
            cout << "Keyword: " << node->keyword << " | Balance Factor: " << balance << endl;
            displayBalanceFactors(node->right);
        }
    }

    void displayAllBalanceFactors() {
        cout << "\nBalance Factors of All Nodes:\n";
        displayBalanceFactors(root);
    }

    void displayAscending() {
        cout << "\nDictionary (Ascending Order):\n";
        inOrder(root);
    }

    void displayDescending() {
        cout << "\nDictionary (Descending Order):\n";
        reverseInOrder(root);
    }

    void searchWord(string key) {
        int comparisons = 0;
        int result = search(root, key, comparisons);
        if (result == 0)
            cout << "Keyword not found.\n";
        else
            cout << "Comparisons made: " << result << endl;
    }

    int getMaxComparisons() {
        return height(root); // Worst case is the height of the AVL tree
    }
};

// -------- Main Function with Menu --------
int main() {
    AVL dict;
    int choice;
    string word, meaning;

    do {
        cout << "\n========= Dictionary Menu =========\n";
        cout << "1. Add Keyword\n";
        cout << "2. Display (Ascending)\n";
        cout << "3. Display (Descending)\n";
        cout << "4. Search Keyword\n";
        cout << "5. Get Max Comparisons (Tree Height)\n";
        cout << "6. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;
        cin.ignore(); // Clear input buffer

        switch (choice) {
			case 1:
				cout << "Enter keyword: ";
				getline(cin, word);
				cout << "Enter meaning: ";
				getline(cin, meaning);
				dict.insert(word, meaning);
				dict.displayAllBalanceFactors(); // New line added to show balance factors
				break;
            case 2:
                dict.displayAscending();
                break;
            case 3:
                dict.displayDescending();
                break;
            case 4:
                cout << "Enter keyword to search: ";
                getline(cin, word);
                dict.searchWord(word);
                break;
            case 5:
                cout << "Max comparisons = Tree height = " << dict.getMaxComparisons() << endl;
                break;
            case 6:
                cout << "Exiting program. Goodbye!\n";
                break;
            default:
                cout << "Invalid choice. Try again.\n";
        }

    } while (choice != 6);

    return 0;
}
