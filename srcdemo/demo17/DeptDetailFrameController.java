package demo17;

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
import org.hibernate.Session;
import org.openswing.swing.util.server.HibernateUtils;
import org.hibernate.SessionFactory;


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
  private SessionFactory sessions = null;
  private String pk = null;
  private DeptGridFrame gridFrame = null;


  public DeptDetailFrameController(DeptGridFrame gridFrame,String pk,SessionFactory sessions) {
    this.gridFrame = gridFrame;
    this.pk = pk;
    this.sessions = sessions;
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
    Session sess = null;
    try {
      // since this method could be invoked also when selecting another row on the linked grid,
      // the pk attribute must be recalculated from the grid...
      int row = gridFrame.getGrid().getSelectedRow();
      if (row!=-1) {
        DeptVO gridVO = (DeptVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
        pk = gridVO.getDeptCode();
      }

      String baseSQL = "from Depts in class demo17.DeptVO where Depts.deptCode='"+pk+"'";
      sess = sessions.openSession(); // obtain a JDBC connection and instantiate a new Session

      DeptVO vo = (DeptVO)sess.createQuery(baseSQL).uniqueResult();
      return new VOResponse(vo);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        sess.close();
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
    Session session = null;
    try {
      DeptVO vo = (DeptVO)newPersistentObject;
      vo.setStatus("E");

      session = sessions.getCurrentSession();
      session.beginTransaction();
      session.save(vo);
      session.flush();
      session.getTransaction().commit();

//      Session sess = sessions.openSession(); // obtain a JDBC connection and instantiate a new Session
//      sess.save(vo);
//      sess.flush();
//      sess.connection().commit();
//      sess.close();
      return new VOResponse(vo);
    }
    catch (Exception ex) {
      session.getTransaction().rollback();
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        session.close();
      }
      catch (Exception ex1) {
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
    Session session = null;
    try {
      session = sessions.getCurrentSession();
      session.beginTransaction();
      session.update(persistentObject);
      session.flush();
      session.connection().commit();

//        Session sess = sessions.openSession(); // obtain a JDBC connection and instantiate a new Session
//        sess.update(vo);
//        sess.flush();
//        sess.connection().commit();
//        sess.close();
      return new VOResponse(persistentObject);
    }
    catch (SQLException ex) {
      session.getTransaction().rollback();
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        session.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    try {
      DeptVO vo = (DeptVO)persistentObject;
      vo.setStatus("D");

      Session session = sessions.getCurrentSession();
      session.beginTransaction();
      session.update(vo);
      session.flush();
      session.connection().commit();

//        Session sess = sessions.openSession(); // obtain a JDBC connection and instantiate a new Session
//        sess.update(vo);
//        sess.flush();
//        sess.connection().commit();
//        sess.close();
      return new VOResponse(new Boolean(true));
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  public DeptGridFrame getGridFrame() {
    return gridFrame;
  }
  public SessionFactory getSessions() {
    return sessions;
  }




}
