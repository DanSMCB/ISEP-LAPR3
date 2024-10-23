CREATE TABLE Utilizador(
nome VARCHAR2(20),
idUtilizador INT
);

CREATE TABLE Cargo(
codCargo INT,
descricao VARCHAR2(20)
);

CREATE TABLE Pertence(
data DATE,
idUtilizador INT,
codCargo INT
);

CREATE TABLE GestorDistribuicao(
idGestorDistribuicao INT,
codCargo INT
);


CREATE TABLE Condutor(
cartaConducao INT,
codCargo INT
);

CREATE TABLE GestorAgricula(
idGestortAgricula INT,
codCargo INT
);

CREATE TABLE TipoCliente(
codTipoCliente VARCHAR2(1),
descricao VARCHAR2(15)
);

CREATE TABLE Plafond(
numCartao INT,
limiteCredito INT,
dataUltimaAtualizacao DATE
);

CREATE TABLE Cliente(
idCliente INT,
codTipoCliente VARCHAR2(1),
numCartao INT,
idLoc VARCHAR2(5), 
codCargo INT
);

CREATE TABLE TipoMorada(
tipoMorada INT,
descricao VARCHAR(30)
);

CREATE TABLE Morada(
idMorada INT,
codPostal INT,
nPorta INT,
rua VARCHAR2(12),
tipoMorada INT
);

CREATE TABLE MoradaCorrespondencia(
idCliente INT,
idMorada INT,
tipoMorada INT
);

CREATE TABLE Hub(
idLoc VARCHAR2(5),
latitude FLOAT,
longitude FLOAT
);

CREATE TABLE HubEntrega(
idCliente INT,
idMorada INT,
tipoMorada INT
);

CREATE TABLE TipoNivel(
codTipoNivel VARCHAR2(1),
descricao VARCHAR2(15)
);

CREATE TABLE NivelAtual(
idCliente INT,
codTipoNivel VARCHAR2(1),
dataUltimaAtualizacao DATE
);

CREATE TABLE TotalEncomendasAno(
ano INT NOT NULL UNIQUE,
nTotal INT,
valorTotal INT,
idCliente INT
);

CREATE TABLE Encomenda(
numEncomenda INT,
idCliente INT,
ano INT,
valor INT,
idLoc VARCHAR2(5)
);

CREATE TABLE Incidente(
idIncidente INT,
valor INT,
dataOcorrencia DATE,
dataSanado DATE,
idCliente INT,
numEncomenda INT
);

CREATE TABLE Quinta(
nome VARCHAR2(15),
idGestortAgricula INT,
idQuinta INT
);

CREATE TABLE TipoEdificio(
nomeEdificio VARCHAR2(20),
descricao VARCHAR2(50)
);

CREATE TABLE Edificio(
idEdificio INT,
idQuinta INT,
localizacao VARCHAR2(20),
nomeEdificio VARCHAR2(20)
);

CREATE TABLE ModoAplicacao(
codigoAplicacao INT,
descricao VARCHAR2(30)
);



CREATE TABLE CadernoCampo(
anoCaderno INT,
idQuinta INT
);



CREATE TABLE Plano(
anoPlano INT,
idQuinta INT,
idOperacaoAgricola INT
);

CREATE TABLE OperacaoAgricola(
idOperacaoAgricola INT,
localizacaoParcela VARCHAR2(20) NOT NULL,
codigoAplicacao INT NOT NULL,
tipoOperacao VARCHAR2(20) NOT NULL,
dataPlaneada DATE,
dataRealizada DATE
);

CREATE TABLE TipoOperacao(
id_tipoOperacao VARCHAR2(20) NOT NULL,
descricaoc VARCHAR2(50) NOT NULL
);

CREATE TABLE ProdutosUtilizados(
id INT,
idOperacaoAgricola INT NOT NULL,
produtos VARCHAR2(500) NOT NULL,
quantidade NUMERIC(10,2) NOT NULL
);

CREATE TABLE Restricao(
id_restricao INT,
data_inicio DATE NOT NULL,
data_fim DATE NOT NULL,
localizacaoParcela VARCHAR2(20) NOT NULL,
fator_restrito VARCHAR2(20) NOT NULL
);

CREATE TABLE Parcela(
designacao VARCHAR2(20),
area INT,
localizacaoParcela VARCHAR2(20),
idQuinta INT,
codigoDias VARCHAR2(1)
);

CREATE TABLE TipoCultura(
codTipoCultura INT,
descricao VARCHAR2(30)
);

CREATE TABLE Cultura(
nomeCultura VARCHAR2(20),
codTipoCultura INT
);

CREATE TABLE ConstituicaoParcela(
localizacaoParcela VARCHAR2(20),
nomeCultura VARCHAR2(20)
);

CREATE TABLE Colheita(
localizacaoParcela VARCHAR2(20),
nomeCultura VARCHAR2(20),
quantidadeColhida INT,
anoCaderno INT,
lucro FLOAT,
dataColheita DATE
);

CREATE TABLE TipoFator(
codigoTipoFator INT,
descricao VARCHAR2(30)
);

CREATE TABLE FormulacaoFator(
codigoFormulacao INT,
descricao VARCHAR2(30)
);

CREATE TABLE FichaTecnica(
idFicha INT,
fornecedor VARCHAR2(30),
classificacao VARCHAR2(30),
nomeComercial VARCHAR2(20)
);

CREATE TABLE FatorProducao(
nomeComercial VARCHAR2(20),
codigoTipoFator INT,
codigoFormulacao INT,
idFicha INT
);

CREATE TABLE Elemento(
categoria VARCHAR2(20),
unidade INT,
substancia VARCHAR2(20),
idElemento INT
);

CREATE TABLE RegistaElementos(
quantidadeElemento INT,
idFicha INT,
idElemento INT
);

CREATE TABLE TipoSensor(
codSensor INT,
descricao VARCHAR2(30)
);

CREATE TABLE Sensor(
idSensor INT,
codSensor INT,
latitude FLOAT,
longitude FLOAT
);

CREATE TABLE ResumoSensor(
dataUltimaUtilizacao DATE,
idRegistoLeitura INT,
idSensor INT
);

CREATE TABLE RegistoLeituraSensor(
idRegistoLeitura INT,
valorLido INT,
valorReferencia INT,
dataLeitura DATE,
idSensor INT,
anoCaderno INT
);

CREATE TABLE PistasAuditoria(
id_auditoria INT,
utilizador INT,
data_hora TIMESTAMP,
tipo_alteracao VARCHAR2(10)
id_operacao INT
);

ALTER TABLE Utilizador
ADD CONSTRAINT PK_Utilizador PRIMARY KEY (idUtilizador);

ALTER TABLE Cargo
ADD CONSTRAINT PK_Cargo PRIMARY KEY(codCargo)
ADD CONSTRAINT CK_Cargo CHECK(codCargo = 'GA' OR codCargo = 'GD' OR codCargo = 'CO' OR codCargo = 'CL');

ALTER TABLE Pertence
ADD CONSTRAINT PK_Pertence PRIMARY KEY(data,idUtilizador,codCargo)
ADD CONSTRAINT FK_codCargo_Pertence FOREIGN KEY (codCargo) REFERENCES Cargo(codCargo) ON DELETE CASCADE
ADD CONSTRAINT FK_idUtilizador_Pertence FOREIGN KEY (idUtilizador) REFERENCES Utilizador(idUtilizador) ON DELETE CASCADE;

ALTER TABLE GestorDistribuicao
ADD CONSTRAINT PK_GestorDistribuicao PRIMARY KEY(idGestorDistribuicao)
ADD CONSTRAINT FK_idGestorDistribuicao_GestorDistribuicao FOREIGN KEY (codCargo) REFERENCES Cargo(codCargo) ON DELETE SET NULL;

ALTER TABLE Condutor
ADD CONSTRAINT PK_Condutor PRIMARY KEY(cartaConducao)
ADD CONSTRAINT FK_codCargo_GestorAgricula FOREIGN KEY (codCargo) REFERENCES Cargo(codCargo) ON DELETE SET NULL;

ALTER TABLE GestorAgricula
ADD CONSTRAINT PK_GestorAgricula PRIMARY KEY(idGestortAgricula)
ADD CONSTRAINT FK_codCargo_Condutor FOREIGN KEY (codCargo) REFERENCES Cargo(codCargo) ON DELETE SET NULL;

ALTER TABLE TipoCliente
ADD CONSTRAINT PK_TipoCliente PRIMARY KEY(codTipoCliente)
ADD CONSTRAINT CK_TipoCliente CHECK(codTipoCliente = 'E' OR codTipoCliente = 'P');

ALTER TABLE Plafond
ADD CONSTRAINT PK_Plafond PRIMARY KEY(numCartao);

ALTER TABLE Hub
ADD CONSTRAINT PK_Hub PRIMARY KEY(idLoc);

ALTER TABLE Cliente
ADD CONSTRAINT PK_Cliente PRIMARY KEY(idCliente)
ADD CONSTRAINT FK_codCargo_Cliente FOREIGN KEY (codCargo) REFERENCES Cargo(codCargo) ON DELETE SET NULL
ADD CONSTRAINT FK_codTipoCliente_Cliente FOREIGN KEY (codTipoCliente) REFERENCES TipoCliente(codTipoCliente) ON DELETE CASCADE
ADD CONSTRAINT FK_numCartao_Client FOREIGN KEY (numCartao) REFERENCES Plafond(numCartao) ON DELETE SET NULL
ADD CONSTRAINT FK_idLoc_Cliente FOREIGN KEY (idLoc) REFERENCES Hub(idLoc);

ALTER TABLE TipoMorada
ADD CONSTRAINT PK_TipoMorada PRIMARY KEY(tipoMorada);

ALTER TABLE Morada
ADD CONSTRAINT PK_Morada PRIMARY KEY(idMorada)
ADD CONSTRAINT FK_tipoMorada_Morada FOREIGN KEY (tipoMorada) REFERENCES TipoMorada(tipoMorada) ON DELETE SET NULL;

ALTER TABLE MoradaCorrespondencia
ADD CONSTRAINT PK_MoradaCorrespondencia PRIMARY KEY(idMorada)
ADD CONSTRAINT FK_tipoMorada_MoradaCorrespopndencia FOREIGN KEY (tipoMorada) REFERENCES TipoMorada(tipoMorada) ON DELETE CASCADE
ADD CONSTRAINT FK_idMorada_MoradaCorrespopndencia FOREIGN KEY (idMorada) REFERENCES Morada(idMorada) ON DELETE SET NULL;

ALTER TABLE HubEntrega
ADD CONSTRAINT PK_HubEntrega PRIMARY KEY(idMorada)
ADD CONSTRAINT FK_tipoMorada_HubEntrega FOREIGN KEY (tipoMorada) REFERENCES TipoMorada(tipoMorada) ON DELETE CASCADE
ADD CONSTRAINT FK_idMorada_HubEntrega FOREIGN KEY (idMorada) REFERENCES Morada(idMorada) ON DELETE SET NULL
ADD CONSTRAINT FK_idCliente_HubEntrega FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente) ON DELETE CASCADE;

ALTER TABLE TipoNivel
ADD CONSTRAINT PK_TipoNivel PRIMARY KEY(codTipoNivel)
ADD CONSTRAINT CK_TipoNivel CHECK(codTipoNivel = 'A' OR codTipoNivel = 'B' OR codTipoNivel = 'C');

ALTER TABLE NivelAtual
ADD CONSTRAINT PK_NivelAtual PRIMARY KEY(idCliente,codTipoNivel)
ADD CONSTRAINT FK_idCliente_NivelAtual FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente) ON DELETE CASCADE
ADD CONSTRAINT FK_codTipoNivel_NivelAtual FOREIGN KEY (codTipoNivel) REFERENCES TipoNivel(codTipoNivel) ON DELETE CASCADE;

ALTER TABLE TotalEncomendasAno
ADD CONSTRAINT PK_TotalEncomendasAno PRIMARY KEY(ano,idCliente)
ADD CONSTRAINT FK_idCliente_TotalEncomendasAno FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente) ON DELETE CASCADE;

ALTER TABLE Encomenda
ADD CONSTRAINT PK_Encomenda PRIMARY KEY(numEncomenda)
ADD CONSTRAINT FK_idCliente_Encomenda FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente) ON DELETE SET NULL
ADD CONSTRAINT FK_ano_Encomenda  FOREIGN KEY (ano) REFERENCES TotalEncomendasAno(ano) ON DELETE SET NULL
ADD CONSTRAINT FK_idLoc_Encomenda FOREIGN KEY (idLoc) REFERENCES Hub(idLoc) ON DELETE SET NULL;

ALTER TABLE Incidente
ADD CONSTRAINT PK_Incidente PRIMARY KEY(idIncidente)
ADD CONSTRAINT FK_idCliente_Incidente  FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente) ON DELETE SET NULL
ADD CONSTRAINT FK_numEncomenda_Incidente FOREIGN KEY (numEncomenda) REFERENCES Encomenda(numEncomenda) ON DELETE SET NULL;

ALTER TABLE Quinta
ADD CONSTRAINT PK_Quinta PRIMARY KEY(idQuinta)
ADD CONSTRAINT FK_idGestorAgricula_Quinta  FOREIGN KEY (idGestortAgricula) REFERENCES GestorAgricula(idGestortAgricula) ON DELETE SET NULL;

ALTER TABLE TipoEdificio
ADD CONSTRAINT PK_TipoEdificio PRIMARY KEY(nomeEdificio)
ADD CONSTRAINT CK_TipoEdificio CHECK(nomeEdificio = 'Estábulos' OR nomeEdificio = 'Garagens' OR nomeEdificio = 'Armazéns');

ALTER TABLE Edificio
ADD CONSTRAINT PK_Edificio PRIMARY KEY(idEdificio)
ADD CONSTRAINT FK_idQuinta_Edificio  FOREIGN KEY (idQuinta) REFERENCES Quinta(idQuinta) ON DELETE SET NULL
ADD CONSTRAINT FK_nomeEdificio_Edifico FOREIGN KEY (nomeEdificio) REFERENCES TipoEdificio(nomeEdificio) ON DELETE SET NULL;

ALTER TABLE ModoAplicacao
ADD CONSTRAINT PK_ModoAplicacao PRIMARY KEY(codigoAplicacao)
ADD CONSTRAINT CK_ModoAplicacao CHECK(codigoAplicacao = 1 OR codigoAplicacao = 2 OR codigoAplicacao = 3);

ALTER TABLE CadernoCampo
ADD CONSTRAINT PK_CadernoCampo PRIMARY KEY(anoCaderno)
ADD CONSTRAINT FK_idQuinta_CadernoCampo FOREIGN KEY (idQuinta) REFERENCES Quinta(idQuinta) ON DELETE CASCADE;

ALTER TABLE TipoOperacao
ADD CONSTRAINT PK_id_tipoOperacao PRIMARY KEY(id_tipoOperacao);

ALTER TABLE Parcela
ADD CONSTRAINT PK_Parcela PRIMARY KEY(localizacaoParcela)
ADD CONSTRAINT FK_idQuinta_Parcela FOREIGN KEY (idQuinta) REFERENCES Quinta(idQuinta) ON DELETE CASCADE;

ALTER TABLE OperacaoAgricola
ADD CONSTRAINT PK_OperacaoAgricola PRIMARY KEY(idOperacaoAgricola)
ADD CONSTRAINT FK_localizacaoParcela_OperacaoAgricola FOREIGN KEY (localizacaoParcela) REFERENCES Parcela(localizacaoParcela)
ADD CONSTRAINT FK_codigoAplicacao_OperacaoAgricola FOREIGN KEY (codigoAplicacao) REFERENCES ModoAplicacao(codigoAplicacao)
ADD CONSTRAINT FK_tipoOperacao_OperacaoAgricola FOREIGN KEY (tipoOperacao) REFERENCES TipoOperacao(id_tipoOperacao);

ALTER TABLE Plano
ADD CONSTRAINT PK_Plano PRIMARY KEY(anoPlano)
ADD CONSTRAINT FK_idQuinta_Plano FOREIGN KEY (idQuinta) REFERENCES Quinta(idQuinta) ON DELETE CASCADE
ADD CONSTRAINT FK_idOperacaoAgricola_Plano FOREIGN KEY (idOperacaoAgricola) REFERENCES OperacaoAgricola(idOperacaoAgricola) ON DELETE CASCADE;

ALTER TABLE TipoFator
ADD CONSTRAINT PK_TipoFator PRIMARY KEY(codigoTipoFator)
ADD CONSTRAINT CK_TipoFator CHECK(codigoTipoFator = 0 OR codigoTipoFator = 1 OR codigoTipoFator = 2 OR codigoTipoFator = 3);

ALTER TABLE FormulacaoFator
ADD CONSTRAINT PK_FormulacaoFator PRIMARY KEY(codigoFormulacao);

ALTER TABLE FichaTecnica
ADD CONSTRAINT PK_FichaTecnica PRIMARY KEY(idFicha);

ALTER TABLE FatorProducao
ADD CONSTRAINT PK_FatorProducao PRIMARY KEY(nomeComercial)
ADD CONSTRAINT FK_codigoTipoFator_FatorProducao FOREIGN KEY (codigoTipoFator) REFERENCES TipoFator(codigoTipoFator) ON DELETE CASCADE
ADD CONSTRAINT FK_codigoFormulacao_FatorProducao  FOREIGN KEY (codigoFormulacao) REFERENCES FormulacaoFator(codigoFormulacao) ON DELETE CASCADE
ADD CONSTRAINT FK_idFicha_FatorProducao FOREIGN KEY (idFicha) REFERENCES FichaTecnica(idFicha) ON DELETE CASCADE;

ALTER TABLE ProdutosUtilizados
ADD CONSTRAINT PK_ProdutosUtilizados PRIMARY KEY(id)
ADD CONSTRAINT FK_produtos_ProdutosUtilizados FOREIGN KEY (produtos) REFERENCES FatorProducao(nomeComercial) ON DELETE CASCADE
ADD CONSTRAINT FK_idOperacaoAgricola_ProdutosUtilizados FOREIGN KEY (idOperacaoAgricola) REFERENCES OperacaoAgricola(idOperacaoAgricola);

ALTER TABLE Restricao
ADD CONSTRAINT PK_Restricao PRIMARY KEY(id_restricao)
ADD CONSTRAINT FK_localizacaoParcela_Restricao FOREIGN KEY (localizacaoParcela) REFERENCES Parcela(localizacaoParcela)
ADD CONSTRAINT FK_fator_restrito_Restricao FOREIGN KEY (fator_restrito) REFERENCES FatorProducao(nomeComercial)
ADD CHECK (data_fim > data_inicio);

ALTER TABLE TipoCultura
ADD CONSTRAINT PK_TipoCultura PRIMARY KEY(codTipoCultura)
ADD CONSTRAINT CK_TipoCultura CHECK(codTipoCultura = 1 OR codTipoCultura = 2);

ALTER TABLE Cultura
ADD CONSTRAINT PK_Cultura PRIMARY KEY(nomeCultura)
ADD CONSTRAINT FK_codTipoCultura_Cultura FOREIGN KEY (codTipoCultura) REFERENCES TipoCultura(codTipoCultura) ON DELETE SET NULL;

ALTER TABLE ConstituicaoParcela
ADD CONSTRAINT PK_ConstituicaoParcela PRIMARY KEY(localizacaoParcela, nomeCultura)
ADD CONSTRAINT FK_localizacaoParcela_ConstituicaoParcela FOREIGN KEY (localizacaoParcela) REFERENCES Parcela(localizacaoParcela) ON DELETE CASCADE
ADD CONSTRAINT FK_nomeCultura_ConstituicaoParcela FOREIGN KEY (nomeCultura) REFERENCES Cultura(nomeCultura) ON DELETE SET NULL;

ALTER TABLE Colheita
ADD CONSTRAINT PK_Colheita PRIMARY KEY(localizacaoParcela, nomeCultura)
ADD CONSTRAINT FK_localizacaoParcela_Colheita FOREIGN KEY (localizacaoParcela) REFERENCES Parcela(localizacaoParcela) ON DELETE CASCADE
ADD CONSTRAINT FK_nomeCultura_Colheita FOREIGN KEY (nomeCultura) REFERENCES Cultura(nomeCultura) ON DELETE SET NULL
ADD CONSTRAINT FK_anoCaderno_Colheita FOREIGN KEY (anoCaderno) REFERENCES CadernoCampo(anoCaderno) ON DELETE SET NULL;

ALTER TABLE Elemento
ADD CONSTRAINT PK_Elemento PRIMARY KEY(idElemento);

ALTER TABLE RegistaElementos
ADD CONSTRAINT PK_RegistaElementos PRIMARY KEY(idFicha, idElemento)
ADD CONSTRAINT FK_idFicha_RegistaElementos FOREIGN KEY (idFicha) REFERENCES FichaTecnica(idFicha) ON DELETE CASCADE
ADD CONSTRAINT FK_idElemento_RegistaElementos  FOREIGN KEY (idElemento) REFERENCES Elemento(idElemento) ON DELETE CASCADE;

ALTER TABLE TipoSensor
ADD CONSTRAINT PK_TipoSensor PRIMARY KEY(codSensor)
ADD CONSTRAINT CK_TipoSensor CHECK(codSensor = 1 OR codSensor = 2 OR codSensor = 3 OR codSensor = 4 OR codSensor = 5 OR codSensor = 6 OR codSensor = 7);

ALTER TABLE Sensor
ADD CONSTRAINT PK_Sensor PRIMARY KEY(idSensor)
ADD CONSTRAINT FK_codSensor_Sensor FOREIGN KEY (codSensor) REFERENCES TipoSensor(codSensor) ON DELETE CASCADE;

ALTER TABLE RegistoLeituraSensor
ADD CONSTRAINT PK_RegistoLeituraSensor PRIMARY KEY(idRegistoLeitura)
ADD CONSTRAINT FK_idSensor_RegistoLeituraSensor FOREIGN KEY (idSensor) REFERENCES Sensor(idSensor) ON DELETE SET NULL
ADD CONSTRAINT FK_anoCaderno_RegistoLeituraSensor FOREIGN KEY (anoCaderno) REFERENCES CadernoCampo(anoCaderno) ON DELETE SET NULL;

ALTER TABLE ResumoSensor
ADD CONSTRAINT PK_ResumoSensor PRIMARY KEY(idRegistoLeitura,idSensor)
ADD CONSTRAINT FK_idSensor_ResumoSensor FOREIGN KEY (idSensor) REFERENCES Sensor(idSensor) ON DELETE SET NULL
ADD CONSTRAINT FK_idRegistoLeitura_ResumoSensor FOREIGN KEY (idRegistoLeitura) REFERENCES RegistoLeituraSensor(idRegistoLeitura) ON DELETE SET NULL;

ALTER TABLE PistasAuditoria
ADD CONSTRAINT PK_PistasAuditoria PRIMARY KEY(id_auditoria)
ADD CONSTRAINT FK_utilizador_PistasAuditoria FOREIGN KEY (utilizador) REFERENCES Utilizador(idUtilizador) ON DELETE SET NULL;
ADD CONSTRAINT FK_id_setor_PistasAuditoria FOREIGN KEY (id_operacao) REFERENCES OperacaoAgricola(idOperacaoAgricola) ON DELETE SET NULL;