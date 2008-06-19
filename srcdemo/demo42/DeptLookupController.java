package demo42;

import java.sql.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.openswing.swing.lookup.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import org.openswing.swing.tree.java.*;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Controller that validate dept codes.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DeptLookupController extends LookupController {


  private Connection conn = null;


  public DeptLookupController(Connection conn) {
    this.conn = conn;

    this.setLookupGridController(new LookupGridController() {

      /**
       * Method used to define the background color for each cell of the grid.
       * @param rowNumber selected row index
       * @param attributeName attribute name related to the column currently selected
       * @param value object contained in the selected cell
       * @return background color of the selected cell
       */
      public Color getBackgroundColor(int row,String attributeName,Object value) {
        DeptVO vo = (DeptVO)model.getObjectForRow(row);
        if (vo.getDeptCode().equals("S"))
          return Color.orange;
        return super.getBackgroundColor(row,attributeName,value);
      }

    });

    this.setLookupDataLocator(new LookupDataLocator() {

      /**
       * Method called by lookup controller when validating code.
       * @param code code to validate
       * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
       */
      public Response validateCode(String code) {
        try {
          String sql = "select DEPT.DEPT_CODE,DEPT.DESCRIPTION,DEPT.ADDRESS,DEPT.STATUS from DEPT where STATUS='E' and DEPT_CODE='" +code + "'";

          // mapping between attributes and database fields...
          Map attribute2dbField = new HashMap();
          attribute2dbField.put("deptCode","DEPT.DEPT_CODE");
          attribute2dbField.put("description","DEPT.DESCRIPTION");
          attribute2dbField.put("address","DEPT.ADDRESS");
          attribute2dbField.put("status","DEPT.STATUS");

          return QueryUtil.getQuery(
            DeptLookupController.this.conn,
            sql,
            new ArrayList(), // list of values linked to "?" parameters in sql
            attribute2dbField,
            DeptVO.class, // v.o. to dinamically create for each row...
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
          stmt = DeptLookupController.this.conn.createStatement();
          ResultSet rset = stmt.executeQuery(
              "select DEPT.DEPT_CODE,DEPT.DESCRIPTION,DEPT.ADDRESS from DEPT where STATUS='E' and DEPT_CODE='" +code + "'");
          ArrayList list = new ArrayList();
          while (rset.next()) {
            DeptVO vo = new DeptVO();
            vo.setDeptCode(rset.getString(1));
            vo.setDescription(rset.getString(2));
            vo.setAddress(rset.getString(3));
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
          String sql = "select DEPT.DEPT_CODE,DEPT.DESCRIPTION,DEPT.ADDRESS,DEPT.STATUS from DEPT where STATUS='E' ";

          // mapping between attributes and database fields...
          Map attribute2dbField = new HashMap();
          attribute2dbField.put("deptCode","DEPT.DEPT_CODE");
          attribute2dbField.put("description","DEPT.DESCRIPTION");
          attribute2dbField.put("address","DEPT.ADDRESS");
          attribute2dbField.put("status","DEPT.STATUS");

          return QueryUtil.getQuery(
            DeptLookupController.this.conn,
            sql,
            new ArrayList(), // list of values linked to "?" parameters in sql
            attribute2dbField,
            DeptVO.class, // v.o. to dinamically create for each row...
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
          stmt = DeptLookupController.this.conn.createStatement();
          ResultSet rset = stmt.executeQuery(
              "select DEPT.DEPT_CODE,DEPT.DESCRIPTION,DEPT.ADDRESS from DEPT where STATUS='E' ");
          ArrayList list = new ArrayList();
          while (rset.next()) {
            DeptVO vo = new DeptVO();
            vo.setDeptCode(rset.getString(1));
            vo.setDescription(rset.getString(2));
            vo.setAddress(rset.getString(3));
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

    this.setLookupValueObjectClassName("demo42.DeptVO");
    this.addLookup2ParentLink("deptCode", "deptCode");
    this.addLookup2ParentLink("description", "deptDescription");
    this.setAllColumnVisible(true);
    this.setVisibleColumn("address",false);
    this.setVisibleColumn("status",false);
    this.setPreferredWidthColumn("description", 200);
    this.setFilterableColumn("deptCode",true);
    this.setSortableColumn("deptCode",true);
    this.setSortableColumn("description",true);
    this.setFramePreferedSize(new Dimension(350,500));
  }



}
