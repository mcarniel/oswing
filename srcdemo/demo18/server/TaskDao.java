package demo18.server;

import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.openswing.swing.message.receive.java.*;
import org.springframework.dao.DataAccessException;
import org.openswing.swing.message.send.java.GridParams;
import java.util.ArrayList;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.*;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import java.util.Map;
import java.util.HashMap;
import demo18.java.TaskVO;
import java.util.HashSet;
import org.openswing.swing.message.send.java.LookupValidationParams;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: DAO object related to tasks.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class TaskDao {

  private DataSource dataSource;

  private Map attribute2dbField = new HashMap();

  private HashSet pkAttributes = new HashSet();


  public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
  }


  public TaskDao() {
   attribute2dbField.put("taskCode","TASKS.TASK_CODE");
   attribute2dbField.put("description","TASKS.DESCRIPTION");
   attribute2dbField.put("status","TASKS.STATUS");
   pkAttributes.add("taskCode");
  }



  public Response getTasksList(GridParams gridParams) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      String baseSQL = "select TASKS.TASK_CODE,TASKS.DESCRIPTION,TASKS.STATUS  from TASKS where TASKS.STATUS='E'";
      return QueryUtil.getQuery(conn,new UserSessionParameters(),baseSQL,new ArrayList(),attribute2dbField,TaskVO.class,"Y","N",null,gridParams,50,false);
    }
    catch (Exception ex) {
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  public Response validateTask(LookupValidationParams lookupParams) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      String baseSQL = "select TASKS.TASK_CODE,TASKS.DESCRIPTION,TASKS.STATUS from TASKS where TASKS.STATUS='E' and TASKS.TASK_CODE=? ";
      ArrayList values = new ArrayList();
      values.add(lookupParams.getCode());
      return QueryUtil.getQuery(conn,new UserSessionParameters(),baseSQL,values,attribute2dbField,TaskVO.class,"Y","N",null,new GridParams(),50,false);

    }
    catch (Exception ex) {
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  public Response insertTasks(ArrayList persistentObjects) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      for(int i=0;i<persistentObjects.size();i++)
        ((TaskVO)persistentObjects.get(i)).setStatus("E");
      Response res = QueryUtil.insertTable(conn,new UserSessionParameters(),persistentObjects,"TASKS",attribute2dbField,"Y","N",null,false);
      if (res.isError()) {
//        conn.rollback();
        throw new RuntimeException(res.getErrorMessage());
      }
      return res;
    }
    catch (Exception ex) {
//      try {
//        conn.rollback();
//      }
//      catch (Exception ex2) {
//      }
//      return new ErrorResponse(ex.getMessage());
      throw new RuntimeException(ex.getMessage());
    }
    finally {
      try {
        conn.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  public Response updateTasks(ArrayList oldPersistentObjects,ArrayList newPersistentObjects) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      TaskVO oldVO = null;
      TaskVO newVO = null;
      Response res = null;
      for(int i=0;i<oldPersistentObjects.size();i++) {
        oldVO = (TaskVO)oldPersistentObjects.get(i);
        newVO = (TaskVO)newPersistentObjects.get(i);
        res = QueryUtil.updateTable(
          conn,
          new UserSessionParameters(),
          pkAttributes,
          oldVO,
          newVO,
          "TASKS",
          attribute2dbField,
          "Y",
          "N",
          null,
          false
        );
        if (res.isError()) {
//          conn.rollback();
//          return res;
          throw new RuntimeException(res.getErrorMessage());
        }
      }
      return new VOListResponse(newPersistentObjects,false,newPersistentObjects.size());
    }
    catch (Exception ex) {
//      try {
//        conn.rollback();
//      }
//      catch (Exception ex2) {
//      }
//      return new ErrorResponse(ex.getMessage());
      throw new RuntimeException(ex.getMessage());
    }
    finally {
      try {
        conn.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  public Response deleteTasks(ArrayList persistentObjects) throws DataAccessException {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dataSource.getConnection();
      pstmt = conn.prepareStatement("update TASKS set STATUS='D' where TASK_CODE=? ");
      TaskVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (TaskVO)persistentObjects.get(i);
        pstmt.setString(1,vo.getTaskCode());
        pstmt.execute();
      }
      return new VOResponse(Boolean.TRUE);
    }
    catch (Exception ex) {
//      try {
//        conn.rollback();
//      }
//      catch (Exception ex2) {
//      }
//      return new ErrorResponse(ex.getMessage());
      throw new RuntimeException(ex.getMessage());
    }
    finally {
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        conn.close();
      }
      catch (Exception ex1) {
      }
    }
  }



}
