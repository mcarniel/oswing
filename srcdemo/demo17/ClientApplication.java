package demo17;

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
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method: it creates an MDI Frame app
 * and uses Hibernate as persistent layer.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication implements MDIController,LoginController {


  private DemoClientFacade clientFacade = null;
  private Hashtable domains = new Hashtable();


  public ClientApplication() {

    try {
      // create the SessionFactory from hibernate.cfg.xml
      SessionFactory sessions = new Configuration().configure().buildSessionFactory();
      createConnection(sessions);
      clientFacade = new DemoClientFacade(sessions);


      Hashtable domains = new Hashtable();
      Domain sexDomain = new Domain("SEX");
      sexDomain.addDomainPair("M","male");
      sexDomain.addDomainPair("F","female");
      domains.put(
        sexDomain.getDomainId(),
        sexDomain
      );

      Properties props = new Properties();

      props.setProperty("address.phone","Phone");
      props.setProperty("address.address","Address");
      props.setProperty("address.state","State");
      props.setProperty("address.country","Country");
      props.setProperty("address.id","");
      props.setProperty("status","");
      props.setProperty("address.city","City");
      props.setProperty("firstName","First Name");
      props.setProperty("working place","Working place");
      props.setProperty("lastName","Last Name");
      props.setProperty("deptDescription","Dept. Description");
      props.setProperty("dept.deptCode","Dept. Code");
      props.setProperty("dept.description","Dept. Description");
      props.setProperty("taskDescription","Task Description");
      props.setProperty("task.taskCode","Task Code");
      props.setProperty("task.description","Task Description");
      props.setProperty("hire date","Hire Date");
      props.setProperty("sex","Sex");
      props.setProperty("male","Male");
      props.setProperty("female","Female");
      props.setProperty("salary","Salary");
      props.setProperty("empCode","Employee Code");
      props.setProperty("deptCode","Dept Code");
      props.setProperty("description","Description");
      props.setProperty("address","Address");
      props.setProperty("tasks","Tasks");
      props.setProperty("city","City");
      props.setProperty("state","State");
      props.setProperty("country","Country");
      props.setProperty("departments","Departments");
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
      props.setProperty("employees and departments","Employees and Departments");

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
    catch (Throwable ex) {
      ex.printStackTrace();
    }

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
    ApplicationFunction n13 = new ApplicationFunction("Tasks","TASKS","appicon.gif","getTasks");
    ApplicationFunction n14 = new ApplicationFunction("Departments","DEPT","appicon.gif","getDepts");
    ApplicationFunction n15 = new ApplicationFunction("Emps","EMP","appicon.gif","getEmps");
    ApplicationFunction n16 = new ApplicationFunction("Emps and Depts","EMPDEPT","appicon.gif","getEmpDepts");
    n1.add(n13);
    n1.add(n14);
    n1.add(n15);
    n1.add(n16);
    root.add(n1);

    return model;
  }



  /**
   * Create the database connection (using Hypersonic DB - in memory) and initialize tables...
   */
  private void createConnection(SessionFactory sessions) {
    try {
      Session session = sessions.getCurrentSession();
      session.beginTransaction();

      Connection conn = session.connection();
      PreparedStatement stmt = null;
      try {
//        stmt = conn.prepareStatement("create table TASKS(TASK_CODE VARCHAR,DESCRIPTION VARCHAR,STATUS CHAR(1),PRIMARY KEY(TASK_CODE))");
//        stmt.execute();
//        stmt.close();

        for(int i=0;i<200;i++) {
          stmt = conn.prepareStatement("insert into TASKS values('CODE"+i+"','Description"+i+"','E')");
          stmt.execute();
        }
        stmt.close();

        stmt = conn.prepareStatement("insert into ADDRESSES values(1,'34, Fifth Av.','New York','NY','USA',222123456)");
        stmt.execute();
        stmt = conn.prepareStatement("insert into ADDRESSES values(2,'34, Brown St.','Los Angeles','CA','USA',111654321)");
        stmt.execute();

        for(int i=0;i<5;i++) {
          stmt = conn.prepareStatement("insert into DEPTS values('D"+i+"','Description"+i+"',1,'E')");
          stmt.execute();
        }
        stmt.close();

        for(int i=5;i<10;i++) {
          stmt = conn.prepareStatement("insert into DEPTS values('D"+i+"','Description"+i+"',2,'E')");
          stmt.execute();
        }
        stmt.close();

        for(int i=0;i<10;i++) {
          stmt = conn.prepareStatement("insert into EMPS(EMP_CODE,FIRST_NAME,LAST_NAME,DEPT_CODE,SEX,SALARY,HIRE_DATE,NOTE,TASK_CODE,WORKING_PLACE) values('"+i+"','Name"+i+"','Surname"+i+"','D0','M',1233"+i+",null,null,'CODE0','New York')");
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

      session.flush();
      session.getTransaction().commit();

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
