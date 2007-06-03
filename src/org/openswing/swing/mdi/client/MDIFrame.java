package org.openswing.swing.mdi.client;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.*;
import java.lang.reflect.*;


import org.openswing.swing.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.client.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.permissions.client.*;
import org.openswing.swing.mdi.java.ApplicationFunction;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.openswing.swing.logger.client.Logger;
import java.util.HashSet;
import org.openswing.swing.internationalization.java.*;


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
//  private JScrollPane scrollPane = new JScrollPane(desktopPane);
  private static TreeMenu treeMenu = null;
  private JSplitPane splitPane = new JSplitPane();
  private BasicSplitPaneUI splitPaneUI = new BasicSplitPaneUI();

  /** setBusy calls number */
  private static int busyCount = 0;

  /** interface used to manage some MDI Frame settings */
  private static MDIController client = null;

  /** panel at the bottom, containing opened window icons (optional) and status panel */
  private JPanel bottomPanel = new JPanel();

  /** panel containing opened window icons (optional) */
  private static WinIconsPanel winIconsPanel = new WinIconsPanel();

  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  /** last tree menu width */
  private int lastTreeMenuWidth = 0;


  static {
    try {
      UIManager.setLookAndFeel(ClientSettings.LOOK_AND_FEEL_CLASS_NAME);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Contructor.
   * @param client interface used to manage some MDI Frame settings
   */
  public MDIFrame(MDIController client) {
    super(client.getMDIFrameTitle());
    try {
      this.client = client;
      ClientUtils.addBusyListener(this);
      statusBar.removeAll();
      menuWindow.init();
      menuWindow.menuWindowCloseAll_actionPerformed(null);
      enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      desktopPane.init();
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

      client.afterMDIcreation(this);

      //view MDI...
      try {
        Thread.sleep(500);
      }
      catch (Exception ex1) {
      }
      setVisible(true);
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

    menuFileChangeUser.setText(ClientSettings.getInstance().getResources().getResource("change user"));
    menuFileChangeUser.setMnemonic(ClientSettings.getInstance().getResources().getResource("changeusermnemonic").charAt(0));
    menuFileChangeUser.setAccelerator(KeyStroke.getKeyStroke('U',Event.CTRL_MASK));
    menuFileChangeUser.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        menuFileChangeUser_actionPerformed(e);
      }
    });

    menuFileChangeLanguage.setText(ClientSettings.getInstance().getResources().getResource("change language"));
    menuFileChangeLanguage.setMnemonic(ClientSettings.getInstance().getResources().getResource("changelanguagemnemonic").charAt(0));
    menuFileChangeLanguage.setAccelerator(KeyStroke.getKeyStroke('L',Event.CTRL_MASK));
    menuFileChangeLanguage.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        menuFileChangeLanguage_actionPerformed(e);
      }
    });


    menuHelp.setText(ClientSettings.getInstance().getResources().getResource("help"));
    menuHelp.setMnemonic(ClientSettings.getInstance().getResources().getResource("helpmnemonic").charAt(0));

    menuHelpAbout.setText(ClientSettings.getInstance().getResources().getResource("about")+" "+client.getMDIFrameTitle());
    menuHelpAbout.setMnemonic(ClientSettings.getInstance().getResources().getResource("aboutmnemonic").charAt(0));
    menuHelpAbout.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
        menuHelpAbout_actionPerformed(e);
      }
    });
    if (client.viewLoginInMenuBar())
      menuFile.add(menuFileChangeUser);
    if (client.viewChangeLanguageInMenuBar() && client.getLanguages().size()>1)
      menuFile.add(menuFileChangeLanguage);
    menuFile.addSeparator();
    menuFile.add(menuFileExit);
    menuHelp.add(menuHelpAbout);
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

          if (!function.isFolder()) {
            menu = new JMenu(function.toString());
            menu.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                executeFunction(function);
              }
            });
          } else {
            menu = new JMenu(function.toString());
          }
          int j=0;
          try {
            while (j < function.toString().length() &&
                   mnem.contains(function.toString().substring(j, j + 1))) {
              j++;
            }
            if (j < function.toString().length()) {
              mnem.add(function.toString().substring(j, j + 1));
              menu.setMnemonic(function.toString().substring(j, j + 1).charAt(0));
            }
          }
          catch (Exception ex1) {
          }

          menuBar.add(menu);
          addSubFunctionsToMenuBar(menu, function);
        }
      }
    }
    catch (Exception ex) {
      Logger.error(this.getClass().getName(),"jbInit","Error while constructing the functions menu",ex);
    }

    menuBar.add(menuWindow);
    menuBar.add(menuHelp);
    this.setJMenuBar(menuBar);

    JFrame.setDefaultLookAndFeelDecorated(true);
    GraphicsEnvironment env =
      GraphicsEnvironment.getLocalGraphicsEnvironment();
    this.setExtendedState(this.getExtendedState() | client.getExtendedState());
    desktopPane.setPreferredSize(new Dimension(this.getWidth()-ClientSettings.DIVIDER_WIDTH-6-ClientSettings.MENU_WIDTH,this.getHeight()-menuBar.getHeight()-statusBar.getHeight()-103));

    if (client.viewFunctionsInTreePanel() && ((TreeNode)functions.getRoot()).getChildCount()>0) {
      // add tree nodes...
      treeMenu = new TreeMenu(functions);
      treeMenu.setMinimumSize(new Dimension(0,0));
      treeMenu.setMaximumSize(new Dimension(ClientSettings.MAX_MENU_WIDTH,0));
      treeMenu.addMouseListener(new MenuMouseListener());
      lastTreeMenuWidth = treeMenu.getWidth();
      splitPane.setLeftComponent(treeMenu);
//      splitPane.setRightComponent(scrollPane);
      splitPane.setRightComponent(desktopPane);
      resetSplitPane();
      contentPane.add(splitPane, BorderLayout.CENTER);

      // add key listener to intercept an F3 key pressed event...
      ApplicationEventQueue.getInstance().addKeyListener(new KeyAdapter() {

        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode()==KeyEvent.VK_F3) {
            // F3 key pressed: focus will be setted on this component...
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
//      contentPane.add(scrollPane, BorderLayout.CENTER);
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




  /**
   * Add application functions to the menu bar.
   * @param parentMenu menu item just added
   * @param parentFunction application function to expand, organized as a tree
   */
  private void addSubFunctionsToMenuBar(JMenuItem parentMenu,ApplicationFunction parentFunction) {
    JMenuItem menu = null;
    for (int i = 0; i < parentFunction.getChildCount(); i++) {
      final ApplicationFunction function = (ApplicationFunction) parentFunction.getChildAt(i);

      if (!function.isFolder()) {
        if (function.getIconName()==null)
          menu = new JMenuItem(function.toString());
        else
          menu = new JMenuItem(function.toString(),new ImageIcon(ClientUtils.getImage(function.getIconName())));
        menu.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            executeFunction(function);
          }
        });
      } else {
        menu = new JMenu(function.toString());
      }

      parentMenu.add(menu);
      addSubFunctionsToMenuBar(menu, function);
    }
  }


  /**
   * Method called by addSubFunctionsToMenuBar method: execute the corrisponding method in the ClientFacade.
   */
  private void executeFunction(final ApplicationFunction node) {
    ClientUtils.fireBusyEvent(true);
    new Thread() {
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

          JOptionPane.showMessageDialog(
              MDIFrame.this,
              ClientSettings.getInstance().getResources().getResource("Error while executing function")+" '"+node.getMethodName()+"'",
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.WARNING_MESSAGE
          );
        }
      }
    }.start();
  }



  /**
   * @return <code>true</code> if menu window is locked <code>false</code> otherwise
   */
  public final boolean isLocked() {
    return treeMenu.isLocked();
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
    splitPane.setDividerLocation(250);
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
    ClientUtils.fireBusyEvent(true);
    desktopPane.add(frame,maximum);
    menuWindow.addWindow(frame);
    if (client.viewOpenedWindowIcons())
      winIconsPanel.add(frame);
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
    if (JOptionPane.showConfirmDialog(
        this,
        ClientSettings.getInstance().getResources().getResource("are you sure to quit application?"),
        ClientSettings.getInstance().getResources().getResource("quit application"),
        JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)  {
      client.stopApplication();
    }
  }


  /**
   * Start the progress bar.
   * @param busy <code>true</code> to start progress bar, <code>false</code> to stop it
   */
  public void setBusy(boolean busy) {
    if (client==null)
      return;
    if (busyCount==0 && busy) {
      statusBar.setBusy(true);
      // wait cursor...
      try {
        desktopPane.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        desktopPane.getToolkit().sync();
        treeMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        treeMenu.getToolkit().sync();
      } catch (Exception ex) {}
    } if (busy)
      busyCount = busyCount+1;
    else
      busyCount = busyCount-1;
    if (busyCount==0) {
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


  /**
   * @return this
   */
  public static MDIFrame getInstance() {
    Container c = desktopPane.getParent();
    while(!(c instanceof JFrame))
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
   * Add a new component to the status bar, from left to right.
   * @param c component to add
   */
  public static final void addStatusComponent(JComponent c) {
    statusBar.addStatusComponent(c);
  }






}

