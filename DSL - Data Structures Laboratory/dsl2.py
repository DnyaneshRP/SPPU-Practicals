'''
PRACTICAL - 2
Write a Python program to compute following operations on String: 
a) To display word with the longest length 
b) To determines the frequency of occurrence of particular character in the string 
c) To check whether given string is palindrome or not 
d) To display index of first appearance of the substring 
e) To count the occurrences of each word in a given string 
'''

class StringOp:
    st = ""
    def __init__(self):
        self.st = input("Enter a string : ")

    def display(self):
        print(self.st)
        print()

    def splitwords(self):
        words = []
        word = ""
        for ch in self.st:
            if ch == " ":
                words.append(word)
                word = ""
            else:
                word += ch
        if word:
            words.append(word)
        return words
    
    def length(self,s):
        count = 0
        for i in s:
            count += 1
        return count
    
    def longword(self,words):
        lw = ""
        for i in words:
            if self.length(i) > self.length(lw):
                lw = i
        return lw
        
    def occurence(self,ch):
        cnt = 0
        for i in self.st:
            if i == ch:
                cnt += 1
        return cnt
    
    def ispalindrome(self,s):
        return True if s == s[::-1] else False
    
    def substrindex(self,substr):
        n = self.length(self.st)
        m = self.length(substr)
        for i in range(n-m+1):
            match = True
            for j in range(m):
                if self.st[i+j] != substr[j]:
                    match = False
                    break
            if match:
                return i
        return -1
    
    def countwords(self):
        words = self.splitwords()
        wdcnt = {}
        for i in words:
            if i in wdcnt:
                wdcnt[i] += 1
            else:
                wdcnt[i] = 1
        return wdcnt


def main():
    st1 = StringOp()

    print("\nThe string is : ")
    st1.display()

    while True:
        print("\n----------------------------------------------------------------------------------")
        print("                                 MENU      ")
        print("----------------------------------------------------------------------------------\n")
        print("1. To display word with the longest length")
        print("2. To determines the frequency of occurrence of particular character in the string")
        print("3. To check whether given string is palindrome or not")
        print("4. To display index of first appearance of the substring")
        print("5. To count the occurrences of each word in a given string")
        print("6. Quit")
        
        ch = int(input("\nEnter your choice : "))

        if (ch == 1):
            print("\nWord with the longest length : ")
            print(st1.longword(st1.splitwords()))
        elif (ch == 2):
            print("\nFrequency of occurrence of particular character in the string")
            ch = input("Enter a character : ")
            print(st1.occurence(ch))
        elif (ch == 3):
            print("\nWhether given string is palindrome or not : ")
            s = input("Enter a string : ")
            print("Yes a Palindrome") if st1.ispalindrome(s) else print("Not a Palindrome")
        elif (ch == 4):
            print("\nIndex of first appearance of the substring : ")
            sub = input("Enter a substring : ")
            print(st1.substrindex(sub))
        elif (ch == 5):  
            print("\nCount of occurrences of each word in given string : ")
            print(st1.countwords())
        elif (ch == 6):
            print("Quiting.......")
            quit()
        else:
            print("Invalid choice !")


main()