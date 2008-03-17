package demo28;

import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Controller that validate task codes.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TaskLookupController extends LookupController {


  private Connection conn = null;


  public TaskLookupController(Connection conn) {
    this.conn = conn;
    this.setLookupDataLocator(new LookupDataLocator() {

      /**
       * Method called by lookup controller when validating code.
       * @param code code to validate
       * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
       */
      public Response validateCode(String code) {
        try {
          String sql = "select TASKS.TASK_CODE,TASKS.DESCRIPTION,TASKS.STATUS from TASKS where STATUS='E' and TASK_CODE=?";

          // mapping between attributes and database fields...
          Map attribute2dbField = new HashMap();
          attribute2dbField.put("taskCode","TASKS.TASK_CODE");
          attribute2dbField.put("description","TASKS.DESCRIPTION");
          attribute2dbField.put("status","TASKS.STATUS");

          ArrayList vars = new ArrayList();
          vars.add(code);

          return QueryUtil.getQuery(
            TaskLookupController.this.conn,
            sql,
            vars, // list of values linked to "?" parameters in sql
            attribute2dbField,
            TaskVO.class, // v.o. to dinamically create for each row...
            "Y",
            "N",
            new GridParams(),
            true // log query...
          );
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return new ErrorResponse(ex.getMessage());
        }

/*
        // an alternative way: you can define your own business logic to retrieve data and adding filtering/sorting conditions at hand...
        Statement stmt = null;
        try {
          stmt = TaskLookupController.this.conn.createStatement();
          ResultSet rset = stmt.executeQuery(
              "select TASKS.TASK_CODE,TASKS.DESCRIPTION from TASKS where STATUS='E' and TASK_CODE='" +code + "'");
          ArrayList list = new ArrayList();
          while (rset.next()) {
            TaskVO vo = new TaskVO();
            vo.setTaskCode(rset.getString(1));
            vo.setDescription(rset.getString(2));
            list.add(vo);
          }
          if (list.size() > 0)
            return new VOListResponse(list, false, list.size());
          else
            return new ErrorResponse("Code not valid");
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
*/
      }

      /**
       * Method called by lookup controller when user clicks on lookup button.
       * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
       * @param startIndex current index row on grid to use to start fetching data
       * @param filteredColumns filtered columns
       * @param currentSortedColumns sorted columns
       * @param currentSortedVersusColumns ordering versus of sorted columns
       * @param valueObjectType type of value object associated to the lookup grid
       * @return list of value objects to fill in the lookup grid: VOListResponse if data fetching has success, ErrorResponse otherwise
       */
      public Response loadData(
          int action,
          int startIndex,
          Map filteredColumns,
          ArrayList currentSortedColumns,
          ArrayList currentSortedVersusColumns,
          Class valueObjectType
          ) {
        try {
          String sql = "select TASKS.TASK_CODE,TASKS.DESCRIPTION,TASKS.STATUS from TASKS where STATUS='E' ";

          // mapping between attributes and database fields...
          Map attribute2dbField = new HashMap();
          attribute2dbField.put("taskCode","TASKS.TASK_CODE");
          attribute2dbField.put("description","TASKS.DESCRIPTION");
          attribute2dbField.put("status","TASKS.STATUS");

          return QueryUtil.getQuery(
            TaskLookupController.this.conn,
            sql,
            new ArrayList(), // list of values linked to "?" parameters in sql
            attribute2dbField,
            TaskVO.class, // v.o. to dinamically create for each row...
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
        Statement stmt = null;
        try {
          stmt = TaskLookupController.this.conn.createStatement();
          ResultSet rset = stmt.executeQuery(
              "select TASKS.TASK_CODE,TASKS.DESCRIPTION from TASKS where STATUS='E' ");
          ArrayList list = new ArrayList();
          while (rset.next()) {
            TaskVO vo = new TaskVO();
            vo.setTaskCode(rset.getString(1));
            vo.setDescription(rset.getString(2));
            list.add(vo);
          }
          return new VOListResponse(list, false, list.size());
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
*/
      }


      /**
       * Method called by the TreePanel to fill the tree.
       * @return a VOReponse containing a DefaultTreeModel object
       */
      public Response getTreeModel(JTree tree) {
        return new VOResponse(new DefaultTreeModel(new OpenSwingTreeNode()));
      }


    });

    this.setLookupValueObjectClassName("demo28.TaskVO");
    this.addLookup2ParentLink("taskCode", "taskCode");
    this.addLookup2ParentLink("description", "taskDescription");
    this.setAllColumnVisible(true);
    this.setPreferredWidthColumn("description", 200);
  }



}
