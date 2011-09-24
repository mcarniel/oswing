package org.openswing.swing.mdi.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.tree.*;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.mdi.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.client.OptionPane;
import java.beans.Beans;
import java.beans.PropertyVetoException;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: MDI Frame.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class MDIFrame extends JFrame implements BusyListener {


  static {
    try {
      if (!Beans.isDesignTime() &&
          System.getProperty("os.name").toLowerCase().startsWith("linux") &&
          System.getProperty("java.version").startsWith("1.6") &&
          ClientSettings.LOOK_AND_FEEL_CLASS_NAME.endsWith("GTKLookAndFeel"))
        ClientSettings.LOOK_AND_FEEL_CLASS_NAME = "javax.swing.plaf.metal.MetalLookAndFeel";
      UIManager.setLookAndFeel(ClientSettings.LOOK_AND_FEEL_CLASS_NAME);
    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }
    try {
      JFrame.setDefaultLookAndFeelDecorated(ClientSettings.MDI_FRAME_DECORATED);
      JDialog.setDefaultLookAndFeelDecorated(ClientSettings.MDI_FRAME_DECORATED);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private JPanel contentPane;
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuFile = new JMenu();
  private JMenuItem menuFileChangeUser = new JMenuItem();
  private JMenuItem menuFileChangeLanguage = new JMenuItem();
  private JMenuItem menuFileExit = new JMenuItem();
  private static WindowMenu menuWindow = new WindowMenu();
  private JMenu menuHelp = new JMenu();
  private JMenuItem menuHelpAbout = new JMenuItem();
  private static StatusBar statusBar = new StatusBar();
  private BorderLayout borderLayout1 = new BorderLayout();
  private static DesktopPane desktopPane = new DesktopPane();
  private JScrollPane scrollPane = new JScrollPane(desktopPane);
  private static TreeMenu treeMenu = null;
  private JSplitPane splitPane = new JSplitPane();
  private BasicSplitPaneUI splitPaneUI = new BasicSplitPaneUI();

  /** setBusy calls number */
  private static Integer busyCount = new Integer(0);

  /** interface used to manage some MDI Frame settings */
  private static MDIController client = null;

  /** panel at the bottom, containing opened window icons (optional) and status panel */
  private JPanel bottomPanel = new JPanel();

  /** panel containing opened window icons (optional) */
  private static WinIconsPanel winIconsPanel = new WinIconsPanel();

  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  /** last tree menu width */
  private int lastTreeMenuWidth = 0;

  /** toolbar (optional); it will be showed only if there is at least one button added to it (through "addToolbarButton" method) */
  private JToolBar toolbar = new JToolBar();

  /** flag used to check if toolbar has been added to main panel */
  private boolean toolbarAdded = false;

  /** collection of pairs <functionId,related JMenuItem> */
  private Hashtable functionsHooks = new Hashtable();

  /** collection of pairs <functionId,related JMenu> */
  private Hashtable functionsMenuHooks = new Hashtable();

  /** collection of pairs <component name,JComponent added to the status bar> */
  private static Hashtable componentsHooks = new Hashtable();

  /** unique instances of internal frames, expressed as Class elements */
  private HashSet uniqueInstances = new HashSet();



  /**
   * Contructor.
   * @param client interface used to manage some MDI Frame settings
   */
  public MDIFrame(MDIController client) {
    super(client.getMDIFrameTitle());
    try {
      ApplicationEventQueue.getInstance();
      SwingUtilities.updateComponentTreeUI(this);
      this.client = client;
      ClientUtils.addBusyListener(this);
      statusBar.removeAll();
      componentsHooks.clear();
      menuWindow.init();
      menuWindow.menuWindowCloseAll_actionPerformed(null);
      enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      desktopPane.init(client);
      winIconsPanel.init();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setSize(screenSize);
      try {
        jbInit();
      }
      catch (Exception e) {
        e.printStackTrace();
      }

      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          onWindowClosing();
        }
      });

      try {
        ClientSettings.getInstance().getUserGridProfiles().clear();
        ClientSettings.getInstance().getLastUserGridDigests().clear();
        ClientSettings.getInstance().getLastUserGridProfileIds().clear();
        ClientSettings.getInstance().getGridProfileDescriptions().clear();
        ClientSettings.getInstance().getGridPermissions().clear();
        ClientSettings.getInstance().getLastGridPermissionsDigests().clear();
      }
      catch (Exception ex2) {
      }

      if (ClientSettings.MDI_TOOLBAR!=null) {
        // add toolbar...
        toolbar.add(ClientSettings.MDI_TOOLBAR);
        if (!toolbarAdded) {
          toolbarAdded = true;
          contentPane.add(toolbar,BorderLayout.NORTH);
        }
      }

      //view MDI...
      try {
        Thread.sleep(500);
      }
      catch (Exception ex1) {
      }
      setVisible(true);


      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          MDIFrame.this.client.afterMDIcreation(MDIFrame.this);
        }
      });

    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }

  }


  //Component initialization
  private void jbInit() throws Exception  {
    setIconImage(ClientUtils.getImage(ClientSettings.ICON_FILENAME));
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout1);
    statusBar.setText(" ");
    menuFile.setText(ClientSettings.getInstance().getResources().getResource("file"));
    menuFile.setMnemonic(ClientSettings.getInstance().getResources().getResource("filemnemonic").charAt(0));

    menuFileExit.setText(ClientSettings.getInstance().getResources().getResource("exit"));
    menuFileExit.setMnemonic(ClientSettings.getInstance().getResources().getResource("exitmnemonic").charAt(0));
    menuFileExit.setAccelerator(KeyStroke.getKeyStroke('X',Event.CTRL_MASK));
    menuFileExit.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        menuFileExit_actionPerformed(e);
      }
    });
    if(ClientSettings.ICON_MENU_FILE_EXIT!=null)
     this.menuFileExit.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_FILE_EXIT)));

    menuFileChangeUser.setText(ClientSettings.getInstance().getResources().getResource("change user"));
    menuFileChangeUser.setMnemonic(ClientSettings.getInstance().getResources().getResource("changeusermnemonic").charAt(0));
    menuFileChangeUser.setAccelerator(KeyStroke.getKeyStroke('U',Event.CTRL_MASK));
    menuFileChangeUser.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        menuFileChangeUser_actionPerformed(e);
      }
    });
    if(ClientSettings.ICON_MENU_FILE_CHANGE_USER!=null)
      this.menuFileChangeUser.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_FILE_CHANGE_USER)));

    menuFileChangeLanguage.setText(ClientSettings.getInstance().getResources().getResource("change language"));
    menuFileChangeLanguage.setMnemonic(ClientSettings.getInstance().getResources().getResource("changelanguagemnemonic").charAt(0));
    menuFileChangeLanguage.setAccelerator(KeyStroke.getKeyStroke('L',Event.CTRL_MASK));
    menuFileChangeLanguage.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        menuFileChangeLanguage_actionPerformed(e);
      }
    });
    if(ClientSettings.ICON_MENU_FILE_CHANGE_LANGUAGE!=null)
      this.menuFileChangeLanguage.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_FILE_CHANGE_LANGUAGE)));


    menuHelp.setText(ClientSettings.getInstance().getResources().getResource("help"));
    menuHelp.setMnemonic(ClientSettings.getInstance().getResources().getResource("helpmnemonic").charAt(0));

    menuHelpAbout.setText(ClientSettings.getInstance().getResources().getResource("about")+" "+client.getMDIFrameTitle());
    menuHelpAbout.setMnemonic(ClientSettings.getInstance().getResources().getResource("aboutmnemonic").charAt(0));
    menuHelpAbout.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        menuHelpAbout_actionPerformed(e);
      }
    });
    if(ClientSettings.ICON_MENU_HELP_ABOUT!=null)
      this.menuHelpAbout.setIcon(new ImageIcon(ClientUtils.getImage(ClientSettings.ICON_MENU_HELP_ABOUT)));

    if (client.viewLoginInMenuBar())
      menuFile.add(menuFileChangeUser);
    if (client.viewChangeLanguageInMenuBar() && client.getLanguages().size()>1)
      menuFile.add(menuFileChangeLanguage);
    menuFile.addSeparator();
    menuFile.add(menuFileExit);
    menuHelp.add(menuHelpAbout);
    if (client.viewFileMenu())
      menuBar.add(menuFile);

    DefaultTreeModel functions = null;
    try {
      functions = client.getApplicationFunctions();

      if (client.viewFunctionsInMenuBar()) {
        // creating functions menu...
        JMenuItem menu = null;
        TreeNode root = (TreeNode) functions.getRoot();
        HashSet mnem = new HashSet();
        for (int i = 0; i < root.getChildCount(); i++) {
          final ApplicationFunction function = (ApplicationFunction) root.getChildAt(i);

          if (function.isFolder()) {
            menu = new JMenu(function.toString());
            if (ClientSettings.SHOW_TOOLTIP_IN_MENUBAR)
              menu.setToolTipText(function.getTooltipText());
            maybeAddTooltipToStatusBar(menu,function);
          }
          else if (function.isSeparator()) {
            continue;
          }
          else {
            menu = new JMenu(function.toString());
            if (ClientSettings.SHOW_TOOLTIP_IN_MENUBAR)
              menu.setToolTipText(function.getTooltipText());
            maybeAddTooltipToStatusBar(menu,function);

            ((JMenu)menu).addMenuListener(new MenuListener() {

                  /**
                   * Invoked when a menu is selected.
                   *
                   * @param e  a MenuEvent object
                   */
                  public void menuSelected(MenuEvent e) {
                    if (function.getFunctionId()!=null)
                      executeFunction(function);
                  }

                  /**
                   * Invoked when the menu is deselected.
                   *
                   * @param e  a MenuEvent object
                   */
                  public void menuDeselected(MenuEvent e) {}

                  /**
                   * Invoked when the menu is canceled.
                   *
                   * @param e  a MenuEvent object
                   */
                  public void menuCanceled(MenuEvent e) {}

            });
          }
          int j=0;
          try {
            if (function.getShortCut()!=null && !mnem.contains(function.getShortCut().toString())) {
                mnem.add(function.getShortCut().toString());
                menu.setMnemonic(function.getShortCut().charValue());
            }
            else {
              while (j < function.toString().length() &&
                     mnem.contains(function.toString().substring(j, j + 1))) {
                j++;
              }
              if (j < function.toString().length()) {
                mnem.add(function.toString().substring(j, j + 1));
                menu.setMnemonic(function.toString().substring(j, j + 1).charAt(0));
              }
            }

            if (function.getAccelerator()!=null) {
                menu.setAccelerator(function.getAccelerator());
            }

          }
          catch (Exception ex1) {
          }

          menuBar.add(menu);
          addSubFunctionsToMenuBar(menu, function, mnem);
        }
      }
    }
    catch (Exception ex) {
      Logger.error(this.getClass().getName(),"jbInit","Error while constructing the functions menu",ex);
    }

    if (ClientSettings.SHOW_WINDOW_MENU)
      menuBar.add(menuWindow);
    menuBar.add(menuHelp);
    this.setJMenuBar(menuBar);

    JFrame.setDefaultLookAndFeelDecorated(ClientSettings.MDI_FRAME_DECORATED);
    GraphicsEnvironment env =
      GraphicsEnvironment.getLocalGraphicsEnvironment();
    this.setExtendedState(this.getExtendedState() | client.getExtendedState());
    desktopPane.setPreferredSize(new Dimension(this.getWidth()-ClientSettings.DIVIDER_WIDTH-6-ClientSettings.MENU_WIDTH,this.getHeight()-menuBar.getHeight()-statusBar.getHeight()-103));

    if (client.viewFunctionsInTreePanel() && ((TreeNode)functions.getRoot()).getChildCount()>0) {
      // add tree nodes...
      treeMenu = new TreeMenu(functions);
      treeMenu.setMinimumSize(new Dimension(ClientSettings.MIN_MENU_WIDTH,0));
      treeMenu.setMaximumSize(new Dimension(ClientSettings.MAX_MENU_WIDTH,0));
      treeMenu.addMouseListener(new MenuMouseListener());
      lastTreeMenuWidth = treeMenu.getWidth();
      splitPane.setLeftComponent(treeMenu);

      if (ClientSettings.SHOW_SCROLLBARS_IN_MDI) {
        scrollPane.setPreferredSize(
          new Dimension(
            this.getWidth()-ClientSettings.DIVIDER_WIDTH-6-ClientSettings.MENU_WIDTH,
            this.getHeight()-menuBar.getHeight()-statusBar.getHeight()-103
          )
        );
        desktopPane.setPreferredSize(
          new Dimension(
            this.getWidth()-ClientSettings.DIVIDER_WIDTH-6-ClientSettings.MENU_WIDTH-scrollPane.getVerticalScrollBar().getPreferredSize().width,
            this.getHeight()-menuBar.getHeight()-statusBar.getHeight()-103-scrollPane.getHorizontalScrollBar().getPreferredSize().height-20
          )
        );
        splitPane.setRightComponent(scrollPane);
        scrollPane.getViewport().add(desktopPane);
      }
      else
        splitPane.setRightComponent(desktopPane);

      resetSplitPane();
      contentPane.add(splitPane, BorderLayout.CENTER);

      // add key listener to intercept an F3 key pressed event...
      ApplicationEventQueue.getInstance().addKeyListener(new KeyAdapter() {

        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode()==ClientSettings.TREE_MENU_KEY.getKeyCode() &&
              e.getModifiers()+e.getModifiersEx()==ClientSettings.TREE_MENU_KEY.getModifiers()) {
            // shortcut key pressed: focus will be setted on this component...
            treeMenu.getFindTF().requestFocus();
          }

        }

      });

      treeMenu.addComponentListener(new ComponentAdapter() {

        /**
         * Invoked when the component's size changes.
         */
        public void componentResized(ComponentEvent e) {
          JInternalFrame[] frames = desktopPane.getAllFrames();
          for(int i=0;i<frames.length;i++)
            frames[i].setLocation(lastTreeMenuWidth-treeMenu.getWidth()+frames[i].getX(),frames[i].getY());
          lastTreeMenuWidth = treeMenu.getWidth();
        }


      });


    }
    else {
      if (ClientSettings.SHOW_SCROLLBARS_IN_MDI) {
        scrollPane.setPreferredSize(
          new Dimension(
            this.getWidth()-ClientSettings.DIVIDER_WIDTH-6-ClientSettings.MENU_WIDTH,
            this.getHeight()-menuBar.getHeight()-statusBar.getHeight()-103
          )
        );
        desktopPane.setPreferredSize(
          new Dimension(
            this.getWidth()-ClientSettings.DIVIDER_WIDTH-6-ClientSettings.MENU_WIDTH-scrollPane.getVerticalScrollBar().getPreferredSize().width,
            this.getHeight()-menuBar.getHeight()-statusBar.getHeight()-103-scrollPane.getHorizontalScrollBar().getPreferredSize().height-20
          )
        );
        scrollPane.getViewport().add(desktopPane);
        contentPane.add(scrollPane, BorderLayout.CENTER);
      }
      else
        contentPane.add(desktopPane, BorderLayout.CENTER);
    }

    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    bottomPanel.setLayout(gridBagLayout1);
    if (client.viewOpenedWindowIcons())
      bottomPanel.add(winIconsPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    bottomPanel.add(statusBar,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

  }


  private void maybeAddTooltipToStatusBar(JMenuItem menu,final ApplicationFunction function) {
    if (ClientSettings.SHOW_TOOLTIP_IN_MDISTATUSBAR) {
      ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
      menu.addMouseListener(new MouseAdapter() {

        public void mouseEntered(MouseEvent e) {
          getStatusBar().setText(function.getTooltipText());
        }

        public void mouseExited(MouseEvent e) {
          getStatusBar().setText("");
        }

      });
    }
  }


  /**
   * Add application functions to the menu bar.
   * @param parentMenu menu item just added
   * @param parentFunction application function to expand, organized as a tree
   */
  private void addSubFunctionsToMenuBar(JMenuItem parentMenu,ApplicationFunction parentFunction,HashSet mnem) {
    JMenuItem menu = null;
    for (int i = 0; i < parentFunction.getChildCount(); i++) {
      final ApplicationFunction function = (ApplicationFunction) parentFunction.getChildAt(i);

      if (function.isFolder()) {
        menu = new JMenu(function.toString());
        if (ClientSettings.SHOW_TOOLTIP_IN_MENUBAR)
          menu.setToolTipText(function.getTooltipText());
        maybeAddTooltipToStatusBar(menu,function);
      }
      else if (function.isSeparator()) {
        parentMenu.add(new JSeparator());
        continue;
      }
      else {
        if (function.getIconName()==null) {
          menu = new JMenuItem(function.toString());
          if (ClientSettings.SHOW_TOOLTIP_IN_MENUBAR)
            menu.setToolTipText(function.getTooltipText());
          maybeAddTooltipToStatusBar(menu,function);
        }
        else {
          menu = new JMenuItem(function.toString(),new ImageIcon(ClientUtils.getImage(function.getIconName())));
          if (ClientSettings.SHOW_TOOLTIP_IN_MENUBAR)
            menu.setToolTipText(function.getTooltipText());
          maybeAddTooltipToStatusBar(menu,function);
        }
        menu.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            executeFunction(function);
          }
        });

        if (function.getFunctionId()!=null&& !function.getFunctionId().trim().equals("")) {
          functionsHooks.put(function.getFunctionId(),menu);
          functionsMenuHooks.put(function.getFunctionId(),parentMenu);
        }
      }
      int j=0;
      if (function.getShortCut()!=null && !mnem.contains(function.getShortCut().toString())) {
          mnem.add(function.getShortCut().toString());
          menu.setMnemonic(function.getShortCut().charValue());
      }
      else {
        try {
          while (j < menu.getText().length() &&
                 (mnem.contains(menu.getText().substring(j, j + 1)) || !Character.isLetterOrDigit(menu.getText().charAt(j)))) {
            j++;
          }
          if (j < menu.getText().length()) {
            mnem.add(menu.getText().substring(j, j + 1));
            menu.setMnemonic(menu.getText().substring(j, j + 1).charAt(0));
          }
        }
        catch (Exception ex1) {
        }
      }

      if (function.getAccelerator()!=null) {
          menu.setAccelerator(function.getAccelerator());
      }

      parentMenu.add(menu);
      addSubFunctionsToMenuBar(menu, function, mnem);
    }
  }


  /**
   * Method called by addSubFunctionsToMenuBar method: execute the corrisponding method in the ClientFacade.
   */
  private void executeFunction(final ApplicationFunction node) {
    ClientUtils.fireBusyEvent(true);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        try {
          try {
            MDIFrame.getClientFacade().getClass().getMethod(node.getMethodName(),new Class[0]).invoke(MDIFrame.getClientFacade(),new Object[0]);
            ClientUtils.fireBusyEvent(false);
          }
          catch (NoSuchMethodException ex1) {
            MDIFrame.getClientFacade().getClass().getMethod(node.getMethodName(),new Class[]{String.class}).invoke(MDIFrame.getClientFacade(),new Object[]{node.getFunctionId()});
            ClientUtils.fireBusyEvent(false);
          }
        } catch (Throwable ex) {
          ClientUtils.fireBusyEvent(false);
          ex.printStackTrace();

          OptionPane.showMessageDialog(
              MDIFrame.this,
              ClientSettings.getInstance().getResources().getResource("Error while executing function")+" '"+node.getMethodName()+"'",
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.WARNING_MESSAGE
          );
        }
      }
    });

  }



  /**
   * @return <code>true</code> if menu window is locked <code>false</code> otherwise
   */
  public final boolean isLocked() {
    return treeMenu.isLocked();
  }


  /**
   * @param locked <code>true</code> if menu window is locked <code>false</code> otherwise
   */
  public final void setLocked(boolean locked) {
    treeMenu.setLocked(locked);
    if (!locked && splitPane.getDividerLocation()>0)
      splitPane.setDividerLocation(0);

  }


  //File | Exit action performed
  public void menuFileExit_actionPerformed(ActionEvent e) {
    onWindowClosing();
  }

  //File | Change User action performed
  public void menuFileChangeUser_actionPerformed(ActionEvent e) {
    client.viewLoginDialog(this);
  }

  //File | Chnage Language performed
  public void menuFileChangeLanguage_actionPerformed(ActionEvent e) {
    new ChangeLanguageDialog(this,client);
  }



  //Help | About action performed
  public void menuHelpAbout_actionPerformed(ActionEvent e) {
    AboutDialog dlg = new AboutDialog(this,client.getAboutText(),client.getAboutImage());
  }


  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      onWindowClosing();
    }
    else
      super.processWindowEvent(e);
  }


  /**
   * Define splitpane area.
   */
  private void resetSplitPane() {
    splitPane.setUI(splitPaneUI);
    splitPane.setPreferredSize(this.getSize());
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerSize(ClientSettings.DIVIDER_WIDTH);
    splitPane.setDividerLocation(ClientSettings.MENU_WIDTH);
    splitPane.setLastDividerLocation(0);
    splitPaneUI.getDivider().addMouseListener(new DivMouseListener());
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class for listening menu window mouse exit events.</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class MenuMouseListener extends MouseAdapter {

    public void mouseExited(MouseEvent e) {
      if (!isLocked() &&
          (e.getY()<0 || e.getX()>treeMenu.getWidth()) ) {
        if (splitPane.getDividerLocation()>0)
          splitPane.setDividerLocation(0);
      }
    }

  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class for listening menu window mouse exit events.</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class DivMouseListener extends MouseAdapter {

    public void mouseEntered(MouseEvent e) {
      if (!isLocked()) {
        if (splitPane.getDividerLocation()<=1)
          splitPane.setDividerLocation(splitPane.getLastDividerLocation());
      }
    }

    public void mouseExited(MouseEvent e) {
      if (!isLocked()) {
        if (e.getX()>0 && splitPane.getDividerLocation()>0)
          splitPane.setDividerLocation(0);
      }
    }

  }


  /**
   * Add an internal frame to the desktop pane and make it visible.
   * @param frame internal frame
   */
  public static final void add(InternalFrame frame) {
    add(frame,false);
  }


  /**
   * Add an internal frame to the desktop pane and make it visible and maximazed.
   * @param frame internal frame
   */
  public static final void add(InternalFrame frame,boolean maximum) {
    if (frame.isUniqueInstance()) {

      if (MDIFrame.getInstance().isUniqueInstanceAlreadyOpened(frame)){
        for (int i = 0;i<desktopPane.getComponents().length;i++) {
         if (frame.getClass().equals(desktopPane.getComponent(i).getClass())) {
           try {
             ((InternalFrame)desktopPane.getComponent(i)).setSelected(true);
              return;
            }
            catch (Exception ex) {
            }
          }
        }
        return;
      }
      else
        MDIFrame.getInstance().uniqueInstances.add(frame.getClass());
    }

    ClientUtils.fireBusyEvent(true);
    try {
      desktopPane.add(frame, maximum);
      menuWindow.addWindow(frame);
      if (client.viewOpenedWindowIcons()) {
        winIconsPanel.add(frame);
      }
    }
    catch (Throwable ex) {
    }
    ClientUtils.fireBusyEvent(false);
  }


  /**
   * @return window menu
   */
  public static WindowMenu getWindowMenu() {
    return menuWindow;
  }


  /**
   * @return desktop pane dimension
   */
  public static Dimension getDesktopDimension() {
    return desktopPane.getSize();
  }


  /**
   * Method called when closing the MDI Frame
   */
  private void onWindowClosing() {
    Object[] opt = new Object[]{
        ClientSettings.getInstance().getResources().getResource("yes"),
        ClientSettings.getInstance().getResources().getResource("no")
    };
    if (JOptionPane.showOptionDialog(
        this,
        ClientSettings.getInstance().getResources().getResource("are you sure to quit application?"),
        ClientSettings.getInstance().getResources().getResource("quit application"),
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        opt,
        opt[0]
    )==0)  {

      JInternalFrame[] frames = desktopPane.getAllFrames();
      for (int i = 0; i < frames.length; i++) {
        if (frames[i] instanceof InternalFrame)
          try {
            ( (InternalFrame) frames[i]).closeFrame();
          }
          catch (Exception ex) {
          }
      }
      client.stopApplication();
    }
  }


  /**
   * Start the progress bar.
   * @param busy <code>true</code> to start progress bar, <code>false</code> to stop it
   */
  public final void setBusy(boolean busy) {
    if (client==null)
      return;

    synchronized(busyCount) {
      if (busyCount.intValue()==0 && busy) {
        statusBar.setBusy(true);
        // wait cursor...
        try {
          desktopPane.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
          desktopPane.getToolkit().sync();
          treeMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
          treeMenu.getToolkit().sync();
        } catch (Exception ex) {}
      }

      if (busy)
        busyCount = new Integer(busyCount.intValue()+1);
      else if (busyCount.intValue()>0)
        busyCount = new Integer(busyCount.intValue()-1);

      if (busyCount.intValue()==0) {
        statusBar.setBusy(false);
        // default cursor...
        try {
          desktopPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
          desktopPane.getToolkit().sync();
          treeMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
          treeMenu.getToolkit().sync();
        } catch (Exception ex) {}
      }
    }
  }


  /**
   * Callback invoked by WindowMenu when closing an internal frame.
   */
  public final void windowClosed(JInternalFrame frame) {
    if (frame instanceof InternalFrame && ((InternalFrame)frame).isUniqueInstance()) {
      uniqueInstances.remove(frame.getClass());
    }

    stopProgressBar();
  }


  /**
   * Callback invoked by windowClosed method.
   */
  public final void stopProgressBar() {
    synchronized(busyCount) {
      busyCount = new Integer(1);
    }
    setBusy(false);
  }



  /**
   * Set progress bar value. Minimum value is 0 and maximum value is 15.
   * @param progressBarValue value to set for the progress bar; if specified value is less than 0 then 0 is setted; if specified value is greater than 15 then it is set to 15.
   */
  public final void setProgressBarValue(int progressBarValue) {
    statusBar.setProgressBarValue(progressBarValue);
  }


  /**
   * @return current value in progress bar; minimum value is 0 and maximum value is 15
   */
  public final int getProgressBarValue() {
    return statusBar.getProgressBarValue();
  }


  /**
   * @return this
   */
  public static MDIFrame getInstance() {
    Container c = desktopPane.getParent();
    while(c!=null && !(c instanceof JFrame))
      c = c.getParent();
    return (MDIFrame)c;
  }


  /**
   * @return client facade
   */
  public static ClientFacade getClientFacade() {
    return client.getClientFacade();
  }


  /**
   * @return internal frame currently selected
   */
  public static InternalFrame getSelectedFrame() {
    return (InternalFrame)desktopPane.getSelectedFrame();
  }


  /**
   * Set status bar message.
   * @param text message to view into the status bar
   */
  public static final void setStatusBar(String text) {
    statusBar.setText(text);
  }


  /**
   * @return StatusBar object
   */
  public static final StatusBar getStatusBar() {
    return statusBar;
  }


  /**
   * Add a new component to the status bar, from left to right.
   * The component just added can be identitied by its "name" property (if specified).
   * @param c component to add
   */
  public static final void addStatusComponent(JComponent c) {
    addStatusComponent(c.getName(),c);
  }


  /**
   * Add a new component to the status bar, from left to right and identify it with the specified component name.
   * @param name component name, used to identify it
   * @param c component to add
   */
  public static final void addStatusComponent(String name,JComponent c) {
    if (name!=null)
      componentsHooks.put(name,c);
    statusBar.addStatusComponent(c);
  }


  /**
   * @param name component name, used to identify it
   * @return component identified by its name
   */
  public static final JComponent getStatusComponent(String name) {
    return (JComponent)componentsHooks.get(name);
  }


  /**
   * @return JMenu object related to "Help" menu in the menu bar. This hook can be used to add other menu items to "Help" menu; new menu items can be added within MDIFrameController.afterMDIcreation() method
   */
  public final JMenu getMenuHelp() {
    return menuHelp;
  }


  /**
   * @return JMenu object related to "File" menu in the menu bar. This hook can be used to add other menu items to "File" menu; new menu items can be added within MDIFrameController.afterMDIcreation() method
   */
  public final JMenu getMenuFile() {
    return menuFile;
  }


  /**
   * @param functionId menu item identifier
   * @return JMenuItem object, related to the specified functionId; null if there is not any JMenuItem having the specified functionId
   */
  public final JMenuItem getMenuItem(String functionId) {
    return (JMenuItem)functionsHooks.get(functionId);
  }


  /**
   * Add a separator between the top menu item identifier and bottom menu item identifier, ONLY IF they are both visible.
   * @param topFunctionId menu item identifier that is before the separator to add
   * @param bottomFunctionId menu item identifier that is after the separator to add
   */
  public final void addSeparatorToMenuBar(String topFunctionId,String bottomFunctionId) {
    JMenuItem topMenuItem = getMenuItem(topFunctionId);
    JMenu topMenu = (JMenu)functionsMenuHooks.get(topFunctionId);
    JMenu bottomMenu = (JMenu)functionsMenuHooks.get(bottomFunctionId);

    if (topMenu!=null && bottomMenu!=null) {
      for(int i=0;i<topMenu.getMenuComponentCount();i++)
        if (topMenu.getMenuComponent(i).equals(topMenuItem)) {
          topMenu.insertSeparator(i + 1);
          break;
        }
    }
  }


  /**
   * Add a button to the toolbar (and show toolbar if not already done).
   * @param button button to add to the toolbar
   */
  public final void addButtonToToolBar(JButton button) {
    toolbar.add(button);
    if (!toolbarAdded) {
      toolbarAdded = true;
      contentPane.add(toolbar,BorderLayout.NORTH);
    }
  }


  /**
   * Add a button to the toolbar (and show toolbar if not already done).
   * @param imageName name of the image to show within the button
   * @param tooltipText tooltip text (it will be translated according to internationalization settings)
   * @return button just created
   */
  public final JButton addButtonToToolBar(String imageName,String tooltipText) {
    return addButtonToToolBar(ClientUtils.getImage(imageName),tooltipText);
  }


  /**
   * Add a button to the toolbar (and show toolbar if not already done).
   * @param image image to show within the button
   * @param tooltipText tooltip text (it will be translated according to internationalization settings)
   * @return button just created
   */
  public final JButton addButtonToToolBar(Image image,String tooltipText) {
    JButton button = new JButton(new ImageIcon(image));
    button.setToolTipText(ClientSettings.getInstance().getResources().getResource(tooltipText));
    toolbar.add(button);
    if (!toolbarAdded) {
      toolbarAdded = true;
      contentPane.add(toolbar,BorderLayout.NORTH);
    }
    return button;
  }


  /**
   * Add a button to the toolbar (and show toolbar if not already done).
   * @param imageName name of the image to show within the button
   * @param tooltipText tooltip text (it will be translated according to internationalization settings)
   * @param buttonText button text to show within the button (it will be translated according to internationalization settings)
   * @return button just created
   */
  public final JButton addButtonToToolBar(String imageName,String tooltipText,String buttonText) {
    return addButtonToToolBar(ClientUtils.getImage(imageName),tooltipText,buttonText);
  }


  /**
   * Add a button to the toolbar (and show toolbar if not already done).
   * @param image image to show within the button
   * @param tooltipText tooltip text (it will be translated according to internationalization settings)
   * @param buttonText button text to show within the button (it will be translated according to internationalization settings)
   * @return button just created
   */
  public final JButton addButtonToToolBar(Image image,String tooltipText,String buttonText) {
    JButton button = new JButton(
      ClientSettings.getInstance().getResources().getResource(buttonText),
      new ImageIcon(image)
    );
    button.setToolTipText(ClientSettings.getInstance().getResources().getResource(tooltipText));
    toolbar.add(button);
    if (!toolbarAdded) {
      toolbarAdded = true;
      contentPane.add(toolbar,BorderLayout.NORTH);
    }
    return button;
  }



  /**
   * Appends a separator of default size to the end of the tool bar.
   * The default size is determined by the current look and feel.
   */
  public final void addSeparatorToToolBar() {
    toolbar.addSeparator();
    if (!toolbarAdded) {
      toolbarAdded = true;
      contentPane.add(toolbar,BorderLayout.NORTH);
    }
  }


  /**
   * Appends a separator of a specified size to the end
   * of the tool bar.
   *
   * @param size the <code>Dimension</code> of the separator
   */
  public final void addSeparatorOnToolBar(Dimension dim) {
    toolbar.addSeparator(dim);
    if (!toolbarAdded) {
      toolbarAdded = true;
      contentPane.add(toolbar,BorderLayout.NORTH);
    }
  }



  /**
   * Sets the <code>borderPainted</code> property, which is
   * <code>true</code> if the border should be painted.
   * The default value for this property is <code>true</code>.
   * Some look and feels might not implement painted borders;
   * they will ignore this property.
   *
   * @param b if true, the border is painted
   * @see #isBorderPainted
   * @beaninfo
   * description: Does the tool bar paint its borders?
   *       bound: true
   *      expert: true
   */
  public final void setBorderPainterOnToolBar(boolean borderPainted) {
    toolbar.setBorderPainted(borderPainted);
  }


  /**
   * Sets the <code>floatable</code> property,
   * which must be <code>true</code> for the user to move the tool bar.
   * Typically, a floatable tool bar can be
   * dragged into a different position within the same container
   * or out into its own window.
   * The default value of this property is <code>true</code>.
   * Some look and feels might not implement floatable tool bars;
   * they will ignore this property.
   *
   * @param b if <code>true</code>, the tool bar can be moved;
   *          <code>false</code> otherwise
   * @see #isFloatable
   * @beaninfo
   * description: Can the tool bar be made to float by the user?
   *       bound: true
   *   preferred: true
   */
  public final void setFloatableOnToolBar(boolean floatable) {
    toolbar.setFloatable(floatable);
  }


  /**
   * Sets the orientation of the tool bar.  The orientation must have
   * either the value <code>HORIZONTAL</code> or <code>VERTICAL</code>.
   * If <code>orientation</code> is
   * an invalid value, an exception will be thrown.
   *
   * @param o  the new orientation -- either <code>HORIZONTAL</code> or
   *			</code>VERTICAL</code>
   * @exception IllegalArgumentException if orientation is neither
   *		<code>HORIZONTAL</code> nor <code>VERTICAL</code>
   * @see #getOrientation
   * @beaninfo
   * description: The current orientation of the tool bar
   *       bound: true
   *   preferred: true
   */
  public final void setOrientationOnToolBar(int orientation) {
    toolbar.setOrientation(orientation);
  }


  /**
   * Sets the rollover state of this toolbar. If the rollover state is true
   * then the border of the toolbar buttons will be drawn only when the
   * mouse pointer hovers over them. The default value of this property
   * is false.
   * <p>
   * The implementation of a look and feel may choose to ignore this
   * property.
   *
   * @param rollover true for rollover toolbar buttons; otherwise false
   * @since 1.4
   * @beaninfo
   *        bound: true
   *    preferred: true
   *    attribute: visualUpdate true
   *  description: Will draw rollover button borders in the toolbar.
   */
  public final void setRolloverOnToolBar(boolean rollover) {
    toolbar.setRollover(rollover);
  }


  /**
   * @return <code>true</code> if the specified instance of internal frame is already opened, <code>false</code> otherwise
   */
  public final boolean isUniqueInstanceAlreadyOpened(InternalFrame frame) {
    return uniqueInstances.contains(frame.getClass());
  }


}

