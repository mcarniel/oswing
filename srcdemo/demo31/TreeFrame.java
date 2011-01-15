package demo31;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.tree.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
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

  private DefaultMutableTreeNode dragNode = null;


  public TreeFrame(TreeFrameController controller) {
    try {
      super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
      jbInit();
      setTitle("Draggable Tree");
      setSize(600,400);
      tree.setTreeController(controller);
      tree.setTreeDataLocator(controller);
      tree.setLeavesImageName("node.gif");
      tree.setExpandAllNodes(true);
      tree.setIconAttributeName("iconImageName");
      tree.setTooltipAttributeName("tooltip");
      //tree.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
      tree.addPopupMenuItem("rename node",'R',true,new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          TestVO vo = (TestVO)TreeFrame.this.tree.getSelectedNode().getUserObject();
          String newValue = JOptionPane.showInputDialog(
            TreeFrame.this,
            ClientSettings.getInstance().getResources().getResource("new description:"),
            vo.getDescription()
          );
          if (newValue!=null)
            vo.setDescription(newValue);
        }

      });
      tree.addPopupSeparator();
      tree.addPopupMenuItem("remove node",'E',true,new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          DefaultMutableTreeNode node = tree.getSelectedNode();
          ((DefaultMutableTreeNode)node.getParent()).remove(node);
          tree.revalidateTree();
        }

      });


      tree.addPopupMenuItem("show remove command",'S',true,new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          tree.setMenuItemVisible("remove node",true);
          tree.setMenuItemEnabled("show remove command",false);
          tree.setMenuItemEnabled("hide remove command",true);
        }

      });
      tree.addPopupMenuItem("hide remove command",'H',true,new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          tree.setMenuItemVisible("remove node",false);
          tree.setMenuItemEnabled("show remove command",true);
          tree.setMenuItemEnabled("hide remove command",false);
        }

      });




      // enable drag 'n drop onto the tree...
      tree.enableDrag("TREE",new TreeDragNDropListener() {

        public boolean dragEnabled() {
          // drag operation has started...
          dragNode = (DefaultMutableTreeNode)tree.getSelectedNode();
          return true;
        }


        public boolean dropEnabled(DefaultMutableTreeNode node,String treeId) {
          // drop has terminated...
          int num = JOptionPane.showOptionDialog(
            TreeFrame.this,
            ClientSettings.getInstance().getResources().getResource("which operation?"),
            ClientSettings.getInstance().getResources().getResource("node dropped"),
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            new Object[]{
              ClientSettings.getInstance().getResources().getResource("move node"),
              ClientSettings.getInstance().getResources().getResource("copy node"),
              ClientSettings.getInstance().getResources().getResource("cancel")
            },
            null
          );
          if (num==0) {
            // move node...
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)node.getParent();
            if (parentNode!=null) {
              ((DefaultMutableTreeNode)dragNode.getParent()).remove(dragNode);
              if (((TestVO)node.getUserObject()).getCode()==null)
                // dropped node is a folder...
                node.add(dragNode);
              else
                // dropped node is a leaf...
                parentNode.insert(dragNode,parentNode.getIndex(node)+1);
              tree.repaintTree();
              tree.getTree().setSelectionPath(new TreePath(dragNode.getPath()));
            }
          }
          else if (num==1) {
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
              tree.repaintTree();
              tree.getTree().setSelectionPath(new TreePath(newNode.getPath()));
            }
          }

          return true;
        }

      });


      setVisible(true);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    this.getContentPane().add(tree, BorderLayout.CENTER);
  }


}

