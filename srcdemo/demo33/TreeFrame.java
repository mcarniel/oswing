package demo33;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.tree.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Tree Frame</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TreeFrame extends JFrame {

  private TreePanel tree = new TreePanel();


  public TreeFrame(TreeFrameController controller) {
    try {
      jbInit();
      setTitle("Check-boxes Tree");
      setSize(600,300);
      tree.setTreeController(controller);
      tree.setTreeDataLocator(controller);
      tree.setLeavesImageName("node.gif");
      tree.setFolderIconName("cd.gif");
      tree.setExpandAllNodes(true);
      tree.setShowCheckBoxes(true);
      tree.setShowCheckBoxesOnLeaves(false);
      tree.setShowsRootHandles(true);
      tree.setEnabled(true);

      setVisible(true);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    tree.setSelectionForeground(Color.black);
    this.getContentPane().add(tree, BorderLayout.CENTER);
  }


}

