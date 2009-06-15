package org.openswing.swing.table.client;

import org.openswing.swing.table.model.client.VOListTableModel;
import org.openswing.swing.table.model.client.VOListAdapter;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.util.java.Consts;
import java.text.Collator;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class used by grid control in order to sort columns in a custom way.
 * Default column sort algorithm can be changed by overriding <code>compareAttributeValues</code> method.</p>
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
public class OrderPolicy {

  private Collator collator = null;


  /**
   * Method invoked by Grid component in order to compare two rows of the same VOListModel.
   * @param modelAdapter TableModel adapter, used to link ValueObjects to TableModel
   * @param grids Grids grid component
   * @param model grid model
   * @param o1 value object related to first row
   * @param o2 value object related to second row
   * @return row comparison result
   */
  public final int compareRow(VOListAdapter modelAdapter,Grids grids,VOListTableModel model,ValueObject o1,ValueObject o2) {
    if (collator==null)
      // initialize Collator instance...
      collator = Collator.getInstance(grids.getDefaultLocale());

    int colIndex,sign;
    Object val1,val2;
    String attributeName = null;
    String sortVersus = null;
    int retValue = 0;
    for(int i=0;i<grids.getCurrentSortedColumns().size();i++) {
      colIndex = modelAdapter.getFieldIndex(grids.getCurrentSortedColumns().get(i).toString());
      attributeName = modelAdapter.getFieldName(colIndex);
      sortVersus = grids.getCurrentSortedVersusColumns().get(i).toString();
      val1 = modelAdapter.getField(o1,colIndex);
      val2 = modelAdapter.getField(o2,colIndex);
      sign = sortVersus.equals(Consts.ASC_SORTED)?+1:-1;
      if (val1==null && val2==null)
        continue;
      else if (val1==null && val2!=null)
        return -1*sign;
      else if (val1!=null && val2==null)
        return +1*sign;
      else {
        retValue = compareAttributeValues(attributeName,sortVersus,val1, val2,model);
        if (retValue!=0)
          return retValue;
      }
    }
    return 0;
  }



  /**
   * Callback method invoked by grid in order to sort the specified column:
   * it can be overrided in order to define a custom sorting algorithm.
   * The default implementation is able to compare Date, Number  and String objects.
   * @param attributeName attribute name that identities the column to sort
   * @param sortVersus sorting order; allowed values: Consts.ASC_SORTED,Consts.DESC_SORTED,Consts.NO_SORTED
   * @param val1 first value to compare
   * @param val2 second value to compare
   * @param model VOListModel grid model to sort
   * @return cell values comparison result: -1 if val1 is less than val2, +1 if val1 is greater than val2, 0 if val1 is equals to val2
   */
  public int compareAttributeValues(String attributeName,String sortVersus,Object val1, Object val2,VOListTableModel model) {
    int sign = sortVersus.equals(Consts.ASC_SORTED)?+1:-1;

    if (val1 instanceof java.util.Date) {
      if (((java.util.Date)val1).getTime()<((java.util.Date)val2).getTime())
        return -1*sign;
      else if (((java.util.Date)val1).getTime()>((java.util.Date)val2).getTime())
        return +1*sign;
    }
    else if (val1 instanceof Number) {
      if (((Number)val1).doubleValue()<((Number)val2).doubleValue())
        return -1*sign;
      else if (((Number)val1).doubleValue()>((Number)val2).doubleValue())
        return +1*sign;
    }
    else {
//              if (val1.toString().compareTo(val2.toString())<0)
//                return -1*sign;
//              else if (val1.toString().compareTo(val2.toString())>0)
//                return +1*sign;
      if (collator.compare(val1.toString(),val2.toString())<0)
        return -1*sign;
      else if (collator.compare(val1.toString(),val2.toString())>0)
        return +1*sign;
    }
    return 0;
  }


  /**
   * Callback method invoked by grid after internally sorting columns.
   * @param model VOListModel grid model just sorted
   */
  public void afterSorting(VOListTableModel model) {}



}
