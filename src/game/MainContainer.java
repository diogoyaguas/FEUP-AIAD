package game;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/**
 * Has the main container of the game
 */
public class MainContainer {

    private ContainerController mainContainer;

    public MainContainer()
    {

        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
        p1.setParameter(Profile.CONTAINER_NAME, "GameContainer");
        p1.setParameter(Profile.GUI,"true");
        mainContainer = rt.createMainContainer(p1);
    }

    public ContainerController getController()
    {
        return this.mainContainer;
    }

    public void setController(ContainerController cc)
    {
        this.mainContainer=cc;
    }

    public AgentController createAgent(String name,String className) throws StaleProxyException {
        Object[] args = new Object[0];
        return mainContainer.createNewAgent(name, className, args);
    }
}
