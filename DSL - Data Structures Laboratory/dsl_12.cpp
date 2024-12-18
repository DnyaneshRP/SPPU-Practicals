/*
PRACTICAL - 12
Write program to implement a priority queue in C++ using an inorder list to store the
items in the queue. Create a class that includes the data items (which should be template)
and the priority (which should be int). The inorder list should contain these objects, with
operator <= overloaded so that the items with highest priority appear at the beginning
of the list (which will make it relatively easy to retrieve the highest item.)
*/

#include<iostream>
using namespace std;

template <class T>
class Node{
    public:
        T data;
        int priority;
        Node* next;
        Node(T d, int p){
            data = d;
            priority = p;
            next = nullptr;
        }
        bool operator <=(const Node<T> &other){
            return priority <= other.priority;
        }
        
};

template <class T>
class PriorityQueue{
    private:
        Node<T>* front;
    public:
        PriorityQueue(){
            front = nullptr;
        }
        bool isEmpty(){
            return front == nullptr;
        }
        void insert(T d, int p){
            Node<T>* newNode = new Node<T>(d,p);
            if(isEmpty()){
                front = newNode;
            }else{
                Node<T>* current = front;
                Node<T>* prev = nullptr;
                while(current != nullptr && *current <= *newNode){
                    prev = current;
                    current = current -> next;
                }
                if(prev == nullptr){
                    newNode -> next = front;
                    front = newNode;
                }else{
                    newNode -> next = current;
                    prev -> next = newNode;
                }
            }
        }
        void remove(){
            if(isEmpty()){
                cout << "\nPriority Queue is empty.\n";
                return;
            }
            Node<T>* temp = front;
            front = front -> next;
            cout << "\nRemoved : " << temp -> data << endl;
            delete temp;
        }
        void display(){
            if(isEmpty()){
                cout << "\nPriority Queue is empty.\n";
                return;
            }
            Node<T>* temp = front;
            cout << "\nPriority Queue : \n\n";
            while(temp != nullptr){
                cout << "(" << temp -> priority << " : " << temp -> data << ")\t";
                temp = temp -> next;
            }
            cout << endl;
        }
        ~PriorityQueue(){
            while(!isEmpty()){
                remove();
            }
        }
};

int main(){
    PriorityQueue<string> pq;
    string data;
    int priority;
    int choice;

    while(true){
        cout << "\n----------------------------------------------";
        cout << "\n                    MENU\n";
        cout << "----------------------------------------------\n";
        cout << "\n1. Insert item";
        cout << "\n2. Delete item";
        cout << "\n3. Display priority queue";
        cout << "\n4. Exit\n";

        cout << "\nEnter your choice : ";
        cin >> choice;

        switch(choice){
            case 1:{
                cout << "\nEnter data : ";
                cin >> data;
                cout << "Enter priority : ";
                cin >> priority;
                pq.insert(data,priority);
                break;
            }
            case 2:{
                pq.remove();
                break;
            }
            case 3:{
                pq.display();
                break;
            }
            case 4:{
                cout << "\nExiting....\n";
                exit(0);
                break;
            }
            default:{
                cout << "\nInvalid choice !\n";
            }
        }
    }

    return 0;
}
