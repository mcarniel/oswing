package org.openswing.swing.pivottable.server;

import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.pivottable.tablemodelreaders.server.Reader;
import org.openswing.swing.message.receive.java.*;
import java.util.*;
import org.openswing.swing.pivottable.functions.java.GenericFunction;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Engine used to generate a PivotTableModel, starting from a PivotTableParameters.</p>
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
public class PivotTableEngine {

  /** data model reader */
  private Reader reader = null;


  /**
   * @param reader Data model reader
   */
  public PivotTableEngine(Reader reader) {
    this.reader = reader;
  }


  /**
   * @param pars parameters used in PivotTableEngine to generate the PivotTableModel
   * @return VOResponse that contains a PivotTableModel generated starting from PivotTableParameters; ErrorResponse in case of errors on analyzing data
   */
  public final Response getPivotTableModel(PivotTableParameters pars) {
    if (!reader.initializeScrolling(pars.getInputFilter()))
      return new ErrorResponse("Error on reading data on input.");

    HashSet cols = new HashSet();
    for(int i=0;i<reader.getColumnCount();i++)
      cols.add(reader.getColumnName(i));

      // check whether some row/column/data fields are unknown...
    for(int i=0;i<pars.getRowFields().size();i++)
      if (!cols.contains(((RowField)pars.getRowFields().get(i)).getColumnName())) {
        return new ErrorResponse( ((RowField)pars.getRowFields().get(i)).getColumnName()+"' row field is not defined in data model: Pivot Table cannot be created." );
      }
    for(int i=0;i<pars.getColumnFields().size();i++)
      if (!cols.contains(((ColumnField)pars.getColumnFields().get(i)).getColumnName())) {
        return new ErrorResponse( ((ColumnField)pars.getColumnFields().get(i)).getColumnName()+"' column field is not defined in data model: Pivot Table cannot be created." );
      }
    for(int i=0;i<pars.getDataFields().size();i++)
      if (!cols.contains(((DataField)pars.getDataFields().get(i)).getColumnName())) {
        return new ErrorResponse( ((DataField)pars.getDataFields().get(i)).getColumnName()+"' data field is not defined in data model: Pivot Table cannot be created." );
      }
    if (pars.getRowFields().size()==0) {
      return new ErrorResponse("At least one field must be defined as row field: Pivot Table cannot be created.");
    }
    if (pars.getColumnFields().size()==0) {
      return new ErrorResponse("At least one field must be defined as column field: Pivot Table cannot be created.");
    }
    if (pars.getDataFields().size()==0) {
      return new ErrorResponse("At least one field must be defined as data field: Pivot Table cannot be created.");
    }

    // index input data model column names...
    HashMap indexes = new HashMap();
    for(int j=0;j<reader.getColumnCount();j++) {
      indexes.put(reader.getColumnName(j),new Integer(j));
    }

    // index row fields indexes...
    int[] rowFieldsIndexes = new int[pars.getRowFields().size()];
    for(int j=0;j<pars.getRowFields().size();j++) {
       rowFieldsIndexes[j] = ((Integer)indexes.get(((RowField)pars.getRowFields().get(j)).getColumnName())).intValue();
    }

    // index data fields indexes...
    int[] dataFieldsIndexes = new int[pars.getDataFields().size()];
    for(int j=0;j<pars.getDataFields().size();j++) {
       dataFieldsIndexes[j] = ((Integer)indexes.get(((DataField)pars.getDataFields().get(j)).getColumnName())).intValue();
    }

    // index column fields indexes...
    int[] colFieldsIndexes = new int[pars.getColumnFields().size()];
    for(int j=0;j<pars.getColumnFields().size();j++) {
       colFieldsIndexes[j] = ((Integer)indexes.get(((ColumnField)pars.getColumnFields().get(j)).getColumnName())).intValue();
    }

    // create Pivot TableModel...
    RowGenericNode hroot = new RowGenericNode();
    GlobalColGenericNode vroot = new GlobalColGenericNode();
    PivotTableModel model = new PivotTableModel(hroot,vroot);

    // read all data in input...
//    StringBuffer hpath = new StringBuffer();
    GenericNodeKey hpath = null;
    RowGenericNode hnode = null;
    ColGenericNode vnode = null;
    GlobalColGenericNode globalvnode = null;
    RowGenericNode hparentNode = null;
    ColGenericNode vparentNode = null;
    GlobalColGenericNode globalvparentNode = null;
    RowField rowField = null;
    ColumnField colField = null;
    Object hvalue = null;
    Object vvalue = null;
    HashMap htreeNodes = new HashMap();
    HashMap vtreeNodes = null;
    HashMap globalvtreeNodes = new HashMap();
    GenericFunction[] gf = null;
    GenericNodeKey vpath = null;
    while(reader.nextRow(pars.getInputFilter())) {
      // read row...
      hparentNode = hroot;
      hpath = new GenericNodeKey();

      // for each row: parse row data, along row fields...
      for(int j=0;j<pars.getRowFields().size();j++) {
        rowField = (RowField)pars.getRowFields().get(j);
        hvalue = rowField.getAggregator().decodeValue(reader.getValueAt(rowFieldsIndexes[j]));
        if (hvalue==null)
          hvalue = "";

        hpath = hpath.appendKey(hvalue);
        hnode = (RowGenericNode)htreeNodes.get(hpath);
        if (hnode==null) {
          hnode = new RowGenericNode(hpath);
          htreeNodes.put(hpath,hnode);
          hparentNode.add(hnode);
        }
        vtreeNodes = hnode.getVtreeNodes();

        // for each row field node, parse row data, along column fields and calculate data field values...
        vparentNode = null;
        globalvparentNode = vroot;
        vpath = new GenericNodeKey();
        for(int y=0;y<pars.getColumnFields().size();y++) {
          colField = (ColumnField)pars.getColumnFields().get(y);
          vvalue = colField.getAggregator().decodeValue(reader.getValueAt(colFieldsIndexes[y]));
          if (vvalue==null)
            vvalue = "";

          vpath = vpath.appendKey(vvalue);
          vnode = (ColGenericNode)vtreeNodes.get(vpath);
          if (vnode==null) {
            gf = new GenericFunction[dataFieldsIndexes.length];
            for (int u = 0; u < dataFieldsIndexes.length; u++)
              try {
               gf[u] = (GenericFunction) ( (DataField) pars.getDataFields().get(u)).getFunction().getClass().newInstance();
              }
              catch (Throwable ex) { return new ErrorResponse("Error while analyzing data."); }

            vnode = new ColGenericNode(vpath,gf);
            vtreeNodes.put(vpath,vnode);
            if (vparentNode!=null)
              vparentNode.add(vnode);
            else
              hnode.setColsParentNode(vnode);
          }
          else
            gf = (GenericFunction[])vnode.getGenericFunctions();

          fillRow(pars,dataFieldsIndexes,gf);
          vparentNode = vnode;

          // moreover, create a global column fields hierarchy, without associating data field values...
          globalvnode = (GlobalColGenericNode)globalvtreeNodes.get(vpath);
          if (globalvnode==null) {
            globalvnode = new GlobalColGenericNode(vpath);
            globalvtreeNodes.put(vpath,globalvnode);
            globalvparentNode.add(globalvnode);
          }
          globalvparentNode = globalvnode;

        } // end for on colFields

        hparentNode = hnode;
      } // end for on rowFields
    } // end while


    return new VOResponse(model);
  }


  private void fillRow(PivotTableParameters pars,int[] dataFieldsIndexes,GenericFunction[] gf) {
    Number n = null;
    for(int j=0;j<pars.getDataFields().size();j++) {
      n = (Number)reader.getValueAt(dataFieldsIndexes[j]);
      gf[j].processValue(reader.getValueAt(dataFieldsIndexes[j]));
    }
  }






}
