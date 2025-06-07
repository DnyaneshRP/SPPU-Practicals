/*
Implement all the functions of a dictionary (ADT) using open hashing technique: 
separate chaining using linked list 
Data: Set of (key, value) pairs, Keys are mapped to values, Keys must be comparable, and Keys must be unique. 
Standard Operations: Insert(key, value), Find(key), Delete(key)
*/

#include <iostream>
#include <string>

using namespace std;

// Node structure for the linked list
struct Node {
    string key;
    string value;
    Node* next;

    Node(const string& k, const string& v) : key(k), value(v), next(nullptr) {}
};

// Custom linked list for separate chaining
class LinkedList {
private:
    Node* head;

public:
    LinkedList() : head(nullptr) {}

    // Insert a key-value pair into the linked list
    void insert(const string& key, const string& value) {
        Node* newNode = new Node(key, value);
        if (!head) {
            head = newNode;
        } else {
            Node* temp = head;
            while (temp->next) {
                if (temp->key == key) {
                    temp->value = value; // Update value if key exists
                    delete newNode;
                    return;
                }
                temp = temp->next;
            }
            if (temp->key == key) {
                temp->value = value; // Update value if key exists
                delete newNode;
                return;
            }
            temp->next = newNode;
        }
    }

    // Find the value associated with a key
    string find(const string& key) {
        Node* temp = head;
        while (temp) {
            if (temp->key == key) {
                return temp->value;
            }
            temp = temp->next;
        }
        return ""; // Return empty string if key not found
    }

    // Delete a key-value pair from the linked list
    void remove(const string& key) {
        if (!head) return;

        if (head->key == key) {
            Node* temp = head;
            head = head->next;
            delete temp;
            return;
        }

        Node* prev = head;
        Node* curr = head->next;
        while (curr) {
            if (curr->key == key) {
                prev->next = curr->next;
                delete curr;
                return;
            }
            prev = curr;
            curr = curr->next;
        }
    }

    // Display the linked list
    void display() {
        Node* temp = head;
        while (temp) {
            cout << "[" << temp->key << ": " << temp->value << "] ";
            temp = temp->next;
        }
    }

    // Destructor to clean up memory
    ~LinkedList() {
        Node* temp;
        while (head) {
            temp = head;
            head = head->next;
            delete temp;
        }
    }
};

// Dictionary class using open hashing with custom linked list
class Dictionary {
private:
    static const int HASH_TABLE_SIZE = 10; // Size of the hash table
    LinkedList hashTable[HASH_TABLE_SIZE]; // Array of custom linked lists

    // Hash function to map a key to an index in the hash table
    int hashFunction(const string& key) {
        int hash = 0;
        for (char ch : key) {
            hash += ch;
        }
        return hash % HASH_TABLE_SIZE;
    }

public:
    // Insert a key-value pair into the dictionary
    void insert(const string& key, const string& value) {
        int index = hashFunction(key);
        hashTable[index].insert(key, value);
        cout << "Key-Value pair inserted successfully." << endl;
    }

    // Find the value associated with a key
    void find(const string& key) {
        int index = hashFunction(key);
        string value = hashTable[index].find(key);
        if (!value.empty()) {
            cout << "Key found. Value: " << value << endl;
        } else {
            cout << "Key not found." << endl;
        }
    }

    // Delete a key-value pair from the dictionary
    void remove(const string& key) {
        int index = hashFunction(key);
        hashTable[index].remove(key);
        cout << "Key-Value pair deleted successfully." << endl;
    }

    // Display the entire dictionary
    void display() {
        for (int i = 0; i < HASH_TABLE_SIZE; ++i) {
            cout << "Bucket " << i << ": ";
            hashTable[i].display();
            cout << endl;
        }
    }
};

// Menu-driven program
int main() {
    Dictionary dict;
    int choice;
    string key, value;

    do {
        cout << "\nDictionary Menu:\n";
        cout << "1. Insert\n";
        cout << "2. Find\n";
        cout << "3. Delete\n";
        cout << "4. Display\n";
        cout << "5. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                cout << "Enter key: ";
                cin >> key;
                cout << "Enter value: ";
                cin >> value;
                dict.insert(key, value);
                break;
            case 2:
                cout << "Enter key to find: ";
                cin >> key;
                dict.find(key);
                break;
            case 3:
                cout << "Enter key to delete: ";
                cin >> key;
                dict.remove(key);
                break;
            case 4:
                dict.display();
                break;
            case 5:
                cout << "Exiting program." << endl;
                break;
            default:
                cout << "Invalid choice. Please try again." << endl;
        }
    } while (choice != 5);

    return 0;
}