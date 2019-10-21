package agents.behaviours.single;

public class Test extends SingleAgentBehaviour {
    private int n=0;

    public Test()
    {

    }

    @Override
    public void action() {
        System.out.println("Estou só a fazer contas de somar " + n++);

    }

    @Override
    public boolean done() {
        return n == 3;
    }
}
