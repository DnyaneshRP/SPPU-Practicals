;Write x86 ALP to perform overlapped block transfer with string specific instructions. Block containing data can be defined in the data segment.

%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	arr dq 30h, 40h, 50h, 60h, 70h
	dst dq 0h, 0h, 0h, 0h, 0h
	
	sep db " : "
	seplen equ $-sep
	
	msg db "    Address      |      Value     ", 0xa
	msglen equ $-msg
	
	line db "-----------------------------------",0xa
	llen equ $-line
	
	op1 db 0xa,"BEFORE BLOCK TRANSFER",0xa,0xa
	op1len equ $-op1
	
	op2 db 0xa,0xa,"AFTER BLOCK TRANSFER",0xa,0xa
	op2len equ $-op2
	
	n db 0xa
	nlen equ $-n

section .bss
	cnt resb 1
	count resb 1
	con resb 16
	var resb 16
	
global _start
section .text
_start:	
		rwcall 1,op1,op1len
		rwcall 1,line,llen
		rwcall 1,msg,msglen
		rwcall 1,line,llen
		
		mov byte[cnt], 05h
		mov rsi, arr
		again1:
			mov rax , rsi
			mov rbx, con
			mov byte[count], 16
			push rsi
			call h2a
			rwcall 1,con,16
			rwcall 1,sep,seplen
			pop rsi
			
			mov rax, [rsi]
			mov rbx, con
			mov byte[count], 16
			push rsi
			call h2a
			rwcall 1,con,sep
			rwcall 1,n,nlen
			pop rsi
			
			add rsi, 08h
			dec byte[cnt]
			jnz again1
				
		mov rcx, 05h
		mov rsi, arr + (4*8)
		mov rdi, arr + (7*8)
		std
		rep movsq
		
		rwcall 1,op2,op2len
		rwcall 1,line,llen
		rwcall 1,msg,msglen
		rwcall 1,line,llen
		
		mov byte[cnt], 05h
		mov rsi, arr + (3*8)
		again2:
			mov rax , rsi
			mov rbx, con
			mov byte[count], 16
			push rsi
			call h2a
			rwcall 1,con,16
			rwcall 1,sep,seplen
			pop rsi
			
			mov rax, [rsi]
			mov rbx, con
			mov byte[count], 16
			push rsi
			call h2a
			rwcall 1,con,sep
			rwcall 1,n,nlen
			pop rsi
			
			add rsi, 08h
			dec byte[cnt]
			jnz again2

		jmp exit
		
		
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
		
