insert into Quinta(nome, idGestortAgricula, idQuinta) values ('Quinta A', 1, 1);
insert into Quinta(nome, idGestortAgricula, idQuinta) values ('Quinta B', 2, 2);

insert into Recorrencia(codigoDias, descricao) values ('P', 'Rega em dias pares');
insert into Recorrencia(codigoDias, descricao) values ('I', 'Rega em dias ímpares');
insert into Recorrencia(codigoDias, descricao) values ('T', 'Rega todos os dias');

insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor A', 25, '1', 1, 'P');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor B', 30, '2', 1, 'I');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor C', 20, '3', 1, 'T');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor A', 25, '4', 2, 'P');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor B', 30, '5', 2, 'I');

insert into Colheita(localizacaoParcela, nomeCultura, quantidadeColhida, anoCaderno, lucro, dataColheita) values ('1', 'Pereira', 5, 2019, 15.3, date'2019-11-20');
insert into Colheita(localizacaoParcela, nomeCultura, quantidadeColhida, anoCaderno, lucro, dataColheita) values ('4', 'Pereira', 5, 2019, 20.3, date'2019-11-21');
insert into Colheita(localizacaoParcela, nomeCultura, quantidadeColhida, anoCaderno, lucro, dataColheita) values ('2', 'Macieira', 5, 2019, 15.9, date'2019-11-23');
insert into Colheita(localizacaoParcela, nomeCultura, quantidadeColhida, anoCaderno, lucro, dataColheita) values ('1', 'Limoeiro', 5, 2019, 19.3, date'2019-11-26');
insert into Colheita(localizacaoParcela, nomeCultura, quantidadeColhida, anoCaderno, lucro, dataColheita) values ('1', "Pereira", 5, 2019, 12.3, date'2019-11-12');

insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor A', 25, 1, 1, 'P');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor B', 30, 2, 1, 'I');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor C', 20, 3, 1, 'T');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor D', 25, 4, 2, 'P');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor E', 30, 5, 2, 'I');
insert into Parcela(designacao, area, localizacaoParcela, idQuinta, codigoDias) values ('Setor E', 25, 5, 2, 'T');

insert into NivelAtual(codTipoNivel, descricao) values ('D', 'Clientes sem incidentes');
insert into NivelAtual(codTipoNivel, descricao) values ('A', 'sem incidentes reportados nos últimos 12 meses e volume total de vendas superior a 10000€');