/*
Practical - 2
Develop a program in C++ to create a database of student's information system containing the following information: 
Name, Roll number, Class, Division, Date of Birth, Blood group, Contactaddress, Telephone number, Driving license no. and other. 
Constructthe database withsuitable member functions. Make use of constructor, default constructor, copy constructor,
destructor, static member functions, friend class, this pointer, inline code and dynamic memory allocation operators-new and delete as well as exception handling.
*/

#include<iostream>
#include<string>
#include<cstring>
using namespace std;

class PersonClass{
    private:
        char name[40], clas[10], div[5], dob[15], bloodgrp[5];
        int roll;
    public:
        static int count;
        friend class PersonalClass;
        PersonClass(){
            char *name = new char[40];
            char *clas = new char[10];
            char *div = new char[5];
            char *dob = new char[15];
            char *bloodgrp = new char[5];
            roll = 0;
        }
        static void totalRecordCount(){
            cout << "\n\nTotal no. of records created : " << count;
        }
};

int PersonClass :: count = 0;

class PersonalClass{
    private:
        char address[30], telephone[15], license[10];
    public:
        PersonalClass(){
            strcpy(address,"");
            strcpy(telephone,"");
            strcpy(license,"");
        }
        void inputData(PersonClass *obj);
        void displayData(PersonClass *obj);
        friend class PersonClass;
};

void PersonalClass :: inputData(PersonClass *obj){
    cout << "\nROLL NO : ";
    cin >> obj->roll;
    cout << "\nNAME : ";
    cin >> obj->name;
    cout << "\nCLASS : ";
    cin >> obj->clas;
    cout << "\nDIVISION : ";
    cin >> (char*)(obj->div);
    cout << "\nDATE OF BIRTH (DD-MM-YYYY) : ";
    cin >> obj->dob;
    cout << "\nBLOOD GROUP : ";
    cin >> obj->bloodgrp;
    cout << "\nADDRESS : ";
    cin >> this->address;
    cout << "\nTELEPHONE NUMBER : ";
    cin >> this->telephone;
    cout << "\nDRIVING LICENSE NO. : ";
    cin >> this->license;
    obj->count++;
}

void PersonalClass :: displayData(PersonClass *obj){
    cout << endl;
    cout << obj->roll << "    ";
    cout << obj->name << "   ";
    cout << obj->clas << "     ";
    cout << obj->div << "    ";
    cout << obj->dob << "    ";
    cout << obj->bloodgrp << "    ";
    cout << this->address << "   ";
    cout << this->telephone << "   ";
    cout << this->license << "   "; 
}

int main(){
    PersonClass *c[10];
    PersonalClass *a[10];
    int n = 0, i, choice;
    char ans;
    do{
        cout << "\n\n-: MENU :-";
        cout << "\n\t1.Input Data\n\t2.Display Data\n";
        cout << "\nEnter your choice : ";
        cin >> choice;
        switch(choice){
            case 1:
                cout << "\n\nENTER THE DETAILS :";
                cout << "\n----------------------";
                do{
                    a[n] = new PersonalClass;
                    c[n] = new PersonClass;
                    a[n]->inputData(c[n]);
                    n++;
                    PersonClass::totalRecordCount();
                    cout << "\n\nDo you want to add more records (y/n)? : ";
                    cin >> ans;
                }while(ans == 'y' || ans == 'Y');
                break;
            
            case 2:
                cout << "\n--------------------------------------------------------------------------------------------------------";
                cout << "\nRoll_No  Name         Class   Div    BirthDate   BloodGrp     Address        Telephone    License ";
                cout << "\n--------------------------------------------------------------------------------------------------------\n";
                for(i = 0; i<n; i++){
                    a[i]->displayData(c[i]);
                }
                PersonClass :: totalRecordCount();
                break;
        }
        cout << "\nDo you want to go back to main menu (y/n) ? : ";
        cin >> ans;
        cin.ignore(1,'\n');
    }while(ans == 'y' || ans == 'Y');
    return 0;
}