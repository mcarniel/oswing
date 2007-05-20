package demo19;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import demo18.java.*;
import com.ibatis.sqlmap.client.SqlMapClient;
import org.openswing.swing.util.server.IBatisUtils;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.server.IBatisParamsWrapper;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for employees.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpGridFrameController extends GridController implements GridDataLocator {

  private EmpGridFrame grid = null;
  private SqlMapClient sqlMap = null;

  public EmpGridFrameController(SqlMapClient sqlMap) {
    this.sqlMap = sqlMap;
    grid = new EmpGridFrame(this,sqlMap);
    MDIFrame.add(grid);
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
    try {

      IBatisParamsWrapper gridParams = new IBatisParamsWrapper(
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns
      );

      List emps = sqlMap.queryForList("getEmps",gridParams);
      ArrayList list = new ArrayList();
      list.addAll(emps);
      return new VOListResponse(list,false,list.size());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    GridEmpVO vo = (GridEmpVO)persistentObject;
    new EmpDetailFrameController(grid,vo.getEmpCode(),sqlMap);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    try {
      sqlMap.startTransaction();
      GridEmpVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (GridEmpVO)persistentObjects.get(i);
        sqlMap.delete("deleteEmp",vo.getEmpCode());
      }

      sqlMap.commitTransaction();
      return new VOResponse(Boolean.TRUE);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      sqlMap.endTransaction();
    }
  }



}
