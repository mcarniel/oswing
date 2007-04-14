package demo2;

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


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an MDI Frame app.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication implements MDIController,LoginController {


  private DemoClientFacade clientFacade = null;
  private Connection conn = null;
  private Hashtable domains = new Hashtable();
  private String username = null;

  public ClientApplication() {
    createConnection();
    clientFacade = new DemoClientFacade(conn);


    Hashtable xmlFiles = new Hashtable();
    xmlFiles.put("EN","demo2/Resources_en.xml");
    xmlFiles.put("IT","demo2/Resources_it.xml");
    ClientSettings clientSettings = new ClientSettings(
        new XMLResourcesFactory(xmlFiles,true),
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
    return true;
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
    JDialog d = new LoginDialog(parentFrame,true,this);
    return d;
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
    if ("ADMIN".equalsIgnoreCase((String)loginInfo.get("username")) &&
        "ADMIN".equalsIgnoreCase((String)loginInfo.get("password")) ||
        "MAURO".equalsIgnoreCase((String)loginInfo.get("username")) &&
        "MAURO".equalsIgnoreCase((String)loginInfo.get("password")))
      return true;
    else
      return false;
  }



  public static void main(String[] argv) {
    new ClientApplication();
  }


  /**
   * Method called by LoginDialog to notify the sucessful login.
   * @param loginInfo login information, like username, password, ...
   */
  public void loginSuccessful(Map loginInfo) {
    username = loginInfo.get("username").toString().toUpperCase();
    if (username.equals("ADMIN"))
      ClientSettings.getInstance().setLanguage("EN");
    else if (username.equals("MAURO"))
      ClientSettings.getInstance().setLanguage("IT");

    Domain orderStateDomain = new Domain("ORDERSTATE");
    orderStateDomain.addDomainPair("O","opened");
    orderStateDomain.addDomainPair("S","suspended");
    orderStateDomain.addDomainPair("D","delivered");
    orderStateDomain.addDomainPair("ABC","closed");
    domains.clear();
    domains.put(
      orderStateDomain.getDomainId(),
      orderStateDomain
    );

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
    list.add(new Language("IT","Italiano"));
    return list;
  }


  /**
   * @return application functions (ApplicationFunction objects), organized as a tree
   */
  public DefaultTreeModel getApplicationFunctions() {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
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
   * Create the database connection (using Hypersonic DB - in memory) and initialize tables...
   */
  private void createConnection() {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
      conn = DriverManager.getConnection("jdbc:hsqldb:mem:"+"a"+Math.random(),"sa","");
      PreparedStatement stmt = null;
      try {
        stmt = conn.prepareStatement("create table DEMO2(TEXT VARCHAR,DECNUM DECIMAL(10,2),CURRNUM DECIMAL(10,2),THEDATE DATE,COMBO VARCHAR,CHECK CHAR(1),RADIO CHAR(1),CODE VARCHAR,TA VARCHAR,PRIMARY KEY(TEXT))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table DEMO2_LOOKUP(CODE VARCHAR,DESCRCODE VARCHAR,PRIMARY KEY(CODE))");
        stmt.execute();

        for(int i=0;i<10;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into DEMO2 values('ABC"+i+"',"+12+i+0.333+","+1234+i+0.56+",?,'ABC','Y','Y','A"+i+"','AAAAAA"+i+"')");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
        }

        for(int i=0;i<10;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into DEMO2_LOOKUP values('A"+i+"','ABCDEF"+String.valueOf((char)(i+78))+"')");
          stmt.execute();
        }

      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
      finally {
        try {
          stmt.close();
        }
        catch (SQLException ex2) {
        }
      }

      conn.commit();

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @return <code>true</code> if the MDI frame must show a panel in the bottom, containing last opened window icons, <code>false</code> no panel is showed
   */
  public boolean viewOpenedWindowIcons() {
    return true;
  }



}
