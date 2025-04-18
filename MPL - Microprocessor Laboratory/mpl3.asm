; Write X86/64 ALP to find the largest of the given Byte/Word/Dword/64 bit numbers

%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	arr dq 0x10, 0x80, 0x50, 0x20, 0x70 
	
	opmsg db 0xa, "The greatest number in array is : "
	opmsglen equ $-opmsg
	
	umsg db "h", 0xa, 0xa
	umsglen equ $-umsg

section .bss
	count resb 1
	gnum resb 16
	
global _start
section .text
_start:	
		mov rbx, arr
		mov byte[count], 05h
		mov rax, 0h
		great:
			cmp rax, [rbx]
			jae y
			mov rax, [rbx]
			y:
				add rbx, 8
				dec byte[count]
				jnz great
	
		
		mov byte[count], 16
		mov rbx, gnum
		call h2a	
		
		
		rwcall 1, opmsg, opmsglen
		rwcall 1,gnum,16
		rwcall 1,umsg,umsglen
		
		
		jmp exit
		
		
		h2a:	
			again:
			rol rax,04h
			mov rdx,rax
			and rdx, 0Fh
			cmp rdx, 09h
			jle x
			add  rdx, 07h
			x:
				add rdx, 30h
			mov [rbx], rdx
			inc rbx
			dec byte[count]
			jnz again
			ret		
			
			
		exit:
			mov rax,60
			mov rdi,0
			syscall
		
