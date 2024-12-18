/*
Practical - 7
Write a program in C++ to use map associative container. The keys will be the names of states, and the 
values will be the populations of the states. When the program runs, the user is prompted to type the 
name of a state. The program then looks in the map, using the state name as an index, and returns the 
population of the state.
*/

#include<iostream>
#include<map>
#include<string>
using namespace std;
int main(){
    string state;
    float population;
    char ans = 'y';
    int choice;
    map<string,float> m;
    map<string,float> :: iterator i;
    
    
    do{
        cout << "\nMain Menu";
        cout << "\n\t1. Insert an element";
        cout << "\n\t2. Display";
        cout << "\n\t3. Search\n";

        cout << "\nEnter your choice : ";
        cin >> choice;
        cin.ignore();
        switch (choice)
        {
        case 1:
            cout << "\nEnter the name of state : ";
            getline(cin,state);
            cout << "\nEnter the population (in Cr) : ";
            cin >> population;
            
            m.insert(pair<string,float>(state,population));
            break;
        
        case 2:
            cout << "\nState and Populations are : \n";
            for(i = m.begin(); i != m.end(); i++){
                cout << "[" << (*i).first << ", " << (*i).second << "]" << endl;
            }
            break;

        case 3:
            cout << "\nEnter the name of state for searching its population : ";
            getline(cin,state);
            if(m.count(state) != 0)
                cout << "Population is " << m.find(state) -> second << "Cr" << endl;
            else    
                cout << "State is not present in the list !" << endl;
            break;

        default:
            cout << "Invalid choice !" << endl;
            break;
        }
        cout << "\nDo you want to continue? (y/n): ";
        cin >> ans;
    }while(ans == 'y' || ans == 'Y');
    return 0;
}