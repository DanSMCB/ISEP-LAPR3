create or replace PROCEDURE TipoNivelCliente
	(
        nome in int,        
        nFiscal IN int,        
        telefone IN int,
        emai in varchar(10),
        idCliente in int
	)
AS 
no_data_found EXCEPTION;
BEGIN
    insert into Utilizador(nome,nFiscal,telefone,email,idUtilizador)
    values(nome,nFiscal,telefone,email,1212);
    
    insert into Cargo values (1,'Cliente');
    
    INSERT INTO Pertence VALUES (date'2022-01-12',1212,1);
    
    INSERT INTO Pertence VALUES (date'2022-01-12',1212,1);
    
    INSERT INTO Cliente VALUES (idCliente,1,null,null,1);
    
    if(idCliente = null) then
    EXCEPTION
    WHEN no_data_found THEN
        raise_application_error(-20001, 'Insertion failed');
    end if;
    
    DBMS_OUTPUT.PUT_LINE('IdCliente = ' || idCliente); 

END;
/
-------------------------------

create or replace PROCEDURE AtualizaEncomenda
	(
        nTotal in int,        
        valorTotal IN int,
        idCliente in int,
	)
AS 
ano:= INTEGER DEFAULT extract(YEAR FROM sysdate)

BEGIN
    insert into nTotalEncomedasAno(nTotal,valorTotal,idCliente,ano)
    values(nTotal,valorTotal,idCliente,ano);

END;
/
--------------------------------

create view as ClienteNivel 
select idCliente,codTipoNivel,dataOcorrencia,idCliente,valorTotal from Cliente,NivelAtual,Incidente,TotalEncomendasAno
where idCliente.Cliente = idCliente.NivelAtual 
and dataOcorrencia.Incidente = (SELECT Max(dataOcorrencia)
FROM Incidente,Cliente
WHERE dataOcorrencia.Incidente = idCliente.Cliente),
and idCliente.TotalEncomendasAno = idCliente.Cliente,
and idCliente.TotalEncomendasAno = idCliente.Incidente,
and idCliente.TotalEncomendasAno = idCliente.NivelAtual,
group by codTipoNivel.TipoNivel
;
/
-----------------------------

CREATE OR REPLACE FUNCTION Criterio5 (
    idCliente  idCliente.Cliente%TYPE,
    ano_edicao  INTEGER DEFAULT extract(YEAR FROM sysdate),
    
) RETURN FLOAT 
IS
    valorTotalIncidentes  INTEGER;
    numEncomendaIncidente INTEGER;
    racio float;
BEGIN 
    select count(*) into valorTotalIncidentes from Incidente,Encomenda
    where idCliente = idCliente.Incidente
    and ano.Encomenda = ano_edicao
    and idCliente,Encomenda = idCliente.Incidente;
    
    select count(numEncomenda) into numEncomendaIncidente from Incidente,Encomenda
    where idCliente = idCliente.Incidente
    and ano.Encomenda = ano_edicao
    and idCliente,Encomenda = idCliente.Incidente
    and dataEncomenda > (select Max(dataOcorrencia) from Incidente where idCliente.Incidente = idCliente);
    
    racio := valorTotalIncidentes/numEncomendas;
    
    return racio;
    
    end;
/
----------------------------------------
create or replace PROCEDURE TipoNivelCliente
	(
        idCliente in int      
	)
AS 
cliente idCliente.Cliente%TYPE;
BEGIN

select idCliente into cliente
from Cliente,Incidente
where idCliente = idCliente.Cliente
and idCliente = idCliente.Incidente;

if(cliente = null) then
   
   select idCliente into cliente
   from Cliente,TotalEncomendasAno
   where idCliente = idCliente.Cliente
   and idCliente = idCliente.TotalEncomendasAno
   and idCliente.Cliente = idCliente.TotalEncomendasAno
   and valorTotal.TotalEncomendasAno > 10000;
   
   if(cliente != null )
   UPDATE NivelAtual
   SET codTipoNivel = 'A'
   WHERE idCliente.NivelAtual = cliente;

   else
   
   select idCliente into cliente
   from Cliente,TotalEncomendasAno
   where idCliente = idCliente.Cliente
   and idCliente = idCliente.TotalEncomendasAno
   and idCliente.Cliente = idCliente.TotalEncomendasAno
   and valorTotal.TotalEncomendasAno < 10000
   and valorTotal.TotalEncomendasAno > 5000;
   
   if(cliente != null )
   UPDATE NivelAtual
   SET codTipoNivel = 'B'
   WHERE idCliente.NivelAtual = cliente;
   
   else 
   UPDATE NivelAtual
   SET codTipoNivel = 'C'
   WHERE idCliente.NivelAtual = cliente;
   
   
   End if;
   
END;
/