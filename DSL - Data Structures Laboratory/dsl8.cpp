/*
PRACTICAL - 8
Second year Computer Engineering class, 
set A of students like Vanilla Ice-cream and 
set B of students like Butterscotch ice-cream. 
Write C++ program to store two sets using linked list.
Compute and display
a) Set of students who like both vanilla and butterscotch
b) Set of students who like either vanilla or butterscotch or not both
c) Number of students who like neither vanilla nor butterscotch
*/

#include <iostream>
using namespace std;

class Node{
public:
    int studentID;
    Node* next;

    Node(){
        next = nullptr;
    }
    Node(int id){
        studentID = id;
        next = nullptr;
    }
};

class Set {
    private:
        Node* head;
    public:
        Set(){
            head = nullptr;
        }

        void addStudent(int id){
            if (contains(id)){
                return;
            }
            Node* newNode = new Node(id);
            if (head == nullptr){
                head = newNode;
            } else {
                Node* temp = head;
                while (temp -> next != nullptr){
                    temp = temp -> next;
                }
                temp -> next = newNode;
            }
        }

        void displaySet(){
            if (head == nullptr){
                cout << "Set is empty." << endl;
                return;
            }
            Node* temp = head;
            while (temp != nullptr){
                cout << (temp -> studentID) << "  ";
                temp = temp -> next;
            }
            cout << endl;
        }

        bool contains(int id){
            Node* temp = head;
            while (temp != nullptr){
                if (temp -> studentID == id) {
                    return true;
                }
                temp = temp -> next;
            }
            return false;
        }

        Set unionSets(Set &other){
            Set result;
            Node* temp = head;
            while (temp != nullptr){
                result.addStudent(temp -> studentID);
                temp = temp -> next;
            }
            temp = other.head;
            while (temp != nullptr){
                if (!contains(temp -> studentID)){
                    result.addStudent(temp -> studentID);
                }
                temp = temp -> next;
            }
            return result;
        }

        Set intersectionSets(Set &other){
            Set result;
            Node* temp = head;
            while (temp != nullptr){
                if (other.contains(temp -> studentID)){
                    result.addStudent(temp -> studentID);
                }
                temp = temp -> next;
            }
            return result;
        }

        Set differenceSets(Set &other){
            Set result;
            Node* temp = head;
            while (temp != nullptr){
                if (!other.contains(temp->studentID)){
                    result.addStudent(temp -> studentID);
                }
                temp = temp -> next;
            }
            return result;
        }

        Set symmetricDifferenceSets(Set &other){
            Set unionSet = unionSets(other);
            Set intersectionSet = intersectionSets(other);
            return unionSet.differenceSets(intersectionSet);
        }

        Set complement(Set &universalSet){
            Set result;
            Node* temp = universalSet.head;
            while (temp != nullptr) {
                if (!contains(temp -> studentID)) {
                    result.addStudent(temp -> studentID);
                }
                temp = temp -> next;
            }
            return result;
        }

        int count(){
            int count = 0;
            Node* temp = head;
            while (temp != nullptr){
                count++;
                temp = temp -> next;
            }
            return count;
        }

};

void displayMenu() {
    cout << "\n-----------------------------------------------------------------------";
    cout << "\n                                 MENU";
    cout << "\n-----------------------------------------------------------------------\n";
    cout << "\n1. Set of students who like both Vanilla and Butterscotch";
    cout << "\n2. Set of students who like either vanilla or butterscotch or not both";
    cout << "\n3. Number of students who like neither Vanilla nor Butterscotch";
    cout << "\n4. Exit\n";
    cout << "\nEnter your choice: ";
}

int main() {
    Set uSet, vSet, bSet;
    int uSize, vSize, bSize, id, choice;

    cout << "\nEnter the total number of students in second year C.E. : \n";
    cin >> uSize;
    cout << "\nEnter student IDs of second year C.E. :\n";
    for (int i = 1; i <= uSize; i++){
        cin >> id;
        uSet.addStudent(id);
    }

    cout << "\nEnter number of students who like Vanilla Ice-cream: \n";
    cin >> vSize;
    cout << "\nEnter student IDs who like Vanilla Ice-cream:\n";
    for (int i = 1; i <= vSize; i++) {
        cin >> id;
        vSet.addStudent(id);
    }

    cout << "\nEnter number of students who like Butterscotch Ice-cream: \n";
    cin >> bSize;
    cout << "\nEnter student IDs who like Butterscotch Ice-cream:\n";
    for (int i = 1; i <= bSize; i++) {
        cin >> id;
        bSet.addStudent(id);
    }

    cout << "\nSecond Year Students are : \n";
    uSet.displaySet();

    cout << "\nStudents who like Vanila flavour : \n";
    vSet.displaySet();

    cout << "\nStudents who like ButterScotch flavour :\n";
    bSet.displaySet();

    while (true){
        displayMenu();
        cin >> choice;

        switch (choice){
            case 1:{
                Set vbSet = vSet.intersectionSets(bSet);
                cout << "\nSet of students who like both Vanilla and Butterscotch :\n";
                vbSet.displaySet();
                break;
            }
            case 2:{
                Set evbSet = vSet.symmetricDifferenceSets(bSet);
                cout << "\nSet of students who like either Vanilla or Butterscotch, but not both:\n";
                evbSet.displaySet();
                break;
            }
            case 3:{
                Set unionSet = vSet.unionSets(bSet);
                Set complementSet = unionSet.complement(uSet);
                cout << "\nStudents who like neither Vanilla nor Butterscotch :\n";
                complementSet.displaySet();
                cout << "\nNumber of students : " << complementSet.count() << endl;
                break;
            }
            case 4:{
                cout << "\nExiting...\n";
                exit(0);
            }
            default:
                cout << "\nInvalid choice !\n";
        }
    }

    return 0;
}