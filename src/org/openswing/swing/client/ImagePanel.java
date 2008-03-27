package org.openswing.swing.client;

import java.beans.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel based on a JScrollPane that can shows an image.
 * Image can be retrieved by "images" subfolder, by specifying and image name or
 * by passing a byte[] argument.</p>
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
public class ImagePanel extends JPanel {

  private InnerImagePanel imagePanel = new InnerImagePanel();

  /** image, expressed as byte[] */
  private byte[] image = null;

  /** image file name */
  private String imageName = null;

  private BorderLayout borderLayout1 = new BorderLayout();

  private JScrollPane sp = new JScrollPane(imagePanel);

  public static final int SCROLLBAR_AS_NEEDED = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
  public static final int SCROLLBAR_NEVER = JScrollPane.VERTICAL_SCROLLBAR_NEVER;
  public static final int SCROLLBAR_ALWAYS = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;

  /** tooltip text */
  private String toolTipText = null;


  public ImagePanel() {
    try {
      imagePanel.setOpaque(false);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * @return image height
   */
  public final int getImageHeight() {
    return imagePanel.getHeight();
  }


  /**
   * @return image width
   */
  public final int getImageWidth() {
    return imagePanel.getWidth();
  }


  /**
   * Set the image from a byte[].
   * @param image image to set
   */
  public final void setImage(byte[] image) {
    if (image!=null)
      imagePanel.setImage(new ImageIcon(image).getImage());
    else
      imagePanel.setImage(null);
    this.image = image;
  }


  /**
   * Set the image.
   * @param image image to set
   */
  public final void setImage(Image image) {
    if (image!=null)
      imagePanel.setImage(image);
    else
      imagePanel.setImage(null);
  }


  /**
   * Set the image from an InputStream.
   * The InputStream will be automatically close after reading it.
   * @param in image to set, expressed as an InputStream
   */
  public final void setImageAsStream(InputStream in) throws IOException {
    byte[] aux = new byte[1024];
    byte[] aux2 = new byte[0];
    byte[] image = new byte[0];
    int len = 0;
    while((len=in.read(aux))>0) {
      aux2 = new byte[image.length+len];
      System.arraycopy(image,0,aux2,0,image.length);
      System.arraycopy(aux,0,aux2,image.length,len);
      image = aux2;
    }
    in.close();
    imagePanel.setImage(new ImageIcon(image).getImage());
    this.image = image;
  }


  /**
   * Set the image from a byte[].
   * @param image image to set
   */
  public final byte[] getImage() {
    if (image==null && imagePanel.getImage()!=null) {
      PixelGrabber pg = new PixelGrabber(imagePanel.getImage(),0,0,imagePanel.getImage().getWidth(imagePanel),imagePanel.getImage().getHeight(imagePanel),true);
      image = (byte[])pg.getPixels();
    }
    return image;
  }


  /**
   * Set the image from a file name. The specified file name must reside in "images" subfolder.
   * @param imageName file nome used to retrieve the image
   */
  public final void setImageName(String imageName) {
    if (imageName!=null) {
      Image img = ClientUtils.getImage(imageName);
      imagePanel.setImage(img);
    }
    else
      imagePanel.setImage(null);
    this.imageName = imageName;
  }


  /**
   * Set a tooltip text. This text will be translated according to the internationalization settings.
   * @param toolTipText tool tip text entry in the dictionary
   */
  public final void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
    if (!Beans.isDesignTime())
      imagePanel.setToolTipText(ClientSettings.getInstance().getResources().getResource(toolTipText));
  }


  /**
   * @return tool tip text entry in the dictionary
   */
  public final String getToolTipText() {
    return toolTipText;
  }


  /**
   * Set the image from a file name. The specified file name must reside in "images" subfolder.
   * @param imageName file nome used to retrieve the image
   */
  public final String getImageName() {
    return imageName;
  }


  private void jbInit() throws Exception {
//    this.setBorder(BorderFactory.createLoweredBevelBorder());
    this.setLayout(borderLayout1);
    this.add(sp,BorderLayout.CENTER);
    sp.getViewport().add(imagePanel);
    sp.getViewport().setOpaque(false);
    sp.setOpaque(false);
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
    if (policy==SCROLLBAR_NEVER) {
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      sp.setBorder(BorderFactory.createEmptyBorder());
    }
    else if (policy==SCROLLBAR_AS_NEEDED) {
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    else if (policy==SCROLLBAR_ALWAYS) {
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }
  }


  /**
   * @return scrollbars policy; possible values: SCROLLBAR_AS_NEEDED, SCROLLBAR_NEVER, SCROLLBAR_ALWAYS
   */
  public final int getScrollBarsPolicy() {
    return sp.getVerticalScrollBarPolicy();
  }


  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner panel that contains the image.</p>
   */
  class InnerImagePanel extends JPanel {

    /** image to show */
    private Image image = null;


    private Image getImage() {
      return image;
    }


    public void setImage(Image image) {
      this.image = image;
      if (image!=null) {
        setSize(image.getWidth(this),image.getHeight(this));
        setMinimumSize(new Dimension(image.getWidth(this),image.getHeight(this)));
        setPreferredSize(new Dimension(image.getWidth(this),image.getHeight(this)));
      }
      revalidate();
      repaint();
    }


    public void paint(Graphics g) {
      super.paint(g);
      if (Beans.isDesignTime())
        return;
      if (image!=null)
        g.drawImage(image,0,0,image.getWidth(this),image.getHeight(this),this);
    }


  }


  public void addMouseListener(MouseListener listener) {
    imagePanel.addMouseListener(listener);
  }


  public void removeMouseListener(MouseListener listener) {
    imagePanel.removeMouseListener(listener);
  }


}
