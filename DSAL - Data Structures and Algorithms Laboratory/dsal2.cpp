/*
A dictionary stores keywords and its meanings.
Provide facility for adding new keywords, deleting keywords, updating values of any entry.
Provide facility to display whole data sorted in ascending/ Descending order.
Also find how many maximum comparisons may require for finding any keyword.
Use Binary Search Tree for implementation.
 */

#include<iostream>
#include<string>
using namespace std;

class Node{
	private:
		string keyword;
		string meaning;
		Node* left;
		Node* right;
	public:
		Node(string k, string m){
			keyword = k;
			meaning = m;
			left = right = nullptr;
		}
		friend class Dictionary;
};

class Dictionary{
	private:
		Node* root;

		Node* insert(Node* node, const string &keyword, const string &meaning){
			if(node == nullptr){
				return new Node(keyword, meaning);
			}
			if(keyword < node->keyword){
				node->left = insert(node->left,keyword,meaning);
			}
			else if(keyword > node->keyword){
				node->right = insert(node->right,keyword,meaning);
			}
			return node;
		}

		Node* find(Node* node, const string &keyword, int &comparisons){
		    comparisons = 0; // Initialize comparison counter
		    while (node != nullptr) {
		        comparisons++; // Increment for each comparison
		        if (node->keyword == keyword) {
		            return node; // Keyword found
		        }
		        if (keyword < node->keyword) {
		            node = node->left;
		        } else {
		            node = node->right;
		        }
		    }
		    return nullptr; // Keyword not found
		}


		Node* findMin(Node* node){
			while(node && node->left != nullptr){
				node = node->left;
			}
			return node;
		}

		Node* deleteNode(Node* node, const string &keyword){
			if(node == nullptr){
				return node;
			}
			if(keyword < node->keyword){
				node->left = deleteNode(node->left, keyword);
			}else if(keyword > node->keyword){
				node->right = deleteNode(node->right, keyword);
			}else{
				if(node->left == nullptr){
					Node* temp = node->right;
					delete node;
					return temp;
				}else if(node->right == nullptr){
					Node* temp = node->left;
					delete node;
					return temp;
				}
				Node* temp = findMin(node->right);

				node->keyword = temp->keyword;
				node->meaning = temp->meaning;

				node->right = deleteNode(node->right, temp->keyword);
			}

			return node;
		}

		void inorder(Node* node){
			if(node != nullptr){
				inorder(node->left);
				cout << node->keyword << " : " << node->meaning << endl;
				inorder(node->right);
			}
		}

		Node* update(Node* node, const string &keyword, const string &newMeaning){
			if(node == nullptr){
				return nullptr;
			}
			if(node->keyword == keyword){
				node->meaning = newMeaning;
				return node;
			}
			if(keyword < node->keyword){
				return update(node->left, keyword, newMeaning);
			}

			return update(node->right, keyword, newMeaning);
		}


		void reverseInorder(Node* node){
			if(node != nullptr){
				reverseInorder(node->right);
				cout << node->keyword << " : " << node->meaning << endl;
				reverseInorder(node->left);
			}
		}

	public:
		Dictionary(){
			root = nullptr;
		}

		void insertKeyword(const string &keyword, const string &meaning){
			root = insert(root, keyword, meaning);
		}

		string findKeyword(const string &keyword){
		    int comparisons = 0; // Initialize counter
		    Node* result = find(root, keyword, comparisons);
		    if (result != nullptr) {
		        cout << "Comparisons required: " << comparisons << endl;
		        return result->keyword + " : " + result->meaning;
		    } else {
		        cout << "Comparisons required: " << comparisons << endl;
		        return "Keyword not found!";
		    }
		}

		void deleteKeyword(const string &keyword) {
		    int comparisons = 0;
		    if (find(root, keyword, comparisons) == nullptr) {
		        cout << "Keyword not found!" << endl;
		        return;
		    }
		    root = deleteNode(root, keyword);
		    cout << "Comparisons required : " << comparisons << endl;
		    cout << "\nKeyword deleted successfully!" << endl;
		}

		void updateMeaning(const string &keyword, const string &newMeaning){
			Node* updatedNode = update(root, keyword, newMeaning);
			if(updatedNode == nullptr){
				cout << "Keyword not found!" << endl;
			}else{
				cout << "Meaning updated!" << endl;
			}
		}

		void displayAscending(){
			cout << "\nDictionary in Ascending Order : \n" << endl;
			inorder(root);
		}

		void displayDescending(){
			cout << "Dictionary in Descending Order : " << endl;
			reverseInorder(root);
		}


};

void displayMenu(){
	cout << "\n====================================\n";
	cout << "		Menu";
	cout << "\n====================================\n";
	cout << "1. Add new Keyword\n";
	cout << "2. Find Keyword\n";
	cout << "3. Update Meaning\n";
	cout << "4. Delete Keyword\n";
	cout << "5. Display Dictionary (Ascending)\n";
	cout << "6. Display Dictionary (Descending)\n";
	cout << "7. Exit\n";
	cout << "====================================\n";
}

int main(){
	Dictionary d;
	int choice;
	string keyword, meaning, newMeaning;

	while(true){
		displayMenu();
		cout << "\nEnter your choice : ";
		cin >> choice;

		switch(choice){
			case 1:
				cout << "\nEnter keyword to add :";
				cin >> keyword;
				cout << "Enter meaning : ";
				cin.ignore();
				getline(cin, meaning);
				d.insertKeyword(keyword, meaning);
				break;

			case 2:
				cout << "\nEnter keyword to find : ";
				cin >> keyword;
				cout << d.findKeyword(keyword) << endl;
				break;

			case 3:
				cout << "\nEnter keyword to update : ";
				cin >> keyword;
				cout << "Enter new meaning : ";
				cin.ignore();
				getline(cin, newMeaning);
				d.updateMeaning(keyword, newMeaning);
				break;

			case 4:
				cout << "\nEnter keyword to delete : ";
				cin >> keyword;
				d.deleteKeyword(keyword);
				break;

			case 5:
				d.displayAscending();
				break;

			case 6:
				d.displayDescending();
				break;

			case 7:
				cout << "\nExiting...\n";
				return 0;

			default:
				cout << "\nInvalid choice !\n";
				break;
		}
	}

	return 0;
}
