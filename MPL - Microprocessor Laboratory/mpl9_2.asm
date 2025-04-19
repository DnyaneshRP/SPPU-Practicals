%macro rwocall 4
	mov rax,%1
	mov rdi,%2
	mov rsi,%3
	mov rdx,%4
	syscall
%endmacro

section .data
	
section .bss
	
	extern buffer, spcnt, nlcnt, occnt, cnt1, cnt2, cnt3
	ans resb 2
	count resb 1
	
global functions
section .text
functions:	
		global spaces, lines, occurences
		
		spaces:
			mov rsi, buffer
			up:
				mov al, byte[rsi]
				cmp al, 20h
				jne spi
				inc byte[spcnt]
				spi:
					inc rsi
					dec byte[cnt1]
				jnz up
			mov al, byte[spcnt]
			mov rbx, ans
			mov byte[count], 2
			call h2a
			rwocall 1,1,ans,2 
			ret
			
		lines:
			mov rsi, buffer
			up1:
				mov al, byte[rsi]
				cmp al, 0Ah
				jne nli
				inc byte[nlcnt]
				nli:
					inc rsi
					dec byte[cnt2]
				jnz up1
			mov al, byte[nlcnt]
			mov rbx, ans
			mov byte[count], 2
			call h2a
			rwocall 1,1,ans,2 
			ret
			
		occurences:
			mov rsi, buffer
			up2:
				mov al, byte[rsi]
				cmp al, bl
				jne oci
				inc byte[occnt]
				oci:
					inc rsi
					dec byte[cnt3]
				jnz up2
			mov al, byte[occnt]
			mov rbx, ans
			mov byte[count], 2
			call h2a
			rwocall 1,1,ans,2 		
			ret
			
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
