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
import demo18.java.EmpVO;
import demo18.java.GridEmpVO;
import java.util.HashSet;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: DAO object related to Emps.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class EmpDao {

  private DataSource dataSource;

  private Map attribute2dbField = new HashMap();

  private Map insUpdAttribute2dbField = new HashMap();

  private HashSet pkAttributes = new HashSet();


  public EmpDao() {
   attribute2dbField.put("empCode","EMP.EMP_CODE");
   attribute2dbField.put("firstName","EMP.FIRST_NAME");
   attribute2dbField.put("lastName","EMP.LAST_NAME");
   attribute2dbField.put("deptCode","EMP.DEPT_CODE");
   attribute2dbField.put("deptDescription","DEPT.DESCRIPTION");

   attribute2dbField.put("sex","EMP.SEX");
   attribute2dbField.put("salary","EMP.SALARY");
   attribute2dbField.put("hireDate","EMP.HIRE_DATE");
   attribute2dbField.put("note","EMP.NOTE");
   attribute2dbField.put("taskCode","EMP.TASK_CODE");
   attribute2dbField.put("taskDescription","TASKS.DESCRIPTION");

   insUpdAttribute2dbField.put("empCode","EMP.EMP_CODE");
   insUpdAttribute2dbField.put("firstName","EMP.FIRST_NAME");
   insUpdAttribute2dbField.put("lastName","EMP.LAST_NAME");
   insUpdAttribute2dbField.put("deptCode","EMP.DEPT_CODE");
   insUpdAttribute2dbField.put("sex","EMP.SEX");
   insUpdAttribute2dbField.put("salary","EMP.SALARY");
   insUpdAttribute2dbField.put("hireDate","EMP.HIRE_DATE");
   insUpdAttribute2dbField.put("note","EMP.NOTE");
   insUpdAttribute2dbField.put("taskCode","EMP.TASK_CODE");

   pkAttributes.add("empCode");
  }


  public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
  }




  public Response getEmpsList(GridParams gridParams) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      String baseSQL = "select EMP.EMP_CODE,EMP.FIRST_NAME, EMP.LAST_NAME,EMP.DEPT_CODE,DEPT.DESCRIPTION from EMP,DEPT where EMP.DEPT_CODE=DEPT.DEPT_CODE";
      return QueryUtil.getQuery(conn,new UserSessionParameters(),baseSQL,new ArrayList(),attribute2dbField,GridEmpVO.class,"Y","N",null,gridParams,50,false);
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


  public Response getEmp(Object pk) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      String baseSQL =
          "select EMP.EMP_CODE,EMP.FIRST_NAME, EMP.LAST_NAME,EMP.DEPT_CODE,"+
          "DEPT.DESCRIPTION,EMP.SEX,EMP.SALARY,EMP.HIRE_DATE,EMP.NOTE,EMP.TASK_CODE,TASKS.DESCRIPTION "+
          " from EMP,DEPT,TASKS where EMP.DEPT_CODE=DEPT.DEPT_CODE and EMP.TASK_CODE=TASKS.TASK_CODE ";
      return QueryUtil.getQuery(conn,new UserSessionParameters(),baseSQL,new ArrayList(),attribute2dbField,EmpVO.class,"Y","N",null,false);
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


  public Response insertEmp(EmpVO vo) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      Response res = QueryUtil.insertTable(
        conn,
        new UserSessionParameters(),
        vo,
        "EMP",
        insUpdAttribute2dbField,
        "Y",
        "N",
        null,
        false
      );
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


  public Response updateEmp(EmpVO oldVO,EmpVO newVO) throws DataAccessException {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      Response res = null;
      res = QueryUtil.updateTable(
        conn,
        new UserSessionParameters(),
        pkAttributes,
        oldVO,
        newVO,
        "EMP",
        insUpdAttribute2dbField,
        "Y",
        "N",
        null,
        false
      );
      if (res.isError()) {
//        conn.rollback();
//        return res;
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


  public Response deleteEmps(ArrayList persistentObjects) throws DataAccessException {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = dataSource.getConnection();
      pstmt = conn.prepareStatement("delete from EMP where EMP_CODE=? ");
      GridEmpVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (GridEmpVO)persistentObjects.get(i);
        pstmt.setString(1,vo.getEmpCode());
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
