.section .data

	.global state
	.global inc

.section .text
	
	.global pcg32_random_r
	
pcg32_random_r:

	#prologue
	pushq	%rbp
	movq	%rsp, %rbp
	
	movq	state(%rip), %rax
	movq	%rax, -8(%rbp)
	movq	-8(%rbp), %rax
	movabsq	$6364136223846793005, %rdx
	imulq	%rax, %rdx
	movq	inc(%rip), %rax
	orq	$1, %rax
	addq	%rdx, %rax
	movq	%rax, state(%rip)
	movq	-8(%rbp), %rax
	shrq	$18, %rax
	xorq	-8(%rbp), %rax
	shrq	$27, %rax
	movl	%eax, -16(%rbp) #-12 -> -16
	movq	-8(%rbp), %rax
	shrq	$59, %rax
	movl	%eax, -24(%rbp) # -16 -> -24
	movl	-24(%rbp), %eax
	movl	-16(%rbp), %edx
	movl	%eax, %ecx
	rorl	%cl, %edx
	movl	%edx, %eax
	
	#epilogue
	popq	%rbp
	
	ret
