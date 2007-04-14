package demo2;

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
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery("select DEMO2.TEXT,DEMO2.DECNUM,DEMO2.CURRNUM,DEMO2.THEDATE,DEMO2.COMBO,DEMO2.CHECK,DEMO2.RADIO,DEMO2.CODE,DEMO2_LOOKUP.DESCRCODE,DEMO2.TA from DEMO2,DEMO2_LOOKUP where TEXT='"+pk+"' and DEMO2.CODE=DEMO2_LOOKUP.CODE");
      if (rset.next()) {
        TestVO vo = new TestVO();
        vo.setCheckValue(rset.getObject(6)==null || !rset.getObject(6).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
        vo.setComboValue(rset.getString(5));
        vo.setCurrencyValue(rset.getBigDecimal(3));
        vo.setDateValue(rset.getDate(4));
        vo.setNumericValue(rset.getBigDecimal(2));
        vo.setRadioButtonValue(rset.getObject(7)==null || !rset.getObject(7).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
        vo.setStringValue(rset.getString(1));
        vo.setLookupValue(rset.getString(8));
        vo.setDescrLookupValue(rset.getString(9));
        vo.setTaValue(rset.getString(10));
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

  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.getControlCurrency().setCurrencySymbol("€");
    frame.getControlCurrency().setDecimals(2);
    frame.getControlCurrency().setDecimalSymbol('.');
    frame.getControlCurrency().setGroupingSymbol(',');
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into DEMO2(TEXT,DECNUM,CURRNUM,THEDATE,COMBO,CHECK,RADIO,CODE,TA) values(?,?,?,?,?,?,?,?,?)");
      TestVO vo = (TestVO)newPersistentObject;
      stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
      stmt.setString(5,vo.getComboValue());
      stmt.setBigDecimal(3,vo.getCurrencyValue());
      stmt.setDate(4,vo.getDateValue());
      stmt.setBigDecimal(2,vo.getNumericValue());
      stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
      stmt.setString(1,vo.getStringValue());
      stmt.setString(8,vo.getLookupValue());
      stmt.setString(9,vo.getTaValue());
      stmt.execute();
      pk = vo.getStringValue();
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

  }

  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update DEMO2 set TEXT=?,DECNUM=?,CURRNUM=?,THEDATE=?,COMBO=?,CHECK=?,RADIO=?,CODE=?,TA=? where TEXT=?");
      TestVO vo = (TestVO)persistentObject;
      stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
      stmt.setString(5,vo.getComboValue());
      stmt.setBigDecimal(3,vo.getCurrencyValue());
      stmt.setDate(4,vo.getDateValue());
      stmt.setBigDecimal(2,vo.getNumericValue());
      stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
      stmt.setString(1,vo.getStringValue());
      stmt.setString(8,vo.getLookupValue());
      stmt.setString(9,vo.getTaValue());
      stmt.setString(10,vo.getStringValue());
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
  }

  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from DEMO2 where TEXT=?");
      TestVO vo = (TestVO)persistentObject;
      stmt.setString(1,vo.getStringValue());
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
  }





}