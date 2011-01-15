package demo39;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.tree.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import org.openswing.swing.util.client.ClientUtils;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Frame that contains two trees.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TreeFrame extends JFrame {

  private TreePanel firstTree = new TreePanel();
  private TreePanel secondTree = new TreePanel();
  private JSplitPane splitPane = new JSplitPane();

  /** cursor to show on dragging */
  private Cursor dragCursor = null;

  private DefaultMutableTreeNode dragNode = null; // node in first tree currently dragged


  public TreeFrame() {
    try {
      super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
      jbInit();
      setTitle("Draggable Tree");
      setSize(600,600);

      dragCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        ClientUtils.getImage("drag.gif"),
        new Point(15, 10),
        ClientSettings.getInstance().getResources().getResource("drag")
      );

      FirstTreeController firstController = new FirstTreeController();
      firstTree.setTreeController(firstController);
      firstTree.setTreeDataLocator(firstController);
      firstTree.setLeavesImageName("node.gif");
      firstTree.setExpandAllNodes(true);


      SecondTreeController secondController = new SecondTreeController();
      secondTree.setTreeController(secondController);
      secondTree.setTreeDataLocator(secondController);
      secondTree.setLeavesImageName("node.gif");
      secondTree.setExpandAllNodes(true);

      // enable drag from the first tree and drop ONLY onto the second tree...
      firstTree.enableDrag("FIRST_TREE",new TreeDragNDropListener() { // FIRST_TREE is the identifier of the first tree, to use to decide whether allowing drop operation

        public boolean dragEnabled() {
          // drag operation has started...
          dragNode = (DefaultMutableTreeNode)firstTree.getSelectedNode();
          return true;
        }


        public boolean dropEnabled(DefaultMutableTreeNode node,String treeId) {
            return false; // drop not allowed in trees different from the second tree...
        }

      });


      // enable ONLY drop onto the second tree, starting from nodes of the first tree...
      secondTree.enableDrag("SECOND_TREE",new TreeDragNDropListener() {

        public boolean dragEnabled() {
            return false; // draw not allowed in the second tree...
        }


        public boolean dropEnabled(DefaultMutableTreeNode node,String treeId) {
          if (treeId.equals("FIRST_TREE")) {
            // copy node...
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
            if (parentNode!=null) {
              DefaultMutableTreeNode newNode = new OpenSwingTreeNode(dragNode.getUserObject());
              if (((TestVO)node.getUserObject()).getCode()==null)
                // dropped node is a folder...
                node.add(newNode);
              else
                // dropped node is a leaf...
                parentNode.insert(newNode,parentNode.getIndex(node)+1);
              secondTree.repaintTree();
              secondTree.getTree().setSelectionPath(new TreePath(newNode.getPath()));
              dragCursor = null;
            }
            return true;
          }
          else
            return false; // drop not allowed in trees different from the second tree...
        }


        public void dropEnter() {
          if (dragNode!=null)
            secondTree.setCursor(dragCursor);
        }


      });





      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    splitPane.setDividerLocation(300);
    firstTree.setSelectionBackground(Color.orange);
    firstTree.setSelectionForeground(SystemColor.textHighlight);
    secondTree.setSelectionBackground(Color.orange);
    secondTree.setSelectionForeground(SystemColor.textHighlight);
    splitPane.add(firstTree,JSplitPane.TOP);
    splitPane.add(secondTree,JSplitPane.BOTTOM);
    this.getContentPane().add(splitPane, BorderLayout.CENTER);
  }


}

