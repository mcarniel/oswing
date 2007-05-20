package demo19;

import org.openswing.swing.client.*;
import java.util.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.internationalization.java.EnglishOnlyResourceFactory;
import org.openswing.swing.form.model.client.VOModel;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import com.ibatis.sqlmap.client.SqlMapClient;


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
  private String pk = null;
  private EmpGridFrame gridFrame = null;
  private SqlMapClient sqlMap = null;


  public EmpDetailFrameController(EmpGridFrame gridFrame,String pk,SqlMapClient sqlMap) {
    this.gridFrame = gridFrame;
    this.pk = pk;
    this.sqlMap = sqlMap;
    frame = new EmpDetailFrame(this,sqlMap);
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
      EmpVO vo = (EmpVO)sqlMap.queryForObject("getEmp",pk);
      return new VOResponse(vo);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
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
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    try {
      sqlMap.startTransaction();
      EmpVO vo = (EmpVO)newPersistentObject;
      sqlMap.insert("insertEmp",vo);

      sqlMap.commitTransaction();
      gridFrame.reloadData();
      pk = vo.getEmpCode();
      return new VOResponse(vo);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      sqlMap.endTransaction();
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
      sqlMap.startTransaction();
      EmpVO oldVO = (EmpVO)oldPersistentObject;
      EmpVO newVO = (EmpVO)persistentObject;
      int row = sqlMap.update("updateEmp",new EmpVOs(oldVO,newVO));
      sqlMap.commitTransaction();

      if (row==0) {
        return new ErrorResponse("Updating not performed: the record was previously updated.");
      }

      gridFrame.reloadData();
      return new VOResponse(newVO);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      sqlMap.endTransaction();
    }
  }

  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    try {
      sqlMap.startTransaction();
      EmpVO vo = (EmpVO)persistentObject;
      sqlMap.delete("deleteEmp",vo.getEmpCode());

      sqlMap.commitTransaction();
      gridFrame.reloadData();
      return new VOResponse(Boolean.TRUE);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      sqlMap.endTransaction();
    }
  }





}
