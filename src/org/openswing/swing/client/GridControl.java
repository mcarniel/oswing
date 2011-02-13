package org.openswing.swing.client;

import java.beans.*;
import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import org.openswing.swing.form.client.*;
import org.openswing.swing.logger.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.filter.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.table.model.client.*;
import org.openswing.swing.table.profiles.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import org.openswing.swing.export.java.GridExportOptions;
import org.openswing.swing.table.permissions.java.GridPermissions;
import org.openswing.swing.table.client.OrderPolicy;
import org.openswing.swing.message.receive.java.ValueObject;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid based on VOListTableModel.</p>
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
public class GridControl extends JPanel {

  /** grid contained in this panel; it will be created only on run-time */
  private Grids table = null;

  /** top grid contained in this panel (optional); it will be created only on run-time */
  private Grids topTable = null;

  /** bottom grid contained in this panel (optional); it will be created only on run-time */
  private Grids bottomTable = null;

  /** columns container; used only in design-time */
  private ColumnContainer itsColumnContainer = new ColumnContainer();

  /** scrollpane used to contain the columns container; used only in design-time */
  private JScrollPane designScrollPane = new JScrollPane();

  /** layout used by the columns container */
  private FlowLayout flowLayout1 = new FlowLayout();

  /** layout used by the scrollpane */
  private BorderLayout borderLayout1 = new BorderLayout(0,0);

  /** value object class name (all rows of the grid will have a value object of this type) */
  private String valueObjectClassName = null;

  /** flag used to specify if column reordering is allowed */
  private boolean reorderingAllowed = true;

  /** flag used to specify if column resizing is allowed */
  private boolean resizingAllowed = true;

  /** current grid mode; default value: READONLY */
  private int mode = Consts.READONLY;

  /** grid container */
  private GridController controller = new GridController();

  /* status panel */
  private GridStatusPanel labelPanel = new GridStatusPanel();

  /** panel used to contain the grid and the status panel */
  private JPanel gridStatusPanel = new JPanel();

  /** layout used by panel used to contain the grid and the status panel */
  private BorderLayout borderLayout2 = new BorderLayout(0,0);

  /** flag used to specify if the status panel is visible; default value: "true" */
  private boolean visibleStatusPanel = true;

  /** grid selection mode; default value: SINGLE_SELECTION */
  private int selectionMode = ListSelectionModel.SINGLE_SELECTION;

  /** insert button */
  private InsertButton insertButton = null;

  /** export button */
  private ExportButton exportButton = null;

  /** import button */
  private ImportButton importButton = null;

  /** filter/sort button */
  private FilterButton filterButton = null;

  /** copy button */
  private CopyButton copyButton = null;

  /** edit button */
  private EditButton editButton = null;

  /** reload/cancel button */
  private ReloadButton reloadButton = null;

  /** delete button */
  private DeleteButton deleteButton = null;

  /** save button */
  private SaveButton saveButton = null;

  /** flag used to specify if data loading will be automatically performed when the grid is set to visible; default value: "true". */
  private boolean autoLoadData = true;

  /** identifier (functionId) associated to the container */
  private String functionId = null;

  /** other grid parameters */
  private Map otherGridParams = new HashMap();

  /** grid data locator; can be set to "ClientGridDataLocator" or to "ServerGridDataLocator" */
  private GridDataLocator gridDataLocator = null;

  /** navigation bar (optional) */
  private NavigatorBar navBar = null;

  /** maximum number of sorted columns */
  private int maxSortedColumns = 1;

  /** number of locked columns, i.e. columns anchored to the left side of the grid; default value: 0 */
  private int lockedColumns = 0;

  /** define where to anchor locked columns: to the left or to the right of the grid; default value: <code>true</code> i.e. to the left */
  private boolean anchorLockedColumnsToLeft = true;

  /** grid identifier (optional); used in drag 'n drop events */
  private String gridId = null;

  /** cache used to temporally store buttons disabilitation policies (until Gris is set to visibile) */
  private ArrayList buttonsNotEnabled = new ArrayList();

  /** generic buttons */
  private ArrayList genericButtons = new ArrayList();

  /** maximum number of rows to insert by pressing "down" key; default value: 1 */
  private int maxNumberOfRowsOnInsert = 1;

  /** list of columns added to grid */
  private ArrayList components = new ArrayList();

  /** row height of the grid control; default value: ClientSettings.CELL_HEIGHT */
  private int rowHeight = ClientSettings.CELL_HEIGHT;

  /** flag used to define if row height can change for each row, according to image height included in a cell of grid; default value: <code>true</code> */
  private boolean rowHeightFixed = true;

  /** flag used to define if grid sorting operation must always invoke loadData method to retrieve a new list of v.o. or the grid must sort the current v.o. list without invoking loadData (only with the whole result set loaded); default value: <code>true</code> */
  private boolean orderWithLoadData = true;

  /** interface that must be implemented in order to sort columns */
  private OrderPolicy orderPolicy = new OrderPolicy();

  /** <code>true</code> to automatically show a filter panel when moving mouse at right of the grid; <code>false</code> to do not show it */
  private boolean showFilterPanelOnGrid = ClientSettings.FILTER_PANEL_ON_GRID;

  /** this property is used only when "showFilterPanelOnGrid" is set to <code>true</code>; define filter panel policy for hiding it; allowed values: Consts.FILTER_PANEL_ON_GRID_xxx; default value: ClientSettings.FILTER_PANEL_ON_GRID_POLICY */
  private int filterPanelOnGridPolicy = ClientSettings.FILTER_PANEL_ON_GRID_POLICY;

  /** split pane used to split grid component from filter panel (optional) */
  private JSplitPane split = new JSplitPane();

  /** filter panel to show at the right of the grid (optional) */
  private FilterPanel filterPanel = null;

  /** number of rows locked (anchored) on the bottom of the grid; default value: 0 */
  private int lockedRowsOnBottom = 0;

  /** number of rows locked (anchored) on the top of the grid; default value: 0 */
  private int lockedRowsOnTop = 0;

  /** top grid controller (optional) */
  private GridController topGridController = new GridController();

  /** bottom grid controller (optional) */
  private GridController bottomGridController = new GridController();

  /** top grid data locator (optional) */
  private GridDataLocator topGridDataLocator = null;

  /** bottom grid data locator (optional) */
  private GridDataLocator bottomGridDataLocator = null;

  /** container of all grids (top, std and bottom (locked and non) */
  private JPanel tmpPanel = new JPanel();

  /** default value that could be set in the quick filter criteria; default value: ClientSettings.DEFAULT_QUICK_FILTER_CRITERIA; values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH */
  private int defaultQuickFilterCriteria = ClientSettings.DEFAULT_QUICK_FILTER_CRITERIA;

  /** intercell spacing; default value = ClientSettings.INTERCELL_SPACING */
  private Dimension spacing = ClientSettings.INTERCELL_SPACING;

  /** row margin; default value = ClientSettings.ROW_MARGIN */
  private int rowMargin = ClientSettings.ROW_MARGIN;

  /** flag used to define if an inner v.o. must be automatically instantiated when a setter method is invoked; default value: <code>true</code> */
  private boolean createInnerVO = true;

  /** list of objects {int[],int[]} used to merge cells */
  private ArrayList mergedCells = new ArrayList();

  /** flag used to define if background and foreground colors must be setted according to GridController definition only in READONLY mode; default value: <code>true</code> */
  private boolean colorsInReadOnlyMode = true;

  /** list of ActionListener objetcs related to the event "loading data completed" */
  private ArrayList loadDataCompletedListeners = new ArrayList();

  /** list of custom commands added to the popup menu accessed by right mouse click on grid */
  private ArrayList popupCommands = new ArrayList();

  /** current grid profile */
  private GridProfile profile = null;

  /** default grid profile */
  private GridProfile defaultProfile = null;

  /** grid permissions */
  private GridPermissions permissions = null;

  /** profile management menu */
  private JMenu profileMenu = new JMenu(ClientSettings.getInstance().getResources().getResource("grid profile management"));

  /** profile list menu */
  private JMenu profilesMenu = new JMenu(ClientSettings.getInstance().getResources().getResource("select grid profile"));

  /** flag used to anchor the last column on the right margin of the grid, only when all columns width is lesser than grid width */
  private boolean anchorLastColumn = false;

  /** column index declared as expandable, i.e. user can click on it to expand cell to show an inner component; default value: 0 (first column) */
  private int expandableColumn = 0;

  /** define whether expanded rows in the past must be collapsed when expanding the current one; used only when "overwriteRowWhenExpanding" property is not null; default value: <code>false</code> */
  private boolean singleExpandableRow = false;

  /** define whether the row to show, when expanding the current one, must be showed over the current one on in a new row below it; used only when "overwriteRowWhenExpanding" property is not null; default value: <code>false</code> i.e. do not overwrite it*/
  private boolean overwriteRowWhenExpanding = false;

  /** define the controller that manages row expansion */
  private ExpandableRowController expandableRowController = null;

  /** parent frame: JFrame or JInternalFrame */
  private JInternalFrame parentFrame = null;

  /** column properties */
  private Column[] columnProperties = null;

  /** flag used to define if a warning message must be showed when reloading data in grid and grid is in EDIT/INSERT mode; default value: <code>true</code> */
  private boolean showWarnMessageBeforeReloading = true;

  /** flag used to allow insert row (using DOWN key) in edit mode too; default value: <code>false</code> */
  private boolean allowInsertInEdit = false;

  /** define where new rows must be added: <code>true</code> at the top of the grid or <code>false</code> at the bottom; default value: <code>ClientSettings.INSERT_ROWS_ON_TOP</code> */
  private boolean insertRowsOnTop = ClientSettings.INSERT_ROWS_ON_TOP;

  /**  header height; default value: <code>ClientSettings.HEADER_HEIGHT</code> */
  private int headerHeight = ClientSettings.HEADER_HEIGHT;

  /** flag used to force the editing of one row only: the current selected row; default value: <code>false</code>, i.e. all rows are editable */
  private boolean editOnSingleRow = false;

  /** list of objects {int[],int[]} used to merge cells in top table */
  private ArrayList topTableMergedCells = new ArrayList();

  /** list of objects {int[],int[]} used to merge cells in bottom table */
  private ArrayList bottomTableMergedCells = new ArrayList();

  /** combo-box filters to apply to column headers */
  private HashMap listFilters = new HashMap();

  /** flag used to show current page number in grid; default value: ClientSettings.SHOW_PAGE_NUMBER_IN_GRID */
  public boolean showPageNumber = ClientSettings.SHOW_PAGE_NUMBER_IN_GRID;

  /** flag used in grid to enable the retrieval of additional rows in fast search, when search criteria fails; default value: ClientSettings.SEARCH_ADDITION_ROWS */
  public boolean searchAdditionalRows = ClientSettings.SEARCH_ADDITIONAL_ROWS;

  /** flag used to allow the columns sorting in edit mode too; default value: <code>false</code>; note that this setting is used only when <code>orderWithLoadData</code> property is set to <code>false</code> */
  private boolean allowColumnsSortingInEdit = false;

  /** flag used to allow the columns permission; default value:  <code>true</code> */
  private boolean allowColumnsPermission = true;

  /** flag used to allow the columns profile; default value:  <code>true</code> */
  private boolean allowColumnsProfile = true;

  /** criteria to use with findNextValue method: search for values exactly uquals to the one specified as argument in findNextValue method */
  public static int FIND_CRITERIA_EQUALS = 0;

  /** criteria to use with findNextValue method: search for values uqualsIgnoreCase to the one specified as argument in findNextValue method */
  public static int FIND_CRITERIA_EQUALS_IGNORE_CASE = 1;

  /** criteria to use with findNextValue method: search for values that starts with the one specified as argument in findNextValue method */
  public static int FIND_CRITERIA_STARTS_WITH = 2;

  /** criteria to use with findNextValue method: search for values (case insensitive) that starts with the one specified as argument in findNextValue method */
  public static int FIND_CRITERIA_STARTS_WITH_IGNORE_CASE = 3;

  /** criteria to use with findNextValue method: search for values that contains (as for value like '%value%') the one specified as argument in findNextValue method */
  public static int FIND_CRITERIA_CONTAINS = 4;

  /** criteria to use with findNextValue method: search for values that contains (case insensitive, as for VALUE like '%VALUE%') the one specified as argument in findNextValue method */
  public static int FIND_CRITERIA_CONTAINS_IGNORE_CASE = 5;


  /**
   * Costructor.
   */
  public GridControl() {
    super();
    try {
      jbInit();
      setVisibleStatusPanel(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  protected final void jbInit() throws Exception {
    gridStatusPanel.setLayout(borderLayout2);
    gridStatusPanel.add(designScrollPane,BorderLayout.CENTER);
    this.setLayout(borderLayout1);
    super.add(gridStatusPanel,BorderLayout.CENTER);
    designScrollPane.getViewport().add(itsColumnContainer);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    flowLayout1.setHgap(0);
    flowLayout1.setVgap(0);
    itsColumnContainer.setLayout(flowLayout1);
    try {
      labelPanel.setPreferredSize(new Dimension(this.getPreferredSize().width,22));
    }
    catch (Exception ex) {
    }
  }


  /**
   * Method called when the this panel is set to visible: it calls commitColumnContainer method.
   */
  public final void addNotify() {
    super.addNotify();
    commitColumnContainer();
  }


  /**
   * This method is called by addNotify method when this panel is set to visible:
   * it will create the grid and add it to this panel.
   * It must not be manually invoked!
   */
  public final void commitColumnContainer() {
    try {
      long time = System.currentTimeMillis();

      if (Beans.isDesignTime())
        return;

      // the method terminates after the first execution...
      if (itsColumnContainer == null)
        return;

      gridStatusPanel.remove(designScrollPane);

      Component[] c = (Component[]) components.toArray(new Component[components.size()]);
      columnProperties = new Column[c.length];
      for (int i = 0; i < c.length; i++) {
        if (!(c[i] instanceof Column)) {
          Logger.error(this.getClass().getName(), "commitColumnContainer", "You are not allowed to add a '"+c.getClass().getName()+"' class to grid control: only Column type components are accepted.", null);
          return;
        }
        columnProperties[i] = (Column) c[i];
        if (columnProperties[i].getListFilter()!=null)
          listFilters.put(columnProperties[i].getColumnName(),columnProperties[i].getListFilter());
      }
      table = new Grids(
          this,
          lockedColumns,
          anchorLockedColumnsToLeft,
          valueObjectClassName,
          columnProperties,
          controller,
          labelPanel,
          gridDataLocator,
          otherGridParams,
          colorsInReadOnlyMode,
          popupCommands,
          anchorLastColumn,
          expandableColumn,
          singleExpandableRow,
          overwriteRowWhenExpanding,
          expandableRowController,
          listFilters,
          headerHeight,
          searchAdditionalRows,
          allowColumnsSortingInEdit,
          Grid.MAIN_GRID
      );
      table.setEditOnSingleRow(editOnSingleRow);
      for (int i = 0; i < columnProperties.length; i++) {
        columnProperties[i].setTable(table);

        if (columnProperties[i].getDefaultFilterValues()!=null)
          table.getQuickFilterValues().put(
            columnProperties[i].getColumnName(),
            columnProperties[i].getDefaultFilterValues()
          );
      }


      // apply grid permissions, if defined...
      if (ClientSettings.getInstance().GRID_PERMISSION_MANAGER!=null &&
          functionId!=null &&
          !functionId.trim().equals("") &&
          allowColumnsPermission) {
        try {
          String[] columnsAttribute = new String[columnProperties.length];
          String[] headerColumnsName = new String[columnProperties.length];
          boolean[] columnsVisibility = new boolean[columnProperties.length];
          boolean[] columnsEditableInIns = new boolean[columnProperties.length];
          boolean[] columnsEditableInEdit = new boolean[columnProperties.length];
          boolean[] columnsMandatory = new boolean[columnProperties.length];
          for(int i=0;i<columnProperties.length;i++) {
            columnsAttribute[i] = columnProperties[i].getColumnName();
            headerColumnsName[i] = columnProperties[i].getHeaderColumnName();
            columnsVisibility[i] = columnProperties[i].isColumnVisible();
            columnsEditableInIns[i] = columnProperties[i].isEditableOnInsert();
            columnsEditableInEdit[i] = columnProperties[i].isEditableOnEdit();
            columnsMandatory[i] = columnProperties[i].isColumnRequired();
          }

          // compare current and last digests...
          String lastDigest = (String)ClientSettings.getInstance().getLastGridPermissionsDigests().get(functionId);
          if (lastDigest==null) {
            lastDigest = ClientSettings.getInstance().GRID_PERMISSION_MANAGER.getLastGridDigest(functionId);
            if (lastDigest!=null)
              ClientSettings.getInstance().getLastGridPermissionsDigests().put(functionId,lastDigest);
          }
          String currentDigest = ClientSettings.getInstance().GRID_PERMISSION_MANAGER.getCurrentGridDigest(columnsAttribute,functionId);
          if (!currentDigest.equals(lastDigest)) {
            // restore grid digest, remove all grid permissions and create grid permissions defaults...
            ClientSettings.getInstance().GRID_PERMISSION_MANAGER.storeGridDigest(functionId,currentDigest);
            ClientSettings.getInstance().GRID_PERMISSION_MANAGER.deleteAllGridPermissionsPerFunctionId(functionId);
            ClientSettings.getInstance().GRID_PERMISSION_MANAGER.storeGridPermissionsDefaults(
              functionId,columnsAttribute,headerColumnsName,columnsVisibility,columnsEditableInIns,columnsEditableInEdit,columnsMandatory
            );
            ClientSettings.getInstance().getLastGridPermissionsDigests().put(functionId,currentDigest);
            ClientSettings.getInstance().getGridPermissions().remove(functionId);
          }

          applyGridPermissions(columnsAttribute,columnsVisibility,columnsEditableInIns,columnsEditableInEdit,columnsMandatory);
        }
        catch (Throwable ex) {
          Logger.error(this.getClass().getName(), "commitColumnContainer", "Error while fetching grid permissions:\n"+ex.getMessage(),ex);
        }

      }

      if (ClientSettings.getInstance().GRID_PROFILE_MANAGER!=null &&
          functionId!=null &&
          !functionId.trim().equals("") &&
          allowColumnsProfile) {
        try {
          // compare current and last digests...
          String lastDigest = (String)ClientSettings.getInstance().getLastUserGridDigests().get(functionId);
          if (lastDigest==null) {
            lastDigest = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getLastGridDigest(functionId);
            if (lastDigest!=null)
              ClientSettings.getInstance().getLastUserGridDigests().put(functionId,lastDigest);
          }
          String[] columnNames = new String[columnProperties.length];
          for(int i=0;i<columnProperties.length;i++)
            columnNames[i] = columnProperties[i].getColumnName();
          String currentDigest = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getCurrentGridDigest(columnNames,functionId);
          if (!currentDigest.equals(lastDigest)) {
            // restore grid digest and remove all grid profiles...
            ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeGridDigest(functionId,currentDigest);
            ClientSettings.getInstance().GRID_PROFILE_MANAGER.deleteAllGridProfiles(functionId);
            ClientSettings.getInstance().GRID_PROFILE_MANAGER.deleteAllGridProfileIds(functionId);
            ClientSettings.getInstance().getLastUserGridDigests().put(functionId,currentDigest);
            ClientSettings.getInstance().getLastUserGridProfileIds().remove(functionId);
            ClientSettings.getInstance().getUserGridProfiles().remove(functionId);
            ClientSettings.getInstance().getGridProfileDescriptions().remove(functionId);
          }

          addPopupCommand(profileMenu);

          // define default profile...
          setDefaultProfile(columnProperties);

          Object id = ClientSettings.getInstance().getLastUserGridProfileIds().get(functionId);
          if (id==null) {
            id = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getLastGridProfileId(functionId);
            if (id!=null)
              ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId,id);
          }

          if (id!=null) {
            // retrieve a previously stored profile...
            try {
              profile = (GridProfile)ClientSettings.getInstance().getUserGridProfiles(functionId).get(id);
              if (profile==null) {
                profile = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getUserProfile(functionId, id);
                if (profile!=null) {
                  ClientSettings.getInstance().getUserGridProfiles(functionId).put(id,profile);
//                  ClientSettings.getInstance().getGridProfileDescriptions(functionId).add(new GridProfileDescription(profile.getId(),profile.getDescription(),profile.isDefaultProfile()));
                }
              }
            }
            catch (IOException ex1) {
              Logger.error(this.getClass().getName(), "commitColumnContainer", ex1.getMessage()+": "+id,ex1);
              profile = (GridProfile)defaultProfile.clone();

              id = ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeUserProfile(profile);
              profile.setId(id);
              ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeGridProfileId(functionId, profile.getId());
              if (profile!=null) {
                ClientSettings.getInstance().getUserGridProfiles(functionId).put(profile.getId(),profile);
                ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId,profile.getId());
//                ClientSettings.getInstance().getGridProfileDescriptions(functionId).add(new GridProfileDescription(profile.getId(),profile.getDescription(),profile.isDefaultProfile()));
              }
            }
          }
          else {
            // create a new profile...
            profile = (GridProfile)defaultProfile.clone();

            id = ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeUserProfile(profile);
            profile.setId(id);
            ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeGridProfileId(functionId, profile.getId());
            if (profile!=null) {
              ClientSettings.getInstance().getUserGridProfiles(functionId).put(profile.getId(),profile);
              ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId,profile.getId());
              ClientSettings.getInstance().getGridProfileDescriptions(functionId).add(new GridProfileDescription(profile.getId(),profile.getDescription(),profile.isDefaultProfile()));
            }
          }

          JMenuItem restoreMenu = new JMenuItem(ClientSettings.getInstance().getResources().getResource("restore default grid profile"));
          restoreMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              try {
                for(int i=0;i<profilesMenu.getMenuComponentCount();i++)
                  ((JCheckBoxMenuItem)profilesMenu.getMenuComponent(i)).setSelected(false);

                // remove previous default profile from the storage media...
                profile = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getDefaultProfile(functionId);
                if (profile!=null) {
                  ClientSettings.getInstance().GRID_PROFILE_MANAGER.deleteUserProfile(functionId,profile.getId());
                  ClientSettings.getInstance().getUserGridProfiles(functionId).remove(profile.getId());
//                  ClientSettings.getInstance().getGridProfileDescriptions(functionId).remove(new GridProfileDescription(profile.getId(),profile.getDescription(),profile.isDefaultProfile()));
                }

                profile = (GridProfile)defaultProfile.clone();
                Object id = ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeUserProfile(profile);
                profile.setId(id);
                ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeGridProfileId(functionId, profile.getId());
                applyProfile(columnProperties, profile, true);

                if (profile!=null) {
                  ClientSettings.getInstance().getUserGridProfiles(functionId).put(profile.getId(),profile);
                  ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId,profile.getId());
//                  ClientSettings.getInstance().getGridProfileDescriptions(functionId).add(new GridProfileDescription(profile.getId(),profile.getDescription(),profile.isDefaultProfile()));
                }
              }
              catch (Throwable ex) {
                Logger.error(this.getClass().getName(), "commitColumnContainer", "Error while storing grid profile: "+ex.getMessage(),ex);
                return;
              }
            }
          });
          profileMenu.add(restoreMenu);

          JMenuItem newMenu = new JMenuItem(ClientSettings.getInstance().getResources().getResource("create new grid profile"));
          newMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

              String desc = OptionPane.showInputDialog(
                GridControl.this,
                "profile description",
                "create new grid profile",
                JOptionPane.QUESTION_MESSAGE
              );
              if (desc==null)
                return;

              GridProfile profile = (GridProfile)defaultProfile.clone();
              profile.setId(null);
              profile.setDefaultProfile(false);
              profile.setDescription(desc);
              try {
                Object id = ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeUserProfile(profile);
                profile.setId(id);
                ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeGridProfileId(functionId, profile.getId());
                applyProfile(columnProperties, profile, true);
                ClientSettings.getInstance().getUserGridProfiles(functionId).put(profile.getId(),profile);
                ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId,profile.getId());
                ClientSettings.getInstance().getGridProfileDescriptions(functionId).add(new GridProfileDescription(profile.getId(),profile.getDescription(),profile.isDefaultProfile()));
              }
              catch (Throwable ex) {
                Logger.error(this.getClass().getName(), "commitColumnContainer", "Error while storing grid profile: "+ex.getMessage(),ex);
                return;
              }
              final Object id = profile.getId();

              for(int i=0;i<profilesMenu.getMenuComponentCount();i++)
                ((JCheckBoxMenuItem)profilesMenu.getMenuComponent(i)).setSelected(false);
              final JCheckBoxMenuItem item = new JCheckBoxMenuItem(profile.getDescription(),true);
              item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  try {
                    for(int i=0;i<profilesMenu.getMenuComponentCount();i++)
                      ((JCheckBoxMenuItem)profilesMenu.getMenuComponent(i)).setSelected(false);
                    item.setSelected(true);
                    maybeStoreProfile(columnProperties);
                    GridControl.this.profile = (GridProfile)ClientSettings.getInstance().getUserGridProfiles(functionId).get(id);
                    if (GridControl.this.profile==null) {
                      GridControl.this.profile = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getUserProfile(functionId,id);
                      if (GridControl.this.profile!=null) {
                        ClientSettings.getInstance().getUserGridProfiles(functionId).put(id,GridControl.this.profile);
                      }
                    }
                    applyProfile(columnProperties,GridControl.this.profile,true);
                    ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId, GridControl.this.profile.getId());
                  }
                  catch (Throwable ex) {
                    Logger.error(this.getClass().getName(), "commitColumnContainer", "Error while fetching grid profile: "+ex.getMessage(),ex);
                  }
                }
              });

              if (profilesMenu.getComponentCount()==0) {
                profileMenu.add(profilesMenu);
                profileMenu.revalidate();
              }
              profilesMenu.add(item);

            }
          });
          profileMenu.add(newMenu);

          JMenuItem removeMenu = new JMenuItem(ClientSettings.getInstance().getResources().getResource("remove current grid profile"));
          removeMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
              try {
                if (profile.getId().equals(defaultProfile.getId())) // this test should be unuseful...
                  return;

                for(int i=0;i<profilesMenu.getMenuComponentCount();i++)
                  ((JCheckBoxMenuItem)profilesMenu.getMenuComponent(i)).setSelected(false);

                String descriptionToRemove = profile.getDescription();
                ClientSettings.getInstance().GRID_PROFILE_MANAGER.deleteUserProfile(functionId,profile.getId());
                ClientSettings.getInstance().getUserGridProfiles(functionId).remove(profile.getId());
                ClientSettings.getInstance().getGridProfileDescriptions(functionId).remove(new GridProfileDescription(profile.getId(),profile.getDescription(),profile.isDefaultProfile()));

                profile = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getDefaultProfile(functionId);
                if (profile==null) {
                  profile = (GridProfile)defaultProfile.clone();
                  Object id = ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeUserProfile(profile);
                  profile.setId(id);
                  ClientSettings.getInstance().getUserGridProfiles(functionId).put(profile.getId(),profile);
                  ClientSettings.getInstance().getGridProfileDescriptions(functionId).add(new GridProfileDescription(profile.getId(),profile.getDescription(),profile.isDefaultProfile()));
                }
                ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeGridProfileId(functionId,profile.getId());
                ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId,profile.getId());
                applyProfile(columnProperties,profile,true);

                for(int i=0;i<profilesMenu.getMenuComponentCount();i++)
                  if (((JMenuItem)profilesMenu.getMenuComponent(i)).getText().equals(descriptionToRemove)) {
                    profilesMenu.remove(i);
                    profilesMenu.revalidate();

                    if (profileMenu.getComponentCount()==0)
                      profileMenu.getParent().remove(profileMenu);

                    break;
                  }

              }
              catch (Throwable ex) {
                Logger.error(this.getClass().getName(), "commitColumnContainer", "Error while removing grid profile: "+ex.getMessage(),ex);
              }
            }
          });
          profileMenu.add(removeMenu);

          profileMenu.addSeparator();

          try {
            ArrayList list =  ClientSettings.getInstance().getGridProfileDescriptions(functionId);
            if (list.size()==0) {
              list = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getUserProfiles(functionId);
              ClientSettings.getInstance().getGridProfileDescriptions(functionId).addAll(list);
            }
//            Arrays.sort(list,new Comparator() {
//
//              public boolean equals(Object obj) {
//                return obj.equals(this);
//              }
//
//              public int compare(Object o1, Object o2) {
//                GridProfileDescription p1 = (GridProfileDescription)o1;
//                GridProfileDescription p2 = (GridProfileDescription)o2;
//                return p1.getDescription().compareTo(p2.getDescription());
//              }
//
//            });

            for(int i=0;i<list.size();i++) {
              final GridProfileDescription desc = (GridProfileDescription)list.get(i);
              if (((GridProfileDescription)list.get(i)).isDefaultProfile())
                continue;
              final JCheckBoxMenuItem item = new JCheckBoxMenuItem(desc.getDescription());
              item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                  for(int i=0;i<profilesMenu.getMenuComponentCount();i++)
                    ((JCheckBoxMenuItem)profilesMenu.getMenuComponent(i)).setSelected(false);
                  item.setSelected(true);

                  Object id = desc.getId();
                  try {
                    maybeStoreProfile(columnProperties);

                    profile = (GridProfile)ClientSettings.getInstance().getUserGridProfiles(functionId).get(id);
                    if (profile==null) {
                      profile = ClientSettings.getInstance().GRID_PROFILE_MANAGER.getUserProfile(functionId,id);
                      ClientSettings.getInstance().getUserGridProfiles(functionId).put(id,profile);
                    }
                    ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeGridProfileId(functionId,id);
                    ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId,id);
                    applyProfile(columnProperties,profile,true);
                  }
                  catch (Throwable ex) {
                    Logger.error(this.getClass().getName(), "commitColumnContainer", "Error while fetching grid profile: "+ex.getMessage(),ex);
                  }
                }
              });
              if (profile.getId().equals(desc.getId()))
                item.setSelected(true);
              profilesMenu.add(item);
            }
            if (profilesMenu.getMenuComponentCount()>0)
              profileMenu.add(profilesMenu);
          }
          catch (Throwable ex) {
            Logger.error(this.getClass().getName(), "commitColumnContainer", "Error while fetching grid profiles: "+ex.getMessage(),ex);
          }

          // add a "dispose grid" listener...
          this.addAncestorListener(new AncestorListener() {

               public void ancestorAdded(AncestorEvent event) {
               }

               public void ancestorMoved(AncestorEvent event) {
               }

               public void ancestorRemoved(AncestorEvent event) {
                 maybeStoreProfile(columnProperties);
               }
          });
        }
        catch (Throwable ex) {
          ex.printStackTrace();
          profile = null;
        }
      } // end if on GRID_PROFILE_MANAGER usage
      else if ( ClientSettings.getInstance().GRID_PROFILE_MANAGER!=null &&
                functionId!=null &&
                !functionId.trim().equals("") &&
                allowColumnsProfile)
        Logger.warn(this.getClass().getName(), "commitColumnContainer", "Grid profile not enabled because no 'functionId' property setted on GridControl");

      table.setReorderingAllowed(reorderingAllowed);
      table.setSelectionMode(selectionMode);
      table.setMaxSortedColumns(maxSortedColumns);
      if (insertButton != null)
        table.setInsertButton(insertButton);
      if (exportButton != null)
        table.setExportButton(exportButton);
      if (importButton != null)
        table.setImportButton(importButton);
      if (filterButton != null)
        table.setFilterButton(filterButton);
      if (copyButton != null)
        table.setCopyButton(copyButton);
      if (editButton != null)
        table.setEditButton(editButton);
      if (reloadButton != null)
        table.setReloadButton(reloadButton);
      if (deleteButton != null)
        table.setDeleteButton(deleteButton);
      if (saveButton != null)
        table.setSaveButton(saveButton);
      if (navBar != null)
        table.setNavBar(navBar);

      GenericButton button = null;
      for (int i = 0; i < genericButtons.size(); i++) {
        button = (GenericButton) genericButtons.get(i);
        table.getGenericButtons().add(button);
        button.addDataController(this.table);
      }

      table.setFunctionId(functionId);

      // add buttons disabilitation policies...
      Object[] policy = null;
      for (int i = 0; i < buttonsNotEnabled.size(); i++) {
        policy = (Object[]) buttonsNotEnabled.get(i);
        table.addButtonsNotEnabled( (HashSet) policy[0],
                                   (GenericButtonController) policy[1]);
      }

      if (gridId != null)
        table.enableDrag(gridId);

      if (spacing != null)
        table.setIntercellSpacing(spacing);
      if (rowMargin != -1)
        table.setRowMargin(rowMargin);


      // add top grid (optionally)...
      if (lockedRowsOnTop>0) {
        topTable = new Grids(
            this,
            lockedColumns,
            anchorLockedColumnsToLeft,
            valueObjectClassName,
            columnProperties,
            topGridController,
            labelPanel,
            topGridDataLocator,
            otherGridParams,
            colorsInReadOnlyMode,
            new ArrayList(),
            anchorLastColumn,
            0,
            false,
            false,
            null,
            listFilters,
            headerHeight,
            searchAdditionalRows,
            allowColumnsSortingInEdit,
            Grid.TOP_GRID
        );
        topTable.setEditOnSingleRow(editOnSingleRow);
        topTable.setReorderingAllowed(reorderingAllowed);
        topTable.setResizingAllowed(resizingAllowed);
        topTable.setSelectionMode(selectionMode);
        topTable.setMaxSortedColumns(maxSortedColumns);

  //      table.setFunctionId(functionId);

        topTable.getVOListTableModel().setMode(Consts.READONLY);

        topTable.getGrid().addFocusListener(new FocusListener() {

          public void focusGained(FocusEvent e) {

            if (parentFrame==null) {
              parentFrame = ClientUtils.getParentInternalFrame(GridControl.this);
            }
            if (parentFrame!=null && !parentFrame.isSelected()) {
              if (parentFrame.getDesktopPane().getSelectedFrame()!=null)
                parentFrame.getDesktopPane().getSelectedFrame().requestFocus();
              return;
            }

            tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_FOCUS_BORDER,1));
            dropFocusFromAllForms();
          }

          public void focusLost(FocusEvent e) {
            tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_NO_FOCUS_BORDER,1));
          }
        });

        if (lockedColumns>0)
          topTable.getLockedGrid().addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {

              if (parentFrame==null) {
                parentFrame = ClientUtils.getParentInternalFrame(GridControl.this);
              }
              if (parentFrame!=null && !parentFrame.isSelected()) {
                if (parentFrame.getDesktopPane().getSelectedFrame()!=null)
                  parentFrame.getDesktopPane().getSelectedFrame().requestFocus();
                return;
              }

              tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_FOCUS_BORDER,1));
              dropFocusFromAllForms();
            }

            public void focusLost(FocusEvent e) {
              tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_NO_FOCUS_BORDER,1));
            }
          });


        if (spacing!=null)
          topTable.setIntercellSpacing(spacing);
        if (rowMargin!=-1)
          topTable.setRowMargin(rowMargin);


      } // end top grid...


        // add bottom grid (optionally)...
      if (lockedRowsOnBottom>0) {
        bottomTable = new Grids(
            this,
            lockedColumns,
            anchorLockedColumnsToLeft,
            valueObjectClassName,
            columnProperties,
            bottomGridController,
            labelPanel,
            bottomGridDataLocator,
            otherGridParams,
            colorsInReadOnlyMode,
            new ArrayList(),
            anchorLastColumn,
            0,
            false,
            false,
            null,
            new HashMap(),
            headerHeight,
            searchAdditionalRows,
            allowColumnsSortingInEdit,
            Grid.BOTTOM_GRID
        );
        bottomTable.setEditOnSingleRow(editOnSingleRow);
        bottomTable.setReorderingAllowed(reorderingAllowed);
        bottomTable.setResizingAllowed(resizingAllowed);
        bottomTable.setSelectionMode(selectionMode);
        bottomTable.setMaxSortedColumns(maxSortedColumns);

    //      table.setFunctionId(functionId);

        bottomTable.getVOListTableModel().setMode(Consts.READONLY);

        bottomTable.getGrid().addFocusListener(new FocusListener() {

          public void focusGained(FocusEvent e) {

            if (parentFrame==null) {
              parentFrame = ClientUtils.getParentInternalFrame(GridControl.this);
            }
            if (parentFrame!=null && !parentFrame.isSelected()) {
              if (parentFrame.getDesktopPane().getSelectedFrame()!=null)
                parentFrame.getDesktopPane().getSelectedFrame().requestFocus();
              return;
            }

            tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_FOCUS_BORDER,1));
            dropFocusFromAllForms();
          }

          public void focusLost(FocusEvent e) {
            tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_NO_FOCUS_BORDER,1));
          }
        });

        if (lockedColumns>0)
          bottomTable.getLockedGrid().addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {

              if (parentFrame==null) {
                parentFrame = ClientUtils.getParentInternalFrame(GridControl.this);
              }
              if (parentFrame!=null && !parentFrame.isSelected()) {
                if (parentFrame.getDesktopPane().getSelectedFrame()!=null)
                  parentFrame.getDesktopPane().getSelectedFrame().requestFocus();
                return;
              }

              tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_FOCUS_BORDER,1));
              dropFocusFromAllForms();
            }

            public void focusLost(FocusEvent e) {
              tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_NO_FOCUS_BORDER,1));
            }
          });

        if (spacing!=null)
          bottomTable.setIntercellSpacing(spacing);
        if (rowMargin!=-1)
          bottomTable.setRowMargin(rowMargin);


      } // end bottom grid...



      tmpPanel.setLayout(new GridBagLayout());


      if (topTable!=null) {
        topTable.setMinimumSize(new Dimension(getWidth(),lockedRowsOnTop*rowHeight));
        tmpPanel.add(topTable,
                       new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, lockedRowsOnTop*rowHeight));
      }
      tmpPanel.add(table,
                     new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.BOTH,
                                            new Insets(0, 0, 0, 0), 0, 0));
      if (bottomTable!=null) {
        bottomTable.setMinimumSize(new Dimension(getWidth(),lockedRowsOnBottom*rowHeight));
        tmpPanel.add(bottomTable,
                       new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.BOTH,
                                              new Insets(0, 0, 0, 0), 0, lockedRowsOnBottom*rowHeight));
      }
      table.getGrid().addFocusListener(new FocusListener() {

        public void focusGained(FocusEvent e) {

          if (parentFrame==null) {
            parentFrame = ClientUtils.getParentInternalFrame(GridControl.this);
          }
          if (parentFrame!=null && !parentFrame.isSelected()) {
            if (parentFrame.getDesktopPane().getSelectedFrame()!=null)
              parentFrame.getDesktopPane().getSelectedFrame().requestFocus();
            return;
          }


          if (table.getCurrentNestedComponent()!=null) {
            Container cont = (Container)table.getCurrentNestedComponent();
            while(cont!=null && !(cont instanceof Form) && !cont.equals(table.getGrid()))
              cont = cont.getParent();
            if (cont!=null && cont instanceof Form) {
              ((Form)cont).setFocusOnForm();
              return;
            }
          }

          tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_FOCUS_BORDER,1));
          dropFocusFromAllForms();
        }

        public void focusLost(FocusEvent e) {
//          if (e.getSource()!=null && e.getSource() instanceof Component) {
//            Component c = (Component)e.getSource();
//            while(c!=null && !Consts.EXPANDED_COMPONENT.equals(c.getName()))
//              c = c.getParent();
//            if (c!=null && Consts.EXPANDED_COMPONENT.equals(c.getName())) {
//              table.dispatchEvent(new FocusEvent(c,FocusEvent.FOCUS_GAINED,false));
//              throw new RuntimeException("");
//            }
//
//          }
          tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_NO_FOCUS_BORDER,1));
//          if (table.getCurrentNestedComponent()!=null)
//            throw new RuntimeException("");

        }
      });

      if (lockedColumns>0)
        table.getLockedGrid().addFocusListener(new FocusListener() {

          public void focusGained(FocusEvent e) {

            if (parentFrame==null) {
              parentFrame = ClientUtils.getParentInternalFrame(GridControl.this);
            }
            if (parentFrame!=null && !parentFrame.isSelected()) {
              if (parentFrame.getDesktopPane().getSelectedFrame()!=null)
                parentFrame.getDesktopPane().getSelectedFrame().requestFocus();
              return;
            }

            tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_FOCUS_BORDER,1));
            dropFocusFromAllForms();
          }

          public void focusLost(FocusEvent e) {
            tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_NO_FOCUS_BORDER,1));
          }
        });


      gridStatusPanel.add(tmpPanel,BorderLayout.CENTER);
      if (visibleStatusPanel)
        gridStatusPanel.add(labelPanel,BorderLayout.SOUTH);

      if (showFilterPanelOnGrid) {
        filterPanel = new FilterPanel(columnProperties,table,filterPanelOnGridPolicy);

        split.setOrientation(split.HORIZONTAL_SPLIT);
        split.setDividerSize(1);
        split.add(gridStatusPanel,split.LEFT);
        split.add(filterPanel,split.RIGHT);
        super.add(split, BorderLayout.CENTER);
        split.setDividerLocation(2048);
        table.getGrid().addMouseListener(new MouseAdapter() {

          public void mouseEntered(MouseEvent e) {
            if (filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_CLOSE_ON_EXIT ||
                filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED && !filterPanel.isLocked() ||
                filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED && !filterPanel.isLocked())
              split.setDividerLocation(split.getWidth()-10);
          }

        });

         if (filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_CLOSE_BUTTON) {
           filterPanel.getClosePanel().addMouseListener(new MouseAdapter() {
             public void mouseClicked(MouseEvent e) {
               split.setDividerLocation(split.getWidth()-10);
             }
           });
         }

        if (table.getLockedGrid()!=null)
          table.getLockedGrid().addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
              split.setDividerLocation(split.getWidth()-10);
            }

          });

        filterPanel.addMouseListener(new MouseAdapter() {

          public void mouseEntered(MouseEvent e) {
            if (split.getDividerLocation()<=split.getWidth()-(int)filterPanel.getPreferredSize().getWidth()-20)
              return;

            if (!(filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED && filterPanel.isLocked()) &&
                !(filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED && filterPanel.isLocked()))
              filterPanel.init();

            if (split.getLastDividerLocation()>0 &&
                split.getLastDividerLocation()<filterPanel.getPreferredSize().getWidth() &&
                filterPanel.getPreferredSize().getWidth()<300)
              split.setDividerLocation(split.getLastDividerLocation());
            else
              split.setDividerLocation(split.getWidth()-(int)filterPanel.getPreferredSize().getWidth()-20);
          }

          public void mouseExited(MouseEvent e) {
            if (filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_CLOSE_ON_EXIT ||
                filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_PRESSED && !filterPanel.isLocked() ||
                filterPanelOnGridPolicy==Consts.FILTER_PANEL_ON_GRID_USE_PADLOCK_UNPRESSED && !filterPanel.isLocked()) {
              if (e.getX()<=0 || e.getY()<=0 || e.getX()>=filterPanel.getWidth() || e.getY()>=filterPanel.getHeight())
                split.setDividerLocation(split.getWidth()-10);
            }
          }

        });

      }

      gridStatusPanel.revalidate();
      gridStatusPanel.repaint();

      itsColumnContainer = null;

      tmpPanel.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_NO_FOCUS_BORDER,1));


      table.getVOListTableModel().setMode(mode);
      table.setMaxNumberOfRowsOnInsert(maxNumberOfRowsOnInsert);
      table.setAllowInsertInEdit(allowInsertInEdit);
      table.setInsertRowsOnTop(insertRowsOnTop);

      if (mode==Consts.READONLY && autoLoadData)
        table.reload();

      for(int i=0;i<loadDataCompletedListeners.size();i++)
        table.addLoadDataCompletedListener((ActionListener)loadDataCompletedListeners.get(i));

      table.addLoadDataCompletedListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          for(int i=0;i<mergedCells.size();i++)
            table.mergeCells(
              (int[])((Object[])mergedCells.get(i))[0],
              (int[])((Object[])mergedCells.get(i))[1]
            );
        }
      });

      if (topTable!=null)
        topTable.addLoadDataCompletedListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            for(int i=0;i<topTableMergedCells.size();i++)
              topTable.mergeCells(
                (int[])((Object[])topTableMergedCells.get(i))[0],
                (int[])((Object[])topTableMergedCells.get(i))[1]
              );
          }
        });

      if (bottomTable!=null)
        bottomTable.addLoadDataCompletedListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            for(int i=0;i<bottomTableMergedCells.size();i++)
              bottomTable.mergeCells(
                (int[])((Object[])bottomTableMergedCells.get(i))[0],
                (int[])((Object[])bottomTableMergedCells.get(i))[1]
              );
          }
        });

      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
//          if (!table.getGrid().hasFocus())
//            table.getGrid().requestFocus();
        }
      });

      if (rowHeightFixed) {
        table.getGrid().setRowHeight(rowHeight);
        if (topTable!=null)
          topTable.getGrid().setRowHeight(rowHeight);
        if (bottomTable!=null)
          bottomTable.getGrid().setRowHeight(rowHeight);
        if (table.getLockedGrid()!=null)
          table.getLockedGrid().setRowHeight(rowHeight);
        if (topTable!=null && topTable.getLockedGrid()!=null)
          topTable.getLockedGrid().setRowHeight(rowHeight);
        if (bottomTable!=null && bottomTable.getLockedGrid()!=null)
          bottomTable.getLockedGrid().setRowHeight(rowHeight);
      }
      else {
        table.getGrid().setRowHeightFixed(false);
        if (table.getLockedGrid()!=null)
          table.getLockedGrid().setRowHeightFixed(false);
        if (topTable!=null) {
          topTable.getGrid().setRowHeightFixed(false);
          if (topTable.getLockedGrid()!=null)
            topTable.getLockedGrid().setRowHeightFixed(false);
        }
        if (bottomTable!=null) {
          bottomTable.getGrid().setRowHeightFixed(false);
          if (bottomTable.getLockedGrid()!=null)
            bottomTable.getLockedGrid().setRowHeightFixed(false);
        }
      }

      table.getGrid().setOrderWithLoadData(orderWithLoadData,orderPolicy);
      if (table.getLockedGrid()!=null)
        table.getLockedGrid().setOrderWithLoadData(orderWithLoadData,orderPolicy);

      // apply the profile (e.g. reorder column properties, etc)...
    if (ClientSettings.getInstance().GRID_PROFILE_MANAGER!=null &&
        functionId!=null &&
        !functionId.trim().equals("") &&
        allowColumnsProfile &&
        profile!=null)
        applyProfile(columnProperties,profile,false);


    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }

  }


  /**
   * @return column container
   */
  public final ColumnContainer getColumnContainer() {
    return itsColumnContainer;
  }


  /**
   * @return value object class name
   */
  public final String getValueObjectClassName() {
    try {
      AttributeNameEditor.setDesignVOClass(Class.forName(valueObjectClassName));
    }
    catch (Throwable ex) {
    }
    return valueObjectClassName;
  }


  /**
   * Set the value object class name.
   * @param valueObjectClassName value object class name
   */
  public final void setValueObjectClassName(String valueObjectClassName) {
    this.valueObjectClassName = valueObjectClassName;
    try {
      AttributeNameEditor.setDesignVOClass(Class.forName(valueObjectClassName));
    }
    catch (Throwable ex) {
    }
  }


  /**
   * @return allows column reordering
   */
  public final boolean isReorderingAllowed() {
    return reorderingAllowed;
  }


  /**
   * Define if column reordering is allowed.
   * @param reorderingAllowed allows column reordering
   */
  public final void setReorderingAllowed(boolean reorderingAllowed) {
    this.reorderingAllowed = reorderingAllowed;
  }


  /**
   * @return allows column resizing
   */
  public final boolean isResizingAllowed() {
    return resizingAllowed;
  }


  /**
   * Define if column resizing is allowed.
   * @param resizingAllowed allows column resizing
   */
  public final void setResizingAllowed(boolean resizingAllowed) {
    this.resizingAllowed = resizingAllowed;
  }


  /**
   * @return current grid mode
   */
  public final int getMode() {
    if (table==null)
      return mode;
    else
      return table.getMode();
  }


  /**
   * Set grid mode.
   * @param mode grid mode; possibile values: READONLY, INSERT, EDIT
   */
  public final void setMode(int mode) {
    this.mode = mode;
    if (table!=null)
      table.getVOListTableModel().setMode(mode);
  }


  /**
   * @return grid container
   */
  public final GridController getController() {
    return controller;
  }


  /**
   * Set grid container. This method is callable ONLY before the grid is set to visible.
   * @param container grid container
   */
  public final void setController(GridController controller) {
    this.controller = controller;
  }


  /**
   * Set status panel visibility.
   * @param visibleStatusPanel <code>true</code> to show the status panel, <code>false</code> to hide the status panel
   */
  public final void setVisibleStatusPanel(boolean visibleStatusPanel) {
    this.visibleStatusPanel = visibleStatusPanel;
    if (visibleStatusPanel)
      gridStatusPanel.add(labelPanel,BorderLayout.SOUTH);
    else
      gridStatusPanel.remove(labelPanel);
  }


  /**
   * @return status panel visibility
   */
  public final boolean isVisibleStatusPanel() {
    return visibleStatusPanel;
  }


  /**
   * @return grid data model
   */
  public final VOListTableModel getVOListTableModel() {
    if (table!=null)
      return table.getVOListTableModel();
    else
      return null;
  }


  /**
   * @return grid selection mode
   */
  public final int getSelectionMode() {
    return selectionMode;
  }


  /**
   * Set grid selection mode.
   * @param selectionMode grid selection mode
   */
  public final void setSelectionMode(int selectionMode) {
    this.selectionMode = selectionMode;
  }


  /**
   * @return insert button
   */
  public final InsertButton getInsertButton() {
    return insertButton;
  }


  /**
   * @return export button
   */
  public final ExportButton getExportButton() {
    return exportButton;
  }


  /**
   * @return import button
   */
  public final ImportButton getImportButton() {
    return importButton;
  }


  /**
   * @return filter button
   */
  public final FilterButton getFilterButton() {
    return filterButton;
  }


  /**
   * @return copy button
   */
  public final CopyButton getCopyButton() {
    return copyButton;
  }


  /**
   * @return edit button
   */
  public final EditButton getEditButton() {
    return editButton;
  }


  /**
   * Set the edit button.
   * @param editButton edit button
   */
  public final void setEditButton(EditButton editButton) {
    if (editButton!=null)
      editButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Edit record (CTRL+E)"));
    this.editButton = editButton;
    if (this.table!=null)
      this.table.setEditButton(editButton);
  }


  /**
   * Set the insert button.
   * @param insertButton insert button
   */
  public final void setInsertButton(InsertButton insertButton) {
    if (insertButton!=null)
      insertButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("New record (CTRL+I)"));
    this.insertButton = insertButton;
    if (this.table!=null)
      this.table.setInsertButton(insertButton);
  }


  /**
   * Set the export button.
   * @param exportButton export button
   */
  public final void setExportButton(ExportButton exportButton) {
    if (exportButton!=null)
      exportButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Export record (CTRL+X)"));
    this.exportButton = exportButton;
    if (this.table!=null)
      this.table.setExportButton(exportButton);
  }


  /**
   * Set the import button.
   * @param importButton import button
   */
  public final void setImportButton(ImportButton importButton) {
    if (importButton!=null)
      importButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Import records (CTRL+M)"));
    this.importButton = importButton;
    if (this.table!=null)
      this.table.setImportButton(importButton);
  }


  /**
   * Set the copy button.
   * @param copyButton insert button
   */
  public final void setCopyButton(CopyButton copyButton) {
    if (copyButton!=null)
      copyButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Copy record (CTRL+C)"));
    this.copyButton = copyButton;
    if (this.table!=null)
      this.table.setCopyButton(copyButton);
  }


  /**
   * @return reload/cancel button
   */
  public final ReloadButton getReloadButton() {
    return reloadButton;
  }


  /**
   * Set reload/cancel button.
   * @param reloadButton reload/cancel button
   */
  public final void setReloadButton(ReloadButton reloadButton) {
    if (reloadButton!=null)
      reloadButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Reload record/Cancel current operation (CTRL+Z)"));
    this.reloadButton = reloadButton;
    if (this.table!=null)
      this.table.setReloadButton(reloadButton);
  }


  /**
   * @return delete button
   */
  public final DeleteButton getDeleteButton() {
    return deleteButton;
  }


  /**
   * Set the delete button.
   * @param deleteButton delete button
   */
  public final void setDeleteButton(DeleteButton deleteButton) {
    if (deleteButton!=null)
      deleteButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Delete record (CTRL+D)"));
    this.deleteButton = deleteButton;
    if (this.table!=null)
      this.table.setDeleteButton(deleteButton);
  }


  /**
   * Set the filter button.
   * @param filterButton filter button
   */
  public final void setFilterButton(FilterButton filterButton) {
    if (filterButton!=null)
      filterButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Filtering/Sorting data (CTRL+F)"));
    this.filterButton = filterButton;
    if (this.table!=null)
      this.table.setFilterButton(filterButton);
  }


  /**
   * @return save button
   */
  public final SaveButton getSaveButton() {
    return saveButton;
  }


  /**
   * Set the save button.
   * @param saveButton save button
   */
  public final void setSaveButton(SaveButton saveButton) {
    if (saveButton!=null)
      saveButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("Save record (CTRL+S)"));
    this.saveButton = saveButton;
    if (this.table!=null)
      this.table.setSaveButton(saveButton);
  }


  /**
   * Add an optional button.
   * @param button generic button
   */
  public final void addGenericButton(GenericButton button) {
    if (button!=null) {
      if (table==null)
        genericButtons.add(button);
      else {
        this.table.getGenericButtons().add(button);
        button.addDataController(this.table);
      }
    }
  }


  /**
   * Remove an optional button.
   * @param button generic button
   */
  public final void removeGenericButton(GenericButton button) {
    if (button!=null) {
      if (table==null)
        genericButtons.remove(button);
      else
        this.table.getGenericButtons().remove(button);
    }
  }



  /**
   * @return <code>true</code> to automatically load data when the grid is showed, <code>false</code> otherwise
   */
  public final boolean isAutoLoadData() {
    return this.autoLoadData;
  }


  /**
   * Define if grid must be automatically loaded when it is showed.
   * @param autoLoadData <code>true</code> to automatically load data when the grid is showed, <code>false</code> otherwise
   */
  public final void setAutoLoadData(boolean autoLoadData) {
    this.autoLoadData = autoLoadData;
  }


  /**
   * Sets READONLY mode and reloads current block of data.
   * Data Loading is executed in a separated thread.
   * After data loading, the old selected row is selected again.
   * Note: This method can be called ONLY when the grid is already visibile.
   */
  public final void reloadCurrentBlockOfData() {
    if (this.table!=null)
      this.table.reload();
  }


  /**
   * Sets READONLY mode and reloads the first block of data.
   * Data Loading is executed in a separated thread.
   * After data loading, the first row is automatically selected.
   * Note: This method can be called ONLY when the grid is already visibile.
   */
  public final void reloadData() {
    if (this.table!=null)
      this.table.reloadDataFromStart();
  }


  /**
   * Save data and sets grid in READONLY mode (if saving task was successfully completed).
   */
  public final boolean save() {
    if (this.table!=null)
      return this.table.save();
    else
      return false;
  }



  /**
   * Set the functionId identifier, associated to the container
   * @param functionId identifier associated to the container
   */
  public final void setFunctionId(String functionId) {
    this.functionId = functionId;
  }


  /**
   * @return identifier (functionId) associated to the container
   */
  public final String getFunctionId() {
    return functionId;
  }


  /**
   * @return grid data locator; can be set to "ClientGridDataLocator" or to "ServerGridDataLocator"
   */
  public final GridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }


  /**
   * Set the grid data locator.
   * @param gridDataLocator grid data locator; can be set to "ClientGridDataLocator" or to "ServerGridDataLocator"
   */
  public final void setGridDataLocator(GridDataLocator gridDataLocator) {
    this.gridDataLocator = gridDataLocator;
  }


  /**
   * @return navigation bar (optional)
   */
  public final NavigatorBar getNavBar() {
    return navBar;
  }


  /**
   * Set navigation bar.
   * @param navBar navigation bar
   */
  public final void setNavBar(NavigatorBar navBar) {
    this.navBar = navBar;
    if (this.table!=null)
     this.table.setNavBar(navBar);
  }


  /**
   * @return other grid parameters
   */
  public final Map getOtherGridParams() {
    if (this.table!=null)
      return this.table.getOtherGridParams();
    return otherGridParams;
  }


  /**
   * Set other grid parameters.
   * @param otherGridParams other grid parameters
   */
  public final void setOtherGridParams(Map otherGridParams) {
    this.otherGridParams = otherGridParams;
    if (this.table!=null)
      this.table.setOtherGridParams(otherGridParams);
  }


  /**
   * Set maximum number of sorted columns.
   * @param maxSortedColumns maximum number of sorted columns
   */
  public final void setMaxSortedColumns(int maxSortedColumns) {
    this.maxSortedColumns = maxSortedColumns;
  }


  /**
   * Set maximum number of sorted columns.
   * @param maxSortedColumns maximum number of sorted columns
   */
  public final int getMaxSortedColumns() {
    return this.maxSortedColumns;
  }


  /**
   * Clear grid content.
   */
  public final void clearData() {
    if (this.table!=null)
      this.table.clearData();
  }


  /**
   * Clean up cells content for the specified row.
   * Note: this method can be invoked only in INSERT/EDIT modes.
   */
  public final void cleanUp(int row) {
    cleanUp(row,true);
  }


  /**
   * Clean up cells content for the specified row, for each field or editable only cells.
   * Note: this method can be invoked only in INSERT/EDIT modes.
   * @param cleanUpAlsoNotEditableCells define if all cells must be clean up
   */
  public final void cleanUp(int row,boolean cleanUpAlsoNotEditableCells) {
    if (table==null)
      Logger.error(this.getClass().getName(), "cleanUp", "You are not allowed to invoke this method before showing grid.", null);
    else
      table.cleanUp(row,cleanUpAlsoNotEditableCells);
  }


  /**
   * @return number of locked columns, i.e. columns anchored to the left side of the grid
   */
  public final int getLockedColumns() {
    return lockedColumns;
  }


  /**
   * Set the number of locked columns, i.e. columns anchored to the left side of the grid.
   * @param lockedColumns number of locked columns, i.e. columns anchored to the left side of the grid
   */
  public final void setLockedColumns(int lockedColumns) {
    this.lockedColumns = lockedColumns;
  }


  /**
   * @param rowNumber TableModel row index
   * @param attributeName attribute name that identifies a grid column
   * @return <code>true</code> means that the cell having the specified row and column index is editable, <code>false</code> otherwise
   */
  public final boolean isFieldEditable(int rowNumber,String attributeName) {
    return table.isFieldEditable(rowNumber,attributeName);
  }


  /**
   * Returns the indices of all selected rows.
   *
   * @return an array of integers containing the indices of all selected rows,
   *         or an empty array if no row is selected
   * @see #getSelectedRow
   */
  public int[] getSelectedRows() {
    if (table==null)
      return new int[0];
    else
      return table.getSelectedRows();
  }


  /**
   * Returns the index of the first selected row, -1 if no row is selected.
   * @return the index of the first selected row
   */
  public int getSelectedRow() {
    if (table==null)
      return -1;
    else
      return table.getSelectedRow();
  }


  /**
   * Returns the index of the first selected column,
   * -1 if no column is selected.
   * @return the index of the first selected column
   */
  public int getSelectedColumn() {
    if (table==null)
      return -1;
    else
      return table.getSelectedColumn();
  }


  /**
   * @return <code>true</code> if current editing cell is in valid state, <code>false</code> otherwise
   */
  public final boolean stopCellEditing() {
    if (table!=null)
      return table.getGrid().stopCellEditing();
    return false;
  }


  /**
   * Enable drag onto the grid.
   * @param gridId grid identifier
   */
  public final void enableDrag(String gridId) {
    if (table!=null)
      table.enableDrag(gridId);
    else
      this.gridId = gridId;
  }


  /**
   * Define GenericButton objects linked to this that will be disabled (independently from the grid mode)
   * when the specified attribute will be set to the specified value.
   * @param buttons collections GenericButton objects linked to this that have to be disabled
   * @param buttonController interface that defines button disabilitation, according to some custom policy
   */
  public final void addButtonsNotEnabled(HashSet buttons,GenericButtonController buttonController) {
    if (table!=null)
      table.addButtonsNotEnabled(buttons,buttonController);
    else
      buttonsNotEnabled.add(new Object[]{buttons,buttonController});
  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState grid method)
   */
  public final boolean isButtonDisabled(GenericButton button) {
    if (table!=null)
      return table.isButtonDisabled(button);
    else
      return false;
  }


  /**
   * Method automatically called by the Grid to check buttons disabilitation.
   */
  public final void resetButtonsState() {
    if (table!=null)
      table.resetButtonsState();
  }


  /**
   * Remove cell editing, if needed.
   */
  public final void transferFocus() {
    try {
      if (table!=null)
        table.transferFocus();
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return maximum number of rows to insert by pressing "down" key; default value: 1
   */
  public final int getMaxNumberOfRowsOnInsert() {
    if (table==null)
      return maxNumberOfRowsOnInsert;
    else
     return table.getMaxNumberOfRowsOnInsert();
  }


  /**
   * Set the maximum number of rows to insert by pressing "down" key; default value: 1.
   * @param maxNumberOfRowsOnInsert maximum number of rows to insert by pressing "down" key
   */
  public final void setMaxNumberOfRowsOnInsert(int maxNumberOfRowsOnInsert) {
    if (table==null)
      this.maxNumberOfRowsOnInsert = maxNumberOfRowsOnInsert;
    else
      table.setMaxNumberOfRowsOnInsert(maxNumberOfRowsOnInsert);
  }


  /**
   * @return collection of pairs < attribute name,new FilterWhereClause[2] > related to filters currently applied to grid
   */
  public final Map getQuickFilterValues() {
    if (table==null)
      return new HashMap();
    return table.getQuickFilterValues();
  }


  /**
   * Method invoked by UI designers (e.g. JDeveloper) that does not correctly use the "containerDelegate" property defined in GridControlBeanInfo
   * @param comp Component to add
   * @param constraint contraint to use for the component to add
   */
  public final void add(Component comp,Object constraint) {
    itsColumnContainer.add(comp,constraint);
  }


  /**
   * Method invoked by UI designers (e.g. Eclipse + Window Builder plugin) that does not correctly use the "containerDelegate" property defined in GridControlBeanInfo
   * @param comp Component to add
   * @param constraint contraint to use for the component to add
   */
  public final Component add(Component comp) {
    itsColumnContainer.add(comp,null);
    return comp;
  }


  public final void paint(Graphics g) {
    if (Beans.isDesignTime()) {
      // show "itsColumnContainer" children components...
      if (getColumnContainer().getComponents().length>0)
        // JBuilder/NetBeans...
        super.paint(g);
      else {
        // JDeveloper UI designer...
        Component[] comps = itsColumnContainer.getComponents();
        ArrayList tmp = new ArrayList(Arrays.asList(comps));
        tmp.addAll(components);
        g.setColor(getBackground());
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(Color.GRAY);
        Column comp = null;
        int width = 0;
        boolean b = false;
        for(int i=0;i<tmp.size();i++) {
          comp = (Column)tmp.get(i);
          b = true;
          if (width<getWidth()) {
            g.setColor(Color.GRAY);
            g.drawRect(width,0,comp.getPreferredSize().width,19);

            g.setColor(Color.WHITE);
            g.drawLine(width+1,1,width+comp.getPreferredSize().width-1,1);
            g.drawLine(width+1,1,width+1,19);
            g.setColor(new Color(100,100,100));
            g.drawLine(width+1,19,width+comp.getPreferredSize().width-1,19);
            g.drawLine(width+comp.getPreferredSize().width-1,1,width+comp.getPreferredSize().width-1,18);
            g.setColor(Color.BLACK);
            g.drawString(comp.getHeaderColumnName(),width+3,15);
            g.setColor(Color.WHITE);
            g.fillRect(width+1,21,comp.getPreferredSize().width-1,18);
          }
          width += comp.getPreferredSize().width;
        }
        if (b) {
          g.drawRect(0,getHeight()-20,getWidth()-1,19);
          g.setColor(new Color(100,100,100));
          g.drawLine(1,getHeight()-19,getWidth()-2,getHeight()-19);
          g.drawLine(1,getHeight()-19,1,getHeight()-1);
          g.setColor(Color.WHITE);
          g.drawLine(1,getHeight()-1,getWidth()-1,getHeight()-1);
          g.drawLine(getWidth()-1,getHeight()-19,getWidth()-1,getHeight()-1);
        }

      }
    }
    else
      super.paint(g);
  }


  public LayoutManager getLayout() {
    return flowLayout1;
  }


  /**
   * @return row height of the grid control
   */
  public final int getRowHeight() {
    return rowHeight;
  }


  /**
   * Set the row height of the grid control.
   * @param rowHeight row height of the grid control
   */
  public final void setRowHeight(int rowHeight) {
    this.rowHeight = rowHeight;
  }


  /**
   * @return define if row height can change for each row, according to image height included in a cell of grid; default value: <code>true</code>
   */
  public final boolean isRowHeightFixed() {
    return rowHeightFixed;
  }


  /**
   * @param rowHeightFixed define if row height can change for each row, according to image height included in a cell of grid; default value: <code>true</code>
   */
  public final void setRowHeightFixed(boolean rowHeightFixed) {
    this.rowHeightFixed = rowHeightFixed;
  }


  /**
   * @return flag used to define if grid sorting operation must always invoke loadData method to retrieve a new list of v.o. or the grid must sort the current v.o. list without invoking loadData (only with the whole result set loaded)
   */
  public final boolean isOrderWithLoadData() {
    return orderWithLoadData;
  }


  /**
   * Used to define if grid sorting operation must always invoke loadData method to retrieve a new list of v.o. or
   * the grid must sort the current v.o. list without invoking loadData (only with the whole result set loaded).
   * @param orderWithLoadData flag used to define if grid sorting operation must always invoke loadData method to retrieve a new list of v.o. or the grid must sort the current v.o. list without invoking loadData (only with the whole result set loaded)
   */
  public final void setOrderWithLoadData(boolean orderWithLoadData) {
    this.orderWithLoadData = orderWithLoadData;
  }


  /**
   * Used to define the sorting algorithm to use to sort columns.
   * Note that this method is used by grid control ONLY IF <code>orderWithLoadData</code> property has been set to <code>false</code>,
   * otherwise this setting will be ignored.
   * @param orderPolicy interface that must be implemented in order to sort columns
   */
  public final void setOrderPolicy(OrderPolicy orderPolicy) {
    this.orderPolicy = orderPolicy;
  }


  /**
   * @return <code>true</code> to automatically show a filter panel when moving mouse at right of the grid; <code>false</code> to do not show it
   */
  public final boolean isShowFilterPanelOnGrid() {
    return showFilterPanelOnGrid;
  }


  /**
   * Set a <code>true</code> value to automatically show a filter panel when moving mouse at right of the grid; <code>false</code> to do not show it
   * @param showFilterPanelOnGrid <code>true</code> to automatically show a filter panel when moving mouse at right of the grid; <code>false</code> to do not show it
   */
  public final void setShowFilterPanelOnGrid(boolean showFilterPanelOnGrid) {
    this.showFilterPanelOnGrid = showFilterPanelOnGrid;
  }


  /**
   * @return number of rows locked (anchored) on the bottom of the grid
   */
  public final int getLockedRowsOnBottom() {
    return lockedRowsOnBottom;
  }


  /**
   * @return number of rows locked (anchored) on the top of the grid; default value: 0
   */
  public final int getLockedRowsOnTop() {
    return lockedRowsOnTop;
  }


  /**
   * Set the number of rows locked (anchored) on the top of the grid.
   * @param lockedRowsOnBottom number of rows locked (anchored) on the top of the grid
   */
  public final void setLockedRowsOnBottom(int lockedRowsOnBottom) {
    this.lockedRowsOnBottom = lockedRowsOnBottom;
  }


  /**
   * Set the number of rows locked (anchored) on the bottom of the grid.
   * @param lockedRowsOnTop number of rows locked (anchored) on the top of the grid
   */
  public final void setLockedRowsOnTop(int lockedRowsOnTop) {
    this.lockedRowsOnTop = lockedRowsOnTop;
  }


  /**
   * @return top grid controller
   */
  public final GridController getTopGridController() {
    return topGridController;
  }


  /**
   * @return top grid data locator
   */
  public final GridDataLocator getTopGridDataLocator() {
    return topGridDataLocator;
  }


  /**
   * Set the top grid data locator.
   * @param topGridDataLocator top grid data locator
   */
  public final void setTopGridDataLocator(GridDataLocator topGridDataLocator) {
    this.topGridDataLocator = topGridDataLocator;
  }


  /**
   * Set the top grid controller.
   * @param topGridController top grid controller
   */
  public final void setTopGridController(GridController topGridController) {
    this.topGridController = topGridController;
  }


  /**
   * @return top grid containepd in this panel (optional); it will be created only on run-time
   */
  public final Grids getTopTable() {
    return topTable;
  }


  /**
   * @return bottom grid contained in this panel (optional); it will be created only on run-time
   */
  public final Grids getBottomTable() {
    return bottomTable;
  }


  /**
   * @return std grid contained in this panel; it will be created only on run-time
   * DO NOT USE THIS HOOK TO ACCESS TO INTERNAL JTABLE CLASS AND CHANGE SOME SETTINGS!
   */
  public final Grids getTable() {
    return table;
  }


  /**
   * @return bottom grid data locator
   */
  public final GridDataLocator getBottomGridDataLocator() {
    return bottomGridDataLocator;
  }


  /**
   * @return bottom grid controller
   */
  public final GridController getBottomGridController() {
    return bottomGridController;
  }


  /**
   * Set the bottom grid controller.
   * @param bottomGridController bottom grid controller
   */
  public final void setBottomGridController(GridController bottomGridController) {
    this.bottomGridController = bottomGridController;
  }


  /**
   * Set the bottomgrid data locator.
   * @param bottomGridDataLocator bottom grid data locator
   */
  public final void setBottomGridDataLocator(GridDataLocator bottomGridDataLocator) {
    this.bottomGridDataLocator = bottomGridDataLocator;
  }


  /**
   * @return default value that could be set in the quick filter criteria; values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH
   */
  public final int getDefaultQuickFilterCriteria() {
    return defaultQuickFilterCriteria;
  }


  /**
   * Set default value that could be set in the quick filter criteria; values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH.
   * @param defaultQuickFilterCriteria values allowed: Consts.EQUALS,Consts.CONTAINS,Consts.STARTS_WITH,Consts.ENDS_WITH
   */
  public final void setDefaultQuickFilterCriteria(int defaultQuickFilterCriteria) {
    this.defaultQuickFilterCriteria = defaultQuickFilterCriteria;
  }


  /**
   * Set row selection interval.
   * @param startRow first selected row index
   * @param endRow last selected row index
   */
  public final void setRowSelectionInterval(int startRow,int endRow) {
    if (table!=null) {
      table.setRowSelectionInterval(startRow,endRow);
    }
  }


  /**
   * Adds the rows from <code>index0</code> to <code>index1</code>, inclusive, to
   * the current selection.
   *
   * @exception IllegalArgumentException      if <code>index0</code> or <code>index1</code>
   *                                          lie outside [0, <code>getRowCount()</code>-1]
   * @param   index0 one end of the interval
   * @param   index1 the other end of the interval
   */
  public final void addRowSelectionInterval(int index0, int index1) {
    if (table!=null)
      table.addRowSelectionInterval(index0,index1);
  }


  /**
   * Deselects the rows from <code>index0</code> to <code>index1</code>, inclusive.
   *
   * @exception IllegalArgumentException      if <code>index0</code> or
   *						<code>index1</code> lie outside
   *                                          [0, <code>getRowCount()</code>-1]
   * @param   index0 one end of the interval
   * @param   index1 the other end of the interval
   */
  public final void removeRowSelectionInterval(int index0, int index1) {
    if (table!=null)
      table.removeRowSelectionInterval(index0,index1);
  }



  /**
   * Deselects all selected columns and rows.
   */
  public final void clearSelection() {
    if (table!=null)
      table.clearSelection();
  }


  /**
   * Set editing to the specified cell.
   * This method is ignored if the grid control is in READONY mode.
   * @param row row index
   * @param attributeName attribute name that identify the grid column
   */
  public final void editCellAt(final int row,String attributeName) {
    try {
      if (getMode() == Consts.READONLY) {
        return;
      }
      int colIndex = table.getVOListTableModel().findColumn(attributeName);
      if (colIndex==-1)
        Logger.error(this.getClass().getName(), "editCellAt", "The specified attribute '"+attributeName+"' does not exist.",null);
      else {
        colIndex = table.getGrid().convertColumnIndexToView(colIndex);
        if (colIndex==-1) {
        if (table.getLockedGrid()!=null) {
          colIndex = table.getLockedGrid().convertColumnIndexToView(colIndex);
          if (colIndex!=-1) {
            editCellAt(row,colIndex);
          }
          else
            Logger.error(this.getClass().getName(), "editCellAt", "The specified attribute '"+attributeName+"' has not a visible column associated.",null);
        }
        else
          Logger.error(this.getClass().getName(), "editCellAt", "The specified attribute '"+attributeName+"' has not a visible column associated.",null);
        }
        else {
          editCellAt(row,colIndex);
        }
      }
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(), "editCellAt", "Error while setting cell editing",ex);
    }
  }


  /**
   * Method called by editCellAt public method.
   * @param row row index
   * @param colIndex column index (related to the JTable)
   */
  private void editCellAt(final int row,final int colIndex) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        setRowSelectionInterval(row,row);
        table.getGrid().editCellAt(row,colIndex);
      }
    });
  }


  /**
   * Sets the <code>rowMargin</code> and the <code>columnMargin</code> --
   * the height and width of the space between cells -- to
   * <code>intercellSpacing</code>.
   *
   * @param   intercellSpacing        a <code>Dimension</code>
   *					specifying the new width
   *					and height between cells
   * @see     #getIntercellSpacing
   * @beaninfo
   *  description: The spacing between the cells,
   *               drawn in the background color of the JTable.
   *
   * NOTE: there is a bug in JDK1.4.x: do not use this property with java 1.4!!!
   */
  public final void setIntercellSpacing(Dimension spacing) {
    this.spacing = spacing;
    if (table!=null)
      table.setIntercellSpacing(spacing);
  }


  /**
   * Sets the amount of empty space between cells in adjacent rows.
   *
   * @param  rowMargin  the number of pixels between cells in a row
   * @see     #getRowMargin
   * @beaninfo
   *  bound: true
   *  description: The amount of space between cells.
   */
  public final void setRowMargin(int rowMargin) {
    this.rowMargin = rowMargin;
    if (table!=null)
      table.setRowMargin(rowMargin);
  }


  /**
   * @return a <code>Dimension</code> specifying the new width and height between cells
   */
  public final Dimension getIntercellSpacing() {
    return this.spacing;
  }


  /**
   * @return the number of pixels between cells in a row
   */
  public final int getRowMargin() {
    return this.rowMargin;
  }


  /**
   * @return define if an inner v.o. must be automatically instantiated when a setter method is invoked
   */
  public final boolean isCreateInnerVO() {
    return createInnerVO;
  }


  /**
   * Define if an inner v.o. must be automatically instantiated when a setter method is invoked.
   * @param createInnerVO define if an inner v.o. must be automatically instantiated when a setter method is invoked
   */
  public final void setCreateInnerVO(boolean createInnerVO) {
    this.createInnerVO = createInnerVO;
  }


  /**
   * Set the cell span for the specified range of cells.
   * @param rows row indexes that identify the cells to merge
   * @param columns column indexes that identify the cells to merge
   * @return <code>true</code> if merge operation is allowed, <code>false</code> if the cells range is invalid
   */
  public final boolean mergeCells(int[] rows,int[] columns) {
    if (table!=null)
      return table.mergeCells(rows,columns);
    else {
      mergedCells.add(new Object[]{rows,columns});
      return true;
    }
  }


  /**
   * Set the cell span for the specified range of cells in the locked rows on the top of the table.
   * @param rows row indexes that identify the cells to merge
   * @param columns column indexes that identify the cells to merge
   * @return <code>true</code> if merge operation is allowed, <code>false</code> if the cells range is invalid
   */
  public final boolean mergeCellsOnTop(int[] rows,int[] columns) {
    if (topTable!=null)
      return topTable.mergeCells(rows,columns);
    else {
      topTableMergedCells.add(new Object[]{rows,columns});
      return true;
    }
  }


  /**
   * Set the cell span for the specified range of cells in the locked rows at the bottom of the table.
   * @param rows row indexes that identify the cells to merge
   * @param columns column indexes that identify the cells to merge
   * @return <code>true</code> if merge operation is allowed, <code>false</code> if the cells range is invalid
   */
  public final boolean mergeCellsOnBottom(int[] rows,int[] columns) {
    if (bottomTable!=null)
      return bottomTable.mergeCells(rows,columns);
    else {
      bottomTableMergedCells.add(new Object[]{rows,columns});
      return true;
    }
  }


  /**
   * @return define if background and foreground colors must be setted according to GridController definition only in READONLY mode
   */
  public final boolean isColorsInReadOnlyMode() {
    return colorsInReadOnlyMode;
  }


  /**
   * Define if background and foreground colors must be setted according to GridController definition only in READONLY mode.
   * @param colorsInReadOnlyMode <code>false</code> to enable background and foreground colors to be setted according to GridController definition in all grid modes; <code>true</code> to enable background and foreground colors to be setted according to GridController definition only in READONLY mode
   */
  public final void setColorsInReadOnlyMode(boolean colorsInReadOnlyMode) {
    this.colorsInReadOnlyMode = colorsInReadOnlyMode;
    if (table!=null)
      table.setColorsInReadOnlyMode(colorsInReadOnlyMode);
    if (topTable!=null)
      topTable.setColorsInReadOnlyMode(colorsInReadOnlyMode);
    if (bottomTable!=null)
      bottomTable.setColorsInReadOnlyMode(colorsInReadOnlyMode);
  }


  /**
   * Add a "load data completed" listener.
   */
  public final void addLoadDataCompletedListener(ActionListener listener) {
    loadDataCompletedListeners.add(listener);
    if (table!=null)
      table.addLoadDataCompletedListener(listener);
  }


  /**
   * Remove a "load data completed" listener.
   */
  public final void removeLoadDataCompletedListener(ActionListener listener) {
    loadDataCompletedListeners.remove(listener);
    if (table!=null)
      table.removeLoadDataCompletedListener(listener);
  }


  /**
   * Add a menu item to the popup menu accessed through the right mouse click onto the grid.
   * @param command menu item to add
   */
  public final void addPopupCommand(JMenuItem command) {
    popupCommands.add(command);
  }


  /**
   * Add a menu item to the popup menu accessed through the right mouse click onto the grid.
   * @param command menu item text (this text will be translated according to internationalization settings)
   * @param listener ActionListener linked to this menu item
   * @param enabled flag used to initially enable/disable the menu item
   */
  public final void addPopupCommand(String command,ActionListener listener,boolean enabled) {
    JMenuItem menu = new JMenuItem(ClientSettings.getInstance().getResources().getResource(command));
    menu.setEnabled(enabled);
    menu.addActionListener(listener);
    popupCommands.add(menu);
  }


  /**
   * Add a menu item to the popup menu accessed through the right mouse click onto the grid.
   * @param command menu item text (this text will be translated according to internationalization settings)
   * @param enabled flag used to initially enable/disable the menu item
   * @param icon icon image associated to this menu item
   * @param listener ActionListener linked to this menu item
   */
  public final void addPopupCommand(String command,ActionListener listener,boolean enabled,Icon icon) {
    JMenuItem menu = new JMenuItem(ClientSettings.getInstance().getResources().getResource(command),icon);
    menu.setEnabled(enabled);
    menu.addActionListener(listener);
    popupCommands.add(menu);
  }


  /**
   * Add a menu item to the popup menu accessed through the right mouse click onto the grid.
   * @param command menu item text (this text will be translated according to internationalization settings)
   * @param listener ActionListener linked to this menu item
   * @param enabled flag used to initially enable/disable the menu item
   * @param mnemonic ALT+char key combination that fires the item selection event
   */
  public final void addPopupCommand(String command,ActionListener listener,boolean enabled,int mnemonic) {
    JMenuItem menu = new JMenuItem(ClientSettings.getInstance().getResources().getResource(command),mnemonic);
    menu.setEnabled(enabled);
    menu.addActionListener(listener);
    popupCommands.add(menu);
  }


  /**
   * Add a menu item to the popup menu accessed through the right mouse click onto the grid.
   * @param command menu item text (this text will be translated according to internationalization settings)
   * @param mnemonic mnemonic char associated to this menu item
   * @param listener ActionListener linked to this menu item
   * @param enabled flag used to initially enable/disable the menu item
   * @param mnemonic ALT+char key combination that fires the item selection event
   * @param accelerator key combination (e.g. CTRL+key, etc.) that fires the item selection event
   */
  public final void addPopupCommand(String command,ActionListener listener,boolean enabled,int mnemonic,KeyStroke accelerator) {
    JMenuItem menu = new JMenuItem(ClientSettings.getInstance().getResources().getResource(command),mnemonic);
    menu.setAccelerator(accelerator);
    menu.setEnabled(enabled);
    menu.addActionListener(listener);
    popupCommands.add(menu);
  }



  /**
   * Rename a menu item added to the popup menu and accessed through the right mouse click onto the grid.
   * @param oldCommand old menu item text (this text will be translated according to internationalization settings)
   * @param newCommand new menu item text (this text will be translated according to internationalization settings)
   */
  public final void renamePopupCommand(String oldCommand,String newCommand) {
    if (table!=null) {
      ArrayList list =  table.getPopupCommands();
      String text = ClientSettings.getInstance().getResources().getResource(oldCommand);
      JMenuItem menu = null;
      for(int i=0;i<list.size();i++) {
        menu = (JMenuItem)list.get(i);
        if (menu.getText().equals(text)) {
          menu.setText(ClientSettings.getInstance().getResources().getResource(newCommand));
          return;
        }
      }
    }
  }


  /**
   * Enable/disable a menu item added to the popup menu and accessed through the right mouse click onto the grid.
   * @param command menu item text (this text will be translated according to internationalization settings)
   * @param enabled flag used to define if enable or disable the specified menu item
   */
  public final void setEnablePopupCommand(String command,boolean enabled) {
    if (table!=null) {
      ArrayList list =  table.getPopupCommands();
      String text = ClientSettings.getInstance().getResources().getResource(command);
      JMenuItem menu = null;
      for(int i=0;i<list.size();i++) {
        menu = (JMenuItem)list.get(i);
        if (menu.getText().equals(text)) {
          menu.setEnabled(enabled);
          return;
        }
      }
    }
  }


  /**
   * Apply the specified profile to the grid.
   */
  private void applyProfile(Column[] columnProperties,GridProfile profile,boolean reloadGrid) {
    this.profile = profile;
    labelPanel.setProfile(profile.getDescription());

    for(int i=0;i<profile.getColumnsAttribute().length;i++) {
      if (this.permissions==null ||
          this.permissions!=null && this.permissions.getColumnsVisibility()[i])
        columnProperties[i].setColumnVisible(profile.getColumnsVisibility()[i]);
      columnProperties[i].setPreferredWidth(profile.getColumnsWidth()[i]);
    }

    Grid g = null;

    if (table.getLockedGrid()!=null) {
      // remove all columns...
      g = table.getLockedGrid();
      g.resetColumns(profile);
    }
    // remove all columns...
    g = table.getGrid();
    g.resetColumns(profile);


    if (table!=null) {
      table.getCurrentSortedColumns().clear();
      table.getCurrentSortedColumns().addAll(profile.getCurrentSortedColumns());
      table.getCurrentSortedVersusColumns().clear();
      table.getCurrentSortedVersusColumns().addAll(profile.getCurrentSortedVersusColumns());
      table.getQuickFilterValues().clear();

      for (int i = 0; i < columnProperties.length; i++) {
        if (columnProperties[i].getDefaultFilterValues()!=null)
          table.getQuickFilterValues().put(
            columnProperties[i].getColumnName(),
            columnProperties[i].getDefaultFilterValues()
          );
      }

      table.getQuickFilterValues().putAll(profile.getQuickFilterValues());
    }
    if (topTable!=null) {
      g = topTable.getGrid();
      g.resetColumns(profile);
      topTable.getCurrentSortedColumns().clear();
      topTable.getCurrentSortedColumns().addAll(profile.getCurrentSortedColumns());
      topTable.getCurrentSortedVersusColumns().clear();
      topTable.getCurrentSortedVersusColumns().addAll(profile.getCurrentSortedVersusColumns());
      topTable.getQuickFilterValues().clear();
      topTable.getQuickFilterValues().putAll(profile.getQuickFilterValues());
    }
    if (bottomTable!=null) {
      g = bottomTable.getGrid();
      g.resetColumns(profile);
      bottomTable.getCurrentSortedColumns().clear();
      bottomTable.getCurrentSortedColumns().addAll(profile.getCurrentSortedColumns());
      bottomTable.getCurrentSortedVersusColumns().clear();
      bottomTable.getCurrentSortedVersusColumns().addAll(profile.getCurrentSortedVersusColumns());
      bottomTable.getQuickFilterValues().clear();
      bottomTable.getQuickFilterValues().putAll(profile.getQuickFilterValues());
    }

    if (reloadGrid)
      table.reload();
  }


  /**
   * Apply grid permissions to grid.
   */
  private void applyGridPermissions(String[] columnsAttribute,boolean[] columnsVisibility,boolean[] columnsEditableInIns,boolean[] columnsEditableInEdit,boolean[] columnsMandatory) throws Throwable {
    if (this.permissions==null) {
      this.permissions = (GridPermissions)ClientSettings.getInstance().getGridPermissions().get(functionId);
      if (this.permissions==null) {
        this.permissions = ClientSettings.getInstance().GRID_PERMISSION_MANAGER.getUserGridPermissions(
          functionId,
          ClientSettings.getInstance().GRID_PERMISSION_MANAGER.getUserRoles(),
          columnsAttribute,
          columnsVisibility,
          columnsEditableInIns,
          columnsEditableInEdit,
          columnsMandatory
        );
        if (this.permissions!=null)
          ClientSettings.getInstance().getGridPermissions().put(functionId,this.permissions);
      }
    }

    for(int i=0;i<permissions.getColumnsAttribute().length;i++) {
      columnProperties[i].setColumnVisible(permissions.getColumnsVisibility()[i]);
      if (!permissions.getColumnsVisibility()[i])
        columnProperties[i].setColumnSelectable(false);
      columnProperties[i].setEditableOnInsert(permissions.getColumnsEditabilityInInsert()[i]);
      columnProperties[i].setEditableOnEdit(permissions.getColumnsEditabilityInEdit()[i]);
      columnProperties[i].setColumnRequired(permissions.getColumnsMandatory()[i]);
    }
  }


  private void setDefaultProfile(Column[] columnProperties) {
    String[] columnsAttribute = new String[columnProperties.length];
    boolean[] columnsVisibility = new boolean[columnProperties.length];
    int[] columnsWidth = new int[columnProperties.length];
    ArrayList currentSortedColumns = new ArrayList();
    ArrayList currentSortedVersusColumns = new ArrayList();
    HashMap filters = new HashMap();

    int[] aux = new int[columnProperties.length+1];
    for(int i=0;i<aux.length;i++)
      aux[i] = -1;
    for(int i=0;i<columnProperties.length;i++) {

      columnsAttribute[i] = columnProperties[i].getColumnName();
      columnsVisibility[i] = columnProperties[i].isColumnVisible();
      columnsWidth[i] = columnProperties[i].getPreferredWidth();
      if (!columnProperties[i].getSortVersus().equals(Consts.NO_SORTED)) {
        aux[columnProperties[i].getSortingOrder()] = i;
      }

      if (columnProperties[i].getDefaultFilterValues()!=null)
        filters.put(columnProperties[i].getColumnName(),columnProperties[i].getDefaultFilterValues());

    }
    for(int i=0;i<aux.length;i++)
      if (aux[i]>=0) {
        currentSortedColumns.add(columnProperties[aux[i]].getColumnName());
        currentSortedVersusColumns.add(columnProperties[aux[i]].getSortVersus());
    }

    defaultProfile = new GridProfile(
      ClientSettings.getInstance().getResources().getResource("default profile"),
      functionId,
      ClientSettings.getInstance().GRID_PROFILE_MANAGER.getUsername(),
      currentSortedColumns,
      currentSortedVersusColumns,
      filters,
      columnsAttribute,
      columnsVisibility,
      columnsWidth,
      true
    );
  }


  /**
   * @return current grid profile
   */
  public final GridProfile getProfile() {
    return profile;
  }


  /**
   * Store profile if changed.
   */
  private void maybeStoreProfile(Column[] columnProperties) {
    if (table==null)
      return;

    try {
      boolean changed = false;

      // check for changes in column reordering, visibility, width...
      String[] attributesName = new String[profile.getColumnsAttribute().length];
      boolean[] visibility = new boolean[profile.getColumnsVisibility().length];
      int[] width = new int[profile.getColumnsWidth().length];
      ArrayList sortedCols = new ArrayList();
      ArrayList sortedColVersus = new ArrayList();
      HashMap filters = new HashMap();

      Grid g = table.getLockedGrid();
      if (g!=null) {
        TableColumnModel model = g.getColumnModel();
        for(int i=0;i<model.getColumnCount();i++) {
          attributesName[i] = columnProperties[g.convertColumnIndexToModel(i)].getColumnName();
          visibility[g.convertColumnIndexToModel(i)] = columnProperties[g.convertColumnIndexToModel(i)].isColumnVisible();
          width[g.convertColumnIndexToModel(i)] = model.getColumn(i).getWidth();
        }
      }
      g = table.getGrid();
      TableColumnModel model = g.getColumnModel();
      for(int i=0;i<model.getColumnCount();i++) {
        attributesName[i+lockedColumns] = columnProperties[g.convertColumnIndexToModel(i)].getColumnName();
        visibility[g.convertColumnIndexToModel(i)] = columnProperties[g.convertColumnIndexToModel(i)].isColumnVisible();
        width[g.convertColumnIndexToModel(i)] = model.getColumn(i).getWidth();
      }

      // retrieve sorting conditions and set not visible attributes...
      int k = attributesName.length-1;
      Hashtable sortingVersus = new Hashtable(columnProperties.length);
      for(int i=0;i<columnProperties.length;i++) {
        if (!columnProperties[i].isColumnVisible())
          attributesName[k--] = columnProperties[i].getColumnName();
        if (!columnProperties[i].getSortVersus().equals(Consts.NO_SORTED)) {
          sortingVersus.put(new Integer(columnProperties[i].getSortingOrder()),new Integer(i));
        }
      }
      Integer index = null;
      for(int i=0;i<columnProperties.length;i++) {
        index = (Integer)sortingVersus.get(new Integer(i));
        if (index!=null) {
          sortedCols.add(columnProperties[index.intValue()].getColumnName());
          sortedColVersus.add(columnProperties[index.intValue()].getSortVersus());
        }
      }

      // retrieve filtering conditions...
      if (table!=null) {
        filters.putAll( table.getQuickFilterValues() );
      }
      if (topTable!=null) {
        filters.putAll( topTable.getQuickFilterValues() );
      }
      if (bottomTable!=null) {
        filters.putAll( bottomTable.getQuickFilterValues() );
      }

      if (!Utils.equals(attributesName,profile.getColumnsAttribute())) {
        profile.setColumnsAttribute(attributesName);
        changed = true;
      }
      if (!Utils.equals(visibility,profile.getColumnsVisibility())) {
        profile.setColumnsVisibility(visibility);
        changed = true;
      }
      if (!Utils.equals(width,profile.getColumnsWidth())) {
        profile.setColumnsWidth(width);
        changed = true;
      }
      if (!Utils.equals(sortedCols,profile.getCurrentSortedColumns())) {
        profile.setCurrentSortedColumns(sortedCols);
        changed = true;
      }
      if (!Utils.equals(sortedColVersus,profile.getCurrentSortedVersusColumns())) {
        profile.setCurrentSortedVersusColumns(sortedColVersus);
        changed = true;
      }
      if (!Utils.equals(filters,profile.getQuickFilterValues())) {
        profile.setQuickFilterValues(filters);
        changed = true;
      }


      if (changed) {
        Object id = ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeUserProfile(profile);
        profile.setId(id);
        ClientSettings.getInstance().GRID_PROFILE_MANAGER.storeGridProfileId(functionId, profile.getId());
        ClientSettings.getInstance().getLastUserGridProfileIds().put(functionId, profile.getId());
      }
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(), "maybeStoreProfile", "Error while saving grid profile: "+ex.getMessage(),ex);
    }
  }


  /**
   * @return used only when "showFilterPanelOnGrid" is set to <code>true</code>; define filter panel policy for hiding it; allowed values: Consts.FILTER_PANEL_ON_GRID_xxx; default value: ClientSettings.FILTER_PANEL_ON_GRID_POLICY
   */
  public final int getFilterPanelOnGridPolicy() {
    return filterPanelOnGridPolicy;
  }


  /**
   * Define filter panel policy for hiding it; allowed values: Consts.FILTER_PANEL_ON_GRID_xxx; default value: ClientSettings.FILTER_PANEL_ON_GRID_POLICY
   * @param filterPanelOnGridPolicy used only when "showFilterPanelOnGrid" is set to <code>true</code>; define filter panel policy for hiding it; allowed values: Consts.FILTER_PANEL_ON_GRID_xxx
   */
  public final void setFilterPanelOnGridPolicy(int filterPanelOnGridPolicy) {
    this.filterPanelOnGridPolicy = filterPanelOnGridPolicy;
  }


  /**
   * @return define if the last column must be to anchored on the right margin of the grid, only when all columns width is lesser than grid width
   */
  public boolean isAnchorLastColumn() {
    return anchorLastColumn;
  }


  /**
   * Define if the last column must be anchored on the right margin of the grid, only when all columns width is lesser than grid width.
   * @param anchorLastColumn flag used to anchor the last column on the right margin of the grid, only when all columns width is lesser than grid width
   */
  public void setAnchorLastColumn(boolean anchorLastColumn) {
    this.anchorLastColumn = anchorLastColumn;
  }


  /**
   * @return column index declared as expandable, i.e. user can click on it to expand cell to show an inner component
   */
  public final int getExpandableColumn() {
    return expandableColumn;
  }


  /**
   * Define which column index declared as expandable, i.e. user can click on it to expand cell to show an inner component.
   * @param expandableColumn column index declared as expandable, i.e. user can click on it to expand cell to show an inner component
   */
  public final void setExpandableColumn(int expandableColumn) {
    this.expandableColumn = expandableColumn;
  }


  /**
   * Collapse all expanded rows.
   * This command will be performed only if "expandableRowController" property is setted and row is not yet collapsed
   * @param row row number
   */
  public final void collapseAllRows() {
    if (table!=null)
      table.collapseAllRows();
  }


  /**
   * @return define whether expanded rows in the past must be collapsed when expanding the current one; used only when "expandableColumn" property is not null
   */
  public final boolean isSingleExpandableRow() {
    return singleExpandableRow;
  }


  /**
   * Define whether expanded rows in the past must be collapsed when expanding the current one; used only when "expandableColumn" property is not null.
   * @param singleExpandableRow define whether expanded rows in the past must be collapsed when expanding the current one; used only when "expandableColumn" property is not null
   */
  public final void setSingleExpandableRow(boolean singleExpandableRow) {
    this.singleExpandableRow = singleExpandableRow;
  }


  /**
   * @return controller that manages row expansion
   */
  public final ExpandableRowController getExpandableRowController() {
    return expandableRowController;
  }


  /**
   * Define the controller that manages row expansion.
   * @param expandableRowController controller that manages row expansion
   */
  public final void setExpandableRowController(ExpandableRowController expandableRowController) {
    this.expandableRowController = expandableRowController;
  }


  /**
   * @return define whether the row to show, when expanding the current one, must be showed over the current one on in a new row below it; used only when "overwriteRowWhenExpanding" property is not null
   */
  public final boolean isOverwriteRowWhenExpanding() {
    return overwriteRowWhenExpanding;
  }


  /**
   * Define whether the row to show, when expanding the current one, must be showed over the current one on in a new row below it; used only when "overwriteRowWhenExpanding" property is not null.
   * @param overwriteRowWhenExpanding define whether the row to show, when expanding the current one, must be showed over the current one on in a new row below it
   */
  public final void setOverwriteRowWhenExpanding(boolean overwriteRowWhenExpanding) {
    this.overwriteRowWhenExpanding = overwriteRowWhenExpanding;
  }


  public final void finalize() {
    try {
      if (table==null)
        return;

      if (ClientSettings.getInstance().GRID_PROFILE_MANAGER!=null &&
          functionId!=null &&
          !functionId.trim().equals("") &&
          allowColumnsProfile)
        maybeStoreProfile(columnProperties);

      table.finalize();
      table = null;

      if (topTable!=null)
        topTable.finalize();
      topTable = null;

      if (bottomTable!=null)
        bottomTable.finalize();
      bottomTable = null;

      FocusListener[] fl = getFocusListeners();
      for (int i = 0; i < fl.length; i++) {
        this.removeFocusListener(fl[i]);
      }
      MouseListener[] ml = getMouseListeners();
      for (int i = 0; i < ml.length; i++) {
        this.removeMouseListener(ml[i]);
      }
      KeyListener[] ll = getKeyListeners();
      for (int i = 0; i < ll.length; i++) {
        this.removeKeyListener(ll[i]);
      }

      controller = null;
      gridDataLocator = null;
      if (this.getParent()!=null)
        this.getParent().remove(this);

      if (itsColumnContainer!=null)
        ClientUtils.disposeComponents(itsColumnContainer.getComponents());
      ClientUtils.disposeComponents(getComponents());

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    insertButton = null;
    exportButton = null;
    importButton = null;
    filterButton = null;
    copyButton = null;
    editButton = null;
    reloadButton = null;
    deleteButton = null;
    saveButton = null;
    navBar = null;
    buttonsNotEnabled = null;
    genericButtons = null;
    components = null;
    filterPanel = null;
    topGridController = null;
    bottomGridController = null;
    topGridDataLocator = null;
    bottomGridDataLocator = null;
    tmpPanel = null;
    mergedCells = null;
    topTableMergedCells = null;
    bottomTableMergedCells = null;
    loadDataCompletedListeners = null;
    popupCommands = null;
    profile = null;
    defaultProfile = null;
    profileMenu = null;
    profilesMenu = null;
    expandableRowController = null;
    itsColumnContainer = null;
  }


  public final void requestFocus() {
    if (table!=null)
      table.getGrid().requestFocus();
    else
      super.requestFocus();
  }


  /**
   * @return define if a warning message must be showed when reloading data in grid and grid is in EDIT/INSERT mode
   */
  public final boolean isShowWarnMessageBeforeReloading() {
    return showWarnMessageBeforeReloading;
  }


  /**
   * Define if a warning message must be showed when reloading data in grid and grid is in EDIT/INSERT mode.
   * @param showWarnMessageBeforeReloading flag used to define if a warning message must be showed when reloading data in grid and grid is in EDIT/INSERT mode
   */
  public final void setShowWarnMessageBeforeReloading(boolean showWarnMessageBeforeReloading) {
    this.showWarnMessageBeforeReloading = showWarnMessageBeforeReloading;
  }


  /**
   * @return allow insert row (using DOWN key) in edit mode too; default value: <code>false</code>
   */
  public final boolean isAllowInsertInEdit() {
    if (table==null)
      return allowInsertInEdit;
    else
      return table.isAllowInsertInEdit();
  }


  /**
   * Allow insert row (using DOWN key) in edit mode too; default value: <code>false</code>
   * @param allowInsertInEdit allow insert row (using DOWN key) in edit mode too
   */
  public final void setAllowInsertInEdit(boolean allowInsertInEdit) {
    if (table==null)
      this.allowInsertInEdit = allowInsertInEdit;
    else
      table.setAllowInsertInEdit(allowInsertInEdit);
  }


  /**
   * @return define where new rows must be added: <code>true</code> at the top of the grid or <code>false</code> at the bottom
   */
  public final boolean isInsertRowsOnTop() {
    if (table==null)
      return insertRowsOnTop;
    else
      return table.isInsertRowsOnTop();
  }


  /**
   * Define where new rows must be added: <code>true</code> at the top of the grid or <code>false</code> at the bottom; default value: <code>true</code>
   * @param insertRowsOnTop define where new rows must be added: <code>true</code> at the top of the grid or <code>false</code> at the bottom
   */
  public final void setInsertRowsOnTop(boolean insertRowsOnTop) {
    if (table==null)
      this.insertRowsOnTop = insertRowsOnTop;
    else
      table.setInsertRowsOnTop(insertRowsOnTop);
  }


  /**
   * @return header height
   */
  public final int getHeaderHeight() {
      return headerHeight;
  }


  /**
   * Set the header height.
   * This method can be invoked only before grid control is visible.
   * @param headerHeight header height
   */
  public final void setHeaderHeight(int headerHeight) {
    if (table==null)
    this.headerHeight = headerHeight;
  }


  /**
   * @return force the editing of one row only: the current selected row: <code>false</code> all rows are editable, <code>true</code> edit is allowed only on current selected row
   */
  public final boolean isEditOnSingleRow() {
    return editOnSingleRow;
  }


  /**
   * Define if cell editing is allows on one row only or on all rows: <code>false</code> all rows are editable, <code>true</code> edit is allowed only on current selected row
   * @param editOnSingleRow <code>false</code> all rows are editable, <code>true</code> edit is allowed only on current selected row
   */
  public final void setEditOnSingleRow(boolean editOnSingleRow) {
    this.editOnSingleRow = editOnSingleRow;
  }


  /**
   * @return GridExportOptions object created, starting from current visible columns
   */
  public final GridExportOptions getDefaultGridExportOptions() {
    if (table!=null)
      return table.getDefaultGridExportOptions();
    else
      return null;
  }


  /**
   * @param attributesToExport attributes to export
   * @return GridExportOptions object created, starting from specified attributes to export
   */
  public final GridExportOptions getDefaultGridExportOptions(HashSet attributesToExport) {
    if (table!=null)
      return table.getDefaultGridExportOptions(attributesToExport);
    else
      return null;
  }


  /**
   * Add a list-filter for the specified column, showed in the quick filter panel.
   * @param attributeName attribute name that identifies the column having a list-filter to remove
   */
  public final void addListFilter(String attributeName,ListFilterController filter) {
    listFilters.put(attributeName,filter);
  }


  /**
   * Remove a list-filter for the specified column.
   * @param attributeName attribute name that identifies the column having a filter to remove
   */
  public final void removeComboFilter(String attributeName) {
    listFilters.remove(attributeName);
  }


  /**
   * Remove global key listeners related to Form panels.
   */
  private void dropFocusFromAllForms() {
    try {
      KeyListener[] ll = ApplicationEventQueue.getInstance().getKeyListeners();
      for(int i=0;i<ll.length;i++)
        if (ll[i] instanceof FormShortcutsListener)
          ((FormShortcutsListener)ll[i]).getForm().dropFocusFromForm();
    }
    catch (Exception ex1) {
    }
  }


  /**
   * @return show current page number in grid
   */
  public final boolean isShowPageNumber() {
    return showPageNumber;
  }


  /**
   * Show/hide current page number in grid.
   * @param showPageNumber flag used to show current page number in grid
   */
  public final void setShowPageNumber(boolean showPageNumber) {
    this.showPageNumber = showPageNumber;
  }


  /**
   * Show/hide a column.
   * Do not invoke this method before grid is being visible.
   * @param attributeName attribute name that identities the column
   * @param visible <code>true</code> to show column; <code>false</code> to hide it
   */
  public final void setVisibleColumn(String attributeName,boolean visible) {
    if (table!=null) {
      table.setVisibleColumn(attributeName,visible);
    }
  }


  /**
   * @return current sorted columns
   */
  public final ArrayList getCurrentSortedColumns() {
    if (topTable!=null)
      return topTable.getCurrentSortedColumns();
    else if (table!=null)
      return table.getCurrentSortedColumns();
    else
      return null;
  }


  /**
   * @return current sorted columns versus (Ascending/Descending)
   */
  public final ArrayList getCurrentSortedVersusColumns() {
    if (topTable!=null)
      return topTable.getCurrentSortedVersusColumns();
    else if (table!=null)
      return table.getCurrentSortedVersusColumns();
    else
      return null;
  }


  /**
   * Remove the sorting condition currently applied to the specified column.
   * Do not invoke this method before grid is being visible.
   * @param attributeName attribute name that identities the column
   * @param reloadGrid <code>true</code> to force grid reloading; <code>false</code> to do not reload grid (for instance when sorting conditions for other columns must be removed too)
   */
  public final void removeSortedColumn(String attributeName,boolean reloadGrid) {
    if (topTable!=null) {
      int colIndex = topTable.getCurrentSortedColumns().indexOf(attributeName);
      if (colIndex==-1) {
        Logger.error(this.getClass().getName(), "removeSortedColumn", "The specified attribute name '"+attributeName+"' does not exists", null);
        return;
      }
      topTable.getCurrentSortedColumns().remove(colIndex);
      topTable.getCurrentSortedVersusColumns().remove(colIndex);
      topTable.repaint();
      if (reloadGrid)
        reloadData();
      else if (!orderWithLoadData && table!=null) {
        table.getGrid().internalSorting();
        if (table.getLockedGrid()!=null)
          table.getLockedGrid().internalSorting();
      }

    }
    else if (table!=null) {
      int colIndex = table.getCurrentSortedColumns().indexOf(attributeName);
      if (colIndex==-1) {
        Logger.error(this.getClass().getName(), "removeSortedColumn", "The specified attribute name '"+attributeName+"' does not exists", null);
        return;
      }
      table.getCurrentSortedColumns().remove(colIndex);
      table.getCurrentSortedVersusColumns().remove(colIndex);
      table.repaint();
      if (reloadGrid)
        reloadData();
      else if (!orderWithLoadData && table!=null) {
        table.getGrid().internalSorting();
        if (table.getLockedGrid()!=null)
          table.getLockedGrid().internalSorting();
      }

    }
  }


  /**
   * Add the specified sorting condition to the column identified by the attribute name (as last sorting condition).
   * Do not invoke this method before grid is being visible.
   * @param attributeName attribute name that identities the column
   * @param sortingVersus sorting versus; possible values: Consts.ASC_SORTED,Consts.DESC_SORTED
   * @param reloadGrid <code>true</code> to force grid reloading; <code>false</code> to do not reload grid (for instance when sorting conditions for other columns must be add too)
   */
  public final void addSortedColumn(String attributeName,String sortingVersus,boolean reloadGrid) {
    if (!sortingVersus.equals(Consts.ASC_SORTED) &&
        !sortingVersus.equals(Consts.DESC_SORTED)) {
      Logger.error(this.getClass().getName(), "addSortedColumn", "Invalid sorting versus. Allowed values: Consts.ASC_SORTED,Consts.DESC_SORTED", null);
      return;
    }
    if (topTable!=null) {
      int colIndex = topTable.getCurrentSortedColumns().indexOf(attributeName);
      if (colIndex!=-1) {
        topTable.getCurrentSortedColumns().remove(colIndex);
        topTable.getCurrentSortedVersusColumns().remove(colIndex);
      }
      topTable.getCurrentSortedColumns().add(attributeName);
      topTable.getCurrentSortedVersusColumns().add(sortingVersus);
      topTable.repaint();
      if (reloadGrid)
        reloadData();
      else if (!orderWithLoadData && table!=null) {
        table.getGrid().internalSorting();
        if (table.getLockedGrid()!=null)
          table.getLockedGrid().internalSorting();
      }
    }
    else if (table!=null) {
      int colIndex = table.getCurrentSortedColumns().indexOf(attributeName);
      if (colIndex!=-1) {
        table.getCurrentSortedColumns().remove(colIndex);
        table.getCurrentSortedVersusColumns().remove(colIndex);
      }
      table.getCurrentSortedColumns().add(attributeName);
      table.getCurrentSortedVersusColumns().add(sortingVersus);
      table.repaint();
      if (reloadGrid)
        reloadData();
      else if (!orderWithLoadData && table!=null) {
        table.getGrid().internalSorting();
        if (table.getLockedGrid()!=null)
          table.getLockedGrid().internalSorting();
      }
    }
  }


  /**
   * @return <code>true</code> means that grid has to automatically retrieve additional rows in fast search, when search criteria fails
   */
  public final boolean isSearchAdditionalRows() {
    return searchAdditionalRows;
  }


  /**
   * Define whether enabling the retrieval of additional rows in fast search, when search criteria fails.
   * Default value: ClientSettings.SEARCH_ADDITION_ROWS
   * @param searchAdditionalRows flag used in grid to enable the retrieval of additional rows in fast search, when search criteria fails
   */
  public final void setSearchAdditionalRows(boolean searchAdditionalRows) {
    this.searchAdditionalRows = searchAdditionalRows;
  }


  /**
   * @return height of status panel (expressed in pixels)
   */
  public final int getStatusPanelHeight() {
    return labelPanel.getSize().height;
  }

  /**
   * @return allows the columns sorting in edit mode too; note that this setting is used only when <code>orderWithLoadData</code> property is set to <code>false</code> */
  public final boolean isAllowColumnsSortingInEdit() {
    return allowColumnsSortingInEdit;
  }


  /**
   * Define whether columns sorting is enabled also in edit mode.
   * Note that this setting is used only when <code>orderWithLoadData</code> property is set to <code>false</code>
   * @param allowColumnsSortingInEdit allows the columns sorting in edit mode too
   */
  public final void setAllowColumnsSortingInEdit(boolean allowColumnsSortingInEdit) {
    this.allowColumnsSortingInEdit = allowColumnsSortingInEdit;
  }


  /**
   * @return define where to anchor locked columns: to the left or to the right of the grid; default value: <code>true</code> i.e. to the left
   */
  public final boolean isAnchorLockedColumnsToLeft() {
    return anchorLockedColumnsToLeft;
  }


  /**
   * Define where to anchor locked columns: to the left or to the right of the grid.
   * @param anchorLockedColumnsToLeft <code>true</code> to anchor locked columns the left of the grid
   */
  public final void setAnchorLockedColumnsToLeft(boolean anchorLockedColumnsToLeft) {
    this.anchorLockedColumnsToLeft = anchorLockedColumnsToLeft;
  }
  public boolean isAllowColumnsPermission() {
    return allowColumnsPermission;
  }
  public boolean isAllowColumnsProfile() {
    return allowColumnsProfile;
  }
  public void setAllowColumnsPermission(boolean allowColumnsPermission) {
    this.allowColumnsPermission = allowColumnsPermission;
  }
  public void setAllowColumnsProfile(boolean allowColumnsProfile) {
    this.allowColumnsProfile = allowColumnsProfile;
  }


  /**
   * Find (starting from the first row) in grid the specified value within the column identified by
   * the specified attribute name, using "equalsIgnoreCase" as matching criteria.
   * @param attributeName attribute used to identify the column where restricting the search
   * @param value value to search
   * @return row index having the specified value or -1 in case of search without a result
   */
  public final int findNextValue(String attributeName,Object value) {
    return findNextValue(attributeName,value,0);
  }


  /**
   * Find in grid the specified value within the column identified by the specified attribute name,
   * using "equalsIgnoreCase" as matching criteria.
   * @param attributeName attribute used to identify the column where restricting the search
   * @param value value to search
   * @param startingFromRow row index in grid to use to start the search
   * @param findCritera criteria to use with findNextValue method; allowed values: <code>GridControl.FIND_CRITERIA_EQUALS</code>, <code>GridControl.FIND_CRITERIA_EQUALS_IGNORE_CASE</code>, <code>GridControl.FIND_CRITERIA_STARTS_WITH</code>, <code>GridControl.FIND_CRITERIA_STARTS_WITH_IGNORE_CASE</code>, <code>GridControl.FIND_CRITERIA_CONTAINS</code>, <code>GridControl.FIND_CRITERIA_CONTAINS_IGNORE_CASE</code>
   * @return row index having the specified value or -1 in case of search without a result
   */
  public final int findNextValue(String attributeName,Object value,int startingFromRow) {
    return findNextValue(attributeName,value,startingFromRow,GridControl.FIND_CRITERIA_EQUALS_IGNORE_CASE);
  }


  /**
   * Find in grid the specified value within the column identified by the specified attribute name,
   * using the matching criteria specified as argument.
   * @param attributeName attribute used to identify the column where restricting the search
   * @param value value to search
   * @param startingFromRow row index in grid to use to start the search
   * @param findCritera criteria to use with findNextValue method; allowed values: <code>GridControl.FIND_CRITERIA_EQUALS</code>, <code>GridControl.FIND_CRITERIA_EQUALS_IGNORE_CASE</code>, <code>GridControl.FIND_CRITERIA_STARTS_WITH</code>, <code>GridControl.FIND_CRITERIA_STARTS_WITH_IGNORE_CASE</code>, <code>GridControl.FIND_CRITERIA_CONTAINS</code>, <code>GridControl.FIND_CRITERIA_CONTAINS_IGNORE_CASE</code>
   * @return row index having the specified value or -1 in case of search without a result
   */
  public final int findNextValue(String attributeName,Object value,int startingFromRow,int findCritera) {
    if (startingFromRow>table.getVOListTableModel().getRowCount())
      return -1;
    int col = table.getVOListTableModel().findColumn(attributeName);
    Object obj = null;
    for(int i=startingFromRow;i<table.getVOListTableModel().getRowCount();i++) {
      obj = table.getVOListTableModel().getValueAt(i, col);
      if (obj==null && value==null)
        return i;
      else if (obj!=null && matchValue(obj.toString(),value.toString(),findCritera))
        return i;
    }
    return -1;
  }


  private boolean matchValue(String o1,String o2,int findCritera) {
    if (findCritera==GridControl.FIND_CRITERIA_EQUALS)
      return o1.equals(o2);
    else if (findCritera==GridControl.FIND_CRITERIA_EQUALS_IGNORE_CASE)
      return o1.equalsIgnoreCase(o2);
    else if (findCritera==GridControl.FIND_CRITERIA_STARTS_WITH)
      return o1.startsWith(o2);
    else if (findCritera==GridControl.FIND_CRITERIA_STARTS_WITH_IGNORE_CASE)
      return o1.toUpperCase().startsWith(o2.toUpperCase());
    else if (findCritera==GridControl.FIND_CRITERIA_CONTAINS)
      return o1.indexOf(o2)!=-1;
    else if (findCritera==GridControl.FIND_CRITERIA_CONTAINS_IGNORE_CASE)
      return o1.toUpperCase().indexOf(o2.toUpperCase())!=-1;
    else {
      Logger.error(this.getClass().getName(), "matchValue", "Unsopported search criteria", null);
      return false;
    }
  }




  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used inside GridControl to add column to the columns container.
   * Moreover, it add the column to components list (used by JDeveloper UI Designer).</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @version 1.0
   */
  public class ColumnContainer extends JPanel { /** columns container; used only in design-time */

    /**
     * Method invoked by UI designers (e.g. NetBeans and Swing Designer in Eclipse) that adds a column via "containerDelegate" property defined in GridControlBeanInfo
     * @param comp Component to add, WITHOUT contraint
     */
    public final Component add(Component comp) {
      super.add(comp,null);
      components.add(comp);
      return comp;
    }


      /**
       * Method invoked by UI designers (e.g. JDeveloper) that does not correctly use the "containerDelegate" property defined in GridControlBeanInfo
       * @param comp Component to add
       * @param constraint contraint to use for the component to add
       */
      public final void add(Component comp,Object constraint) {
        super.add(comp,constraint);
        components.add(comp);
      }

  }


}

