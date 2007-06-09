package org.openswing.swing.util.java;

import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;


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

  /** default value that could be set in the quick filter criteria: equals */
  public static final int EQUALS = 0;

  /** default value that could be set in the quick filter criteria: contains */
  public static final int CONTAINS = 1;

  /** default value that could be set in the quick filter criteria: starts with */
  public static final int STARTS_WITH = 2;

  /** default value that could be set in the quick filter criteria: ends with */
  public static final int ENDS_WITH = 3;



}
