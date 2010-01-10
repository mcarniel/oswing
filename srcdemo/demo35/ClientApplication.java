package demo35;

import java.util.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.client.*;
import java.awt.Image;
import javax.swing.*;
import org.openswing.swing.internationalization.java.Language;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.internationalization.java.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.openswing.swing.util.server.JPAUtils;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an MDI Frame app and uses JPA to store/retrieve data.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication implements MDIController,LoginController {


  private DemoClientFacade clientFacade = null;
  private Hashtable domains = new Hashtable();
  private String username = null;
  private EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("demo35PU");


  public ClientApplication() {
    setupDB();

    clientFacade = new DemoClientFacade(emf);

    Domain orderStateDomain = new Domain("COUNTRIES");
    orderStateDomain.addDomainPair("Italy","Italy");
    orderStateDomain.addDomainPair("USA","USA");
    orderStateDomain.addDomainPair("Germany","Germany");
    orderStateDomain.addDomainPair("UK","UK");
    orderStateDomain.addDomainPair("France","France");
    orderStateDomain.addDomainPair("Spain","Spain");
    orderStateDomain.addDomainPair("Japan","Japan");
    orderStateDomain.addDomainPair("China","China");
    orderStateDomain.addDomainPair("Other","Other");

    domains.clear();
    domains.put(
      orderStateDomain.getDomainId(),
      orderStateDomain
    );


    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("$",new Properties(),false),
        domains
    );
    ClientSettings.BACKGROUND = "background4.jpg";
    ClientSettings.TREE_BACK = "treeback2.jpg";
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.VIEW_MANDATORY_SYMBOL = true;

    MDIFrame mdi = new MDIFrame(this);
  }


  /**
   * Method called after MDI creation.
   */
  public void afterMDIcreation(MDIFrame frame) {
    GenericStatusPanel userPanel = new GenericStatusPanel();
    userPanel.setColumns(12);
    MDIFrame.addStatusComponent(userPanel);
    userPanel.setText(username);
    MDIFrame.addStatusComponent(new Clock());
  }


  /**
   * @see JFrame getExtendedState method
   */
  public int getExtendedState() {
    return JFrame.MAXIMIZED_BOTH;
  }


  /**
   * @return client facade, invoked by the MDI Frame tree/menu
   */
  public ClientFacade getClientFacade() {
    return clientFacade;
  }


  /**
   * Method used to destroy application.
   */
  public void stopApplication() {
    System.exit(0);
  }


  /**
   * Defines if application functions must be viewed inside a tree panel of MDI Frame.
   * @return <code>true</code> if application functions must be viewed inside a tree panel of MDI Frame, <code>false</code> no tree is viewed
   */
  public boolean viewFunctionsInTreePanel() {
    return true;
  }


  /**
   * Defines if application functions must be viewed in the menubar of MDI Frame.
   * @return <code>true</code> if application functions must be viewed in the menubar of MDI Frame, <code>false</code> otherwise
   */
  public boolean viewFunctionsInMenuBar() {
    return true;
  }


  /**
   * @return <code>true</code> if the MDI frame must show a login menu in the menubar, <code>false</code> no login menu item will be added
   */
  public boolean viewLoginInMenuBar() {
    return false;
  }


  /**
   * @return application title
   */
  public String getMDIFrameTitle() {
    return "Demo";
  }


  /**
   * @return text to view in the about dialog window
   */
  public String getAboutText() {
    return
        "This is an MDI Frame demo application\n"+
        "\n"+
        "Copyright: Copyright (C) 2006 Mauro Carniel\n"+
        "Author: Mauro Carniel";
  }


  /**
   * @return image name to view in the about dialog window
   */
  public String getAboutImage() {
    return "about.jpg";
  }


  /**
   * @param parentFrame parent frame
   * @return a dialog window to logon the application; the method can return null if viewLoginInMenuBar returns false
   */
  public JDialog viewLoginDialog(JFrame parentFrame) {
    return null;
  }



  /**
   * @return maximum number of failed login
   */
  public int getMaxAttempts() {
    return 3;
  }


  /**
   * Method called by MDI Frame to authenticate the user.
   * @param loginInfo login information, like username, password, ...
   * @return <code>true</code> if user is correcly authenticated, <code>false</code> otherwise
   */
  public boolean authenticateUser(Map loginInfo) throws Exception {
      return true;
  }



  public static void main(String[] argv) {
    new ClientApplication();
  }


  /**
   * Method called by LoginDialog to notify the sucessful login.
   * @param loginInfo login information, like username, password, ...
   */
  public void loginSuccessful(Map loginInfo) {
  }


  /**
   * @return <code>true</code> if the MDI frame must show a change language menu in the menubar, <code>false</code> no change language menu item will be added
   */
  public boolean viewChangeLanguageInMenuBar() {
    return false;
  }


  /**
   * @return <code>true</code> if the MDI frame must show the "File" menu in the menubar of the frame, <code>false</code> to hide it
   */
  public boolean viewFileMenu() {
     return true;
  }


  /**
   * @return list of languages supported by the application
   */
  public ArrayList getLanguages() {
    ArrayList list = new ArrayList();
    list.add(new Language("EN","English"));
    return list;
  }


  /**
   * @return application functions (ApplicationFunction objects), organized as a tree
   */
  public DefaultTreeModel getApplicationFunctions() {
    DefaultMutableTreeNode root = new OpenSwingTreeNode();
    DefaultTreeModel model = new DefaultTreeModel(root);
    ApplicationFunction n1 = new ApplicationFunction("Folder1",null);
    ApplicationFunction n2 = new ApplicationFunction("Folder2",null);
    ApplicationFunction n3 = new ApplicationFunction("Folder3",null);
    ApplicationFunction n11 = new ApplicationFunction("Function1","F1",null,"getF1");
    ApplicationFunction n21 = new ApplicationFunction("Function2","F2",null,"getF2");
    ApplicationFunction n22 = new ApplicationFunction("Function3","F3",null,"getF3");
    ApplicationFunction n31 = new ApplicationFunction("Folder31",null);
    ApplicationFunction n311 = new ApplicationFunction("Function4","F4",null,"getF4");
    ApplicationFunction n312 = new ApplicationFunction("Function5","F5",null,"getF5");
    n1.add(n11);
    n2.add(n21);
    n2.add(n22);
    n3.add(n31);
    n31.add(n311);
    n31.add(n312);
    root.add(n1);
    root.add(n2);
    root.add(n3);

    return model;
  }



  /**
   * @return <code>true</code> if the MDI frame must show a panel in the bottom, containing last opened window icons, <code>false</code> no panel is showed
   */
  public boolean viewOpenedWindowIcons() {
    return true;
  }


    private Collection<Person> createPersons() {
	Collection<Person> retVal = new ArrayList<Person>(5);
        for(int i=0;i<200;i++) {
            Person p = new Person();
            p.setName("Name"+i);
            p.setSurname("Surname"+i);
            Address a = new Address();
            a.setCity("New York");
            a.setCountry("USA");
            a.setStreet("Fifth Av. "+i);
            p.setAddress(a);
            retVal.add(p);
        }
	return retVal;
    }


    public void setupDB() {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      try {
        for (Person p: createPersons())
          em.persist(p);
        em.getTransaction().commit();
      } catch (Exception e) {
        e.printStackTrace();
        em.getTransaction().rollback();
      }
      finally {
          try {
            em.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
      }
    }



}
