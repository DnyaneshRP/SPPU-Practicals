;Write x86 ALP to find the factorial of a given integer number on a command line by using recursion. Explicit stack manipulation is expected in the code.

%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	
	op1 db 0xa, "Factorial of "
	op1len equ $-op1
	
	fa1 db "0000000000000001", 0xa, 0xa
	fa1len equ $-fa1
	
	sep db " : "
	seplen equ $-sep
	
	n db 0xa,0xa
	nl equ $-n

section .bss
	count resb 1
	num resb 1
	fa resb 16
global _start
section .text
_start:	
		pop rdi
		pop rdi
		pop rdi
		
		mov bl, byte[rdi]
		
		mov [num], rdi
		rwcall 1,op1,op1len
		rwcall 1,[num],1
		rwcall 1,sep,seplen
	
		call a2h
		
		cmp rbx, 1
		jbe print1
		
		mov rax,rbx
		call fact
		
		mov rbx, fa
		mov byte[count], 16
		call h2a
		rwcall 1,fa,16
		rwcall 1,n,nl
		jmp exit
		
		
		print1:
			rwcall 1,fa1,fa1len
			jmp exit
			
			
		fact:
			cmp rbx, 1
			jbe f1
			dec rbx
			mul rbx
			call fact			
			f1:
				ret
			
		
		a2h:
			cmp bl,39h
			jbe y
			sub bl,07h
			y:
				sub bl,30h
			mov al,bl
			ret
		
		
		h2a:	
			again:
				rol rax,04h
				mov rdx,rax
				and rdx, 0Fh
				cmp dl, 09h
				jle x
				add  dl, 07h
				x:
					add dl, 30h
				mov [rbx], dl
				inc rbx
				dec byte[count]
				jnz again
			ret
			
			
		exit:
			mov rax,60
			mov rdi,0
			syscall
		
