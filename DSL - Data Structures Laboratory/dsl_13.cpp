/*
PRACTICAL - 13
A double-ended queue (deque) is a linear list in which additions and 
deletions may be made at either end. Obtain a data representation mapping 
a deque into a one dimensional array. Write C++ program to simulate deque 
with functions to add and delete elements from either end of the deque.
*/

#include<iostream>
using namespace std;

class Dequeue{
    private:
        int* arr;
        int front, rear, capacity;
        bool isInputRestricted;
    public:
        Dequeue(int size,bool mode){
            capacity = size;
            arr = new int[capacity];
            front = rear = -1;
            isInputRestricted = mode;
        }

        bool isFull(){
            return (((rear+1)%capacity) == front);
        }

        bool isEmpty(){
            return (front == -1);
        }

        void addRear(){
            if(isFull()){
                cout << "\nDequeue is full !\n";
                return;
            }
            int value;
            cout << "\nEnter value to add at rear : ";
            cin >> value;            
            if(isEmpty()){
                front = rear = 0;
                arr[rear] = value;
            }else{
                rear = (rear+1) % capacity;
                arr[rear] = value;
            }
        }

        void addFront(){
            if(isFull()){
                cout << "\nDequeue is full !\n";
                return;
            }
            int value;
            cout << "\nEnter value to add at front : ";
            cin >> value;
            if(isEmpty()){
                front = rear = 0;
                arr[front] = value;
            }else{
                front = (front == 0) ? capacity - 1 : front - 1;
                arr[front] = value;

            }
        }

        void deleteFront(){
            if(isEmpty()){
                cout << "\nDequeue is empty !\n";
                return;
            }
            cout << "\nDeleted from front : " << arr[front] << endl;
            if(front == rear){
                front = rear = -1;
            }else{
                front = (front+1) % capacity;
            }
        }

        void deleteRear(){
            if(isEmpty()){
                cout << "\nDequeue is empty !\n";
                return;
            }
            cout << "\nDeleted from rear : " << arr[rear] << endl;
            if(front == rear){
                front = rear = -1;
            }else{
                rear = (rear == 0) ? capacity - 1 : rear - 1;
            }
        }

        void display(){
            if(isEmpty()){
                cout << "\nDequeue is empty !\n";
                return;
            }
            cout << "\nDequeue contains : \n\n";
            int i = front;
            while(i != rear){
                cout << arr[i] << "\t";
                i = (i+1) % capacity;
            }
            cout << arr[rear] << endl;
        }

        ~Dequeue(){
            delete[] arr;
        }
};

int main(){
    int size,choice,value,typech;

    cout << "\nEnter size of dequeue : ";
    cin >> size;

    cout << "\nChoose type of Dequeue : ";
    cout << "\n1. Input-Restricted Dequeue (I/P : only rear, O/P : both ends)";
    cout << "\n2. Output-Restricted Dequeue (I/P : both ends, O/P : only front)\n\n";
    cin >> typech;

    bool isInputRestrcited = (typech == 1);

    Dequeue dq(size,isInputRestrcited);

    while(true){
        cout << "\n----------------------------------------------";
        cout << "\n                    MENU\n";
        cout << "----------------------------------------------\n";
        cout << "\n1. Add element at front";
        cout << "\n2. Add element at rear";
        cout << "\n3. Delete element from front";
        cout << "\n4. Delete element from rear";
        cout << "\n5. Display dequeue";
        cout << "\n6. Exit\n";

        cout << "\nEnter your choice : ";
        cin >> choice;

        switch(choice){
            case 1:{
                if(!isInputRestrcited){
                    dq.addFront();
                }else{
                    cout << "\nCannot add at front in Input-Restricted Dequeue !\n";
                }
                break;
            }
            case 2:{
                dq.addRear();
                break;
            }
            case 3:{
                dq.deleteFront();
                break;
            }
            case 4:{
                if(isInputRestrcited){
                    dq.deleteRear();
                }else{
                    cout << "\nCannot delete from rear in Output-Restricted Dequeue !\n";
                }
                break;
            }
            case 5:{
                dq.display();
                break;
            }
            case 6:{
                cout << "\nExiting....\n";
                exit(0);
                break;
            }
            default:{
                cout << "\nInvalid choice !\n";
            }
        }
    }
}

