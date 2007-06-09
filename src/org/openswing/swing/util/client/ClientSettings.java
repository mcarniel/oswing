package org.openswing.swing.util.client;


import org.openswing.swing.domains.java.Domain;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.permissions.java.ButtonsAuthorizations;
import org.openswing.swing.internationalization.java.*;
import java.lang.reflect.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Client settings, used to initialize MDI Frame.</p>
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
public class ClientSettings {

  /** image name for tree menu icons */
  public static String ICON_FILENAME = "appicon.gif";

  /** image name for tree menu background */
  public static String TREE_BACK = "treeback.jpg";

  /** image name for opened padlock icon */
  public static String LOCK_ON = "lock_on.gif";

  /** image name for closed padlock icon */
  public static String LOCK_OFF = "lock_off.gif";

  /** image name for tree menu icons */
  public static String PERC_TREE_NODE = "node.gif";

  /** image name for tree menu icons */
  public static String PERC_TREE_FOLDER = "folder.gif";

  /** image name for MDI background */
  public static String BACKGROUND = "background.jpg";

  /** image name for calendar button in date input control */
  public static String CALENDAR = "calendar.gif";

  /** divider width of MDI Frame splitter */
  public static int DIVIDER_WIDTH = 15;

  /** width of MDI Frame menu window */
  public static int MENU_WIDTH = 200;

  /** maximum width of MDI Frame menu window */
  public static int MAX_MENU_WIDTH = 300;

  /** foreground color of grid active cell */
  public static Color GRID_ACTIVE_CELL_BACKGROUND = new Color(195,229,254);

  /** background color of grid selected row */
  public static Color GRID_SELECTION_BACKGROUND = new Color(185,219,243);

  /** foreground color of grid selected row */
  public static Color GRID_SELECTION_FOREGROUND = Color.black;

  /** background color of grid cell */
  public static Color GRID_CELL_BACKGROUND = new Color(238,238,238);

  /** foreground color of grid cell */
  public static Color GRID_CELL_FOREGROUND = Color.black;

  /** border color of a grid cell that is mandatory */
  public static Color GRID_REQUIRED_CELL_BORDER = Color.red;

  /** border color of an editable grid cell */
  public static Color GRID_EDITABLE_CELL_BACKGROUND = Color.white;

  /** border color of a not editable grid cell */
  public static Color GRID_NOT_EDITABLE_CELL_BACKGROUND = GRID_CELL_BACKGROUND;

  /** border color of the grid that currently has the focus */
  public static Color GRID_FOCUS_BORDER = Color.black;

  /** border color of the grid that currently doesn't has the focus */
  public static Color GRID_NO_FOCUS_BORDER = Color.gray;

  /** border color of the form currently has the focus */
  public static Color FORM_FOCUS_BORDER = Color.gray;

  /** image name of ascending order versus icon */
  public static String SORT_UP = "sortup.gif";

  /** image name of descending order versus icon */
  public static String SORT_DOWN = "sortdown.gif";

  /** height of grid rows */
  public static int CELL_HEIGHT = 20;

  /** height of grid headers */
  public static int HEADER_HEIGHT = 24;

  /** progress bar color (inside the status panel of the MDI frame) */
  public static Color PROGRESS_BAR_COLOR = new Color(185,219,243);

  /** progress bar delay (in milliseconds) */
  public static int PROGRESS_BAR_DELAY = 100;

  /** masimum number of exportable rows */
  public static int MAX_EXPORTABLE_ROWS = 10000;

  /** define if must be show the mandatory symbol "*" in mandatory input controls */
  public static boolean VIEW_MANDATORY_SYMBOL = false;

  /** define if must be set a background color on focusing an input control */
  public static boolean VIEW_BACKGROUND_SEL_COLOR = false;

  /** color to set as background in the focused input control (only if VIEW_BACKGROUND_SEL_COLOR is set to <code>true</code>) */
  public static Color BACKGROUND_SEL_COLOR = GRID_ACTIVE_CELL_BACKGROUND;

  /** factory class used to retrieve internationalization settings */
  private ResourcesFactory resourceFactory = null;

  /** collection of domains, i.e. associations (domainId, Domain object) */
  private Hashtable domains = null;

  /** the single instance of that class */
  private static ClientSettings instance = null;

  /** buttons authorizations */
  private ButtonsAuthorizations buttonsAuthorizations = null;

  /** look 'n feel to use for the MDI frame; default value: UIManager.getSystemLookAndFeelClassName() */
  public static String LOOK_AND_FEEL_CLASS_NAME = UIManager.getSystemLookAndFeelClassName();

  /** <code>true</code> to automatically show a filter panel when moving mouse at right of the grid; <code>false</code> to do not show it */
  public static boolean FILTER_PANEL_ON_GRID = false;

  /** default value that could be set in the quick filter criteria; values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH */
  public static int DEFAULT_QUICK_FILTER_CRITERIA = Consts.EQUALS;


  /**
   * Contains application settings.
   * Buttons permission manager not defined: buttons are always enabled (as default).
   * @param resourceFactory factory class used to retrieve internationalization settings
   * @param domains collection of domains, i.e. associations (domainId, Domain object)
   * @param controller Server Controller (Java Servlet) to contact (optional)
   */
  public ClientSettings(
      ResourcesFactory resourceFactory,
      Hashtable domains
  ) {
    this(resourceFactory,domains,new ButtonsAuthorizations());
  }


  /**
   * Contains application settings.
   * Font size is small.
   * @param resourceFactory factory class used to retrieve internationalization settings
   * @param domains collection of domains, i.e. associations (domainId, Domain object)
   * @param buttonsAuthorizations buttons authorizations
   * @param useSystemFontSettings <code>true</code> font in java 1.4. is small; <code>false</code> font size is higher
   */
  public ClientSettings(
      ResourcesFactory resourceFactory,
      Hashtable domains,
      ButtonsAuthorizations buttonsAuthorizations
  ) {
    this(resourceFactory,domains,buttonsAuthorizations,true);
  }

  /**
   * Contains application settings.
   * @param resourceFactory factory class used to retrieve internationalization settings
   * @param domains collection of domains, i.e. associations (domainId, Domain object)
   * @param buttonsAuthorizations buttons authorizations
   * @param useSystemFontSettings <code>true</code> font in java 1.4. is small; <code>false</code> font size is higher
   */
  public ClientSettings(
      ResourcesFactory resourceFactory,
      Hashtable domains,
      ButtonsAuthorizations buttonsAuthorizations,
      boolean useSystemFontSettings

  ) {
    try {
      UIManager.setLookAndFeel(ClientSettings.LOOK_AND_FEEL_CLASS_NAME);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    this.resourceFactory = resourceFactory;
    this.domains = domains;
    this.buttonsAuthorizations = buttonsAuthorizations;
    instance = this;
    try {
      if (!useSystemFontSettings) {
        System.class.getMethod("setProperty", new Class[] {String.class, String.class}).
            invoke(null, new Object[] {"swing.useSystemFontSettings", "false"});
      }
    }
    catch (Throwable ex1) {
      ex1.printStackTrace();
    }
  }


  /**
   * @return the single instance of that class
   */
  public static final ClientSettings getInstance() {
    if (instance==null) {
      System.err.println("You must set ClientSettings object");
      instance = new ClientSettings(new EnglishOnlyResourceFactory("E",new Properties(),true),new Hashtable());
    }
    return instance;
  }


  /**
   * @return dictionary used to internalization settings
   */
  public final Resources getResources() {
    if (resourceFactory==null) {
      System.err.println("You must set ResourcesFactory object.");
      resourceFactory = new EnglishOnlyResourceFactory("Euro",new Properties(),true);
    }
    return resourceFactory.getResources();
  }


  /**
   * Load internalization settings according with the specified languageId.
   */
  public final void setLanguage(String languageId) {
    resourceFactory.setLanguage(languageId);
  }


  /**
   * @param domainId domain identifier
   * @return Domain object, identified by domainId
   */
  public final Domain getDomain(String domainId) {
    return (Domain)domains.get(domainId);
  }


 /**
   * @return buttons authorizations
   */
  public final ButtonsAuthorizations getButtonsAuthorizations() {
    return buttonsAuthorizations;
  }



}
