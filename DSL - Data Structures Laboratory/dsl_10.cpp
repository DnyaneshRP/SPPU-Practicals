/*
PRACTICAL - 10
Implement C++ program for expression conversion as infix to postfix and 
its evaluation using stack based on given conditions:
1. Operands and operator, both must be single character.
2. Input Postfix expression must be in a desired format.
3. Only '+', '-', '*' and '/ ' operators are expected.
*/

#include<iostream>
#include<string>
#include<cctype>
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
                cout << "\nStack Overflow\n";
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

int precedence(char op){
    if(op == '+' || op == '-'){
        return 1;
    }else if(op == '*' || op == '/'){
        return 2;
    }
    return 0;
}

bool isOperator(char c){
    return (c == '+' || c == '-' || c == '*' || c == '/');
}

bool isValidOperand(char c){
    return isalpha(c) || isdigit(c);
}

bool isValidOperandPE(char c){
    return isdigit(c);
}

string infixToPostfix(string infix){
    Stack s(infix.length());
    string postfix = "";

    for(int i = 0; i<infix.length(); i++){
        char c = infix[i];
        
        if(isValidOperand(c)){
            postfix += c;
        }else if(c == '('){
            s.push(c);
        }else if(c == ')'){
            while(!s.isEmpty() && s.peek() != '('){
                postfix += s.pop();
            }
            s.pop();
        }else if(isOperator(c)){
            while(!s.isEmpty() && precedence(s.peek()) >= precedence(c)){
                postfix += s.pop();
            }
            s.push(c);
        }else{
            cout << "\nInvalid character encountered : " << c << endl;
            return "";
        }
    }
    while(!s.isEmpty()){
        postfix += s.pop();
    }
    return postfix;
}

int evaluatePostfix(string postfix){
    Stack s(postfix.length());

    for(int i=0; i<postfix.length(); i++){
        char c = postfix[i];

        if(isdigit(c)){
            s.push(c - '0');
        }else if(isOperator(c)){
            int operand2 = s.pop();
            int operand1 = s.pop();
            int result;
            switch(c){
                case '+':
                    result = operand1 + operand2;
                    break;
                case '-':
                    result = operand1 - operand2;
                    break;
                case '*':
                    result = operand1 * operand2;
                    break;
                case '/':
                    if(operand2 == 0){
                        cout << "\nDivision by zero error\n";
                        return -1;
                    }
                    result = operand1 / operand2;
                    break;
                default:
                    cout << "\nInvalid operator : " << c << endl;
                    return -1;
            }
            s.push(result);
        }else{
            cout << "\nInvalid character encountered : " << c << endl;
            return -1;
        }
    }

    return s.pop();
}

bool isValidPostfix(string postfix){
    for(char ch : postfix){
        if(!isValidOperandPE(ch) && !isOperator(ch)){
            return false;
        }
    }
    return true;
}

int main(){
    while(true){
        cout << "\n------------------------------------------------";
        cout << "\n                    MENU\n";
        cout << "------------------------------------------------\n";
        cout << "\n1. Infix to Postfix Conversion";
        cout << "\n2. Postfix Evaluation";
        cout << "\n3. Exit\n";

        int choice;
        cout << "\nEnter your choice : ";
        cin >> choice;

        switch(choice){
            case 1:{
                string infix;
                cout << "\nEnter infix expression : ";
                cin >> infix;
                if(infix.empty()){
                    cout << "\nInfix expression cannot be empty !\n";
                    break;
                }
                string postfix = infixToPostfix(infix);
                if(!postfix.empty()){
                    cout << "\nPosfix expression : " << postfix << endl;
                }
                break;
            }
            case 2:{
                string postfix;
                cout << "\nEnter postfix expression : ";
                cin >> postfix;
                if(postfix.empty()){
                    cout << "\nPostfix expression cannot be empty !\n";
                    break;
                }
                if(!isValidPostfix(postfix)){
                    cout << "\nInvalid postfix expression !\n";
                    break;
                }
                int result = evaluatePostfix(postfix);
                if(result != -1){
                    cout << "\nEvaluation result : " << result << endl;
                }
                break;
            }
            case 3:{
                cout << "\nExiting...\n";
                exit(0);
            }
            default:{
                cout << "\nInvalid choice !\n";
            }

        }
    }
    return 0;
}
