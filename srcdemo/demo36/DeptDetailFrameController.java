package demo36;

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
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.Persistent;
import org.apache.cayenne.DataObject;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Detail frame controller for the department.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class DeptDetailFrameController extends FormController {

  private DeptDetailFrame frame = null;
  private DataContext context = null;
  private String pk = null;
  private DeptGridFrame gridFrame = null;


  public DeptDetailFrameController(DeptGridFrame gridFrame,String pk,DataContext context) {
    this.gridFrame = gridFrame;
    this.pk = pk;
    this.context = context;
    frame = new DeptDetailFrame(this);
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
        DeptVO gridVO = (DeptVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
        pk = gridVO.getDeptCode();
      }


      Expression qualifier2 = ExpressionFactory.likeIgnoreCaseExp(
                      DeptVO.DEPT_CODE_PROPERTY,
                      pk);
      SelectQuery select2 = new SelectQuery(DeptVO.class, qualifier2);
      List list = context.performQuery(select2);
      DeptVO vo = (DeptVO)list.get(0);

      return new VOResponse(vo);
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
      ((DeptVO)newPersistentObject).setStatus("Y");
      context.registerNewObject((DataObject)newPersistentObject);
      context.commitChanges();
      return new VOResponse(newPersistentObject);
    }
    catch (Exception ex) {
      try {
        context.rollbackChanges();
      }
      catch (Exception ex1) {
      }
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
      context.commitChanges();
      return new VOResponse(persistentObject);
    }
    catch (Exception ex) {
      try {
        context.rollbackChanges();
      }
      catch (Exception ex1) {
      }
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    try {
      context.deleteObject((Persistent)persistentObject);
      context.commitChanges();
      return new VOResponse(new Boolean(true));
    }
    catch (Exception ex) {
      try {
        context.rollbackChanges();
      }
      catch (Exception ex1) {
      }
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  public DeptGridFrame getGridFrame() {
    return gridFrame;
  }




}
