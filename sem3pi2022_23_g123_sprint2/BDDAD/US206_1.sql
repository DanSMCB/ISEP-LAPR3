create or replace PROCEDURE ParcelaInsert
	(
        designacao in varchar2(20),        
        area in int,
        localizacaoParcela in varchar2(20),
        idQuinta in int,
        codigoDias in varchar2(1)
	)
AS 
BEGIN
    insert into Parcela
    values(designacao,area,localizacaoParcela,idQuinta,codigoDias);

END;