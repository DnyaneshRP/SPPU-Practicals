;Write X86 ALP to find, 
;a) Number of Blank spaces 
;b) Number of lines 
;c) Occurrence of a particular character. 
;Accept the data from the text file. 
;The text file has to be accessed during Program_1 execution and write FAR PROCEDURES in Program_2 for the rest of the processing. 
;Use of GLOBAL and EXTERN directives is mandatory

%macro rwocall 4
	mov rax,%1
	mov rdi,%2
	mov rsi,%3
	mov rdx,%4
	syscall
%endmacro

section .data
	
	file db "abc.txt",0
	
	m1 db 0xa, "Error while opening the file!",0xa
	m1l equ $-m1
	
	m2 db 0xa, "File opened successfully!",0xa,0xa
	m2l equ $-m2
	
	m3 db 0xa,0xa,"File closed successfully!",0xa
	m3l equ $-m3
	
	m4 db 0xa,"No. of spaces     : "
	m4l equ $-m4
	
	m5 db 0xa,0xa,"No. of lines      : "
	m5l equ $-m5
	
	m6 db 0xa,0xa,"Enter character   : "
	m6l equ $-m6
	
	m7 db 0xa,"No. of occurences : "
	m7l equ $-m7

section .bss

	global buffer, spcnt, nlcnt, occnt, cnt1, cnt2, cnt3
	
	buffer resb 100	
	char resb 2
	spcnt resb 2
	nlcnt resb 2
	occnt resb 2
	count resb 1
	
	buflen resq 1
	fd resq 1
	cnt1 resq 1
	cnt2 resq 1
	cnt3 resq 1
	
	
global _start
section .text
_start:	
		extern spaces, lines, occurences		
		
		;open() for opening file
		rwocall 2,file,2,0777
		
		mov qword[fd], rax
		
		bt rax, 63
		jnc succ
		rwocall 1,1,m1,m1l
		jmp exit
		
		succ:
			rwocall 1,1,m2,m2l
			
			;read() for reading file
			rwocall 0,[fd],buffer,100
			
			mov qword[buflen], rax
			mov qword[cnt1], rax
			mov qword[cnt2], rax			
			mov qword[cnt3], rax
			
			rwocall 1,1,buffer,[buflen]
			
			rwocall 1,1,m4,m4l
			call spaces
			
			rwocall 1,1,m5,m5l
			call lines
			
			rwocall 1,1,m6,m6l
			rwocall 0,0,char,2
			mov bl, byte[char]
			rwocall 1,1,m7,m7l
			call occurences

		exit:
			mov rax, 03
			mov rdi, [fd]
			syscall
				
			rwocall 1,1,m3,m3l
				
			mov rax,60
			mov rdi,0
			syscall
		
