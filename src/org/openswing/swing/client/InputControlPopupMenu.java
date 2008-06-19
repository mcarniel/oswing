package org.openswing.swing.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.util.client.*;
import java.awt.datatransfer.*;
import java.text.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.form.model.client.VOModel;
import org.openswing.swing.form.model.client.ValueChangeEvent;
import org.openswing.swing.form.model.client.ValueChangeListener;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class that could be used as bease class for an input control.</p>
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
public class InputControlPopupMenu extends JPopupMenu implements MouseListener {

  /** input control linked to this popup menu */
  private InputControl inputControl = null;

  /** flag used to show copy/cut/past command in popup menu */
  private boolean copyAndPasteEnabled = false;


  /**
   * Constructor invoked by input control when right mouse clicking on it.
   * @param inputControl input control linked to this popup menu
   */
  public InputControlPopupMenu(InputControl inputControl) {
    this.inputControl = inputControl;
    if (inputControl instanceof BaseInputControl)
      ((BaseInputControl)inputControl).getBindingComponent().addMouseListener(this);
    else
      ((JComponent)inputControl).addMouseListener(this);

    copyAndPasteEnabled =
        !(inputControl instanceof CheckBoxControl) &&
        !(inputControl instanceof RadioButtonControl) &&
        !(inputControl instanceof ComboBoxControl) &&
        !(inputControl instanceof ComboBoxVOControl) &&
        !(inputControl instanceof ListControl) &&
        !(inputControl instanceof ListVOControl) &&
        !(inputControl instanceof ProgressBarControl) &&
        !(inputControl instanceof ImageControl);
  }


  /**
   * Set visibility state of popup menu.
   */
  public final void setVisible(boolean v) {
    super.setVisible(v);
    if (!v &&
        inputControl!=null &&
        ((JComponent)inputControl).isVisible()) {
      if (inputControl instanceof BaseInputControl && !((BaseInputControl)inputControl).getBindingComponent().hasFocus())
        ((BaseInputControl)inputControl).getBindingComponent().requestFocus();
      else if (!(inputControl instanceof BaseInputControl) && !((JComponent)inputControl).hasFocus())
        ((JComponent)inputControl).requestFocus();
    }
  }


  /**
   * Copy data within input control to the system clipboard.
   */
  private boolean copyToClipboard(Object value) {
    Transferable contents = null;
    if (value instanceof String)
      contents = new StringSelection(value.toString());
    else if (value instanceof java.util.Date) {
      SimpleDateFormat sdfdatetime = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE_TIME));
      contents = new StringSelection(sdfdatetime.format((java.util.Date)value));
    }
    else if (value instanceof Number)
      contents = new StringSelection(value.toString());

    if (contents!=null) {
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, null);
      return true;
    }
    else
      return false;
  }



  /**
   * Method called when input control content has been changed.
   */
  private void maybeFireValueChangedEvent(Object oldValue,Object newValue) {
    // retrieve current value...

    if ((oldValue==null && newValue!=null) ||
        (oldValue!=null && newValue==null) ||
        (oldValue!=null && newValue!=null && ! oldValue.equals(newValue))) {

      // mark the input control as changed
      inputControl.setChanged(true);

      // fire value changed events...
      ValueChangeEvent e = new ValueChangeEvent(inputControl, inputControl.getAttributeName(), oldValue, newValue);
      for (int i = 0; i < inputControl.getValueChangeListeners().length; i++)
        inputControl.getValueChangeListeners()[i].valueChanged(e);
    }

  }



  /**
   * Method called when user right clicks with the mouse on the linked input control.
   */
  private void showPopupMenu(final int x,final int y) {
    try {
      final Point tablexy=((JComponent)inputControl).getLocationOnScreen();
      final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

      removeAll();

      final Object value = inputControl.getValue();
      if (value!=null && copyAndPasteEnabled) {
        // add "copy" menu item...
        JMenuItem copyMenu = new JMenuItem(
          ClientSettings.getInstance().getResources().getResource("copy"),
          ClientSettings.getInstance().getResources().getResource("copymnemonic").charAt(0)
        );
        copyMenu.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            copyToClipboard(value);
          }
        });
        this.add(copyMenu);
      }


      if (inputControl.isEnabled()) {

        if (copyAndPasteEnabled) {

          JMenuItem cutMenu = new JMenuItem(
            ClientSettings.getInstance().getResources().getResource("cut"),
            ClientSettings.getInstance().getResources().getResource("cutmnemonic").charAt(0)
          );
          cutMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if (copyToClipboard(value)) {
                Object oldValue = inputControl.getValue();
                inputControl.setValue(null);
                maybeFireValueChangedEvent(oldValue,null);
              }
            }
          });
          this.add(cutMenu);


          JMenuItem pasteMenu = new JMenuItem(
            ClientSettings.getInstance().getResources().getResource("paste"),
            ClientSettings.getInstance().getResources().getResource("pastemnemonic").charAt(0)
          );
          pasteMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              Transferable content = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
              if(content != null) {
                try {
                  String data = (String) content.getTransferData(DataFlavor.stringFlavor);
                  if (data!=null && !data.equals("")) {
                    Object value = null;
                    if (inputControl instanceof CodLookupControl) {
                      if (((CodLookupControl)inputControl).isAllowOnlyNumbers())
                        value = new Double(data);
                      else
                        value = data;
                    }
                    else if (inputControl instanceof NumericControl) {
                      value = new Double(data);
                    }
                    else if (inputControl instanceof DateControl) {
                      SimpleDateFormat sdfdatetime = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE_TIME));
                      value = sdfdatetime.parse(data);
                    }
                    else if (inputControl instanceof FormattedTextControl) {
                      value = data;
                    }
                    else if (inputControl instanceof TextControl) {
                      value = data;
                    }
                    else if (inputControl instanceof TextAreaControl) {
                      int pos = ((TextAreaControl)inputControl).getTextArea().getCaretPosition();
                      if (pos<0)
                        pos = 0;
                      value = ((TextAreaControl)inputControl).getTextArea().getText();
                      value =
                          (pos==0)?
                          (data+value):
                          (
                           pos==value.toString().length()-1?
                           value.toString().substring(0,pos)+data:
                           value.toString().substring(0,pos)+data+value.toString().substring(pos)
                          );
                    }
                    if (value!=null) {
                      Object oldValue = inputControl.getValue();
                      inputControl.setValue(value);
                      maybeFireValueChangedEvent(oldValue,value);
                    }
                  }
                }
                catch(Throwable ex) {
                  System.out.println("Couldn't get contents in format: " + DataFlavor.stringFlavor.getHumanPresentableName());
                }
              }
            }
          });
          this.add(pasteMenu);
        } // end if copyAndPasteEnabled

      }
//      if (this.getComponentCount()>0)
//        this.add(new JSeparator());


      if (this.getComponentCount()>0) {
        int xOverflow = x+(int)tablexy.getX()+this.getWidth()-(int)screenSize.getWidth();
        int yOverflow = y+(int)tablexy.getY()+this.getHeight()-(int)screenSize.getHeight();
        int popupX = xOverflow>0?x-xOverflow-20:x;
        int popupY = yOverflow>0?y-yOverflow-20:y;
        this.show(((JComponent)inputControl),popupX,popupY);
      }
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(),"showPopupMenu","Error while constructing this menu.",ex);
      ex.printStackTrace();
    }
  }

  /**
   * mouseClicked
   *
   * @param e MouseEvent
   */
  public void mouseClicked(MouseEvent e) {
    if (SwingUtilities.isRightMouseButton(e)) {
      showPopupMenu(e.getX(),e.getY());
    }
    else if (inputControl.isEnabled()) {
      ((JComponent)inputControl).requestFocus();
    }
  }

  /**
   * mouseEntered
   *
   * @param e MouseEvent
   */
  public void mouseEntered(MouseEvent e) {
  }

  /**
   * mouseExited
   *
   * @param e MouseEvent
   */
  public void mouseExited(MouseEvent e) {
  }

  /**
   * mousePressed
   *
   * @param e MouseEvent
   */
  public void mousePressed(MouseEvent e) {
  }

  /**
   * mouseReleased
   *
   * @param e MouseEvent
   */
  public void mouseReleased(MouseEvent e) {
  }



}
