package demo38;

import org.openswing.swing.tree.java.OpenSwingTreeNode;
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
import org.openswing.swing.permissions.java.CryptUtils;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an MDI Frame app with users managements and authentication having encripted password stored on db.
 * NOTE: use "admin" / "admin" in login dialog.
 * </p>
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
  private Properties langs = new Properties();


  public ClientApplication() {
    createConnection();
    clientFacade = new DemoClientFacade(conn);


    Hashtable xmlFiles = new Hashtable();
    xmlFiles.put("EN","demo38/Resources_en.xml");
    xmlFiles.put("IT","demo38/Resources_it.xml");
    ClientSettings clientSettings = new ClientSettings(
        new XMLResourcesFactory(xmlFiles,false),
        domains
    );
    ClientSettings.BACKGROUND = "background4.jpg";
    ClientSettings.TREE_BACK = "treeback2.jpg";
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.VIEW_MANDATORY_SYMBOL = true;
    ClientSettings.ALLOW_OR_OPERATOR = false;
    ClientSettings.INCLUDE_IN_OPERATOR = false;
    ClientSettings.getInstance().setLanguage("EN");

    langs.setProperty("EN","English");
    langs.setProperty("IT","Italiano");

    LoginDialog d = new LoginDialog(
      null,
      false,
      this,
      "Authentication",
      "Login",
      'L',
      "Exit",
      'E',
      "Store Account",
      "DEMO38",
      CryptUtils.getInstance(),
      langs,
      "EN"
    );
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
    JDialog d = new LoginDialog(
      parentFrame,
      true,
      this,
      "Authentication",
      "Login",
      'L',
      "Exit",
      'E',
      "Store Account",
      "DEMO38",
      CryptUtils.getInstance(),
      langs,
      ClientSettings.getInstance().getResources().getLanguageId()
    );
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
    String username = loginInfo.get("username").toString().toUpperCase();
    String encpassword = new String((byte[])loginInfo.get("password"));

    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      String sql = "select USERS.USERNAME,USERS.PASSWORD FROM USERS where USERS.USERNAME=? and USERS.PASSWORD=?";
      stmt = conn.prepareStatement(sql);
      stmt.setString(1,username);
      stmt.setString(2,encpassword);
      rset = stmt.executeQuery();
      if (rset.next())
        return true;
      else
        return false;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    finally {
      try {
        rset.close();
      }
      catch (Exception ex1) {
      }
      try {
        stmt.close();
      }
      catch (Exception ex1) {
      }
    }
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
    DefaultMutableTreeNode root = new OpenSwingTreeNode();
    DefaultTreeModel model = new DefaultTreeModel(root);
    ApplicationFunction n1 = new ApplicationFunction("Administration",null);
    ApplicationFunction n11 = new ApplicationFunction("Users","USERS",null,"getUsers");
    n1.add(n11);
    root.add(n1);

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
        stmt = conn.prepareStatement("create table USERS(USERNAME VARCHAR(20),PASSWORD VARCHAR,DESCRIPTION VARCHAR,PRIMARY KEY(USERNAME))");
        stmt.execute();
        stmt.close();

        String passwd = "admin";
        String encPasswd = new String(CryptUtils.getInstance().encodeText(passwd));

        stmt = conn.prepareStatement("insert into USERS values('ADMIN','"+encPasswd+"','Administrator')");
        stmt.execute();

      }
      catch (Throwable ex1) {
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
