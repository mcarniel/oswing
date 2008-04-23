package org.openswing.swing.permissions.client;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Dialog window used to logon the application.
 * This window is not translated: it's always in english.</p>
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
public class LoginDialog extends JDialog {

  JPanel mainPanel = new JPanel();
  JPanel controlsPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JLabel usernameLabel = new JLabel();
  JLabel passwdLabel = new JLabel();
  JTextField usernameTF = new JTextField();
  JPasswordField passwdTF = new JPasswordField();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JPanel buttonsPanel = new JPanel();
  JButton exitButton = new JButton();
  JButton loginButton = new JButton();

  /** login controller */
  private LoginController loginController = null;

  /** number of faild login attempts */
  private int attempts = 1;

  /** flag used to indicate that the login dialog is opened inside the application: if user will click on "Exit" button then the application will not be closed */
  private boolean changeLogin;

  /** parent frame; may be null */
  private JFrame parentFrame = null;

  /** flag used in windowClosed method */
  private boolean fromOtherMethod = false;

  /** text to show in login button */
  private String loginButtonText = null;

  /** text to show in cancel button */
  private String exitButtonText = null;

  /** text to show in login button */
  private char loginButtonMnemonic;

  /** text to show in exit button */
  private char exitButtonMnemonic;
  JCheckBox storeAccountCheckBox = new JCheckBox();

  /** store account text label */
  private String storeAccount = null;

  /** appId used to identify the application: for each distinct appId it will be stored a specific account */
  private String appId;


  static {
    try {
      UIManager.setLookAndFeel(ClientSettings.LOOK_AND_FEEL_CLASS_NAME);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Constructor: it shows a username + password fields.
   * @param parentFrame parent frame to use as parent of dialog window; could be set to null
   * @param changeLogin flag used to indicate that the login dialog is opened inside the application: if user will click on "Exit" button then the application will not be closed
   * @param loginController login controller
   */
  public LoginDialog(JFrame parentFrame, boolean changeLogin,
                     LoginController loginController) {
    this(parentFrame, changeLogin, loginController, "Logon", "Login", 'L',
         "Exit", 'E', null, null);
  }


  /**
   * Constructor: it shows a username + password fields.
   * A "store account" check box is showed only if "appId" and "storeAccount" arguments are not null.
   * @param parentFrame parent frame to use as parent of dialog window; could be set to null
   * @param changeLogin flag used to indicate that the login dialog is opened inside the application: if user will click on "Exit" button then the application will not be closed
   * @param loginController login controller
   * @param title window title
   * @param loginButtonText text to show in login button
   * @param loginButtonMnemonic text to show in login button
   * @param cancelButtonText text to show in exit button
   * @param cancelButtonMnemonic text to show in exit button
   * @param storeAccount store account text label
   * @param appId used to identify the application: for each distinct appId it will be stored a specific account
   */
  public LoginDialog(
      JFrame parentFrame,
      boolean changeLogin,
      LoginController loginController,
      String title,
      String loginButtonText,
      char loginButtonMnemonic,
      String exitButtonText,
      char exitButtonMnemonic,
      String storeAccount,
      String appId
    ) {
    super(parentFrame==null?new JFrame():parentFrame,title,true);
    this.parentFrame = parentFrame;

    this.loginButtonText = loginButtonText;
    this.loginButtonMnemonic = loginButtonMnemonic;
    this.exitButtonText = exitButtonText;
    this.exitButtonMnemonic = exitButtonMnemonic;
    this.storeAccount = storeAccount;
    this.appId = appId;

    Dimension dim = new Dimension(
        (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-190,
        (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-90
    );
    if (parentFrame==null) {
      super.getParent().setVisible(true);
      super.getParent().setLocation(dim.width,dim.height);
    }
    this.changeLogin = changeLogin;
    this.loginController = loginController;
    try {
      jbInit();
      setSize(380,180+(appId!=null && storeAccount!=null?20:0));
      setLocation(dim.width,dim.height);

      if (storeAccount!=null && appId!=null)
        loadAccount();

      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Load stored account, if there exists anyone.
   */
  private void loadAccount() {
    try {
      File f = new File(System.getProperty("user.home")+"/"+appId+".acc");
      Properties p = new Properties();
      FileInputStream in = new FileInputStream(f);
      p.load(in);
      in.close();
      usernameTF.setText(p.getProperty("username"));
      passwdTF.setText(p.getProperty("password"));
      storeAccountCheckBox.setSelected(true);
    }
    catch (Throwable ex) {
    }
  }


  /**
   * Save account, if "store account" check box has been selected.
   */
  private void saveAccount() {
    try {
      File f = new File(System.getProperty("user.home")+"/"+appId+".acc");
      if (storeAccountCheckBox.isSelected()) {
        Properties p = new Properties();
        p.setProperty("username",usernameTF.getText());
        p.setProperty("password",passwdTF.getText());
        FileOutputStream out = new FileOutputStream(f);
        p.store(out,"Stored account for "+appId);
        out.close();
      }
      else
        f.delete();
    }
    catch (Throwable ex) {
    }
  }


  private void jbInit() throws Exception {
    mainPanel.setLayout(gridBagLayout1);
    this.getContentPane().setLayout(gridBagLayout2);
    usernameLabel.setText("Username");
    passwdLabel.setText("Password");
    usernameTF.setColumns(15);
    usernameTF.setMinimumSize(new Dimension(usernameTF.getFontMetrics(usernameTF.getFont()).stringWidth("               "),usernameTF.getHeight()));
    passwdTF.setColumns(15);
    passwdTF.setMinimumSize(new Dimension(passwdTF.getFontMetrics(passwdTF.getFont()).stringWidth("               "),passwdTF.getHeight()));
    passwdTF.addActionListener(new LoginDialog_passwdTF_actionAdapter(this));
    mainPanel.setBorder(BorderFactory.createEtchedBorder());
    exitButton.setMnemonic(exitButtonMnemonic);
    exitButton.setText(exitButtonText);
    exitButton.addActionListener(new LoginDialog_exitButton_actionAdapter(this));
    loginButton.setMnemonic(loginButtonMnemonic);
    loginButton.setText(loginButtonText);
    loginButton.addActionListener(new LoginDialog_loginButton_actionAdapter(this));
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.addWindowListener(new LoginDialog_this_windowAdapter(this));
    storeAccountCheckBox.setText(storeAccount);
    getContentPane().add(mainPanel,          new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    controlsPanel.setLayout(gridBagLayout3);
    mainPanel.add(controlsPanel,            new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(5, 5, 0, 5), 0, 0));
    controlsPanel.add(usernameLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    controlsPanel.add(passwdLabel,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    controlsPanel.add(usernameTF,       new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    controlsPanel.add(passwdTF,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
    if (appId!=null && storeAccount!=null)
      controlsPanel.add(storeAccountCheckBox, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(buttonsPanel,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(loginButton, null);
    buttonsPanel.add(exitButton, null);
  }


  void loginButton_actionPerformed(ActionEvent e) {
    boolean ok = false;
    HashMap map = new HashMap();
    map.put("username",usernameTF.getText());
    map.put("password",passwdTF.getText());

    try {
      if (! (ok = loginController.authenticateUser(map)) &&
          attempts < loginController.getMaxAttempts()) {
        attempts++;
        passwdTF.requestFocus();
        return;
      }
    }
    catch (Exception ex) {
      if (attempts < loginController.getMaxAttempts()) {
        JOptionPane.showMessageDialog(
          parentFrame,
          ex.getMessage(),
          "Error",
          JOptionPane.ERROR_MESSAGE
        );
        attempts++;
        passwdTF.requestFocus();
        return;
      }
      else
        ok = false;
    }

    if (!ok) {
      // max number of failed attempts reached: the application will be closed
      exitButton_actionPerformed(null);
      return;
    }
    else if (parentFrame==null) {
        this.getParent().setVisible(false);
        ((JFrame)this.getParent()).dispose();
    }
    else
      parentFrame.setVisible(false);

    // logon ok...
    fromOtherMethod = true;
    setVisible(false);

    if (storeAccount!=null && appId!=null)
      saveAccount();

    loginController.loginSuccessful(map);
  }


  void exitButton_actionPerformed(ActionEvent e) {
    fromOtherMethod = true;
    setVisible(false);
    if (!changeLogin) {
      if (parentFrame==null) {
        this.getParent().setVisible(false);
        ((JFrame)this.getParent()).dispose();
      }
      loginController.stopApplication();
    }
  }


  void passwdTF_actionPerformed(ActionEvent e) {
    loginButton_actionPerformed(null);
  }

  void this_windowClosed(WindowEvent e) {
    if (!fromOtherMethod && !changeLogin) {
      if (parentFrame==null) {
        this.getParent().setVisible(false);
        ((JFrame)this.getParent()).dispose();
      }
      loginController.stopApplication();
    }
  }

}

class LoginDialog_loginButton_actionAdapter implements java.awt.event.ActionListener {
  LoginDialog adaptee;

  LoginDialog_loginButton_actionAdapter(LoginDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.loginButton_actionPerformed(e);
  }
}

class LoginDialog_exitButton_actionAdapter implements java.awt.event.ActionListener {
  LoginDialog adaptee;

  LoginDialog_exitButton_actionAdapter(LoginDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.exitButton_actionPerformed(e);
  }
}

class LoginDialog_passwdTF_actionAdapter implements java.awt.event.ActionListener {
  LoginDialog adaptee;

  LoginDialog_passwdTF_actionAdapter(LoginDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.passwdTF_actionPerformed(e);
  }
}

class LoginDialog_this_windowAdapter extends java.awt.event.WindowAdapter {
  LoginDialog adaptee;

  LoginDialog_this_windowAdapter(LoginDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void windowClosed(WindowEvent e) {
    adaptee.this_windowClosed(e);
  }
}
