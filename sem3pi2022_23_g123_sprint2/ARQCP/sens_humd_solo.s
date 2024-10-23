.section .data
	
.section .text
	.global sens_humd_solo

sens_humd_solo:
	# ult_hmd_solo em %dil - Última humidade do solo medida (percentagem)
	# ult_pluvio em %sil - Último valor de pluviosidade medido (mm)
	# comp_rand em %dl - Componente aleatório para a geração do novo valor da humidade do solo

	gerar_valor_modificacao:
		movb %sil, %al     # ult_pluvio em %al
		movb %dl, %cl      # com_rand em %cl
		imulb %cl          # com_rand * ult_pluvio

	gerar_novo_valor:
		incb %dil           # incrementar ult_hmd_solo
		addb %dil, %al      # valor de modificação + ult_hmd_solo
		
	verificar_valor_gerado:
		cmpb $0, %al
		jl novo_valor_zero  # se o valor é negativo, então vai ser retornado a zero
		jmp fim
		
	novo_valor_zero:
		movb $0, %al
		
		
	fim:
	# %al - nova medição do valor da humidade do solo (percentagem)
	ret
