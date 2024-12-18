'''
PRACTICAL - 5 and 6
Write a Python program to store second year percentage of students in array. Write 
function for sorting array of floating-point numbers in ascending order using 
a) Insertion sort and 
b) Shell Sort and 
display top five scores.
'''

class Student:
    p = []
    n = 0

    def __init__(self):
        self.n = int(input("\nEnter no. of students in Second Year : "))
        
        print(f"\nEnter percentage of {self.n} students :")
        for i in range(self.n):
            self.p.append(float(input()))

        print("\nThe percentage of second year students are : ")
        self.display()
  

    def display(self):
        for i in range(self.n):
            print(self.p[i], end="  ")
        print()

    def insertionSort(self):
        for i in range(1,self.n):
            key = self.p[i]
            j = i - 1
            while j>=0 and self.p[j] > key:
                self.p[j+1] = self.p[j]
                j -= 1
            self.p[j+1] = key
    
    def shellSort(self):
        gap = self.n // 2
        while gap > 0:
            for i in range(gap,self.n):
                temp = self.p[i]
                j = i
                while j >= gap and self.p[j-gap] > temp:
                    self.p[j] = self.p[j-gap]
                    j -= gap
                self.p[j] = temp
            gap //= 2

    def partition(self,low,high):
        pivot = self.p[low]
        start = low
        end = high
        while start <= end:
            while self.p[start] <= pivot:
                start += 1
            while self.p[end] > pivot:
                end -= 1
            if start < end:
                self.p[start], self.p[end] = self.p[end], self.p[start]
        self.p[low], self.p[end] = self.p[end], self.p[low]
        return end
    
    def quickSort(self,low,high):
        if low < high:
            pi = self.partition(low,high)
            self.quickSort(low,pi-1)
            self.quickSort(pi+1,high)

    def topfive(self):
        tf = []
        for i in sorted(self.p)[::-1]:
            if i not in tf:
                tf.append(i)
                
        print("\nTop Five scores are : \n")
        for n,i in zip(range(1,6),range(min(5,len(tf)))):
            print(f"{n}. {tf[i]} %")
        print()

def main():
    s = Student()

    while(True):
        print("\n-------------------------------")
        print("             MENU")
        print("-------------------------------\n")
        print("1. Insertion Sort")
        print("2. Shell Sort")
        print("3. Quick Sort")
        print("4. Display Top Five Scores")
        print("5. Quit")

        ch = int(input("\nEnter your choice : "))

        if (ch == 1):
            print("\nINSERTION SORT\n")
            s.insertionSort()
            s.display()
        elif (ch == 2):
            print("\nSHELL SORT\n")
            s.shellSort()
            s.display()
        elif (ch == 3):
            print("\nQUICK SORT\n")
            s.quickSort(0,s.n - 1)
            s.display()
        elif (ch == 4):
            s.topfive()
        elif (ch == 5):
            print("\nQuiting...\n")
            quit()
        else:
            print("\nInvalid Choice !\n")


main()
