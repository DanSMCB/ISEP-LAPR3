CREATE SEQUENCE ID_PRODUTOSUTILIZADOS
START WITH 1
INCREMENT BY 1;

CREATE SEQUENCE ID_OPERCAOAGRICOLA
START WITH 1
INCREMENT BY 1;

----------------------1---------------------------

create or replace PROCEDURE adicionar_operacao_com_produtos (
p_localizacao_parcela IN OperacaoAgricola.localizacaoParcela%TYPE,
p_codigo_aplicacao IN OperacaoAgricola.codigoAplicacao%TYPE,
p_tipo_operacao IN OperacaoAgricola.tipoOperacao%TYPE,
p_data_planeada IN OperacaoAgricola.dataPlaneada%TYPE,
p_id_produto IN ProdutosUtilizados.produtos%TYPE,
p_quantidade IN ProdutosUtilizados.quantidade%TYPE,
p_id_operacao_agricola INT := id_operacaoagricola.nextval,
p_id_produtos_utilizados INT := id_produtosutilizados.nextval
)


AS
BEGIN


-- Adiciona a operação agrícola à tabela OperacaoAgricola
    INSERT INTO OperacaoAgricola (idOperacaoAgricola, localizacaoParcela, codigoAplicacao, tipoOperacao, dataPlaneada, dataRealizada)
    VALUES (p_id_operacao_agricola, p_localizacao_parcela, p_codigo_aplicacao, p_tipo_operacao, p_data_planeada, null);

    INSERT INTO ProdutosUtilizados (id, idOperacaoAgricola, produtos, quantidade)
    VALUES (p_id_produtos_utilizados, p_id_operacao_agricola, p_id_produto, p_quantidade);

-- Confirma a transação e salva os dados na tabela
COMMIT;

END;



BEGIN
  ADICIONAR_OPERACAO_COM_PRODUTOS('Setor 1',1, 'rega', TO_DATE('01/01/2023', 'DD/MM/YYYY'), 'Produto A, Produto B', 10, 5);
END;


----------------------------------2----------------------------

create or replace FUNCTION verificar_restricoes_operacao (
p_setor IN OperacaoAgricola.localizacaoParcela%TYPE,
p_data_operacao IN OperacaoAgricola.dataPlaneada%TYPE
)
RETURN VARCHAR2
AS
v_restricao VARCHAR2(30);
BEGIN
SELECT fator_restrito INTO v_restricao FROM Restricao WHERE localizacaoParcela = p_setor AND data_inicio <= p_data_operacao AND data_fim >= p_data_operacao;
RETURN v_restricao;
END;

DECLARE
v_resultado VARCHAR2(30);
BEGIN
v_resultado := verificar_restricoes_operacao('Setor A', TO_DATE('2022-01-01', 'YYYY-MM-DD'));
DBMS_OUTPUT.PUT_LINE(v_resultado);
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('Não há restrições aquando da marcação no setor especificado.');
END;

----------------------------------3-----------------------------

CREATE OR REPLACE FUNCTION verificar_restricoes_semana_anterior (
  p_setor IN Restricao.localizacaoParcela%TYPE,
  p_data_planeada IN OperacaoAgricola.dataPlaneada%TYPE
)
RETURN VARCHAR2
AS
  v_semana_anterior DATE;
  v_resultado VARCHAR2(30);
BEGIN
  v_semana_anterior := p_data_planeada - 7;
  SELECT fator_restrito INTO v_resultado FROM Restricao WHERE localizacaoParcela = p_setor AND data_inicio <= v_semana_anterior AND data_fim > v_semana_anterior;
  RETURN v_resultado;
END;

DECLARE
v_resultado VARCHAR2(30);
BEGIN
v_resultado := verificar_restricoes_semana_anterior('Setor A', TO_DATE('2022-01-01', 'YYYY-MM-DD'));
DBMS_OUTPUT.PUT_LINE(v_resultado);
EXCEPTION
WHEN NO_DATA_FOUND THEN
DBMS_OUTPUT.PUT_LINE('Não há restrições para a semana anterior no setor especificado.');
END;

----------------------------------4-----------------------------

create or replace FUNCTION listar_restricoes (p_setor IN Restricao.localizacaoParcela%TYPE,p_data IN Restricao.data_inicio%TYPE ) return sys_refcursor is
v_rc sys_refcursor;
BEGIN
  OPEN v_rc FOR
    SELECT * FROM RESTRICAO
  WHERE localizacaoParcela = p_setor AND
        data_inicio <= p_data AND
        data_fim >= p_data;
RETURN (v_rc);
END;


DECLARE
 l_restricoes sys_refcursor;
 l_restricao Restricao%ROWTYPE;

BEGIN
l_restricoes := listar_restricoes('Setor 1',TO_DATE('01/01/2023', 'DD/MM/YYYY') );
LOOP
Fetch l_restricoes into l_restricao;
EXIT WHEN l_restricoes%NOTFOUND;
dbms_output.put_line(l_restricao.id_restricao || ' - ' || l_restricao.data_inicio || ' - ' || l_restricao.data_fim || ' - ' || l_restricao.localizacaoParcela || ' - ' || l_restricao.fator_restrito);
END LOOP;
END;

------------------------------------5---------------------------

create or replace FUNCTION listar_operacoes (
p_setor IN OperacaoAgricola.localizacaoParcela%TYPE,
p_data_inicio IN OperacaoAgricola.dataPlaneada%TYPE,
p_data_fim IN OperacaoAgricola.dataPlaneada%TYPE)
return sys_refcursor is v_rc sys_refcursor;

BEGIN
  OPEN v_rc FOR
    SELECT * FROM OperacaoAgricola WHERE localizacaoParcela = p_setor AND dataPlaneada BETWEEN p_data_inicio AND p_data_fim
    ORDER BY dataPlaneada;
RETURN (v_rc);
END;


DECLARE
 l_operacoes sys_refcursor;
 l_operacao OperacaoAgricola%ROWTYPE;

BEGIN
l_operacoes := listar_operacoes('Setor 1',TO_DATE('01/01/2023', 'DD/MM/YYYY'),TO_DATE('02/01/2023', 'DD/MM/YYYY') );
LOOP
Fetch l_operacoes into l_operacao;
EXIT WHEN l_operacoes%NOTFOUND;
dbms_output.put_line(l_operacao.idOperacaoAgricola || ' - ' || l_operacao.dataPlaneada || ' - ' || l_operacao.dataRealizada || ' - ' || l_operacao.localizacaoParcela || ' - ' || l_operacao.codigoAplicacao || ' - ' || l_operacao.tipoOperacao);
END LOOP;
END;