; Write an X86/64 ALP to accept the 64 bit Hexadecimal numbers from user and store them in an array and display the accepted numbers.

%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	ipmsg db 0xa, "Enter 5 64-bit hex numbers : ", 0xa
	ipmsglen equ $-ipmsg

	opmsg db 0xa, "The numbers you entered : ", 0xa
	opmsglen equ $-opmsg
	
	cnt db 5

section .bss
	num resb 85
	
global _start
section .text
_start:	
		rwcall 1,ipmsg,ipmsglen
		
		mov rbx, num
		x:
			rwcall 0,rbx,17
			add rbx,17
			dec byte[cnt]
			jnz x
		
		rwcall 1,opmsg,opmsglen
		
		mov rbx,num
		y:
			rwcall 1,rbx,17
			add rbx,17
			dec byte[cnt]
			jnz y
			
		
		mov rax,60
		mov rdi,0
		syscall
		
