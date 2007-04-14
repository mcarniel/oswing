package org.openswing.swing.client;

import org.openswing.swing.logger.client.Logger;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.client.ClientUtils;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.table.client.Grids;


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

  /** load the previous block of records into the grid */
  private JButton prevButton = new GenericButton(new ImageIcon(ClientUtils.getImage("prev.gif")));

  /** load the next block of records into the grid */
  private JButton nextButton = new GenericButton(new ImageIcon(ClientUtils.getImage("next.gif")));

  /** load the last block of records into the grid */
  private JButton lastButton = new GenericButton(new ImageIcon(ClientUtils.getImage("last.gif")));

  /** grid control */
  private Grids resultSetController = null;


  public NavigatorBar() {
    try {
      jbInit();
      firstButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Load the first block of records"));
      firstButton.setPreferredSize(new Dimension(32,32));
      prevButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Load the previous block of records"));
      prevButton.setPreferredSize(new Dimension(32,32));
      nextButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Load the next block of records"));
      nextButton.setPreferredSize(new Dimension(32,32));
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
    prevButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        prevButton_actionPerformed(e);
      }
    });
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
    this.add(prevButton,null);
    this.add(nextButton,null);
    this.add(lastButton,null);
  }


  public void firstButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null)
      resultSetController.firstRow();
  }

  public void prevButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null)
      resultSetController.previousRow();
  }

  public void nextButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null)
      resultSetController.nextRow();
  }

  public void lastButton_actionPerformed(ActionEvent e) {
    if (resultSetController!=null)
      resultSetController.lastRow();
  }



  /**
   * Method called by grid component each time a row is selected.
   */
  public void setFirstRow(boolean isFirstRecord) {
    firstButton.setEnabled(!isFirstRecord);
    prevButton.setEnabled(!isFirstRecord);
  }


  /**
   * Method called by grid component each time a row is selected.
   */
  public void setLastRow(boolean isLastRecord) {
    lastButton.setEnabled(!isLastRecord);
    nextButton.setEnabled(!isLastRecord);
  }


  public void setEnabled(boolean enabled) {
    firstButton.setEnabled(enabled);
    prevButton.setEnabled(enabled);
    nextButton.setEnabled(enabled);
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


}
