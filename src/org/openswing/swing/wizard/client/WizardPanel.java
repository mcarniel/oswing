package org.openswing.swing.wizard.client;

import java.beans.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains two areas:
 * - a list of panels showed alternatively and
 * - a buttons panel, that typically contains a back, next and cancel buttons.
 * A panel may optionally have at the left an image.
 *
 * As default setting, three buttons are showed in the panel: "Back", "Next" and "Cancel".
 * The "Cancel" button is automatically renamed to "Finish" when the wizard shows the last panel.
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
  FlowLayout flowLayout1 = new FlowLayout();

  /** list of action listeners linked to the buttons */
  private ArrayList listeners = new ArrayList();

  /** list of WizardInnerPanel objects */
  private ArrayList panels = new ArrayList();

  /** navigation logic controller */
  private WizardController wizardController = new WizardController();

  /** WizardInnerPanel panel currently showed */
  private WizardInnerPanel currentVisiblePanel = null;

  /** image panel, showed at the left of an inner panel */
  private ImagePanel imagePanel = new ImagePanel();

  /** (optional) image name to show in all inner panels; this setting is overrided by the getImageName() method defined for each inner panel */
  private String imageName = null;

  /** image name for "Back" button */
  private String backImageName = "prev.gif";

  /** image name for "Next" button */
  private String nextImageName = "next.gif";

  /** image name for "Cancel" button */
  private String cancelImageName = "cancel.gif";

  /** image name for "Finish" button */
  private String finishImageName = "exec.gif";


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
    backButton.setPreferredSize(new Dimension(120,30));
    backButton.setMinimumSize(new Dimension(120,30));
    backButton.setEnabled(false);
    backButton.setEnabled(false);
    backButton.setText(ClientSettings.getInstance().getResources().getResource("back"));
    backButton.addActionListener(this);
    nextButton.setPreferredSize(new Dimension(120,30));
    nextButton.setMinimumSize(new Dimension(120,30));
    nextButton.setEnabled(false);
    nextButton.setText(ClientSettings.getInstance().getResources().getResource("next"));
    nextButton.addActionListener(this);
    cancelButton.setPreferredSize(new Dimension(120,30));
    cancelButton.setMinimumSize(new Dimension(120,30));
    cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    cancelButton.addActionListener(this);
    flowLayout1.setAlignment(FlowLayout.RIGHT);
    this.add(imagePanel,  BorderLayout.WEST);
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

        imagePanel.setImage( imageName );
        if (currentVisiblePanel.getImageName()!=null)
          imagePanel.setImage( currentVisiblePanel.getImageName() );

        backButton.setIcon(new ImageIcon(ClientUtils.getImage(backImageName)));
        nextButton.setIcon(new ImageIcon(ClientUtils.getImage(nextImageName)));
        cancelButton.setIcon(new ImageIcon(ClientUtils.getImage(cancelImageName)));

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
  public final void actionPerformed(ActionEvent actionEvent) {
    for(int i=0;i<listeners.size();i++)
      ((ActionListener)listeners.get(i)).actionPerformed(actionEvent);


    if (actionEvent.getSource().equals(backButton)) {
      String panelId = wizardController.getBackPanelId(this);
      if (panelId!=null) {
        currentVisiblePanel = getPanel( panelId );
        cardLayout1.show(mainPanel,panelId);
        if (panelId.equals(wizardController.getFirstPanelId(this))) {
          backButton.setEnabled(false);
          nextButton.setEnabled(true);
        }
        else {
          backButton.setEnabled(true);
          nextButton.setEnabled(true);
        }

        cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
        cancelButton.setIcon(new ImageIcon(ClientUtils.getImage(cancelImageName)));

        buttonsPanel.revalidate();
        buttonsPanel.repaint();

        imagePanel.setImage( imageName );
        if (currentVisiblePanel.getImageName()!=null)
          imagePanel.setImage( currentVisiblePanel.getImageName() );

        currentVisiblePanel.init();
      }
    }
    else if (actionEvent.getSource().equals(nextButton)) {
      String panelId = wizardController.getNextPanelId(this);
      if (panelId!=null) {
        currentVisiblePanel = getPanel( panelId );
        cardLayout1.show(mainPanel,panelId);
        if (panelId.equals(wizardController.getLastPanelId(this))) {
          nextButton.setEnabled(false);
          backButton.setEnabled(true);

          cancelButton.setText(ClientSettings.getInstance().getResources().getResource("finish"));
          cancelButton.setIcon(new ImageIcon(ClientUtils.getImage(finishImageName)));
          buttonsPanel.revalidate();
          buttonsPanel.repaint();
        } else {
          nextButton.setEnabled(true);
          backButton.setEnabled(true);
        }
        imagePanel.setImage( imageName );
        if (currentVisiblePanel.getImageName()!=null)
          imagePanel.setImage( currentVisiblePanel.getImageName() );

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
   * @return panes nagitation controller
   */
  public final WizardController getNavigationLogic() {
    return wizardController;
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
   * This method allows to define an image that will be showed at the left of each inner panel
   * This setting is overrided by the getImageName() method defined for each inner panel.
   * @param imageName image name; null if no image is required
   */
  public final void setImageName(String imageName) {
    this.imageName = imageName;
  }


  /**
   * @return image name
   */
  public final String getImageName() {
    return imageName;
  }


  /**
   * @return image name for "Cancel" button
   */
  public final String getCancelImageName() {
    return cancelImageName;
  }


  /**
   * Set image name for "Cancel" button.
   * @param cancelImageName image name for "Cancel" button
   */
  public final void setCancelImageName(String cancelImageName) {
    this.cancelImageName = cancelImageName;
  }


  /**
   * @return image name for "Back" button
   */
  public final String getBackImageName() {
    return backImageName;
  }


  /**
   * Set image name for "Back" button.
   * @param backImageName image name for "Back" button
   */
  public final void setBackImageName(String backImageName) {
    this.backImageName = backImageName;
  }


  /**
   * @return image name for "Finish" button
   */
  public final String getFinishImageName() {
    return finishImageName;
  }


  /**
   * Set image name for "Finish" button.
   * @param finishImageName image name for "Finish" button
   */
  public final void setFinishImageName(String finishImageName) {
    this.finishImageName = finishImageName;
  }


  /**
   * @return image name for "Next" button
   */
  public final String getNextImageName() {
    return nextImageName;
  }


  /**
   * Set image name for "Next" button.
   * @param nextImageName image name for "Next" button
   */
  public final void setNextImageName(String nextImageName) {
    this.nextImageName = nextImageName;
  }




  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to show an image.</p>
   */
  class ImagePanel extends JPanel {

    /** image to show */
    private Image image = null;


    /**
     * Set image.
     * @param imageName image name (it must be stored in "images" subfolder)
     */
    public void setImage(String imageName) {
      if (imageName==null) {
        image = null;
        setSize(0,0);
        setPreferredSize(new Dimension(0,0));
        setMinimumSize(new Dimension(0,0));
        return;
      }
      image = ClientUtils.getImage(imageName);
      if (image==null)
        return;
      setSize(image.getWidth(this),image.getHeight(this));
      setPreferredSize(new Dimension(image.getWidth(this),image.getHeight(this)));
      setMinimumSize(new Dimension(image.getWidth(this),image.getHeight(this)));
      repaint();
    }


    public void paint(Graphics g) {
      super.paint(g);
      if (image!=null)
        g.drawImage(image,0,0,image.getWidth(this),image.getHeight(this),this);
    }


  }



}
