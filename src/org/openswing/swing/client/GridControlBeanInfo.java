package org.openswing.swing.client;

import java.beans.*;

import java.awt.*;


public class GridControlBeanInfo extends SimpleBeanInfo {
  Class beanClass = GridControl.class;
  String iconColor16x16Filename = "GridControl16.png";
  String iconColor32x32Filename = "GridControl.png";
  String iconMono16x16Filename = "GridControl16.png";
  String iconMono32x32Filename = "GridControl.png";

  public GridControlBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _allowColumnsSortingInEdit = new PropertyDescriptor("allowColumnsSortingInEdit", beanClass, "isAllowColumnsSortingInEdit", "setAllowColumnsSortingInEdit");
      PropertyDescriptor _allowInsertInEdit = new PropertyDescriptor("allowInsertInEdit", beanClass, "isAllowInsertInEdit", "setAllowInsertInEdit");
      PropertyDescriptor _anchorLastColumn = new PropertyDescriptor("anchorLastColumn", beanClass, "isAnchorLastColumn", "setAnchorLastColumn");
      PropertyDescriptor _anchorLockedColumnsToLeft = new PropertyDescriptor("anchorLockedColumnsToLeft", beanClass, "isAnchorLockedColumnsToLeft", "setAnchorLockedColumnsToLeft");
      PropertyDescriptor _autoLoadData = new PropertyDescriptor("autoLoadData", beanClass, "isAutoLoadData", "setAutoLoadData");
      PropertyDescriptor _colorsInReadOnlyMode = new PropertyDescriptor("colorsInReadOnlyMode", beanClass, "isColorsInReadOnlyMode", "setColorsInReadOnlyMode");
      PropertyDescriptor _copyButton = new PropertyDescriptor("copyButton", beanClass, "getCopyButton", "setCopyButton");
      PropertyDescriptor _defaultQuickFilterCriteria = new PropertyDescriptor("defaultQuickFilterCriteria", beanClass, "getDefaultQuickFilterCriteria", "setDefaultQuickFilterCriteria");
      _defaultQuickFilterCriteria.setPropertyEditorClass(org.openswing.swing.client.QuickFilterCriteriaEditor.class);
      PropertyDescriptor _deleteButton = new PropertyDescriptor("deleteButton", beanClass, "getDeleteButton", "setDeleteButton");
      PropertyDescriptor _editButton = new PropertyDescriptor("editButton", beanClass, "getEditButton", "setEditButton");
      PropertyDescriptor _editOnSingleRow = new PropertyDescriptor("editOnSingleRow", beanClass, "isEditOnSingleRow", "setEditOnSingleRow");
      PropertyDescriptor _expandableColumn = new PropertyDescriptor("expandableColumn", beanClass, "getExpandableColumn", "setExpandableColumn");
      PropertyDescriptor _filterPanelOnGridPolicy = new PropertyDescriptor("filterPanelOnGridPolicy", beanClass, "getFilterPanelOnGridPolicy", "setFilterPanelOnGridPolicy");
      _filterPanelOnGridPolicy.setPropertyEditorClass(org.openswing.swing.client.FilterPanelOnGridPolicyEditor.class);
      PropertyDescriptor _gridDataLocator = new PropertyDescriptor("gridDataLocator", beanClass, "getGridDataLocator", "setGridDataLocator");
      PropertyDescriptor _headerHeight = new PropertyDescriptor("headerHeight", beanClass, "getHeaderHeight", "setHeaderHeight");
      PropertyDescriptor _importButton = new PropertyDescriptor("importButton", beanClass, "getImportButton", "setImportButton");
      PropertyDescriptor _insertButton = new PropertyDescriptor("insertButton", beanClass, "getInsertButton", "setInsertButton");
      PropertyDescriptor _expandableRowController = new PropertyDescriptor("expandableRowController", beanClass, "getExpandableRowController", "setExpandableRowController");
      PropertyDescriptor _exportButton = new PropertyDescriptor("exportButton", beanClass, "getExportButton", "setExportButton");
      PropertyDescriptor _filterButton = new PropertyDescriptor("filterButton", beanClass, "getFilterButton", "setFilterButton");
      PropertyDescriptor _intercellSpacing = new PropertyDescriptor("intercellSpacing", beanClass, "getIntercellSpacing", "setIntercellSpacing");
      PropertyDescriptor _insertRowsOnTop = new PropertyDescriptor("insertRowsOnTop", beanClass, "isInsertRowsOnTop", "setInsertRowsOnTop");
      PropertyDescriptor _lockedColumns = new PropertyDescriptor("lockedColumns", beanClass, "getLockedColumns", "setLockedColumns");
      PropertyDescriptor _lockedRowsOnTop = new PropertyDescriptor("lockedRowsOnTop", beanClass, "getLockedRowsOnTop", "setLockedRowsOnTop");
      PropertyDescriptor _lockedRowsOnBottom = new PropertyDescriptor("lockedRowsOnBottom", beanClass, "getLockedRowsOnBottom", "setLockedRowsOnBottom");
      PropertyDescriptor _mode = new PropertyDescriptor("mode", beanClass, "getMode", "setMode");
      PropertyDescriptor _navBar = new PropertyDescriptor("navBar", beanClass, "getNavBar", "setNavBar");
      PropertyDescriptor _functionId = new PropertyDescriptor("functionId", beanClass, "getFunctionId", "setFunctionId");
      PropertyDescriptor _maxNumberOfRowsOnInsert = new PropertyDescriptor("maxNumberOfRowsOnInsert", beanClass, "getMaxNumberOfRowsOnInsert", "setMaxNumberOfRowsOnInsert");
      PropertyDescriptor _maxSortedColumns = new PropertyDescriptor("maxSortedColumns", beanClass, "getMaxSortedColumns", "setMaxSortedColumns");
      PropertyDescriptor _orderWithLoadData = new PropertyDescriptor("orderWithLoadData", beanClass, "isOrderWithLoadData", "setOrderWithLoadData");
      PropertyDescriptor _overwriteRowWhenExpanding = new PropertyDescriptor("overwriteRowWhenExpanding", beanClass, "isOverwriteRowWhenExpanding", "setOverwriteRowWhenExpanding");
      PropertyDescriptor _preferredSize = new PropertyDescriptor("preferredSize", beanClass, "getPreferredSize", "setPreferredSize");
      PropertyDescriptor _reloadButton = new PropertyDescriptor("reloadButton", beanClass, "getReloadButton", "setReloadButton");
      PropertyDescriptor _reorderingAllowed = new PropertyDescriptor("reorderingAllowed", beanClass, "isReorderingAllowed", "setReorderingAllowed");
      PropertyDescriptor _resizingAllowed = new PropertyDescriptor("resizingAllowed", beanClass, "isResizingAllowed", "setResizingAllowed");
      PropertyDescriptor _rowHeight = new PropertyDescriptor("rowHeight", beanClass, "getRowHeight", "setRowHeight");
      PropertyDescriptor _rowHeightFixed = new PropertyDescriptor("rowHeightFixed", beanClass, "isRowHeightFixed", "setRowHeightFixed");
      PropertyDescriptor _rowMargin = new PropertyDescriptor("rowMargin", beanClass, "getRowMargin", "setRowMargin");
      PropertyDescriptor _saveButton = new PropertyDescriptor("saveButton", beanClass, "getSaveButton", "setSaveButton");
      PropertyDescriptor _searchAdditionalRows = new PropertyDescriptor("searchAdditionalRows", beanClass, "isSearchAdditionalRows", "setSearchAdditionalRows");
      PropertyDescriptor _selectionMode = new PropertyDescriptor("selectionMode", beanClass, "getSelectionMode", "setSelectionMode");
      PropertyDescriptor _showFilterPanelOnGrid = new PropertyDescriptor("showFilterPanelOnGrid", beanClass, "isShowFilterPanelOnGrid", "setShowFilterPanelOnGrid");
      PropertyDescriptor _showPageNumber = new PropertyDescriptor("showPageNumber", beanClass, "isShowPageNumber", "setShowPageNumber");
      PropertyDescriptor _showWarnMessageBeforeReloading = new PropertyDescriptor("showWarnMessageBeforeReloading", beanClass, "isShowWarnMessageBeforeReloading", "setShowWarnMessageBeforeReloading");
      PropertyDescriptor _singleExpandableRow = new PropertyDescriptor("singleExpandableRow", beanClass, "isSingleExpandableRow", "setSingleExpandableRow");
      PropertyDescriptor _valueObjectClassName = new PropertyDescriptor("valueObjectClassName", beanClass, "getValueObjectClassName", "setValueObjectClassName");
      PropertyDescriptor _visibleStatusPanel = new PropertyDescriptor("visibleStatusPanel", beanClass, "isVisibleStatusPanel", "setVisibleStatusPanel");
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
        _allowColumnsSortingInEdit,
        _allowInsertInEdit,
        _anchorLastColumn,
        _anchorLockedColumnsToLeft,
        _autoLoadData,
        _colorsInReadOnlyMode,
        _copyButton,
        _defaultQuickFilterCriteria,
        _deleteButton,
        _editButton,
        _editOnSingleRow,
        _expandableColumn,
        _expandableRowController,
        _exportButton,
        _filterButton,
        _filterPanelOnGridPolicy,
        _functionId,
        _gridDataLocator,
        _headerHeight,
        _importButton,
        _insertRowsOnTop,
        _intercellSpacing,
        _lockedColumns,
        _lockedRowsOnTop,
        _lockedRowsOnBottom,
        _maxNumberOfRowsOnInsert,
        _maxSortedColumns,
        _insertButton,
        _mode,
        _navBar,
        _orderWithLoadData,
        _overwriteRowWhenExpanding,
        _preferredSize,
        _reloadButton,
        _reorderingAllowed,
        _resizingAllowed,
        _rowHeight,
        _rowHeightFixed,
        _rowMargin,
        _saveButton,
        _searchAdditionalRows,
        _selectionMode,
        _showFilterPanelOnGrid,
        _showPageNumber,
        _showWarnMessageBeforeReloading,
        _singleExpandableRow,
        _valueObjectClassName,
        _visibleStatusPanel
      };
      return pds;
    }
    catch(IntrospectionException ex) {
      ex.printStackTrace();
      return null;
    }
  }


  public java.awt.Image getIcon(int iconKind) {
    switch (iconKind) {
      case BeanInfo.ICON_COLOR_16x16:
        return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
      case BeanInfo.ICON_COLOR_32x32:
        return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
      case BeanInfo.ICON_MONO_16x16:
        return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
      case BeanInfo.ICON_MONO_32x32:
        return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
    }
    return null;
  }



  public BeanDescriptor getBeanDescriptor() {
    BeanDescriptor bd =
        new BeanDescriptor(GridControl.class);
    bd.setName("GridControl");
    bd.setValue("preferred",Boolean.TRUE);
    bd.setValue("containerDelegate","getColumnContainer");
    bd.setValue("isContainer",Boolean.TRUE);
    bd.setValue("layoutManager", FlowLayout.class);
    return bd;
  }




}
