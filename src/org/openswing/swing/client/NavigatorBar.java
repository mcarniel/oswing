package org.openswing.swing.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Navigator bar: contains first, previous, next and last buttons.
 * This class must be used inside a GridControl.</p>
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
public class NavigatorBar extends JPanel {

  /** load the first block of records into the grid */
  private JButton firstButton = new GenericButton(new ImageIcon(ClientUtils.getImage("first.gif")));

  /** select the previous row in the grid */
  private JButton prevButton = new GenericButton(new ImageIcon(ClientUtils.getImage("prev.gif")));

  /** select the next row in the grid */
  private JButton nextButton = new GenericButton(new ImageIcon(ClientUtils.getImage("next.gif")));

  /** load the last block of records into the grid */
  private JButton lastButton = new GenericButton(new ImageIcon(ClientUtils.getImage("last.gif")));

  /** load the next block of records from the grid */
  private JButton nextPgButton = new GenericButton(new ImageIcon(ClientUtils.getImage("nextpg.gif")));

  /** load the previous block of records from the grid */
  private JButton prevPgButton = new GenericButton(new ImageIcon(ClientUtils.getImage("prevpg.gif")));

  /** grid control */
  private Grids resultSetController = null;

  /** list of ActionListeners registered to this navigator bar; these listeners will be called AFTER a navigator button has been pressed (AFTER selecting row event) */
  private ArrayList afterActionListeners = new ArrayList();

  /** list of ActionListeners registered to this navigator bar; these listeners will be called as the first instruction when a navigator button is being pressed (BEFORE selecting row event) */
  private ArrayList beforeActionListeners = new ArrayList();

  public static final String FIRST_BUTTON = "FIRST_BUTTON";
  public static final String PREV_BUTTON = "PREV_BUTTON";
  public static final String NEXT_BUTTON = "NEXT_BUTTON";
  public static final String LAST_BUTTON = "LAST_BUTTON";
  public static final String NEXT_PG_BUTTON = "NEXT_PG_BUTTON";
  public static final String PREV_PG_BUTTON = "PREV_PG_BUTTON";
  public static final String UP_KEY = "UP_KEY";
  public static final String DOWN_KEY = "DOWN_KEY";
  public static final String LEFT_MOUSE_BUTTON = "LEFT_MOUSE_BUTTON";


  public NavigatorBar() {
    try {
      jbInit();
      firstButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Load the first block of records"));
      firstButton.setPreferredSize(new Dimension(32,32));
      prevPgButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Load the previous block of records"));
      prevPgButton.setPreferredSize(new Dimension(32,32));
      prevButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Select the previous row in grid"));
      prevButton.setPreferredSize(new Dimension(32,32));
      nextButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Select the next row in grid"));
      nextButton.setPreferredSize(new Dimension(32,32));
      nextPgButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Load the next block of records"));
      nextPgButton.setPreferredSize(new Dimension(32,32));
      lastButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Load the last block of records"));
      lastButton.setPreferredSize(new Dimension(32,32));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    firstButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        firstButton_actionPerformed(e);
      }
    });
    prevButton.setActionCommand(PREV_BUTTON);
    prevButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        prevButton_actionPerformed(e);
      }
    });
    prevPgButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        prevPgButton_actionPerformed(e);
      }
    });
    nextPgButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nextPgButton_actionPerformed(e);
      }
    });
    nextButton.setActionCommand(NEXT_BUTTON);
    nextButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        nextButton_actionPerformed(e);
      }
    });
    lastButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        lastButton_actionPerformed(e);
      }
    });

    this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    this.add(firstButton,null);
    if (ClientSettings.SHOW_PAGINATION_BUTTONS_ON_NAVBAR)
      this.add(prevPgButton,null);
    this.add(prevButton,null);
    this.add(nextButton,null);
    if (ClientSettings.SHOW_PAGINATION_BUTTONS_ON_NAVBAR)
      this.add(nextPgButton,null);
    this.add(lastButton,null);
  }


  public void firstButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null) {
      for(int i=0;i<beforeActionListeners.size();i++)
        ((ActionListener)beforeActionListeners.get(i)).actionPerformed(new ActionEvent(
            this,
            ActionEvent.ACTION_PERFORMED,
            FIRST_BUTTON
        ));
      resultSetController.firstRow(this);
    }
  }

  public void prevPgButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null) {
      for(int i=0;i<beforeActionListeners.size();i++)
        ((ActionListener)beforeActionListeners.get(i)).actionPerformed(new ActionEvent(
            this,
            ActionEvent.ACTION_PERFORMED,
            PREV_PG_BUTTON
        ));
      resultSetController.previousPage(this);
    }
  }

  public void prevButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null) {
      for(int i=0;i<beforeActionListeners.size();i++)
        ((ActionListener)beforeActionListeners.get(i)).actionPerformed(new ActionEvent(
            this,
            ActionEvent.ACTION_PERFORMED,
            e.getActionCommand()
        ));

      resultSetController.previousRow(this,e);
    }
  }

  public void nextButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null) {
      for(int i=0;i<beforeActionListeners.size();i++)
        ((ActionListener)beforeActionListeners.get(i)).actionPerformed(new ActionEvent(
            this,
            ActionEvent.ACTION_PERFORMED,
            e.getActionCommand()
        ));

      resultSetController.nextRow(this,e);
    }
  }


  public void nextPgButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null) {
      for(int i=0;i<beforeActionListeners.size();i++)
        ((ActionListener)beforeActionListeners.get(i)).actionPerformed(new ActionEvent(
            this,
            ActionEvent.ACTION_PERFORMED,
            NEXT_PG_BUTTON
        ));

      resultSetController.nextPage(this);
    }
  }


  public void lastButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null) {
      for(int i=0;i<beforeActionListeners.size();i++)
        ((ActionListener)beforeActionListeners.get(i)).actionPerformed(new ActionEvent(
            this,
            ActionEvent.ACTION_PERFORMED,
            LAST_BUTTON
        ));

      resultSetController.lastRow(this);
    }
  }



  /**
   * Method called by grid component each time a row is selected.
   */
  public void setFirstRow(boolean isFirstRecord) {
    firstButton.setEnabled(!isFirstRecord);
    prevPgButton.setEnabled(!isFirstRecord);
    prevButton.setEnabled(!isFirstRecord);
  }


  /**
   * Method called by grid component each time a row is selected.
   */
  public void setLastRow(boolean isLastRecord) {
    lastButton.setEnabled(!isLastRecord);
    nextButton.setEnabled(!isLastRecord);
    nextPgButton.setEnabled(!isLastRecord);
  }


  public void setEnabled(boolean enabled) {
    firstButton.setEnabled(enabled);
    prevPgButton.setEnabled(enabled);
    prevButton.setEnabled(enabled);
    nextButton.setEnabled(enabled);
    nextPgButton.setEnabled(enabled);
    lastButton.setEnabled(enabled);
  }


  public void initNavigator(Grids resultSetController){
    this.resultSetController = resultSetController;
  }


  public boolean isFirstButtonEnabled() {
    return firstButton.isEnabled();
  }


  public boolean isPrevButtonEnabled() {
    return prevButton.isEnabled();
  }


  public boolean isNextButtonEnabled() {
    return nextButton.isEnabled();
  }


  public boolean isLastButtonEnabled() {
    return lastButton.isEnabled();
  }


  /**
   * Add an ActionListener that will be called AFTER a navigator button has been pressed (AFTER selecting row event).
   * @param listener ActionListener to register.
   */
  public final void addAfterActionListener(ActionListener listener) {
    afterActionListeners.add(listener);
  }


  /**
   * Remove the specified ActionListener.
   * @param listener ActionListener to remove.
   */
  public final void removeAfterActionListener(ActionListener listener) {
    afterActionListeners.remove(listener);
  }


  /**
   * Method invoked by the grid control when pressing a navigator button.
   * @param buttonType possible values: NavigatorBar.xxx_BUTTON
   */
  public final void fireButtonPressedEvent(String buttonType) {
    for(int i=0;i<afterActionListeners.size();i++)
      ((ActionListener)afterActionListeners.get(i)).actionPerformed(new ActionEvent(
          this,
          ActionEvent.ACTION_PERFORMED,
          buttonType
      ));
  }


  /**
   * Add an ActionListener that will be called as the first instruction when a navigator button is being pressed (BEFORE selecting row event).
   * @param listener ActionListener to register.
   */
  public final void addBeforeActionListener(ActionListener listener) {
    beforeActionListeners.add(listener);
  }


  /**
   * Remove the specified ActionListener.
   * @param listener ActionListener to remove.
   */
  public final void removeBeforerActionListener(ActionListener listener) {
    beforeActionListeners.remove(listener);
  }


}
