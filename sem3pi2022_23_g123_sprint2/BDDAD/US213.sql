
--1. Existe uma tabela para registo de pistas de auditoria, ou seja, para o registo de todas as
--operações de escrita na base de dados envolvendo um determinado setor da minha exploração agrícola.
--2. Os mecanismos apropriados para gravação de operações de escrita (INSERT, UPDATE, DELETE) estão implementados.

CREATE SEQUENCE ID_AUDITORIA
START WITH 1
INCREMENT BY 1;

create or replace TRIGGER registoAlteracoes
AFTER INSERT OR UPDATE OR DELETE ON OPERACAOAGRICOLA
FOR EACH ROW

DECLARE
  operacao VARCHAR2(10);



BEGIN
    IF INSERTING THEN
        operacao := 'INSERT';
    ELSIF UPDATING THEN
        operacao := 'UPDATE';
    ELSE
        operacao := 'DELETE';
    END IF;

    INSERT INTO PISTASAUDITORIA (id_auditoria, utilizador, data_hora, tipo_alteracao)
    VALUES (id_auditoria.nextval, USER, SYSDATE, operacao);
END;

VARIABLE cur REFCURSOR;
EXECUTE :cur := consultar_pistas_auditoria(1);
PRINT cur;


--3. É implementado um processo de consulta de pistas de auditoria simples e eficaz.

create or replace FUNCTION consultar_pistas_auditoria (
p_id_setor IN VARCHAR2
) RETURN SYS_REFCURSOR AS
  pistas_auditoria SYS_REFCURSOR;
BEGIN
  OPEN pistas_auditoria FOR
  SELECT OpAg.localizacaoParcela, OpAg.dataPlaneada, OpAg.dataRealizada, Pa.utilizador, Pa.data_hora, Pa.tipo_alteracao
  FROM OperacaoAgricola OpAg
  INNER JOIN Parcela Par ON OpAg.localizacaoParcela = Par.localizacaoParcela
  INNER JOIN PistasAuditoria Pa ON OpAg.idOperacaoAgricola = Pa.id_operacao
  WHERE Par.localizacaoParcela = p_id_setor
  ORDER BY Pa.data_hora;
  RETURN pistas_auditoria;
END;