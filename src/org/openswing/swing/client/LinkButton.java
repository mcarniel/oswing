package org.openswing.swing.client;

import org.openswing.swing.util.client.ClientSettings;
import javax.swing.JLabel;
import org.openswing.swing.internationalization.java.*;
import java.awt.Font;
import java.beans.Beans;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: button having a "link like" look 'n feel, whose text is translated according to internalization settings.
 * An ActionListener can be added to this button to listener click events.</p>
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
public class LinkButton extends JLabel {

  /** label not yet translated */
  private String label = null;

  /** tooltip text */
  private String toolTipText = null;

  /** label color to use when the mouse is over the link; default value: Color.BLUE */
  private Color foregroundColorWhenEntered = Color.blue;

  /** label color to use when the mouse is not over the link */
  private Color foregroundColorWhenExited = null;

  /** list of ActionListener objects */
  private ArrayList listeners = new ArrayList();

  /** flag used to define if show an underline; default value: true */
  private boolean showUnderline = true;


  public LinkButton() {
    setTextAlignment(SwingConstants.LEFT);
    setOpaque(false);
    addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        for(int i=0;i<listeners.size();i++)
          ((ActionListener)listeners.get(i)).actionPerformed(new ActionEvent(
            LinkButton.this,
            ActionEvent.ACTION_PERFORMED,
            ""
          ));
      }

      public void mouseEntered(MouseEvent e) {
        foregroundColorWhenExited = getForeground();
        setForeground(foregroundColorWhenEntered);
      }

      public void mouseExited(MouseEvent e) {
        setForeground(foregroundColorWhenExited);
      }

    });
    setLabel("text to translate");
  }


  /**
   * Set label text.
   * @param label text for the label, translated according to internalization settings
   */
  public final void setText(String label) {
    this.label = label;
    super.setText(ClientSettings.getInstance().getResources().getResource(label));
  }


  /**
   * @return label not yet translated
   */
  public final String getLabel() {
    return label;
  }


  /**
   * @return label not yet translated
   */
  public final void setLabel(String label) {
    setText(label);
  }


  /**
   * @return current Font setting
   */
  public final Font getFont() {
    return super.getFont();
  }


  /**
   * Set the specified font.
   * @param font Font to set
   */
  public final void setFont(Font font) {
    super.setFont(font);
  }


  /**
   * Set a tooltip text. This text will be translated according to the internationalization settings.
   * @param toolTipText tool tip text entry in the dictionary
   */
  public final void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
    if (!Beans.isDesignTime())
      super.setToolTipText(ClientSettings.getInstance().getResources().getResource(toolTipText));
  }


  /**
   * @return tool tip text entry in the dictionary
   */
  public final String getToolTipText() {
    return toolTipText;
  }


  /**
   * @return column text horizontal alignment
   */
  public final int getTextAlignment() {
    return super.getHorizontalAlignment();
  }


  /**
   * Set column text horizontal alignement.
   * @param alignment column text horizontal alignement; possible values: "SwingConstants.LEFT", "SwingConstants.RIGHT", "SwingConstants.CENTER".
   */
  public final void setTextAlignment(int alignment) {
    super.setHorizontalAlignment(alignment);
  }


  /**
   * @return label color to use when the mouse is over the link
   */
  public final Color getForegroundColorWhenEntered() {
    return foregroundColorWhenEntered;
  }


  /**
   * Set the label color to use when the mouse is over the link.
   * @param foregroundColorOnOver label color to use when the mouse is over the link
   */
  public final void setForegroundColorWhenEntered(Color foregroundColorWhenEntered) {
    this.foregroundColorWhenEntered = foregroundColorWhenEntered;
  }


  /**
   * Add an ActionListener to the link button.
   * @param listener ActionListener to add to the link button
   */
  public final void addActionListener(ActionListener listener) {
    listeners.add(listener);
  }


  /**
   * Remove an ActionListener currently applied to the link button.
   * @param listener ActionListener to remove
   */
  public final void removeActionListener(ActionListener listener) {
    listeners.remove(listener);
  }


  /**
   * @return boolean define if show an underline
   */
  public final boolean isShowUnderline() {
    return showUnderline;
  }


  /**
   * Define if show an underline.
   * @param showUnderline define if show an underline
   */
  public final void setShowUnderline(boolean showUnderline) {
    this.showUnderline = showUnderline;
  }


  public final void paint(Graphics g) {
    super.paint(g);
    if (showUnderline) {
      g.setColor(getForeground());
      g.drawLine(0,getHeight()-2,getWidth(),getHeight()-2);
    }
  }


}