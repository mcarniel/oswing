package org.openswing.swing.table.columns.client;

import java.util.*;

import java.awt.event.*;
import javax.swing.table.*;

import org.openswing.swing.domains.java.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;
import org.openswing.swing.util.client.*;
import java.awt.ComponentOrientation;


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

  /** component left margin, with respect to component container; defaut value: 2 */
  private int leftMargin = 2;

  /** component right margin, with respect to component container; defaut value: 0 */
  private int rightMargin = 0;

  /** component top margin, with respect to component container; defaut value: 0 */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container; defaut value: 0 */
  private int bottomMargin = 0;

  /** define if description in combo items must be translated; default value: <code>true</code> */
  private boolean translateItemDescriptions = true;

  /** component orientation */
  private ComponentOrientation orientation = ClientSettings.TEXT_ORIENTATION;

  /** DomainTableCellRenderer (cell renderer), one for each grid controller (top locked rows, bottom locked rows, etc.) */
  private HashMap renderers = new HashMap();

  /** DomainCellEditor (cell editor), one for each grid controller (top locked rows, bottom locked rows, etc.) */
  private HashMap editors = new HashMap();




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

    DomainTableCellRenderer renderer = null;
    DomainCellEditor editor = null;
    Iterator it = renderers.values().iterator();
    while(it.hasNext()) {
      renderer = (DomainTableCellRenderer)it.next();
      renderer.setDomain(getDomain());
    }
    it = editors.values().iterator();
    while(it.hasNext()) {
      editor = (DomainCellEditor)it.next();
      editor.setDomain(getDomain());
    }
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

    DomainTableCellRenderer renderer = null;
    DomainCellEditor editor = null;
    Iterator it = renderers.values().iterator();
    while(it.hasNext()) {
      renderer = (DomainTableCellRenderer)it.next();
      renderer.setDomain(domain);
    }
    it = editors.values().iterator();
    while(it.hasNext()) {
      editor = (DomainCellEditor)it.next();
      editor.setDomain(domain);
    }
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
   * @return component bottom margin, with respect to component container
   */
  public final int getBottomMargin() {
    return bottomMargin;
  }


  /**
   * @return component left margin, with respect to component container
   */
  public final int getLeftMargin() {
    return leftMargin;
  }


  /**
   * @return component right margin, with respect to component container
   */
  public final int getRightMargin() {
    return rightMargin;
  }


  /**
   * @return component top margin, with respect to component container
   */
  public final int getTopMargin() {
    return topMargin;
  }


  /**
   * Set component top margin, with respect to component container.
   * @param topMargin component top margin
   */
  public final void setTopMargin(int topMargin) {
    this.topMargin = topMargin;
  }


  /**
   * Set component right margin, with respect to component container.
   * @param rightMargin component right margin
   */
  public final void setRightMargin(int rightMargin) {
    this.rightMargin = rightMargin;
  }


  /**
   * Set component left margin, with respect to component container.
   * @param leftMargin component left margin
   */
  public final void setLeftMargin(int leftMargin) {
    this.leftMargin = leftMargin;
  }


  /**
   * Set component bottom margin, with respect to component container.
   * @param bottomMargin component bottom margin
   */
  public final void setBottomMargin(int bottomMargin) {
    this.bottomMargin = bottomMargin;
  }


  /**
   * @return define if description in combo items must be translated
   */
  public final boolean isTranslateItemDescriptions() {
    return translateItemDescriptions;
  }


  /**
   * Define if description in combo items must be translated; default value: <code>true</code>.
   * @param translateItemDescriptions flag used to define if description in combo items must be translated
   */
  public final void setTranslateItemDescriptions(boolean translateItemDescriptions) {
    this.translateItemDescriptions = translateItemDescriptions;
  }


  /**
   * Set the component orientation: from left to right or from right to left.
   * @param orientation component orientation
   */
  public final void setTextOrientation(ComponentOrientation orientation) {
    this.orientation = orientation;
  }


  /**
   * @return component orientation
   */
  public final ComponentOrientation getTextOrientation() {
      return orientation;
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
    if (domain!=null) {
      DomainTableCellRenderer renderer = (DomainTableCellRenderer)renderers.get(tableContainer.toString());
      if (renderer==null) {
        renderer = new DomainTableCellRenderer(
            domain,
            translateItemDescriptions,
            tableContainer,
            getTextAlignment(),
            getLeftMargin(),
            getRightMargin(),
            getTopMargin(),
            getBottomMargin(),
            getTextOrientation(),
            getColumnName()
        );
        renderers.put(tableContainer.toString(),renderer);
      }
      return renderer;
    }
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
    if (domain!=null) {
      DomainCellEditor editor = (DomainCellEditor)editors.get(tableContainer.toString());
      if (editor==null) {
        editor = new DomainCellEditor(
            domain,
            translateItemDescriptions,
            isColumnRequired(),
            getTextOrientation(),
            getItemListeners()
        );
        editors.put(tableContainer.toString(),editor);
      }
      return editor;
    }
    else {
      Logger.error(this.getClass().getName(),"getCellEditor","The domainId '"+getDomainId()+"' for the column '"+getColumnName()+"' doesn't exist.",null);
      return null;
    }
  }


}
