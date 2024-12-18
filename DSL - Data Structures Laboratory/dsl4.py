'''
PRACTICAL - 4
a)  Write a Python program to store roll numbers of student in array who attended 
    training program in random order. Write function for searching whether particular 
    student attended training program or not, using Linear search and Sentinel search. 
b)  Write a Python program to store roll numbers of student array who attended training 
    program in sorted order. Write function for searching whether particular student 
    attended training program or not, using Binary search and Fibonacci search.
'''

class Student:
    roll = []
    n = 0
    x = 0
    def __init__(self):
        self.n = int(input("\nEnter number of students who attended the training program : "))
        
        print("\nEnter roll no.s of students : ")
        for i in range(0,self.n):
            self.roll.append(int(input()))
        
        print("\nStudents who attended training program are : ")
        self.display()

        
    def display(self):
        for i in range(self.n):
            print(self.roll[i], end = " ")
        print()

    def linearSearch(self):
        for i in range(self.n):
            if self.roll[i] == self.x:
                return i
        return -1
    
    def sentinelSearch(self):
        self.roll.append(self.x)
        i = 0
        while self.roll[i] != self.x:
            i += 1
        if i < self.n:
            return i
        else:
            return -1
        
    def binarySearch(self):
        self.roll.sort()
        low = 0
        high = self.n - 1
        while low <= high:
            mid = (low + high) // 2
            if self.roll[mid] == self.x:
                return mid
            elif self.roll[mid] < self.x:
                low = mid + 1
            else:
                high = mid - 1
        return -1
    
    def fibonacciSearch(self):
        self.roll.sort()
        fib2 = 0
        fib1 = 1
        fibM = fib2 + fib1

        while fibM < self.n:
            fib2 = fib1
            fib1 = fibM
            fibM = fib2 + fib1

        offset = -1

        while fibM > 1:
            i = min(offset + fib2, self.n - 1)
            if self.roll[i] < self.x:
                fibM = fib1
                fib1 = fib2
                fib2 = fibM - fib1
                offset = i
            elif self.roll[i] > self.x:
                fibM = fib2
                fib1 = fib1 - fib2
                fib2 = fibM - fib1
            else:
                return i
            
        if fib1 and self.roll[offset+1] == self.x:
                return offset + 1
            
        return -1


def main():
    s = Student()

    while(True):
        print("\n----------------------------------------------------------")
        print("                         MENU")
        print("----------------------------------------------------------\n")
        print("1. Linear Search")
        print("2. Sentinel Search")
        print("3. Binary Search")
        print("4. Fibonacci Search")
        print("5. Quit")

        s.x = int(input("\nEnter roll no. of student to search : "))
        
        ch = int(input("\nEnter your choice : "))

        if (ch == 1):
            if s.linearSearch() != -1:
                print(f"\nRoll No. {s.x} attended the training program")
            else:
                print(f"\nRoll No. {s.x} didn't attend the training program!")
        elif (ch == 2):
            if s.sentinelSearch() != -1:
                print(f"\nRoll No. {s.x} attended the training program")
            else:
                print(f"\nRoll No. {s.x} didn't attend the training program!")
        elif (ch == 3):
            if s.binarySearch() != -1:
                print(f"\nRoll No. {s.x} attended the training program")
            else:
                print(f"\nRoll No. {s.x} didn't attend the training program!")
        elif (ch == 4):
            if s.fibonacciSearch() != -1:
                print(f"\nRoll No. {s.x} attended the training program")
            else:
                print(f"\nRoll No. {s.x} didn't attend the training program!")
        elif (ch == 5):
            print("\nQuiting...\n")
            quit()
        else:
            print("\nInvalid choice !\n")


main()
