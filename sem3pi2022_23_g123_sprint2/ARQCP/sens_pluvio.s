.section .data
	.equ charPos, 127
	.equ charNeg, -127
	
.section .text
	.global sens_pluvio

sens_pluvio:

# prologue
	pushq %rbp              # save previous stack frame pointer
	movq %rsp, %rbp         # the stack frame pointer for sum function
        
# body
		
		movsbq  %dil, %r9              # r9 = ultimo valor humidade gerado
        movsbq  %sil,%r11              # r11 = ult_pluvio
        movsbq	%dl,%r8					# r8 = componente aleatoria
        
        cmpq $25,%r11
        jge chovio2
		movq $2,%r10
		cmpq $0,%r8
        jg maior6
        movq $charNeg,%rax               #rax = -127
        movq $-2,%r10
        cqo
        idivq %r10					#rax = -127/2
		cmpq %r8,%rax					# comparamos para saber onde esta o valor aleatorio
		jle metade_final_Neg6
		addq $0,%r9
		movq %r9,%rax
		jmp end6
maior6:
		movq $charPos,%rax               #rax = 127
		movq $2,%r10
		cqo
        idivq %r10					#rax = 127/2
		cmpq %r8,%rax					# comparamos para saber onde esta o valor aleatorio
		jle metade_final_Pos6
		addq $0,%r9
		movq %r9,%rax
		jmp end6
		
metade_final_Pos6:
	cmpq $127,%r8
	je equal_pos6
	addq $1,%r9
	movq %r9,%rax
	jmp end6

metade_final_Neg6:
	cmpq $-127,%r8
	je equal_neg6
	addq $-1,%r9
	movq %r9,%rax
	jmp end6
	
equal_pos6:
	addq $2,%r9
	movq %r9,%rax
	jmp end6
	
chovio2:
	addq %r8,%r9
	movq %r9,%rax
	jmp end6
	
equal_neg6:
	addq $-2,%r9
	movq %r9,%rax

end6:

# epilogue
	movq %rbp, %rsp         #  restore the previous stack pointer ("clear" the stack)
	popq %rbp               #  restore the previous stack frame pointer

	ret
