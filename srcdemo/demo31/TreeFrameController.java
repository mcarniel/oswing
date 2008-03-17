package demo31;

import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.tree.client.*;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TreeFrameController extends TreeDataLocator implements TreeController {

  private TreeFrame tree = null;

  public TreeFrameController() {
    tree = new TreeFrame(this);
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
    vo.setCode("C0");
    vo.setDescription("Folder0");
    DefaultMutableTreeNode root = new OpenSwingTreeNode(vo);
    DefaultTreeModel model = new DefaultTreeModel(root);
    TestVO vo1 = new TestVO();
    vo1.setFolderCode("C1");
    vo1.setDescription("Folder1");
    DefaultMutableTreeNode n1 = new OpenSwingTreeNode(vo1);
    TestVO vo2 = new TestVO();
    vo2.setFolderCode("C2");
    vo2.setDescription("Folder2");
    DefaultMutableTreeNode n2 = new OpenSwingTreeNode(vo2);
    root.add(n1);
    root.add(n2);

    TestVO vo11 = new TestVO();
    vo11.setFolderCode("C11");
    vo11.setDescription("Folder11");
    vo11.setIconImageName("folder3.gif");
    DefaultMutableTreeNode n11 = new OpenSwingTreeNode(vo11);
    TestVO vo12 = new TestVO();
    vo12.setFolderCode("C12");
    vo12.setDescription("Folder12");
    vo12.setIconImageName("folder3.gif");
    DefaultMutableTreeNode n12 = new OpenSwingTreeNode(vo12);
    n1.add(n11);
    n1.add(n12);

    TestVO vo111 = new TestVO();
    vo111.setFolderCode("C11");
    vo111.setCode("C111");
    vo111.setDescription("Node111");
    DefaultMutableTreeNode n111 = new OpenSwingTreeNode(vo111);
    TestVO vo112 = new TestVO();
    vo112.setFolderCode("C11");
    vo112.setCode("C112");
    vo112.setDescription("Node112");
    DefaultMutableTreeNode n112 = new OpenSwingTreeNode(vo112);
    n11.add(n111);
    n11.add(n112);

    TestVO vo121 = new TestVO();
    vo121.setFolderCode("C12");
    vo121.setCode("C121");
    vo121.setDescription("Node121");
    DefaultMutableTreeNode n121 = new OpenSwingTreeNode(vo121);
    TestVO vo122 = new TestVO();
    vo122.setFolderCode("C12");
    vo122.setCode("C122");
    vo122.setDescription("Node122");
    DefaultMutableTreeNode n122 = new OpenSwingTreeNode(vo122);
    n12.add(n121);
    n12.add(n122);

    TestVO vo21 = new TestVO();
    vo21.setFolderCode("C21");
    vo21.setDescription("Folder21");
    vo21.setIconImageName("folder3.gif");
    DefaultMutableTreeNode n21 = new OpenSwingTreeNode(vo21);
    TestVO vo22 = new TestVO();
    vo22.setFolderCode("C22");
    vo22.setDescription("Folder22");
    vo22.setIconImageName("folder3.gif");
    DefaultMutableTreeNode n22 = new OpenSwingTreeNode(vo22);
    n2.add(n21);
    n2.add(n22);

    TestVO vo211 = new TestVO();
    vo211.setFolderCode("C21");
    vo211.setCode("C211");
    vo211.setDescription("Copy File");
    vo211.setIconImageName("copy.gif");
    vo211.setTooltip("Click here to copy the document");
    DefaultMutableTreeNode n211 = new OpenSwingTreeNode(vo211);
    TestVO vo212 = new TestVO();
    vo212.setFolderCode("C21");
    vo212.setCode("C212");
    vo212.setDescription("Write Document");
    vo212.setIconImageName("edit.gif");
    vo212.setTooltip("Click here to edit the document");
    DefaultMutableTreeNode n212 = new OpenSwingTreeNode(vo212);
    n21.add(n211);
    n21.add(n212);

    TestVO vo221 = new TestVO();
    vo221.setFolderCode("C22");
    vo221.setCode("C221");
    vo221.setDescription("Export Data");
    vo221.setIconImageName("export.gif");
    vo221.setTooltip("Click here to export the document");
    DefaultMutableTreeNode n221 = new OpenSwingTreeNode(vo221);
    TestVO vo222 = new TestVO();
    vo222.setFolderCode("C22");
    vo222.setCode("C222");
    vo222.setDescription("Print1");
    vo222.setIconImageName("print.gif");
    vo222.setTooltip("Click here to print the document");
    DefaultMutableTreeNode n222 = new OpenSwingTreeNode(vo222);
    n22.add(n221);
    n22.add(n222);

    TestVO vo3 = new TestVO();
    vo3.setFolderCode("C1");
    vo3.setCode("C3");
    vo3.setDescription("Node3");
    DefaultMutableTreeNode n3 = new OpenSwingTreeNode(vo3);
    n1.add(n3);

    for(int i=0;i<30;i++) {
      TestVO von = new TestVO();
      von.setFolderCode("C"+(i+4));
      von.setCode("C"+(i+4));
      von.setDescription("Node"+(i+4));
      DefaultMutableTreeNode nn = new OpenSwingTreeNode(von);
      n1.add(nn);
    }


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
