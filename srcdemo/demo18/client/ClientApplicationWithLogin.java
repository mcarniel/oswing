package demo18.client;

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
import org.openswing.swing.internationalization.java.XMLResourcesFactory;
import java.sql.*;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.client.OptionPane;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method: it shows a login dialog and
 * after successful authentication it creates an MDI Frame app.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplicationWithLogin implements MDIController,LoginController {


  private DemoClientFacade clientFacade = null;
  private Hashtable domains = new Hashtable();


  public ClientApplicationWithLogin() {
    ClientUtils.setObjectSender(new HessianObjectSender());

    clientFacade = new DemoClientFacade();


    Hashtable domains = new Hashtable();
    Domain sexDomain = new Domain("SEX");
    sexDomain.addDomainPair("M","male");
    sexDomain.addDomainPair("F","female");
    domains.put(
      sexDomain.getDomainId(),
      sexDomain
    );

    Properties props = new Properties();
    props.setProperty("deptCode","Dept Code");
    props.setProperty("description","Description");
    props.setProperty("address","Address");
    props.setProperty("tasks","Tasks");
    props.setProperty("departments","departments");
    props.setProperty("taskCode","Task Code");
    props.setProperty("firstName","First Name");
    props.setProperty("lastName","Last Name");
    props.setProperty("deptDescription","Dept. Description");
    props.setProperty("tadkDescription","Task Description");
    props.setProperty("hire date","Hire Date");
    props.setProperty("sex","Sex");
    props.setProperty("male","Male");
    props.setProperty("female","Female");
    props.setProperty("salary","Salary");
    props.setProperty("empCode","Employee Code");
    props.setProperty("task","Task");
    props.setProperty("department","Department");

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("$",props,true),
        domains
    );

    ClientSettings.BACKGROUND = "background4.jpg";
    ClientSettings.TREE_BACK = "treeback2.jpg";
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.VIEW_MANDATORY_SYMBOL = true;

    LoginDialog d = new LoginDialog(null,false,this);
  }


  /**
   * Method called after MDI creation.
   */
  public void afterMDIcreation(MDIFrame frame) {
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
    return "Demo18";
  }


  /**
   * @return text to view in the about dialog window
   */
  public String getAboutText() {
    return
        "This is an MDI Frame demo application based on Spring.\n"+
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
    return new LoginDialog(parentFrame,true,this);
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
    Response res = ClientUtils.getData("login",loginInfo);
    if (res.isError()) {
      OptionPane.showMessageDialog(null,res.getErrorMessage(),"Login",JOptionPane.ERROR_MESSAGE);
    }
    return !res.isError();
  }




  public static void main(String[] argv) {
    new ClientApplicationWithLogin();
  }


  /**
   * Method called by LoginDialog to notify the successful login.
   * @param loginInfo login information, like username, password, ...
   */
  public void loginSuccessful(Map loginInfo) {
    MDIFrame mdi = new MDIFrame(this);
  }



  /**
   * @return <code>true</code> if the MDI frame must show a change language menu in the menubar, <code>false</code> no change language menu item will be added
   */
  public boolean viewChangeLanguageInMenuBar() {
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
    ApplicationFunction n1 = new ApplicationFunction("Administration",null);
    ApplicationFunction n11 = new ApplicationFunction("Employees","EMP","men.gif","getEmployees");
    ApplicationFunction n12 = new ApplicationFunction("Departments","EMP","appicon.gif","getDepts");
    ApplicationFunction n13 = new ApplicationFunction("Tasks","EMP","appicon.gif","getTasks");
    n1.add(n11);
    n1.add(n12);
    n1.add(n13);
    root.add(n1);

    return model;
  }



  /**
   * @return <code>true</code> if the MDI frame must show a panel in the bottom, containing last opened window icons, <code>false</code> no panel is showed
   */
  public boolean viewOpenedWindowIcons() {
    return true;
  }



}
