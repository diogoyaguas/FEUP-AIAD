package game;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;

public class MainContainer {

    private ContainerController mainContainer;

    public MainContainer()
    {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();
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
}
