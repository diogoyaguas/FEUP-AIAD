package game;

import game.board.Board;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

public class Game {
    /**
     * Número de agentes atualmente no jogo
     */
    private int agent_number = 1;
    /**
     * Container com todos os agentes do jogo. Deve ter o nome de container1
     */
    private Container game_container;
    /**
     * Container necessário para o controlo de todos os outros agentes e container. Contem agentes que gerem nomes e conecções
     */
    private MainContainer main_container;
    /**
     * (Desnecessario porque o singleton é partilhado pelos agentes)
     * Container com 1 agente que controla o jogo
     */
    private Container board_container;
    /**
     * (Desnecessario porque o singleton é partilhado pelos agentes)
     * Agente que controla o jogo, mas não o joga. Ele deve manter o estado do jogo e atualizar sempre que algo muda (comuniçao).
     *
     */
    private AgentController game_controller;
    /**
     * Lista de todos os agentes presentes no jogo
     */
    private ArrayList<AgentController> agent_list = new ArrayList<AgentController>();
    /**
     * Board com as posições/cidades do jogo
     */
    private Board game_board;
    /**
     * O jogo, variavel singleton
     */
    public static Game singleton;

    /**
     * Contrutor do jogo. Cria o singleton,o tabuleiro, o main container, container dos agentes. Não inicia os agentes
     * @param number_of_agents Número de agentes que eu quero criar
     * @param width Largura do tabuleiro
     * @param height Altura do tabuleiro
     */
    public Game(int number_of_agents,int width,int height) {
        Game.singleton = this;
        game_board = new Board(width,height);
        main_container = new MainContainer();
        game_container = new Container();
        board_container = new Container();
        game_controller = board_container.addAgentContainer("game_controller","game.GameController");
        //TODO dispor os agentes pelo tabuleiro
        for (int i = 0; i < number_of_agents; i++) {
            createNewAgent("agent" + agent_number, "agents.types.MilitarAgent");
            agent_number++;
        }
    }

    public void startGameController() throws StaleProxyException {
        game_controller.start();
    }

    /**
     * Começa os agentes criados do jogo
     * @throws StaleProxyException
     */
    public void startAllAgents() throws StaleProxyException {
        for (AgentController agent : agent_list) {
            agent.start();
        }
    }

    /**
     * Começa um agente especifico na lista de agentes
     * @param index o index do agente que quero começar
     * @throws StaleProxyException
     */
    public void startAgent(int index) throws StaleProxyException {
        agent_list.get(index).start();
    }

    /**
     * Cria um novo agente e adiciona à lista de agentes. Não o inicia nem o coloca no tabuleiro
     * @param name
     * @param type
     */
    public void createNewAgent(String name, String type) {
        AgentController ac = game_container.addAgentContainer(name, type);
        agent_list.add(ac);
    }
}
