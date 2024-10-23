.section .data
	
.section .text
	.global sens_velc_vento

sens_velc_vento:
	# ult_velc_vento em %dil - Última velocidade do vento medida (km/h)
	# comp_rand em %sil - Componente aleatório para a geração do novo valor da velocidade do vento
	
	movb %dil, %al  # ult_velc_vento em %al
	incb %al        # incremento ao ult_velc_vento
	addb %sil, %al  # adicionar comp_rand ao %al
	
	# %al - nova medição do valor da velocidade do vento (km/h)
	ret
