package org.openswing.swing.table.columns.client;


import javax.swing.*;
import javax.swing.table.*;

import org.openswing.swing.logger.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type lookup, i.e.
 * it contains an input field for code validation and a lookup button (optional) to open a lookup grid frame.</p>
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
public class CodLookupColumn extends Column {

  /** maximum characters number */
  private int maxCharacters = 255;

  /** flag used to set button visibility */
  private boolean hideButton = false;

  /** flag used to set if code field is editable */
  private boolean enableCodBox = true;

  /** flag used to set code padding (related to maxCharacter property); default "false" */
  private boolean codePadding = false;

  /** flag used to allow numbers only in code field */
  private boolean allowOnlyNumbers = false;

  /** lookup controller */
  private LookupController lookupController = null;

  /** flag used to set null value if code has zero length */
  private boolean zeroLengthAsNull = false;

  /** attribute name linked to code field */
  private String codAttributeName = null;

  /** flag used to hide the code box*/
  private boolean hideCodeBox;

  /** class name of the controller that must be invoked by pressing the "+" button */
  private String controllerClassName = null;

  /** method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button */
  private String controllerMethodName = null;

  /** wait time (expressed in ms) before showing code auto completition feature for lookup controls; default value: ClientSettings.LOOKUP_AUTO_COMPLETITION_WAIT_TIME */
  private long autoCompletitionWaitTime = ClientSettings.LOOKUP_AUTO_COMPLETITION_WAIT_TIME;


  public CodLookupColumn() { }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_LOOKUP;
  }


  /**
   * Set maximum characters number.
   * @param maxCharacters  maximum characters number
   */
  public void setMaxCharacters(int maxCharacters) {
    this.maxCharacters = maxCharacters;
  }


  /**
   * @return  maximum characters number
   */
  public int getMaxCharacters() {
    return maxCharacters;
  }


  /**
   * Set button visibility.
   * @param hideButton button visibility: <code>true</code> button is hidden, <code>false</code> button is visible
   */
  public void setHideButton(boolean hideButton) {
    this.hideButton = hideButton;
  }


  /**
   * @return button visibility: <code>true</code> button is hidden, <code>false</code> button is visible
   */
  public boolean isHideButton() {
    return hideButton;
  }


  /**
   * Set if code field is editable.
   * @param enableCodBox code field is editable
   */
  public void setEnableCodBox(boolean enableCodBox) {
    this.enableCodBox = enableCodBox;
  }


  /**
   * @return code field is editable
   */
  public boolean isEnableCodBox() {
    return enableCodBox;
  }


  /**
   * Set lookup controller.
   * @param lookupController lookup controller
   */
  public void setLookupController(LookupController lookupController) {
    this.lookupController = lookupController;
  }


  /**
   * @return lookup controller
   */
  public LookupController getLookupController() {
    return lookupController;
  }


  /**
   * @return allow numbers only in code field
   */
  public boolean isAllowOnlyNumbers() {
    return allowOnlyNumbers;
  }


  /**
   * @return code padding (related to maxCharacter property)
   */
  public boolean isCodePadding() {
    return codePadding;
  }


  /**
   * Set code padding (related to maxCharacter property); default "false".
   * @param codePadding code padding (related to maxCharacter property)
   */
  public void setCodePadding(boolean codePadding) {
    this.codePadding = codePadding;
  }


  /**
   * Allow numbers only in code field.
   * @param allowOnlyNumbers allow numbers only in code field
   */
  public void setAllowOnlyNumbers(boolean allowOnlyNumbers) {
    this.allowOnlyNumbers = allowOnlyNumbers;
  }


  /**
   * @return <code>true</code> if the control must return null value when code has zero length, <code>false</code> if the control must return "" when code has zero length
   */
  public boolean isZeroLengthAsNull() {
    return zeroLengthAsNull;
  }


  /**
   * Set null value if code has zero length.
   * @param zeroLengthAsNull <code>true</code> if the control must return null value when code has zero length, <code>false</code> if the control must return "" when code has zero length
   */
  public void setZeroLengthAsNull(boolean zeroLengthAsNull) {
    this.zeroLengthAsNull = zeroLengthAsNull;
  }


  /**
   * @return flag used to hide the code box
   */
  public boolean isHideCodeBox() {
    return hideCodeBox;
  }


  /**
   * Used to hide the code box.
   * @param hideCodeBox flag used to hide the code box
   */
  public void setHideCodeBox(boolean hideCodeBox) {
    this.hideCodeBox = hideCodeBox;
  }


  /**
   * @return class name of the controller that must be invoked by pressing the "+" button
   */
  public final String getControllerClassName() {
    return controllerClassName;
  }


  /**
   * @return method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button
   */
  public final String getControllerMethodName() {
    return controllerMethodName;
  }


  /**
   * Set the class name of the controller that must be invoked by pressing the "+" button.
   * @param controllerClassName class name of the controller that must be invoked by pressing the "+" button
   */
  public final void setControllerClassName(String controllerClassName) {
    this.controllerClassName = controllerClassName;
  }


  /**
   * Set the method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button.
   * @param controllerMethodName method name defined in ClientFacade class, related to the controller that must be invoked by pressing the "+" button
   */
  public final void setControllerMethodName(String controllerMethodName) {
    this.controllerMethodName = controllerMethodName;
  }


  /**
   * Force validation.
   * This method can be called by the developer to force a code validation, without losting focus and
   * based on the current selected row in grid.
   */
  public final void forceValidate() {
    forceValidate(getTable().getSelectedRow());
  }


  /**
   * @return wait time (expressed in ms) before showing code auto completition feature for lookup controls; <code>-1</code>, to do not enable auto completition
   */
  public final long getAutoCompletitionWaitTime() {
    return autoCompletitionWaitTime;
  }


  /**
   * Wait time before showing code auto completition feature for this lookup control.
   * @param autoCompletitionWaitTime wait time (expressed in ms) before showing code auto completition feature for this lookup control; default value: <code>-1</code> to do not enable auto completition
   */
  public final void setAutoCompletitionWaitTime(long autoCompletitionWaitTime) {
    this.autoCompletitionWaitTime = autoCompletitionWaitTime;
  }


  /**
   * Force validation.
   * This method can be called by the developer to force a code validation, without losting focus.
   * @param rowNumber row number used to determine where is the cell to validate
   */
  public final void forceValidate(final int rowNumber) {
    try {
      Object codValue = getTable().getVOListTableModel().getValueAt(rowNumber,getTable().getVOListTableModel().findColumn(getColumnName()));
      if (lookupController!=null && codValue!=null)
        try {
          lookupController.validateCode(
            getTable(),
            codValue.toString().toUpperCase(),
            new LookupParent() {

              /**
               * Method called by LookupController to update parent v.o.
               * @param attributeName attribute name in the parent v.o. that must be updated
               * @param value updated value
               */
              public void setValue(String attributeName,Object value) {
                getTable().getVOListTableModel().setValueAt(
                  value,
                  rowNumber,
                  getTable().getVOListTableModel().findColumn(attributeName)
                );
              }

              /**
               * @return parent value object
               */
              public ValueObject getValueObject() {
                return getTable().getVOListTableModel().getObjectForRow(rowNumber);
              }


              /**
               * @return attribute name in the parent value object related to lookup code
               */
              public Object getLookupCodeParentValue() {
                return getTable().getVOListTableModel().getValueAt(
                  rowNumber,
                  getTable().getVOListTableModel().findColumn(getColumnName())
                );
              }


            }
          );
        } catch (RestoreFocusOnInvalidCodeException ex) {
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              try {
                getTable().getGrid().editCellAt(rowNumber,getTable().getGrid().convertColumnIndexToView(getTable().getVOListTableModel().findColumn(getColumnName())));
              }
              catch (Exception ex) {
              }
            }
          });
        }
      if (getTable()!=null)
        getTable().repaint();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new CodLookupCellRenderer(
        tableContainer,
        isHideCodeBox(),
        getTextAlignment()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    if (getLookupController()==null) {
      Logger.error(this.getClass().getName(),"getCellEditor","The column '"+getColumnName()+"' has not set the 'lookupController' property.",null);
      return null;
    }
    return new CodLookupCellEditor(
        getMaxCharacters(),
        getLookupController(),
        isColumnRequired(),
        isAllowOnlyNumbers(),
        !isHideCodeBox(),
        isEnableCodBox(),
        getControllerClassName(),
        getControllerMethodName(),
        getAutoCompletitionWaitTime(),
        getColumnName()
    );

  }



}
