package demo39;

import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.tree.client.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Second tree controller.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class SecondTreeController extends TreeDataLocator implements TreeController {


  public SecondTreeController() {
    this.setNodeNameAttribute("description");
  }


  /**
   * getTreeModel
   *
   * @param tree JTree
   * @return Response
   */
  public Response getTreeModel(JTree tree) {
    TestVO vo = new TestVO();
    vo.setCode("C02");
    vo.setDescription("Folder2-1");
    DefaultMutableTreeNode root = new OpenSwingTreeNode(vo);
    DefaultTreeModel model = new DefaultTreeModel(root);
    TestVO vo1 = new TestVO();
    vo1.setFolderCode("C2-1");
    vo1.setDescription("Folder2-1");
    DefaultMutableTreeNode n1 = new OpenSwingTreeNode(vo1);
    TestVO vo2 = new TestVO();
    vo2.setFolderCode("C2-2");
    vo2.setDescription("Folder2-2");
    DefaultMutableTreeNode n2 = new OpenSwingTreeNode(vo2);
    root.add(n1);
    root.add(n2);

    return new VOResponse(model);
  }

  /**
   * doubleClick
   *
   * @param node DefaultMutableTreeNode
   */
  public void doubleClick(DefaultMutableTreeNode node) {
  }

  /**
   * leftClick
   *
   * @param node DefaultMutableTreeNode
   */
  public void leftClick(DefaultMutableTreeNode node) {
  }

  /**
   * rightClick
   *
   * @param node DefaultMutableTreeNode
   * @return boolean
   */
  public boolean rightClick(DefaultMutableTreeNode node) {
    return true;
  }

}
