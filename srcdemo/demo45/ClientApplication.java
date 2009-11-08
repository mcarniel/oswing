package demo45;

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
import org.openswing.swing.table.profiles.database.server.DbGridProfileManager;
import org.openswing.swing.table.profiles.database.server.DbConnectionSource;
import org.openswing.swing.table.profiles.database.server.DefaultDbActiveProfileDescriptor;
import org.openswing.swing.table.profiles.database.server.DefaultDbDigestDescriptor;
import org.openswing.swing.table.profiles.database.server.DefaultDbProfileDescriptor;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import org.openswing.swing.table.permissions.database.server.*;


/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method: it creates an MDI Frame app.
 * A database profile management and database grid permissions mangament are applied for this application.
 * Use "ADMIN/admin" or "GUEST/guest" to log on.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication implements MDIController,LoginController,org.openswing.swing.table.profiles.database.server.DbConnectionSource,org.openswing.swing.table.permissions.database.server.DbConnectionSource {


  private DemoClientFacade clientFacade = null;
  private Connection conn = null;
  private String username = null;
  private Hashtable domains = new Hashtable();


  public ClientApplication() {
    createConnection();
    clientFacade = new DemoClientFacade(conn);


    Properties props = new Properties();
    props.setProperty("users","Users");
    props.setProperty("grid permissions","Grid permissions");
    props.setProperty("functions","Functions");
    props.setProperty("visible","Visible");
    props.setProperty("editableInIns","Can ins.");
    props.setProperty("editableInEdit","Can edit");
    props.setProperty("required","Required");

    props.setProperty("group sales","Group Sales");
    props.setProperty("totalAmount","Total Amount");
    props.setProperty("salesNumber","Sales Nr.");
    props.setProperty("area","Area");
    props.setProperty("saleDate","Date");
    props.setProperty("note","Note");
    props.setProperty("roleId","Role");
    props.setProperty("username","Username");
    props.setProperty("password","Password");
    props.setProperty("description","Description");

    props.setProperty("administrator","Administrator");
    props.setProperty("restricted user","Restricted user");

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("$",props,true),
        domains
    );

    ClientSettings.BACKGROUND = "background4.jpg";
    ClientSettings.TREE_BACK = "treeback2.jpg";
    ClientSettings.VIEW_BACKGROUND_SEL_COLOR = true;
    ClientSettings.VIEW_MANDATORY_SYMBOL = true;
    ClientSettings.FILTER_PANEL_ON_GRID = true;
    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";
    ClientSettings.RELOAD_LAST_VO_ON_FORM = true;

    ClientSettings.GRID_PROFILE_MANAGER = new DbGridProfileManager(
      this,
      new DefaultDbActiveProfileDescriptor(),
      new DefaultDbDigestDescriptor(),
      new DefaultDbProfileDescriptor()
    );

    ClientSettings.GRID_PERMISSION_MANAGER = new DbGridPermissionsManager(
      this,
      new org.openswing.swing.table.permissions.database.server.DefaultDbDigestDescriptor(),
      new DefaultDbPermissionsDescriptor()
    );


// an alternative: a primary key that includes another field and additional fields to insert/update...
/*
    ClientSettings.GRID_PROFILE_MANAGER = new DbGridProfileManager(
      this,
      new DefaultDbActiveProfileDescriptor() {

        public Hashtable storeGridProfileIdOnInsert() {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          h.put("CREATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));
          return h;
        }

        public Hashtable storeGridProfileIdOnSetUpdate() {
          Hashtable h = new Hashtable();
          h.put("UPDATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));
          return h;
        }

        public Hashtable storeGridProfileIdOnWhereUpdate() {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          return h ;
        }

        public Hashtable deleteAllGridProfileIds(String functionId) {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          return h ;
        }

      },
      new DefaultDbDigestDescriptor() {

        public Hashtable storeGridDigestOnInsert() {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          h.put("CREATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));
          return h;
        }

        public Hashtable storeGridDigestOnSetUpdate() {
          Hashtable h = new Hashtable();
          h.put("UPDATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));
          return h;
        }

        public Hashtable storeGridDigestOnWhereUpdate() {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          return h;
        }

      },
      new DefaultDbProfileDescriptor() {

        public Hashtable storeUserProfileOnInsert() {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          h.put("CREATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));
          return h;
        }

        public Hashtable storeUserProfileOnSetUpdate() {
          Hashtable h = new Hashtable();
          h.put("UPDATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));
          return h;
        }

        public Hashtable storeUserProfileOnWhereUpdate() {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          return h;
        }

        public Hashtable deleteUserProfile(String functionId,Object id) {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          return h;
        }

        public Hashtable deleteAllGridProfiles(String functionId) {
          Hashtable h = new Hashtable();
          h.put("COMPANY_CODE","COMPANY01");
          return h;
        }

      }
    );
*/

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
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      pstmt = conn.prepareStatement("select * from USERS where USERNAME=? and PASSWORD=?");
      pstmt.setString(1,loginInfo.get("username").toString().toUpperCase());
      pstmt.setString(2,(String)loginInfo.get("password"));
      rset = pstmt.executeQuery();
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
        pstmt.close();
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

    ClientSettings.GRID_PROFILE_MANAGER.setUsername(username);
    ClientSettings.GRID_PERMISSION_MANAGER.setUsername(username);

    domains.clear();
    Domain rolesDomain = new Domain("ROLES");
    rolesDomain.addDomainPair("ALL","administrator");
    rolesDomain.addDomainPair("RESTRICTED","restricted user");
    domains.put(
      rolesDomain.getDomainId(),
      rolesDomain
    );

    Domain functionsDomain = new Domain("FUNCTIONS");
    functionsDomain.addDomainPair("getGroupSales","group sales");
    domains.put(
      functionsDomain.getDomainId(),
      functionsDomain
    );

    MDIFrame mdi = new MDIFrame(this);
//    try {
//      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//      SwingUtilities.updateComponentTreeUI(mdi);
//    }
//    catch (Throwable ex) {
//      ex.printStackTrace();
//    }

  }


  /**
   * @return <code>true</code> if the MDI frame must show a change language menu in the menubar, <code>false</code> no change language menu item will be added
   */
  public boolean viewChangeLanguageInMenuBar() {
    return false;
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
    if (username.equals("ADMIN")) {
      ApplicationFunction n1 = new ApplicationFunction("Administration",null);
      ApplicationFunction n11 = new ApplicationFunction("Users","getUserRoles","men.gif","getUserRoles");
      n1.add(n11);
      ApplicationFunction n12 = new ApplicationFunction("Grid permissions","getGridPermissionsPerRole","grid.gif","getGridPermissionsPerRole");
      n1.add(n12);
      root.add(n1);
    }
    ApplicationFunction n2 = new ApplicationFunction("Functionalities",null);
    ApplicationFunction n21 = new ApplicationFunction("Group Sales","getGroupSales","calendar.gif","getGroupSales");
    n2.add(n21);
    root.add(n2);

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
        stmt = conn.prepareStatement("create table GROUP_SALES(SALE_DATE DATE,AREA VARCHAR,TOTAL_AMOUNT DECIMAL,SALES_NUMBER NUMERIC,NOTE VARCHAR,PRIMARY KEY(SALE_DATE,AREA))");
        stmt.execute();

        for(int i=0;i<1;i++) {
          stmt.close();
          stmt = conn.prepareStatement("insert into GROUP_SALES values(?,'AMERICA',"+(12834545*Math.round(5))+","+Math.round(1000*Math.round(2))+",'')");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()-86400000*i*2));
          stmt.execute();
          stmt.close();

          stmt = conn.prepareStatement("insert into GROUP_SALES values(?,'EUROPE',"+(12834545*Math.round(5))+","+Math.round(1000*Math.round(2))+",'')");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()-86400000*i*2));
          stmt.execute();
          stmt.close();

          stmt = conn.prepareStatement("insert into GROUP_SALES values(?,'ASIA',"+(12834545*Math.round(5))+","+Math.round(1000*Math.round(2))+",'')");
          stmt.setObject(1,new java.sql.Date(System.currentTimeMillis()-86400000*i*2));
          stmt.execute();
        }
        stmt.close();



        stmt = conn.prepareStatement("CREATE TABLE PROFILE_DIGESTS(FUNCTION_ID VARCHAR,DIGEST VARCHAR,PRIMARY KEY(FUNCTION_ID))");
// an alternative: a primary key that includes another field and additional fields to insert/update...
//        stmt = conn.prepareStatement("CREATE TABLE PROFILE_DIGESTS(COMPANY_CODE VARCHAR,FUNCTION_ID VARCHAR,DIGEST VARCHAR,CREATE_DATE DATETIME,UPDATE_DATE DATETIME,PRIMARY KEY(COMPANY_CODE,FUNCTION_ID))");

        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("CREATE TABLE ACTIVE_PROFILES(FUNCTION_ID VARCHAR,USERNAME VARCHAR,ID NUMERIC,PRIMARY KEY(FUNCTION_ID,USERNAME))");
// an alternative: a primary key that includes another field and additional fields to insert/update...
//        stmt = conn.prepareStatement("CREATE TABLE ACTIVE_PROFILES(COMPANY_CODE VARCHAR,FUNCTION_ID VARCHAR,USERNAME VARCHAR,ID NUMERIC,CREATE_DATE DATETIME,UPDATE_DATE DATETIME,PRIMARY KEY(COMPANY_CODE,FUNCTION_ID,USERNAME))");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("CREATE TABLE PROFILES(ID VARCHAR,FUNCTION_ID VARCHAR,USERNAME VARCHAR,DESCRIPTION VARCHAR,SORTED_COLS VARCHAR,SORTED_VERSUS VARCHAR,FILTERS VARCHAR,COLS_POS VARCHAR,COLS_VIS VARCHAR,COLS_WIDTH VARCHAR,IS_DEFAULT CHAR(1),PRIMARY KEY(ID))");
// an alternative: a primary key that includes another field and additional fields to insert/update...
//        stmt = conn.prepareStatement("CREATE TABLE PROFILES(COMPANY_CODE VARCHAR,ID VARCHAR,FUNCTION_ID VARCHAR,USERNAME VARCHAR,DESCRIPTION VARCHAR,SORTED_COLS VARCHAR,SORTED_VERSUS VARCHAR,FILTERS VARCHAR,COLS_POS VARCHAR,COLS_VIS VARCHAR,COLS_WIDTH VARCHAR,IS_DEFAULT CHAR(1),CREATE_DATE DATETIME,UPDATE_DATE DATETIME,PRIMARY KEY(COMPANY_CODE,ID))");
        stmt.execute();





        stmt = conn.prepareStatement("CREATE TABLE PERMISSIONS_DIGESTS(FUNCTION_ID VARCHAR,DIGEST VARCHAR,PRIMARY KEY(FUNCTION_ID))");
// an alternative: a primary key that includes another field and additional fields to insert/update...
//        stmt = conn.prepareStatement("CREATE TABLE PERMISSIONS_DIGESTS(COMPANY_CODE VARCHAR,FUNCTION_ID VARCHAR,DIGEST VARCHAR,CREATE_DATE DATETIME,UPDATE_DATE DATETIME,PRIMARY KEY(COMPANY_CODE,FUNCTION_ID))");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("CREATE TABLE USER_ROLES(USERNAME VARCHAR,ROLE_ID VARCHAR,PRIMARY KEY(USERNAME,ROLE_ID))");
// an alternative: a primary key that includes another field and additional fields to insert/update...
//        stmt = conn.prepareStatement("USER_ROLES(COMPANY_CODE VARCHAR,USERNAME VARCHAR,ROLE_ID VARCHAR,PRIMARY KEY(COMPANY_CODE,USERNAME,ROLE_ID))");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("CREATE TABLE GRID_PERMISSIONS_DEFS(FUNCTION_ID VARCHAR,COLS_POS VARCHAR,EDIT_COLS_IN_INS VARCHAR,EDIT_COLS_IN_EDIT VARCHAR,REQUIRED_COLS VARCHAR,COLS_VIS VARCHAR,PRIMARY KEY(FUNCTION_ID))");
// an alternative: a primary key that includes another field and additional fields to insert/update...
//        stmt = conn.prepareStatement("CREATE TABLE GRID_PERMISSIONS_DEFS(COMPANY_CODE VARCHAR,FUNCTION_ID VARCHAR,COLS_POS VARCHAR,EDIT_COLS_IN_INS VARCHAR,EDIT_COLS_IN_EDIT VARCHAR,REQUIRED_COLS VARCHAR,COLS_VIS VARCHAR,PRIMARY KEY(COMPANY_CODE,FUNCTION_ID))");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("CREATE TABLE GRID_PERMISSIONS(FUNCTION_ID VARCHAR,ROLE_ID VARCHAR,COLS_POS VARCHAR,EDIT_COLS_IN_INS VARCHAR,EDIT_COLS_IN_EDIT VARCHAR,REQUIRED_COLS VARCHAR,COLS_VIS VARCHAR,PRIMARY KEY(FUNCTION_ID,ROLE_ID))");
// an alternative: a primary key that includes another field and additional fields to insert/update...
//        stmt = conn.prepareStatement("CREATE TABLE GRID_PERMISSIONS(COMPANY_CODE VARCHAR,FUNCTION_ID VARCHAR,ROLE_ID VARCHAR,COLS_POS VARCHAR,EDIT_COLS_IN_INS VARCHAR,EDIT_COLS_IN_EDIT VARCHAR,REQUIRED_COLS VARCHAR,COLS_VIS VARCHAR,PRIMARY KEY(COMPANY_CODE,FUNCTION_ID,ROLE_ID))");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("CREATE TABLE USERS(USERNAME VARCHAR,PASSWORD VARCHAR,DESCRIPTION VARCHAR,PRIMARY KEY(USERNAME))");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into USERS(USERNAME,PASSWORD,DESCRIPTION) values('ADMIN','admin','Administrator')");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into USERS(USERNAME,PASSWORD,DESCRIPTION) values('GUEST','guest','Guest user')");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into USER_ROLES(USERNAME,ROLE_ID) values('ADMIN','ALL')");
        stmt.execute();
        stmt.close();
        stmt = conn.prepareStatement("insert into USER_ROLES(USERNAME,ROLE_ID) values('GUEST','RESTRICTED')");
        stmt.execute();

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
   * @param context servlet context; used to retrieve database connection settings
   * @return new database connection
   */
  public Connection getConnection() throws Exception {
    return conn;
  }


  /**
   * Release a database connection
   * @param conn database connection to release
   * @param context servlet context; used to retrieve database connection settings
   */
  public void releaseConnection(Connection conn) {}


  /**
   * @return <code>true</code> if the MDI frame must show the "File" menu in the menubar of the frame, <code>false</code> to hide it
   */
  public boolean viewFileMenu() {
    return true;
  }

}
