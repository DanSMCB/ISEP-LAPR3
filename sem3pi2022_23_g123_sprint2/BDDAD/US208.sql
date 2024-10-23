------------Cria Registo Ficha-----------------
create or replace PROCEDURE CREATE_FICHA_TECNICA_REGISTO
	(
        idFicha in int,        
        idElemento IN int,        
        quantidade IN int
        
	)
AS 
	
BEGIN

    insert into registaelementos(quantidadeElemento,idFicha,idElemento)
    values(quantidade, idFicha, idElemento);

END;


---------Cria Ficha----------


create or replace PROCEDURE CREATE_FICHA_TECNICA 
	(
        fornecedor IN varchar2,        
        classificacao IN varchar2,
        idFicha out int        
	)
AS 
	
BEGIN

    idFicha:=ID_FICHATECNICA.NEXTVAL;
x§
    insert into fichatecnica(idficha,fornecedor,classificacao)
    values(idficha, fornecedor, classificacao);


END;

---------Bloco anonimo para testes------------

declare
    novoId int;    
    
begin
    
    CREATE_FICHA_TECNICA('forne_name2','muito bom2', novoId);
    CREATE_FICHA_TECNICA_REGISTO(1, novoId, 1);  -- 1 = idElemento, 10 = quantidade
    DBMS_OUTPUT.put_line ('Ficha criada: ' || novoId);  
end;