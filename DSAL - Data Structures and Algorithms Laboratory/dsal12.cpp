/*
Implementation of a direct access file -
Insertion and deletion of a record from a direct access file
*/

#include <iostream>
#include <fstream>
#include <string.h>
using namespace std;

const int TABLE_SIZE = 10;
const char FILENAME[] = "employee.dat";

class Employee {
public:
    int id;
    char name[20];
    float salary;
    bool isDeleted;

    Employee() {
        id = -1;               // Default: empty slot
        strcpy(name, "");
        salary = 0.0;
        isDeleted = false;
    }
};

// Simple hash function
int getHash(int id) {
    return id % TABLE_SIZE;
}

// Function to create the file with empty records
void createEmptyFile() {
    ofstream file(FILENAME, ios::binary);
    Employee emp;
    for (int i = 0; i < TABLE_SIZE; i++) {
        file.write((char*)&emp, sizeof(emp));
    }
    file.close();
}

// Insert a new employee
void insertEmployee() {
    Employee emp;
    cout << "Enter Employee ID: ";
    cin >> emp.id;
    cin.ignore();
    cout << "Enter Name: ";
    cin.getline(emp.name, 20);
    cout << "Enter Salary: ";
    cin >> emp.salary;

    emp.isDeleted = false;

    fstream file(FILENAME, ios::in | ios::out | ios::binary);
    int index = getHash(emp.id);

    file.seekg(index * sizeof(emp));
    Employee temp;
    file.read((char*)&temp, sizeof(temp));

    if (temp.id == -1 || temp.isDeleted) {
        file.seekp(index * sizeof(emp));
        file.write((char*)&emp, sizeof(emp));
        cout << "Employee inserted successfully.\n";
    } else {
        cout << "Slot already occupied. Try another ID.\n";
    }

    file.close();
}

// Delete an employee by ID
void deleteEmployee() {
    int id;
    cout << "Enter Employee ID to delete: ";
    cin >> id;

    fstream file(FILENAME, ios::in | ios::out | ios::binary);
    int index = getHash(id);

    file.seekg(index * sizeof(Employee));
    Employee emp;
    file.read((char*)&emp, sizeof(emp));

    if (emp.id == id && !emp.isDeleted) {
        emp.isDeleted = true;
        file.seekp(index * sizeof(Employee));
        file.write((char*)&emp, sizeof(emp));
        cout << "Employee deleted successfully.\n";
    } else {
        cout << "Employee not found.\n";
    }

    file.close();
}

// Display all records
void displayAllRecords() {
    ifstream file(FILENAME, ios::binary);
    Employee emp;

    cout << "\n--- Employee Records ---\n";
    for (int i = 0; i < TABLE_SIZE; i++) {
        file.read((char*)&emp, sizeof(emp));
        cout << "Slot " << i << ": ";
        if (emp.id != -1 && !emp.isDeleted) {
            cout << "ID: " << emp.id << ", Name: " << emp.name << ", Salary: " << emp.salary << "\n";
        } else {
            cout << "Empty or Deleted\n";
        }
    }
    file.close();
}

int main() {
    // Create file only once if it doesn't exist
    ifstream check(FILENAME);
    if (!check) {
        createEmptyFile();
    }
    check.close();

    int choice;
    do {
        cout << "\n=== Employee File Menu ===\n";
        cout << "1. Insert Employee\n";
        cout << "2. Delete Employee\n";
        cout << "3. Display All\n";
        cout << "4. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1: insertEmployee(); break;
            case 2: deleteEmployee(); break;
            case 3: displayAllRecords(); break;
            case 4: cout << "Exiting...\n"; break;
            default: cout << "Invalid choice. Try again.\n";
        }
    } while (choice != 4);

    return 0;
}
