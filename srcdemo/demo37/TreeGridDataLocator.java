package demo37;

import org.openswing.swing.tree.client.TreeDataLocator;
import org.openswing.swing.message.receive.java.Response;
import javax.swing.JTree;
import java.math.BigDecimal;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.event.TreeExpansionEvent;

/**
 * <p>Title: OpenSwing Demo</p>
 * <p>Description: Tree-table data locator</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TreeGridDataLocator extends TreeDataLocator implements TreeWillExpandListener {

  JTree t  = null;


  public TreeGridDataLocator() {
  }




  /**
   * Method called by the TreePanel to fill the tree.
   * @return a VOReponse containing a DefaultTreeModel object
   */
  public Response getTreeModel(JTree tree) {
    t = tree;
    tree.removeTreeWillExpandListener(this);
    tree.addTreeWillExpandListener(this);

    // only first level nodes are initially fetched
    TestVO vo = new TestVO();
    vo.setDescription("Product xxx");
    vo.setItemCode("XXX");
    vo.setPrice(new BigDecimal(110));
    vo.setQty(new BigDecimal(1));
    vo.setHasChildren(true);
    DefaultMutableTreeNode root = new OpenSwingTreeNode(vo);
    DefaultTreeModel model = new DefaultTreeModel(root);

    for(int i=1;i<=10;i++) {
      vo = new TestVO();
      vo.setDescription("Component L"+i);
      vo.setItemCode("L"+i);
      vo.setHasChildren(i%2==0);
      vo.setPrice(new BigDecimal(Math.ceil(Math.random()*10)));
      vo.setQty(new BigDecimal(i));
      DefaultMutableTreeNode node = new OpenSwingTreeNode(vo);
      root.add(node);
    }

    return new VOResponse(model);
  }


  /**
   * treeWillCollapse
   *
   * @param event TreeExpansionEvent
   */
  public void treeWillCollapse(TreeExpansionEvent event) {
  }


  /**
   * treeWillExpand
   *
   * @param event TreeExpansionEvent
   */
  public void treeWillExpand(TreeExpansionEvent event) {
    DefaultMutableTreeNode nodeToExpand = (DefaultMutableTreeNode)event.getPath().getLastPathComponent();
    TestVO parentVO = (TestVO)nodeToExpand.getUserObject();

    TestVO vo;
    if (parentVO.isHasChildren() && nodeToExpand.getChildCount()==0)
      for(int i=1;i<=10;i++) {
        vo = new TestVO();
        vo.setDescription("Component "+parentVO.getItemCode()+"."+i);
        vo.setItemCode(parentVO.getItemCode()+"."+i);
        vo.setHasChildren(nodeToExpand.getLevel()<5);
        vo.setPrice(new BigDecimal(Math.ceil(Math.random()*10)));
        vo.setQty(new BigDecimal(i));
        DefaultMutableTreeNode node = new OpenSwingTreeNode(vo);
        nodeToExpand.add(node);
      }
  }

}
