;Write X86/64 ALP to convert 4-digit Hex number into its equivalent BCD number and 5-digit BCD number into its equivalent HEX number. Make your program user friendly to accept the choice from user for: (a) HEX to BCD (b) BCD to HEX(c) EXIT. (Display proper strings to prompt the user while accepting the inputand displaying the result. (wherever necessary, use 64-bit registers).

%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	menu db 0xa, "----------Menu----------", 0xa, "1. HEX TO BCD", 0xa, "2. BCD TO HEX", 0xa, "3. Exit", 0xa, 0xa, "Enter choice : "
	menulen equ $-menu
	
	op1 db "Enter 4 digit HEX number : ",0xa,0xa," HEX : "  
	op1len equ $-op1
	
	op2 db "Enter 5 digit BCD number : ",0xa,0xa," BCD : "
	op2len equ $-op2
	
	op3 db " BCD : "
	op3len equ $-op3
	
	op4 db " HEX : "
	op4len equ $-op4
	
	umsg db "h", 0xa, 0xa
	umsglen equ $-umsg
	
	nm db 0xa
	nml equ $-nm
	
	invm db "Invalid Choice !",0xa
	invml equ $-invm
	
	
section .bss
	num resb 5
	bcd resb 6
	choice resb 2
	count resb 1
	res resb 1
	ans resb 16
global _start
section .text
_start:
	
	rwcall 1, menu, menulen
	rwcall 0, choice, 2
	
	rwcall 1,nm,nml
	
	cmp byte[choice], 31h
	je h2b_case
	
	cmp byte[choice], 32h
	je b2h_case
	
	cmp byte[choice], 33h
	je exit
	
	jmp invalid_choice
	
	h2b_case:
		rwcall 1,op1,op1len		
		rwcall 0,num,5
		call a2h
		call hextobcd
		rwcall 1,nm,nml
		jmp exit
		
	b2h_case:
		rwcall 1,op2,op2len		
		rwcall 0,bcd,6
		call bcdtohex
		call h2a16
		rwcall 1,op4,op4len
		rwcall 1,ans,4
		rwcall 1,nm,nml
		jmp exit
		
	
	invalid_choice:
		rwcall 1,invm,invml
		jmp exit
		
	
	hextobcd:
		mov bx,10
		mov byte[count],0
		back:
			xor dx,dx
			div bx
			push dx
			inc byte[count]
			cmp ax,0
			jnz back
		rwcall 1,op3,op3len
		back1:
			pop dx
			add dl,30h
			mov [res],dl
			rwcall 1,res,1
			dec byte[count]
			jnz back1
		ret
		
	bcdtohex:
		mov rsi, bcd
		xor rax,rax
		mov byte[count],5
		mov rbx, 10
		next:
			xor cx,cx
			mul rbx
			mov cl, [rsi]
			sub cl, 30h
			add ax,cx
			inc rsi
			dec byte[count]
			jnz next
		ret
	
	a2h:
		mov byte[count],4
		mov rbx, num
		again:
			mov dl,[rbx]
			cmp dl,39h
			jbe y
			sub dl,07h
			y:
				sub dl,30h
			shl ax,4
			or al,dl
			inc rbx
			dec byte[count]
			jnz again
			ret
	
		h2a16:	
			mov rbx, ans
			mov byte[count], 4
			again2:
				rol ax,04h
				mov dx,ax
				and dx, 0Fh
				cmp dl, 09h
				jle x
				add  dl, 07h
				x:
					add dl, 30h
				mov [rbx], dl
				inc rbx
				dec byte[count]
				jnz again2
			ret
	
	exit:
		mov rax,60
		mov rdi,0
		syscall
