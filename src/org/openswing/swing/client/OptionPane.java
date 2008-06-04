package org.openswing.swing.client;


import java.beans.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Singleton class that contains a set of static methods for viewing Dialog objects.
 * Based on JOptionPane: this class translates title, text and buttons according to the current internationalization settings.</p>
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
public class OptionPane {


  /**
   * Brings up a dialog with a specified icon, where the number of
   * choices is determined by the <code>optionType</code> parameter.
   * The <code>messageType</code> parameter is primarily used to supply
   * a default icon from the look and feel.
   *
   * @param parentComponent determines the <code>Frame</code> in which the
   *			dialog is displayed; if <code>null</code>,
   *			or if the <code>parentComponent</code> has no
   *			<code>Frame</code>, a
   *			default <code>Frame</code> is used
   * @param message   the Object to display (not yet translated)
   * @param title     the title string for the dialog (not yet translated)
   * @param optionType an int designating the options available on the dialog:
   *                  <code>YES_NO_OPTION</code>,
   *			or <code>YES_NO_CANCEL_OPTION</code>
   * @param messageType an int designating the kind of message this is,
   *                  primarily used to determine the icon from the pluggable
   *                  Look and Feel: <code>ERROR_MESSAGE</code>,
   *			<code>INFORMATION_MESSAGE</code>,
   *                  <code>WARNING_MESSAGE</code>,
   *                  <code>QUESTION_MESSAGE</code>,
   *			or <code>PLAIN_MESSAGE</code>
   * @param imageName name of the file to use as the icon to display in the dialog (it must be stored in "images" subfolder)
   * @return an int indicating the option selected by the user
   * @exception HeadlessException if
   *   <code>GraphicsEnvironment.isHeadless</code> returns
   *   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static int showConfirmDialog(Component parentComponent,
      Object message, String title, int optionType,
      int messageType, String imageName) throws HeadlessException {
      return showOptionDialog(parentComponent, message, title, optionType,
                              messageType, imageName, null, null);
  }


  /**
   * Brings up a dialog where the number of choices is determined
   * by the <code>optionType</code> parameter, where the
   * <code>messageType</code>
   * parameter determines the icon to display.
   * The <code>messageType</code> parameter is primarily used to supply
   * a default icon from the Look and Feel.
   *
   * @param parentComponent determines the <code>Frame</code> in
   *			which the dialog is displayed; if <code>null</code>,
   *			or if the <code>parentComponent</code> has no
   *			<code>Frame</code>, a
   *                  default <code>Frame</code> is used.
   * @param message   the <code>Object</code> to display
   * @param title     the title string for the dialog (not yet translated)
   * @param optionType an integer designating the options available
   *			on the dialog: <code>YES_NO_OPTION</code>,
   *			or <code>YES_NO_CANCEL_OPTION</code>
   * @param messageType an integer designating the kind of message this is;
   *                  primarily used to determine the icon from the pluggable
   *                  Look and Feel: <code>ERROR_MESSAGE</code>,
   *			<code>INFORMATION_MESSAGE</code>,
   *                  <code>WARNING_MESSAGE</code>,
   *                  <code>QUESTION_MESSAGE</code>,
   *			or <code>PLAIN_MESSAGE</code>
   * @return an integer indicating the option selected by the user
   * @exception HeadlessException if
   *   <code>GraphicsEnvironment.isHeadless</code> returns
   *   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static int showConfirmDialog(Component parentComponent,
      Object message, String title, int optionType, int messageType)
      throws HeadlessException {
      return showConfirmDialog(parentComponent, message, title, optionType,
                              messageType, null);
  }


  /**
   * Brings up a dialog where the number of choices is determined
   * by the <code>optionType</code> parameter.
   *
   * @param parentComponent determines the <code>Frame</code> in which the
   *			dialog is displayed; if <code>null</code>,
   *			or if the <code>parentComponent</code> has no
   *			<code>Frame</code>, a
   *                  default <code>Frame</code> is used
   * @param message   the <code>Object</code> to display
   * @param title     the title string for the dialog (not yet translated)
   * @param optionType an int designating the options available on the dialog:
   *                  <code>YES_NO_OPTION</code>, or
   *			<code>YES_NO_CANCEL_OPTION</code>
   * @return an int indicating the option selected by the user
   * @exception HeadlessException if
   *   <code>GraphicsEnvironment.isHeadless</code> returns
   *   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static int showConfirmDialog(Component parentComponent,
      Object message, String title, int optionType)
      throws HeadlessException {
      return showConfirmDialog(parentComponent, message, title, optionType,
                               JOptionPane.QUESTION_MESSAGE);
  }


  /**
   * Prompts the user for input in a blocking dialog where the
   * initial selection, possible selections, and all other options can
   * be specified. The user will able to choose from
   * <code>selectionValues</code>, where <code>null</code> implies the
   * user can input
   * whatever they wish, usually by means of a <code>JTextField</code>.
   * <code>initialSelectionValue</code> is the initial value to prompt
   * the user with. It is up to the UI to decide how best to represent
   * the <code>selectionValues</code>, but usually a
   * <code>JComboBox</code>, <code>JList</code>, or
   * <code>JTextField</code> will be used.
   *
   * @param parentComponent  the parent <code>Component</code> for the
   *			dialog
   * @param message  the <code>Object</code> to display
   * @param title    the <code>String</code> to display in the
   *			dialog title bar
   * @param messageType the type of message to be displayed:
   *                  <code>ERROR_MESSAGE</code>,
   *			<code>INFORMATION_MESSAGE</code>,
   *			<code>WARNING_MESSAGE</code>,
   *                  <code>QUESTION_MESSAGE</code>,
   *			or <code>PLAIN_MESSAGE</code>
   * @param imageName name of the file to use as the icon to display in the dialog (it must be stored in "images" subfolder)
   * @param selectionValues an array of <code>Object</code>s that
   *			gives the possible selections
   * @param initialSelectionValue the value used to initialize the input
   *                 field
   * @return user's input, or <code>null</code> meaning the user
   *			canceled the input
   * @exception HeadlessException if
   *   <code>GraphicsEnvironment.isHeadless</code> returns
   *   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static Object showInputDialog(Component parentComponent,
      Object message, String title, int messageType, String imageName,
      Object[] selectionValues, Object initialSelectionValue)
      throws HeadlessException {
    if (message !=null && message instanceof String)
      message = ClientSettings.getInstance().getResources().getResource(message.toString());
    title = ClientSettings.getInstance().getResources().getResource(title);
    if (selectionValues!=null) {
      for(int i=0;i<selectionValues.length;i++)
        if (selectionValues[i]!=null && selectionValues[i] instanceof String)
          selectionValues[i] = ClientSettings.getInstance().getResources().getResource(selectionValues[i].toString());
    }
    if (initialSelectionValue !=null && initialSelectionValue instanceof String)
      initialSelectionValue = ClientSettings.getInstance().getResources().getResource(initialSelectionValue.toString());

//    else if (messageType==JOptionPane.QUESTION_MESSAGE) {
//      selectionValues = new String[]{
//              ClientSettings.getInstance().getResources().getResource("yes"),
//              ClientSettings.getInstance().getResources().getResource("no")
//      };
//    }

    return showInputDialog2(
      parentComponent,
      message,
      title,
      messageType,
      imageName==null?null:new ImageIcon(ClientUtils.getImage(imageName)),
      selectionValues,
      initialSelectionValue
    );

  }


  public static Object showInputDialog2(Component parentComponent,
      Object message, String title, int messageType, Icon icon,
      Object[] selectionValues, Object initialSelectionValue)
      throws HeadlessException {

      String[] options = new String[]{
          ClientSettings.getInstance().getResources().getResource("ok"),
          ClientSettings.getInstance().getResources().getResource("cancel")
      };

      final JOptionPane pane = new JOptionPane(message, messageType,
                                            JOptionPane.OK_CANCEL_OPTION, icon,
                                            options, null);
      pane.setWantsInput(true);
      pane.setSelectionValues(selectionValues);
      pane.setInitialSelectionValue(initialSelectionValue);
      int style = styleFromMessageType(messageType);

      final JDialog dialog;
      Window window = getWindowForComponent(parentComponent);
      if (window instanceof Frame) {
          dialog = new JDialog((Frame)window, title, true);
      } else {
          dialog = new JDialog((Dialog)window, title, true);
      }
      Container contentPane = dialog.getContentPane();
      contentPane.setLayout(new BorderLayout());

      contentPane.add(pane, BorderLayout.CENTER);
      dialog.setResizable(true);
      if (JDialog.isDefaultLookAndFeelDecorated()) {
          boolean supportsWindowDecorations =
          UIManager.getLookAndFeel().getSupportsWindowDecorations();
          if (supportsWindowDecorations) {
              dialog.setUndecorated(true);
              dialog.getRootPane().setWindowDecorationStyle(style);
          }
      }
      dialog.pack();
      dialog.setLocationRelativeTo(parentComponent);
      dialog.addWindowListener(new WindowAdapter() {
          private boolean gotFocus = false;
          public void windowClosing(WindowEvent we) {
              pane.setValue(null);
          }
          public void windowGainedFocus(WindowEvent we) {
              // Once window gets focus, set initial focus
              if (!gotFocus) {
                  pane.selectInitialValue();
                  gotFocus = true;
              }
          }
      });
      dialog.addComponentListener(new ComponentAdapter() {
          public void componentShown(ComponentEvent ce) {
              // reset value to ensure closing works properly
              pane.setValue(JOptionPane.UNINITIALIZED_VALUE);
          }
      });

      JPanel p = (JPanel)pane.getComponents()[1];
      JButton okB = (JButton)p.getComponent(0);
      JButton cancelB = (JButton)p.getComponent(1);
      okB.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          dialog.hide();
          dialog.dispose();
        }

      });
      cancelB.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          pane.setInitialValue(JOptionPane.UNINITIALIZED_VALUE);
          pane.setInputValue(JOptionPane.UNINITIALIZED_VALUE);
          pane.setInitialSelectionValue(JOptionPane.UNINITIALIZED_VALUE);
          dialog.hide();
          dialog.dispose();
        }

      });

      pane.selectInitialValue();
      dialog.show();
      dialog.dispose();

      Object value = pane.getInputValue();

      if (JOptionPane.UNINITIALIZED_VALUE.equals(value)) {
          return null;
      }
      return value;
  }


  static Window getWindowForComponent(Component parentComponent)
      throws HeadlessException {
      if (parentComponent == null)
          return null;
      if (parentComponent instanceof Frame || parentComponent instanceof Dialog)
          return (Window)parentComponent;
      return getWindowForComponent(parentComponent.getParent());
  }


  private static int styleFromMessageType(int messageType) {
      switch (messageType) {
      case JOptionPane.ERROR_MESSAGE:
          return JRootPane.ERROR_DIALOG;
      case JOptionPane.QUESTION_MESSAGE:
          return JRootPane.QUESTION_DIALOG;
      case JOptionPane.WARNING_MESSAGE:
          return JRootPane.WARNING_DIALOG;
      case JOptionPane.INFORMATION_MESSAGE:
          return JRootPane.INFORMATION_DIALOG;
      case JOptionPane.PLAIN_MESSAGE:
      default:
          return JRootPane.PLAIN_DIALOG;
      }
  }


  /**
   * Shows a dialog requesting input from the user parented to
   * <code>parentComponent</code> with the dialog having the title
   * <code>title</code> and message type <code>messageType</code>.
   *
   * @param parentComponent  the parent <code>Component</code> for the
   *			dialog
   * @param message  the <code>Object</code> to display
   * @param title    the <code>String</code> to display in the dialog
   *			title bar
   * @param messageType the type of message that is to be displayed:
   *                 	<code>ERROR_MESSAGE</code>,
   *			<code>INFORMATION_MESSAGE</code>,
   *			<code>WARNING_MESSAGE</code>,
   *                 	<code>QUESTION_MESSAGE</code>,
   *			or <code>PLAIN_MESSAGE</code>
   * @exception HeadlessException if
   *   <code>GraphicsEnvironment.isHeadless</code> returns
   *   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static String showInputDialog(Component parentComponent,
      Object message, String title, int messageType)
      throws HeadlessException {
      return (String)showInputDialog(parentComponent, message, title,
                                     messageType, null, null, null);
  }


  /**
   * Brings up an internal dialog panel with a specified icon, where
   * the number of choices is determined by the <code>optionType</code>
   * parameter.
   * The <code>messageType</code> parameter is primarily used to supply
   * a default icon from the look and feel.
   *
   * @param parentComponent determines the <code>Frame</code>
   *		in which the dialog is displayed; if <code>null</code>,
   *		or if the parentComponent has no Frame, a
   *          default <code>Frame</code> is used
   * @param message   the Object to display (not yet translated) in the dialog; a
   *		<code>Component</code> object is rendered as a
   *		<code>Component</code>; a <code>String</code>
   *		object is rendered as a string; other objects are
   *		converted to a <code>String</code> using the
   *		<code>toString</code> method
   * @param title     the title string for the dialog (not yet translated)
   * @param optionType an integer designating the options available
   *		on the dialog:
   *          <code>YES_NO_OPTION</code>, or
   *		<code>YES_NO_CANCEL_OPTION</code.
   * @param messageType an integer designating the kind of message this is,
   *		primarily used to determine the icon from the pluggable
   *		Look and Feel: <code>ERROR_MESSAGE</code>,
   *		<code>INFORMATION_MESSAGE</code>,
   *		<code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
   *		or <code>PLAIN_MESSAGE</code>
   * @param imageName name of the file to use as the icon to display in the dialog (it must be stored in "images" subfolder)
   * @return an integer indicating the option selected by the user
   */
  public static int showInternalConfirmDialog(Component parentComponent,
                                      Object message,
                                      String title, int optionType,
                                      int messageType, String imageName) {
      return showInternalOptionDialog(parentComponent, message, title, optionType,
                                      messageType, imageName, null, null);
  }


  /**
   * Brings up an internal dialog panel where the number of choices
   * is determined by the <code>optionType</code> parameter, where
   * the <code>messageType</code> parameter determines the icon to display.
   * The <code>messageType</code> parameter is primarily used to supply
   * a default icon from the Look and Feel.
   *
   * @param parentComponent determines the <code>Frame</code> in
   *		which the dialog is displayed; if <code>null</code>,
   *		or if the <code>parentComponent</code> has no
   *		<code>Frame</code>, a default <code>Frame</code> is used
   * @param message   the Object to display (not yet translated) in the dialog; a
   *		<code>Component</code> object is rendered as a
   *		<code>Component</code>; a <code>String</code>
   *		object is rendered as a string; other objects are
   *		converted to a <code>String</code> using the
   *		<code>toString</code> method
   * @param title     the title string for the dialog (not yet translated)
   * @param optionType an integer designating the options
   *		available on the dialog:
   *		<code>YES_NO_OPTION</code>, or <code>YES_NO_CANCEL_OPTION</code>
   * @param messageType an integer designating the kind of message this is,
   *          primarily used to determine the icon from the
   *		pluggable Look and Feel: <code>ERROR_MESSAGE</code>,
   *		<code>INFORMATION_MESSAGE</code>,
   *		<code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
   *		or <code>PLAIN_MESSAGE</code>
   * @return an integer indicating the option selected by the user
   */
  public static int showInternalConfirmDialog(Component parentComponent,
                                      Object message,
                                      String title, int optionType,
                                      int messageType) {
      return showInternalConfirmDialog(parentComponent, message, title, optionType,
                                       messageType, null);
  }


  /**
   * Brings up a internal dialog panel where the number of choices
   * is determined by the <code>optionType</code> parameter.
   *
   * @param parentComponent determines the <code>Frame</code>
   *		in which the dialog is displayed; if <code>null</code>,
   *		or if the <code>parentComponent</code> has no
   *		<code>Frame</code>, a default <code>Frame</code> is used
   * @param message   the Object to display (not yet translated) in the dialog; a
   *		<code>Component</code> object is rendered as a
   *		<code>Component</code>; a <code>String</code>
   *		object is rendered as a string; other objects
   *		are converted to a <code>String</code> using the
   *		<code>toString</code> method
   * @param title     the title string for the dialog (not yet translated)
   * @param optionType an integer designating the options
   *		available on the dialog: <code>YES_NO_OPTION</code>,
   *		or <code>YES_NO_CANCEL_OPTION</code>
   * @return an integer indicating the option selected by the user
   */
  public static int showInternalConfirmDialog(Component parentComponent,
                                              Object message, String title,
                                              int optionType) {
      return showInternalConfirmDialog(parentComponent, message, title, optionType,
                                       JOptionPane.QUESTION_MESSAGE);
  }


  /**
   * Prompts the user for input in a blocking internal dialog where
   * the initial selection, possible selections, and all other
   * options can be specified. The user will able to choose from
   * <code>selectionValues</code>, where <code>null</code>
   * implies the user can input
   * whatever they wish, usually by means of a <code>JTextField</code>.
   * <code>initialSelectionValue</code> is the initial value to prompt
   * the user with. It is up to the UI to decide how best to represent
   * the <code>selectionValues</code>, but usually a
   * <code>JComboBox</code>, <code>JList</code>, or
   * <code>JTextField</code> will be used.
   *
   * @param parentComponent the parent <code>Component</code> for the dialog
   * @param message  the <code>Object</code> to display
   * @param title    the <code>String</code> to display in the dialog title bar
   * @param messageType the type of message to be displayed:
   *                 	<code>ERROR_MESSAGE</code>, <code>INFORMATION_MESSAGE</code>,
   *			<code>WARNING_MESSAGE</code>,
   *                 	<code>QUESTION_MESSAGE</code>, or <code>PLAIN_MESSAGE</code>
   * @param imageName name of the file to use as the icon to display in the dialog (it must be stored in "images" subfolder)
   * @param selectionValues an array of <code>Objects</code> that
   *			gives the possible selections
   * @param initialSelectionValue the value used to initialize the input
   *                  field
   * @return user's input, or <code>null</code> meaning the user
   *		canceled the input
   */
  public static Object showInternalInputDialog(Component parentComponent,
                    Object message, String title, int messageType, String imageName,
                    Object[] selectionValues, Object initialSelectionValue) {
    if (message !=null && message instanceof String)
      message = ClientSettings.getInstance().getResources().getResource(message.toString());
    title = ClientSettings.getInstance().getResources().getResource(title);
    if (selectionValues!=null)
      for(int i=0;i<selectionValues.length;i++)
        if (selectionValues[i]!=null && selectionValues[i] instanceof String)
          selectionValues[i] = ClientSettings.getInstance().getResources().getResource(selectionValues[i].toString());
    if (initialSelectionValue !=null && initialSelectionValue instanceof String)
      initialSelectionValue = ClientSettings.getInstance().getResources().getResource(initialSelectionValue.toString());

    return JOptionPane.showInternalInputDialog(
      parentComponent,
      message,
      title,
      messageType,
      imageName==null?null:new ImageIcon(ClientUtils.getImage(imageName)),
      selectionValues,
      initialSelectionValue
    );

  }


  /**
   * Shows an internal dialog requesting input from the user parented
   * to <code>parentComponent</code> with the dialog having the title
   * <code>title</code> and message type <code>messageType</code>.
   *
   * @param parentComponent the parent <code>Component</code> for the dialog
   * @param message  the <code>Object</code> to display
   * @param title    the <code>String</code> to display in the dialog title bar
   * @param messageType the type of message that is to be displayed:
   *                    ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE,
   *                    QUESTION_MESSAGE, or PLAIN_MESSAGE
   */
  public static String showInternalInputDialog(Component parentComponent,
                           Object message, String title, int messageType) {
      return (String)showInternalInputDialog(parentComponent, message, title,
                                     messageType, null, null, null);
  }


  /**
   * Brings up an internal dialog panel displaying a message,
   * specifying all parameters.
   *
   * @param parentComponent determines the <code>Frame</code>
   *		in which the dialog is displayed; if <code>null</code>,
   *		or if the <code>parentComponent</code> has no
   *		<code>Frame</code>, a default <code>Frame</code> is used
   * @param message   the <code>Object</code> to display
   * @param title     the title string for the dialog (not yet translated)
   * @param messageType the type of message to be displayed:
   *                  <code>ERROR_MESSAGE</code>,
   *			<code>INFORMATION_MESSAGE</code>,
   *			<code>WARNING_MESSAGE</code>,
   *                  <code>QUESTION_MESSAGE</code>,
   *			or <code>PLAIN_MESSAGE</code>
   * @param imageName name of the file to use as the icon to display in the dialog (it must be stored in "images" subfolder)
   */
  public static void showInternalMessageDialog(Component parentComponent,
                                       Object message,
                                       String title, int messageType,
                                       String imageName){
      showInternalOptionDialog(parentComponent, message, title, JOptionPane.DEFAULT_OPTION,
                               messageType, imageName, null, null);
  }


  /**
   * Brings up an internal dialog panel with a specified icon, where
   * the initial choice is determined by the <code>initialValue</code>
   * parameter and the number of choices is determined by the
   * <code>optionType</code> parameter.
   * <p>
   * If <code>optionType</code> is <code>YES_NO_OPTION</code>, or
   * <code>YES_NO_CANCEL_OPTION</code>
   * and the <code>options</code> parameter is <code>null</code>,
   * then the options are supplied by the Look and Feel.
   * <p>
   * The <code>messageType</code> parameter is primarily used to supply
   * a default icon from the look and feel.
   *
   * @param parentComponent determines the <code>Frame</code>
   *		in which the dialog is displayed; if <code>null</code>,
   *		or if the <code>parentComponent</code> has no
   *		<code>Frame</code>, a default <code>Frame</code> is used
   * @param message   the object to display in the dialog; a
   *		<code>Component</code> object is rendered as a
   *		<code>Component</code>; a <code>String</code>
   *		object is rendered as a string. Other objects are
   *		converted to a <code>String</code> using the
   *		<code>toString</code> method.
   *            (not yet translated)
   * @param title     the title string for the dialog  (not yet translated)
   * @param optionType an integer designating the options available
   *		on the dialog: <code>YES_NO_OPTION</code>,
   *		or <code>YES_NO_CANCEL_OPTION</code>
   * @param messageType an integer designating the kind of message this is;
   *		primarily used to determine the icon from the
   *		pluggable Look and Feel: <code>ERROR_MESSAGE</code>,
   *		<code>INFORMATION_MESSAGE</code>,
   *          <code>WARNING_MESSAGE</code>, <code>QUESTION_MESSAGE</code>,
   *		or <code>PLAIN_MESSAGE</code>
   * @param imageName name of the file to use as the icon to display in the dialog (it must be stored in "images" subfolder)
   * @param options   an array of objects indicating the possible choices
   *          the user can make; if the objects are components, they
   *          are rendered properly; non-<code>String</code>
   *		objects are rendered using their <code>toString</code>
   *		methods; if this parameter is <code>null</code>,
   *		the options are determined by the Look and Feel
   * @param initialValue the object that represents the default selection
   *          for the dialog; only meaningful if <code>options</code>
   *		is used; can be <code>null</code>
   * @return an integer indicating the option chosen by the user,
   *          or <code>CLOSED_OPTION</code> if the user closed the Dialog
   */
  public static int showInternalOptionDialog(Component parentComponent,
                                     Object message,
                                     String title, int optionType,
                                     int messageType, String imageName,
                                     Object[] options, Object initialValue) {
    if (message !=null && message instanceof String)
      message = ClientSettings.getInstance().getResources().getResource(message.toString());
    title = ClientSettings.getInstance().getResources().getResource(title);
    if (options!=null)
      for(int i=0;i<options.length;i++)
        if (options[i]!=null && options[i] instanceof String)
          options[i] = ClientSettings.getInstance().getResources().getResource(options[i].toString());
    if (initialValue !=null && initialValue instanceof String)
      initialValue = ClientSettings.getInstance().getResources().getResource(initialValue.toString());

    return JOptionPane.showInternalOptionDialog(
      parentComponent,
      message,
      title,
      optionType,
      messageType,
      imageName==null?null:new ImageIcon(ClientUtils.getImage(imageName)),
      options,
      initialValue
    );
  }


  /**
   * Brings up a dialog displaying a message, specifying all parameters.
   *
   * @param parentComponent determines the <code>Frame</code> in which the
   *			dialog is displayed; if <code>null</code>,
   *			or if the <code>parentComponent</code> has no
   *			<code>Frame</code>, a
   *                  default <code>Frame</code> is used
   * @param message   the <code>Object</code> to display
   * @param title     the title string for the dialog
   * @param messageType the type of message to be displayed:
   *                  <code>ERROR_MESSAGE</code>,
   *			<code>INFORMATION_MESSAGE</code>,
   *			<code>WARNING_MESSAGE</code>,
   *                  <code>QUESTION_MESSAGE</code>,
   *			or <code>PLAIN_MESSAGE</code>
   * @param imageName name of the file to use as the icon to display in the dialog (it must be stored in "images" subfolder)
   * @exception HeadlessException if
   *   <code>GraphicsEnvironment.isHeadless</code> returns
   *   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static void showMessageDialog(Component parentComponent,
      Object message, String title, int messageType, String imageName)
      throws HeadlessException {
    showOptionDialog(
        parentComponent,
        message,
        title,
        JOptionPane.DEFAULT_OPTION,
        messageType,
        imageName,
        null,
        null
    );

  }


  /**
   * Brings up a dialog that displays a message using a default
   * icon determined by the <code>messageType</code> parameter.
   *
   * @param parentComponent determines the <code>Frame</code>
   *		in which the dialog is displayed; if <code>null</code>,
   *		or if the <code>parentComponent</code> has no
   *		<code>Frame</code>, a default <code>Frame</code> is used
   * @param message   the <code>Object</code> to display
   * @param title     the title string for the dialog (not yet translated)
   * @param messageType the type of message to be displayed:
   *                  <code>ERROR_MESSAGE</code>,
   *			<code>INFORMATION_MESSAGE</code>,
   *			<code>WARNING_MESSAGE</code>,
   *                  <code>QUESTION_MESSAGE</code>,
   *			or <code>PLAIN_MESSAGE</code>
   * @exception HeadlessException if
   *   <code>GraphicsEnvironment.isHeadless</code> returns
   *   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static void showMessageDialog(Component parentComponent,
      Object message, String title, int messageType)
      throws HeadlessException {
    showOptionDialog(parentComponent, message, title, JOptionPane.DEFAULT_OPTION,
                         messageType, null, null, null);
  }


  /**
   * Brings up a dialog with a specified icon, where the initial
   * choice is determined by the <code>initialValue</code> parameter and
   * the number of choices is determined by the <code>optionType</code>
   * parameter.
   * <p>
   * If <code>optionType</code> is <code>YES_NO_OPTION</code>,
   * or <code>YES_NO_CANCEL_OPTION</code>
   * and the <code>options</code> parameter is <code>null</code>,
   * then the options are
   * supplied by the look and feel.
   * <p>
   * The <code>messageType</code> parameter is primarily used to supply
   * a default icon from the look and feel.
   *
   * @param parentComponent determines the <code>Frame</code>
   *			in which the dialog is displayed;  if
   *                  <code>null</code>, or if the
   *			<code>parentComponent</code> has no
   *			<code>Frame</code>, a
   *                  default <code>Frame</code> is used
   * @param message   the <code>Object</code> to display
   * @param title     the title string for the dialog (not yet translated)
   * @param optionType an integer designating the options available on the
   *			dialog: <code>YES_NO_OPTION</code>,
   *			or <code>YES_NO_CANCEL_OPTION</code>
   * @param messageType an integer designating the kind of message this is,
   *                  primarily used to determine the icon from the
   *			pluggable Look and Feel: <code>ERROR_MESSAGE</code>,
   *			<code>INFORMATION_MESSAGE</code>,
   *                  <code>WARNING_MESSAGE</code>,
   *                  <code>QUESTION_MESSAGE</code>,
   *			or <code>PLAIN_MESSAGE</code>
   * @param imageName name of the file to use as the icon to display in the dialog (it must be stored in "images" subfolder)
   * @param options   an array of objects indicating the possible choices
   *                  the user can make; if the objects are components, they
   *                  are rendered properly; non-<code>String</code>
   *			objects are
   *                  rendered using their <code>toString</code> methods;
   *                  if this parameter is <code>null</code>,
   *			the options are determined by the Look and Feel.
   *                  If options are instanceof String then they will be translated.
   * @param initialValue the object that represents the default selection
   *                  for the dialog; only meaningful if <code>options</code>
   *			is used; can be <code>null</code>
   * @return an integer indicating the option chosen by the user,
   *         		or <code>CLOSED_OPTION</code> if the user closed
   *                  the dialog
   * @exception HeadlessException if
   *   <code>GraphicsEnvironment.isHeadless</code> returns
   *   <code>true</code>
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static int showOptionDialog(Component parentComponent,
    Object message, String title, int optionType, int messageType,
    String imageName, Object[] options, Object initialValue)
    throws HeadlessException {
    if (message !=null && message instanceof String)
      message = ClientSettings.getInstance().getResources().getResource(message.toString());
    title = ClientSettings.getInstance().getResources().getResource(title);
    if (options!=null)
      for(int i=0;i<options.length;i++) {
        if (options[i]!=null && options[i] instanceof String)
          options[i] = ClientSettings.getInstance().getResources().getResource(options[i].toString());
      }
    else if (optionType==JOptionPane.YES_NO_CANCEL_OPTION)
      options = new String[]{
          ClientSettings.getInstance().getResources().getResource("yes"),
          ClientSettings.getInstance().getResources().getResource("no"),
          ClientSettings.getInstance().getResources().getResource("cancel")
      };
    else if (optionType==JOptionPane.YES_NO_OPTION)
      options = new String[]{
          ClientSettings.getInstance().getResources().getResource("yes"),
          ClientSettings.getInstance().getResources().getResource("no")
      };
    else if (optionType==JOptionPane.OK_CANCEL_OPTION)
      options = new String[]{
          ClientSettings.getInstance().getResources().getResource("ok"),
          ClientSettings.getInstance().getResources().getResource("cancel")
      };
    else if (optionType==JOptionPane.DEFAULT_OPTION)
      options = new String[]{
          ClientSettings.getInstance().getResources().getResource("ok")
      };

    return JOptionPane.showOptionDialog(
      parentComponent,
      message,
      title,
      optionType,
      messageType,
      imageName==null?null:new ImageIcon(ClientUtils.getImage(imageName)),
      options,
      initialValue
    );
  }





}
