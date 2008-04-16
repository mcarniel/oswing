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
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.exp.ExpressionFactory;
import java.awt.Dimension;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Controller that validate dept codes.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DeptLookupController extends LookupController {


  private DataContext context = null;


  public DeptLookupController(DataContext context) {
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
                          DeptVO.DEPT_CODE_PROPERTY,
                          code);
          SelectQuery select2 = new SelectQuery(DeptVO.class, qualifier2);
          List list = DeptLookupController.this.context.performQuery(select2);
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
          SelectQuery select = new SelectQuery(DeptVO.class);
          List list = DeptLookupController.this.context.performQuery(select);

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
//        "DeptDetailVO",
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
//            "DeptVO",
//            DeptLookupController.this.sessions,
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

    this.setLookupValueObjectClassName("demo36.DeptVO");
    this.addLookup2ParentLink("deptCode", "dept.deptCode");
    this.addLookup2ParentLink("description", "dept.description");
    this.setAllColumnVisible(false);
    this.setVisibleColumn("deptCode",true);
    this.setVisibleColumn("description",true);
    this.setPreferredWidthColumn("description", 200);
    this.setFramePreferedSize(new Dimension(350,400));
  }



}
