package org.openswing.swing.table.columns.client;

import org.openswing.swing.domains.java.Domain;
import java.util.ArrayList;
import java.awt.event.ItemListener;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.client.Grids;
import org.openswing.swing.table.renderers.client.DomainTableCellRenderer;
import org.openswing.swing.logger.client.Logger;
import org.openswing.swing.table.editors.client.DomainCellEditor;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type combo-box:
 * it contains a combo box with a domain linked to it.</p>
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
public class ComboColumn extends Column {

  /** domain ID */
  private String domainId;

  /** domain */
  private Domain domain;

  /** combo item listeners */
  private ArrayList itemListeners = new ArrayList();

  /** define if in insert mode combo box has no item selected; default value: <code>false</code> i.e. the first item is pre-selected */
  private boolean nullAsDefaultValue = false;


  public ComboColumn() { }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_COMBO;
  }


  /**
   * @return domain ID
   */
  public String getDomainId() {
    return domainId;
  }


  /**
   * Set domain ID.
   * @param domainId domain ID
   */
  public void setDomainId(String domainId) {
    this.domainId = domainId;
  }


  /**
   * @return domain
   */
  public Domain getDomain() {
    if (domain==null && domainId!=null)
      domain = ClientSettings.getInstance().getDomain( domainId );
    return domain;
  }

  /**
   * Set the domain.
   * @param domain domain
   */
  public void setDomain(Domain domain) {
    this.domain = domain;
  }


  /**
   * Add an ItemListener to the combo.
   * @param listener ItemListener to add
   */
  public final void addItemListener(ItemListener listener) {
    itemListeners.add(listener);
  }


  /**
   * Remove an ItemListener from the combo.
   * @param listener ItemListener to remove
   */
  public final void removeItemListener(ItemListener listener) {
    itemListeners.remove(listener);
  }


  /**
   * @return ItemListener objects
   */
  public final ArrayList getItemListeners() {
    return itemListeners;
  }


  /**
   * @return define if in insert mode combo box has no item selected
   */
  public final boolean isNullAsDefaultValue() {
    return nullAsDefaultValue;
  }


  /**
   * Define if in insert mode combo box has no item selected.
   * @param nullAsDefaultValue define if in insert mode combo box has no item selected
   */
  public final void setNullAsDefaultValue(boolean nullAsDefaultValue) {
    this.nullAsDefaultValue = nullAsDefaultValue;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    if (getDomainId()==null &&
        getDomain()==null) {
      Logger.error(this.getClass().getName(),"getCellRenderer","You must set the 'domainId' property for the column '"+getColumnName()+"'.",null);
      return null;
    }
    Domain domain = getDomain();
    if (domain==null)
      domain = ClientSettings.getInstance().getDomain( getDomainId() );
    if (domain!=null)
      return new DomainTableCellRenderer(domain,tableContainer,getTextAlignment());
    else {
      Logger.error(this.getClass().getName(),"getCellRenderer","The domainId '"+getDomainId()+"' for the column '"+getColumnName()+"' "+" doesn't exist.",null);
      return null;
    }
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    if (getDomainId()==null &&
        getDomain()==null) {
      Logger.error(this.getClass().getName(),"getCellEditor","You must set the 'domainId' property for the column '"+getColumnName()+"'.",null);
      return null;
    }
    Domain domain = getDomain();
    if (domain==null)
      domain = ClientSettings.getInstance().getDomain( getDomainId() );
    if (domain!=null)
      return new DomainCellEditor(
          domain,
          isColumnRequired(),
          getItemListeners()
      );
    else {
      Logger.error(this.getClass().getName(),"getCellEditor","The domainId '"+getDomainId()+"' for the column '"+getColumnName()+"' doesn't exist.",null);
      return null;
    }
  }


}
