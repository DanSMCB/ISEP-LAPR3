.section .data
	
.section .text
	.global sens_temp

sens_temp:
	# ult_temp em %dil - Último valor de temperatura medido (°C)
	# comp_rand em %sil - Componente aleatório para a geração do novo valor da temperatura
	
	movb %dil, %al  # ult_temp em %al
	incb %al        # incremento ao ult_temp
	addb %sil, %al  # adicionar comp_rand ao %al
	
	# %al - nova medição do valor da temperatura (°C)
	ret
	
	
