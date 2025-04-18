;Write X86/64 ALP to detect protected mode and display the values of GDTR, LDTR, IDTR, TR and MSW Registers. Also identify CPU type using CPUID instruction.

%macro rwcall 3
	mov rax,%1
	mov rdi,%1
	mov rsi,%2
	mov rdx,%3
	syscall
%endmacro

section .data
	
	
	op1 db 0xa, "Processor is in Protected Mode",0xa
	op1len equ $-op1
	
	op2 db 0xa, "Processor is in Real Mode",0xa
	op2len equ $-op2
	
	op3 db 0xa, "======== SYSTEM REGISTERS ========",0xa, 0xa, "GDTR (Base:Limit) : "
	op3len equ $-op3
	
	op4 db 0xa, "IDTR (Base:Limit) : ",
	op4len equ $-op4
	
	op5 db 0xa, "LDTR (Selector)   : "
	op5len equ $-op5
	
	op6 db 0xa, "TR   (Selector)   : "
	op6len equ $-op6
	
	op7 db 0xa, "MSW  (Flags)      : "
	op7len equ $-op7
	
	op8 db 0xa,0xa, "CPU ID : "
	op8len equ $-op8
	
	sep db ":"
	seplen equ $-sep
	
	n db 0xa
	nl equ $-n

section .bss
	count resb 1
	con48 resb 6
	con32 resb 8
	con16 resb 4
	cont resb 2
	vendorstring resb 13
	
global _start
section .text
_start:	

		smsw ax
		bt ax, 0
		jc pe
		rwcall 1,op2,op2len
		jmp exit
		pe:
			rwcall 1,op1,op1len
		
		
		sgdt [con48] 
		
		mov eax, dword[con48]
		mov byte[count], 08h
		mov rbx, con32
		call h2a32
		rwcall 1,op3,op3len
		rwcall 1,con32,8
		
		mov ax, word[con48+4]
		mov byte[count], 04h
		mov rbx, con16
		call h2a16
		rwcall 1,sep,seplen
		rwcall 1,con16,4


		sidt [con48] 
		
		mov eax, dword[con48]
		mov byte[count], 08h
		mov rbx, con32
		call h2a32
		rwcall 1,op4,op4len
		rwcall 1,con32,8
		
		mov ax, word[con48+4]
		mov byte[count], 04h
		mov rbx, con16
		call h2a16
		rwcall 1,sep,seplen
		rwcall 1,con16,4		
		
		
		sldt [cont] 
		
		mov ax, word[cont]
		mov byte[count], 04h
		mov rbx, con16
		call h2a16
		rwcall 1,op5,op5len
		rwcall 1,con16,4
		

		str [cont] 
		
		mov ax, word[cont]
		mov byte[count], 04h
		mov rbx, con16
		call h2a16
		rwcall 1,op6,op6len
		rwcall 1,con16,4
		
		
		smsw [cont] 
		
		mov ax, word[cont]
		mov byte[count], 04h
		mov rbx, con16
		call h2a16
		rwcall 1,op7,op7len
		rwcall 1,con16,4		
		
		
		mov dword[vendorstring], 0
		mov eax,0
		cpuid
		mov dword[vendorstring + 0], ebx
		mov dword[vendorstring + 4], edx
		mov dword[vendorstring + 8], ecx
		rwcall 1,op8,op8len
		rwcall 1,vendorstring,12
		rwcall 1,n,nl
		
		jmp exit
		
		
		h2a32:	
			again1:
				rol eax,04h
				mov edx,eax
				and edx, 0Fh
				cmp dl, 09h
				jle x
				add  dl, 07h
				x:
					add dl, 30h
				mov [rbx], dl
				inc rbx
				dec byte[count]
				jnz again1
			ret
		
			
		h2a16:	
			again2:
				rol ax,04h
				mov dx,ax
				and dx, 0Fh
				cmp dl, 09h
				jle y
				add  dl, 07h
				y:
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
		
