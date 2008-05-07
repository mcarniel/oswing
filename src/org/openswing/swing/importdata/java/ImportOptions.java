package org.openswing.swing.importdata.java;

import java.io.*;
import java.util.*;

import org.openswing.swing.table.java.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Import information needed to import data on the server side.</p>
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
public class ImportOptions implements Serializable {

  public static final String XLS_FORMAT = "XLS";
  public static final String CSV_FORMAT1 = "CSV (,)";
  public static final String CSV_FORMAT2 = "CSV (;)";

  public static final int TYPE_TEXT = 0;
  public static final int TYPE_DATE = Consts.TYPE_DATE;
  public static final int TYPE_DATE_TIME = Consts.TYPE_DATE_TIME;
  public static final int TYPE_TIME = Consts.TYPE_TIME;
  public static final int TYPE_INT = 4;
  public static final int TYPE_DEC = 5;
  public static final int TYPE_CHECK = 6;
  public static final int TYPE_COMBO = 7;
  public static final int TYPE_LOOKUP = 8;
  public static final int TYPE_PERC = 9;
  public static final int TYPE_CURRENCY = 10;
  public static final int TYPE_FORMATTED_TEXT = 13;
  public static final int TYPE_MULTI_LINE_TEXT = 14;
  public static final int TYPE_PROGRESS_BAR = 16;
  public static final int TYPE_COMBO_VO = 17;


}
