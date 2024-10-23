.section .data
	
.section .text
	.global sens_humd_atm

sens_humd_atm:
	# ult_humd_atm em %dil - Última humidade atmosférica medida (percentagem)
	# ult_pluvio em %sil - Último valor de pluviosidade medido (mm)
	# comp_rand em %dl - Componente aleatório para a geração do novo valor da humidade do solo

	gerar_valor_modificacao:
		movb %sil, %al     # ult_pluvio em %al
		movb %dl, %cl      # com_rand em %cl
		imulb %cl          # com_rand * ult_pluvio

	gerar_novo_valor:
		incb %dil           # incrementar ult_humd_atm
		addb %dil, %al      # valor de modificação + ult_humd_atm
		
	verificar_valor_gerado:
		cmpb $0, %al
		jl novo_valor_zero  # se o valor é negativo, então vai ser retornado a zero
		jmp fim
		
	novo_valor_zero:
		movb $0, %al
		
	fim:
		# %al - nova medição do valor da humidade atmosférica (percentagem)
		ret
