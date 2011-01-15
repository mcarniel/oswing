package org.openswing.swing.permissions.client;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.permissions.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.domains.java.Domain;
import javax.swing.border.Border;


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
public class LoginDialog extends JDialog implements ItemListener {

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

  /** text to show as title */
  private String title = null;

  /** text to show in login button */
  private String loginButtonText = null;

  /** text to show in cancel button */
  private String exitButtonText = null;

  /** text to show in login button */
  private char loginButtonMnemonic;

  /** text to show in exit button */
  private char exitButtonMnemonic;

  /** check box used to store account */
  JCheckBox storeAccountCheckBox = new JCheckBox();

  /** store account text label */
  private String storeAccount = null;

  /** appId used to identify the application: for each distinct appId it will be stored a specific account */
  private String appId;

  /** optional cipher that can be used to encode and decode the password field; if this argument is null then no password encoding/decoding task is performed */
  private CryptUtils cipher;

  /** combo box for language selection */
  private ComboBoxControl languagesComboBox = new ComboBoxControl();

  /** supported languages, i.e. collection of pairs <language id,language description>; may be null */
  private Properties supportedLanguageIds = null;

  /** current language identifier; may be null */
  private String currentLanguageIdentifier = null;

  /** text to show in username label */
  private String usernameTextLabel = null;

  /** text to show in password label */
  private String passwordTextLabel = null;

  /** window size; if not specified, the size is automatically setted by this class */
  private Dimension size = null;
  JPanel warnPanel = new JPanel();
  JLabel warnLabel = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  private String capsLockMessage = "Caps lock pressed";


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
   * No password encoding/decoding task is performed.
   * No multiple language is supported in this login dialog.
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
   * No password encoding/decoding task is performed.
   * No multiple language is supported in this login dialog.
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
    this(
        parentFrame,
        changeLogin,
        loginController,
        title,
        loginButtonText,
        loginButtonMnemonic,
        exitButtonText,
        exitButtonMnemonic,
        storeAccount,
        appId,
        null
    );
  }


  /**
   * Constructor: it shows a username + password fields.
   * A "store account" check box is showed only if "appId" and "storeAccount" arguments are not null.
   * No multiple language is supported in this login dialog.
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
   * @param cipher optional cipher that can be used to encode and decode the password field; if this argument is null then no password encoding/decoding task is performed
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
      String appId,
      CryptUtils cipher
    ) {
    this(
        parentFrame,
        changeLogin,
        loginController,
        title,
        loginButtonText,
        loginButtonMnemonic,
        exitButtonText,
        exitButtonMnemonic,
        storeAccount,
        appId,
        cipher,
        null,
        null
    );
  }


  /**
   * Constructor: it shows a username + password fields.
   * A "store account" check box is showed only if "appId" and "storeAccount" arguments are not null.
   * A combo-box for language selection is showed, if "supportedLanguageIds" argument is not null.
   * Username label is prefilled with "Username" text.
   * Password label is prefilled with "Password" text.
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
   * @param cipher optional cipher that can be used to encode and decode the password field; if this argument is null then no password encoding/decoding task is performed
   * @param supportedLanguageIds supported languages, i.e. collection of pairs <language id,language description>; may be null
   * @param currentLanguageIdentifier current language identifier; may be null
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
      String appId,
      CryptUtils cipher,
      Properties supportedLanguageIds,
      String currentLanguageIdentifier
    ) {
    this(
        parentFrame,
        changeLogin,
        loginController,
        title,
        loginButtonText,
        loginButtonMnemonic,
        exitButtonText,
        exitButtonMnemonic,
        storeAccount,
        appId,
        cipher,
        supportedLanguageIds,
        currentLanguageIdentifier,
        "Username",
        "Password"
    );
  }


  /**
   * Constructor: it shows a username + password fields.
   * A "store account" check box is showed only if "appId" and "storeAccount" arguments are not null.
   * A combo-box for language selection is showed, if "supportedLanguageIds" argument is not null.
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
   * @param cipher optional cipher that can be used to encode and decode the password field; if this argument is null then no password encoding/decoding task is performed
   * @param supportedLanguageIds supported languages, i.e. collection of pairs <language id,language description>; may be null
   * @param currentLanguageIdentifier current language identifier; may be null
   * @param usernameTextLabel text to show in username label
   * @param passwordTextLabel text to show in password label
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
      String appId,
      CryptUtils cipher,
      Properties supportedLanguageIds,
      String currentLanguageIdentifier,
      String usernameTextLabel,
      String passwordTextLabel
    ) {
  this(
      parentFrame,
      changeLogin,
      loginController,
      title,
      loginButtonText,
      loginButtonMnemonic,
      exitButtonText,
      exitButtonMnemonic,
      storeAccount,
      appId,
      cipher,
      supportedLanguageIds,
      currentLanguageIdentifier,
      usernameTextLabel,
      passwordTextLabel,
      null
    );
  }


  /**
   * Constructor: it shows a username + password fields.
   * A "store account" check box is showed only if "appId" and "storeAccount" arguments are not null.
   * A combo-box for language selection is showed, if "supportedLanguageIds" argument is not null.
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
   * @param cipher optional cipher that can be used to encode and decode the password field; if this argument is null then no password encoding/decoding task is performed
   * @param supportedLanguageIds supported languages, i.e. collection of pairs <language id,language description>; may be null
   * @param currentLanguageIdentifier current language identifier; may be null
   * @param usernameTextLabel text to show in username label
   * @param passwordTextLabel text to show in password label
   * @param size window size; if not specified, the size is automatically setted by this class
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
      String appId,
      CryptUtils cipher,
      Properties supportedLanguageIds,
      String currentLanguageIdentifier,
      String usernameTextLabel,
      String passwordTextLabel,
      Dimension size
    ) {
    super(parentFrame==null?new JFrame():parentFrame,title,true);
    this.parentFrame = parentFrame;

    this.title = title;
    this.loginButtonText = loginButtonText;
    this.loginButtonMnemonic = loginButtonMnemonic;
    this.exitButtonText = exitButtonText;
    this.exitButtonMnemonic = exitButtonMnemonic;
    this.storeAccount = storeAccount;
    this.appId = appId;
    this.cipher = cipher;
    this.supportedLanguageIds = supportedLanguageIds;
    this.currentLanguageIdentifier = currentLanguageIdentifier;
    this.usernameTextLabel = usernameTextLabel;
    this.passwordTextLabel = passwordTextLabel;

    int width;
    int height;
    int halfWidth;
    int halfHeight;
    int lineHeight;
    if (size!=null) {
      width = size.width;
      height = size.height;
      halfWidth = size.width/2;
      halfHeight = size.width/2;
      lineHeight =
          languagesComboBox.getFontMetrics(languagesComboBox.getFont()).getHeight()+
          languagesComboBox.getFontMetrics(languagesComboBox.getFont()).getAscent()+
          languagesComboBox.getFontMetrics(languagesComboBox.getFont()).getDescent();
    }
    else if (Toolkit.getDefaultToolkit().getScreenResolution() == 96) {
        width = 380;
        height = 190;
        halfWidth = 190;
        halfHeight = 95;
        lineHeight = 20;
    } else {
        width = 476;
        height = 240;
        halfWidth = 238;
        halfHeight = 120;
        lineHeight = 25;
    }

    Dimension dim = new Dimension(
        (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-halfWidth,
        (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-halfHeight
    );

    if (parentFrame==null) {
      super.getParent().setVisible(true);
      super.getParent().setLocation(dim.width,dim.height);
    }
    this.changeLogin = changeLogin;
    this.loginController = loginController;
    try {
      jbInit();

      setSize(width,height+(appId!=null && storeAccount!=null?lineHeight:0)+(supportedLanguageIds!=null?lineHeight:0));
      setLocation(dim.width,dim.height);

      if (storeAccount!=null && appId!=null)
        loadAccount();

      try {
        if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK)) {
          usernameTF.setToolTipText(capsLockMessage);
          passwdTF.setToolTipText(capsLockMessage);
          warnLabel.setText(capsLockMessage);
        }
        KeyAdapter ka = new KeyAdapter() {

          public void keyPressed(KeyEvent e) {
            if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK)) {
              usernameTF.setToolTipText(capsLockMessage);
              passwdTF.setToolTipText(capsLockMessage);
              warnLabel.setText(capsLockMessage);
            }
            else {
              usernameTF.setToolTipText("");
              passwdTF.setToolTipText("");
              warnLabel.setText(" ");
            }
          }

        };
        this.addKeyListener(ka);
        usernameTF.addKeyListener(ka);
        passwdTF.addKeyListener(ka);
      }
      catch (Throwable t) {
      }

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
      if (cipher==null) {
        // no cipher is used for password...
        File f = new File(System.getProperty("user.home")+"/"+appId+".acc");
        Properties p = new Properties();
        FileInputStream in = new FileInputStream(f);
        p.load(in);
        in.close();
        usernameTF.setText(p.getProperty("username"));
        passwdTF.setText(p.getProperty("password"));
        storeAccountCheckBox.setSelected(true);
      }
      else {
        // a cipher is used to decode password...
        File f = new File(System.getProperty("user.home")+"/"+appId+".acc");
        FileInputStream in = new FileInputStream(f);
        byte[] bytes = new byte[(int)f.length()];
        in.read(bytes);
        in.close();

        int i=0;
        for(i=0;i<bytes.length;i++)
          if (bytes[i]=='\n')
            break;
        if (bytes[i]=='\n') {
          byte[] username = new byte[i];
          byte[] password = new byte[bytes.length-i-1];
          System.arraycopy(bytes,0,username,0,username.length);
          System.arraycopy(bytes,i+1,password,0,password.length);
          usernameTF.setText(new String(username));
          passwdTF.setText(cipher.decodeText(password));
          storeAccountCheckBox.setSelected(true);
        }

      }

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

        if (cipher==null) {
          // no cipher is used for password...
          Properties p = new Properties();
          p.setProperty("username",usernameTF.getText());
          p.setProperty("password",passwdTF.getText());
          FileOutputStream out = new FileOutputStream(f);
          p.store(out,"Stored account for "+appId);
          out.close();
        }
        else {
          // a cipher is used to encode password...
          byte[] username = usernameTF.getText().getBytes();
          byte[] password = cipher.encodeText(passwdTF.getText());
          FileOutputStream out = new FileOutputStream(f);
          byte[] bytes = new byte[(int)f.length()];
          out.write(username);
          out.write('\n');
          out.write(password);
          out.close();
        }
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
    usernameLabel.setText(usernameTextLabel);
    warnPanel.setPreferredSize(new Dimension(100,usernameTF.getPreferredSize().height));
    warnPanel.setMinimumSize(new Dimension(100,usernameTF.getPreferredSize().height));
    passwdLabel.setText(passwordTextLabel);
    usernameTF.setColumns(15);
    usernameTF.setMinimumSize(new Dimension(usernameTF.getFontMetrics(usernameTF.getFont()).stringWidth("               "),usernameTF.getHeight()));
    passwdTF.setColumns(15);
    passwdTF.setMinimumSize(new Dimension(passwdTF.getFontMetrics(passwdTF.getFont()).stringWidth("               "),passwdTF.getHeight()));
    usernameTF.addActionListener(new LoginDialog_usernameTF_actionAdapter(this));
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
    warnLabel.setText(" ");
    warnPanel.setLayout(borderLayout1);
    warnPanel.setBorder(BorderFactory.createLoweredBevelBorder());
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
      controlsPanel.add(storeAccountCheckBox, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
              ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.getContentPane().add(buttonsPanel,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(loginButton, null);
    buttonsPanel.add(exitButton, null);
    this.getContentPane().add(warnPanel,     new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    warnPanel.add(warnLabel, BorderLayout.CENTER);

    if (supportedLanguageIds!=null) {
      Domain domain = new Domain("LanguageSelectionInLoginDialogDomain");
      Enumeration en = supportedLanguageIds.propertyNames();
      String langId = null;
      while(en.hasMoreElements()) {
        langId = en.nextElement().toString();
        domain.addDomainPair(
          langId,
          supportedLanguageIds.getProperty(langId)
        );
      }
      languagesComboBox.setDomain(domain);
      if (currentLanguageIdentifier!=null) {
        languagesComboBox.setValue(currentLanguageIdentifier);
        updateLanguage(currentLanguageIdentifier);
      }
      languagesComboBox.addItemListener(this);

      controlsPanel.add(languagesComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    }

  }


  private void updateLanguage(String lang) {
    ClientSettings.getInstance().setLanguage(lang);
    setTitle(ClientSettings.getInstance().getResources().getResource(title));
    loginButton.setText(ClientSettings.getInstance().getResources().getResource(loginButtonText));
    usernameLabel.setText(ClientSettings.getInstance().getResources().getResource(usernameTextLabel));
    passwdLabel.setText(ClientSettings.getInstance().getResources().getResource(passwordTextLabel));
    storeAccountCheckBox.setText(ClientSettings.getInstance().getResources().getResource(storeAccount));
    try {
      loginButton.setMnemonic(ClientSettings.getInstance().getResources().getResource(String.valueOf(loginButtonMnemonic)).charAt(0));
    }
    catch (Exception ex) {
    }
    exitButton.setText(ClientSettings.getInstance().getResources().getResource(exitButtonText));
    try {
      exitButton.setMnemonic(ClientSettings.getInstance().getResources().getResource(String.valueOf(exitButtonMnemonic)).charAt(0));
    }
    catch (Exception ex1) {
    }
    capsLockMessage = ClientSettings.getInstance().getResources().getResource("Caps lock pressed");
    try {
      if (java.awt.Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK)) {
        usernameTF.setToolTipText(capsLockMessage);
        passwdTF.setToolTipText(capsLockMessage);
        warnLabel.setText(capsLockMessage);
      }
      else {
        usernameTF.setToolTipText("");
        passwdTF.setToolTipText("");
        warnLabel.setText(" ");
      }
    }
    catch (Throwable t) {
    }


  }


  /**
   * Method invoked when changing language from combo box.
   */
  public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange()==e.SELECTED && languagesComboBox.getValue()!=null) {
      updateLanguage((String)languagesComboBox.getValue());
    }
  }




  void loginButton_actionPerformed(ActionEvent e) {
    boolean ok = false;
    HashMap map = new HashMap();
    try {
      map.put("username",usernameTF.getText());
      if (cipher==null)
        map.put("password",passwdTF.getText());
      else
        map.put("password",cipher.encodeText(passwdTF.getText()));

      if (! (ok = loginController.authenticateUser(map)) &&
          attempts < loginController.getMaxAttempts()) {
        attempts++;
        passwdTF.requestFocus();
        return;
      }
    }
    catch (Throwable ex) {
      if (attempts < loginController.getMaxAttempts()) {
        OptionPane.showMessageDialog(
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


  void usernameTF_actionPerformed(ActionEvent e) {
    loginButton_actionPerformed(null);
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

class LoginDialog_usernameTF_actionAdapter implements java.awt.event.ActionListener {
  LoginDialog adaptee;

  LoginDialog_usernameTF_actionAdapter(LoginDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.usernameTF_actionPerformed(e);
  }
}
