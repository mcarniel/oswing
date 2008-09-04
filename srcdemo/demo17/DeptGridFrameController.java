package demo17;

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
import org.hibernate.ScrollableResults;
import org.openswing.swing.util.client.ClientSettings;
import org.hibernate.metadata.ClassMetadata;
import org.openswing.swing.util.server.HibernateUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;


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
  private SessionFactory sessions = null;

  public DeptGridFrameController(SessionFactory sessions) {
    this.sessions = sessions;
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
//      String baseSQL = "from demo17.DeptVO as DeptVO where status='E'";
      Session sess = sessions.openSession(); // obtain a JDBC connection and instantiate a new Session

//      READ WHOLE RESULT-SET...
//      Response res = HibernateUtils.getAllFromQuery(
//        filteredColumns,
//        currentSortedColumns,
//        currentSortedVersusColumns,
//        valueObjectType,
//        baseSQL,
//        new Object[0],
//        new Type[0],
//        "DeptVO",
//        sessions,
//        sess
//      );
//      sess.close();
//      return res;
//      END READ WHOLE RESULT-SET...


//    READ A BLOCK OF DATA FROM RESULT-SET...

      Response res = HibernateUtils.getBlockFromClass(
        valueObjectType,
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        action,
        startIndex,
        50, // block size...
        FetchMode.JOIN,
        sess
      );

/*
      Response res = HibernateUtils.getBlockFromQuery(
        action,
        startIndex,
        50, // block size...
        filteredColumns,
        currentSortedColumns,
        currentSortedVersusColumns,
        valueObjectType,
        baseSQL,
        new Object[0],
        new Type[0],
        "DeptVO",
        sessions,
        sess
      );
*/

      sess.close();
      return res;

//    END READ A BLOCK OF DATA FROM RESULT-SET...
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
    new DeptDetailFrameController(grid,vo.getDeptCode(),sessions);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    try {
      DeptVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (DeptVO)persistentObjects.get(i);
        vo.setStatus("D");

        Session session = sessions.getCurrentSession();
        session.beginTransaction();
        session.update(vo);
        session.flush();
        session.connection().commit();

//        Session sess = sessions.openSession(); // obtain a JDBC connection and instantiate a new Session
//        sess.update(vo);
//        sess.flush();
//        sess.connection().commit();
//        sess.close();
      }
      return new VOResponse(new Boolean(true));
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }
  public SessionFactory getSessions() {
    return sessions;
  }



}
