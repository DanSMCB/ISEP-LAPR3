create or replace PROCEDURE TipoCulturaInsert
	(
        codTipoCultura int,
        descricao varchar2(30)
	)
AS 
BEGIN
    insert into TipoCultura
    values(codTipoCultura,descricao);
    
END;
/

create or replace PROCEDURE CulturaInsert
	(no_data_found EXCEPTION;
        nomeCultura varchar2(20),
        codTipoCultura int
	)
AS 
BEGIN
    insert into Cultura
    values(nomeCultura,codTipoCultura);

END;