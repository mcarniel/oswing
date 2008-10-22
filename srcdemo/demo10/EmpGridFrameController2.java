package demo10;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import java.awt.Color;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.export.java.ExportOptions;
import org.openswing.swing.export.java.GridExportOptions;
import org.openswing.swing.export.java.GridExportCallbacks;
import org.openswing.swing.export.java.ComponentExportOptions;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for employees.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpGridFrameController2 extends GridController implements GridDataLocator {

  private EmpGridFrame2 frame = null;
  private Connection conn = null;


  public EmpGridFrameController2(Connection conn) {
    this.conn = conn;
    frame = new EmpGridFrame2(conn,this);
    MDIFrame.add(frame);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    EmpVO newPersistentObject = null;
    for(int i=0;i<rowNumbers.length;i++) {
      newPersistentObject = (EmpVO)newValueObjects.get(i);

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
    }
    try {
      conn.commit();
    }
    catch (Exception ex2) {
    }
    return new VOListResponse(newValueObjects,false,newValueObjects.size());
  }

  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    EmpVO oldPersistentObject,persistentObject;
    for(int i=0;i<rowNumbers.length;i++) {
      oldPersistentObject = (EmpVO)oldPersistentObjects.get(i);
      persistentObject = (EmpVO)persistentObjects.get(i);

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
      if (res.isError()) {
        conn.rollback();
        return res;
      }
    }
    try {
      conn.commit();
    }
    catch (Exception ex2) {
    }
    return new VOListResponse(persistentObjects,false,persistentObjects.size());
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
      String sql =
        "select EMP.EMP_CODE,EMP.FIRST_NAME, EMP.LAST_NAME,EMP.DEPT_CODE,DEPT.DESCRIPTION,EMP.TASK_CODE,TASKS.DESCRIPTION,EMP.SEX,EMP.HIRE_DATE,EMP.SALARY,EMP.NOTE "+
        "from EMP,DEPT,TASKS where EMP.DEPT_CODE=DEPT.DEPT_CODE and EMP.TASK_CODE=TASKS.TASK_CODE";

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
        new GridParams(
          action,
          startIndex,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          otherGridParams
        ),
        true // log query...
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }

/*
    // an alternative way: you can define your own business logic to retrieve data and adding filtering/sorting conditions at hand...
    PreparedStatement stmt = null;
    try {
      String sql = "select EMP.EMP_CODE,EMP.FIRST_NAME, EMP.LAST_NAME,EMP.DEPT_CODE,DEPT.DESCRIPTION from EMP,DEPT where EMP.DEPT_CODE=DEPT.DEPT_CODE";
      Vector vals = new Vector();
      if (filteredColumns.size()>0) {
        FilterWhereClause[] filter = (FilterWhereClause[])filteredColumns.get("deptCode");
        sql += " and EMP.DEPT_CODE "+ filter[0].getOperator()+"?";
        vals.add(filter[0].getValue());
        if (filter[1]!=null) {
          sql += " and EMP.DEPT_CODE "+ filter[1].getOperator()+"?";
          vals.add(filter[1].getValue());
        }
      }
      if (currentSortedColumns.size()>0) {
        sql += " ORDER BY EMP.FIRST_NAME "+currentSortedVersusColumns.get(0);
      }

      stmt = conn.prepareStatement(sql);
      for(int i=0;i<vals.size();i++)
        stmt.setObject(i+1,vals.get(i));

      ResultSet rset = stmt.executeQuery();


      ArrayList list = new ArrayList();
      GridEmpVO vo = null;
      while (rset.next()) {
        vo = new GridEmpVO();
        vo.setEmpCode(rset.getString(1));
        vo.setFirstName(rset.getString(2));
        vo.setLastName(rset.getString(3));
        vo.setDeptCode(rset.getString(4));
        vo.setDeptDescription(rset.getString(5));
        list.add(vo);
      }
      return new VOListResponse(list,false,list.size());
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
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from EMP where EMP_CODE=?");
      GridEmpVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (GridEmpVO)persistentObjects.get(i);
        stmt.setString(1,vo.getEmpCode());
        stmt.execute();
      }
      return new VOResponse(new Boolean(true));
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
   * Callback method invoked when the user has selected another row.
   * @param rowNumber selected row index
   */
  public void rowChanged(int rowNumber) {
    if (rowNumber==-1)
      return;
    EmpVO vo = (EmpVO)frame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
    frame.getWDGrid().getOtherGridParams().put("empCode",vo.getEmpCode());
    frame.getWDGrid().reloadData();
  }


  /**
   * Callback method invoked after saving data when the grid was in INSERT mode (on pressing save button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterInsertGrid(GridControl grid) {
    if (frame.getGrid().getSelectedRow()==-1)
      return;
    EmpVO vo = (EmpVO)frame.getGrid().getVOListTableModel().getObjectForRow(frame.getGrid().getSelectedRow());
    frame.getWDGrid().getOtherGridParams().put("empCode",vo.getEmpCode());
  }


  /**
   * Callback method invoked after deleting data when the grid was in READONLY mode (on pressing delete button).
   * The method is called ONLY if the operation is successfully completed.
   */
  public void afterDeleteGrid() {
     frame.getWDGrid().clearData();
  }


  /**
   * Callback method invoked each time the grid mode is changed.
   * @param currentMode current grid mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.INSERT) {
      frame.getWDGrid().clearData();
      frame.setEnableGridButtons(false);
    }
    else {
      frame.setEnableGridButtons(true);
    }

  }


  /**
   * Callback method invoked by grid before showing exporting dialog;
   * this method can be overrided to redefine document formats allowed for the grid
   * @return list of available formats; possible values: ExportOptions.XLS_FORMAT, ExportOptions.CSV_FORMAT1, ExportOptions.CSV_FORMAT2, ExportOptions.XML_FORMAT, ExportOptions.XML_FORMAT_FAT, ExportOptions.HTML_FORMAT, ExportOptions.PDF_FORMAT, ExportOptions.RTF_FORMAT; default value: ClientSettings.EXPORTING_FORMATS
   */
  public String[] getExportingFormats() {
    return new String[]{ ExportOptions.PDF_FORMAT,ExportOptions.XLS_FORMAT,ExportOptions.HTML_FORMAT,ExportOptions.CSV_FORMAT1,ExportOptions.XML_FORMAT };
  }


  /**
   * Callback method invoked by grid when exporting data from grid.
   * @param exportOptions options used to export data; these options can be programmatically changed, in order to customize exporting result
   */
  public void exportGrid(ExportOptions exportOptions) {
  //    exportOptions.getComponentsExportOptions().add(frame.getWDGrid().getDefaultGridExportOptions());

    GridExportOptions empsOpts = (GridExportOptions)exportOptions.getComponentsExportOptions().get(0);
    final GridExportOptions wdOpts = frame.getWDGrid().getDefaultGridExportOptions();
    GridExportCallbacks callbacks = new GridExportCallbacks() {

      /**
       * @param vo value object related to current exported row
       * @param row row index just exported
       * @return method invoked after adding a row to export document; if not null, then specified ComponentExportOptions will be added after current row
       */
      public ComponentExportOptions getComponentPerRow(ValueObject vo,int row) {
        EmpVO empVO = (EmpVO)vo;
        wdOpts.getOtherGridParams().put("empCode",empVO.getEmpCode());
        return wdOpts;
      }

    };
    empsOpts.setCallbacks(callbacks);
  }


}
