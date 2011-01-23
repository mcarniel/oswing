package org.openswing.swing.util.java;




/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Singleton containing application constants.</p>
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
public class Consts {


  public static final int READONLY = 0;
  public static final int INSERT = 1;
  public static final int EDIT = 2;


  /** ascending order versus */
  public static final String ASC_SORTED = "ASC";

  /** descending order versus */
  public static final String DESC_SORTED = "DESC";

  /** no order versus */
  public static final String NO_SORTED = "NO_SORT";

  public static final int TYPE_DATE = 1;
  public static final int TYPE_DATE_TIME = 2;
  public static final int TYPE_TIME = 3;

  /** filter to apply in lookup grid data */
  public static final String TREE_FILTER = "TREE_FILTER";

  /** filter criteria available in filter panel */
  public static final String EQ = "=";
  public static final String GE = ">=";
  public static final String GT = ">";
  public static final String IS_NOT_NULL = "is not null";
  public static final String IS_NULL = "is null";
  public static final String LE = "<=";
  public static final String LIKE = "like";
  public static final String LT = "<";
  public static final String NEQ = "<>";
  public static final String IN = "in";
  public static final String NOT_IN = "not in";

  /** default value that could be set in the quick filter criteria: equals */
  public static final int EQUALS = 0;

  /** default value that could be set in the quick filter criteria: contains */
  public static final int CONTAINS = 1;

  /** default value that could be set in the quick filter criteria: starts with */
  public static final int STARTS_WITH = 2;

  /** default value that could be set in the quick filter criteria: ends with */
  public static final int ENDS_WITH = 3;

  /** constant assignable in "filterPanelOnGridPolicy" property of GridControl to automatically hide filter panel when mouse exits from that panel */
  public static final int FILTER_PANEL_ON_GRID_CLOSE_ON_EXIT = 0;

  /** constant assignable in "filterPanelOnGridPolicy" property of GridControl to hide filter panel by pressing a close button */
  public static final int FILTER_PANEL_ON_GRID_USE_CLOSE_BUTTON = 1;

  /** constant assignable in "filterPanelOnGridPolicy" property of GridControl to hide filter panel by using a padlock button (pressed as default) */
  public static final int FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED = 2;

  /** constant assignable in "filterPanelOnGridPolicy" property of GridControl to hide filter panel by using a padlock button (unpressed as default) */
  public static final int FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED = 3;

  /** constant used in ClientSetting.BACK_IMAGE_DISPOSITION: it defines that background image in MDI must be drawed centered */
  public static final int BACK_IMAGE_CENTERED = 0;

  /** constant used in ClientSetting.BACK_IMAGE_DISPOSITION: it defines that background image in MDI must be drawed repeated */
  public static final int BACK_IMAGE_REPEATED = 1;

  /** constant used in ClientSetting.BACK_IMAGE_DISPOSITION: it defines that background image in MDI must be drawed stretched */
  public static final int BACK_IMAGE_STRETCHED = 2;

  /** used in ClientSettings.BUTTON_BEHAVIOR global property to show image on default buttons (insert, edit, ...) ) */
  public static final int BUTTON_IMAGE_ONLY = 0;

  /** used in ClientSettings.BUTTON_BEHAVIOR global property to show text on default buttons (insert, edit, ...) ) */
  public static final int BUTTON_TEXT_ONLY = 1;

  /** used in ClientSettings.BUTTON_BEHAVIOR global property to show image and text on default buttons (insert, edit, ...) ) */
  public static final int BUTTON_IMAGE_AND_TEXT = 2;

  /** used in ClientSettings.GRID_SCROLL_BLOCK_INCREMENT global property to set the block increment in grid to one row per time */
  public static final int GRID_SCROLL_BLOCK_INCREMENT_ROW = 1;

  /** used in ClientSettings.GRID_SCROLL_BLOCK_INCREMENT global property to set the block increment in grid to one page per time */
  public static final int GRID_SCROLL_BLOCK_INCREMENT_PAGE = 2;

}
