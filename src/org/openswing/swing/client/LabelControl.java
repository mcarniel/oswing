package org.openswing.swing.client;

import java.beans.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: label whose text is translated according to internalization settings.</p>
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
public class LabelControl extends JLabel {

  /** label not yet translated */
  private String label = null;

  /** tooltip text */
  private String toolTipText = null;


  public LabelControl() {
    setTextAlignment(SwingConstants.LEFT);
    if (ClientSettings.TEXT_ORIENTATION!=null)
        setComponentOrientation(ClientSettings.TEXT_ORIENTATION);
  }


  public LabelControl(String text) {
    setTextAlignment(SwingConstants.LEFT);
    if (ClientSettings.TEXT_ORIENTATION!=null)
        setComponentOrientation(ClientSettings.TEXT_ORIENTATION);
    setText(text);
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


}
