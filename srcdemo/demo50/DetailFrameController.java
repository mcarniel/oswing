package demo50;

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
import org.openswing.swing.domains.java.Domain;
import java.math.BigDecimal;
import org.openswing.swing.server.QueryUtil;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DetailFrameController extends FormController {

  private DetailFrame frame = null;
  private Connection conn = null;
  private String pk = null;

  public DetailFrameController(Connection conn) {
    this.pk = pk;
    this.conn = conn;
    frame = new DetailFrame(conn,this);
    frame.getMainPanel().setMode(Consts.INSERT);
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    try {
      HashMap map = new HashMap();
      map.put("stringValue","DEMO50.CODE");
      map.put("file","DEMO50.MYFILE");
      map.put("fileDescription","DEMO50.FILE_DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(pk);
      return QueryUtil.getQuery(
        conn,
        "select DEMO50.CODE,DEMO50.MYFILE,DEMO50.FILE_DESCRIPTION from DEMO50 where CODE=?",
        values,
        map,
        DetailTestVO.class,
        "Y",
        "N",
        true
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }




  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    try {
      HashMap map = new HashMap();
      map.put("stringValue","CODE");
      map.put("file","MYFILE");
      map.put("fileDescription","FILE_DESCRIPTION");
      Response res = QueryUtil.insertTable(conn,newPersistentObject,"DEMO50",map,"Y","N",true);
      if (res.isError())
        conn.rollback();
      else {
        pk = ((DetailTestVO)newPersistentObject).getStringValue();
        conn.commit();
      }
      return res;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    try {
      HashSet pkAttrs = new HashSet();
      pkAttrs.add("stringValue");
      HashMap map = new HashMap();
      map.put("stringValue","CODE");
      map.put("file","MYFILE");
      map.put("fileDescription","FILE_DESCRIPTION");
      Response res = QueryUtil.updateTable(conn,pkAttrs,oldPersistentObject,persistentObject,"DEMO50",map,"Y","N",true);
      if (res.isError())
        conn.rollback();
      else
        conn.commit();
      return res;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement("delete from DEMO50 where CODE=?");
      pstmt.execute();
      conn.commit();
      return new VOResponse(Boolean.TRUE);
    }
    catch (Exception ex) {
      try {
        conn.rollback();
      }
      catch (Exception ex1) {
      }
      ex.printStackTrace();
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
