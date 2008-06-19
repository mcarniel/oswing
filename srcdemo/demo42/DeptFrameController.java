package demo42;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for departments.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DeptFrameController extends GridController implements GridDataLocator {

  private DeptGridFrame grid = null;
  private Connection conn = null;

  public DeptFrameController(Connection conn) {
    this.conn = conn;
    grid = new DeptGridFrame(conn,this);
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    DeptVO vo = (DeptVO)valueObject;
    vo.setStatus("E");
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
      String sql = "select DEPT.DEPT_CODE,DEPT.DESCRIPTION,DEPT.ADDRESS,DEPT.STATUS from DEPT where DEPT.STATUS='E'";

      // mapping between attributes and database fields...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("deptCode","DEPT.DEPT_CODE");
      attribute2dbField.put("description","DEPT.DESCRIPTION");
      attribute2dbField.put("address","DEPT.ADDRESS");
      attribute2dbField.put("status","DEPT.STATUS");

      return QueryUtil.getQuery(
        conn,
        sql,
        new ArrayList(), // list of values linked to "?" parameters in sql
        attribute2dbField,
        DeptVO.class, // v.o. to dinamically create for each row...
        "Y",
        "N",
        new GridParams(
          action,
          startIndex,
          filteredColumns,
          currentSortedColumns,
          currentSortedVersusColumns,
          new HashMap() // other params...
        ),
        50, // pagination size...
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
      String sql = "select DEPT.DEPT_CODE,DEPT.DESCRIPTION,DEPT.ADDRESS from DEPT where DEPT.STATUS='E'";

      Vector vals = new Vector();
      if (filteredColumns.size()>0) {
        FilterWhereClause[] filter = (FilterWhereClause[])filteredColumns.get("deptCode");
        sql += " and DEPT.DEPT_CODE "+ filter[0].getOperator()+"?";
        vals.add(filter[0].getValue());
        if (filter[1]!=null) {
          sql += " and DEPT.DEPT_CODE "+ filter[1].getOperator()+"?";
          vals.add(filter[1].getValue());
        }
      }
      if (currentSortedColumns.size()>0) {
        sql += " ORDER BY DEPT.DEPT_CODE "+currentSortedVersusColumns.get(0);
      }

      stmt = conn.prepareStatement(sql);
      for(int i=0;i<vals.size();i++)
        stmt.setObject(i+1,vals.get(i));

      ResultSet rset = stmt.executeQuery();


      ArrayList list = new ArrayList();
      DeptVO vo = null;
      while (rset.next()) {
        vo = new DeptVO();
        vo.setDeptCode(rset.getString(1));
        vo.setDescription(rset.getString(2));
        vo.setAddress(rset.getString(3));
        list.add(vo);
      }
      return new VOListResponse(list,false,list.size());
    }
    catch (Exception ex) {
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
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    // mapping between attributes and database fields...
    Map attribute2dbField = new HashMap();
    attribute2dbField.put("deptCode","DEPT_CODE");
    attribute2dbField.put("description","DESCRIPTION");
    attribute2dbField.put("address","ADDRESS");
    attribute2dbField.put("status","STATUS");

    Response res = QueryUtil.insertTable(conn,newValueObjects,"DEPT",attribute2dbField,"Y","N",true);
    if (res.isError())
      conn.rollback();
    else
      conn.commit();
    return res;

/*
    // an alternative way: you can define your own business logic to store data at hand...
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into DEPT(DEPT_CODE,DESCRIPTION,ADDRESS,STATUS) values(?,?,?,?)");
      DeptVO vo = null;
      for(int i=0;i<newValueObjects.size();i++) {
        vo = (DeptVO)newValueObjects.get(i);
        stmt.setString(1,vo.getDeptCode());
        stmt.setString(2,vo.getDescription());
        stmt.setString(3,vo.getAddress());
        stmt.setString(4,"E");
        stmt.execute();
      }
      return new VOListResponse(newValueObjects,false,newValueObjects.size());
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
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    // mapping between attributes and database fields...
    Map attribute2dbField = new HashMap();
    attribute2dbField.put("deptCode","DEPT_CODE");
    attribute2dbField.put("description","DESCRIPTION");
    attribute2dbField.put("address","ADDRESS");
    attribute2dbField.put("status","STATUS");

    HashSet pk = new HashSet();
    pk.add("deptCode");

    Response res = null;
    DeptVO oldVO = null;
    DeptVO newVO = null;
    for(int i=0;i<persistentObjects.size();i++) {
      oldVO = (DeptVO)oldPersistentObjects.get(i);
      newVO = (DeptVO)persistentObjects.get(i);
      res = QueryUtil.updateTable(conn,pk,oldVO,newVO,"DEPT",attribute2dbField,"Y","N",true);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
    }
    conn.commit();
    return new VOListResponse(persistentObjects,false,persistentObjects.size());

/*
    // an alternative way: you can define your own business logic to store data at hand...
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update DEPT set DEPT_CODE=?,DESCRIPTION=?,ADDRESS=? where DEPT_CODE=?");
      DeptVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (DeptVO)persistentObjects.get(i);
        stmt.setString(1,vo.getDeptCode());
        stmt.setString(2,vo.getDescription());
        stmt.setString(3,vo.getAddress());
        stmt.setString(4,vo.getDeptCode());
        stmt.execute();
      }
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
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
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update DEPT set STATUS='D' where DEPT_CODE=?");
      DeptVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (DeptVO)persistentObjects.get(i);
        stmt.setString(1,vo.getDeptCode());
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



}
