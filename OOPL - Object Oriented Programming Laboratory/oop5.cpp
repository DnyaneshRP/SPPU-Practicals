/*
Practical - 5
Write a function template selection sort and insertion sort.
Write a program that inputs, sorts and outputs an integer 
array and a float array.
*/

#include<iostream>
using namespace std;

#define SIZE 10
int n;

template <class T>
void selection(T A[SIZE]){
    int i, j, min;
    T temp;
    for(i=0; i<=n-2; i++){
        min = i;
        for(j=i+1; j<=n-1; j++){
            if(A[j]<A[min]){
                min = j;
            }
        }
        temp = A[i];
        A[i] = A[min];
        A[min] = temp;
    }
    cout << "\n The sorted array is : \n";
    for(i=0; i<n; i++){
        cout << "\t" << A[i];
    }
}

template <class T>
void insertion(T A[SIZE]){
    for (int i = 1; i < n; i++) {
        T key = A[i];
        int j = i - 1;
        while (j >= 0 && A[j] > key) {
            A[j + 1] = A[j];
            j = j - 1;
        }
        A[j + 1] = key;
    }
    cout << "\n The sorted array is : \n";
    for(int i=0; i<n; i++){
        cout << "\t" << A[i];
    }
}

int main(){
    int i, A[SIZE];
    float B[SIZE];
    
    cout << "\n\tSelection Sort\n";
    cout << "\nHandling integer elements\n";
    cout << "\n How many elements are there : ";
    cin >> n;
    cout << "\nEnter the integer elements : \n";
    for(i=0; i<n; i++){
        cin >> A[i];
    }
    selection(A);
    
    cout << "\n\n\tInsertion Sort\n";
    cout << "\nHandling float elements\n";
    cout << "\n How many elements are there : ";
    cin >> n;
    cout << "\nEnter the float elements : \n";
    for(i=0; i<n; i++){
        cin >> B[i];
    }
    insertion(B);

    return 0;
}