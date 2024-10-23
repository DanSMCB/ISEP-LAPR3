prog: sensores.o sens_dir_vento.o sens_humd_atm.o sens_humd_solo.o sens_pluvio.o sens_temp.o sens_velc_vento.o demo_pcg.o read_rnd.o update_matrix.o main.o
	gcc -g -Wall sensores.o sens_dir_vento.o sens_humd_atm.o sens_humd_solo.o sens_pluvio.o sens_temp.o sens_velc_vento.o demo_pcg.o read_rnd.o update_matrix.o main.o -o prog
	
	
sensores: sensores.c
	gcc -g -Wall -c sensores.c -o sensores.o	
	
sens_dir_vento: sens_dir_vento.s
	gcc -g -Wall -c sens_dir_vento.s -o sens_dir_vento.o
	
sens_humd_atm: sens_humd_atm.s
	gcc -g -Wall -c sens_humd_atm.s -o sens_humd_atm.o
	
sens_humd_solo: sens_humd_solo.s
	gcc -g -Wall -c sens_humd_solo.s -o sens_humd_solo.o
	
sens_pluvio: sens_pluvio.s
	gcc -g -Wall -c sens_pluvio.s -o sens_pluvio.o
	
sens_temp: sens_temp.s
	gcc -g -Wall -c sens_temp.s -o sens_temp.o
	
sens_velc_vento: sens_velc_vento.s
	gcc -g -Wall -c sens_velc_vento.s -o sens_velc_vento.o
	
demo_pcg: demo_pcg.s
	gcc -g -Wall -c demo_pcg.s -o demo_pcg.o
	
read_rnd: read_rnd.c
	gcc -g -Wall -c read_rnd.c -o read_rnd.o
	
update_matrix: update_matrix.c
	gcc -g -Wall -c update_matrix.c -o update_matrix.o

main.o: main.c
	gcc -g -Wall -c main.c -o main.o

clean:
	rm -f *.o prog

run: prog
	./prog
	
