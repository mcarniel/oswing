package demo10;

import org.openswing.swing.client.*;
import java.util.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.form.model.client.VOModel;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.util.java.Consts;
import java.sql.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Detail frame controller for the employee</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class EmpDetailFrameController extends FormController {

  private EmpDetailFrame frame = null;
  private Connection conn = null;
  private String pk = null;
  private EmpGridFrame gridFrame = null;


  public EmpDetailFrameController(EmpGridFrame gridFrame,String pk,Connection conn) {
    this.gridFrame = gridFrame;
    this.pk = pk;
    this.conn = conn;
    frame = new EmpDetailFrame(conn,this);
    MDIFrame.add(frame);

    if (pk!=null) {
      frame.getMainPanel().setMode(Consts.READONLY);
      frame.getMainPanel().reload();
    }
    else {
      frame.getMainPanel().setMode(Consts.INSERT);
    }

  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    try {
      // since this method could be invoked also when selecting another row on the linked grid,
      // the pk attribute must be recalculated from the grid...
      int row = gridFrame.getGrid().getSelectedRow();
      if (row!=-1) {
        GridEmpVO gridVO = (GridEmpVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
        pk = gridVO.getEmpCode();
      }

      String sql =
        "select EMP.EMP_CODE,EMP.FIRST_NAME, EMP.LAST_NAME,EMP.DEPT_CODE,DEPT.DESCRIPTION,EMP.TASK_CODE,TASKS.DESCRIPTION,EMP.SEX,EMP.HIRE_DATE,EMP.SALARY,EMP.NOTE "+
        "from EMP,DEPT,TASKS where EMP.DEPT_CODE=DEPT.DEPT_CODE and EMP.TASK_CODE=TASKS.TASK_CODE and EMP.EMP_CODE='"+pk+"'";

      // mapping between attributes and database fields...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("empCode","EMP.EMP_CODE");
      attribute2dbField.put("firstName","EMP.FIRST_NAME");
      attribute2dbField.put("lastName","EMP.LAST_NAME");
      attribute2dbField.put("deptCode","EMP.DEPT_CODE");
      attribute2dbField.put("deptDescription","DEPT.DESCRIPTION");
      attribute2dbField.put("taskCode","EMP.TASK_CODE");
      attribute2dbField.put("taskDescription","TASKS.DESCRIPTION");
      attribute2dbField.put("sex","EMP.SEX");
      attribute2dbField.put("hireDate","EMP.HIRE_DATE");
      attribute2dbField.put("salary","EMP.SALARY");
      attribute2dbField.put("note","EMP.NOTE");

      return QueryUtil.getQuery(
        conn,
        sql,
        new ArrayList(), // list of values linked to "?" parameters in sql
        attribute2dbField,
        EmpVO.class, // v.o. to dinamically create for each row...
        "Y",
        "N",
        true // log query...
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }

/*
    // an alternative way: you can define your own business logic to retrieve data and adding filtering/sorting conditions at hand...
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
        "select EMP.EMP_CODE,EMP.FIRST_NAME, EMP.LAST_NAME,EMP.DEPT_CODE,DEPT.DESCRIPTION,EMP.TASK_CODE,TASKS.DESCRIPTION,EMP.SEX,EMP.HIRE_DATE,EMP.SALARY,EMP.NOTE "+
        "from EMP,DEPT,TASKS where EMP.DEPT_CODE=DEPT.DEPT_CODE and EMP.TASK_CODE=TASKS.TASK_CODE and EMP.EMP_CODE='"+pk+"'"
      );
      if (rset.next()) {
        EmpVO vo = new EmpVO();
        vo.setEmpCode(rset.getString(1));
        vo.setFirstName(rset.getString(2));
        vo.setLastName(rset.getString(3));
        vo.setDeptCode(rset.getString(4));
        vo.setDeptDescription(rset.getString(5));
        vo.setTaskCode(rset.getString(6));
        vo.setTaskDescription(rset.getString(7));
        vo.setSex(rset.getString(8));
        vo.setHireDate(rset.getDate(9));
        vo.setSalary(rset.getBigDecimal(10));
        vo.setNote(rset.getString(11));
        return new VOResponse(vo);
      }
      else
        return new ErrorResponse("No data found.");
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (SQLException ex1) {
      }
    }
*/
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.getControlCurrency().setCurrencySymbol("$");
    frame.getControlCurrency().setDecimals(2);
    frame.getControlCurrency().setDecimalSymbol('.');
    frame.getControlCurrency().setGroupingSymbol(',');

    EmpVO vo = (EmpVO)frame.getMainPanel().getVOModel().getValueObject();
    frame.getGrid().getOtherGridParams().put("empCode",vo.getEmpCode());
    frame.getGrid().reloadData();
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    // mapping between attributes and database fields...
    Map attribute2dbField = new HashMap();
    attribute2dbField.put("empCode","EMP_CODE");
    attribute2dbField.put("firstName","FIRST_NAME");
    attribute2dbField.put("lastName","LAST_NAME");
    attribute2dbField.put("deptCode","DEPT_CODE");
    attribute2dbField.put("taskCode","TASK_CODE");
    attribute2dbField.put("sex","SEX");
    attribute2dbField.put("hireDate","HIRE_DATE");
    attribute2dbField.put("salary","SALARY");
    attribute2dbField.put("note","NOTE");

    Response res = QueryUtil.insertTable(conn,newPersistentObject,"EMP",attribute2dbField,"Y","N",true);
    if (res.isError()) {
      conn.rollback();
      return res;
    }
    else {
      // insert 7 records in WORKING_DAYS, one for each day of week...
      PreparedStatement pstmt = null;
      try {
        pstmt = conn.prepareStatement("insert into WORKING_DAYS(EMP_CODE,DAY) values(?,?)");

        pstmt.setString(1,((EmpVO)newPersistentObject).getEmpCode());
        pstmt.setInt(2,Calendar.SUNDAY);
        pstmt.execute();

        pstmt.setString(1,((EmpVO)newPersistentObject).getEmpCode());
        pstmt.setInt(2,Calendar.MONDAY);
        pstmt.execute();

        pstmt.setString(1,((EmpVO)newPersistentObject).getEmpCode());
        pstmt.setInt(2,Calendar.TUESDAY);
        pstmt.execute();

        pstmt.setString(1,((EmpVO)newPersistentObject).getEmpCode());
        pstmt.setInt(2,Calendar.WEDNESDAY);
        pstmt.execute();

        pstmt.setString(1,((EmpVO)newPersistentObject).getEmpCode());
        pstmt.setInt(2,Calendar.THURSDAY);
        pstmt.execute();

        pstmt.setString(1,((EmpVO)newPersistentObject).getEmpCode());
        pstmt.setInt(2,Calendar.FRIDAY);
        pstmt.execute();

        pstmt.setString(1,((EmpVO)newPersistentObject).getEmpCode());
        pstmt.setInt(2,Calendar.SATURDAY);
        pstmt.execute();

        conn.commit();

        frame.getGrid().getOtherGridParams().put("empCode",((EmpVO)newPersistentObject).getEmpCode());
        frame.getGrid().reloadData();

        return res;
      }
      catch (Exception ex) {
        conn.rollback();
        return new ErrorResponse(ex.getMessage());
      }
      finally {
        try {
          pstmt.close();
        }
        catch (Exception ex1) {
        }
      }

    }

/*
    // an alternative way: you can define your own business logic to store data at hand...
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into EMP(EMP_CODE,FIRST_NAME,LAST_NAME,DEPT_CODE,TASK_CODE,SEX,HIRE_DATE,SALARY,NOTE) values(?,?,?,?,?,?,?,?,?)");
      EmpVO vo = (EmpVO)newPersistentObject;
      stmt.setString(1,vo.getEmpCode());
      stmt.setString(2,vo.getFirstName());
      stmt.setString(3,vo.getLastName());
      stmt.setString(4,vo.getDeptCode());
      stmt.setString(5,vo.getTaskCode());
      stmt.setString(6,vo.getSex());
      stmt.setDate(7,vo.getHireDate());
      stmt.setBigDecimal(8,vo.getSalary());
      stmt.setString(9,vo.getNote());

      stmt.execute();
      pk = vo.getEmpCode();
      gridFrame.reloadData();
      return new VOResponse(vo);
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
*/
  }

  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    // mapping between attributes and database fields...
    Map attribute2dbField = new HashMap();
    attribute2dbField.put("empCode","EMP_CODE");
    attribute2dbField.put("firstName","FIRST_NAME");
    attribute2dbField.put("lastName","LAST_NAME");
    attribute2dbField.put("deptCode","DEPT_CODE");
    attribute2dbField.put("taskCode","TASK_CODE");
    attribute2dbField.put("sex","SEX");
    attribute2dbField.put("hireDate","HIRE_DATE");
    attribute2dbField.put("salary","SALARY");
    attribute2dbField.put("note","NOTE");

    HashSet pk = new HashSet();
    pk.add("empCode");

    Response res = QueryUtil.updateTable(conn,pk,oldPersistentObject,persistentObject,"EMP",attribute2dbField,"Y","N",true);
    if (res.isError())
      conn.rollback();
    else
      conn.commit();
    return res;

/*
    // an alternative way: you can define your own business logic to store data at hand...
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update EMP set EMP_CODE=?,FIRST_NAME=?,LAST_NAME=?,DEPT_CODE=?,TASK_CODE=?,SEX=?,HIRE_DATE=?,SALARY=?,NOTE=? where EMP_CODE=?");
      EmpVO vo = (EmpVO)persistentObject;
      stmt.setString(1,vo.getEmpCode());
      stmt.setString(2,vo.getFirstName());
      stmt.setString(3,vo.getLastName());
      stmt.setString(4,vo.getDeptCode());
      stmt.setString(5,vo.getTaskCode());
      stmt.setString(6,vo.getSex());
      stmt.setDate(7,vo.getHireDate());
      stmt.setBigDecimal(8,vo.getSalary());
      stmt.setString(9,vo.getNote());
      stmt.setString(10,vo.getEmpCode());
      stmt.execute();
      gridFrame.reloadData();
      return new VOResponse(vo);
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
*/
  }

  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      EmpVO vo = (EmpVO)persistentObject;

      // delete from WORKING_DAYS...
      stmt = conn.prepareStatement("delete from WORKING_DAYS where EMP_CODE=?");
      stmt.setString(1,vo.getEmpCode());
      stmt.execute();
      stmt.close();

      // delete from EMP...
      stmt = conn.prepareStatement("delete from EMP where EMP_CODE=?");
      stmt.setString(1,vo.getEmpCode());
      stmt.execute();
      gridFrame.reloadData();

      frame.getGrid().clearData();

      return new VOResponse(vo);
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


  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.INSERT) {
      frame.getGrid().clearData();
      frame.setEnableGridButtons(false);
    }
    else {
      frame.setEnableGridButtons(true);
    }

  }


  public EmpGridFrame getGridFrame() {
    return gridFrame;
  }




}
