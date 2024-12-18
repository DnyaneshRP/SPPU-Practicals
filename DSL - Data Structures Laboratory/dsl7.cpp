/*
PRACTICAL - 7
Write C++ program for storing binary number using doubly linked lists. 
Write functions
a) To compute 1's and 2's complement 
b) Add two binary numbers
*/

#include<iostream>
using namespace std;

class Node{
    public:
        Node* prev;
        bool b;
        Node* next;
        Node(){
            prev = next = nullptr;
        }
        Node(bool n){
            b = n;
            prev = next = nullptr;
        }
};

class Binary{
    private:
        Node* head;
    public:
        Binary(){
            head = nullptr;
        }
        
        void generateBinary(int no);
        
        void addBitAtBegin(bool val);
        
        void displayBinary();

        void onesComplement();

        void twosComplement();

        Binary operator +(Binary n1);
     
};

void Binary :: generateBinary(int no){
    head = nullptr;
    bool rem;
    while(no != 0){
        rem = no % 2;
        no = no / 2;
        addBitAtBegin(rem);
    }
}

void Binary :: addBitAtBegin(bool val){
    Node* newNode = new Node(val);
    if(head == nullptr){
        head = newNode;
    }else{
        newNode->next = head;
        head->prev = newNode;
        head = newNode;
    }
}

void Binary :: displayBinary(){
    Node* t = head;
    while(t != nullptr){
        cout << (t -> b) << " ";
        t = t -> next;
    }
    cout << endl;
}

void Binary :: onesComplement(){
    Node* t = head;
    while(t != nullptr){
        if((t -> b) == 0){
            t -> b = 1;
        }else{
            t -> b = 0;
        }
        t = t -> next;
    }
}

void Binary :: twosComplement(){
    onesComplement();
    bool carry = 1;
    Node* t = head;
    while(t -> next != nullptr){
        t = t -> next;
    }
    while(t != nullptr){
        if((t -> b) == 1 && carry == 1){
            t -> b = 0;
            carry = 1;
        }else if((t -> b) == 0 && carry == 1){
            t -> b = 1;
            carry = 0;
        }else if(carry == 0){
            break;
        }
        t = t -> prev;
    }
    if (carry == 1) {
        addBitAtBegin(carry);
    }
}

Binary Binary :: operator +(Binary n1){
    Binary sum;
    Node* a = head;
    Node* b = n1.head;
    bool s = 0;
    bool carry = 0;
    while(a != nullptr && a -> next != nullptr){
        a = a -> next;
    }
    while(b != nullptr && b -> next != nullptr){
        b = b -> next;
    }
    while(a != nullptr || b != nullptr){
        bool bitA = (a != nullptr) ? (a -> b) : 0;
        bool bitB = (b != nullptr) ? (b -> b) : 0;
        s = (bitA^bitB^carry);
        carry = ((bitA && bitB) || (bitA && carry) || (bitB && carry));
        sum.addBitAtBegin(s);
        if (a != nullptr) 
            a = a -> prev;
        if (b != nullptr) 
            b = b -> prev;
    }
    if(carry){
        sum.addBitAtBegin(carry);
    }
    return sum;
}

int main(){
    int n1,n2;
    Binary b1,b2,b3;
    while(true){
        cout << "\n-----------------------------------------------";
        cout << "\n                    MENU\n";
        cout << "-----------------------------------------------\n";
        cout << "\n1. Generate Binary Number";
        cout << "\n2. One's complement";
        cout << "\n3. Two's complement";
        cout << "\n4. Add two Binary Numbers : ";
        cout << "\n5. Exit\n";
        
        int ch;
        cout << "\nEnter your choice : ";
        cin >> ch;

        switch(ch){
            case 1:{
                cout << "\nGenerate Binary Number\n";
                cout << "\nEnter a number in decimal form : ";
                cin >> n1;
                b1.generateBinary(n1);
                cout << "\nBinary Representation : ";
                b1.displayBinary();
                break;
            }
            case 2:{
                cout << "\nOne's Complement\n";
                cout << "\nEnter a number in decimal form : ";
                cin >> n1;
                b1.generateBinary(n1);
                cout << "\nBinary Representation : ";
                b1.displayBinary();
                b1.onesComplement();
                cout << "\nOne's Complement : ";
                b1.displayBinary();
                break;
            }
            case 3:{
                cout << "\nTwo's Complement\n";
                cout << "\nEnter a number in decimal form : ";
                cin >> n1;
                b1.generateBinary(n1);
                cout << "\nBinary Representation : ";
                b1.displayBinary();
                b1.twosComplement();
                cout << "\nTwo's Complement : ";
                b1.displayBinary();
                break;
            }
            case 4:{
                cout << "\nAdd two Binary Numbers\n";
                cout << "Enter two numbers : \n";
                cin >> n1 >> n2;
                b1.generateBinary(n1);
                b2.generateBinary(n2);
                cout << "\nAddition of binary numbers : \n";
                cout << "Binary Number one : \t";
                b1.displayBinary();
                cout << "Binary Number two : \t";
                b2.displayBinary();
                cout << "Result : \t\t";
                b3 = b1 + b2;
                b3.displayBinary();
                break;
            }
            case 5:{
                cout << "\nExiting...\n";
                exit(0);
                break;
            }
            default:
                cout << "\nInvalid choice !\n";
        }
    }

    return 0;
}