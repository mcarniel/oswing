package demo10;

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
import org.openswing.swing.miscellaneous.client.TipInternalFrame;
import org.openswing.swing.miscellaneous.client.TipPanelContent;
import java.math.BigDecimal;
import org.openswing.swing.table.profiles.client.FileGridProfileManager;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.lookup.client.LookupController;
//import org.openswing.swing.kbs.client.FileKBSStorageManager;


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
    Domain day = new Domain("DAYS");
    day.addDomainPair(new BigDecimal(Calendar.SUNDAY),"sunday");
    day.addDomainPair(new BigDecimal(Calendar.MONDAY),"monday");
    day.addDomainPair(new BigDecimal(Calendar.TUESDAY),"tuesday");
    day.addDomainPair(new BigDecimal(Calendar.WEDNESDAY),"wednesday");
    day.addDomainPair(new BigDecimal(Calendar.THURSDAY),"thursday");
    day.addDomainPair(new BigDecimal(Calendar.FRIDAY),"friday");
    day.addDomainPair(new BigDecimal(Calendar.SATURDAY),"saturday");
    domains.put(
      day.getDomainId(),
      day
    );


    Properties props = new Properties();
    props.setProperty("deptCode","Dept Code");
    props.setProperty("description","Description");
    props.setProperty("address","Address");
    props.setProperty("tasks","Tasks");
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

    props.setProperty("day","Day Of Week");
    props.setProperty("startMorningHour","Start Hour Morning");
    props.setProperty("endMorningHour","End Hour Morning");
    props.setProperty("startAfternoonHour","Start Hour Aftern.");
    props.setProperty("endAfternoonHour","End Hour Aftern.");

    props.setProperty("sunday","Sunday");
    props.setProperty("monday","Monday");
    props.setProperty("tuesday","Tuesday");
    props.setProperty("wednesday","Wednesday");
    props.setProperty("thursday","Thursday");
    props.setProperty("friday","Friday");
    props.setProperty("saturday","Saturday");

    props.setProperty("close","Close");


    // tips...
    props.setProperty(
      "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>press ctrl+i to switch to insert mode in a grid or in a form panel\n"+
      "press ctrl+e to switch to edit mode in a grid or in a form panel\n"+
      "press ctrl+z to switch to read only mode in a grid or in a form panel\n"+
      "press ctrl+d to switch to delete records in a grid or in a form panel.</font></body></html>\n",
      "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>Press CTRL+I to switch to insert mode in a grid or in a form panel\n"+
      "<br/>Press CTRL+E to switch to edit mode in a grid or in a form panel\n"+
      "<br/>Press CTRL+Z to switch to read only mode in a grid or in a form panel\n"+
      "<br/>Press CTRL+D to switch to delete records in a grid or in a form panel.</font></body></html>\n"
    );
    props.setProperty(
      "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>shortcuts can be customized using ClientSettings class</font></body></html>",
      "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>Shortcuts can be customized using ClientSettings class</font></body></html>"
    );

    props.setProperty(
    "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>you may right click with the mouse button inside a grid to show\n"+
    "a popup menu that allows to:\n"+
    "<ul><li>filter data of the current selected column</li>\n"+
    "<li>show/hide columns</li></ul></font></body></html>",
    "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>You may right click with the mouse button inside a grid to show\n"+
    "a popup menu that allows to:\n"+
    "<ul><li>Filter data of the current selected column</li>\n"+
    "<li>Show/hide columns</li></ul></font></body></html>"
    );

    props.setProperty("shortcuts are customizable","Shortcuts are customizable");
    props.setProperty("shortcuts in grid and form controls","Shortcuts in grid and form controls");
    props.setProperty("quick filter and other features in grid control","Quick filter and other features in grid control");



    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("$",props,true),
        domains
    );

    ClientSettings.BACKGROUND = "background4.jpg";
//    ClientSettings.TREE_BACK = "treeback2.jpg";
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.TREE_BACK = null;
    ClientSettings.VIEW_MANDATORY_SYMBOL = true;
    ClientSettings.FILTER_PANEL_ON_GRID = true;
    ClientSettings.SHOW_FILTER_SYMBOL = true;
    ClientSettings.ASK_BEFORE_CLOSE = true;
//    ClientSettings.SHOW_TREE_MENU_ROOT = false;

//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "net.infonode.gui.laf.InfoNodeLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "com.birosoft.liquid.LiquidLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "ch.randelshofer.quaqua.QuaquaLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "com.l2fprod.gui.plaf.skin.SkinLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.jvnet.substance.skin.SubstanceBusinessLookAndFeel";
    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.jvnet.substance.skin.SubstanceMistSilverLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.jvnet.substance.skin.SubstanceModerateLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "net.sourceforge.mlf.metouia.MetouiaLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.fife.plaf.Office2003.Office2003LookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.fife.plaf.OfficeXP.OfficeXPLookAndFeel";
//    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel";

    ClientSettings.GRID_PROFILE_MANAGER = new FileGridProfileManager();
    ClientSettings.LOOKUP_FRAME_CONTENT = LookupController.GRID_AND_FILTER_FRAME;

//    ClientSettings.KB_STORAGE_MANAGER = new FileKBSStorageManager();


    MDIFrame mdi = new MDIFrame(this);

//    com.pagosoft.plaf.PlafOptions.setAsLookAndFeel();
//    com.pagosoft.plaf.PlafOptions.updateAllUIs();

  }


  /**
   * Method called after MDI creation.
   */
  public void afterMDIcreation(MDIFrame frame) {
    MDIFrame.addStatusComponent(new Clock());
    frame.addSeparatorToMenuBar("getEmployees2","getDepts");

    // show tip of the day internal frame...
    showTipFrame();
  }


  /**
   * Show 'tip of the day' internal frame.
   */
  private void showTipFrame() {
    TipPanelContent tipPanel = new TipPanelContent() {

      /**
       * @return list of titles, for each tip
       */
      public String[] getTitles() {
        return new String[] {
            "shortcuts in grid and form controls",
            "shortcuts are customizable",
            "quick filter and other features in grid control"
        };
      }


      /**
       * @return list of tips
       */
      public String[] getTips() {
        return new String[] {
            "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>press ctrl+i to switch to insert mode in a grid or in a form panel\n"+
            "press ctrl+e to switch to edit mode in a grid or in a form panel\n"+
            "press ctrl+z to switch to read only mode in a grid or in a form panel\n"+
            "press ctrl+d to switch to delete records in a grid or in a form panel.</font></body></html>\n",

            "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>shortcuts can be customized using ClientSettings class</font></body></html>",

            "<html><body><font style='FONT-SIZE: 80%;FONT-FAMILY: Verdana, Helvetica, sans-serif'>you may right click with the mouse button inside a grid to show\n"+
            "a popup menu that allows to:\n"+
            "<ul><li>filter data of the current selected column</li>\n"+
            "<li>show/hide columns</li></ul></font></body></html>"
        };
      }


    };
    TipInternalFrame tipFrame1 = new TipInternalFrame(tipPanel);
    tipFrame1.setShowCheck(false);
    tipFrame1.setSize(700,300);
    MDIFrame.add(tipFrame1);
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
    return "Demo10";
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
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new ClientApplication();
      }
    });
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
    n1.setShortCut(new Character('A'));
    ApplicationFunction n11 = new ApplicationFunction("Employees in grid+detail","getEmployees","men.gif","getEmployees");
    n11.setShortCut(new Character('M'));
    ApplicationFunction n12 = new ApplicationFunction("Employees in editable grid","getEmployees2","men.gif","getEmployees2");
    n12.setShortCut(new Character('G'));
    ApplicationFunction n13 = new ApplicationFunction("Departments","getDepts","appicon.gif","getDepts");
    n13.setShortCut(new Character('D'));
    ApplicationFunction n14 = new ApplicationFunction("Tasks","getTasks","appicon.gif","getTasks");
    n14.setShortCut(new Character('K'));
    n1.add(n11);
    n1.add(n12);
    n1.add(n13);
    n1.add(n14);

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

        stmt = conn.prepareStatement("create table WORKING_DAYS(DAY NUMERIC,EMP_CODE VARCHAR,START_MORNING_HOUR DATETIME,END_MORNING_HOUR DATETIME,START_AFTERNOON_HOUR DATETIME,END_AFTERNOON_HOUR DATETIME,PRIMARY KEY(DAY,EMP_CODE))");
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

        Calendar cal = Calendar.getInstance();
        cal.set(cal.YEAR,0);
        cal.set(cal.MONTH,0);
        cal.set(cal.DAY_OF_MONTH,0);
        cal.set(cal.HOUR_OF_DAY,8);
        cal.set(cal.MINUTE,0);
        cal.set(cal.SECOND,0);
        java.sql.Timestamp t1 = new java.sql.Timestamp(cal.getTime().getTime());
        cal.set(cal.HOUR_OF_DAY,12);
        java.sql.Timestamp t2 = new java.sql.Timestamp(cal.getTime().getTime());
        cal.set(cal.HOUR_OF_DAY,13);
        java.sql.Timestamp t3 = new java.sql.Timestamp(cal.getTime().getTime());
        cal.set(cal.HOUR_OF_DAY,17);
        java.sql.Timestamp t4 = new java.sql.Timestamp(cal.getTime().getTime());

        for(int i=0;i<50;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into EMP values('E"+(i<10?"00":"")+(i>=10 && i<100?"0":"")+(i)+"','Name"+(i+1)+"','Surname"+(i+1)+"',"+(1000+i*100)+",?,'M','SF','DEP',null)");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()+86400000*i));
          stmt.execute();
          stmt.close();

          stmt = conn.prepareStatement("insert into WORKING_DAYS values(?,'E"+(i<10?"00":"")+(i>=10 && i<100?"0":"")+(i)+"',?,?,?,?)");
          stmt.setInt(1,cal.SUNDAY);
          stmt.setNull(2,Types.DATE);
          stmt.setNull(2,Types.DATE);
          stmt.setNull(3,Types.DATE);
          stmt.setNull(4,Types.DATE);
          stmt.execute();
          stmt = conn.prepareStatement("insert into WORKING_DAYS values(?,'E"+(i<10?"00":"")+(i>=10 && i<100?"0":"")+(i)+"',?,?,?,?)");
          stmt.setInt(1,cal.MONDAY);
          stmt.setObject(2,t1);
          stmt.setObject(3,t2);
          stmt.setObject(4,t3);
          stmt.setObject(5,t4);
          stmt.execute();
          stmt = conn.prepareStatement("insert into WORKING_DAYS values(?,'E"+(i<10?"00":"")+(i>=10 && i<100?"0":"")+(i)+"',?,?,?,?)");
          stmt.setInt(1,cal.TUESDAY);
          stmt.setObject(2,t1);
          stmt.setObject(3,t2);
          stmt.setObject(4,t3);
          stmt.setObject(5,t4);
          stmt.execute();
          stmt = conn.prepareStatement("insert into WORKING_DAYS values(?,'E"+(i<10?"00":"")+(i>=10 && i<100?"0":"")+(i)+"',?,?,?,?)");
          stmt.setInt(1,cal.WEDNESDAY);
          stmt.setObject(2,t1);
          stmt.setObject(3,t2);
          stmt.setObject(4,t3);
          stmt.setObject(5,t4);
          stmt.execute();
          stmt = conn.prepareStatement("insert into WORKING_DAYS values(?,'E"+(i<10?"00":"")+(i>=10 && i<100?"0":"")+(i)+"',?,?,?,?)");
          stmt.setInt(1,cal.THURSDAY);
          stmt.setObject(2,t1);
          stmt.setObject(3,t2);
          stmt.setObject(4,t3);
          stmt.setObject(5,t4);
          stmt.execute();
          stmt = conn.prepareStatement("insert into WORKING_DAYS values(?,'E"+(i<10?"00":"")+(i>=10 && i<100?"0":"")+(i)+"',?,?,?,?)");
          stmt.setInt(1,cal.FRIDAY);
          stmt.setObject(2,t1);
          stmt.setObject(3,t2);
          stmt.setObject(4,t3);
          stmt.setObject(5,t4);
          stmt.execute();
          stmt = conn.prepareStatement("insert into WORKING_DAYS values(?,'E"+(i<10?"00":"")+(i>=10 && i<100?"0":"")+(i)+"',?,?,?,?)");
          stmt.setInt(1,cal.SATURDAY);
          stmt.setNull(2,Types.DATE);
          stmt.setNull(2,Types.DATE);
          stmt.setNull(3,Types.DATE);
          stmt.setNull(4,Types.DATE);
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


  /**
   * @return <code>true</code> if the MDI frame must show the "File" menu in the menubar of the frame, <code>false</code> to hide it
   */
  public boolean viewFileMenu() {
    return false;
  }


}
