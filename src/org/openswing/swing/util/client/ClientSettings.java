package org.openswing.swing.util.client;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.openswing.swing.domains.java.*;
import org.openswing.swing.export.java.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.permissions.java.*;
import org.openswing.swing.table.profiles.java.*;
import org.openswing.swing.util.java.*;


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

  /** image name for the MDI Frame */
  public static String ICON_FILENAME = "appicon.gif";

  /** image name for tree menu background */
  public static String TREE_BACK = "treeback.jpg";

  /** image name for opened padlock icon in tree menu */
  public static String LOCK_ON = "lock_on.gif";

  /** image name for closed padlock icon in tree menu */
  public static String LOCK_OFF = "lock_off.gif";

  /** image name for "find function" icon in tree menu (optional); default value: null, i.e. do not show any icon */
  public static String FIND_FUNCTION_ICON = null;

  /** show "find function" label at the left of the search input field of the tree menu */
  public static boolean SHOW_FIND_FUNCTION_LABEL = true;

  /** fill search input field of the tree menu til the right margin of the panel */
  public static boolean FILL_FIND_FUNCTION_FIELD = false;

  /** show "functions" label at the bottom of the search input field of the tree menu */
  public static boolean SHOW_FUNCTIONS_LABEL = true;

  /** flag used to show/hide a padlock in the tree menu panel; default value: <code>true</code> */
  public static boolean SHOW_PADLOCK_IN_TREE_MENU = true;

  /** define if tree menu must by automatically expanded; default value: <code>false</code> */
  public static boolean AUTO_EXPAND_TREE_MENU = false;

  /** image name for opened padlock icon in filter panel */
  public static String FILTER_PANEL_LOCK_ON = "lock.gif";

  /** image name for closed padlock icon in filter panel */
  public static String FILTER_PANEL_LOCK_OFF = "unlock.gif";

  /** image name for close button in filter panel */
  public static String CLOSE_BUTTON_ON_FILTER_PANEL = "cancel.gif";

  /** image name for tree menu icons */
  public static String PERC_TREE_NODE = "node.gif";

  /** image name for tree menu icons */
  public static String PERC_TREE_FOLDER = "folder.gif";

  /** image name for MDI background; may be null */
  public static String BACKGROUND = "background.jpg";

  /** set how background image in MDI must be drawed: centered, repeated, streched; allowed values: Consts.BACK_IMAGE_xxx; defalut value: Consts.BACK_IMAGE_REPEATED */
  public static int BACK_IMAGE_DISPOSITION = Consts.BACK_IMAGE_REPEATED;

  /** optional property used within the DekstopPane of the MDI frame to draw background content; default value: null */
  public static BackgroundPainter BACKGROUND_PAINTER = null;

  /** image name for calendar button in date input control */
  public static String CALENDAR = "calendar.gif";

  /** divider width of MDI Frame splitter */
  public static int DIVIDER_WIDTH = 15;

  /** width of MDI Frame menu window */
  public static int MENU_WIDTH = 250;

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
  public static Color GRID_NO_FOCUS_BORDER = Color.lightGray;

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

  /** adapter used when exporting grid data in PDF format: it provides some overridable methods */
  public static ExportToPDFAdapter EXPORT_TO_PDF_ADAPTER = new ExportToPDFAdapter();

  /** flag used to add a filter panel on top of the exported grid, in order to show filtering conditions; this pane is visibile only whether there is at least one filtering condition applied; default value: <code>false</code> */
  public static boolean SHOW_FILTERING_CONDITIONS_IN_EXPORT = false;

  /** flag used to show the title of the frame that contains the grid component currently exported; title is showed on top of the exported grid, in order to show filtering conditions; default value: <code>false</code> */
  public static boolean SHOW_FRAME_TITLE_IN_EXPORT = false;

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

  /** used as default value in "filterPanelOnGridPolicy" property of GridControl: it used only when "showFilterPanelOnGrid" property is set to <code>true</code>; define filter panel policy for hiding it; allowed values: Consts.FILTER_PANEL_ON_GRID_xxx; default value: Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED */
  public static int FILTER_PANEL_ON_GRID_POLICY = Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED;

  /** default value that could be set in the quick filter criteria; values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH */
  public static int DEFAULT_QUICK_FILTER_CRITERIA = Consts.EQUALS;

  /** define if IN operator must be included in quick filter and filter panel; default value: <code>true</code> */
  public static boolean INCLUDE_IN_OPERATOR = true;

  /** define if OR logical connector is allowable for operators different from IS NULL, IN NOT NULL and IN, within the filter panel; default value: <code>true</code> */
  public static boolean ALLOW_OR_OPERATOR = true;

  /** SQL operator to use for "like"; for instance in PosgreSQL database this can be replaced with case insensitive "ilke"; default value: "like" */
  public static String LIKE = "like";

  /** <code>true</code> to show pagination buttons in vertical scrollbar of grid; <code>false</code> to do not show them; default value: <code>true</code> */
  public static boolean SHOW_PAGINATION_BUTTONS_ON_GRID = true;

  /** <code>true</code> to show pagination buttons in navigator bar; <code>false</code> to do not show them; default value: <code>true</code> */
  public static boolean SHOW_PAGINATION_BUTTONS_ON_NAVBAR = true;

  /** <code>true</code> to show sorting order (number) in the column header of the grid; <code>false</code> to do not show it; default value: <code>false</code> */
  public static boolean SHOW_SORTING_ORDER = false;

  /** default font to use in column headers of grid; default value: null (i.e. use default JLabel font) */
  public static Font HEADER_FONT = null;

  /** default intercell spacing between cells in grid; default value: 1,1 */
  public static Dimension INTERCELL_SPACING = new Dimension(1,1);

  /** default margin between two rows in grid; default value: 1 */
  public static int ROW_MARGIN = 1;

  /** grid profile manager: it manages grid profile storing and fetching */
  public static GridProfileManager GRID_PROFILE_MANAGER = null;

  /** <code>true</code> to show a border around a Form panel when it has focus; <code>false</code> to do not show it; default value: <code>true</code> */
  public static boolean SHOW_FOCUS_BORDER_ON_FORM = true;

  /** define what a lookup has to do when an invalid code has been setted; possible values: LookupController.ON_INVALID_CODE_xxx; default value: LookupController.ON_INVALID_CODE_CLEAR_CODE */
  public static int ON_INVALID_CODE = LookupController.ON_INVALID_CODE_CLEAR_CODE;

  /** background color of tree selected row */
  public static Color TREE_SELECTION_BACKGROUND = new Color(185,219,243);

  /** foreground color of tree selected row */
  public static Color TREE_SELECTION_FOREGROUND = Color.black;

  /** shortcut key used to open the calendar popup in date control and date column; default value: KeyStroke.getKeyStroke(KeyEvent.VK_F2,0) */
  public static KeyStroke CALENDAR_OPEN_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_F2,0);

  /** shortcut key used to set current date in date control and date column; default value: KeyStroke.getKeyStroke(KeyEvent.VK_F1,0) */
  public static KeyStroke CALENDAR_CURRENT_DATE_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_F1,0);

  /** shortcut key used to open lookup grid in lookup control and lookup column; default value: KeyEvent.VK_F1 */
  public static KeyStroke LOOKUP_OPEN_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_F1,0);

  /** shortcut key used to open lookup controller frame in lookup control and lookup column; default value: KeyEvent.VK_F2 */
  public static KeyStroke LOOKUP_CONTROLLER_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_F2,0);

  /** shortcut key used for insert button, within a form or a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_I,KeyEvent.CTRL_MASK) */
  public static KeyStroke INSERT_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_I,KeyEvent.CTRL_MASK);

  /** shortcut key used for edit button, within a form or a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_MASK) */
  public static KeyStroke EDIT_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_MASK);

  /** shortcut key used for copy button, within a form or a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK) */
  public static KeyStroke COPY_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK);

  /** shortcut key used for save button, within a form or a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK) */
  public static KeyStroke SAVE_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK);

  /** shortcut key used for reload button, within a form or a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK) */
  public static KeyStroke RELOAD_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK);

  /** shortcut key used for delete button, within a form or a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_MASK) */
  public static KeyStroke DELETE_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_D,KeyEvent.CTRL_MASK);

  /** shortcut key used for export button within a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK) */
  public static KeyStroke EXPORT_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK);

  /** shortcut key used for import button within a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK) */
  public static KeyStroke IMPORT_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK);

  /** shortcut key used for filter button, within a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_F,KeyEvent.CTRL_MASK) */
  public static KeyStroke FILTER_BUTTON_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_F,KeyEvent.CTRL_MASK);

  /** shortcut key used to show popup menu within a grid control; default value: KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK) */
  public static KeyStroke GRID_POPUP_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK);

  /** shortcut key used to set focus in tree menu; default value: KeyStroke.getKeyStroke(KeyEvent.VK_F3,0) */
  public static KeyStroke TREE_MENU_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_F3,0);

  /** shortcut key used to remove a filter in quick filter panel; default value: KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.CTRL_MASK) */
  public static KeyStroke REMOVE_FILTER_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.CTRL_MASK);

  /** shortcut key used to expand a row in grid; default value: KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK) */
  public static KeyStroke EXPAND_CELL_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK);

  /** shortcut key used to collable an already expanded row in grid; default value: KeyStroke.getKeyStroke(KeyEvent.VK_L,KeyEvent.CTRL_MASK) */
  public static KeyStroke COLLAPSE_CELL_KEY = KeyStroke.getKeyStroke(KeyEvent.VK_L,KeyEvent.CTRL_MASK);

  /** shortcut key used to add new row in grid, in insert mode (or edit mode) */
  public static KeyStroke ADD_ROW_IN_GRID = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0);

  /** shortcut key used to remove an already added row in grid, in insert mode (or edit mode) */
  public static KeyStroke REMOVE_ROW_FROM_GRID = KeyStroke.getKeyStroke(KeyEvent.VK_UP,0);

  /** define if a warning dialog must be showed when closing an internal frame that contains a Form/GridControl in edit/insert mode */
  public static boolean ASK_BEFORE_CLOSE = false;

  /** define whether showing image/text on default buttons (insert, edit, ...); allowed values: Consts.BUTTON_IMAGE_ONLY, Consts.BUTTON_TEXT_ONLY, Consts.BUTTON_IMAGE_AND_TEXT; default value: Consts.BUTTON_IMAGE_ONLY */
  public static int BUTTON_BEHAVIOR = Consts.BUTTON_IMAGE_ONLY;

  /** define default content for lookup frame; allowed values are: GRID_FRAME, TREE_FRAME, TREE_GRID_FRAME, GRID_AND_FILTER_FRAME, TREE_GRID_AND_FILTER_FRAME, GRID_AND_PANEL_FRAME, TREE_GRID_AND_PANEL_FRAME; default value: LookupController.GRID_FRAME */
  public static int LOOKUP_FRAME_CONTENT = LookupController.GRID_FRAME;

  /** define if a lookup control must set focus over lookup button when pressing TAB on code field and field is correctly filled; default value: <code>false</code> */
  public static boolean FORCE_FOCUS_ON_LOOKUP_CONTROL = false;

  /** wait time (expressed in ms) before showing code auto completition feature for lookup controls; default value: <code>-1</code>, i.e. do not enable auto completition */
  public static long LOOKUP_AUTO_COMPLETITION_WAIT_TIME = -1;

  /** <code>true</code> to enable focus on disabled input controls: this allows to select disabled content of input control for copy & paste; <code>false</code> to do not allow focus setting on disabled input controls; default value: <code>false</code> */
  public static boolean DISABLED_INPUT_CONTROLS_FOCUSABLE = false;


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
