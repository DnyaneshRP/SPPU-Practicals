/*
Practical - 1
Implement a class Complex which represents the Complex Number data type.
Implement the following :
1. Constructor (including a default constructor which creates the complex number 0+0i).
2. Overload operator+ to add two complex numbers.
3. Overload operator* to multiply two complex numbers.
4. Overload operators << and >> to print and read Complex Numbers.
*/

#include<iostream>
using namespace std;
class Complex{
    private:
        float real,img;
        Complex *ptr;
    public:
        Complex(){
            real = 0;
            img = 0;
            ptr = null;
        }
        Complex operator+ (Complex obj){
            Complex temp;
            temp.real = real + obj.real;
            temp.img = img + obj.img;
            return temp;
        }
        Complex operator* (Complex obj){
            Complex temp;
            temp.real = real * obj.real;
            temp.img = img * obj.img;
            return temp;
        }
       friend istream &operator>>(istream &is, Complex &obj){
            is >> obj.real;
            is >> obj.img;
            return is;
        }
        friend ostream &operator<<(ostream &os, Complex &obj){
            os << obj.real << " + "  << obj.img << "i";
            return os;
        }
};

int main(){
    Complex a,b,c,d;
    cout << "\nDefault Constructor : " << a << endl;
    cout << "\nThe first Complex number is : ";
    cout << "\nEnter real and img : ";
    cin >> a;
    cout << "\nThe second Complex number is : ";
    cout << "\nEnter real and img : ";
    cin >> b;
    cout << "\n\t--- Arithmetic Operations ---";
    c = a + b;
    cout << "\nAddition = ";
    cout << c;
    d = a * b;
    cout << "\nMultiplication = ";
    cout << d;
    cout << endl;
    return 0;
}