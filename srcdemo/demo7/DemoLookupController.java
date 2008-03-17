package demo7;

import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.java.*;
import java.sql.*;
import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.Dimension;
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
    this.setFramePreferedSize(new Dimension(600,400));
    this.setCodeSelectionWindow(this.TREE_GRID_FRAME);
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
              "select DEMO7_LOOKUP.CODE,DEMO7_LOOKUP.DESCRCODE from DEMO7_LOOKUP where CODE='" +
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
        Statement stmt = null;
        try {
          stmt = DemoLookupController.this.conn.createStatement();
          ResultSet rset = stmt.executeQuery(
              "select DEMO7_LOOKUP.CODE,DEMO7_LOOKUP.DESCRCODE from DEMO7_LOOKUP");
          ArrayList list = new ArrayList();
          while (rset.next()) {
            TestLookupVO vo = new TestLookupVO();
            vo.setLookupValue(rset.getString(1));
            vo.setDescrLookupValue(rset.getString(2));
            list.add(vo);
          }
          Map map = getLookupFrameParams();
          if (map.get(Consts.TREE_FILTER)!=null) {
            TreeNodeVO nodeVO = (TreeNodeVO)map.get(Consts.TREE_FILTER);
            if (nodeVO.getCodLevel().equals("1")) {
              list.remove(0);
              list.remove(0);
              list.remove(0);
            }
            else if (nodeVO.getCodLevel().equals("2")) {
              int len = list.size()-3;
              for(int i=0;i<len;i++)
                list.remove(3);
            }

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

      }


      /**
       * Method called by the TreePanel to fill the tree.
       * @return a VOReponse containing a DefaultTreeModel object
       */
      public Response getTreeModel(JTree tree) {
        try {
          TreeNodeVO vo = null;

          vo = new TreeNodeVO();
          vo.setCodLevel("0");
          vo.setDescrLevel("Level0");
          DefaultMutableTreeNode root = new OpenSwingTreeNode(vo);

          vo = new TreeNodeVO();
          vo.setCodLevel("1");
          vo.setDescrLevel("Level1");
          DefaultMutableTreeNode n1 = new OpenSwingTreeNode(vo);
          root.add(n1);

          vo = new TreeNodeVO();
          vo.setCodLevel("2");
          vo.setDescrLevel("Level2");
          DefaultMutableTreeNode n2 = new OpenSwingTreeNode(vo);
          root.add(n2);

          return new VOResponse(new DefaultTreeModel(root));
        }
        catch (Exception ex) {
          ex.printStackTrace();
          return new VOResponse(new DefaultTreeModel(new OpenSwingTreeNode()));
        }

      }



    });

    this.getLookupDataLocator().setNodeNameAttribute("descrLevel");

    this.setLookupValueObjectClassName("demo7.TestLookupVO");
    this.addLookup2ParentLink("lookupValue", "lookupValue");
    this.addLookup2ParentLink("descrLookupValue", "descrLookupValue");
    this.setAllColumnVisible(true);
    this.setPreferredWidthColumn("descrLookupValue", 200);
  }



}
