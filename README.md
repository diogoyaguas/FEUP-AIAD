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


**Description**:<br>
	The strategy game consists of several communities getting hold of each other through various strategies. Each community will receive money passively, directly proportional to the number of cities it owns. At the same time, communities will be able to buy resources for cash. They will need to manage and use resources/money to defeat opponents, requiring them to make quick decisions about how to manage resources. The game map will be divided into an NxM grid in which you can assume any size, each grid space being a city. Each community will have an equal number of map cities, which they will initially occupy. Each city, at the beginning of the game, starts from scratch and can be upgraded to a maximum level for cash. As the city evolves, the amount of money it manages increases. Only interactions between adjacent cities are possible. If, towards the end of the game, there is no community to stand out (a draw) and the number of cities in each one remains constant for a while, the game is over and wins whoever has the most cities. There are several strategies that each community can adopt - explained in the table below.

<table class="tg">
  <tr>
    <th class="tg-0pky">Strategies</th>
    <th class="tg-0pky">Rules</th>
  </tr>
  <tr>
    <td class="tg-0pky">Economist</td>
    <td class="tg-0pky"><li> The economist's goal is to buy cities from the opposing team (no one gets the money for the purchase) </li> <li> The opponent can buy the city they lost for a higher price. This causes the price of the city to increase in general. </li> <li> The price of the city decreases in value as the game progresses. After the city is bought, the state it was in remains the same. the same </li></td>
  </tr>
  <tr>
    <td class="tg-0pky">Religious</td>
    <td class="tg-0pky"><li> Spends your resources to advertise in other cities </li> <li> Propaganda slowly converts city people to your community. After a certain amount of conversion the city changes communities </li> <li> It is possible to counter-advertise using its own resources, nullifying its effects </li> <li> After the city is converted, the state in which this was still the same </li></td>
  </tr>
  <tr>
    <td class="tg-0pky">Military</td>
    <td class="tg-0pky"><li> Uses resources to invade other cities </li> <li> The military invests a certain amount of resources in an invasion and will only be able to conquer the city to be invaded if this amount is greater than the amount allocated to defend the invaded city < / li> <li> After capture the city starts from scratch and has to be rebuilt </li></td>
  </tr>
</table>

**Examples of independent variables**: the strategy adopted by each community; the number of cities belonging to each initial community; the number of equal strategies. <br>
**Examples of dependent variables**: the winning community/strategy; the number of cities conquered in the event of a tie.

