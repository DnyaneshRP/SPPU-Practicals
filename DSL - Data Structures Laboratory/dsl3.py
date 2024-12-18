'''
PRACTICAL - 3
Write a Python program to compute following computation on matrix: 
a) Addition of two matrices 
b) Subtraction of two matrices 
c) Multiplication of two matrices 
d) Transpose of a matrix
'''

class MatOp:
    X = [[],[],[]]
    Y = [[],[],[]]
    
    A = [[],[],[]]
    S = [[],[],[]]
    M = [[],[],[]]
    r,c = 3,3

    def __init__(self):
        print("\nEnter elements of matrix X : ")
        self.X = self.inputmat()

        print("\nEnter elements of matrix Y : ")
        self.Y = self.inputmat()

        print("\n----- Matrix A -----")
        self.displaymat(self.X)

        print("\n----- Matrix B -----")
        self.displaymat(self.Y)

        self.A = [[0,0,0],[0,0,0],[0,0,0]]
        self.S = [[0,0,0],[0,0,0],[0,0,0]]
        self.M = [[0,0,0],[0,0,0],[0,0,0]]

    def inputmat(self):
        T = []
        for i in range(self.r):
            row = []
            for j in range(self.c):
                row.append(int(input()))
            T.append(row)
        return T
    
    def displaymat(self,T):
        for i in range(self.r):
            for j in range(self.c):
                print(T[i][j],end = " ")
            print()
        print()

    def addmat(self):
        for i in range(self.r):
            for j in range(self.c):
                self.A[i][j] = self.X[i][j] + self.Y[i][j]
    
    def submat(self):
        for i in range(self.r):
            for j in range(self.c):
                self.S[i][j] = self.X[i][j] - self.Y[i][j]

    def mulmat(self):
        for i in range(self.r):
            for j in range(self.c):
                for k in range(self.c):
                    self.M[i][j] += self.X[i][k] * self.Y[k][j]

    def transpose(self):
        print("\n----- Transpose of A -----")
        for i in range(self.r):
            for j in range(self.c):
                print(self.X[j][i],end = " ")
            print()
        print()
        print("----- Transpose of B -----")
        for i in range(self.r):
            for j in range(self.c):
                print(self.Y[j][i],end = " ")
            print()
        print()


def main():
    m1 = MatOp()

    while(True):
        print("\n-------------------------------------------------")
        print("                       MENU")
        print("-------------------------------------------------\n")
        print("1. Addition of two matrices ")
        print("2. Subtraction of two matrices ")
        print("3. Multiplication of two matrices ")
        print("4. Transpose of a matrix")
        print("5. Quit")
        
        ch = int(input("\nEnter your choice : "))

        if (ch == 1):
            print("\n----- Addition of two matrices -----")
            m1.addmat()
            m1.displaymat(m1.A)
        elif (ch == 2):
            print("\n--- Subtraction of two matrices ---")
            m1.submat()
            m1.displaymat(m1.S)
        elif (ch == 3):
            print("\n-- Multiplication of two matrices --")
            m1.mulmat()
            m1.displaymat(m1.M)
        elif (ch == 4):
            m1.transpose()
        elif (ch == 5):
            print("\nQuiting....\n")
            quit()
        else:
            print("Invalid choice !")


main()