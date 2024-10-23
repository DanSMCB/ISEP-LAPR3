#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <unistd.h>
#include "update_matrix.h" 
#include "sensores.h" 

unsigned long frequencia_temp;
unsigned long frequencia_velcVent;
unsigned long frequencia_dirVento;
unsigned long frequencia_pluvio;
unsigned long frequencia_hmdAtm;
unsigned long frequencia_hmdSolo;
unsigned char ult_temp = 0;
unsigned char ult_velcVento = 0;
unsigned char ult_dirVento = 0;
unsigned char ult_pluvio = 0;
unsigned char ult_humAtm = 0;
unsigned char ult_humSolo = 0;
unsigned short id = 0;
double matrizDiaria[6][3];

//Preencher sensor
Sensor preecher_sensor(Sensor s){

	printf("Introduza o valor máximo do sensor: \n");
	scanf("%hd", &s.max_limit);
	
	while (s.max_limit < 0){
		printf("Opção Inválida. Introduza o valor máximo do sensor novamente: \n");
		scanf("%hd", &s.max_limit);
	}

	printf("Introduza o valor mínimo do sensor: \n");
	scanf("%hd", &s.min_limit);
	
	while (s.min_limit < 0 || s.min_limit > s.max_limit){
		printf("Opção Inválida. Introduza o valor mínimo do sensor novamente: \n");
		scanf("%hd", &s.min_limit);
	}
	
	id++;
	s.id = id;

	printf("Introduza o valor máximo de possíveis leituras erradas: \n");
	scanf("%d", &s.n_erradas);
	
	while (s.n_erradas < 0){
		printf("Opção Inválida. Introduza novamente o valor máximo de possíveis leituras erradas: \n");
		scanf("%d", &s.n_erradas);
	}
	
	printf("\n");

   switch(s.sensor_type){
			case 1:
                s.frequency = frequencia_temp;
			    s.readings_size = (24*3600)/s.frequency;
				ult_temp = gerar_valor_temperatura(s, ult_temp);
				
				break;

			case 2:
                s.frequency = frequencia_velcVent;
			    s.readings_size = (24*3600)/s.frequency;
				gerar_valor_veloc_vento (s, ult_velcVento);
				break;

			case 3:
                s.frequency = frequencia_dirVento;
			    s.readings_size = (24*3600)/s.frequency;
				gerar_valor_dir_vento (s, ult_dirVento);
				break;

			case 4:
                s.frequency = frequencia_pluvio;
			    s.readings_size = (24*3600)/s.frequency;
				ult_pluvio = gerar_valor_pluviosidade (s, ult_pluvio, ult_temp);
				break;

			case 5:
                s.frequency = frequencia_hmdAtm;
			    s.readings_size = (24*3600)/s.frequency;
				gerar_valor_humidade_atm (s, ult_humAtm, ult_pluvio);
				break;

			case 6:
                s.frequency = frequencia_hmdSolo;
			    s.readings_size = (24*3600)/s.frequency;
				gerar_valor_humidade_solo (s, ult_humSolo, ult_pluvio);
				break;
	}
	return s;
	//updateMatrix(matrizDiaria, s.readings, s.readings_size, s.id);
}

int main(void){
	int menu;
	unsigned int n_sensores_temp;
	unsigned int n_sensores_velcVent;
	unsigned int n_sensores_dirVent;
	unsigned int n_sensores_pluvio;
	unsigned int n_sensores_hmdSolo;
	unsigned int n_sensores_hmdAtm;

	/*TEMPERATURA*/

		printf("\nQuantos sensores do tipo temperatura pretende inserir? \n");
		scanf("%d", &n_sensores_temp);
		
		while (n_sensores_temp < 0){
			printf("Opção Inválida. Quantos sensores do tipo temperatura pretende inserir? \n");
			scanf("%d", &n_sensores_temp);
		}

		Sensor* ptr_temp = (Sensor*) calloc (n_sensores_temp, sizeof(Sensor));

		printf("Introduza o valor da frequência em segundos: \n");
		scanf("%ld", &frequencia_temp);
		
		while (frequencia_temp < 0){
			printf("Opção Inválida. Introduza o valor da frequência em segundos: \n");
			scanf("%ld", &frequencia_temp);
		}

		for(int i=0; i<n_sensores_temp; i++){
			Sensor s_temp;
			s_temp.sensor_type = 1;
			s_temp = preecher_sensor(s_temp);
			*(ptr_temp + i) = s_temp;
		}

		/*VELOCIDADE VENTO*/

		printf("\nQuantos sensores do tipo velocidade do vento pretende inserir? \n");
		scanf("%d", &n_sensores_velcVent);
		
		while (n_sensores_velcVent < 0){
			printf("Opção Inválida. Quantos sensores do tipo velocidade do vento pretende inserir? \n");
			scanf("%d", &n_sensores_velcVent);
		}

		Sensor *ptr_velocVent = (Sensor*) calloc (n_sensores_velcVent, sizeof(Sensor));

		printf("Introduza o valor da frequência em segundos: \n");
		scanf("%ld", &frequencia_velcVent);
		
		while (frequencia_velcVent < 0){
			printf("Opção Inválida. Introduza o valor da frequência em segundos: \n");
			scanf("%ld", &frequencia_velcVent);
		}

		for(int i=0; i<n_sensores_velcVent; i++){
			Sensor s_velocVent;
			s_velocVent.sensor_type = 2;
			s_velocVent = preecher_sensor(s_velocVent);
			*(ptr_velocVent + i) = s_velocVent;
		}


		/*DIREÇÃO VENTO*/

		printf("\nQuantos sensores do tipo direção do vento pretende inserir? \n");
		scanf("%d", &n_sensores_dirVent);
		
		while (n_sensores_dirVent < 0){
			printf("Opção Inválida. Quantos sensores do tipo direção do vento pretende inserir? \n");
			scanf("%d", &n_sensores_dirVent);
		}

		Sensor *ptr_dirVento = (Sensor*) calloc (n_sensores_dirVent, sizeof(Sensor));

		printf("Introduza o valor da frequência em segundos: \n");
		scanf("%ld", &frequencia_dirVento);
		
		while (frequencia_dirVento < 0){
			printf("Opção Inválida. Introduza o valor da frequência em segundos: \n");
			scanf("%ld", &frequencia_dirVento);
		}

		for(int i=0; i<n_sensores_dirVent; i++){
			Sensor s_dirVent;
			s_dirVent.sensor_type = 3;
			s_dirVent = preecher_sensor(s_dirVent);
			*(ptr_dirVento + i) = s_dirVent;
		}


		/*PLUVIOSIDADE*/

		printf("\nQuantos sensores do tipo pluviosidade pretende inserir? \n");
		scanf("%d", &n_sensores_pluvio);

		while (n_sensores_pluvio < 0){
			printf("Opção Inválida. Quantos sensores do tipo pluviosidade pretende inserir? \n");
			scanf("%d", &n_sensores_pluvio);
		}

		Sensor *ptr_pluvio = (Sensor*) calloc (n_sensores_pluvio, sizeof(Sensor));

		printf("Introduza o valor da frequência em segundos: \n");
		scanf("%ld", &frequencia_pluvio);
		
		while (frequencia_pluvio < 0){
			printf("Opção Inválida. Introduza o valor da frequência em segundos: \n");
			scanf("%ld", &frequencia_pluvio);
		}

		for(int i=0; i<n_sensores_pluvio; i++){
			Sensor s_pluvio;
			s_pluvio.sensor_type = 4;
			s_pluvio = preecher_sensor(s_pluvio);
			*(ptr_pluvio + i) = s_pluvio;
		}


		/*HUMIDADE ATM*/

		printf("\nQuantos sensores do tipo humidade atmosférica pretende inserir? \n");
		scanf("%d", &n_sensores_hmdAtm);
		
		while (n_sensores_hmdAtm < 0){
			printf("Opção Inválida. Quantos sensores do tipo humidade atmosférica pretende inserir? \n");
			scanf("%d", &n_sensores_hmdAtm);
		}

		Sensor *ptr_hmdAtm = (Sensor*) calloc (n_sensores_hmdAtm, sizeof(Sensor));

		printf("Introduza o valor da frequência em segundos: \n");
		scanf("%ld", &frequencia_hmdAtm);
		
		while (frequencia_hmdAtm < 0){
			printf("Opção Inválida. Introduza o valor da frequência em segundos: \n");
			scanf("%ld", &frequencia_hmdAtm);
		}

		for(int i=0; i<n_sensores_hmdAtm; i++){
			Sensor s_hmdAtm;
			s_hmdAtm.sensor_type = 5;
			s_hmdAtm = preecher_sensor(s_hmdAtm);
			*(ptr_hmdAtm + i) = s_hmdAtm;
		}


		/*HUMIDADE SOLO*/

		printf("\nQuantos sensores do tipo humidade solo pretende inserir? \n");
		scanf("%d", &n_sensores_hmdSolo);
		
		while (n_sensores_hmdSolo < 0){
			printf("Opção Inválida. Quantos sensores do tipo humidade do solo pretende inserir? \n");
			scanf("%d", &n_sensores_hmdSolo);
		}

		Sensor *ptr_hmdSolo = (Sensor*) calloc (n_sensores_hmdSolo, sizeof(Sensor));

		printf("Introduza o valor da frequência em segundos: \n");
		scanf("%ld", &frequencia_hmdSolo);
		
		while (frequencia_hmdSolo < 0){
			printf("Opção Inválida. Introduza o valor da frequência em segundos: \n");
			scanf("%ld", &frequencia_hmdSolo);
		}

		for(int i=0; i<n_sensores_hmdSolo; i++){
			Sensor s_hmdSolo;
			s_hmdSolo.sensor_type = 6;
			s_hmdSolo = preecher_sensor(s_hmdSolo);
			*(ptr_hmdSolo + i) = s_hmdSolo;
		}

	do{

		unsigned char tipo_sensor_adicionar;
		unsigned char tipo_sensor_alterar;
		unsigned char tipo_sensor_eliminar;
		unsigned int nmr_sensores_adicionar;
		unsigned int nmr_sensores_eliminar;
		
		unsigned long frequencia_nova;
		char filename[50];

		printf("\nInsira: \n 1.Se deseja continuar a inserir sensores \n 2.Se deseja eliminar um sensor \n 3.Se deseja alterar a frequência de leituras de um sensor \n 4.Se deseja exportar para um ficheiro CSV, os dados e leituras de cada um dos sensores \n 5.Se deseja exportar para um ficheiro CSV os dados da matriz diaria de resumo \n 6.Se deseja sair \n");
		scanf("%d", &menu);

		while (menu < 1 || menu > 6){
			printf("Opcão inválida. Insira de novo: \n");
			scanf("%d", &menu);
		} 

		if(menu==1){			
			
			printf("Inserir novo sensor\n");
			printf("\nEscolha o tipo de sensor que pretende inserir: \n 1.Temperatura \n 2.Velocidade do Vento \n 3.Direção do Vento \n 4.Pluviosidade \n 5.Humidade Atmosférica \n 6.Humidade do Solo \n");
			scanf("%hhd", &tipo_sensor_adicionar);
			
			while (tipo_sensor_adicionar < 1 || tipo_sensor_adicionar > 6){
				printf("Opcão inválida. Insira de novo: \n");
				scanf("%hhd", &tipo_sensor_adicionar);
			} 
			
			printf("\nQuantos sensores deste tipo pretende adicionar?\n");
			scanf("%d", &nmr_sensores_adicionar);
			
			Sensor *ptr_temporario = NULL;
			
			switch(tipo_sensor_adicionar){
				
				case 1:
				ptr_temporario = (Sensor*) realloc(ptr_temp,(n_sensores_temp + nmr_sensores_adicionar) * sizeof(Sensor));
				if(ptr_temporario!= NULL){
					
					for(int i = 0; i < nmr_sensores_adicionar; i++){
						Sensor s_novo;
						s_novo.sensor_type = 1;
						s_novo = preecher_sensor(s_novo); /*preenche estrutura daquele tipo com novo sensor*/
						*(ptr_temporario+ n_sensores_temp + i) = s_novo;
					}
					
					ptr_temp = ptr_temporario;
					n_sensores_temp += nmr_sensores_adicionar;
					ptr_temporario = NULL;
					printf("\n %d sensor(es) de temperatura adicionados",nmr_sensores_adicionar);

				}
				break;
				
				case 2: 
				ptr_temporario = (Sensor*) realloc(ptr_velocVent,(n_sensores_velcVent + nmr_sensores_adicionar) * sizeof(Sensor));
				if(ptr_temporario!= NULL){
					for(int i = 0; i < nmr_sensores_adicionar; i++){
						Sensor s_novo;
						s_novo.sensor_type = 2;
						s_novo = preecher_sensor(s_novo); /*preenche estrutura daquele tipo com novo sensor*/
						*(ptr_temporario+ n_sensores_velcVent + i) = s_novo;
					}
					ptr_velocVent = ptr_temporario;
					n_sensores_velcVent += nmr_sensores_adicionar;
					ptr_temporario = NULL;
					printf("\n %d sensor(es) de velocidade de vento adicionados",nmr_sensores_adicionar);

				}
				break;
				
				case 3:
				ptr_temporario = (Sensor*) realloc(ptr_dirVento,(n_sensores_dirVent + nmr_sensores_adicionar) * sizeof(Sensor));
				if(ptr_temporario!= NULL){
					for(int i = 0; i < nmr_sensores_adicionar; i++){
						Sensor s_novo;
						s_novo.sensor_type = 3;
						s_novo = preecher_sensor(s_novo); /*preenche estrutura daquele tipo com novo sensor*/
						*(ptr_temporario+ n_sensores_dirVent + i) = s_novo;
					}
					ptr_dirVento = ptr_temporario;
					n_sensores_dirVent += nmr_sensores_adicionar;
					ptr_temporario = NULL;
					printf("\n %d sensor(es) de direção do vento adicionados",nmr_sensores_adicionar);

				}
				break;
				
				case 4:
				ptr_temporario = (Sensor*) realloc(ptr_pluvio,(n_sensores_pluvio + nmr_sensores_adicionar) * sizeof(Sensor));
				if(ptr_temporario!= NULL){
					for(int i = 0; i < nmr_sensores_adicionar; i++){
						Sensor s_novo;
						s_novo.sensor_type = 4;
						s_novo = preecher_sensor(s_novo); /*preenche estrutura daquele tipo com novo sensor*/
						*(ptr_temporario+ n_sensores_pluvio + i) = s_novo;
					}
					ptr_pluvio = ptr_temporario;
					n_sensores_pluvio += nmr_sensores_adicionar;
					ptr_temporario = NULL;
					printf("\n %d sensor(es) de pluviosidade adicionados",nmr_sensores_adicionar);

				}
				break;
				
				case 5:
				ptr_temporario = (Sensor*) realloc(ptr_hmdAtm,(n_sensores_hmdAtm + nmr_sensores_adicionar) * sizeof(Sensor));
				if(ptr_temporario!= NULL){
					for(int i = 0; i < nmr_sensores_adicionar; i++){
						Sensor s_novo;
						s_novo.sensor_type = 5;
						s_novo = preecher_sensor(s_novo); /*preenche estrutura daquele tipo com novo sensor*/
						*(ptr_temporario+ n_sensores_hmdAtm + i) = s_novo;
					}
					ptr_hmdAtm = ptr_temporario;
					n_sensores_hmdAtm += nmr_sensores_adicionar;
					ptr_temporario = NULL;
					printf("\n %d sensor(es) de humidade atmosférica adicionados",nmr_sensores_adicionar);

				}
				break;
				
				case 6:
				ptr_temporario = (Sensor*) realloc(ptr_hmdSolo,(n_sensores_hmdSolo + nmr_sensores_adicionar) * sizeof(Sensor));
				if(ptr_temporario!= NULL){
					for(int i = 0; i < nmr_sensores_adicionar; i++){
						Sensor s_novo;
						s_novo.sensor_type = 1;
						s_novo = preecher_sensor(s_novo); /*preenche estrutura daquele tipo com novo sensor*/
						*(ptr_temporario+ n_sensores_hmdSolo + i) = s_novo;
					}
					ptr_hmdSolo = ptr_temporario;
					n_sensores_hmdSolo += nmr_sensores_adicionar;
					ptr_temporario = NULL;
					printf("\n %d sensor(es) de humidade do solo adicionados",nmr_sensores_adicionar);

				}
				break;
			}
		}

		if(menu==2){
			
			printf("\nEliminar sensor/sensores");
			printf("\nEscolha o tipo de sensor que pretende eliminar: \n 1.Temperatura \n 2.Velocidade do Vento \n 3.Direção do Vento \n 4.Pluviosidade \n 5.Humidade Atmosférica \n 6.Humidade do Solo \n");
			scanf("%hhd", &tipo_sensor_eliminar);
			printf("\nQuantos sensores do tipo escolhido pretende eliminar? \n");
			scanf("%d", &nmr_sensores_eliminar);

			Sensor *ptr_temporario = NULL;
			
			switch(tipo_sensor_eliminar){
				
				case 1:
				ptr_temporario = (Sensor*) realloc(ptr_temp, (n_sensores_temp - nmr_sensores_eliminar) * sizeof(Sensor));
				if (ptr_temporario != NULL){
					ptr_temp = ptr_temporario;
					n_sensores_temp = n_sensores_temp - nmr_sensores_eliminar;
					ptr_temporario = NULL;
					printf("\n%d sensor(es) de temperatura eliminados",nmr_sensores_eliminar);
				}
				break;

				case 2:
				ptr_temporario = (Sensor*) realloc(ptr_velocVent, (n_sensores_velcVent - nmr_sensores_eliminar) * sizeof(Sensor));
				if (ptr_temporario != NULL){
					ptr_velocVent = ptr_temporario;
					n_sensores_velcVent = n_sensores_velcVent - nmr_sensores_eliminar;
					ptr_temporario = NULL;
					printf("\n%d sensor(es) de velocidade de vento eliminados",nmr_sensores_eliminar);
				}
				break;

				case 3:
				ptr_temporario = (Sensor*) realloc(ptr_dirVento, (n_sensores_dirVent - nmr_sensores_eliminar) * sizeof(Sensor));
				if (ptr_temporario != NULL){
					ptr_dirVento = ptr_temporario;
					n_sensores_dirVent = n_sensores_dirVent - nmr_sensores_eliminar;
					ptr_temporario = NULL;
					printf("\n%d sensor(es) de direção de vento eliminados",nmr_sensores_eliminar);
				}
				break;

				case 4:
				ptr_temporario = (Sensor*) realloc(ptr_pluvio, (n_sensores_pluvio - nmr_sensores_eliminar) * sizeof(Sensor));
				if (ptr_temporario != NULL){
					ptr_pluvio = ptr_temporario;
					n_sensores_pluvio = n_sensores_pluvio - nmr_sensores_eliminar;
					ptr_temporario = NULL;
					printf("\n%d sensor(es) de pluviosidade eliminados",nmr_sensores_eliminar);
				}
				break;

				case 5:
				ptr_temporario = (Sensor*) realloc(ptr_hmdAtm, (n_sensores_hmdAtm - nmr_sensores_eliminar) * sizeof(Sensor));
				if (ptr_temporario != NULL){
					ptr_hmdAtm = ptr_temporario;
					n_sensores_hmdAtm = n_sensores_hmdAtm -nmr_sensores_eliminar;
					ptr_temporario = NULL;
					printf("\n%d sensor(es) de humidade atmosférica eliminados",nmr_sensores_eliminar);
				}
				break;

				case 6:
				ptr_temporario = (Sensor*) realloc(ptr_hmdSolo, (n_sensores_hmdSolo - nmr_sensores_eliminar) * sizeof(Sensor));
				if (ptr_temporario != NULL){
					ptr_hmdSolo = ptr_temporario;
					n_sensores_hmdSolo = n_sensores_hmdSolo - nmr_sensores_eliminar;
					ptr_temporario = NULL;
					printf("\n%d sensor(es) de humidade do solo eliminados",nmr_sensores_eliminar);
				}
				break;
			}
		}

		if(menu==3){
			printf("\nAlterar a frequência de leituras de um sensor\n");
			printf("\nEscolha o tipo de sensor para o qual pretende alterar a frequência: \n 1.Temperatura \n 2.Velocidade do Vento \n 3.Direção do Vento \n 4.Pluviosidade \n 5.Humidade Atmosférica \n 6.Humidade do Solo \n");
			scanf("%hhd", &tipo_sensor_alterar);
			printf("Introduza a nova frequência: \n");
			scanf("%ld", &frequencia_nova);

			unsigned short *readings_tmp = NULL;
			unsigned short *readings = NULL;
			unsigned long readings_size = ptr_temp->readings_size;
			long diferenca;
				
			switch(tipo_sensor_alterar){
				case 1: 
				if(frequencia_nova > frequencia_temp){
					diferenca = (24*3600) / (frequencia_nova - frequencia_temp);
					for(int i = 0; i < n_sensores_temp; i++){
						readings = (ptr_temp + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size - diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_temp + i)->readings = readings_tmp;
							(ptr_temp + i)->frequency = frequencia_nova;
							(ptr_temp + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				if(frequencia_nova < frequencia_temp){
					diferenca = (24*3600) / (frequencia_temp - frequencia_nova);
					for(int i = 0; i < n_sensores_temp; i++){
						readings = (ptr_temp + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size + diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_temp + i)->readings = readings_tmp;
							(ptr_temp + i)->frequency = frequencia_nova;
							(ptr_temp + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				printf("\nFrequência de leituras alterada\n");
				break;

				case 2:
				if(frequencia_nova > frequencia_velcVent){
					diferenca = (24*3600) / (frequencia_nova - frequencia_velcVent);
					for(int i = 0; i < n_sensores_velcVent; i++){
						readings = (ptr_velocVent + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size - diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_velocVent + i)->readings = readings_tmp;
							(ptr_velocVent + i)->frequency = frequencia_nova;
							(ptr_velocVent + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				if(frequencia_nova < frequencia_velcVent){
					diferenca = (24*3600) / (frequencia_velcVent - frequencia_nova);
					for(int i = 0; i < n_sensores_velcVent; i++){
						readings = (ptr_velocVent + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size + diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_velocVent + i)->readings = readings_tmp;
							(ptr_velocVent + i)->frequency = frequencia_nova;
							(ptr_velocVent + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				printf("\nFrequência de leituras alterada\n");
				break;

				case 3:
				if(frequencia_nova > frequencia_dirVento){
					diferenca = (24*3600) / (frequencia_nova - frequencia_dirVento);
					for(int i = 0; i < n_sensores_dirVent; i++){
						readings = (ptr_dirVento + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size - diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_dirVento + i)->readings = readings_tmp;
							(ptr_dirVento + i)->frequency = frequencia_nova;
							(ptr_dirVento + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				if(frequencia_nova < frequencia_dirVento){
					diferenca = (24*3600) / (frequencia_dirVento - frequencia_nova);
					for(int i = 0; i < n_sensores_dirVent; i++){
						readings = (ptr_dirVento + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size + diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_dirVento + i)->readings = readings_tmp;
							(ptr_dirVento + i)->frequency = frequencia_nova;
							(ptr_dirVento + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				printf("\nFrequência de leituras alterada\n");
				break;

				case 4:
				if(frequencia_nova > frequencia_pluvio){
					diferenca = (24*3600) / (frequencia_nova - frequencia_pluvio);
					for(int i = 0; i < n_sensores_pluvio; i++){
						readings = (ptr_pluvio + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size - diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_pluvio + i)->readings = readings_tmp;
							(ptr_pluvio + i)->frequency = frequencia_nova;
							(ptr_pluvio + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				if(frequencia_nova < frequencia_pluvio){
					diferenca = (24*3600) / (frequencia_pluvio - frequencia_nova);
					for(int i = 0; i < n_sensores_pluvio; i++){
						readings = (ptr_pluvio + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size + diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_pluvio + i)->readings = readings_tmp;
							(ptr_pluvio + i)->frequency = frequencia_nova;
							(ptr_pluvio + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				printf("\nFrequência de leituras alterada\n");
				break;

				case 5:
				if(frequencia_nova > frequencia_hmdAtm){
					diferenca = (24*3600) / (frequencia_nova - frequencia_hmdAtm);
					for(int i = 0; i < n_sensores_hmdAtm; i++){
						readings = (ptr_hmdAtm + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size - diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_hmdAtm + i)->readings = readings_tmp;
							(ptr_hmdAtm + i)->frequency = frequencia_nova;
							(ptr_hmdAtm + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				if(frequencia_nova < frequencia_hmdAtm){
					diferenca = (24*3600) / (frequencia_hmdAtm - frequencia_nova);
					for(int i = 0; i < n_sensores_hmdAtm; i++){
						readings = (ptr_hmdAtm + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size + diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_hmdAtm + i)->readings = readings_tmp;
							(ptr_hmdAtm + i)->frequency = frequencia_nova;
							(ptr_hmdAtm + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				printf("\nFrequência de leituras alterada\n");
				break;

				case 6:
				if(frequencia_nova > frequencia_hmdSolo){
					diferenca = (24*3600) / (frequencia_nova - frequencia_hmdSolo);
					for(int i = 0; i < n_sensores_hmdSolo; i++){
						readings = (ptr_hmdSolo + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size - diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_hmdSolo + i)->readings = readings_tmp;
							(ptr_hmdSolo + i)->frequency = frequencia_nova;
							(ptr_hmdSolo + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				if(frequencia_nova < frequencia_hmdSolo){
					diferenca = (24*3600) / (frequencia_hmdSolo - frequencia_nova);
					for(int i = 0; i < n_sensores_hmdSolo; i++){
						readings = (ptr_hmdSolo + i)->readings;
						readings_tmp = (unsigned short*) realloc(readings, (readings_size + diferenca) * sizeof(short));
						if(readings_tmp!=NULL){
							(ptr_hmdSolo + i)->readings = readings_tmp;
							(ptr_hmdSolo + i)->frequency = frequencia_nova;
							(ptr_hmdSolo + i)->readings_size = (24*3600)/frequencia_nova;
						}
					}
				}
				printf("\nFrequência de leituras alterada\n");
				break;  
			}

			
		}

		if(menu==4){
			printf("CSV com dados e leituras de cada um dos sensores: \n");
			printf("Introduza o nome do ficheiro para exportar os dados dos sensores: \n");
			scanf("%s", filename);

			FILE *fpt;

			fpt = fopen(filename, "w+");

			fprintf(fpt,"***Sensores de temperatura***\n");

			for(int i = 0; i < n_sensores_temp; i++){
			   Sensor s;
			   s = *(ptr_temp+i); 
                                
			   fprintf(fpt,"Sensor ID: %d\n",s.id);
			   fprintf(fpt,"Frequency: %ld\n",s.frequency);
			   fprintf(fpt,"Max limit: %hd\n",s.max_limit);
               fprintf(fpt,"Max limit: %hd\n",s.min_limit);
               fprintf(fpt,"Readings:\n");
               
               //for(int j = 0; j < s.readings_size; j++){
					//fprintf(fpt,"%d\n", s.readings[j]); 
               //}
               
               fprintf(fpt,"Número de leituras erradas: %d\n",s.min_limit);
		      }

                  fprintf(fpt,"***Sensores de velocidade de vento***\n");                  

			for(int i = 0; i < n_sensores_velcVent; i++){
			   Sensor s;
			   s = *(ptr_velocVent+i);
    
			   fprintf(fpt,"Sensor ID: %d\n",s.id);
			   fprintf(fpt,"Frequency: %ld\n",s.frequency);
			   fprintf(fpt,"Max limit: %hd\n",s.max_limit);
                     fprintf(fpt,"Max limit: %hd\n",s.min_limit);
                     fprintf(fpt,"Readings:\n");
                 
                      //for(int j = 0; j < s.readings_size; j++){
						//fprintf(fpt,"%d\n", s.readings[j]); 
					//}
                      
                    fprintf(fpt,"Número de leituras erradas: %d\n",s.min_limit);
			}
                   
                  fprintf(fpt,"***Sensores de direção de vento***\n");                  

			for(int i = 0; i < n_sensores_dirVent; i++){
			Sensor s;
			s = *(ptr_dirVento+i);
			    fprintf(fpt,"Sensor ID: %d\n",s.id);
			    fprintf(fpt,"Frequency: %ld\n",s.frequency);
			    fprintf(fpt,"Max limit: %hd\n",s.max_limit);
                      fprintf(fpt,"Max limit: %hd\n",s.min_limit);
                      fprintf(fpt,"Readings:\n");
                 
                     //for(int j = 0; j < s.readings_size; j++){
					//fprintf(fpt,"%d\n", s.readings[j]); 
					//}
                      fprintf(fpt,"Número de leituras erradas: %d\n",s.min_limit);
			}
                    
                  fprintf(fpt,"***Sensores de pluviosidade***\n");                  

			for(int i = 0; i < n_sensores_pluvio; i++){
			    Sensor s;
			    s = *(ptr_pluvio+i);
			    
                      fprintf(fpt,"Sensor ID: %d\n",s.id);
			    fprintf(fpt,"Frequency: %ld\n",s.frequency);
			    fprintf(fpt,"Max limit: %hd\n",s.max_limit);
                      fprintf(fpt,"Max limit: %hd\n",s.min_limit);
                      fprintf(fpt,"Readings:\n");
                
                      //for(int j = 0; j < s.readings_size; j++){
					//fprintf(fpt,"%d\n", s.readings[j]); 
                    //}
                      fprintf(fpt,"Número de leituras erradas: %d\n",s.min_limit);
			}
                  
                  fprintf(fpt,"***Sensores de humidade solo***\n");                  

			for(int i = 0; i < n_sensores_hmdSolo; i++){
			   Sensor s;
			   s = *(ptr_hmdSolo + i);
			    
                fprintf(fpt,"Sensor ID: %d\n",s.id);
			    fprintf(fpt,"Frequency: %ld\n",s.frequency);
			    fprintf(fpt,"Max limit: %hd\n",s.max_limit);
                fprintf(fpt,"Max limit: %hd\n",s.min_limit);
                      fprintf(fpt,"Readings:\n");
                 
                      //for(int j = 0; j < s.readings_size; j++){
					//fprintf(fpt,"%d\n", s.readings[j]); 
                     //}
                      fprintf(fpt,"Número de leituras erradas: %d\n",s.min_limit);
				  }

                  fprintf(fpt,"***Sensores de humidade atmosférica***\n");                  

			for(int i = 0; i < n_sensores_hmdAtm; i++){
			    Sensor s;
			    s = *(ptr_hmdAtm+i);
			    
                fprintf(fpt,"Sensor ID: %d\n",s.id);
			    fprintf(fpt,"Frequency: %ld\n",s.frequency);
			    fprintf(fpt,"Max limit: %hd\n",s.max_limit);
                fprintf(fpt,"Max limit: %hd\n",s.min_limit);
                fprintf(fpt,"Readings:\n");
                 
				//for(int j = 0; j < s.readings_size; j++){
					//fprintf(fpt,"%d\n", s.readings[j]); 
				//}

                fprintf(fpt,"Número de leituras erradas: %d\n",s.min_limit);
			}
		}
		
                                                                              
		
		if(menu==5){
			printf("CSV com dados da matriz diaria: \n");
			printf("Introduza o nome do ficheiro para exportar os dados da matriz: \n");
			scanf("%s", filename);
			
			FILE *fpt1;

                  fpt1 = fopen(filename, "w+");

                  fprintf(fpt1,"Minimo,Maximo,Media\n");

                  int i, j;

                  for(i = 0; i < 6; i++){
                     for(j = 0; j < 3; j++){
                //         fprintf(fpt1,"%f ",((matrix + i) + j));
                     }
                  fprintf(fpt1,"\n");
                  }
                  fclose(fpt1);
			}

	}while(menu!=6);

	free(ptr_temp);
	free(ptr_velocVent);
	free(ptr_dirVento);
	free(ptr_pluvio);
	free(ptr_hmdAtm);
	free(ptr_hmdSolo);

	printf("\nProcesso terminado\n");

	return 0;
}



