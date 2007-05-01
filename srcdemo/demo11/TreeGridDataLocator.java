
package demo11;

import org.openswing.swing.tree.client.TreeDataLocator;
import org.openswing.swing.message.receive.java.Response;
import javax.swing.JTree;
import java.math.BigDecimal;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.message.receive.java.VOResponse;

/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Tree-table data locator</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TreeGridDataLocator extends TreeDataLocator {


  public TreeGridDataLocator() {
    this.setNodeNameAttribute("itemCode");
  }


  public static void main(String[] args) {
    TreeGridDataLocator treeTableDataLocator1 = new TreeGridDataLocator();
  }


  /**
   * Method called by the TreePanel to fill the tree.
   * @return a VOReponse containing a DefaultTreeModel object
   */
  public Response getTreeModel(JTree tree) {
    TestVO vo = new TestVO();
    vo.setDescription("Chair");
    vo.setItemCode("C1");
    vo.setPrice(new BigDecimal(110));
    vo.setQty(new BigDecimal(1));
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(vo);
    DefaultTreeModel model = new DefaultTreeModel(root);

    vo = new TestVO();
    vo.setDescription("Legged");
    vo.setItemCode("L1");
    vo.setPrice(new BigDecimal(10));
    vo.setQty(new BigDecimal(4));
    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(vo);
    root.add(node1);

    vo = new TestVO();
    vo.setDescription("Backrest");
    vo.setItemCode("B1");
    vo.setPrice(new BigDecimal(50));
    vo.setQty(new BigDecimal(1));
    DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(vo);
    root.add(node2);

    vo = new TestVO();
    vo.setDescription("Seat");
    vo.setItemCode("S1");
    vo.setPrice(new BigDecimal(50));
    vo.setQty(new BigDecimal(1));
    DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(vo);
    root.add(node3);

    vo = new TestVO();
    vo.setDescription("Pillow");
    vo.setItemCode("P1");
    vo.setPrice(new BigDecimal(40));
    vo.setQty(new BigDecimal(1));
    DefaultMutableTreeNode node31 = new DefaultMutableTreeNode(vo);
    node3.add(node31);

    vo = new TestVO();
    vo.setDescription("Wood");
    vo.setItemCode("W1");
    vo.setPrice(new BigDecimal(10));
    vo.setQty(new BigDecimal(1));
    DefaultMutableTreeNode node32 = new DefaultMutableTreeNode(vo);
    node3.add(node32);

    return new VOResponse(model);
  }

}
