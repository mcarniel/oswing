package demo35;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.server.JPAUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridFrameController extends GridController implements GridDataLocator {

  private GridFrame grid = null;
  private EntityManagerFactory emf = null;

  public GridFrameController(EntityManagerFactory emf) {
    this.emf = emf;
    grid = new GridFrame(this);
    MDIFrame.add(grid);
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
      Person p = (Person)valueObject;
      Address a = new Address();
      a.setCountry(grid.getSelectedCountry());
      if (a.getCountry()==null)
          a.setCountry("USA");
      p.setAddress(a);
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
     String sql = "select p from Person p ";
     Object[] paramValues = new Object[]{};
     if (grid.getSelectedCountry()!=null) {
       sql += "where p.address.country = ?1";
       paramValues = new Object[]{grid.getSelectedCountry()};
     }

/*
      return JPAUtils.getAllFromQuery(
            filteredColumns,
            currentSortedColumns,
            currentSortedVersusColumns,
            Person.class,
            "select p from Person p",
            paramValues,
            emf.createEntityManager()
      );
*/
      return JPAUtils.getBlockFromQuery(
            action,
            startIndex,
            25, // blockSize
            filteredColumns,
            currentSortedColumns,
            currentSortedVersusColumns,
            Person.class,
            sql,
            paramValues,
            emf.createEntityManager()
      );

    }
    catch (Throwable ex) {
      ex.printStackTrace();
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      try {
          for (Object p: newValueObjects)
            em.persist(p);
          em.getTransaction().commit();
          return new VOListResponse(newValueObjects,false,newValueObjects.size());
      }
      catch(Throwable e) {
        e.printStackTrace();
        em.getTransaction().rollback();
        return new ErrorResponse(e.getMessage());
      }
      finally {
        try {
          em.close();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      try {
          for (Object p: persistentObjects)
            em.merge(p);
          em.getTransaction().commit();
          return new VOListResponse(persistentObjects,false,persistentObjects.size());
      }
      catch(Throwable e) {
        e.printStackTrace();
        em.getTransaction().rollback();
        return new ErrorResponse(e.getMessage());
      }
      finally {
        try {
          em.close();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
  }

  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
      EntityManager em = emf.createEntityManager();
      em.getTransaction().begin();
      try {
          for (Object p: persistentObjects)
            em.remove(em.merge(p));

          em.getTransaction().commit();
          return new VOListResponse(persistentObjects,false,persistentObjects.size());
      }
      catch(Throwable e) {
        e.printStackTrace();
        em.getTransaction().rollback();
        return new ErrorResponse(e.getMessage());
      }
      finally {
        try {
          em.close();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
  }






}
