package demo26;

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
import org.openswing.swing.server.UserSessionParameters;


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
  private GridFrame gridFrame = null;

  public DetailFrameController(GridFrame gridFrame,String pk,Connection conn) {
    this.gridFrame = gridFrame;
    this.pk = pk;
    this.conn = conn;
    frame = new DetailFrame(conn,this);

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
        TestVO gridVO = (TestVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
        pk = gridVO.getCustomerCode();
      }

      ArrayList vals = new ArrayList();
      vals.add(pk);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("customerCode","CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("name","CUSTOMERS.NAME");
      attribute2dbField.put("surname","CUSTOMERS.SURNAME");
      attribute2dbField.put("city","CUSTOMERS.CITY");
      attribute2dbField.put("zipCode","CUSTOMERS.ZIP_CODE");
      attribute2dbField.put("state","CUSTOMERS.STATE");
      attribute2dbField.put("address","CUSTOMERS.ADDRESS");

      return QueryUtil.getQuery(
        conn,
        new UserSessionParameters(),
        "select CUSTOMERS.CUSTOMER_CODE,CUSTOMERS.NAME,CUSTOMERS.SURNAME,CUSTOMERS.CITY,CUSTOMERS.ZIP_CODE,CUSTOMERS.STATE,CUSTOMERS.ADDRESS from CUSTOMERS where CUSTOMER_CODE=?",
        vals,
        attribute2dbField,
        DetailTestVO.class,
        "Y",
        "N",
        null,
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
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("customerCode","CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("name","CUSTOMERS.NAME");
      attribute2dbField.put("surname","CUSTOMERS.SURNAME");
      attribute2dbField.put("city","CUSTOMERS.CITY");
      attribute2dbField.put("zipCode","CUSTOMERS.ZIP_CODE");
      attribute2dbField.put("state","CUSTOMERS.STATE");
      attribute2dbField.put("address","CUSTOMERS.ADDRESS");

      return QueryUtil.insertTable(
        conn,
        new UserSessionParameters(),
        newPersistentObject,
        "CUSTOMERS",
        attribute2dbField,
        "Y",
        "N",
        null,
        true
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.commit();
      }
      catch (SQLException ex1) {
      }
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
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("customerCode","CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("name","CUSTOMERS.NAME");
      attribute2dbField.put("surname","CUSTOMERS.SURNAME");
      attribute2dbField.put("city","CUSTOMERS.CITY");
      attribute2dbField.put("zipCode","CUSTOMERS.ZIP_CODE");
      attribute2dbField.put("state","CUSTOMERS.STATE");
      attribute2dbField.put("address","CUSTOMERS.ADDRESS");

      HashSet pks = new HashSet();
      pks.add("customerCode");

      return QueryUtil.updateTable(
        conn,
        new UserSessionParameters(),
        pks,
        oldPersistentObject,
        persistentObject,
        "CUSTOMERS",
        attribute2dbField,
        "Y",
        "N",
        null,
        true
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        conn.commit();
      }
      catch (SQLException ex1) {
      }
    }
  }

  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from DEMO26 where CUSTOMER_CODE=?");
      DetailTestVO vo = (DetailTestVO)persistentObject;
      stmt.setString(1,vo.getCustomerCode());
      stmt.execute();

// this instruction is no more needed: the grid has been linked to the Form (see Form.linkGrid method...)
//      gridFrame.reloadData();
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
  public GridFrame getGridFrame() {
    return gridFrame;
  }
  public String getPk() {
    return pk;
  }
  public void setPk(String pk) {
    this.pk = pk;
  }


  /**
   * Callback method invoked each time an input control is edited: this method define if the new value is valid.
   * Default behaviour: input control value is valid.
   * @param attributeName attribute name related to the input control currently edited
   * @param oldValue old input control value (before editing)
   * @param newValue new input control value (just edited)
   * @return <code>true</code> if input control value is valid, <code>false</code> otherwise
   */
  public boolean validateControl(String attributeName,Object oldValue,Object newValue) {
    if (attributeName.equals("numericValue") &&
        newValue!=null &&
        ((BigDecimal)newValue).doubleValue()==0) {
      // zero value not allowed...
      return false;
    }
    return true;
  }




}
