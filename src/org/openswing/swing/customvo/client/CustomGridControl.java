package org.openswing.swing.customvo.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.table.client.GridController;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.util.client.ClientSettings;
import java.util.ArrayList;
import org.openswing.swing.customvo.java.CustomFieldVO;
import org.openswing.swing.domains.java.Domain;
import java.math.BigDecimal;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame used to list custom grid.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JAllInOne ERP/CRM application.
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
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
public final class CustomGridControl extends GridControl {


  public CustomGridControl(CustomGridControlController controller) {
    try {
      setController(controller);
      setGridDataLocator(controller);
      setValueObjectClassName("org.openswing.swing.customvo.java.CustomValueObject");
      init(controller.getCustomFields());
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init(ArrayList customFields) throws Exception {
    TextColumn colText = null;
    DecimalColumn colDec = null;
    IntegerColumn colInt = null;
    ComboColumn colCombo = null;
    Column col = null;
    CustomFieldVO vo = null;
    for(int i=0;i<customFields.size();i++) {
      vo = (CustomFieldVO)customFields.get(i);
      if (vo.getAttributeType()==vo.TYPE_DATE) {
        col = new DateColumn();
      }
      if (vo.getAttributeType()==vo.TYPE_DATE_TIME) {
        col = new DateTimeColumn();
      }
      if (vo.getAttributeType()==vo.TYPE_TIME) {
        col = new TimeColumn();
      }
      if (vo.getAttributeType()==vo.TYPE_NUM) {
        if (vo.getConstraintValues()!=null && vo.getConstraintValues().length()>0) {
          String[] values = vo.getConstraintValues().split(",");
          Domain domain = new Domain("DOMAIN_"+vo.getAttributeName());
          for(int j=0;j<values.length;j++)
            domain.addDomainPair(new BigDecimal(values[j]),values[j]);
          colCombo.setDomain(domain);
          col = colCombo;
        }
        else if (vo.getDecimals()==0) {
          colInt = new IntegerColumn();
          colInt.setMaxValue((int)Math.pow(10,vo.getIntegers()));
          col = colInt;
        }
        else {
          colDec = new DecimalColumn();
          colDec.setMaxValue((int)Math.pow(10,vo.getIntegers()));
          colDec.setDecimals(vo.getDecimals());
          col = colDec;
        }
      }
      else if (vo.getAttributeType()==vo.TYPE_TEXT) {
        if (vo.getConstraintValues()!=null && vo.getConstraintValues().length()>0) {
          String[] values = vo.getConstraintValues().split(",");
          Domain domain = new Domain("DOMAIN_"+vo.getAttributeName());
          for(int j=0;j<values.length;j++)
            domain.addDomainPair(values[j],values[j]);
          colCombo.setDomain(domain);
          col = colCombo;
        }
        else  {
          colText = new TextColumn();
          colText.setMaxCharacters(vo.getMaxChars());
          col = colText;
        }
      }
      else if (vo.getAttributeType()==vo.TYPE_CHAR) {
        if (vo.getConstraintValues()!=null && vo.getConstraintValues().length()>0) {
          String[] values = vo.getConstraintValues().split(",");
          Domain domain = new Domain("DOMAIN_"+vo.getAttributeName());
          for(int j=0;j<values.length;j++)
            domain.addDomainPair(values[j],values[j]);
          colCombo.setDomain(domain);
          col = colCombo;
        }
        else  {
          colText = new TextColumn();
          colText.setMaxCharacters(vo.getMaxChars());
          colText.setRpadding(true);
          col = colText;
        }
      }

      col.setColumnName(vo.getAttributeName());
      col.setColumnFilterable(vo.isFilterable());
      col.setColumnRequired(vo.isRequired());
      col.setColumnSelectable(vo.isVisible());
      col.setColumnVisible(vo.isVisible());
      col.setColumnSortable(vo.isSortable());
      col.setEditableOnEdit(vo.isEditableOnEdit());
      col.setEditableOnInsert(vo.isEditableOnInsert());
      col.setHeaderColumnName(vo.getDescription());
      col.setPreferredWidth(vo.getColumnWidth());
      col.setSortingOrder(vo.getSortOrder());
      col.setSortVersus(vo.getSortVersus());
      getColumnContainer().add(col, null);
    }
  }


}
