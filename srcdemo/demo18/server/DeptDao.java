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
import demo18.java.DeptVO;
import java.util.HashSet;
import org.openswing.swing.message.send.java.LookupValidationParams;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: DAO object related to depts.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class DeptDao {

  private DataSource dataSource;

  private Map attribute2dbField = new HashMap();

  private HashSet pkAttributes = new HashSet();


  public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
  }


  public DeptDao() {
   attribute2dbField.put("deptCode","DEPT.DEPT_CODE");
   attribute2dbField.put("description","DEPT.DESCRIPTION");
   attribute2dbField.put("address","DEPT.ADDRESS");
   attribute2dbField.put("status","DEPT.STATUS");
   pkAttributes.add("deptCode");
  }



  public Response getDeptsList(GridParams gridParams) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      String baseSQL = "select DEPT.DEPT_CODE,DEPT.DESCRIPTION,DEPT.ADDRESS,DEPT.ADDRESS,DEPT.STATUS from DEPT where DEPT.STATUS='E'";
      return QueryUtil.getQuery(conn,new UserSessionParameters(),baseSQL,new ArrayList(),attribute2dbField,DeptVO.class,"Y","N",null,gridParams,50,false);
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


  public Response validateDept(LookupValidationParams lookupParams) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      String baseSQL = "select DEPT.DEPT_CODE,DEPT.DESCRIPTION,DEPT.ADDRESS,DEPT.ADDRESS,DEPT.STATUS  from DEPT where DEPT.STATUS='E' and DEPT.DEPT_CODE=?";
      ArrayList values = new ArrayList();
      values.add(lookupParams.getCode());
      return QueryUtil.getQuery(conn,new UserSessionParameters(),baseSQL,values,attribute2dbField,DeptVO.class,"Y","N",null,new GridParams(),50,false);
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



  public Response insertDepts(ArrayList persistentObjects) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      for(int i=0;i<persistentObjects.size();i++)
        ((DeptVO)persistentObjects.get(i)).setStatus("E");
      Response res = QueryUtil.insertTable(conn,new UserSessionParameters(),persistentObjects,"DEPT",attribute2dbField,"Y","N",null,false);
      if (res.isError())
        throw new RuntimeException(res.getErrorMessage());
//        conn.rollback();
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


  public Response updateDepts(ArrayList oldPersistentObjects,ArrayList newPersistentObjects) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      DeptVO oldVO = null;
      DeptVO newVO = null;
      Response res = null;
      for(int i=0;i<oldPersistentObjects.size();i++) {
        oldVO = (DeptVO)oldPersistentObjects.get(i);
        newVO = (DeptVO)newPersistentObjects.get(i);
        res = QueryUtil.updateTable(
          conn,
          new UserSessionParameters(),
          pkAttributes,
          oldVO,
          newVO,
          "DEPT",
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


  public Response deleteDepts(ArrayList persistentObjects) throws DataAccessException {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dataSource.getConnection();
      pstmt = conn.prepareStatement("update DEPT set STATUS='D' where DEPT_CODE=? ");
      DeptVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (DeptVO)persistentObjects.get(i);
        pstmt.setString(1,vo.getDeptCode());
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
