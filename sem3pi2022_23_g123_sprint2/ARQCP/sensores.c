#include "sensores.h"
#include "sens_dir_vento.h"
#include "sens_humd_atm.h"
#include "sens_humd_solo.h"
#include "sens_pluvio.h"
#include "sens_temp.h"
#include "sens_velc_vento.h"
#include "demo_pcg.h"
#include "read_rnd.h"
#include <stdio.h>



//Sensor temperatura
char gerar_valor_temperatura (Sensor s, unsigned char ult_temp){
	int contador_temp = 0;
	char comp_rand;
	unsigned short vec[s.readings_size];
	unsigned char valor_temp; 
	
	for(int i=0; i<s.readings_size; i++){
		
		do{
			if(read_rnd()==0){
				comp_rand = (char) pcg32_random_r();
				
			}
		}while (comp_rand > 20 || comp_rand < -10);
		
		//printf("comp rand = %d \n\n", comp_rand);
	
	
		valor_temp = sens_temp(ult_temp, comp_rand);
		//printf("valor_temp = %d\n\n", valor_temp);
		
		vec[i]  = (unsigned short) valor_temp;
		if(valor_temp > s.max_limit || valor_temp < s.min_limit){
			contador_temp++;
			
			if (contador_temp == s.n_erradas){
				for (int j=0; j<s.n_erradas; j++){
					vec[i-j] = 0;
				}
				contador_temp = 0;
				ult_temp = 0;
			}else{
				ult_temp = valor_temp;
			}
		} else {
			ult_temp = valor_temp;
		}
		
		//printf("ult_valor_temp = %d\n\n", ult_temp);
	}	
	
	s.readings = vec;
	
	/*for (int i=0; i<s.readings_size; i++){
		printf("pos = %d -> %d \n", i, s.readings[i]);
	}*/
	
	return ult_temp;
}


//Sensor velocidade vento
void gerar_valor_veloc_vento (Sensor s, unsigned char ult_velcVento){
	int contador_velcVento = 0;
	unsigned char valor_velc_vento; 
	char comp_rand;
	unsigned short vec[s.readings_size];
	
	for(int i=0; i<s.readings_size; i++){
	
		do{
			if(read_rnd()==0){
				comp_rand = (char) pcg32_random_r();
			}
		}while (comp_rand > 50 && comp_rand < -50);
	
		valor_velc_vento = sens_velc_vento(ult_velcVento, comp_rand);
		vec[i]  = (unsigned short) valor_velc_vento;
		
		if(valor_velc_vento > s.max_limit || valor_velc_vento < s.min_limit){
			contador_velcVento++;
			
			if (contador_velcVento == s.n_erradas){
				contador_velcVento = 0;
				ult_velcVento = 0;
			}else{
			ult_velcVento = valor_velc_vento;
			}
		}
	}
	
	s.readings = vec;	
}

//Sensor direção vento
void gerar_valor_dir_vento (Sensor s, unsigned char ult_dirVento){
	int contador_dirVento = 0;
	unsigned short valor_dirVento; 
	short comp_rand;
	unsigned short vec[s.readings_size];
	
	for(int i=0; i<s.readings_size; i++){
	
		do{
			if(read_rnd()==0){
				comp_rand = (short) pcg32_random_r();
			}
		}while (comp_rand > 5 && comp_rand < -5);
		
		valor_dirVento = sens_dir_vento(ult_dirVento, comp_rand);
		vec[i] = valor_dirVento;
		
		if(valor_dirVento > s.max_limit || valor_dirVento < s.min_limit){
			contador_dirVento++;
			
			if (contador_dirVento == s.n_erradas){
				contador_dirVento = 0;
				ult_dirVento = 0;
			}else{
			ult_dirVento = valor_dirVento;
			}
		}
	}
	
	s.readings = vec;	
}

//Sensor pluviosidade
char gerar_valor_pluviosidade (Sensor s, unsigned char ult_pluvio, char ult_temp){
	int contador_pluvio = 0;
	unsigned char valor_pluvio; 
	char comp_rand;
	unsigned short vec[s.readings_size];
	
	for(int i=0; i<s.readings_size; i++){
		
		do{
			if(read_rnd()==0){
				comp_rand = (char) pcg32_random_r();
			}
		}while (comp_rand > 5 && comp_rand < 20);
	
		valor_pluvio = sens_pluvio(ult_pluvio, ult_temp, comp_rand);
		vec[i] = (unsigned short) valor_pluvio;
		
		if(valor_pluvio > s.max_limit || valor_pluvio < s.min_limit){
			contador_pluvio++;
			if (contador_pluvio == s.n_erradas){
				contador_pluvio = 0;
				ult_pluvio = 0;
			}
		}else{
			ult_pluvio = valor_pluvio;
		} 	
	}
	
	s.readings = vec;
	
	return ult_pluvio;
}

//Sensor humidade atmosférica
void gerar_valor_humidade_atm (Sensor s, unsigned char ult_pluvio, char ult_humAtm){
	int contador_humAtm = 0;
	unsigned char valor_humAtm;
	char comp_rand;
	unsigned short vec[s.readings_size];
	
	for(int i=0; i<s.readings_size; i++){
	
		do{
			if(read_rnd()==0){
				comp_rand = (char) pcg32_random_r();
			}
		}while (comp_rand > -5 && comp_rand < 5);
	
		valor_humAtm = sens_humd_atm(ult_humAtm, ult_pluvio, comp_rand);
		vec[i] = (unsigned short) valor_humAtm;
		
		if(valor_humAtm > s.max_limit || valor_humAtm < s.min_limit){
			contador_humAtm++;
			if (contador_humAtm == s.n_erradas){
				contador_humAtm = 0;
				ult_humAtm = 0;
			}
		}else{
			ult_humAtm = valor_humAtm;
		} 	
	}
	
	s.readings = vec;
}

//Sensor humidade solo
void gerar_valor_humidade_solo (Sensor s, unsigned char ult_pluvio, char ult_humSolo){
	int contador_humSolo = 0;
	unsigned char valor_humSolo;
	char comp_rand;
	unsigned short vec[s.readings_size];
	
	for(int i=0; i<s.readings_size; i++){
		
		do{
			if(read_rnd()==0){
				comp_rand = (char) pcg32_random_r();
			}
		}while (comp_rand > -5 && comp_rand < 5);
	
		valor_humSolo = sens_humd_solo(ult_humSolo, ult_pluvio, comp_rand);
		vec[i] = (unsigned short) valor_humSolo;
		
		if(valor_humSolo > s.max_limit || valor_humSolo < s.min_limit){
			contador_humSolo++;
			if (contador_humSolo == s.n_erradas){
				contador_humSolo = 0;
				ult_humSolo = 0;
			}
		}else{
			ult_humSolo = valor_humSolo;
		} 	
	}
	
	s.readings = vec;
}


