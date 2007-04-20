package org.openswing.swing.wizard.client;

import javax.swing.*;
import java.awt.*;

import org.openswing.swing.client.*;

import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.beans.Beans;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains two areas:
 * - a list of panels showed alternatively and
 * - a buttons panel, that typically contains a back, next and cancel buttons.
 * As default setting, three buttons are showed in the panel: "Back", "Next" and "Cancel".
 * Another button named "Finish" is automatically showed when the wizard shows the last panel.
 *
 * A programmer must add a list of WizardInnerPanel objects though the addPanel() method and
 * could define navigation logic through the method setNavigationLogic(). If this method is not called
 * then a default navigation logic is defined: shows panels from the first added to the last added.
 *
 * When user clicks on the "Next" button the WizardController.getNextPanelId() is called and the related panel is showed.
 * If null is returned then no panel is showed (the current panel is still visible).
 * When user clicks on the "Back" button the WizardController.getBackPanelId() is called and the related panel is showed.
 * If null is returned then no panel is showed (the current panel is still visible).
 * "Back" and "Next" buttons are enabled/disabled automatically, according to WizardController.getFirstPanelId() and
 * WizardController.getLastPanelId() return values.
 *
 * When a panel is showed the init() method is automatically invoked (after "Back"/"Next" buttons abilitations).
 * </p>
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
public class WizardPanel extends JPanel implements ActionListener {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  JButton backButton = new JButton();
  JButton nextButton = new JButton();
  JButton cancelButton = new JButton();
  JButton finishButton = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();

  /** list of action listeners linked to the buttons */
  private ArrayList listeners = new ArrayList();

  /** list of WizardInnerPanel objects */
  private ArrayList panels = new ArrayList();

  /** navigation logic controller */
  private WizardController wizardController = new WizardController();

  /** WizardInnerPanel panel currently showed */
  private WizardInnerPanel currentVisiblePanel = null;


  public WizardPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
    mainPanel.setLayout(cardLayout1);
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    buttonsPanel.setLayout(flowLayout1);
    mainPanel.setBorder(BorderFactory.createEtchedBorder());
    backButton.setEnabled(false);
    backButton.setEnabled(false);
    backButton.setText(ClientSettings.getInstance().getResources().getResource("back"));
    backButton.addActionListener(this);
    nextButton.setEnabled(false);
    nextButton.setText(ClientSettings.getInstance().getResources().getResource("next"));
    nextButton.addActionListener(this);
    cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    cancelButton.addActionListener(this);
    finishButton.setText(ClientSettings.getInstance().getResources().getResource("finish"));
    finishButton.addActionListener(this);
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    this.add(mainPanel,  BorderLayout.CENTER);
    this.add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(backButton, null);
    buttonsPanel.add(nextButton, null);
    buttonsPanel.add(cancelButton, null);
  }


  /**
   * Method called when the this panel is set to visible: it calls commitColumnContainer method.
   */
  public final void addNotify() {
    super.addNotify();
    if (!Beans.isDesignTime() && currentVisiblePanel == null) {
      String panelId = wizardController.getFirstPanelId(this);
      if (panelId!=null) {
        currentVisiblePanel = getPanel( panelId );
        cardLayout1.show(mainPanel,currentVisiblePanel.getPanelId());
        if (panels.size()>1)
          nextButton.setEnabled(true);

        currentVisiblePanel.init();
      }
    }
  }


  /**
   * @return buttons panel
   */
  public final JPanel getButtonsPanel() {
    return buttonsPanel;
  }


  /**
   * Add a button in the specified position.
   * @param button button to add
   * @param pos position inside the buttons panel
   */
  public final void addButton(JButton button,int pos) {
    buttonsPanel.add(button,pos);
    button.addActionListener(this);
  }


  /**
   * Remove a button already added.
   * @param button button to remove
   */
  public final void removeButton(JButton button) {
    buttonsPanel.remove(button);
    buttonsPanel.revalidate();
    buttonsPanel.repaint();
    button.removeActionListener(this);
  }


  /**
   * Add a listener of buttons clicking events.
   * @param listener action listener
   */
  public final void addActionListener(ActionListener listener) {
    listeners.add(listener);
  }


  /**
   * Remove a listener of buttons clicking events.
   * @param listener action listener to remove
   */
  public final void removeActionListener(ActionListener listener) {
    listeners.remove(listener);
  }


  /**
   * Listen for additional buttons clicks.
   */
  public void actionPerformed(ActionEvent actionEvent) {
    for(int i=0;i<listeners.size();i++)
      ((ActionListener)listeners.get(i)).actionPerformed(actionEvent);


    if (actionEvent.getSource().equals(backButton)) {
      String panelId = wizardController.getBackPanelId(this);
      if (panelId!=null) {
        currentVisiblePanel = getPanel( panelId );
        cardLayout1.show(mainPanel,panelId);
        if (currentVisiblePanel.equals(wizardController.getFirstPanelId(this)))
          backButton.setEnabled(false);
        else
          backButton.setEnabled(true);

        buttonsPanel.remove(finishButton);
        buttonsPanel.revalidate();
        buttonsPanel.repaint();

        currentVisiblePanel.init();
      }
    }
    else if (actionEvent.getSource().equals(nextButton)) {
      String panelId = wizardController.getNextPanelId(this);
      if (panelId!=null) {
        currentVisiblePanel = getPanel( panelId );
        cardLayout1.show(mainPanel,panelId);
        if (currentVisiblePanel.equals(wizardController.getLastPanelId(this))) {
          nextButton.setEnabled(false);

          buttonsPanel.add(finishButton,null);
          buttonsPanel.revalidate();
          buttonsPanel.repaint();
        } else
          nextButton.setEnabled(true);

        currentVisiblePanel.init();
      }
    }
  }


  /**
   * @return "Back" button
   */
  public final JButton getBackButton() {
    return backButton;
  }


  /**
   * @return "Cancel/Finish" button
   */
  public final JButton getCancelButton() {
    return cancelButton;
  }


  /**
   * @return "Next" button
   */
  public final JButton getNextButton() {
    return nextButton;
  }


  /**
   * Register a panel that must be showed inside the wizard panel.
   * @param panel panel to add
   */
  public final void addPanel(WizardInnerPanel panel) {
    panels.add(panel);
    mainPanel.add(panel.getPanelId(),panel);
  }


  /**
   * Define a navigation logic.
   * If this method is not defined, then panels are showed in the same order used to add them to this.
   */
  public final void setNavigationLogic(WizardController wizardController) {
    this.wizardController = wizardController;
  }


  /**
   * @return list of WizardInnerPanel currently registered.
   */
  public WizardInnerPanel[] getPanels() {
    return (WizardInnerPanel[])panels.toArray(new WizardInnerPanel[panels.size()]);
  }


  /**
   * @return WizardInnerPanel panel currently showed
   */
  public final WizardInnerPanel getCurrentVisiblePanel() {
    return currentVisiblePanel;
  }


  /**
   * @param panelId panel identifier
   * @return WizardInnerPanel panel identified by the first argument
   */
  public final WizardInnerPanel getPanel(String panelId) {
    for(int i=0;i<panels.size();i++)
      if (((WizardInnerPanel)panels.get(i)).getPanelId().equals(panelId))
        return (WizardInnerPanel)panels.get(i);
    return null;
  }


  /**
   * @return "Finish" button
   */
  public final JButton getFinishButton() {
    return finishButton;
  }




}
