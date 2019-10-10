# FEUP-AIAD
Project for the Agents and Distributed Artificial Intelligence (AIAD) class of the Master in Informatics and Computer Engineering (MIEIC) at the Faculty of Engineering of the University of Porto (FEUP).
<br><br>
### Team Members <hr>
Amadeu Prazeres Pereira<br>
* Student Number: 201605646
* E-Mail: up201605646@fe.up.pt

Diogo Filipe da Silva Yaguas<br>
* Student Number: 201606165
* E-Mail: up201606165@fe.up.pt

João Augusto dos Santos Lima<br>
* Student Number: 201605314
* E-Mail: up201605314@fe.up.pt

### Project description <hr>
[Civilization Project](https://docs.google.com/document/d/1h-eEixjVxt7AA2ZcLfiRKmXZoYjZzY8SMuLyzgD6hJg/edit?usp=sharing)


Descrição:
	O jogo de estratégia consiste em diversas comunidades conseguirem apoderar-se das outras através de diversas estratégias. Cada comunidade irá receber dinheiro passivamente, diretamente proporcional ao número de cidades que possui. Ao mesmo tempo, as comunidades poderão comprar recursos a troco de dinheiro.
Será necessário que gerenciem e façam uso dos recursos / dinheiro de forma a derrotar os oponentes, requerendo que os se tomem decisões rápidas de como gerir os recursos.
O mapa do jogo irá estar dividido numa grelha de NxM no qual pode assumir qualquer tamanho, sendo cada espaço da grelha uma cidade. Cada comunidade irá ter um número igual de cidades do mapa, que ocuparão inicialmente. Cada cidade, no início do jogo, começa do zero e pode ser evoluída até um nível máximo, a troco de dinheiro. À medida que a cidade evolui, a quantidade de dinheiro que esta gere aumenta. Só é possível interações entre cidade adjacentes.
Se, mais para o fim do jogo, não houver nenhuma comunidade a destacar-se (empate) e o número de cidades em cada uma se mantiver constante durante algum tempo, o jogo acaba e ganha quem tiver mais cidades.
Existem várias estratégias que cada comunidade poderá adotar - explicitadas na tabela abaixo.

<table class="tg">
  <tr>
    <th class="tg-0pky">Estratégias</th>
    <th class="tg-0pky">Regras</th>
  </tr>
  <tr>
    <td class="tg-0pky">Economista</td>
    <td class="tg-0pky"><li>O objetivo do economista é comprar cidades da equipa adversária (ninguém recebe o dinheiro da compra)</li><li>O adversário pode comprar a cidade que perdeu por um preço mais elevado. Isto faz com que o preço da cidade aumente em geral</li><li>O preço da cidade vai descendo de valor à medida que o jogo avança<br>Após a cidade ser comprada, o estado em que esta estava continua a ser o mesmo</li></td>
  </tr>
  <tr>
    <td class="tg-0pky">Religioso</td>
    <td class="tg-0pky"><li>Gasta os seus recursos para fazer propaganda em outras cidades</li><li>Propaganda converte lentamente as pessoas da cidade para a sua comunidade. Após uma certa quantidade de conversão a cidade muda de comunidade</li><li>É possível fazer contra-propaganda usando os próprios recursos, anulando os seus efeitos</li><li>Após a cidade ser convertida, o estado em que esta estava continua a ser o mesmo</li> </td>
  </tr>
  <tr>
    <td class="tg-0pky">Militar</td>
    <td class="tg-0pky"><li>Utiliza recursos para invadir outras cidades</li><li>vO militar investe uma certa quantia de recursos numa invasão e apenas conseguirá conquistar a cidade a ser invadida se essa quantidade for maior que a quantidade alocada para defesa da cidade invadida</li><li>Após a captura a cidade começa do zero tendo de ser reconstruída</li></td>
  </tr>
</table>

**Exemplos de variáveis independentes**: a estratégia adotada por cada comunidade; o número de cidades pertencentes a cada comunidade inicial; o número de estratégias iguais. <br>
**Exemplos de variáveis dependentes**: a comunidade/estratégia vencedora; o número de cidades conquistadas em caso de empate.

