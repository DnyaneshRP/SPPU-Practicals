; Write X86/64 ALP to count positive and negative numbers in array

%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	arr dq 0x8000000000000010, 0x0000000000000080, 0xF000000000000050, 0x0000000000000020, 0xA000000000000070, 0xB000000000000010, 0x0000000000000080, 0xB000000000000050, 0x0000000000000020, 0x8000000000000070
	
	op1 db 0xa, "Count of positive numbers : "
	op1len equ $-op1
	
	op2 db "Count of negative numbers : "
	op2len equ $-op2
	
	umsg db "h", 0xa, 0xa
	umsglen equ $-umsg

section .bss
	count resb 1
	pc resb 1
	nc resb 1
	pn resb 2
	nn resb 2
	
global _start
section .text
_start:	


		mov rbx, arr
		mov byte[count], 10
		mov byte[pc], 0h
		mov byte[nc], 0h
		
		pncounter:
			mov rax, [rbx]
			rcl rax, 1
			jc n
			;add rax, 0
			;js n
			inc byte[pc]
			add rbx, 8
			dec byte[count]
			jnz pncounter
			n:
				inc byte[nc]
				add rbx, 8
				dec byte[count]
				jnz pncounter
		

		mov byte[count], 02h
		mov al, byte[pc]
		mov rbx, pn
		call h2a	
		
		rwcall 1, op1, op1len
		rwcall 1,pn,2
		rwcall 1,umsg,umsglen
		
		
		mov byte[count], 02h
		mov al, byte[nc]
		mov rbx, nn
		call h2a	
		
		rwcall 1, op2, op2len
		rwcall 1,nn,2
		rwcall 1,umsg,umsglen
		
		
		jmp exit
		
		
		h2a:	
			again:
				rol al,04h
				mov dl,al
				and dl, 0Fh
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
		
