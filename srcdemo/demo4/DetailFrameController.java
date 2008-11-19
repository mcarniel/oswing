package demo4;

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
    Statement stmt = null;
    try {
      // since this method could be invoked also when selecting another row on the linked grid,
      // the pk attribute must be recalculated from the grid...
      int row = gridFrame.getGrid().getSelectedRow();
      if (row!=-1) {
        TestVO gridVO = (TestVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
        pk = gridVO.getStringValue();
      }

      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery("select DEMO4.TEXT,DEMO4.DECNUM,DEMO4.CURRNUM,DEMO4.THEDATE,DEMO4.COMBO,DEMO4.CHECK_BOX,DEMO4.RADIO,DEMO4.CODE,DEMO4_LOOKUP.DESCRCODE,DEMO4.TA,DEMO4.FORMATTED_TEXT,DEMO4.URI,DEMO4.LINK_LABEL from DEMO4,DEMO4_LOOKUP where TEXT='"+pk+"' and DEMO4.CODE=DEMO4_LOOKUP.CODE");
      if (rset.next()) {
        DetailTestVO vo = new DetailTestVO();
        vo.setCheckValue(rset.getObject(6)==null || !rset.getObject(6).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
        vo.setCombo(new ComboVO());
        vo.getCombo().setCode(rset.getString(5));

        // this is a simplification: in a real situation combo v.o. will be retrieved from the database...
        Domain d = ClientSettings.getInstance().getDomain("ORDERSTATE");
        if (vo.getCombo().getCode().equals("O"))
          vo.getCombo().setDescription("opened");
        else if (vo.getCombo().getCode().equals("S"))
          vo.getCombo().setDescription("sospended");
        else if (vo.getCombo().getCode().equals("D"))
          vo.getCombo().setDescription("delivered");
        else if (vo.getCombo().getCode().equals("C"))
          vo.getCombo().setDescription("closed");

        vo.setCurrencyValue(rset.getBigDecimal(3));
        vo.setDateValue(rset.getDate(4));
        vo.setNumericValue(rset.getBigDecimal(2));
        vo.setRadioButtonValue(rset.getObject(7)==null || !rset.getObject(7).equals("Y") ? Boolean.FALSE:Boolean.TRUE);
        vo.setStringValue(rset.getString(1));
        vo.setLookupValue(rset.getString(8));
        vo.setDescrLookupValue(rset.getString(9));
        vo.setTaValue(rset.getString(10));
        vo.setFormattedTextValue(rset.getString(11));
        vo.setUri(rset.getString(12));
        vo.setLinkLabel(rset.getString(13));
        vo.setTooltipURI(vo.getUri());

        stmt.close();
        stmt = conn.createStatement();
        rset = stmt.executeQuery("select DEMO4_LIST_VALUES.CODE from DEMO4_LIST_VALUES where TEXT='"+pk+"'");
        ArrayList codes = new ArrayList();
        while(rset.next()) {
          codes.add(rset.getString(1));
        }
        vo.setListValues(codes);

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
      catch (Exception ex1) {
      }
    }

  }




  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into DEMO4(TEXT,DECNUM,CURRNUM,THEDATE,COMBO,CHECK_BOX,RADIO,CODE,TA,FORMATTED_TEXT,URI,LINK_LABEL) values(?,?,?,?,?,?,?,?,?,?,?,?)");
      DetailTestVO vo = (DetailTestVO)newPersistentObject;
      stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
      stmt.setString(5,vo.getCombo().getCode());
      stmt.setBigDecimal(3,vo.getCurrencyValue());
      stmt.setDate(4,vo.getDateValue());
      stmt.setBigDecimal(2,vo.getNumericValue());
      stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
      stmt.setString(1,vo.getStringValue());
      stmt.setString(8,vo.getLookupValue());
      stmt.setString(9,vo.getTaValue());
      stmt.setString(10,vo.getFormattedTextValue());
      stmt.setString(11,vo.getUri());
      stmt.setString(12,vo.getLinkLabel());
      stmt.execute();
      pk = vo.getStringValue();

      if (vo.getListValues()!=null) {
        stmt.close();
        stmt = conn.prepareStatement("insert into DEMO4_LIST_VALUES(TEXT,CODE) values(?,?)");

        for(int i=0;i<vo.getListValues().size();i++) {
          stmt.setString(1,pk);
          stmt.setString(2,vo.getListValues().get(i).toString());
          stmt.execute();
        }
      }

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

  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update DEMO4 set TEXT=?,DECNUM=?,CURRNUM=?,THEDATE=?,COMBO=?,CHECK_BOX=?,RADIO=?,CODE=?,TA=?,FORMATTED_TEXT=?,URI=?,LINK_LABEL=? where TEXT=?");
      DetailTestVO vo = (DetailTestVO)persistentObject;
      DetailTestVO oldVO = (DetailTestVO)oldPersistentObject;
      stmt.setObject(6,vo.getCheckValue()==null || !vo.getCheckValue().booleanValue() ? "N":"Y");
      stmt.setString(5,vo.getCombo().getCode());
      stmt.setBigDecimal(3,vo.getCurrencyValue());
      stmt.setDate(4,vo.getDateValue());
      stmt.setBigDecimal(2,vo.getNumericValue());
      stmt.setObject(7,vo.getRadioButtonValue()==null || !vo.getRadioButtonValue().booleanValue() ? "N":"Y");
      stmt.setString(1,vo.getStringValue());
      stmt.setString(8,vo.getLookupValue());
      stmt.setString(9,vo.getTaValue());
      stmt.setString(10,vo.getFormattedTextValue());
      stmt.setString(11,vo.getUri());
      stmt.setString(12,vo.getLinkLabel());
      stmt.setString(13,oldVO.getStringValue());
      stmt.execute();

      stmt.close();
      stmt = conn.prepareStatement("delete from DEMO4_LIST_VALUES where TEXT=?");
      stmt.setString(1,pk);
      stmt.execute();

      if (vo.getListValues()!=null) {
        stmt.close();
        stmt = conn.prepareStatement("insert into DEMO4_LIST_VALUES(TEXT,CODE) values(?,?)");

        for(int i=0;i<vo.getListValues().size();i++) {
          stmt.setString(1,pk);
          stmt.setString(2,vo.getListValues().get(i).toString());
          stmt.execute();
        }
      }


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

  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from DEMO4 where TEXT=?");
      DetailTestVO vo = (DetailTestVO)persistentObject;
      stmt.setString(1,vo.getStringValue());
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
