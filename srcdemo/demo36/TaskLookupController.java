package demo36;

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
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Controller that validate task codes.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TaskLookupController extends LookupController {


  private DataContext context = null;


  public TaskLookupController(DataContext context) {
    this.context = context;
    this.setLookupDataLocator(new LookupDataLocator() {

      /**
       * Method called by lookup controller when validating code.
       * @param code code to validate
       * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
       */
      public Response validateCode(String code) {
        try {
          Expression qualifier2 = ExpressionFactory.likeIgnoreCaseExp(
                          TasksVO.TASK_CODE_PROPERTY,
                          code);
          SelectQuery select2 = new SelectQuery(TasksVO.class, qualifier2);
          List list = TaskLookupController.this.context.performQuery(select2);
          return new VOListResponse(new ArrayList(list),false,list.size());
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return new ErrorResponse(ex.getMessage());
        }
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
          SelectQuery select = new SelectQuery(TasksVO.class);
          List list = TaskLookupController.this.context.performQuery(select);

          return new VOListResponse(new ArrayList(list),false,list.size());

//      READ WHOLE RESULT-SET...
//      Response res = HibernateUtils.getAllFromQuery(
//        filteredColumns,
//        currentSortedColumns,
//        currentSortedVersusColumns,
//        valueObjectType,
//        baseSQL,
//        new Object[0],
//        new Type[0],
//        "TASKS",
//        sessions,
//        sess
//      );
//      sess.close();
//      return res;
//      END READ WHOLE RESULT-SET...


//    READ A BLOCK OF DATA FROM RESULT-SET...
//          Response res = HibernateUtils.getBlockFromQuery(
//            action,
//            startIndex,
//            50, // block size...
//            filteredColumns,
//            currentSortedColumns,
//            currentSortedVersusColumns,
//            valueObjectType,
//            baseSQL,
//            new Object[0],
//            new Type[0],
//            "TASKS",
//            TaskLookupController.this.sessions,
//            sess
//          );
//          sess.close();
//          return res;
//    END READ A BLOCK OF DATA FROM RESULT-SET...
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return new ErrorResponse(ex.getMessage());
        }
      }


      /**
       * Method called by the TreePanel to fill the tree.
       * @return a VOReponse containing a DefaultTreeModel object
       */
      public Response getTreeModel(JTree tree) {
        return new VOResponse(new DefaultTreeModel(new OpenSwingTreeNode()));
      }


    });

    this.setLookupValueObjectClassName("demo36.TasksVO");
    this.addLookup2ParentLink("taskCode", "task.taskCode");
    this.addLookup2ParentLink("description", "task.description");
    this.setAllColumnVisible(false);
    this.setVisibleColumn("taskCode", true);
    this.setVisibleColumn("description", true);
    this.setPreferredWidthColumn("description", 200);
  }



}
