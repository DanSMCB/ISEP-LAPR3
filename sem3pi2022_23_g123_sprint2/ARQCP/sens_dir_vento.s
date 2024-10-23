.section .data
	
.section .text
	.global sens_dir_vento

sens_dir_vento:
	# ult_dir_vento em %di - Última direção do vento medida (graus)
	# comp_rand em %si - Componente aleatório para a geração do novo valor da direção do vento
	
	movw %di, %ax  # ult_dir_vento em %al
	incw %ax       # incremento ao ult_dir_vento
	addw %si, %ax  # adicionar comp_rand ao %al
	
	# %ax - nova medição do valor da direção do vento (graus)
	ret
	
