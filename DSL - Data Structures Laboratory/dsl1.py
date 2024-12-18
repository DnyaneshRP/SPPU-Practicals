'''
PRACTICAL - 1
In second year, computer engineering class, group A student's play cricket, group 
B students play badminton and group C students play football. Write a Python 
program using functions to compute following: -
a) List of students who play both cricket and badminton 
b) List of students who play either cricket or badminton but not both 
c) Number of students who play neither cricket nor badminton 
d) Number of students who play cricket and football but not badminton. 
(Note- While realizing the group, duplicate entries should be avoided, do not use 
SET built-in functions) 
'''

def set_input():
    lst = []
    n = int(input("Enter no. of players : "))
    print(f"Enter roll no.s of {n} players : \n")
    for i in range(n):
        rn = int(input())
        if rn not in lst:
            lst.append(rn)
    return lst

def set_union(a, b):
    c = []
    c = b.copy()
    for i in a:
        if i not in b:
            c.append(i)
    return c

def set_difference(a,b):
    c = []
    for i in a:
        if i not in b:
            c.append(i)
    return c

def set_intersection(a,b):
    c = []
    for i in a:
        if i in b:
            c.append(i)
    return c

def set_symmetric_difference(a,b):
    c = []
    c = set_difference(set_union(a,b),set_intersection(a,b))
    return c

def display_set(a):
    for i in a:
        print(f"{i}", end = "  ")
    print()

def main():
    c = []
    b = []
    f = []
    
    print("\nEnter students playing cricket :")
    c = set_input()
    print("\nEnter students playing badminton :")
    b = set_input()
    print("\nEnter students playing football :")
    f = set_input()

    print("\nCricket Set : ")
    display_set(c)

    print("\nBadminton Set : ")
    display_set(b)

    print("\nFootball Set : ")
    display_set(f)

    while True:
        print("\n-------------------------------------------------------------------------")
        print("                                 MENU      ")
        print("-------------------------------------------------------------------------\n")
        print("1. List of students who play both cricket and badminton")
        print("2. List of students who play either cricket or badminton but not both")
        print("3. Number of students who play neither cricket nor badminton")
        print("4. Number of students who play cricket and football but not badminton")
        print("5. Quit")

        ch = int(input("\nEnter your choice : "))

        if (ch == 1):
            print("\nList of students who play both cricket and badminton : ")
            display_set(set_intersection(c,b))
        elif (ch == 2):
            print("\nList of students who play either cricket or badminton but not both : ")
            display_set(set_symmetric_difference(c,b))
        elif (ch == 3):
            print("\nNumber of students who play neither cricket nor badminton : ")
            set1 = set_difference(set_union(set_union(c,b),f),set_union(c,b))
            display_set(set1)
            print("Number of students : ",len(set1))
        elif (ch == 4):
            print("\nNumber of students who play cricket and football but not badminton : ")
            set2 = set_difference(set_intersection(c,f),b)
            display_set(set2)
            print("Number of students : ",len(set2))
        elif (ch == 5):
            print("Quiting.......")
            quit()
        else:
            print("Invalid choice !")


main()