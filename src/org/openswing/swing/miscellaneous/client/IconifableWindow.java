package org.openswing.swing.miscellaneous.client;

import javax.swing.*;
import java.awt.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.client.MultiLineLabelControl;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.openswing.swing.client.LabelControl;
import org.openswing.swing.util.client.ClientUtils;
import javax.swing.JLayeredPane;
import java.applet.Applet;
import org.openswing.swing.client.ImagePanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.beans.Beans;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: this class allows to show an iconifable window, i.e. a window that can contains any kind of graphics component within it,
 * since it inherits from JPanel. As default behavior, it is composed of a top panel and a main panel:
 * - top panel may contain: an optional title icon, a title (showed in bold style) and two buttons: a "reduce to icon" button and a "close" button, both can be hidden
 * - main panel is this, i.e. any content manually added to this is showed inside this main panel.
 * Window can be reduced to icon by pressing the "reduce to icon" button or by double clicking inside the top panel (if no "reduce to icon" button is visible).
 * Window can be closed by pressing the "close" button or by clicking inside the main panel (if no "close" button is visible).
 * As default settings, this panel has a dimension of 300 x 150 pixels.
 * Window location can be defined in several ways:
 * - using absolute location, by using this.setLocation method
 * - by anchoring the window to the Window.TOP/Window.BOTTOM of another component, through setAnchorWindow() method.</p>
 * Several events fired by this window can be listened, through the method: addIconifableWindowListener.
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
public class IconifableWindow extends JPanel {

  /** window */
  private JWindow window;

  /** title to show inside this panel */
  private LabelControl title = new LabelControl();

  /** anchor the window to the top of the specified component */
  public static final int TOP = 0;

  /** anchor the window to the bottom of the specified component */
  public static final int BOTTOM = 1;

  /** component to anchor to */
  private JComponent anchorComponent;

  /** anchor type: TOP or BOTTOM */
  protected int constraint;

  /** window dimension */
  protected Dimension dim;

  /** define if a close button must be showed (<code>false</code> = window will be closed by simply click inside it); default value: <code>false</code> */
  private boolean showCloseButton;

  /** define if a reduce to icon button must be showed (<code>true</code> means that the iconified window will be enlarged by clicking on it); default value: <code>false</code> */
  private boolean showReduceToIconButton;

  /** panel that contains the title and "close" and "reduce to icon" buttons */
  private JPanel topPanel = new JPanel();

  /** close window button */
  private ImagePanel closeButton = new ImagePanel();

  /** reduce window to icon button */
  private ImagePanel reduceToIconButton = new ImagePanel();

  /** restore window to original dimension button */
  private ImagePanel restoreButton = new ImagePanel();

  /** last window dimension (before reducing it to icon) */
  private Dimension lastWindowDimension = null;

  /** window height, when it is iconified */
  private int iconHeight = 20;

  /** listeners of type IconifableWindowListener */
  private ArrayList iconifableWindowListeners = new ArrayList();

  /** title image name (optional), to show at the left of the title */
  private String titleImageName;

  /** title image */
  private ImagePanel titleImage = new ImagePanel();


  public IconifableWindow() {
    title.setFont(new Font(title.getFont().getName(),Font.BOLD,title.getFont().getSize()));
    titleImage.setOpaque(false);
    titleImage.setBorder(BorderFactory.createEmptyBorder());
    topPanel.setOpaque(false);
    topPanel.setLayout(new GridBagLayout());

    closeButton.setImageName("closealert.gif");
    closeButton.setOpaque(false);
    closeButton.setBorder(BorderFactory.createEmptyBorder());
    closeButton.setScrollBarsPolicy(closeButton.SCROLLBAR_NEVER);
    closeButton.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        hideWindow();
      }

    });

    restoreButton.setImageName("maximizealert.gif");
    restoreButton.setOpaque(false);
    restoreButton.setBorder(BorderFactory.createEmptyBorder());
    restoreButton.setScrollBarsPolicy(restoreButton.SCROLLBAR_NEVER);
    restoreButton.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        setupTopPanel(false);
        window.setLocation(window.getLocation().x,window.getLocation().y-lastWindowDimension.height+iconHeight);
        window.setSize(lastWindowDimension);
      }

    });

    reduceToIconButton.setImageName("reducealert.gif");
    reduceToIconButton.setOpaque(false);
    reduceToIconButton.setBorder(BorderFactory.createEmptyBorder());
    reduceToIconButton.setScrollBarsPolicy(reduceToIconButton.SCROLLBAR_NEVER);
    reduceToIconButton.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        window.setSize(lastWindowDimension.width,iconHeight);
        window.setLocation(window.getLocation().x,window.getLocation().y+lastWindowDimension.height-iconHeight);
        setupTopPanel(true);
      }

    });

    super.setSize(300,150);
    super.setFocusable(false);
    dim = new Dimension(300,150);
    setBackground(UIManager.getColor("Label.background"));
    super.setPreferredSize(new Dimension(300,150));
    addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && !showCloseButton) {
          hideWindow();
        }
      }

    });

  }


  /**
   * Method invoked by "reduce to icon" or "restore" buttons to prepare top panel content, according to window settings.
   * @param iconifyWindow <code>true</code> to insert the "restore" button inside the top panel, <code>false</code> to insert the "reduce to icon" button inside the top panel
   */
  protected final void setupTopPanel(boolean windowIconified) {
    topPanel.removeAll();
    if (titleImageName!=null && !titleImageName.equals(""))
      topPanel.add(titleImage,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
           ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
    if (title.getLabel()!=null && !title.getLabel().equals(""))
      topPanel.add(title,     new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
           ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2,2,2,2), 0, 0));

    if (windowIconified) {
      if (showReduceToIconButton)
        topPanel.add(restoreButton,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
             ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
      if (showCloseButton)
        topPanel.add(closeButton,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
             ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
    }
    else {
      if (showReduceToIconButton)
        topPanel.add(reduceToIconButton,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
             ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
      if (showCloseButton)
        topPanel.add(closeButton,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
             ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2,2,2,2), 0, 0));
    }

    topPanel.revalidate();
    topPanel.repaint();
  }


  /**
   * Show a window that contains this.
   */
  public final void showWindow() {
    closeButton.setBackground(this.getBackground());
    reduceToIconButton.setBackground(this.getBackground());
    restoreButton.setBackground(this.getBackground());

    if (window!=null) {
      window.getContentPane().removeAll();
      window.setVisible(false);
      window.dispose();
    }

    if (anchorComponent!=null) {
      Component topLevelAncestor = getTopLevelAncestor(anchorComponent);
      if (topLevelAncestor instanceof Frame) {
        window = new JWindow((Frame)topLevelAncestor);
      }
      else if (topLevelAncestor instanceof Window) {
        window = new JWindow((Window)topLevelAncestor);
      }
      else {
        Frame frame = ClientUtils.getParentFrame((JComponent)topLevelAncestor);
        window = new JWindow((Frame)frame);
      }
    }
    else
      window = new JWindow();

    window.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && !showCloseButton) {
          hideWindow();
        }
      }

    });

    if (dim!=null) {
      window.setSize(dim);
    }

    if (anchorComponent!=null && (constraint==TOP || constraint==BOTTOM)) {
      Point location = anchorComponent.getLocationOnScreen();
      if (constraint==TOP)
        location.y = location.y-dim.height-1;
      else
        location.y = location.y+dim.height+1;
      if (location.x<0)
        location.x = 0;
      if (location.y<0)
        location.y = 0;
      setLocation(location);
    }
    if (this.getLocation()!=null)
      window.setLocation(this.getLocation());

    // set window content...
    ((JComponent)window.getContentPane()).setBorder(BorderFactory.createLineBorder(Color.lightGray));
    window.getContentPane().setLayout(new BorderLayout());
    window.getContentPane().setBackground(this.getBackground());
    window.getContentPane().add(this,BorderLayout.CENTER);

    // add top panel to the window...
    setupTopPanel(false);
    window.getContentPane().add(topPanel, BorderLayout.NORTH);

    lastWindowDimension = getWindow().getSize();

    fireWindowEvent(IconifableWindowListener.WINDOW_CREATED);
    showWindowInternally();

    window.toFront();
  }



  /**
   * This method shows the window.
   */
  protected void showWindowInternally() {
    window.setVisible(true);
    fireWindowEvent(IconifableWindowListener.WINDOW_SHOWED);
  }


  /**
   * Manually hide the window.
   */
  public final void hideWindow() {
    if (window!=null) {
      window.getContentPane().removeAll();
      window.setVisible(false);
      window.dispose();
      fireWindowEvent(IconifableWindowListener.WINDOW_CLOSED);
    }
  }


  /**
   * Anchor the window to the specified component.
   * @param component component used to calculate window location
   * @param constraint
   */
  public final void anchorWindow(JComponent anchorComponent,int constraint) {
    this.anchorComponent = anchorComponent;
    this.constraint = constraint;
  }


  /**
   * Sets the preferred size of this component.
   * If <code>preferredSize</code> is <code>null</code>, the UI will
   * be asked for the preferred size.
   * @beaninfo
   *   preferred: true
   *       bound: true
   * description: The preferred size of the component.
   */
  public final void setPreferredSize(Dimension preferredSize) {
    this.dim = preferredSize;
  }


  /**
   * Resizes this component so that it has width <code>d.width</code>
   * and height <code>d.height</code>.
   * @param d the dimension specifying the new size
   * 		of this component
   * @see #setSize
   * @see #setBounds
   * @since JDK1.1
   */
  public final void setSize(Dimension d) {
    this.dim = d;
  }


  /**
   * Resizes this component so that it has width <code>d.width</code>
   * and height <code>d.height</code>.
   * @param d the dimension specifying the new size
   * 		of this component
   * @see #setSize
   * @see #setBounds
   * @since JDK1.1
   */
  public final void setSize(int width,int height) {
    this.dim = new Dimension(width,height);
  }


  /**
   * @return define if a close button must be showed (<code>false</code> = window will be closed by simply click inside it)
   */
  public final boolean isShowCloseButton() {
    return showCloseButton;
  }


  /**
   * Define if a close button must be showed (<code>false</code> = window will be closed by simply click inside it); default value: <code>false</code>.
   * @param showCloseButton define if a close button must be showed (<code>false</code> = window will be closed by simply click inside it)
   */
  public final void setShowCloseButton(boolean showCloseButton) {
    this.showCloseButton = showCloseButton;
  }


  /**
   * @return define if a reduce to icon button must be showed (<code>true</code> means that the iconified window will be enlarged by clicking on it)
   */
  public final boolean isShowReduceToIconButton() {
    return showReduceToIconButton;
  }


  /**
   * Define if a reduce to icon button must be showed (<code>true</code> means that the iconified window will be enlarged by clicking on it); default value: <code>false</code>.
   * @param showReduceToIconButton define if a reduce to icon button must be showed (<code>true</code> means that the iconified window will be enlarged by clicking on it)
   */
  public final void setShowReduceToIconButton(boolean showReduceToIconButton) {
    this.showReduceToIconButton = showReduceToIconButton;
  }


  /**
   * @param component component to analyze
   * @return parent component (Window or Frame)
   */
  private final Component getTopLevelAncestor(Component component) {
    if (component == null)
      return null;
    for (Component p = component; p != null; p = p.getParent())
      if (p instanceof Window || p instanceof Applet)
        return p;
    return null;
  }


  /**
   * Set the title text to show inside this panel.
   * @param title text to show inside this panel; it will be translated, according to language settings
   */
  public final void setTitle(String title) {
    this.title.setLabel(title);
  }


  /**
   * @return title text to show inside this panel
   */
  public final String getTitle() {
    return this.title.getLabel();
  }



  /**
   * @return window height, when it is iconified
   */
  public final int getIconHeight() {
    return iconHeight;
  }


  /**
   * Set window height, when it is iconified.
   * @param iconHeight window height, when it is iconified
   */
  public final void setIconHeight(int iconHeight) {
    this.iconHeight = iconHeight;
  }


  /**
   * Add an IconifableWindowListener to this window.
   * @param listener listener of events fired by this window
   */
  public final void addIconifableWindowListener(IconifableWindowListener listener) {
    iconifableWindowListeners.add(listener);
  }


  /**
   * Remove an IconifableWindowListener to this window.
   * @param listener listener of events fired by this window that must be removed
   */
  public final void removeIconifableWindowListener(IconifableWindowListener listener) {
    iconifableWindowListeners.remove(listener);
  }


  /**
   * @return title image name (optional)
   */
  public final String getTitleImageName() {
    return titleImageName;
  }


  /**
   * Set the title image name (optional), to show at the left of the title.
   * @param titleImageName title image name (optional)
   */
  public final void setTitleImageName(String titleImageName) {
    this.titleImageName = titleImageName;
    if (!Beans.isDesignTime()) {
      titleImage.setImageName(titleImageName);
    }
  }


  /**
   * Fire the specified window event.
   * @param event window event to fire
   */
  protected final void fireWindowEvent(int event) {
    for(int i=0;i<iconifableWindowListeners.size();i++)
      ((IconifableWindowListener)iconifableWindowListeners.get(i)).windowEvent(event);
  }


  /**
   * @return iconifable window; null if showWindow() method has not been yet called
   */
  public final JWindow getWindow() {
    return window;
  }



}
