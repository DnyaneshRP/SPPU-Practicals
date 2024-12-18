/*
PRACTICAL - 11
Queues are frequently used in computer programming, and a typical example is the
creation of a job queue by an operating system. If the operating system does not 
use priorities, then the jobs are processed in the order they enter the system. 
Write C++ program for simulating job queue. Write functions to add job and delete 
job from queue. 
*/

#include<iostream>
using namespace std;

class JobQueue{
    private:
        string* queue;
        int front,rear,maxSize;
    public:
        JobQueue(int size){
            maxSize = size;
            queue = new string[maxSize];
            front = rear = -1;
        }
        ~JobQueue(){
            delete[] queue;
        }
        bool isFull(){
            return (rear == maxSize - 1);
        }
        bool isEmpty(){
            return (front == -1 || front > rear);
        }
        void addJob(){
            if(isFull()){
                cout << "\nJob Queue is full.\n";
                return;
            }
            if(front == -1){
                front = 0;
            }
            string job;
            cout << "\nEnter job description : ";
            cin >> job;
            queue[++rear] = job;
            cout << "\nJob added to queue : " << job << endl;
        }
        void deleteJob(){
            if(isEmpty()){
                cout << "\nJob Queue is empty.\n";
                return;
            }
            cout << "Job deleted from queue : " << queue[front++] << endl;
        }
        void displayJobs(){
            if(isEmpty()){
                cout << "\nJob Queue is empty.\n";
                return;
            }
            cout << "\nJobs in queue : \n\n";
            for(int i = front; i <= rear; i++){
                cout << queue[i] << "\t";
            }
            cout << endl;
        }
};

int main(){
    int size,choice;
    cout << "\nEnter size of Job Queue : ";
    cin >> size;
    JobQueue q(size);

    while(true){
        cout << "\n-----------------------------------------";
        cout << "\n                  MENU\n";
        cout << "-----------------------------------------\n";
        cout << "\n1. Add job";
        cout << "\n2. Delete job";
        cout << "\n3. Display jobs";
        cout << "\n4. Exit\n";

        cout << "\nEnter your choice : ";
        cin >> choice;

        switch(choice){
            case 1:{
                q.addJob();
                break;
            }
            case 2:{
                q.deleteJob();
                break;
            }
            case 3:{
                q.displayJobs();
                break;
            }
            case 4:{
                cout << "\nExiting...\n";
                exit(0);
            }
            default:{
                cout << "\nInvalid choice !\n";
                break;
            }
        }

    }
    return 0;
}