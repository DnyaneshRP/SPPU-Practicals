/*
Practical - 6
Write C++ program using STL for sorting and searching with user defined records 
such as person record (Name, DOB, Telephone number) using vector container.
*/

#include <iostream>
#include <vector>
#include <algorithm>
#include <iterator>
using namespace std;

class Person
{
    public:
        string name;
        int day, month, year;
        string dob;
        int telephone;
        Person()
        {
            name = "";
            telephone = 0;
            day = 1;
            month = 1;
            year = 2024;
        }
        bool operator==(Person p2)
        {
            return name == p2.name;
        }
        void input()
        {
            cout << "Enter name: ";
            cin >> name;
            cout << "Enter telephone: ";
            cin >> telephone;
            cout << "Enter Date of birth\n(first DD [ENTER], then MM [ENTER], then YYYY [ENTER] format):\n";
            cin >> day >> month >> year;
            dob = "" + to_string(day) + "/" + to_string(month) + "/" + to_string(year);
        }
        void display()
        {
            cout << "(Name: " << name << ")(Telephone: " << telephone << ")(DOB: " << dob << ")\n" << endl;
        }
};

bool compareDate(Person p1, Person p2)
{
    return ((p1.year == p2.year) ? ((p1.month == p2.month) ? ((p1.day <= p2.day) ? true : false)   : ((p1.month < p2.month) ? true : false)) : ((p1.year < p2.year) ? true : false));
}
bool compareTelephone(Person p1, Person p2)
{
    return (p1.telephone < p2.telephone);
}
bool compareName(Person p1, Person p2)
{
    return (p1.name < p2.name);
}

Person return_obj()
{
    Person a;
    a.input();
    return a;
}

void srch(vector<Person> vs1)
{
    Person p1;
    cout << "Enter name of the person you want to find: " << endl;
    cin >> p1.name;
    vector<Person>::iterator ir;
    ir = find(vs1.begin(), vs1.end(), p1);
    if (ir == vs1.end())
        cout << "The person with name " << p1.name << " is not in the vector." << endl;
    else
    {
        cout << "Person found!" << endl;
        ir->display();
        cout << endl;
    }
}
void deletevec(vector<Person> &vs1)
{
    Person p1;
    cout << "Enter name of the person you want to delete: " << endl;
    cin >> p1.name;
    vector<Person>::iterator ir;
    ir = find(vs1.begin(), vs1.end(), p1);
    if (ir == vs1.end())
        cout << "The person with name " << p1.name << "is not in the vector." << endl;
    else
    {
        ir->display();
        cout << "\n";
        vs1.erase(ir);
        cout << "Deleted\n";
    }
}

void vec_display(vector<Person> vs1)
{
    for (auto elem : vs1)
    {
        elem.display();
    }
    cout << "\n";
}

int main()
{

    vector<Person> v1;

    cout << "Enter number of people: ";
    int n, ch, ch2;
    cin >> n;

    for (int i = 0; i < n; i++)
    {
        v1.push_back(return_obj());
    }

    while (1)
    {
        cout << "\t\tMenu\n1.Add Person\n";
        cout << "2.Delete Person\n";
        cout << "3.Sort\n";
        cout << "4.Search\n";
        cout << "5.Display\n";
        cout << "6. Exit\n";
        cout << "Choose an option (1-6): ";
        cin >> ch;
        switch (ch)
        {
        case 1:
            v1.push_back(return_obj());
            break;
        case 2:
            deletevec(v1);
            break;
        case 3:
            cout << "1.Sort by DOB , 2 Sort by Name , 3 Sort by Telephone no.\n";
            cin >> ch2;
            switch (ch2)
            {
            case 1:
                sort(v1.begin(), v1.end(), compareDate);
                break;
            case 2:
                sort(v1.begin(), v1.end(), compareName);
                break;
            case 3:
                sort(v1.begin(), v1.end(), compareTelephone);
                break;
            }
            vec_display(v1);
            break;
        case 4:
            srch(v1);
            break;
        case 5:
            vec_display(v1);
            break;
        case 6:
            exit(1);
        default:
            cout << "Please choose a valid option.\n";
        }
    }
}