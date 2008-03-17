package demo13;

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
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method: it creates an MDI Frame app.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication implements MDIController,LoginController {


  private DemoClientFacade clientFacade = null;
  private Connection conn = null;
  private Hashtable domains = new Hashtable();


  public ClientApplication() {
    createConnection();
    clientFacade = new DemoClientFacade(conn);


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

    MDIFrame mdi = new MDIFrame(this);
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
    return 0;
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
  public void loginSuccessful(Map loginInfo) { }



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
   * Create the database connection (using Hypersonic DB - in memory) and initialize tables...
   */
  private void createConnection() {
    try {
      Class.forName("org.hsqldb.jdbcDriver");
      conn = DriverManager.getConnection("jdbc:hsqldb:mem:"+"a"+Math.random(),"sa","");
      PreparedStatement stmt = null;
      try {
        stmt = conn.prepareStatement("create table EMP(EMP_CODE VARCHAR,FIRST_NAME VARCHAR,LAST_NAME VARCHAR,SALARY DECIMAL(10,2),HIRE_DATE DATE,SEX CHAR(1),DEPT_CODE VARCHAR,TASK_CODE VARCHAR,NOTE VARCHAR,PRIMARY KEY(EMP_CODE))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table TASKS(TASK_CODE VARCHAR,DESCRIPTION VARCHAR,STATUS CHAR(1),PRIMARY KEY(TASK_CODE))");
        stmt.execute();
        stmt.close();

        stmt = conn.prepareStatement("create table DEPT(DEPT_CODE VARCHAR,DESCRIPTION VARCHAR,ADDRESS VARCHAR,STATUS CHAR(1),PRIMARY KEY(DEPT_CODE))");
        stmt.execute();

        stmt.close();
        stmt = conn.prepareStatement("insert into TASKS values('DEV','Developer','E')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into TASKS values('PM','Project Manager','E')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into TASKS values('ANA','Analist','E')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into TASKS values('TEST','Tester','E')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into TASKS values('DEP','Deployer','E')");
        stmt.execute();

        stmt.close();
        stmt = conn.prepareStatement("insert into DEPT values('S','Sales','5th Ev. - NY','E')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into DEPT values('P','Purchases','5th Ev. - NY','E')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into DEPT values('A','Accounting','14 Rome St.  - London','E')");
        stmt.execute();
        stmt = conn.prepareStatement("insert into DEPT values('SF','Software Factory','14 Rome St.  - London','E')");
        stmt.execute();

        for(int i=0;i<10;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into EMP values('E"+(i+1)+"','Name"+(i+1)+"','Surname"+(i+1)+"',"+(1000+i*100)+",?,'M','SF','DEP',null)");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
        }

        for(int i=11;i<20;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into EMP values('E"+(i+1)+"','Name"+(i+1)+"','Surname"+(i+1)+"',"+(1000+i*100)+",?,'M','P','DEP',null)");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
        }

        for(int i=21;i<30;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into EMP values('E"+(i+1)+"','Name"+(i+1)+"','Surname"+(i+1)+"',"+(1000+i*100)+",?,'M','S','DEP',null)");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
        }

        stmt.close();

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
