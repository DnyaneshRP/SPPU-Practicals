;Write a switch case driven x86/64 ALP to perform 64-bit hexadecimal arithmetic operations(+,-,*,/) using suitable macros. Define procedure for each operation


%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	menu db 0xa, "----------Menu----------", 0xa, "1. Addition", 0xa, "2. Subtraction", 0xa, "3. Multiplication", 0xa, "4. Division", 0xa, "5. Signed Multiplication", 0xa, "6. Signed Division", 0xa, "7. Exit", 0xa, 0xa, "Enter choice : "
	menulen equ $-menu
	
	chm db 0xa, "Option choosen : "
	chmlen equ $-chm
	
	umsg db "h", 0xa, 0xa
	umsglen equ $-umsg
	
	nm db 0xa
	nml equ $-nm
	
	qm db 0xa, "Quotient  : "
	qml equ $-qm
	
	rm db "Remainder : "
	rml equ $-rm
	
	invm db "Invalid Choice !",0xa
	invml equ $-invm
	
	un1 dq 0x0000000000000040
	un2 dq 0x0000000000000020
	
	sn1 dq -0x0000000000000004
	sn2 dq -0x0000000000000002
	
	rem dq 0x0
	quo dq 0x0
	
	
section .bss
	choice resb 1
	count resb 1
	ans resb 16
	rema resb 16
	
global _start
section .text
_start:
	
	rwcall 1, menu, menulen
	rwcall 0, choice, 1
	
	rwcall 1, chm, chmlen
	rwcall 1, choice, 1
	rwcall 1, nm, nml
	
	cmp byte[choice], 31h
	je add_case
	
	cmp byte[choice], 32h
	je sub_case
	
	cmp byte[choice], 33h
	je mul_case
	
	cmp byte[choice], 34h
	je div_case
	
	cmp byte[choice], 35h
	je smul_case
	
	cmp byte[choice], 36h
	je sdiv_case
	
	cmp byte[choice], 37h
	je exit
	
	jmp invalid_choice
	
	add_case:
		call addproc
		jmp exit
		
	sub_case:
		call subproc
		jmp exit
		
	mul_case:
		call mulproc
		jmp exit
		
	div_case:
		call divproc
		jmp exit
		
	smul_case:
		call smulproc
		jmp exit
	
	sdiv_case:
		call sdivproc
		jmp exit
	
	invalid_choice:
		rwcall 1,invm,invml
		jmp exit
		
	
	addproc:
		mov rax, [un1]
		mov rbx, [un2]
		add rax, rbx
		call h2a
		ret
		
	subproc:
		mov rax, [un1]
		mov rbx, [un2]
		sub rax, rbx
		call h2a
		ret
	
	mulproc:
		mov rax, [un1]
		mov rbx, [un2]
		mul rbx
		call h2a
		ret
		
	divproc:
		xor rax,rax
		mov rax, [un1]
		xor rdx,rdx
		mov rbx, [un2]
		idiv rbx
		mov [rem], rdx
		mov [quo], rax
		rwcall 1,qm,qml
		mov rax, [quo]
		call h2a
		rwcall 1,rm,rml
		mov rax, [rem]
		call h2a
		ret
	
	smulproc:
		mov rax, [sn1]
		mov rbx, [sn2]
		imul rbx
		call h2a
		ret
	
	sdivproc:
		xor rax,rax
		mov rax, [sn1]
		xor rdx,rdx
		mov rbx, [sn2]
		cqo
		idiv rbx
		mov [rem], rdx
		mov [quo], rax
		rwcall 1,qm,qml
		mov rax, [quo]
		call h2a
		rwcall 1,rm,rml
		mov rax, [rem]
		call h2a
		ret
	
	h2a:	
		mov byte[count], 16
		mov rbx, ans
		again:
			rol rax,04h
			mov rdx,rax
			and rdx, 0xF
			cmp rdx, 09h
			jle x
			add  rdx, 07h
			x:
				add rdx, 30h
			mov [rbx], dl
			inc rbx
			dec byte[count]
			jnz again
			rwcall 1,ans,16
			rwcall 1,umsg,umsglen
			ret
	
	exit:
		mov rax,60
		mov rdi,0
		syscall
