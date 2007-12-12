package demo30;

import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import java.util.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.items.client.ItemsDataLocator;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Data locator for a ComboBoxVOControl.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DemoComboDataLocator extends ItemsDataLocator {


  private Connection conn = null;


  public DemoComboDataLocator(Connection conn) {
    this.conn = conn;
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
        Class valueObjectType
        ) {
      Statement stmt = null;
      try {
        stmt = DemoComboDataLocator.this.conn.createStatement();
        ResultSet rset = stmt.executeQuery(
            "select CITIES.CITY,CITIES.ZIP_CODE,CITIES.STATE from CITIES order by CITIES.CITY");
        ArrayList list = new ArrayList();
        while (rset.next()) {
          TestComboVO vo = new TestComboVO();
          vo.setCity(rset.getString(1));
          vo.setZipCode(rset.getString(2));
          vo.setState(rset.getString(3));
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
    }



}
