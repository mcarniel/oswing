package org.openswing.swing.lookup.client;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.util.HashMap;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.message.receive.java.*;
import java.awt.*;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.logger.client.Logger;
import org.openswing.swing.form.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.model.client.*;
import org.openswing.swing.form.model.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.form.model.client.*;

import org.openswing.swing.util.java.Consts;
import org.openswing.swing.util.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.tree.client.TreePanel;
import javax.swing.JSplitPane;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.tree.client.TreeDataLocator;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup controller.</p>
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
public class LookupController {

  /** mapping between lookup v.o. attributes and lookup container v.o. attributes */
  private LookupMapper lookupMapper = new LookupMapper();

  /** lookup value object */
  private ValueObject lookupVO = null;

  /** lookup frame title */
  private String frameTitle = "Code Selection";

  /** lookup frame frame size; default: 300 x 400 pixels */
  private Dimension framePreferredSize = new Dimension(300,400);

  /** lookup grid frame reference */
  private LookupGridFrame lookupGridFrame = null;

  /** lookup tree frame reference */
  private LookupTreeFrame lookupTreeFrame = null;

  /** lookup tree+grid frame reference */
  private LookupTreeGridFrame lookupTreeGridFrame = null;

  /** selected row on lookup grid */
  private int selectedRow = -1;

  /** list of lookup listeners */
  private ArrayList listeners = new ArrayList();

  /** columns associated to lookup grid */
  private Column[] colProperties = new Column[0];

  /** flag used to set visibility on all columns of lookup grid; default "false"  */
  private boolean allColumnVisible = false;

  /** default preferredWidth for all columns of lookup grid; default 100 pixels */
  private int allColumnPreferredWidth = 100;

  /** lookup container (optional) */
  private Form form = null;

  /** lookup data source */
  private LookupDataLocator lookupDataLocator = null;

  /** constant used in "codeSelectionWindow" to view a grid frame when pressing the lookup button */
  public static final int GRID_FRAME = 0;

  /** constant used in "codeSelectionWindow" to view a tree frame when pressing the lookup button */
  public static final int TREE_FRAME = 1;

  /** constant used in "codeSelectionWindow" to view a tree+grid frame when pressing the lookup button */
  public static final int TREE_GRID_FRAME = 2;

  /** this flag is used to set the code selection window; three values are allowed: GRID_FRAME, TREE_FRAME and TREE_GRID_FRAME */
  private int codeSelectionWindow = GRID_FRAME;

  /** this flag is used when codeSelectionWindow is set to TREE_FRAME: it means that user can select only leaves (by double clicking) */
  private boolean allowTreeLeafSelectionOnly = true;

  /** this flag is used to define what to do in case of invalid code on validation task; possible values: ON_INVALID_CODE_xxx; default value: ClientSettings.ON_INVALID_CODE */
  private int onInvalidCode = ClientSettings.ON_INVALID_CODE;

  /** constant used in "onInvalidCode" property to clear code on validation task that returns an invalid code */
  public static final int ON_INVALID_CODE_CLEAR_CODE = 0;

  /** constant used in "onInvalidCode" property to restore the last valid code (on "" if there no exists any valid code in the past) on validation task that returns an invalid code */
  public static final int ON_INVALID_CODE_RESTORE_LAST_VALID_CODE = 1;

  /** constant used in "onInvalidCode" property to do nothing except restore focus inside the code input field on validation task that returns an invalid code */
  public static final int ON_INVALID_CODE_RESTORE_FOCUS = 2;

  /** last valid code */
  private String lastValidCode = null;

  /** last invalid code */
  private String lastInvalidCode = "";

  /** flag used by Form.save to check if lookup control is in a valid state */
  private boolean codeValid = true;


  /**
   * Execute the code validation.
   * @param parentComponent component which contains the code input field
   * @param code code to validate
   * @return <code>true</code> if code is correcly validated, <code>false</code> otherwise
   */
  public final void validateCode(JComponent parentComponent,String code, final LookupParent lookupParent) throws RestoreFocusOnInvalidCodeException {
    try {
      if (lastValidCode == null) {
        lastValidCode = lookupParent.getLookupCodeParentValue().toString();
      }
    }
    catch (Exception ex1) {
      lastValidCode = "";
    }

    fireLookupActionEvent(lookupParent.getValueObject());
    if (code==null || code.trim().length()==0) {
      codeValid = true;

      // clear code event: reset lookup v.o. ...
      createVoidLookupVO();
      // update lookup container aggiorno v.o. ...
      updateParentModel(lookupParent);
      // fire code changed event...
      fireCodeChangedEvent(lookupParent.getValueObject());
      // fire code validated event...
      fireCodeValidatedEvent(true);
    }
    else {
      try {
        // execute code validation...
        final Response r = lookupDataLocator.validateCode( code );
        if (r==null || !(r instanceof VOListResponse || r instanceof ErrorResponse)) {
          Logger.error(this.getClass().getName(),"validateCode","Error while validating lookup code: lookup data locator must always returns an instanceof VOListResponse.",null);
          return;
        }

        if (!r.isError() && ((VOListResponse)r).getRows().size()==1) {
          // code was correctly validated...
          codeValid = true;
          lastValidCode = code;
          lookupVO = (ValueObject)((VOListResponse)r).getRows().get(0);
          updateParentModel(lookupParent);

// already fired inside updateParentModel method!
//          // fire code changed event...
//          fireCodeChangedEvent(lookupParent.getValueObject());
          // fire code validated event...
          fireCodeValidatedEvent(true);
        }
        else if (!r.isError() && ((VOListResponse)r).getRows().size()>1) {
          // code was correctly validated but more than one row has been returned: a lookup grid frame will be opened...
          if (!createLookupGrid(
            ClientUtils.getParentFrame(parentComponent),
            lookupParent,
            new GridDataLocator() {

              public Response loadData(
                  int action,
                  int startIndex,
                  Map filteredColumns,
                  ArrayList currentSortedColumns,
                  ArrayList currentSortedVersusColumns,
                  Class valueObjectType,
                  Map otherGridParams) {
                return r;
              }

            }))
              fireCodeValidatedEvent(false);
        }
        else {
          // code was NOT correctly validated...
          if (onInvalidCode==ON_INVALID_CODE_CLEAR_CODE) {
            codeValid = true;
            createVoidLookupVO();
            updateParentModel(lookupParent);
            // fire code changed event...
            fireCodeChangedEvent(lookupParent.getValueObject());

            fireCodeValidatedEvent(false);
            JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(parentComponent),
                ClientSettings.getInstance().getResources().getResource("Code is not correct."),
                ClientSettings.getInstance().getResources().getResource("Code Validation"),
                JOptionPane.ERROR_MESSAGE
            );
          }
          else if (onInvalidCode==ON_INVALID_CODE_RESTORE_LAST_VALID_CODE) {
            JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(parentComponent),
                ClientSettings.getInstance().getResources().getResource("Code is not correct."),
                ClientSettings.getInstance().getResources().getResource("Code Validation"),
                JOptionPane.ERROR_MESSAGE
            );
            if (!lastValidCode.equals(code))
              validateCode(parentComponent,lastValidCode,lookupParent);
            else
              validateCode(parentComponent,"",lookupParent);
          }
          else if (onInvalidCode==ON_INVALID_CODE_RESTORE_FOCUS) {
            if (!lastInvalidCode.equals(code)) {
              lastInvalidCode = code;
              JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(parentComponent),
                ClientSettings.getInstance().getResources().getResource("Code is not correct."),
                ClientSettings.getInstance().getResources().getResource("Code Validation"),
                JOptionPane.ERROR_MESSAGE
              );
            }
            codeValid = false;
            throw new RestoreFocusOnInvalidCodeException();
          }
          else
            Logger.error(this.getClass().getName(),"validateCode","Error while validating lookup code: invalid 'onInvalidCode' property value: "+onInvalidCode,null);

        }
      }
      catch (RestoreFocusOnInvalidCodeException ex) {
        throw ex;
      }
      catch (Exception ex) {
        createVoidLookupVO();
        updateParentModel(lookupParent);
        // fire code changed event...
        fireCodeChangedEvent(lookupParent.getValueObject());
        // fire code validated event...
        fireCodeValidatedEvent(false);

        Logger.error(this.getClass().getName(),"validateCode","Error while validating lookup code.",ex);
      }
    }
  }


  /**
   * Update the value object of the lookup parent container, only for attributes defined in LookupMapper.
   * @param lookupParent lookup container
   */
  private void updateParentModel(LookupParent lookupParent) {
    if (lookupVO!=null && lookupMapper!=null) {
      try {
        // update lookup container vo from lookup vo values...
        Enumeration lookupAttributes = lookupMapper.getLookupChangedAttributes();
        String lookupAttributeName, lookupMethodName;
        Method lookupMethod;
        String attrName = null;
        while (lookupAttributes.hasMoreElements()) {
          lookupAttributeName = (String) lookupAttributes.nextElement();
          if (lookupAttributeName.length()==0) {
            // there has been defined a link between the whole lookup v.o. and an attribute in the container v.o.
            // related to an inner v.o.
            if (!lookupMapper.setParentAttribute(
                lookupParent,
                lookupAttributeName,
                lookupVO.getClass(),
                lookupVO))
              Logger.error(this.getClass().getName(), "updateParentModel", "Error while setting lookup container value object.", null);
            else if (form!=null) {
               attrName = (String)lookupMapper.getParentAttributeName(lookupAttributeName);
               if (attrName!=null && form!=null)
                 form.pull(attrName);
             }
         }
         else {
           lookupMethodName = "get" + String.valueOf(Character.toUpperCase(lookupAttributeName.charAt(0))) + lookupAttributeName.substring(1);
           lookupMethod = lookupVO.getClass().getMethod(lookupMethodName, new Class[0]);
           if (!lookupMapper.setParentAttribute(
                 lookupParent,
                 lookupAttributeName,
                 lookupMethod.getReturnType(),
                 lookupMethod.invoke(lookupVO, new Object[0])
           ))
             Logger.error(this.getClass().getName(),"updateParentModel","Error while setting lookup container value object.",null);
           else if (form!=null) {
              attrName = (String)lookupMapper.getParentAttributeName(lookupAttributeName);
              if (attrName!=null && form!=null)
                form.pull(attrName);
            }
         }
        }

      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      catch (Error er) {
        er.printStackTrace();
      }

      fireCodeChangedEvent(lookupParent.getValueObject());

    } else {
      Logger.error(this.getClass().getName(),"updateParentModel","You must set 'lookupMapper' property",null);
    }
  }


  /**
   * Method called by CodLookupControl when clicking on lookup button: it opens lookup frame (grid/tree/grid+tree frame).
   */
  public final void openLookupFrame(JComponent source,LookupParent lookupParent) {
    openLookupFrame(
        ClientUtils.getParentFrame(source),
        lookupParent
    );
  }


  /**
   * Method called by a grid column cell editor when user click on lookup button: it opens lookup frame (grid/tree/grid+tree frame).
   * @param parentFrame parent frame
   * @param lookupParent lookup container
   */
  public final void openLookupFrame(JFrame parentFrame,final LookupParent lookupParent) {
    if (codeSelectionWindow==GRID_FRAME)
      createLookupGrid(
        parentFrame,
        lookupParent
      );
    else if (codeSelectionWindow==TREE_FRAME)
      createLookupTree(
        parentFrame,
        lookupParent
      );
    else if (codeSelectionWindow==TREE_GRID_FRAME)
      createLookupTreeGrid(
        parentFrame,
        lookupParent
      );
  }


  /**
   * Method called by openLookupGrid methods to open lookup grid frame.
   * @param parentFrame parent frame
   * @param parentVO lookup container v.o. (which is updated when user will select a grid row)
   * @param dataLocator data source used to fetching data for lookup grid
   * @return <code>true</code> if grid frame was correcly opened, <code>false</code> otherwise
   */
  private boolean createLookupGrid(JFrame parentFrame,LookupParent lookupParent) {
    return createLookupGrid(
      parentFrame,
      lookupParent,
      new GridDataLocator() {

        public Response loadData(
            int action,
            int startIndex,
            Map filteredColumns,
            ArrayList currentSortedColumns,
            ArrayList currentSortedVersusColumns,
            Class valueObjectType,
            Map otherGridParams) {
              return lookupDataLocator.loadData(
                  action,
                  startIndex,
                  filteredColumns,
                  currentSortedColumns,
                  currentSortedVersusColumns,
                  valueObjectType
              );
        }

      }
    );
  }



  /**
   * Method called by openLookupGrid methods to open lookup grid frame.
   * @param parentFrame parent frame
   * @param parentVO lookup container v.o. (which is updated when user will select a grid row)
   * @param dataLocator data source used to fetching data for lookup grid
   * @return <code>true</code> if grid frame was correcly opened, <code>false</code> otherwise
   */
  private boolean createLookupGrid(JFrame parentFrame,final LookupParent lookupParent,GridDataLocator dataLocator) {
    fireLookupActionEvent(lookupParent.getValueObject());
    selectedRow = -1;
    if (lookupVO==null) {
      Logger.error(this.getClass().getName(),"createLookupGrid","You must set 'lookupVO' property",null);
      return false;
    }
    try {
      if (lookupGridFrame!=null) {
        lookupGridFrame.setVisible(false);
        lookupGridFrame.dispose();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    try {
      GridController container = new GridController() {

        public void enterButton(int rowNumber,ValueObject valueObject) { doubleClick(rowNumber,valueObject); }
        public void doubleClick(int rowNumber,ValueObject valueObject) {
          selectedRow = rowNumber;
          lookupVO = (ValueObject) lookupGridFrame.getTable().getVOListTableModel().getObjectForRow(selectedRow);
          updateParentModel(lookupParent);
          lookupGridFrame.setVisible(false);
          lookupGridFrame.dispose();
          codeValid = true;
        }
      };

      Grids table = new Grids(
          null,
          0,
          lookupVO.getClass().getName(),
          colProperties,
          container,
          new GridStatusPanel(),
          dataLocator,
          new HashMap(),
          true,
          new ArrayList(),
          Grid.MAIN_GRID
      );

      // create the lookup grid frame...
      lookupGridFrame = new LookupGridFrame(parentFrame,frameTitle, table);
      lookupGridFrame.setSize(framePreferredSize);
      table.reload();
      ClientUtils.centerDialog(parentFrame,lookupGridFrame);
      lookupGridFrame.setVisible(true);


      return true;
    }
    catch (Exception ex) {
      Logger.error(this.getClass().getName(),"createLookupGrid","Error while creating lookup grid frame.",ex);
      return false;
    }
  }


  /**
   * Method called by openLookupGrid methods to open lookup tree frame.
   * @param parentFrame parent frame
   * @param parentVO lookup container v.o. (which is updated when user will select a grid row)
   * @param dataLocator data source used to fetching data for lookup grid
   * @return <code>true</code> if grid frame was correcly opend, <code>false</code> otherwise
   */
  private boolean createLookupTree(JFrame parentFrame,final LookupParent lookupParent) {
    fireLookupActionEvent(lookupParent.getValueObject());
    if (lookupVO==null) {
      Logger.error(this.getClass().getName(),"createLookupTree","You must set 'lookupVO' property",null);
      return false;
    }
    try {
      if (lookupTreeFrame!=null) {
        lookupTreeFrame.setVisible(false);
        lookupTreeFrame.dispose();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    try {
      TreeController container = new TreeController() {

        public void leftClick(DefaultMutableTreeNode node) {}
        public boolean rightClick(DefaultMutableTreeNode node) { return false; }
        public void doubleClick(DefaultMutableTreeNode node) {
          if (allowTreeLeafSelectionOnly && !node.isLeaf())
            return;
          lookupVO = (ValueObject)node.getUserObject();
          updateParentModel(lookupParent);
          lookupTreeFrame.setVisible(false);
          lookupTreeFrame.dispose();
        }
      };

      TreePanel treePanel = new TreePanel();
      treePanel.setTreeController(container);
      treePanel.setTreeDataLocator(lookupDataLocator);

      // create the lookup tree frame...
      lookupTreeFrame = new LookupTreeFrame(parentFrame,frameTitle, treePanel);
      lookupTreeFrame.setSize(framePreferredSize);
      ClientUtils.centerDialog(parentFrame,lookupTreeFrame);
      lookupTreeFrame.setVisible(true);

      return true;
    }
    catch (Exception ex) {
      Logger.error(this.getClass().getName(),"createLookupTree","Error while creating lookup tree frame.",ex);
      return false;
    }
  }


  /**
   * Method called by openLookupGrid methods to open lookup tree+grid frame.
   * @param parentFrame parent frame
   * @param parentVO lookup container v.o. (which is updated when user will select a grid row)
   * @param dataLocator data source used to fetching data for lookup grid
   * @return <code>true</code> if grid frame was correcly opend, <code>false</code> otherwise
   */
  private boolean createLookupTreeGrid(JFrame parentFrame,final LookupParent lookupParent) {
    fireLookupActionEvent(lookupParent.getValueObject());
    selectedRow = -1;
    if (lookupVO==null) {
      Logger.error(this.getClass().getName(),"createLookupTreeGrid","You must set 'lookupVO' property",null);
      return false;
    }
    try {
      if (lookupTreeGridFrame!=null) {
        lookupTreeGridFrame.setVisible(false);
        lookupTreeGridFrame.dispose();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    try {
      GridController gridContainer = new GridController() {

        public void enterButton(int rowNumber,ValueObject valueObject) { doubleClick(rowNumber,valueObject); }
        public void doubleClick(int rowNumber,ValueObject valueObject) {
          selectedRow = rowNumber;
          lookupVO = (ValueObject) lookupTreeGridFrame.getTable().getVOListTableModel().getObjectForRow(selectedRow);
          updateParentModel(lookupParent);
          lookupTreeGridFrame.setVisible(false);
          lookupTreeGridFrame.dispose();
        }
      };

      final Grids table = new Grids(
          null,
          0,
          lookupVO.getClass().getName(),
          colProperties,
          gridContainer,
          new GridStatusPanel(),
          new GridDataLocator() {

            public Response loadData(
                int action,
                int startIndex,
                Map filteredColumns,
                ArrayList currentSortedColumns,
                ArrayList currentSortedVersusColumns,
                Class valueObjectType,
                Map otherGridParams) {
                  return lookupDataLocator.loadData(
                      action,
                      startIndex,
                      filteredColumns,
                      currentSortedColumns,
                      currentSortedVersusColumns,
                      valueObjectType
                  );
            }

          },
          new HashMap(),
          true,
          new ArrayList(),
          Grid.MAIN_GRID
      );

      TreeController treeContainer = new TreeController() {

        public void leftClick(DefaultMutableTreeNode node) {
          // if the user select a tree node,
          // then grid is reloaded, filtered by the current selected tree node UserObject...
          Map map = lookupDataLocator.getLookupFrameParams();
          map.put(Consts.TREE_FILTER,node.getUserObject());
          table.reload();
        }
        public boolean rightClick(DefaultMutableTreeNode node) { return false; }
        public void doubleClick(DefaultMutableTreeNode node) { }
      };

      TreePanel treePanel = new TreePanel();
      treePanel.setTreeController(treeContainer);
      treePanel.setTreeDataLocator(lookupDataLocator);

      // create the lookup tree grid frame...
      lookupTreeGridFrame = new LookupTreeGridFrame(parentFrame,frameTitle,treePanel,table);
      lookupTreeGridFrame.setSize(framePreferredSize);
//      table.reload();
      ClientUtils.centerDialog(parentFrame,lookupTreeGridFrame);
      lookupTreeGridFrame.setVisible(true);

      return true;
    }
    catch (Exception ex) {
      Logger.error(this.getClass().getName(),"createLookupTreeGrid","Error while creating lookup tree grid frame.",ex);
      return false;
    }
  }


  /**
   * Create an empty value object for the lookup.
   */
  private void createVoidLookupVO() {
    try {
      if (this.lookupVO==null)
        Logger.error(this.getClass().getName(),"createVoidLookupVO","Error while creating lookup value object: 'lookupVO' is null.",null);
      else
        this.lookupVO = (ValueObject) Class.forName(this.lookupVO.getClass().getName()).getConstructor(new Class[0]).newInstance(new Object[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      this.lookupVO = null;
    }
    catch (Error er) {
      er.printStackTrace();
      this.lookupVO = null;
    }
  }


  /**
   * Set column visibility in the lookup grid frame.
   * @param lookupAttributeName attribute name that identifies the grid column
   * @param visible column visibility state
   */
  public final void setVisibleColumn(String lookupAttributeName, boolean visible) {
    try {
      Column infoTemp;
      int visibleIndex = -1;
      int index = -1;
      for (int i = 0; i < colProperties.length; i++) {
        if (colProperties[i].isVisible())
          visibleIndex = i;
        if (colProperties[i].getColumnName().equals(lookupAttributeName)) {
          colProperties[i].setColumnVisible(visible);
          colProperties[i].setColumnSelectable(visible);
          index = i;
          break;
        }
      }
      if (visible) {
        if (visibleIndex==-1)
          visibleIndex=0;
        else if ((visibleIndex-1) < colProperties.length)
          visibleIndex++;
        if ( (index > -1) && (index != visibleIndex)) {
          infoTemp = colProperties[index];
          colProperties[index] = colProperties[visibleIndex];
          colProperties[visibleIndex] = infoTemp;
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Set column width in the lookup grid frame.
   * @param lookupAttributeName attribute name that identifies the grid column
   * @param preferredWidth column width
   */
  public final void setPreferredWidthColumn(String lookupAttributeName,int preferredWidth) {
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].getColumnName().equals(lookupAttributeName)) {
        colProperties[i].setPreferredWidth(preferredWidth);
        return;
      }
    Logger.error(this.getClass().getName(),"setPreferredWidthColumn","The attribute '"+(lookupAttributeName==null?"null":"'"+lookupAttributeName+"'")+"' does not exist.",null);
  }


  /**
   * @return lookup frame title
   */
  public final String getFrameTitle() {
    return frameTitle;
  }


  /**
   * Set lookup frame title.
   * @param frameTitle lookup frame title
   */
  public final void setFrameTitle(String frameTitle) {
    this.frameTitle = frameTitle;
  }


  /**
   * Add a lookup listener.
   * @param listener lookup listener.
   */
  public final void addLookupListener(LookupListener listener) {
    listeners.add(listener);
  }


  /**
   * Remove a lookup listener.
   * @param listener lookup listener
   */
  public final void removeLookupListener(LookupListener listener) {
    listeners.remove(listener);
  }


  /**
   * Method called when a code is validated and also when a row is selected in the lookup grid frame.
   * @param validated <code>true</code> if code is correct, <code>false</code> otherwise
   */
  private final void fireCodeValidatedEvent(boolean validated) {
    for(int i=0;i<listeners.size();i++)
      ((LookupListener)listeners.get(i)).codeValidated(validated);
  }


  /**
   * Method called when the code is changed.
   * @param parentVO value object of the lookup parent container
   */
  private final void fireCodeChangedEvent(ValueObject parentVO) {
    for(int i=0;i<listeners.size();i++)
      ((LookupListener)listeners.get(i)).codeChanged(parentVO,lookupMapper.getParentChangedAttributes());
  }


  /**
   * Method called before a code is validate and also when the lookup grid is viewed.
   */
  private final void fireLookupActionEvent(ValueObject parentVO) {
    for(int i=0;i<listeners.size();i++)
      ((LookupListener)listeners.get(i)).beforeLookupAction(parentVO);
  }


  /**
   * @return columns visibility
   */
  public final boolean isAllColumnVisible() {
    return this.allColumnVisible;
  }


  /**
   * Set column visibility for the whole columns of the lookup grid frame.
   * @param visible columns visibility
   */
  public final void setAllColumnVisible(boolean visible) {
    this.allColumnVisible = visible;
    for(int i=0; i<colProperties.length; i++) {
      colProperties[i].setColumnVisible(visible);
      colProperties[i].setColumnSelectable(visible);
    }
  }


  /**
   * @return columns width
   */
  public final int getAllColumnPreferredWidth() {
    return this.allColumnPreferredWidth;
  }


  /**
   * Set columns width for the whole columns of the lookup grid frame.
   * @param preferredWidth columns width
   */
  public final void setAllColumnPreferredWidth(int preferredWidth) {
    this.allColumnPreferredWidth = preferredWidth;
    for(int i=0; i<colProperties.length; i++)
      colProperties[i].setPreferredWidth(preferredWidth);
  }


  /**
   * @return preferred size of the lookup frame
   */
  public final Dimension getFramePreferedSize() {
    return framePreferredSize;
  }


  /**
   * Set the preferred size of the lookup frame.
   * @param framePreferredSize preferred size of the lookup frame
   */
  public final void setFramePreferedSize(Dimension framePreferredSize) {
    this.framePreferredSize = framePreferredSize;
  }


  /**
   * Force the code validation: it calles fireForceValidateEvent method.
   */
  public final void forceValidate() {
    fireForceValidateEvent();
  }


  /**
   * Method called by forceValidate method.
   */
  private void fireForceValidateEvent() {
    for(int i=0;i<listeners.size();i++)
      ((LookupListener)listeners.get(i)).forceValidate();
  }


  /**
   * Method called by CodLookupControl to set the Form container panel.
   * @param form Form container panel
   */
  public final void setForm(Form form) {
    if (this.form==null)
      this.form = form;
  }


  /**
   * @return Form container panel
   */
  public final Form getForm() {
    return this.form;
  }


  /**
   * Set a column header in the lookup grid frame.
   * @param lookupAttributeName attribute name that identifies the column
   * @param name column header name
   */
  public final void setHeaderColumnName(String lookupAttributeName,String name) {
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].getColumnName().equals(lookupAttributeName)) {
        colProperties[i].setHeaderColumnName(name);
        return;
      }
    Logger.error(this.getClass().getName(),"setHeaderColumnName","The attribute '"+(lookupAttributeName==null?"null":"'"+lookupAttributeName+"'")+"' does not exist.",null);
  }


  /**
   * Set grouping setting in a numeric column.
   * @param lookupAttributeName attribute name that identifies the column
   * @param groupEnabled flag used to grouping in a numeric column
   */
  public final void setGroupingEnabledColumn(String lookupAttributeName,boolean groupEnabled) {
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].getColumnName().equals(lookupAttributeName) &&
          colProperties[i] instanceof DecimalColumn) {
        ((DecimalColumn)colProperties[i]).setGrouping(groupEnabled);
        return;
      }
      else if (colProperties[i].getColumnName().equals(lookupAttributeName) &&
          colProperties[i] instanceof IntegerColumn) {
        ((IntegerColumn)colProperties[i]).setGrouping(groupEnabled);
        return;
      }
    Logger.error(this.getClass().getName(),"setGroupingEnabledColumn","There no exists a numeric column having the attribute '"+(lookupAttributeName==null?"null":"'"+lookupAttributeName+"'")+"'",null);
  }


  /**
   * Set domain identifier in a domain type column.
   * @param lookupAttributeName attribute name that identifies the column
   * @param domainId domain identifier
   */
  public final void setDomainColumn(String lookupAttributeName,String domainId) {
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].getColumnName().equals(lookupAttributeName)) {
        Column oldCol = colProperties[i];
        colProperties[i] = new ComboColumn();
        ((ComboColumn)colProperties[i]).setColumnName(lookupAttributeName);
        ((ComboColumn)colProperties[i]).setPreferredWidth(oldCol.getPreferredWidth());
        ((ComboColumn)colProperties[i]).setVisible(oldCol.isVisible());
        ((ComboColumn)colProperties[i]).setSortingOrder(oldCol.getSortingOrder());
        ((ComboColumn)colProperties[i]).setSortVersus(oldCol.getSortVersus());
        ((ComboColumn)colProperties[i]).setHeaderColumnName(oldCol.getHeaderColumnName());
        ((ComboColumn)colProperties[i]).setDomainId(domainId);
        return;
      }
    Logger.error(this.getClass().getName(),"setDomainColumn","The attribute '"+(lookupAttributeName==null?"null":"'"+lookupAttributeName+"'")+"' does not exist.",null);
  }


  /**
   * Define if a column is sortable in the lookup grid frame.
   * @param lookupAttributeName attribute name that identifies the column
   * @param sortable <code>true</code> if the column is sortable, <code>false</code> otherwise
   */
  public final void setSortableColumn(String lookupAttributeName,boolean sortable) {
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].getColumnName().equals(lookupAttributeName)) {
        colProperties[i].setColumnSortable(sortable);
        return;
      }
    Logger.error(this.getClass().getName(),"setSortableColumn","The attribute '"+(lookupAttributeName==null?"null":"'"+lookupAttributeName+"'")+"' does not exist.",null);
  }


  /**
   * Define if a column is filterable in the lookup grid frame.
   * @param lookupAttributeName attribute name that identifies the column
   * @param filterable <code>true</code> if the column is filterable, <code>false</code> otherwise
   */
  public final void setFilterableColumn(String lookupAttributeName,boolean filterable) {
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].getColumnName().equals(lookupAttributeName)) {
        colProperties[i].setColumnFilterable(filterable);
        return;
      }
    Logger.error(this.getClass().getName(),"setFilterableColumn","The attribute '"+(lookupAttributeName==null?"null":"'"+lookupAttributeName+"'")+"' does not exist.",null);
  }


  /**
   * Define if a column is sorted when the lookup grid frame is opened.
   * @param lookupAttributeName attribute name that identifies the column
   * @param sortVersus ascending/descending ordering versus; possible values: Consts.ASC_SORTED or Consts.DESC_SORTED
   */
  public final void setSortedColumn(String lookupAttributeName,String sortVersus) {
    for(int i=0;i<colProperties.length;i++)
      if (colProperties[i].getColumnName().equals(lookupAttributeName)) {
        colProperties[i].setSortVersus(sortVersus);
        return;
      }
    Logger.error(this.getClass().getName(),"setSortedColumn","The attribute '"+(lookupAttributeName==null?"null":"'"+lookupAttributeName+"'")+"' does not exist.",null);
  }


  /**
   * Add a link from an attribute of the lookup v.o. to an attribute of the lookup container v.o.
   * @param lookupAttributeName attribute of the lookup v.o.
   * @param parentAttributeName attribute of the lookup container v.o.
   */
  public final void addLookup2ParentLink(String lookupAttributeName,String parentAttributeName) {
    lookupMapper.addLookup2ParentLink(lookupAttributeName,parentAttributeName);
  }


  /**
   * Add a link from the whole lookup value object to an equivalent inner v.o. included in the container v.o.
   * @param parentAttributeName attribute of the lookup container v.o., related to an inner v.o. having the same type of the lookup v.o.
   */
  public final void addLookup2ParentLink(String parentAttributeName) {
    lookupMapper.addLookup2ParentLink("",parentAttributeName);
  }


  /**
   * @return lookup data locator
   */
  public final LookupDataLocator getLookupDataLocator() {
    return lookupDataLocator;
  }


  /**
   * Set lookup data locator.
   * @param lookupDataLocator lookup data locator
   */
  public final void setLookupDataLocator(LookupDataLocator lookupDataLocator) {
    this.lookupDataLocator = lookupDataLocator;
  }



  /**
   * Set value object class name associated to the lookup: this method calls initLookupVO method.
   * @param lookupValeuObjectClassName value object class name associated to the lookup
   */
  public final void setLookupValueObjectClassName(String lookupValueObjectClassName) {
    initLookupVO(lookupValueObjectClassName);
  }


  /**
   * Method called by setLookupValueObjectClassName:
   * - it creates an empty lookup v.o
   * - it initializes lookup grid column properties.
   * @param lookupValueObjectClassName lookup value object class name
   */
  private void initLookupVO(String lookupValueObjectClassName) {
    try {
      this.lookupVO = (ValueObject) Class.forName(lookupValueObjectClassName).getConstructor(new Class[0]).newInstance(new Object[0]);

      Method[] methods = lookupVO.getClass().getMethods();
      int count = 0;
      for(int i=0;i<methods.length;i++) {
        if (methods[i].getName().startsWith("get") &&
            methods[i].getParameterTypes().length==0 &&
            ( methods[i].getReturnType().equals(String.class) ||
              methods[i].getReturnType().equals(java.math.BigDecimal.class) ||
              methods[i].getReturnType().equals(java.util.Date.class) ||
              methods[i].getReturnType().equals(java.sql.Date.class) ||
              methods[i].getReturnType().equals(java.sql.Timestamp.class) ||
              methods[i].getReturnType().equals(Integer.class) ||
              methods[i].getReturnType().equals(Long.class) ||
              methods[i].getReturnType().equals(Double.class) ||
              methods[i].getReturnType().equals(Float.class) ||
              methods[i].getReturnType().equals(Short.class) ||
              methods[i].getReturnType().equals(Boolean.class))
        )
          count++;
      }
      String[] attributeNames = new String[count];
      this.colProperties = new Column[count];
      count = 0;
      Class colType = null;
      for(int i=0;i<methods.length;i++) {
        if (methods[i].getName().startsWith("get") &&
            methods[i].getParameterTypes().length==0 &&
            ( methods[i].getReturnType().equals(String.class) ||
              methods[i].getReturnType().equals(java.math.BigDecimal.class) ||
              methods[i].getReturnType().equals(java.util.Date.class) ||
              methods[i].getReturnType().equals(java.sql.Date.class) ||
              methods[i].getReturnType().equals(java.sql.Timestamp.class) ||
              methods[i].getReturnType().equals(Integer.class) ||
              methods[i].getReturnType().equals(Long.class) ||
              methods[i].getReturnType().equals(Double.class) ||
              methods[i].getReturnType().equals(Float.class) ||
              methods[i].getReturnType().equals(Short.class) ||
              methods[i].getReturnType().equals(Boolean.class))
        ) {
          attributeNames[count] = methods[i].getName().substring(3);
          if (attributeNames[count].length()>1)
            attributeNames[count] = attributeNames[count].substring(0,1).toLowerCase()+attributeNames[count].substring(1);
          colType = methods[i].getReturnType();
          if (colType.equals(String.class))
            colProperties[count] = new TextColumn();
          else if (colType.equals(Integer.class) || colType.equals(Long.class))
            colProperties[count] = new IntegerColumn();
          else if (colType.equals(BigDecimal.class) || colType.equals(Double.class) || colType.equals(Float.class))
            colProperties[count] = new DecimalColumn();
          else if (colType.equals(Boolean.class))
            colProperties[count] = new CheckBoxColumn();
          else if (colType.equals(Date.class))
            colProperties[count] = new DateColumn();
          else if (colType.equals(java.sql.Date.class))
            colProperties[count] = new DateColumn();
          else if (colType.equals(Timestamp.class))
            colProperties[count] = new DateColumn();

          colProperties[count].setColumnName(attributeNames[count]);
          if (colProperties[count].getHeaderColumnName().equals("columnname"))
            colProperties[count].setHeaderColumnName(String.valueOf(attributeNames[count].charAt(0)).toUpperCase()+attributeNames[count].substring(1));
          colProperties[count].setColumnVisible(this.allColumnVisible);
          colProperties[count].setPreferredWidth(this.allColumnPreferredWidth);
          count++;
        }
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
      this.lookupVO = null;
    }
    catch (Error er) {
      er.printStackTrace();
      this.lookupVO = null;
    }
  }


  /**
   * @return this flag is used to set the code selection window; three values are allowed: GRID_FRAME, TREE_FRAME and TREE_GRID_FRAME
   */
  public final int getCodeSelectionWindow() {
    return codeSelectionWindow;
  }


  /**
   * Set the code selection window.
   * @param codeSelectionWindow three values are allowed: GRID_FRAME, TREE_FRAME and TREE_GRID_FRAME
   */
  public final void setCodeSelectionWindow(int codeSelectionWindow) {
    this.codeSelectionWindow = codeSelectionWindow;
  }


  /**
   * @user can select only leaves (by double clicking)
   */
  public final boolean isAllowTreeLeafSelectionOnly() {
    return allowTreeLeafSelectionOnly;
  }


  /**
   * This is used when codeSelectionWindow is set to TREE_FRAME: it means that user can select only leaves (by double clicking)
   * @param allowTreeLeafSelectionOnly user can select only leaves (by double clicking)
   */
  public final void setAllowTreeLeafSelectionOnly(boolean allowTreeLeafSelectionOnly) {
    this.allowTreeLeafSelectionOnly = allowTreeLeafSelectionOnly;
  }


  /**
   * @return lookup value object
   */
  public final ValueObject getLookupVO() {
    return lookupVO;
  }


  /**
   * @return define what to do in case of invalid code on validation task; possible values: ON_INVALID_CODE_xxx; default value: ClientSettings.ON_INVALID_CODE
   */
  public final int getOnInvalidCode() {
    return onInvalidCode;
  }


  /**
   * Define what to do in case of invalid code on validation task: clear code on input field or restore last invalid code in the input field or restore focus in the input field
   * @param onInvalidCode define what to do in case of invalid code on validation task; possible values: ON_INVALID_CODE_xxx
   */
  public final void setOnInvalidCode(int onInvalidCode) {
    this.onInvalidCode = onInvalidCode;
  }


  /**
   * @return used by Form.save to check if lookup control is in a valid state
   */
  public final boolean isCodeValid() {
    return codeValid;
  }


}


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Grid Frame.</p>
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
class LookupGridFrame extends JDialog {

  /** lookup grid */
  private Grids table = null;


  public LookupGridFrame(JFrame parentFrame,String title,Grids table) {
    super(parentFrame,ClientSettings.getInstance().getResources().getResource(title),true);
    this.table = table;
    getContentPane().setLayout(new BorderLayout());
    JPanel p = new JPanel();
    getContentPane().add(p,BorderLayout.CENTER);
    p.setLayout(new GridBagLayout());
    p.setBorder(BorderFactory.createLineBorder(ClientSettings.GRID_FOCUS_BORDER,2));
    p.add(table,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

  }


  /**
   * @return lookup grid
   */
  public final Grids getTable() {
    return table;
  }
}





/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Tree Frame.</p>
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
class LookupTreeFrame extends JDialog {

  /** lookup tree */
  private TreePanel treePanel = null;


  public LookupTreeFrame(JFrame parentFrame,String title,TreePanel treePanel) {
    super(parentFrame,ClientSettings.getInstance().getResources().getResource(title),true);
    this.treePanel = treePanel;
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JScrollPane(treePanel),BorderLayout.CENTER);
  }

}




/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Tree+Grid Frame.</p>
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
class LookupTreeGridFrame extends JDialog {

  /** filter tree */
  private TreePanel treePanel = null;

  /** lookup grid */
  private Grids table = null;


  public LookupTreeGridFrame(JFrame parentFrame,String title,TreePanel treePanel,Grids table) {
    super(parentFrame,ClientSettings.getInstance().getResources().getResource(title),true);
    this.treePanel = treePanel;
    this.table = table;
    getContentPane().setLayout(new BorderLayout());
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    getContentPane().add(split,BorderLayout.CENTER);
    split.add(new JScrollPane(treePanel),split.LEFT);
//    split.add(new JScrollPane(table),split.RIGHT);
    split.add(table,split.RIGHT);
    split.setDividerLocation(200);
  }


  /**
   * @return lookup grid
   */
  public final Grids getTable() {
    return table;
  }


}
