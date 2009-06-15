package org.openswing.swing.client;

import java.awt.*;
import org.openswing.swing.client.*;
import javax.swing.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.ValueObject;
import java.util.Collection;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.logger.client.Logger;
import java.util.ArrayList;
import org.openswing.swing.util.client.ClientSettings;

/**
 * <p>Title: OpenSwing Framework</p>
* <p>Description: Panel that contains a lookup that allows to select more than one code and stores
* a list of codes, in order to filter data according to a list of codes.</p>
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
public class MultiCodLookupControl extends JPanel {

  /** lookup control */
  private CodLookupControl lookupControl = new CodLookupControl();

  /** list of ValueObjects related to selected codes */
  private ArrayList selectedVOs = new ArrayList();

  /** clear button */
  private JButton clearButton = new JButton() {

    public void paint(Graphics g) {
      super.paint(g);
      int width = g.getFontMetrics().stringWidth("C");
      g.drawString("C",(this.getWidth()-width+1)/2, this.getHeight()/2+4);
    }

  };

  /** JList model */
  private DefaultListModel model = new DefaultListModel();

  /** JList that contains selected codes and other info */
  private JList codesList = new JList(model);

  /** lookup controller */
  private LookupController controller = null;

  /** list of lookup v.o. attributes, whose values will be showed within the JList */
  private String[] lookupAttributeNames = null;

  /** separator to use within the JList to separate tokens */
  private String sep = null;

  private JScrollPane scrollPaneList = new JScrollPane();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();


  /**
   * Void constructor, used when adding this control from UI designer:
   * remember to invoke setController too.
   */
  public MultiCodLookupControl() {
    this(new LookupController(),new String[]{""}," - ");
  }


  /**
   * Constructor used when the JList has to contain only codes (and no other info).
   * @param controller LookupController
   * @param lookupAttributeName lookup v.o. attribute to map into the JList
   */
  public MultiCodLookupControl(LookupController controller,String lookupAttributeName) {
    this(controller,new String[]{lookupAttributeName}," - ");
  }


  /**
   * Constructor used when the JList has to contain more info, such as code, description, etc.
   * @param controller LookupController
   * @param lookupAttributeNames list of lookup v.o. attributes, whose values will be showed within the JList
   * @param sep separator to use within the JList to separate tokens
   */
  public MultiCodLookupControl(final LookupController controller,String[] lookupAttributeNames,String sep) {
    this.lookupAttributeNames = lookupAttributeNames;
    this.sep = sep;
    try {
      jbInit();
      setController(controller);

      if (ClientSettings.TEXT_ORIENTATION!=null)
          setComponentOrientation(ClientSettings.TEXT_ORIENTATION);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Set JList height
   * @param height JList height to set
   */
  public final void setListHeight(int height) {
    scrollPaneList.setPreferredSize(new Dimension(100, height));
  }


  /**
   * @return JList height to set
   */
  public final int getListHeight() {
    return scrollPaneList.getPreferredSize().height;
  }


  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    clearButton.setPreferredSize(new Dimension(20, 20));
    clearButton.setMnemonic('C');
    clearButton.setText("");
    clearButton.addActionListener(new MultiCodLookupControl_clearButton_actionAdapter(this));
    scrollPaneList.setPreferredSize(new Dimension(100, 100));
    this.add(lookupControl,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(clearButton,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.add(scrollPaneList,      new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    scrollPaneList.getViewport().add(codesList, null);
  }


  void clearButton_actionPerformed(ActionEvent e) {
    selectedVOs.clear();
    model.removeAllElements();
    codesList.revalidate();
    codesList.repaint();
  }


  /**
   * @return list of ValueObjects, related to selected codes
   */
  public ArrayList getSelectedVOs() {
    return selectedVOs;
  }


  public final CodLookupControl getLookupControl() {
    return lookupControl;
  }


  /**
   * @return LookupController
   */
  public final LookupController getController() {
    return controller;
  }


  /**
   * Set lookup controller
   * @param controller LookupController
   */
  public final void setController(final LookupController controller) {
    this.lookupControl.setLookupController(controller);
    this.controller = controller;
    this.controller.setDisableFrameClosing(true);
    this.controller.addLookupListener(new LookupListener() {

      public void beforeLookupAction(ValueObject parentVO) {
      }

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        try {
          StringBuffer sb = new StringBuffer();
          Object value = null;
          for(int i=0;i<lookupAttributeNames.length;i++) {
            value= ClientUtils.getValue(controller.getLookupVO(),lookupAttributeNames[i]);
            sb.append(value).append(sep);
          }
          sb.delete(sb.length()-sep.length(),sb.length());
          model.addElement(sb.toString());
          selectedVOs.add(controller.getLookupVO());
          codesList.revalidate();
          codesList.repaint();
        }
        catch (Exception ex) {
          Logger.error(this.getClass().getName(),"codeChanged",ex.getMessage(),ex);
        }
      }

      public void codeValidated(boolean validated) {
      }

      public void forceValidate() {
      }

    });
  }


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param o component orientation
   */
  public final void setTextOrientation(ComponentOrientation o) {
    lookupControl.setComponentOrientation(o);
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
    try {
      return lookupControl.getTextOrientation();
    }
    catch (Exception ex) {
      return null;
    }
  }


}

class MultiCodLookupControl_clearButton_actionAdapter implements java.awt.event.ActionListener {
  MultiCodLookupControl adaptee;

  MultiCodLookupControl_clearButton_actionAdapter(MultiCodLookupControl adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.clearButton_actionPerformed(e);
  }
}
