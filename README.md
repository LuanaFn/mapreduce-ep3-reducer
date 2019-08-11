# mapreduce-ep3-client
Um sistema que permite a um programa cliente requisitar, a uma arquitetura Map-Reduce, a cria��o de um �ndice invertido de links (semelhante a uma das atividades do PageRank do Google) 

## Defini��o
Fluxo:
1. O cliente envia uma requisi��o, contendo uma lista L de URLs, para um n� denominado coordenador. A lista ser� lida de um arquivo da pasta local do cliente.
2. O coordenador recebe a lista L e a divide em M listas L1, L2, �, LI, �, LM.
3. O coordenador envia cada lista LI a um n� diferente, denominado mapper.
4. Cada mapper I recebe a parte LI, realiza uma atividade (ver 1.1.) e envia o resultado da atividade a um n� denominado reducer.
5. O reducer, somente ap�s receber o resultado de todos os M mappers, gera um �ndice invertido de links (ver 1.2.) e envia para o cliente.
6. O cliente recebe o �ndice invertido do reducer e o armazena em um arquivo na mesma pasta local do ponto 1. O arquivo poder� ser visualizado pelo professor.

### A atividade do mapper
Dever� conter, no m�nimo, as seguintes opera��es:
a) Receber em uma requisi��o uma lista de URLs do coordenador.
b) Baixar e armazenar na mem�ria a p�gina Web de cada URL da lista. Pode usar como exemplo o c�digo em [1].
c) Obter todos os links que existem no conte�do da p�gina baixada. Para isso, pode usar uma livraria externa, como Jsoup (e.g., [2]) ou utilizar um parser com express�es regulares (e.g., [3]).
d) Criar uma estrutura que contenha, para cada URL da lista, todos os links extra�dos dessa URL. Na se��o 1.3. poder� observar um exemplo dessa estrutura.
e) Enviar a estrutura ao reducer. 

### A atividade do reducer
Dever� conter, no m�nimo, as seguintes opera��es:
a) Receber a estrutura de cada mapper.
b) Verificar se j� recebeu todas as estruturas dos M mappers.
c) Somente ap�s receber as M estruturas, criar o �ndice invertido de links, como mostrado na Se��o 1.3, e orden�-lo de forma decrescente pela quantidade.
d) Enviar o �ndice ordenado para o cliente.
* Assuma que o cliente conhece o endere�o e porta do Coordenador.
* Assuma que o Coordenador conhece o endere�o e porta de todos os Mappers.
* Assuma que cada Mapper conhece o endere�o e porta do Reducer.
* Assuma que o Reducer n�o conhece diretamente o endere�o e porta do Cliente.
A execu��o do programa dever� poder ser realizada com um n�mero arbitr�rio de n�s mappers. Na avalia��o, ser� exigida a sua execu��o com os seguintes n�s: 1 cliente, 1 coordenador, 3 (tr�s) mappers e 1 reducer. Cada um desses n�s dever� rodar em pelo menos 2 m�quinas f�sicas, com IPs diferentes em 6 processos (JVM) diferentes. N�o ser�o  aceitos trabalhos que executam em somente uma m�quina (mesmo sendo diferentes m�quinas virtuais).

### Cria��o do �ndice invertido de links
A estrutura criada na opera��o d) do mapper permite, dada uma URL X, conhecer as URLs
que X aponta.
Por exemplo, vamos supor que o mapper1 baixou e processou o www.site1.br, enviando ao reducer a seguinte estrutura:

www.site1.br: {www.site2.br, www.site3.br}

O mapper2 baixou e processou o www.site2.br enviando ao reducer a seguinte estrutura:

www.site2.br: {www.site3.br, www.site4.br}

E o mapper3 baixou e processou o www.site3.br, enviando ao reducer a seguinte estrutura:

www.site3.br: {www.site4.br}

Se quiser ver os apontamentos como um grafo, o exemplo acima define a figura abaixo.
J� o �ndice invertido criado na opera��o c) do reducer permite, dada uma URL Y, conhecer que URLs apontam para Y (exatamente o contr�rio da estrutura do mapper). Para o exemplo acima, o �ndice invertido de links (ordenado pela quantidade de apontamentos) ser�:

www.site3.br: {www.site1.br, www.site2.br} // tamanho 2

www.site4.br: {www.site2.br, www.site3.br} // tamanho 2

www.site2.br: {www.site1.br} // tamanho 1

www.site1.br: {} // tamanho 0

Note que o www.site4.br n�o foi baixado por nenhum mapper, por�m apareceu na lista pelos link extra�dos no item c).

## Refer�ncias
- [Conex�o cliente-servidor por TCP](https://www.pegaxchange.com/2017/12/07/simple-tcp-ip-server-client-java/)
