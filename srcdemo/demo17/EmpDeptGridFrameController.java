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


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for departments.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpDeptGridFrameController extends GridController implements GridDataLocator {

  private EmpDeptGridFrame grid = null;
  private SessionFactory sessions = null;

  public EmpDeptGridFrameController(SessionFactory sessions) {
    this.sessions = sessions;
    grid = new EmpDeptGridFrame(this);
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
      String baseSQL = "select emp.empCode as empCode,dept.deptCode as deptCode,dept.description as description from demo17.EmpVO emp inner join emp.dept as dept";
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


      HashMap map = new HashMap();
      map.put("deptCode","dept.deptCode");
      map.put("description","dept.description");
      map.put("empCode","emp.empCode");

//    READ A BLOCK OF DATA FROM RESULT-SET...
      Response res = HibernateUtils.getBlockFromQuery(
        map,
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
      sess.close();
      return res;
//    END READ A BLOCK OF DATA FROM RESULT-SET...
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }




}
