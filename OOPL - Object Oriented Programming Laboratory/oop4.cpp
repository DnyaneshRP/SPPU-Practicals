/*
Practical - 4
Write a C++ program that creates an output file, writes information to
it, closes the file, open it again as an input file and read the information
from the file. Consider class Person, open a file and write as many objects
as the user wants then read and display the entire contents of the file.
*/

#include <iostream>
#include <fstream>
using namespace std;

class Employee 
{
    private:
        string Name;
        int ID;
        double salary;

    public:
        void accept()
        {
            cout << "\n Name : ";
            cin.ignore();
            getline(cin, Name);
            cout << "\n Id : ";
            cin >> ID;
            cout << "\n Salary : ";
            cin >> salary;
        }
        void display()
        {
            cout << "\n Name : " << Name;
            cout << "\n Id : " << ID;
            cout << "\n Salary : " << salary << endl;
        }
};

int main()
{
    Employee o[5];
    Employee is[5];
    fstream f;
    int i, n;

    f.open("demo.txt", ios::out);
    cout << "\n Enter the number of employees you want to store : ";
    cin >> n;
    for (i = 0; i < n; i++)
    {
        cout << "\n Enter information of Employee " << i + 1 << "\n";
        o[i].accept();
        f.write((char *)&o[i], sizeof o[i]);
    }

    f.close();

    f.open("demo.txt", ios::in);
    cout << "\n Information of Employees is as follows : \n";
    for (i = 0; i < n; i++)
    {
        cout << "\nEmployee " << i + 1 << "\n";
        f.read((char *)&is[i], sizeof o[i]);
        is[i].display();
    }
    f.close();

    return 0;
}