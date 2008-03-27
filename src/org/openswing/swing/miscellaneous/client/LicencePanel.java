package org.openswing.swing.miscellaneous.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.html.*;

import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that shows a licence agreement. Image icon is optional.</p>
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
public class LicencePanel extends JPanel {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel topPanel = new JPanel();
  JPanel mainPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel labelTitle = new JLabel();
  JLabel labelSubTitle = new JLabel();
  ImagePanel imagePanel = null;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JScrollPane scrollPane = new JScrollPane();
  JEditorPane controlLicence = new JEditorPane();
  JRadioButton radioButtonOk = new JRadioButton();
  JRadioButton radioButtonNo = new JRadioButton();
  JPanel buttonsPanel = new JPanel();
  JButton buttonCancel = new JButton();
  JButton buttonOk = new JButton();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  JButton buttonBack = new JButton();

  /** flag used to show/hide the "back" button */
  private boolean showBackButton = true;

  /** flag used to show/hide the "ok" button */
  private boolean showOkButton = true;

  /** flag used to show/hide the "cancel" button */
  private boolean showCancelButton = true;

  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JPanel voidPanel = new JPanel();

  /** image name */
  private String imageName = null;


  public LicencePanel() {
    try {
      jbInit();
      controlLicence.setEditorKit(new HTMLEditorKit());
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Set title in bold style. It will be translated, according to ClientSettings.
   * @param title title in bold style
   */
  public final void setTitle(String title) {
    labelTitle.setText(title);
  }


  /**
   * Set tip image.
   * @param imageName tip image
   */
  public final void setImageName(String imageName) {
    this.imageName = imageName;
    if (imagePanel==null) {
      imagePanel = new ImagePanel();
      topPanel.add(imagePanel,  new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }
    imagePanel.setImageName(imageName);
    imagePanel.setScrollBarsPolicy(ImagePanel.SCROLLBAR_NEVER);
    imagePanel.setMinimumSize(new Dimension(imagePanel.getImageWidth(),imagePanel.getImageHeight()));
    imagePanel.setMaximumSize(new Dimension(imagePanel.getImageWidth(),imagePanel.getImageHeight()));
    imagePanel.setPreferredSize(new Dimension(imagePanel.getImageWidth(),imagePanel.getImageHeight()));
  }


  /**
   * Set subtitle. It will be translated, according to ClientSettings.
   * @param subTitle subtitle
   */
  public final void setSubTitle(String subTitle) {
    labelSubTitle.setText(subTitle);
  }


  /**
   * @return title in bold style
   */
  public final String getTitle() {
    return labelTitle.getText();
  }


  /**
   * @return subtitle
   */
  public final String getSubTitle() {
    return labelSubTitle.getText();
  }


  /**
   * Set subtitle. It will be translated, according to ClientSettings.
   * @param licence licence text; it can be in HTML format
   */
  public final void setLicence(String licence) {
    controlLicence.setText(licence);
  }


  /**
   * @return licence text
   */
  public final String getLicence() {
    return controlLicence.getText();
  }


  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    topPanel.setLayout(gridBagLayout1);
    labelTitle.setFont(new java.awt.Font(labelSubTitle.getFont().getName(),Font.BOLD,labelSubTitle.getFont().getSize()+1));
    topPanel.setBackground(Color.white);
    mainPanel.setLayout(gridBagLayout2);
    controlLicence.setEditable(false);
    radioButtonOk.setText(ClientSettings.getInstance().getResources().getResource("i accept the terms in the licence agreement"));
    radioButtonOk.addItemListener(new LicencePanel_radioButtonOk_itemAdapter(this));
    radioButtonNo.setSelected(true);
    radioButtonNo.setText(ClientSettings.getInstance().getResources().getResource("i do not accept the terms in the licence agreement"));
    buttonsPanel.setLayout(gridBagLayout3);
    buttonCancel.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    buttonOk.setEnabled(false);
    buttonOk.setText(ClientSettings.getInstance().getResources().getResource("ok"));
    buttonBack.setText("< "+ClientSettings.getInstance().getResources().getResource("back"));
    this.add(topPanel,  BorderLayout.NORTH);
    this.add(mainPanel,  BorderLayout.CENTER);
    topPanel.add(labelTitle,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelSubTitle,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 25, 5, 5), 0, 0));
    mainPanel.add(scrollPane,     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(20, 20, 0, 20), 0, 0));
    scrollPane.getViewport().add(controlLicence, null);
    mainPanel.add(radioButtonOk,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(radioButtonNo,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    mainPanel.add(buttonsPanel,    new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(buttonBack,     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(buttonOk,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(buttonCancel,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(voidPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    buttonGroup1.add(radioButtonOk);
    buttonGroup1.add(radioButtonNo);
  }


  void radioButtonOk_itemStateChanged(ItemEvent e) {
    buttonOk.setEnabled(radioButtonOk.isSelected());
  }


  /**
   * Add an ItemListener to the "ok" radio button.
   * @param listener ItemListener added to the "ok" radio button
   */
  public final void addOkRadioButtonItemListener(ItemListener listener) {
    radioButtonOk.addItemListener(listener);
  }


  /**
   * Add an ActionListener to the "ok" button.
   * @param listener ActionListener to add
   */
  public final void addOkActionListener(ActionListener listener) {
    buttonOk.addActionListener(listener);
  }


  /**
   * Add an ActionListener to the "cancel" button.
   * @param listener ActionListener to add
   */
  public final void addCancelActionListener(ActionListener listener) {
    buttonCancel.addActionListener(listener);
  }


  /**
   * Add an ActionListener to the "back" button.
   * @param listener ActionListener to add
   */
  public final void addBackActionListener(ActionListener listener) {
    buttonBack.addActionListener(listener);
  }


  /**
   * @return flag used to show/hide the "back" button
   */
  public final boolean isShowBackButton() {
    return showBackButton;
  }


  /**
   * @return flag used to show/hide the "cancel" button
   */
  public final boolean isShowCancelButton() {
    return showCancelButton;
  }


  /**
   * @return flag used to show/hide the "ok" button
   */
  public final boolean isShowOkButton() {
    return showOkButton;
  }


  /**
   * Set the flag used to show/hide the "ok" button.
   * @param showOkButton flag used to show/hide the "ok" button
   */
  public final void setShowOkButton(boolean showOkButton) {
    if (this.showOkButton && !showOkButton)
      buttonsPanel.remove(buttonOk);
    else if (!this.showOkButton && showOkButton)
      buttonsPanel.add(buttonOk,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.revalidate();
    this.showOkButton = showOkButton;
  }


  /**
   * Set the flag used to show/hide the "cancel" button.
   * @param showCancelButton flag used to show/hide the "cancel" button
   */
  public final void setShowCancelButton(boolean showCancelButton) {
    if (this.showCancelButton && !showCancelButton)
      buttonsPanel.remove(buttonCancel);
    else if (!this.showCancelButton && showCancelButton)
      buttonsPanel.add(buttonCancel,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.revalidate();
    this.showCancelButton = showCancelButton;

  }


  /**
   * Set the flag used to show/hide the "back" button.
   * @param showBackButton flag used to show/hide the "back" button
   */
  public final void setShowBackButton(boolean showBackButton) {
    if (this.showBackButton && !showBackButton)
      buttonsPanel.remove(buttonBack);
    else if (!this.showBackButton && showBackButton)
      buttonsPanel.add(buttonBack,     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.revalidate();
    this.showBackButton = showBackButton;

  }
  public String getImageName() {
    return imageName;
  }



}


class LicencePanel_radioButtonOk_itemAdapter implements java.awt.event.ItemListener {
  LicencePanel adaptee;

  LicencePanel_radioButtonOk_itemAdapter(LicencePanel adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.radioButtonOk_itemStateChanged(e);
  }
}
