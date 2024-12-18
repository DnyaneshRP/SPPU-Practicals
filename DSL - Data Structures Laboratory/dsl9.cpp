/*
PRACTICAL - 9
In any language program mostly syntax error occurs due to unbalancing delimiter
such as (), {}, []. Write C++ program using stack to check whether given 
expression is well parenthesized or not. 
*/

#include<iostream>
#include<string>
using namespace std;

class Stack{
    private:
        char* arr;
        int top;
        int capacity;
    public:
        Stack(int size){
            capacity = size;
            arr = new char[capacity];
            top = -1;
        } 

        ~Stack(){
            delete[] arr;
        }

        bool isFull(){
            return (top == (capacity-1));
        }

        bool isEmpty(){
            return top == -1;
        }

        void push(char ch){
            if(isFull()){
                cout << "\nStack Overflow !\n";
                return;
            }
            arr[++top] = ch;
        }

        char pop(){
            if(isEmpty()){
                cout << "\nStack Underflow\n";
                return '\0';
            }
            return arr[top--];
        }

        char peek(){
            if(isEmpty()){
                cout << "\nStack Underflow\n";
                return '\0';
            }
            return arr[top];
        }
        
};

class ExpressionChecker{
    private:
        Stack* s;

        bool isMatchingPair(char open, char close){
            return (open == '(' && close == ')') || (open == '{' && close == '}') || (open == '[' && close == ']');
        }
    
    public:
        ExpressionChecker(int size){
            s = new Stack(size);
        }

        ~ExpressionChecker(){
            delete s;
        }

        bool isWellParenthesized(const string &expression){
            for(char ch : expression){
                if(ch == '(' || ch == '{' || ch == '['){
                    s->push(ch);
                }else if(ch == ')' || ch == '}' || ch == ']'){
                    if(s->isEmpty() || !isMatchingPair(s->peek(),ch)){
                        return false;
                    }
                    s->pop();
                }
            }
            return s->isEmpty();
        }
};

int main(){
    while(true){
        cout << "\n-----------------------------------------";
        cout << "\n                   MENU";
        cout << "\n-----------------------------------------\n";
        cout << "\n1. Check an expression";
        cout << "\n2. Exit\n";

        int choice;
        cout << "\nEnter your choice : ";
        cin >> choice;

        switch(choice){
            case 1:{
                string expression;
                cout << "\nEnter an expresion : ";
                cin >> expression;
                ExpressionChecker checker(50);
                if(checker.isWellParenthesized(expression)){
                    cout << "\nThe expression is well parenthesized.\n";
                }else{
                    cout << "\nThe expression is not well parenthesized.\n";
                }
                break;
            }
            case 2:{
                cout << "\nExiting....\n";
                exit(0);
            }
            default:
                cout << "\nInvalid choice !\n";
        }    
    }
    return 0;
}
