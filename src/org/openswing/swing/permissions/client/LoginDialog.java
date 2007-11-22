package org.openswing.swing.permissions.client;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.client.ClientSettings;


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
  GridBagLayout gridBagLayout1 = new GridBagLayout();
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


  static {
    try {
      UIManager.setLookAndFeel(ClientSettings.LOOK_AND_FEEL_CLASS_NAME);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }



  /**
   * Constructor.
   * @param parentFrame parent frame to use as parent of dialog window; could be set to null
   * @param changeLogin flag used to indicate that the login dialog is opened inside the application: if user will click on "Exit" button then the application will not be closed
   * @param loginController login controller
   */
  public LoginDialog(JFrame parentFrame,boolean changeLogin,LoginController loginController) {
    super(parentFrame==null?new JFrame():parentFrame,"Logon",true);
    this.parentFrame = parentFrame;
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
      setSize(380,180);
      setLocation(dim.width,dim.height);
      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
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
    exitButton.setMnemonic('E');
    exitButton.setText("Exit");
    exitButton.addActionListener(new LoginDialog_exitButton_actionAdapter(this));
    loginButton.setMnemonic('L');
    loginButton.setText("Login");
    loginButton.addActionListener(new LoginDialog_loginButton_actionAdapter(this));
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.addWindowListener(new LoginDialog_this_windowAdapter(this));
    getContentPane().add(mainPanel,        new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(usernameLabel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(passwdLabel,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(usernameTF,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(passwdTF,    new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
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
