/*
Consider telephone book database of N clients.
Make use of a hash table implementation to quickly look up client's telephone number.
Make use of two collision handling techniques and compare them using number of comparisons
required to find a set of telephone numbers
(Note: Use linear probing with replacement and without replacement)
*/

#include <iostream>
#include <string>
using namespace std;

const int TABLE_SIZE = 10;

class Client {
	public:
		string name;
		string phoneNumber;
		bool isDeleted;

		Client(){
			name = "";
			phoneNumber = "";
			isDeleted = false;
		}

		Client(string n, string p){
			name = n;
			phoneNumber = p;
			isDeleted = false;
		}
};

class HashTable {
	private:
		Client table[TABLE_SIZE];

		int hashFunction(string key) {
			int hash = 0;
			for (char ch : key) {
				hash += ch;
			}
			return hash % TABLE_SIZE;
		}

	public:

		void insertWithoutReplacement(string name, string phoneNumber) {
			int index = hashFunction(name);
			int originalIndex = index;

			while (table[index].name != "" && !table[index].isDeleted) {
				index = (index + 1) % TABLE_SIZE;
				if (index == originalIndex) {
					cout << "Hash table is full!" << endl;
					return;
				}
			}

			table[index] = Client(name, phoneNumber);
		}

		void insertWithReplacement(string name, string phoneNumber) {
			int index = hashFunction(name);
			int originalIndex = index;

			if (table[index].name != "" && hashFunction(table[index].name) != index) {
				Client temp = table[index];
				table[index] = Client(name, phoneNumber);
				name = temp.name;
				phoneNumber = temp.phoneNumber;
				index = (index + 1) % TABLE_SIZE;
			}

			while (table[index].name != "" && !table[index].isDeleted) {
				index = (index + 1) % TABLE_SIZE;
				if (index == originalIndex) {
					cout << "Hash table is full!" << endl;
					return;
				}
			}

			table[index] = Client(name, phoneNumber);
		}

		pair<int, string> search(string name) {
			int index = hashFunction(name);
			int originalIndex = index;
			int comparisons = 0;

			while (table[index].name != name && table[index].name != "") {
				index = (index + 1) % TABLE_SIZE;
				comparisons++;
				if (index == originalIndex) {
					return make_pair(-1, ""); // Client not found
				}
			}

			if (table[index].name == name && !table[index].isDeleted) {
				return make_pair(comparisons + 1, table[index].phoneNumber); // Return comparisons and phone number
			} else {
				return make_pair(-1, ""); // Client not found
			}
		}

		void remove(string name) {
			int index = hashFunction(name);
			int originalIndex = index;

			while (table[index].name != name && table[index].name != "") {
				index = (index + 1) % TABLE_SIZE;
				if (index == originalIndex) {
					cout << "Client not found!" << endl;
					return;
				}
			}

			if (table[index].name == name) {
				table[index].isDeleted = true;
				cout << "Client removed successfully!" << endl;
			} else {
				cout << "Client not found!" << endl;
			}
		}

		void display() {
			for (int i = 0; i < TABLE_SIZE; i++) {
				if (table[i].name != "" && !table[i].isDeleted) {
					cout << "Index " << i << ": " << table[i].name << " - " << table[i].phoneNumber << endl;
				} else {
					cout << "Index " << i << ": -" << endl; // Show empty locations as '-'
				}
			}
		}
};

int main() {
    HashTable htWithoutReplacement, htWithReplacement;
    int choice;
    string name, phoneNumber;
    pair<int, string> resultWithout;
    pair<int, string> resultWith;

    do {
        cout << "\n================ Menu ===============\n";
        cout << "1. Insert Client (Without Replacement)\n";
        cout << "2. Insert Client (With Replacement)\n";
        cout << "3. Search Client\n";
        cout << "4. Remove Client\n";
        cout << "5. Display Hash Table\n";
        cout << "6. Exit\n";
        cout << "\nEnter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                cout << "Enter client name: ";
                cin >> name;
                cout << "Enter client phone number: ";
                cin >> phoneNumber;
                htWithoutReplacement.insertWithoutReplacement(name, phoneNumber);
                break;
            case 2:
                cout << "Enter client name: ";
                cin >> name;
                cout << "Enter client phone number: ";
                cin >> phoneNumber;
                htWithReplacement.insertWithReplacement(name, phoneNumber);
                break;
            case 3:{
                cout << "Enter client name to search: ";
                cin >> name;
                resultWithout = htWithoutReplacement.search(name);
                resultWith = htWithReplacement.search(name);
                if (resultWithout.first != -1) {
                   cout << "Client found in Without Replacement table with " << resultWithout.first << " comparisons.\n";
                   cout << "Phone Number: " << resultWithout.second << endl;
                } else {
                	cout << "Client not found in Without Replacement table.\n";
                }
                if (resultWith.first != -1) {
                	cout << "Client found in With Replacement table with " << resultWith.first << " comparisons.\n";
                	cout << "Phone Number: " << resultWith.second << endl;
                } else {
                	cout << "Client not found in With Replacement table.\n";
                }
                break;
            }
            case 4:
                cout << "Enter client name to remove: ";
                cin >> name;
                htWithoutReplacement.remove(name);
                htWithReplacement.remove(name);
                break;
            case 5:
                cout << "Hash Table (Without Replacement):\n";
                htWithoutReplacement.display();
                cout << "\nHash Table (With Replacement):\n";
                htWithReplacement.display();
                break;
            case 6:
                cout << "Exiting...\n";
                break;
            default:
                cout << "Invalid choice! Please try again.\n";
        }
    } while (choice != 6);

    return 0;
}
