; Write an x86/64 ALP to accept a string and to display its length  

%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	ipmsg db 0xa, "Enter a string : "
	ipmsglen equ $-ipmsg

	opmsg db 0xa, "Length of string is : "
	opmsglen equ $-opmsg
	
	umsg db "h", 0xa, 0xa
	umsglen equ $-umsg
	

section .bss
	mystr resb 100
	arr resb 2
	count resb 1
	
global _start
section .text
_start:	
		rwcall 1,ipmsg,ipmsglen
		
		mov rbx, arr
		mov byte[count], 02h
		
		rwcall 0,mystr,100
		
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
		
		rwcall 1,opmsg,opmsglen	
		rwcall 1,arr,2
		rwcall 1,umsg,umsglen
			
		mov rax,60
		mov rdi,0
		syscall
		
