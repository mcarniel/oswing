package demo6;

import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Controller</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DemoLookupController extends LookupController {


  private Connection conn = null;


  public DemoLookupController(Connection conn) {
    this.conn = conn;
    this.setCodeSelectionWindow(this.TREE_FRAME);
    this.setLookupDataLocator(new LookupDataLocator() {

      /**
       * Method called by lookup controller when validating code.
       * @param code code to validate
       * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
       */
      public Response validateCode(String code) {
        Statement stmt = null;
        try {
          stmt = DemoLookupController.this.conn.createStatement();
          ResultSet rset = stmt.executeQuery(
              "select DEMO6_LOOKUP.CODE,DEMO6_LOOKUP.DESCRCODE from DEMO6_LOOKUP where CODE='" +
              code + "'");
          ArrayList list = new ArrayList();
          while (rset.next()) {
            TestLookupVO vo = new TestLookupVO();
            vo.setLookupValue(rset.getString(1));
            vo.setDescrLookupValue(rset.getString(2));
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
        // method not required...
        return null;
      }



      /**
       * Method called by the TreePanel to fill the tree.
       * @return a VOReponse containing a DefaultTreeModel object
       */
      public Response getTreeModel(JTree tree) {
        Statement stmt = null;
        try {
          DefaultMutableTreeNode root = new OpenSwingTreeNode();
          int lev = 0;
          DefaultMutableTreeNode currN = null;

          stmt = DemoLookupController.this.conn.createStatement();
          ResultSet rset = stmt.executeQuery(
              "select DEMO6_LOOKUP.CODE,DEMO6_LOOKUP.DESCRCODE from DEMO6_LOOKUP");
          while (rset.next()) {
            TestLookupVO vo = new TestLookupVO();
            vo.setLookupValue(rset.getString(1));
            vo.setDescrLookupValue(rset.getString(2));
            DefaultMutableTreeNode n = new OpenSwingTreeNode(vo);
            if (lev==0) {
              root.add(n);
              currN = n;
              lev++;
            }
            else if (lev==1) {
              currN.add(n);
              currN = n;
              lev++;
            }
            else if (lev==2) {
              currN.add(n);
              lev=0;
            }
          }


          return new VOResponse(new DefaultTreeModel(root));
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          return new VOResponse(new DefaultTreeModel(new OpenSwingTreeNode()));
        }
        finally {
          try {
            stmt.close();
          }
          catch (SQLException ex1) {
          }
        }
      }



    });

    this.getLookupDataLocator().setNodeNameAttribute("descrLookupValue");

    this.setLookupValueObjectClassName("demo6.TestLookupVO");
    this.addLookup2ParentLink("lookupValue", "lookupValue");
    this.addLookup2ParentLink("descrLookupValue", "descrLookupValue");
    this.setAllColumnVisible(true);
    this.setPreferredWidthColumn("descrLookupValue", 200);
  }



}
