package demo36;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientSettings;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SelectQuery;
import org.openswing.swing.util.server.CayenneUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for departments.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DeptGridFrameController extends GridController implements GridDataLocator {

  private DeptGridFrame grid = null;
  private DataContext context = null;

  public DeptGridFrameController(DataContext context) {
    this.context = context;
    grid = new DeptGridFrame(this);
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


      SelectQuery select = new SelectQuery(DeptVO.class);
      List list = context.performQuery(select);

      return CayenneUtils.getAllFromQuery(
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          valueObjectType,
          new SelectQuery(DeptVO.class),
          new HashMap(),
          context
      );
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
    DeptVO vo = (DeptVO)persistentObject;
    new DeptDetailFrameController(grid,vo.getDeptCode(),context);
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    try {
      context.deleteObjects(persistentObjects);
      context.commitChanges();
      return new VOResponse(new Boolean(true));
    }
    catch (Exception ex) {
      try {
        context.rollbackChanges();
      }
      catch (Exception ex1) {
      }
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  public DataContext getContext() {
    return context;
  }


}
