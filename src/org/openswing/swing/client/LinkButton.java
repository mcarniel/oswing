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
import org.openswing.swing.util.client.ClientUtils;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.*;


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
public class LinkButton extends JPanel {

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

  /** attribute name linked to the text of this link (optional), used to bind this link to a Form's value object */
  public String labelAttributeName = null;

  /** attribute name linked to the tooltip text of this link (optional), used to bind this link to a Form's value object */
  public String tooltipAttributeName = null;

  /** attribute name linked to the uri of this link (optional), used to bind this link to a Form's value object; if binded, this is the URI to automatically open when clicking on link */
  public String uriAttributeName = null;

  /** URI to automatically open when clicking on the link (optional) */
  private String uri = null;

  /** old enabled value */
  private boolean oldValue = true;

  /** enabled value */
  private boolean enabled = true;

  /** link button */
  private JLabel linkButton = new JLabel() {

    public final void paint(Graphics g) {
      super.paint(g);
      if (showUnderline) {
        g.setColor(linkButton.getForeground());
        g.drawLine(0,getHeight()-2,getWidth(),getHeight()-2);
      }
    }

  };


  public final void linkClicked() {
    if (!enabled)
      return;
    for(int i=0;i<listeners.size();i++)
      ((ActionListener)listeners.get(i)).actionPerformed(new ActionEvent(
        LinkButton.this,
        ActionEvent.ACTION_PERFORMED,
        ""
      ));
      if (uri!=null && !uri.equals(""))
        ClientUtils.displayURL(uri);
  }


  public LinkButton() {
    setTextAlignment(SwingConstants.LEFT);
    linkButton.setOpaque(false);
    linkButton.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        linkClicked();
      }

      public void mouseEntered(MouseEvent e) {
        if (!enabled)
          return;
        foregroundColorWhenExited = linkButton.getForeground();
        linkButton.setForeground(foregroundColorWhenEntered);
      }

      public void mouseExited(MouseEvent e) {
        if (!enabled)
          return;
        linkButton.setForeground(foregroundColorWhenExited);
      }

    });
    setLabel("text to translate");
//    setLayout(new FlowLayout(FlowLayout.LEFT));
//    add(linkButton,null);

    setLayout(new GridBagLayout());
    add(linkButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
          , GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
          new Insets(0, 0, 0, 0), 0, 0));

    setEnabled(true);
  }


  /**
   * Set label text.
   * @param label text for the label, translated according to internalization settings
   */
  public final void setText(String label) {
    this.label = label;
    linkButton.setText(ClientSettings.getInstance().getResources().getResource(label));
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
    try {
      return linkButton.getFont();
    }
    catch (Exception ex) {
      return super.getFont();
    }
  }


  /**
   * Set the specified font.
   * @param font Font to set
   */
  public final void setFont(Font font) {
    try {
      linkButton.setFont(font);
    }
    catch (Exception ex) {
    }
  }


  /**
   * Set a tooltip text. This text will be translated according to the internationalization settings.
   * @param toolTipText tool tip text entry in the dictionary
   */
  public final void setToolTipText(String toolTipText) {
    this.toolTipText = toolTipText;
    if (!Beans.isDesignTime())
      linkButton.setToolTipText(ClientSettings.getInstance().getResources().getResource(toolTipText));
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
    return linkButton.getHorizontalAlignment();
  }


  /**
   * Set column text horizontal alignement.
   * @param alignment column text horizontal alignement; possible values: "SwingConstants.LEFT", "SwingConstants.RIGHT", "SwingConstants.CENTER".
   */
  public final void setTextAlignment(int alignment) {
    linkButton.setHorizontalAlignment(alignment);
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
   * @param ActionListeners added to the link button
   */
  public final ActionListener[] getActionListeners() {
    return (ActionListener[])listeners.toArray(new ActionListener[listeners.size()]);
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


  /**
   * Bind the link text to the form which contains it and with the specified the attribute.
   * @param labelAttributeName attribute name binded to text of this link
   */
  public final void setLabelAttributeName(String labelAttributeName) {
    this.labelAttributeName = labelAttributeName;
  }


  /**
   * @return attribute name binded to the text of this link
   */
  public final String getLabelAttributeName() {
    return labelAttributeName;
  }


  /**
   * Bind the link tooltip to the form which contains it and with the specified the attribute.
   * @param tooltipAttributeName attribute name binded to tooltip of this link
   */
  public final void setTooltipAttributeName(String tooltipAttributeName) {
    this.tooltipAttributeName = tooltipAttributeName;
  }


  /**
   * @return attribute name binded to the tooltip of this link
   */
  public final String getTooltipAttributeName() {
    return tooltipAttributeName;
  }


  /**
   * @return URI to automatically open when clicking on the link (optional)
   */
  public final String getUri() {
    return uri;
  }


  /**
   * Set the URI to automatically open when clicking on the link (optional).
   * @param uri URI to automatically open when clicking on the link (optional)
   */
  public final void setUri(String uri) {
    this.uri = uri;
  }


  /**
   * @return attribute name linked to the uri of this link (optional); if binded, this is the URI to automatically open when clicking on link
   */
  public final String getUriAttributeName() {
    return uriAttributeName;
  }


  /**
   * Set the attribute name linked to the uri of this link (optional), used to bind this link to a Form's value object.
   * If binded, this is the URI to automatically open when clicking on link.
   * @param uriAttributeName attribute name linked to the uri of this link (optional); if binded, this is the URI to automatically open when clicking on link
   */
  public final void setUriAttributeName(String uriAttributeName) {
    this.uriAttributeName = uriAttributeName;
  }


  /**
   * @return old enabled value
   */
  public final boolean getOldValue() {
    return oldValue;
  }


  /**
   * @return link button current abilitation state
   */
  public final boolean isEnabled() {
    return enabled;
  }


  /**
   * Set abilitation state for this link button.
   * @param enabled abilitation state
   */
  public final void setEnabled(boolean enabled) {
    if (this.enabled && !enabled) {
      foregroundColorWhenExited = linkButton.getForeground();
      linkButton.setForeground(Color.gray);
    }
    else if (!this.enabled && enabled) {
      linkButton.setForeground(foregroundColorWhenExited);
    }

    if (this.enabled==enabled)
      return;

    oldValue = this.enabled || enabled;
    this.enabled = enabled;
  }
  public JLabel getLinkButton() {
    return linkButton;
  }


}
