package demo32;

import org.openswing.swing.tree.java.OpenSwingTreeNode;
import java.util.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.client.*;
import javax.swing.*;
import org.openswing.swing.internationalization.java.Language;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.client.SplashScreen;
import org.openswing.swing.miscellaneous.client.IconifableWindowsContainer;
import org.openswing.swing.miscellaneous.client.IconifableWindow;
import java.awt.BorderLayout;
import org.openswing.swing.client.ListControl;
import org.openswing.swing.client.ListVOControl;
import org.openswing.swing.items.client.ItemsDataLocator;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import com.toedter.calendar.JCalendar;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Used to start application from main method:
 * it creates an email client application, based on the MDI paradimg.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ClientApplication implements MDIController,LoginController {

  private JSplitPane mainPane = new JSplitPane();
  private IconifableWindowsContainer iconifablePanes = new IconifableWindowsContainer();


  private DemoClientFacade clientFacade = new DemoClientFacade();

  public ClientApplication() {
    Properties props = new Properties();

    ClientSettings clientSettings = new ClientSettings(
        new EnglishOnlyResourceFactory("E",props,false),
        new Hashtable()
    );
    ClientSettings.ICON_FILENAME = "email3.gif";
    ClientSettings.BACKGROUND = "background4.jpg";
    ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel";
//    ClientSettings.MAX_MENU_WIDTH = 300;
//    ClientSettings.MENU_WIDTH = 300;

    MDIFrame mdi = new MDIFrame(this);
//    mdi.setLocked(false);
  }


  /**
   * Method called after MDI creation.
   */
  public void afterMDIcreation(MDIFrame frame) {
    frame.getContentPane().add(mainPane);
    mainPane.setDividerLocation(350);
    mainPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    mainPane.add(iconifablePanes,JSplitPane.LEFT);
    JPanel c = new JPanel();
    mainPane.add(c,mainPane.RIGHT);

    iconifablePanes.setAutoExpandWindow(true);
    IconifableWindow w1 = new IconifableWindow();
    w1.setTitle("Calendar");
    w1.setTitleImageName("calendar.gif");
    iconifablePanes.addIconifableWindow(w1);
    w1.setLayout(new GridBagLayout());
    Calendar cal = Calendar.getInstance();
    cal.set(cal.MONTH,cal.getInstance().get(cal.MONTH)-1);
    Date d1 = cal.getTime();
    cal.set(cal.MONTH,cal.getInstance().get(cal.MONTH));
    Date d2 = cal.getTime();
    cal.set(cal.MONTH,cal.getInstance().get(cal.MONTH)+1);
    Date d3 = cal.getTime();
    w1.add(new JCalendar(d1),      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
       ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
    w1.add(new JCalendar(d2),      new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
       ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));
    w1.add(new JCalendar(d3),      new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
       ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5,5,5,5), 0, 0));

    IconifableWindow w2 = new IconifableWindow();
    w2.setTitle("Email");
    w2.setTitleImageName("email3.gif");
    iconifablePanes.addIconifableWindow(w2);

    IconifableWindow w3 = new IconifableWindow();
    w3.setTitle("Contacts");
    w3.setTitleImageName("men.gif");
    iconifablePanes.addIconifableWindow(w3);
    w3.setLayout(new BorderLayout());
    ListVOControl l3 = new ListVOControl();
    l3.setAllColumnVisible(false);
    w3.add(l3,BorderLayout.CENTER);
    l3.setListValueObjectClassName("demo32.ContactsVO");
    l3.setVisibleColumn("emailAddress",true);
    l3.setVisibleColumn("name",true);
    l3.setVisibleColumn("surname",true);
    l3.setPreferredWidthColumn("emailAddress",200);
    l3.setListDataLocator(new ItemsDataLocator() {
      public Response loadData(Class valueObjectType) {
        ArrayList list = new ArrayList();
        ContactsVO vo1 = new ContactsVO();
        vo1.setName("Mauro");
        vo1.setSurname("Mauro");
        vo1.setEmailAddress("mcarniel@users.sourceforge.net");
        list.add(vo1);
        ContactsVO vo2 = new ContactsVO();
        vo2.setName("Sourceforge.net");
        vo2.setEmailAddress("noreply@sourceforge.net");
        list.add(vo2);
        return new VOListResponse(list,false,list.size());
      }
    });

    IconifableWindow w4 = new IconifableWindow();
    w4.setTitle("Activities");
    w4.setTitleImageName("exec.gif");
    iconifablePanes.addIconifableWindow(w4);

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
    return false;
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
    return "Email Client Application";
  }


  /**
   * @return text to view in the about dialog window
   */
  public String getAboutText() {
    return
        "This is an email client application,\n"+
        "based on the MDI paradigm\n"+
        "\n"+
        "Copyright: Copyright (C) 2007 Mauro Carniel\n"+
        "Author: Mauro Carniel";
  }


  /**
   * @return image name to view in the about dialog window
   */
  public String getAboutImage() {
    return "setup3.gif";
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
    ApplicationFunction n0 = new ApplicationFunction("Email",null);
    ApplicationFunction n1 = new ApplicationFunction("New message","NEW",null,"newEmail");
    ApplicationFunction n2 = new ApplicationFunction("Reply","REPLY",null,"replyToEmail");
    ApplicationFunction n3 = new ApplicationFunction("Send/Receive","SENDRECEIVE",null,"sendReceive");
    ApplicationFunction n4 = new ApplicationFunction("Options","OPTIONS",null,"options");

    ApplicationFunction n5 = new ApplicationFunction("Tools",null);
    ApplicationFunction n6 = new ApplicationFunction("Contacts","CONTACTS",null,"contacts");
    ApplicationFunction n7 = new ApplicationFunction("Calendar","CONTACTS",null,"calendar");
    ApplicationFunction n8 = new ApplicationFunction("Activities","ACTIVITIES",null,"activities");

    root.add(n0);
    n0.add(n1);
    n0.add(n2);
    n0.add(n3);

    root.add(n5);
    n5.add(n6);
    n5.add(n7);
    n5.add(n8);

    return model;
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
    return true;
  }

}
