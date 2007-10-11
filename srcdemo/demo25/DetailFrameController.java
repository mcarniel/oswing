package demo25;

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
    // retrieve the detail v.o. from grid model instead of retriving it from the db...
    return new VOResponse(
      gridFrame.getGrid().getVOListTableModel().getObjectForRow(gridFrame.getGrid().getSelectedRow())
    );
  }




  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into DEMO25(CITY,STATE,POPULATION) values(?,?,?)");
      TestVO vo = (TestVO)newPersistentObject;
      stmt.setString(1,vo.getCity());
      stmt.setString(2,vo.getState());
      stmt.setBigDecimal(3,vo.getPopulation());
      stmt.execute();
      pk = vo.getCity();

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
      stmt = conn.prepareStatement("update DEMO25 set STATE=?,POPULATION=? where CITY=? and STATE=? and POPULATION=?");
      TestVO vo = (TestVO)persistentObject;
      TestVO oldVO = (TestVO)oldPersistentObject;
      stmt.setString(1,vo.getState());
      stmt.setBigDecimal(2,vo.getPopulation());
      stmt.setString(3,vo.getCity());
      stmt.setString(4,oldVO.getState());
      stmt.setBigDecimal(5,oldVO.getPopulation());
      int rows = stmt.executeUpdate();

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
      stmt = conn.prepareStatement("delete from DEMO25 where CITY=?");
      TestVO vo = (TestVO)persistentObject;
      stmt.setString(1,vo.getCity());
      stmt.execute();
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



}
