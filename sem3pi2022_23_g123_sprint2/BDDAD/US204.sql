INSERT INTO Utilizador VALUES ('Pedro Silva',1111);
INSERT INTO Utilizador VALUES ('Ana Lopes',2222);
INSERT INTO Utilizador VALUES ('Miguel Coentro',3333);
INSERT INTO Utilizador VALUES ('Roberto Gomes',4444);

INSERT INTO Cargo VALUES ('CL','Cliente');
INSERT INTO Cargo VALUES ('GA','G.Agricula');
INSERT INTO Cargo VALUES ('GD','G.Distribuicao');
INSERT INTO Cargo VALUES ('CO','Condutor');

INSERT INTO Pertence VALUES (date'2022-01-12',1111,1);
INSERT INTO Pertence VALUES (date'2022-02-12',2222,2);
INSERT INTO Pertence VALUES (date'2022-03-12',3333,3);
INSERT INTO Pertence VALUES (date'2022-04-12',4444,4);


INSERT INTO TipoCliente VALUES ('P','Privado');
INSERT INTO TipoCliente VALUES ('E','Empresa');

INSERT INTO Plafond VALUES (12345,14000,date'2022-05-12');

INSERT INTO Cliente VALUES (1112,1,12345,1);
INSERT INTO GestorAgricula VALUES (1115,2);
INSERT INTO GestorDistribuicao VALUES (1113,3);
INSERT INTO Condutor VALUES (1114,4);

INSERT INTO TipoMorada VALUES (1,'MCorrespondencia');
INSERT INTO TipoMorada VALUES (2,'HubEntrega');

INSERT INTO Morada VALUES (1,2260,23,'Marcel',1);
INSERT INTO Morada VALUES (2,2261,24,'Rua X',2);

INSERT INTO MoradaCorrespondencia VALUES (1112,1,1);
INSERT INTO HubEntrega VALUES (1112,2,2);

INSERT INTO TipoNivel VALUES ('A','sem incidentes reportados nos últimos 12 meses e volume total de vendas superior a 10000€');

INSERT INTO TotalEncomendasAno VALUES (2022,1,14000,1112);

INSERT INTO Encomenda VALUES (2221,1112,2022,14000,2);

INSERT INTO Incidente VALUES (1,200,date'2022-08-12',date'2022-09-12',1112,2221);

INSERT INTO Quinta VALUES ('Quinta1',1115,1212);

INSERT INTO TipoEdificio VALUES ('Estábulo','estábulos para animais');
INSERT INTO TipoEdificio VALUES ('Garagens','garagens para máquinas e alfaias');
INSERT INTO TipoEdificio VALUES ('Armazém','armazéns para colheitas');

INSERT INTO ModoAplicacao VALUES (1, 'via Foliar')
INSERT INTO ModoAplicacao VALUES (2, 'sistema de rega')
INSERT INTO ModoAplicacao VALUES (3, 'aplicação direta ao solo')

INSERT INTO Recorrencia VALUES ("P", "Rega em dias pares");
INSERT INTO Recorrencia VALUES ("I", "Rega em dias ímpares");
insert into Recorrencia VALUES ("T", "Rega todos os dias");


