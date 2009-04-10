package org.openswing.swing.table.editors.client;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.*;

import org.openswing.swing.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.util.client.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column editor used to show an image and select and import an image from file system.</p>
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
public class ImageCellEditor extends AbstractCellEditor implements TableCellEditor {

  /** button inside the editable cell */
  /** selection button; used to select an image file from local file system */
  private JButton selButton = new JButton() {
    public void paint(Graphics g) {
      super.paint(g);
      int width = g.getFontMetrics().stringWidth("...");
      if (isEnabled())
        g.setColor(UIManager.getColor("Button.foreground"));
      else
        g.setColor(UIManager.getColor("Button.disabledForeground"));
      g.drawString("...", (this.getWidth()-width+1)/2, this.getHeight()/2+4);
    }
  };

  /** cell content */
  private ImagePanel imagePanel = new ImagePanel();

  private JPanel cell = new JPanel();

  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  /** table */
  private JTable table = null;

  /** current row index */
  private int row = -1;

  /** current column index */
  private int col = -1;


  /**
   * Constructor.
   * @param text button text
   * @param actionListeners list of ActionListeners linked to the button
   */
  public ImageCellEditor(boolean showButton,final FileFilter fileFilter,final ArrayList listeners,final boolean showPreview) {
    cell.setLayout(gridBagLayout1);
    cell.add(imagePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    imagePanel.setScrollBarsPolicy(ImagePanel.SCROLLBAR_NEVER);
    cell.setFocusable(true);

    selButton.setPreferredSize(new Dimension(21, 20));
    selButton.setMinimumSize(new Dimension(21, 20));
    selButton.setMaximumSize(new Dimension(21, 20));
    selButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        JFileChooser f = new JFileChooser();
        f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("image selection"));
        Container c = cell.getParent();
        while(c!=null && !(c instanceof JFrame || c instanceof JInternalFrame))
          c = c.getParent();
        if (c==null)
          c = MDIFrame.getInstance();
        f.setFileSelectionMode(f.FILES_ONLY);

        if (showPreview) {
          final PreviewImage ip = new PreviewImage();
          f.setAccessory(ip);
          ip.setPreferredSize(new Dimension(100,100));
          ip.setBorder(BorderFactory.createEtchedBorder());
          f.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
              try {
                if (evt.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                  ip.setImage((File) evt.getNewValue());
                }
              }
              catch (Exception ex) {
                ex.printStackTrace();
              }
            }

          });
        }


        if (fileFilter!=null)
          f.setFileFilter(fileFilter);
        int res = f.showOpenDialog(c);
        if (res==f.APPROVE_OPTION) {
          // image file selected: it will be set into image panel...
          try {
            imagePanel.setImageAsStream(new FileInputStream(f.getSelectedFile()));
            if (table!=null && table instanceof Grid && !((Grid)table).isRowHeightFixed() && row!=-1 && col!=-1)
              table.setRowHeight(row,imagePanel.getImageHeight());
            if (row!=-1 && col!=-1) {
              table.getModel().setValueAt(imagePanel.getImage(),row,col);
            }
            for(int i=0;i<listeners.size();i++)
              ((ActionListener)listeners.get(i)).actionPerformed(new ActionEvent(ImageCellEditor.this,e.getID(),f.getSelectedFile().getAbsolutePath()));

          }
          catch (Exception ex) {
            Logger.error("org.openswing.swing.client.ImageControl","actionPerformed",ex.getMessage(),ex);
            OptionPane.showMessageDialog(c,"Error",ex.getMessage(),JOptionPane.ERROR_MESSAGE);
          }
        }
      }

    });

    if (showButton)
      cell.add(selButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

  }


  /**
   * Stop cell editing. This method stops cell editing (effectively committing the edit) only if the data entered is validated successfully.
   * @return <code>true</code> if cell editing may stop, and <code>false</code> otherwise.
   */
  public final boolean stopCellEditing() {
    return true;
  }


  public final Object getCellEditorValue() {
    return imagePanel.getImage();
  }


  public final Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column) {
    this.table = table;
    this.row = row;
    this.col = column;
    imagePanel.setImage((byte[])value);

    if (table instanceof Grid && !((Grid)table).isCellEditable(row,column))
      return imagePanel;
    else
      return cell;
  }


  public final void finalize() {
    table = null;
  }



  class PreviewImage extends JLabel {

    public PreviewImage() {
      setPreferredSize(new Dimension(100,100));
      setBorder(BorderFactory.createEtchedBorder());
    }


    public void setImage(File f) {
      ImageIcon icon = new ImageIcon(f.getPath());
      if (icon.getIconWidth()>getWidth())
        icon = new ImageIcon(icon.getImage().getScaledInstance(getWidth(),-1,Image.SCALE_DEFAULT));
      setIcon(icon);
      repaint();
    }


  }


}


