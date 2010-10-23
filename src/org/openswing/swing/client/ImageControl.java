package org.openswing.swing.client;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Input Control used to show an image or to set an image.</p>
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
public class ImageControl extends BaseInputControl implements InputControl {

  /** image panel */
  private ImagePanel imagePanel = new ImagePanel();

  /** flag used to define if an image selection button must be showed; default value: <code>true</code> */
  private boolean showButton = true;

  /** flag used to define if the image panel could auto-resize or it must autosize to width x height */
  private boolean autoResize = false;

  /** image panel width */
  private int imageWidth = 0;

  /** image panel height */
  private int imageHeight = 0;

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


  /** file filter used to filter image file selection from select button; default value: jpg and gif files only */
  private FileFilter fileFilter = new FileFilter() {

    /**
     * Whether the given file is accepted by this filter.
     */
    public boolean accept(File f) {
      return
        f.getName().toLowerCase().endsWith(".gif") ||
        f.getName().toLowerCase().endsWith(".jpg") ||
        f.isDirectory();
    }

    /**
     * The description of this filter. For example: "JPG and GIF Images"
     * @see FileView#getName
     */
    public String getDescription() {
      return "JPEG/GIF image (*.jpg; *.gif)";
    }

  };

  /** list of ActionListener objects added to selection button */
  private ArrayList listeners = new ArrayList();

  /** flag used to show the preview of the image in ImageControl and Image Column components; default value: <code>ClientSettings.SHOW_PREVIEW_OF_IMAGE</code> */
  public static boolean showPreview = ClientSettings.SHOW_PREVIEW_OF_IMAGE;


  GridBagLayout gridBagLayout1 = new GridBagLayout();



  public ImageControl() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }

    initListeners();
  }


  /**
   * @return define if an image selection button must be showed
   */
  public final boolean isShowButton() {
    return showButton;
  }


  /**
   * Define if an image selection button must be showed.
   * @param showButton define if an image selection button must be showed
   */
  public final void setShowButton(boolean showButton) {
    this.showButton = showButton;
    if (showButton) {
      this.remove(selButton);
      this.add(selButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    }
    else
      this.remove(selButton);
    this.revalidate();
    this.repaint();
  }


  /**
   * @return image panel width
   */
  public final int getImageWidth() {
    return imageWidth;
  }


  /**
   * Set image panel width.
   * @param width image panel width
   */
  public final void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
    if (!autoResize && imageWidth>0 && imageWidth>0) {
      forceImageDimensions();
    }
  }


  /**
   * @return image panel height
   */
  public final int getImageHeight() {
    return imageHeight;
  }


  /**
   * Set image panel height.
   * @param height image panel height
   */
  public final void setImageHeight(int imageHeight) {
    this.imageHeight = imageHeight;
    if (!autoResize && imageHeight>0 && imageHeight>0) {
      forceImageDimensions();
    }
  }


  /**
   * Define if the image panel could auto-resize or it must autosize to width x height.
   * @param autoResize <code>true</code> if width and height must be ignored and image panel could autoresize; <code>false</code> to fix the dimension at width x height values (if they are not equal to 0)
   */
  public final void setAutoResize(boolean autoResize) {
    this.autoResize = autoResize;
    if (!autoResize && imageWidth>0 && imageHeight>0) {
      forceImageDimensions();
    }
  }


  private void forceImageDimensions() {
    imagePanel.setMinimumSize(new Dimension(imageWidth,imageHeight));
    imagePanel.setMaximumSize(new Dimension(imageWidth,imageHeight));
    imagePanel.setPreferredSize(new Dimension(imageWidth,imageHeight));

    setMinimumSize(new Dimension(imageWidth,imageHeight));
    setMaximumSize(new Dimension(imageWidth,imageHeight));
    setPreferredSize(new Dimension(imageWidth,imageHeight));
  }


  /**
   * @return define if the image panel could auto-resize or it must autosize to width x height
   */
  public final boolean isAutoResize() {
    return autoResize;
  }


  /**
   * @return component inside this whose contains the value
   */
  public final JComponent getBindingComponent() {
    return selButton;
  }


  /**
   * @return file filter used to filter image file selection from select button
   */
  public final FileFilter getFileFilter() {
    return fileFilter;
  }


  /**
   * Set the file filter used to filter image file selection from select button.
   * Default value: jpg and gif files only.
   * @param fileFilter file filter used to filter image file selection from select button
   */
  public final void setFileFilter(FileFilter fileFilter) {
    this.fileFilter = fileFilter;
  }


  /**
   * @return byte[] related to the image currentlt showed
   */
  public final Object getValue() {
    return getImage();
  }


  /**
   * @param value number to set
   */
  public final void setValue(Object value) {
    try {
      setImage((byte[])value);
      if (autoResize) {
        imageWidth = imagePanel.getImageWidth();
        imageHeight = imagePanel.getImageHeight();
        forceImageDimensions();
      }
    }
    catch (Exception ex) {
      setImage((Image)null);
      Logger.error(this.getClass().getName(),"setValue",ex.getMessage(),ex);
    }
  }


  /**
   * Set the image from a byte[].
   * @param image image to set
   */
  public final byte[] getImage() {
    return imagePanel.getImage();
  }


  /**
   * Set the image from a byte[].
   * @param image image to set
   */
  public final void setImage(byte[] image) {
    imagePanel.setImage(image);
    if (autoResize) {
      imageWidth = imagePanel.getImageWidth();
      imageHeight = imagePanel.getImageHeight();
      forceImageDimensions();
    }
  }


  /**
   * Set the image from a byte[].
   * @param image image to set
   */
  public final void setImage(Image image) {
    imagePanel.setImage(image);
    if (autoResize) {
      imageWidth = imagePanel.getImageWidth();
      imageHeight = imagePanel.getImageHeight();
      forceImageDimensions();
    }
  }


  /**
   * Replace enabled setting with editable setting (this allow tab swithing).
   * @param enabled flag used to set abilitation of control
   */
  public void setEnabled(boolean enabled) {
    selButton.setEnabled(enabled);
  }


  /**
   * @return current input control abilitation
   */
  public final boolean isEnabled() {
    try {
      return selButton.isEnabled();
    }
    catch (Exception ex) {
      return false;
    }
  }


  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    this.add(imagePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    selButton.setPreferredSize(new Dimension(21, 20));
    selButton.setMinimumSize(new Dimension(21, 20));
    selButton.setMaximumSize(new Dimension(21, 20));
    selButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        JFileChooser f = new JFileChooser();
        f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("image selection"));
        Container c = ImageControl.this.getParent();
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
            for(int i=0;i<listeners.size();i++)
              ((ActionListener)listeners.get(i)).actionPerformed(new ActionEvent(ImageControl.this,e.getID(),f.getSelectedFile().getAbsolutePath()));

          }
          catch (Exception ex) {
            Logger.error("org.openswing.swing.client.ImageControl","actionPerformed",ex.getMessage(),ex);
            OptionPane.showMessageDialog(c,"Error",ex.getMessage(),JOptionPane.ERROR_MESSAGE);
          }
        }
      }

    });


    this.add(selButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));

  }


  /**
   * Add an action listener to the selection button.
   * When the user has selected an image from file system the actionPerfomed method is invoked; the command value contains the image file name.
   * @param listener listener to add
   */
  public final void addActionListener(ActionListener listener) {
    listeners.add(listener);
  }


  /**
   * Remove an action listener from the selection button.
   * @param listener listener to remove
   */
  public final void removeActionListener(ActionListener listener) {
    listeners.remove(listener);
  }


  /**
   * Determines when the scrollbars appears in the scrollpane.
   * Legal values are:
   * <ul>
   * <li>SCROLLBAR_AS_NEEDED
   * <li>SCROLLBAR_NEVER
   * <li>SCROLLBAR_ALWAYS
   * </ul>
   */
  public final void setScrollBarsPolicy(int policy) {
    imagePanel.setScrollBarsPolicy(policy);
  }


  /**
   * @return scrollbars policy; possible values: ImagePanel.SCROLLBAR_AS_NEEDED, ImagePanel.SCROLLBAR_NEVER, ImagePanel.SCROLLBAR_ALWAYS
   */
  public final int getScrollBarsPolicy() {
    return imagePanel.getScrollBarsPolicy();
  }


  /**
   * @return show the preview of the image in ImageControl and Image Column components; default value: <code>ClientSettings.SHOW_PREVIEW_OF_IMAGE</code>
   */
  public final boolean isShowPreview() {
    return showPreview;
  }


  /**
   * Define if showing the preview of the image in ImageControl and Image Column components; default value: <code>ClientSettings.SHOW_PREVIEW_OF_IMAGE</code>
   * @param showPreview show/hide the preview of the image in ImageControl and Image Column components
   */
  public final void setShowPreview(boolean showPreview) {
    this.showPreview = showPreview;
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

      if (autoResize) {
        imageWidth = icon.getIconWidth();
        imageHeight = icon.getIconHeight();
        forceImageDimensions();
      }
      repaint();
    }


  }


}


