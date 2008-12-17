package org.openswing.swing.lookup.client;

import java.util.*;
import org.openswing.swing.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import java.lang.reflect.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: data locator used to retrieve a list of data to show in a window.
 * </p>
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
public class LookupAutoCompletitionDataLocator implements AutoCompletitionDataLocator {

  /** lookup controller */
  private LookupController controller = null;

  /** attribute name in lookup v.o. related to lookup code */
  private String lookupCodeAttribute = null;


  /**
   * Constructor invoked by lookup.
   * @param controller lookup controller
   * @param codeAttribute attribute name in parent v.o. related to lookup code
   */
  public LookupAutoCompletitionDataLocator(LookupController controller,String codeAttribute) {
    this.controller = controller;
    this.lookupCodeAttribute=controller.getLookupAttributeName(codeAttribute);
  }


  /**
   * @param pattern pattern to use to filter the list of data
   * @return list of data to show in a window, where each data starts with the specified pattern
   */
  public final ArrayList getListOfData(Object pattern) {
    if (pattern!=null && pattern instanceof String)
      pattern = pattern.toString().toUpperCase();

    ArrayList data = new ArrayList();

    Map filteredColumns = new HashMap();
    filteredColumns.put(lookupCodeAttribute,new FilterWhereClause[]{
      new FilterWhereClause(lookupCodeAttribute,"like",pattern+"%"),
      null
    });

    ArrayList currentSortedColumns = new ArrayList();
    ArrayList currentSortedVersusColumns = new ArrayList();
    if (lookupCodeAttribute!=null) {
      currentSortedColumns.add(lookupCodeAttribute);
      currentSortedVersusColumns.add("ASC");
    }

    try {
      Class lookupVOClass = Class.forName(controller.getLookupValueObjectClassName());
      Response res = controller.getLookupDataLocator().loadData(
          GridParams.NEXT_BLOCK_ACTION,
          0,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          lookupVOClass
      );
      if (!res.isError()) {
        List rows = ((VOListResponse)res).getRows();

        String aName = lookupCodeAttribute;
        Method getter = null;
        Class clazz = null;
        Object obj = null;

        for(int i=0;i<rows.size();i++) {
          obj = rows.get(i);
          clazz = obj.getClass();

          while(aName.indexOf(".")!=-1) {
            try {
              getter = clazz.getMethod(
                "get" +
                aName.substring(0, 1).
                toUpperCase() +
                aName.substring(1,aName.indexOf(".")),
                new Class[0]
              );
            }
            catch (NoSuchMethodException ex2) {
              getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1,aName.indexOf(".")),new Class[0]);
            }
            obj = getter.invoke(obj,new Object[0]);
            if (obj==null)
              break;
            aName = aName.substring(aName.indexOf(".")+1);
            clazz = getter.getReturnType();
          }

          if (obj!=null) {
            try {
              getter = clazz.getMethod(
                "get" +
                aName.substring(0, 1).
                toUpperCase() +
                aName.substring(1),
                new Class[0]
              );
            }
            catch (NoSuchMethodException ex2) {
              getter = clazz.getMethod("is"+aName.substring(0,1).toUpperCase()+aName.substring(1),new Class[0]);
            }
            obj = getter.invoke(obj,new Object[0]);
            if (obj!=null)
              data.add(obj);
          }
        }



//        ValueObject vo = null;
//        String lookupMethodName = "get" + String.valueOf(Character.toUpperCase(lookupCodeAttribute.charAt(0))) + lookupCodeAttribute.substring(1);
//        Method lookupMethod = null;
//        try {
//          lookupMethod = lookupVOClass.getMethod(lookupMethodName,new Class[0]);
//        }
//        catch (NoSuchMethodException ex1) {
//          try {
//            lookupMethodName = "is" + String.valueOf(Character.toUpperCase(lookupCodeAttribute.charAt(0))) + lookupCodeAttribute.substring(1);
//            lookupMethod = lookupVOClass.getMethod(lookupMethodName,new Class[0]);
//          }
//          catch (NoSuchMethodException ex2) {
//          }
//        }
//        if (lookupMethod!=null)
//          for(int i=0;i<rows.size();i++) {
//            vo = (ValueObject)rows.get(i);
//            data.add( lookupMethod.invoke(vo,new Object[0]) );
//          }
      }
    }
    catch (Throwable ex) {
    }

    return data;
  }


}
