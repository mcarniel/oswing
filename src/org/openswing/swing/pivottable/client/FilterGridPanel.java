package org.openswing.swing.pivottable.client;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.util.client.ClientSettings;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Filter grid panel, used within dialog for pivot table.</p>
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
public class FilterGridPanel extends JPanel {

  GridControl grid = new GridControl();
  BorderLayout borderLayout2 = new BorderLayout();
  TextColumn colName = new TextColumn();
  CheckBoxColumn colChecked = new CheckBoxColumn();
  private HashMap allFields = null;
  private HashMap reverseAllFields = new HashMap();
  private String gridId = null;
  private ArrayList selectedFields = null;
  private int selRow = -1;


  /**
   * @param collection of pairs <description, DataField/RowField/ColumnField element>
   * @param selectedFields list of DataField/RowField/ColumnField that are currently selected
   */
  public FilterGridPanel(HashMap allFields,ArrayList selectedFields) {
    this.allFields = allFields;
    this.selectedFields = selectedFields;
    this.gridId = String.valueOf(System.currentTimeMillis());
    Iterator it = allFields.keySet().iterator();
    Object desc = null;
    while(it.hasNext()) {
      desc = it.next();
      this.reverseAllFields.put(allFields.get(desc),desc);
    }
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    setLayout(borderLayout2);
    grid.setValueObjectClassName("org.openswing.swing.pivottable.client.FieldVO");
    grid.setVisibleStatusPanel(false);
    grid.setShowFilterPanelOnGrid(false);
    colName.setColumnName("description");
    colName.setHeaderColumnName("field");
    colName.setPreferredWidth(150);
    colChecked.setColumnName("selected");
    colChecked.setEditableOnEdit(true);
    colChecked.setPreferredWidth(50);
    colChecked.setHeaderColumnName("checked");
    this.add(grid,  BorderLayout.CENTER);
    grid.getColumnContainer().add(colName, null);
    grid.getColumnContainer().add(colChecked, null);
    grid.setGridDataLocator(new GridDataLocator() {

      public Response loadData(int action, int startIndex, Map filteredColumns,
                               ArrayList currentSortedColumns,
                               ArrayList currentSortedVersusColumns,
                               Class valueObjectType, Map otherGridParams) {
        ArrayList rows = new ArrayList();
        Object field = null;
        String desc = null;
        FieldVO vo = null;
        for(int i=0;i<selectedFields.size();i++) {
          field = selectedFields.get(i);
          desc = (String)reverseAllFields.get(field);
          vo = new FieldVO();
          vo.setDescription(desc);
          vo.setSelected(true);
          rows.add(vo);
          reverseAllFields.remove(field);
        }
        Iterator it = reverseAllFields.keySet().iterator();
        while(it.hasNext()) {
          field = it.next();
          desc = (String)reverseAllFields.get(field);
          vo = new FieldVO();
          vo.setDescription(desc);
          vo.setSelected(false);
          rows.add(vo);
        }
        return new VOListResponse(rows,false,rows.size());
      }

    });
    grid.enableDrag(FilterGridPanel.this.gridId);
    grid.setController(new GridController() {

      public void dropOver (int row) {
        grid.setRowSelectionInterval(row,row);
      }

      /**
       * Method called on beginning a drag event.
       * @return <code>true</code>, dragging can continue, <code>false</code> drag is not allowed; default value: <code>true</code>
       */
      public boolean dragEnabled() {
        selRow = grid.getSelectedRow();
        return true;
      }

      public void loadDataCompleted(boolean error) {
        grid.setMode(Consts.EDIT);
      }

      /**
       * Method called on firing a drop event onto the grid.
       * @param gridId identifier of the source grid (grid that generate a draf event)
       * @return <code>true</code>, drop is allowed, <code>false</code> drop is not allowed; default value: <code>true</code>
       */
      public boolean dropEnabled(String gridId) {
        boolean ok = gridId.equals(FilterGridPanel.this.gridId);
        if (ok) {
          int newSelRow = grid.getSelectedRow();
          ValueObject vo = grid.getVOListTableModel().getObjectForRow(selRow);
          grid.getVOListTableModel().removeObjectAt(selRow);
          grid.getVOListTableModel().insertObjectAt(vo,newSelRow);
          selRow = -1;
          grid.repaint();
        }
        return ok;
      }

    });
  }


  public final ArrayList getSelectedFields() {
    grid.transferFocus();
    ArrayList fields = new ArrayList();
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      FieldVO vo = (FieldVO)grid.getVOListTableModel().getObjectForRow(i);
      if (vo.isSelected()) {
        fields.add(allFields.get(vo.getDescription()));
      }
    }
    return fields;
  }


}

