package org.openswing.swing.items.client;

import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Renderer for a ComboBoxVOControl or ListVOControl.</p>
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
public class ItemRenderer extends JPanel implements ListCellRenderer, Serializable {

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    /** columns associated to lookup grid */
    private Column[] colProperties = null;

    /** collection of pairs <v.o. attribute name,Method object, related to the attribute getter method> */
    private Hashtable getters = new Hashtable();

    /** current vo */
    private Object vo = null;

    private SimpleDateFormat sdf = null;

    /** component left margin, with respect to component container */
    private int leftMargin = 0;

    /** component right margin, with respect to component container */
    private int rightMargin = 0;

    /** component top margin, with respect to component container */
    private int topMargin = 0;

    /** component bottom margin, with respect to component container */
    private int bottomMargin = 0;


    public ItemRenderer() {
      super();
      setOpaque(true);
      setBorder(noFocusBorder);
      sdf = new SimpleDateFormat(ClientSettings.getInstance().getResources().getDateMask(Consts.TYPE_DATE));
    }


    public void init(Hashtable getters,Column[] colProperties,int leftMargin,int rightMargin,int topMargin,int bottomMargin) {
        this.getters = getters;
        this.colProperties = colProperties;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
    }


    public Component getListCellRendererComponent(
                                                 JList list,
                                                 Object value,
                                                 int index,
                                                 boolean isSelected,
                                                 boolean cellHasFocus)
    {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setFont(list.getFont());
        this.vo = value;
        repaint();
        return this;
    }


    public Dimension getPreferredSize() {
      Dimension size = new JLabel(" ").getPreferredSize();
      if (colProperties!=null) {
        int width = 0;
        for(int i=0;i<colProperties.length;i++) {
          if (colProperties[i].isColumnVisible())
            width += colProperties[i].getPreferredWidth() + 6;
        }
        size = new Dimension(width,(int)size.getHeight());
      }
      return size;
    }


    public void paint(Graphics g) {
      super.paint(g);

      if (vo!=null && getters!=null && colProperties!=null) {
        int x = 0;
        Object obj = null;
        Object val = null;
        String valS = null;
        Color col = g.getColor();
        Color col2 = Color.gray;
        for(int i=0;i<colProperties.length;i++) {
          if (colProperties[i].isColumnVisible()) {
            try {
              if (colProperties[i].getColumnName().indexOf('.') > 0) {
                  String hostProperty = colProperties[i].getColumnName().substring(0, colProperties[i].getColumnName().indexOf('.'));
                  obj = vo.getClass().getMethod("get"+hostProperty.substring(0,1).toUpperCase()+hostProperty.substring(1), new Class[0]).invoke(vo, new Object[0]);
              } else {
                  obj = vo;
              }
              val = ( (Method) getters.get(colProperties[i].getColumnName())).invoke(obj, new Object[0]);
              if (val!=null) {
                if (colProperties[i] instanceof DateColumn) {
                  valS = sdf.format((java.util.Date)val);
                }
                else
                  valS = val.toString();
              }
              else
                valS = null;
              if (x != 0) {
                g.setColor(col2);
                g.drawLine(x - 3, 0, x - 3, getHeight());
              }
              if (valS != null) {
                g.setColor(col);
//                g.drawString(valS, x, getHeight()-g.getFontMetrics().getDescent());

                int hOffset= Math.max(0,( getHeight()+1 - g.getFontMetrics().getHeight() )/2);
                g.drawString(valS, x+leftMargin, getHeight()+topMargin - g.getFontMetrics().getDescent() - hOffset);
              }
              x += colProperties[i].getPreferredWidth() + 6;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
          }
        }
        g.setColor(col);
      }

    }


    /**
     * A subclass of ItemRenderer that implements UIResource.
     * ItemRenderer doesn't implement UIResource
     * directly so that applications can safely override the
     * cellRenderer property with ItemRenderer subclasses.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases. The current serialization support is
     * appropriate for short term storage or RMI between applications running
     * the same version of Swing.  As of 1.4, support for long term storage
     * of all JavaBeans<sup><font size="-2">TM</font></sup>
     * has been added to the <code>java.beans</code> package.
     * Please see {@link java.beans.XMLEncoder}.
     */
    public static class UIResource extends ItemRenderer implements javax.swing.plaf.UIResource {
    }
}


