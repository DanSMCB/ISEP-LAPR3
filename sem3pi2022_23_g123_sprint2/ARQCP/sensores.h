typedef struct {
	 unsigned short id;
	 unsigned char sensor_type;
	 unsigned short max_limit; // limites do sensor
	 unsigned short min_limit; 
	 unsigned long frequency; // frequency de leituras (em segundos)
	 unsigned long readings_size; // tamanho do array de leituras 
	 unsigned short *readings; // array de leituras diárias
	 unsigned int n_erradas;   // numero de leitura erradas
} Sensor;



//Sensor temperatura
char gerar_valor_temperatura (Sensor s, unsigned char ult_temp);

//Sensor velocidade vento
void gerar_valor_veloc_vento (Sensor s, unsigned char ult_velcVento);

//Sensor direção vento
void gerar_valor_dir_vento (Sensor s, unsigned char ult_dirVento);

//Sensor pluviosidade
char gerar_valor_pluviosidade (Sensor s, unsigned char ult_pluvio, char ult_temp);

//Sensor thumidade atmosférica
void gerar_valor_humidade_atm (Sensor s, unsigned char ult_pluvio, char ult_humAtm);

//Sensor humidade solo
void gerar_valor_humidade_solo (Sensor s, unsigned char ult_pluvio, char ult_humSolo);

