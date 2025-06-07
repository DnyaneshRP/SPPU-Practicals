/*
Department maintains a student information.
The file contains roll number, name, division and address.
Allow user to add, delete information of student. Display information of particular student.
If record of student does not exist an appropriate message is displayed.
If it is, then the system displays the student details.
Use sequential file to maintain the data.
 */

#include <iostream>
#include <fstream>
using namespace std;

class Student {
    int roll;
    char name[50];
    char division;
    char address[100];

public:
    void input() {
        cout << "\nEnter Roll Number: ";
        cin >> roll;
        cin.ignore();
        cout << "Enter Name: ";
        cin.getline(name, 50);
        cout << "Enter Division: ";
        cin >> division;
        cin.ignore();
        cout << "Enter Address: ";
        cin.getline(address, 100);
    }

    void display() const {
    	cout << roll << "\t" << name << "\t" << division << "\t" << address << "\n";
    }

    int getRoll() const {
        return roll;
    }
};

void addStudent() {
    Student s;
    fstream file;
    file.open("students.dat", ios::binary | ios::app | ios::out);
    s.input();
    file.write((char*)&s, sizeof(s));
    file.close();
    cout << "Student added successfully.\n";
}

void displayAll() {
    Student s;
    fstream file;
    file.open("students.dat", ios::binary | ios::in);
    if (!file) {
        cout << "File not found.\n";
        return;
    }
    cout << "\nRoll\tName\t\tDiv\tAddress\n";
    cout << "---------------------------------------------\n";
    while (file.read((char*)&s, sizeof(s))) {
        s.display();
    }
    file.close();
}

void searchStudent() {
    int roll;
    bool found = false;
    Student s;
    cout << "\nEnter roll number to search: ";
    cin >> roll;

    fstream file;
    file.open("students.dat", ios::binary | ios::in);
    while (file.read((char*)&s, sizeof(s))) {
        if (s.getRoll() == roll) {
            s.display();
            found = true;
            break;
        }
    }
    file.close();
    if (!found)
        cout << "\nStudent with roll number " << roll << " not found.\n";
}

void deleteStudent() {
    int roll;
    bool found = false;
    cout << "\nEnter roll number to delete: ";
    cin >> roll;

    Student s;
    fstream file, temp;
    file.open("students.dat", ios::binary | ios::in);
    temp.open("temp.dat", ios::binary | ios::out);

    while (file.read((char*)&s, sizeof(s))) {
        if (s.getRoll() == roll) {
            found = true;
            continue; // skip writing this record
        }
        temp.write((char*)&s, sizeof(s));
    }

    file.close();
    temp.close();

    remove("students.dat");
    rename("temp.dat", "students.dat");

    if (found)
        cout << "\nStudent record deleted successfully.\n";
    else
        cout << "\nStudent with roll number " << roll << " not found.\n";
}
/*
int main() {
    int choice;
    do {
        cout << "\n--- Student Record Management ---\n";
        cout << "1. Add Student\n";
        cout << "2. Delete Student\n";
        cout << "3. Search Student\n";
        cout << "4. Display All Students\n";
        cout << "5. Exit\n";
        cout << "\nEnter your choice: ";
        cin >> choice;

        switch (choice) {
        case 1: addStudent(); break;
        case 2: deleteStudent(); break;
        case 3: searchStudent(); break;
        case 4: displayAll(); break;
        case 5: cout << "Exiting...\n"; break;
        default: cout << "Invalid choice. Try again.\n";
        }
    } while (choice != 5);

    return 0;
}
*/
