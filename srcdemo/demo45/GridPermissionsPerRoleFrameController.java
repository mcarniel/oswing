package demo45;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for user roles.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridPermissionsPerRoleFrameController extends GridController implements GridDataLocator {

  private GridPermissionsPerRoleFrame frame = null;
  private Connection conn = null;


  public GridPermissionsPerRoleFrameController(Connection conn) {
    this.conn = conn;
    frame = new GridPermissionsPerRoleFrame(conn,this);
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    GridPermissionsPerRoleVO vo = (GridPermissionsPerRoleVO)grid.getVOListTableModel().getObjectForRow(row);
    if (attributeName.equals("editableInEdit") && !vo.isDefaultEditableInEdit())
      return false;
    if (attributeName.equals("editableInIns") && !vo.isDefaultEditableInIns())
      return false;
    if (attributeName.equals("required") && vo.isDefaultRequired())
      return false;
    return grid.isFieldEditable(row,attributeName);
  }


  /**
   * Callback method invoked to load data on the grid.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param filteredColumns filtered columns
   * @param currentSortedColumns sorted columns
   * @param currentSortedVersusColumns ordering versus of sorted columns
   * @param valueObjectType v.o. type
   * @param otherGridParams other grid parameters
   * @return response from the server: an object of type VOListResponse if data loading was successfully completed, or an ErrorResponse onject if some error occours
   */
  public Response loadData(
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Class valueObjectType,
      Map otherGridParams) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      String sql = "select GRID_PERMISSIONS_DEFS.COLS_POS,GRID_PERMISSIONS_DEFS.EDIT_COLS_IN_INS,GRID_PERMISSIONS_DEFS.EDIT_COLS_IN_EDIT,GRID_PERMISSIONS_DEFS.REQUIRED_COLS,GRID_PERMISSIONS_DEFS.COLS_VIS from GRID_PERMISSIONS_DEFS where GRID_PERMISSIONS_DEFS.FUNCTION_ID=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,frame.getFunctionId());
      rset = pstmt.executeQuery();
      ArrayList rows = new ArrayList();
      String[] colsPos = null;
      String[] editColsInIns = null;
      String[] editColsInEdit = null;
      String[] colsReq = null;
      String[] colsVis = null;
      Hashtable cols = new Hashtable(); // collection of pairs: <attribute name,GridPermissionsPerRoleVO object>
      GridPermissionsPerRoleVO vo = null;
      while(rset.next()) {
        colsPos = rset.getString(1).split(",");
        editColsInIns = rset.getString(2).split(",");
        editColsInEdit = rset.getString(3).split(",");
        colsReq = rset.getString(4).split(",");
        colsVis = rset.getString(5).split(",");
        for(int i=0;i<colsPos.length;i++) {
          vo = new GridPermissionsPerRoleVO();
          vo.setColumnName(colsPos[i]);
          vo.setDescription(ClientSettings.getInstance().getResources().getResource(colsPos[i]));
          vo.setDefaultEditableInEdit(editColsInEdit[i].equals("true"));
          vo.setDefaultEditableInIns(editColsInIns[i].equals("true"));
          vo.setDefaultRequired(colsReq[i].equals("true"));
          vo.setDefaultVisible(colsVis[i].equals("true"));
          vo.setEditableInEdit(vo.isDefaultEditableInEdit());
          vo.setEditableInIns(vo.isDefaultEditableInIns());
          vo.setRequired(vo.isDefaultRequired());
          vo.setVisible(vo.isDefaultVisible());
          rows.add(vo);
          cols.put(vo.getColumnName(),vo);
        }
      }
      if (rows.size()==0)
        return new VOListResponse(new ArrayList(),false,0);

      rset.close();
      pstmt.close();
      sql = "select GRID_PERMISSIONS.COLS_POS,GRID_PERMISSIONS.EDIT_COLS_IN_INS,GRID_PERMISSIONS.EDIT_COLS_IN_EDIT,GRID_PERMISSIONS.REQUIRED_COLS,GRID_PERMISSIONS.COLS_VIS from GRID_PERMISSIONS where GRID_PERMISSIONS.FUNCTION_ID=? and GRID_PERMISSIONS.ROLE_ID=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,frame.getFunctionId());
      pstmt.setString(2,frame.getRoleId());
      rset = pstmt.executeQuery();
      while(rset.next()) {
        colsPos = rset.getString(1).split(",");
        editColsInIns = rset.getString(2).split(",");
        editColsInEdit = rset.getString(3).split(",");
        colsReq = rset.getString(4).split(",");
        colsVis = rset.getString(5).split(",");

        for(int i=0;i<colsPos.length;i++) {
          vo.setColumnName(colsPos[i]);
          vo = (GridPermissionsPerRoleVO)cols.get(vo.getColumnName());
          vo.setDescription(ClientSettings.getInstance().getResources().getResource(colsPos[i]));
          vo.setEditableInEdit(editColsInEdit[i].equals("true"));
          vo.setEditableInIns(editColsInIns[i].equals("true"));
          vo.setRequired(colsReq[i].equals("true"));
          vo.setVisible(colsVis[i].equals("true"));
        }
      }
      return new VOListResponse(rows,false,rows.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        rset.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from  GRID_PERMISSIONS where FUNCTION_ID=? and ROLE_ID=?");
      stmt.setString(1,frame.getFunctionId());
      stmt.setString(2,frame.getRoleId());
      stmt.execute();
      stmt.close();
      stmt = conn.prepareStatement("insert into GRID_PERMISSIONS(COLS_POS,EDIT_COLS_IN_INS,EDIT_COLS_IN_EDIT,REQUIRED_COLS,COLS_VIS,FUNCTION_ID,ROLE_ID) values(?,?,?,?,?,?,?)");
      GridPermissionsPerRoleVO vo = null;
      String colsPos = "";
      String editColsInIns = "";
      String editColsInEdit = "";
      String colsReq = "";
      String colsVis = "";
      for(int i=0;i<frame.getGrid().getVOListTableModel().getRowCount();i++) {
        vo = (GridPermissionsPerRoleVO)frame.getGrid().getVOListTableModel().getObjectForRow(i);
        colsPos += vo.getColumnName()+",";
        editColsInIns += (vo.isEditableInIns()?"true":"false")+",";
        editColsInEdit += (vo.isEditableInEdit()?"true":"false")+",";
        colsReq += (vo.isRequired()?"true":"false")+",";
        colsVis += (vo.isVisible()?"true":"false")+",";
      }
      colsPos = colsPos.substring(0,colsPos.length()-1);
      editColsInIns = editColsInIns.substring(0,editColsInIns.length()-1);
      editColsInEdit = editColsInEdit.substring(0,editColsInEdit.length()-1);
      colsReq = colsReq.substring(0,colsReq.length()-1);
      colsVis = colsVis.substring(0,colsVis.length()-1);

      stmt.setString(1,colsPos);
      stmt.setString(2,editColsInIns);
      stmt.setString(3,editColsInEdit);
      stmt.setString(4,colsReq);
      stmt.setString(5,colsVis);
      stmt.setString(6,frame.getFunctionId());
      stmt.setString(7,frame.getRoleId());
      stmt.execute();
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
        conn.commit();
      }
      catch (SQLException ex1) {
      }
    }
  }



}
