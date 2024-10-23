--construir data warehouse 5--
create or replace PROCEDURE PreencheWHTIME AS
  -- Declare variables to store the start and end dates.
  start_year int := 2022;
  end_year int := 2023;
  
  y int := start_year;  
  m int;

BEGIN
  -- Loop through the dates from the start date to the end date.
  WHILE y <= end_year LOOP
    m :=1;
    while m <= 12 LOOP
        -- Insert a record for the current date.
        INSERT INTO WHTIME (code, ano,mes) VALUES (y*100+m,y,m);
        -- Increment the current date by one month.
        m:=m+1;
    end loop;
    y := y+1;
  END LOOP;
END;


ALTER TABLE WHCULTURA  
MODIFY (id DEFAULT WHCULTURASEQ.NEXTVAL )

insert into WHCULTURA (nomecultura, CODTIPOCULTURA, descricao)
select NOMECultura, cultura.codtipocultura, descricao
from cultura inner join tipocultura on cultura.codtipocultura=tipocultura.codtipocultura;

insert into WHPRODUCAO (CULTURACODE, TIMECODE, VALOR, QUANTIDADE)
select colheitas.nomecultura, colheitas.ano*100+colheitas.mes, colheitas.valor, colheitas.qtd
from 
(select nomecultura, EXTRACT(YEAR FROM datacolheita) as ano, EXTRACT(MONTH FROM datacolheita) as mes, sum(quantidadecolhida) as qtd, sum(lucro) as valor
 from COLHEITA
 group by nomecultura, EXTRACT(YEAR FROM datacolheita), EXTRACT(MONTH FROM datacolheita) 
) colheitas;

--6a--
 select WHTIME.ano, sum(WHPRODUCAO.Quantidade) as prod
 from WHProducao inner join WHTIME on WHProducao.timecode = WHTIME.code
 where culturacode='xpto'
 and WHTIME.ano >= EXTRACT(YEAR FROM current_DATE)
 group by WHTIME.ano
 order by WHTIME.ano;

--6c--
select WHTIME.ano, WHTIME.mes, WHCULTURA.descricao, sum(WHPRODUCAO.Valor) as Valor
from WHProducao 
inner join WHTIME on WHProducao.timecode = WHTIME.code
inner join WHCULTURA on WHProducao.culturacode = WHCULTURA.NOMECULTURA

 group by WHTIME.ano, WHTIME.mes, WHCULTURA.descricao
 order by WHTIME.ano, WHTIME.mes, WHCULTURA.descricao;
