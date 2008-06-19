package demo42;

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

public class EmpDetailPanelController extends FormController {

  private Connection conn = null;
  private GridEmpVO gridVO = null;
  private EmpDetailPanel panel = null;


  public EmpDetailPanelController(GridEmpVO gridVO,Connection conn) {
    this.gridVO = gridVO;
    this.conn = conn;
    panel = new EmpDetailPanel(conn);
    panel.getMainPanel().setFormController(this);
    panel.getMainPanel().setMode(Consts.READONLY);
    panel.getMainPanel().reload();
  }


  public EmpDetailPanel getPanel() {
    return panel;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    try {
      String sql =
        "select EMP.EMP_CODE,EMP.FIRST_NAME, EMP.LAST_NAME,EMP.DEPT_CODE,DEPT.DESCRIPTION,EMP.TASK_CODE,TASKS.DESCRIPTION,EMP.SEX,EMP.HIRE_DATE,EMP.SALARY,EMP.NOTE "+
        "from EMP,DEPT,TASKS where EMP.DEPT_CODE=DEPT.DEPT_CODE and EMP.TASK_CODE=TASKS.TASK_CODE and EMP.EMP_CODE='"+gridVO.getEmpCode()+"'";

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
    panel.getControlCurrency().setCurrencySymbol("$");
    panel.getControlCurrency().setDecimals(2);
    panel.getControlCurrency().setDecimalSymbol('.');
    panel.getControlCurrency().setGroupingSymbol(',');

    EmpVO vo = (EmpVO)panel.getMainPanel().getVOModel().getValueObject();
    panel.getGrid().getOtherGridParams().put("empCode",vo.getEmpCode());
    panel.getGrid().reloadData();
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
      gridpanel.reloadData();
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
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.INSERT) {
      panel.getGrid().clearData();
      panel.setEnableGridButtons(false);
    }
    else {
      panel.setEnableGridButtons(true);
    }

  }




}
