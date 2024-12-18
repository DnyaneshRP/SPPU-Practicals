/*
Practical - 3
Imagine a publishing company which does marketing for book 
and audio cassette versions. Create a class publication that 
stores the title (a string) and price (type float) of publications. 
From this class derive two classes: book which adds a page 
count (type int) and tape which adds a playing time in minutes 
(type float). Write a program that instantiates the book and 
tape class, allows user to enter data and displays the data 
members. If an exception is caught, replace all the data 
member values with zero values
*/

#include<iostream>
#include<string>
using namespace std;

class publication{
    private:
        string title;
        float price;
    public:
        void getdata(){
            cin.ignore();
            cout << "\nEnter title of publication : ";
            getline(cin,title);
            try{
                if(title.empty()){
                    throw title;
                }
            }catch(string s){
                cout << "\nInvalid title !\n";
                title = "NULL";
            }
            cout << "\nEnter price of publication : ";
            cin >> price;
            try{
                if(price <= 0){
                    throw price;
                }
            }catch(float f){
                cout << "\nInvalid price !\n";
                price = 0.0;
            }
        }
        void putdata(){
            cout << "\nPublication Title : " << title;
            cout << "\nPublication Price : " << price;
        }
};

class book : public publication{
    private:
        int pagecount;
    public:
        void getdata(){
            publication::getdata();
            cout << "\nEnter book page count : ";
            cin >> pagecount;
            try{
                if(pagecount <= 0){
                    throw pagecount;
                }
            }catch(int e)
            {
                cout << "\nInvalid page count !\n";
                pagecount = 0;
            }
        }
        void putdata(){
            publication::putdata();
            cout << "\nBook page count : " << pagecount << endl;
        }
};

class tape : public publication{
    private:
        float ptime;
    public: 
        void getdata(){
            publication::getdata();
            cout << "\nEnter tape's playing time(in min) : ";
            cin >> ptime;
            try{
                if(ptime <= 0){
                    throw ptime;
                }
            }catch(float e)
            {
                cout << "\nInvalid play time !\n";
                ptime = 0.0;
            }
        }
        void putdata(){
            publication::putdata();
            cout << "\nTape's playing time : " << ptime << endl;
        }
};

int main(){
    book b;
    tape t;
    
    cout << "\n\n\n---------- Enter Book Details ----------\n";
    b.getdata();
    cout << "\n\n---------- Enter Tape Details ----------\n";
    t.getdata();

    cout << "\n========== Book Details ==========\n";
    b.putdata();
    cout << endl;
    cout << "\n========== Tape Details ==========\n";
    t.putdata();
    return 0;
}