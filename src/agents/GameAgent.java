package agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public abstract class GameAgent extends Agent {

    protected int[] pos;

    public void setup()
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName("GameAgent");
        sd.setType("player");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        while (true) {
            ACLMessage msg = blockingReceive();
            String[] params = msg.getContent().split(" ");
            if (params[0].equals("Init")) {
                pos = new int[]{Integer.parseInt(params[1]), Integer.parseInt(params[2])};
                break;
            }
        }

        System.out.println("Agent " + getName() + ": Coords " + pos[0] + "," + pos[1]);

    }
    public abstract void start();
    public abstract void takeDown();
}
