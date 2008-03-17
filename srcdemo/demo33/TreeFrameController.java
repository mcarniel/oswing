package demo33;

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
    DefaultMutableTreeNode root = new OpenSwingTreeNode();
    DefaultTreeModel model = new DefaultTreeModel(root);

    TestVO vo = new TestVO();
    vo.setCode("BEATLES");
    vo.setDescription("Beatles");
    DefaultMutableTreeNode nb = new OpenSwingTreeNode(vo);
    root.add(nb);

    TestVO vo1 = new TestVO();
    vo1.setFolderCode("B1");
    vo1.setDescription("Love me do");
    DefaultMutableTreeNode n1 = new OpenSwingTreeNode(vo1);
    TestVO vo2 = new TestVO();
    vo2.setFolderCode("B2");
    vo2.setDescription("From me to you");
    DefaultMutableTreeNode n2 = new OpenSwingTreeNode(vo2);
    TestVO vo3 = new TestVO();
    vo3.setFolderCode("B3");
    vo3.setDescription("She loves you");
    DefaultMutableTreeNode n3 = new OpenSwingTreeNode(vo3);
    TestVO vo4 = new TestVO();
    vo4.setFolderCode("B4");
    vo4.setDescription("I want to hold your hand");
    DefaultMutableTreeNode n4 = new OpenSwingTreeNode(vo4);
    TestVO vo5 = new TestVO();
    vo5.setFolderCode("B5");
    vo5.setDescription("Cant buy me love");
    DefaultMutableTreeNode n5 = new OpenSwingTreeNode(vo5);
    TestVO vo6 = new TestVO();
    vo6.setFolderCode("B6");
    vo6.setDescription("A hard day s night");
    DefaultMutableTreeNode n6 = new OpenSwingTreeNode(vo6);
    TestVO vo7 = new TestVO();
    vo7.setFolderCode("B7");
    vo7.setDescription("I feel fine");
    DefaultMutableTreeNode n7 = new OpenSwingTreeNode(vo7);
    nb.add(n1);
    nb.add(n2);
    nb.add(n3);
    nb.add(n4);
    nb.add(n5);
    nb.add(n2);
    nb.add(n7);


    vo = new TestVO();
    vo.setCode("HAPPYDAYS");
    vo.setDescription("Happy Days");
    nb = new OpenSwingTreeNode(vo);
    root.add(nb);

    vo1 = new TestVO();
    vo1.setFolderCode("H1");
    vo1.setDescription("We are the world");
    n1 = new OpenSwingTreeNode(vo1);
    vo2 = new TestVO();
    vo2.setFolderCode("H2");
    vo2.setDescription("Do they know it s xmas");
    n2 = new OpenSwingTreeNode(vo2);
    vo3 = new TestVO();
    vo3.setFolderCode("H3");
    vo3.setDescription("I d like to teach the world to sing");
    n3 = new OpenSwingTreeNode(vo3);
    vo4 = new TestVO();
    vo4.setFolderCode("H4");
    vo4.setDescription("Dont worry be happy");
    n4 = new OpenSwingTreeNode(vo4);
    vo5 = new TestVO();
    vo5.setFolderCode("H5");
    vo5.setDescription("Last Xmas");
    n5 = new OpenSwingTreeNode(vo5);
    vo6 = new TestVO();
    vo6.setFolderCode("H6");
    vo6.setDescription("I just called to say I love you");
    n6 = new OpenSwingTreeNode(vo6);
    vo7 = new TestVO();
    vo7.setFolderCode("H7");
    vo7.setDescription("Thanks God its xmas");
    n7 = new OpenSwingTreeNode(vo7);
    nb.add(n1);
    nb.add(n2);
    nb.add(n3);
    nb.add(n4);
    nb.add(n5);
    nb.add(n2);
    nb.add(n7);

    vo = new TestVO();
    vo.setCode("LIGABUE");
    vo.setDescription("Ligabue Collection");
    DefaultMutableTreeNode nl = new OpenSwingTreeNode(vo);
    root.add(nl);

    vo = new TestVO();
    vo.setCode("LIGABUE1");
    vo.setDescription("Ligabue - CD 1");
    nb = new OpenSwingTreeNode(vo);
    nl.add(nb);

    vo1 = new TestVO();
    vo1.setFolderCode("L11");
    vo1.setDescription("Piccola Stella Senza Cielo");
    n1 = new OpenSwingTreeNode(vo1);
    vo2 = new TestVO();
    vo2.setFolderCode("L12");
    vo2.setDescription("Si Viene E Si Va");
    n2 = new OpenSwingTreeNode(vo2);
    vo3 = new TestVO();
    vo3.setFolderCode("L13");
    vo3.setDescription("Il Giorno Di Dolore Che Uno Ha");
    n3 = new OpenSwingTreeNode(vo3);
    vo4 = new TestVO();
    vo4.setFolderCode("L14");
    vo4.setDescription("Sogni Di R&R");
    n4 = new OpenSwingTreeNode(vo4);
    vo5 = new TestVO();
    vo5.setFolderCode("L15");
    vo5.setDescription("Walter Il Mago");
    n5 = new OpenSwingTreeNode(vo5);
    vo6 = new TestVO();
    vo6.setFolderCode("L16");
    vo6.setDescription("Vivo Morto O X");
    n6 = new OpenSwingTreeNode(vo6);
    vo7 = new TestVO();
    vo7.setFolderCode("L17");
    vo7.setDescription("Camera Con Vista Sul Deserto");
    n7 = new OpenSwingTreeNode(vo7);
    nb.add(n1);
    nb.add(n2);
    nb.add(n3);
    nb.add(n4);
    nb.add(n5);
    nb.add(n2);
    nb.add(n7);

    vo = new TestVO();
    vo.setCode("LIGABUE2");
    vo.setDescription("Ligabue - CD 2");
    nb = new OpenSwingTreeNode(vo);
    nl.add(nb);

    vo1 = new TestVO();
    vo1.setFolderCode("L21");
    vo1.setDescription("Voglio Volere");
    n1 = new OpenSwingTreeNode(vo1);
    vo2 = new TestVO();
    vo2.setFolderCode("L22");
    vo2.setDescription("Sarà Un Bel Souvenir");
    n2 = new OpenSwingTreeNode(vo2);
    vo3 = new TestVO();
    vo3.setFolderCode("L23");
    vo3.setDescription("Una Vita Da Mediano");
    n3 = new OpenSwingTreeNode(vo3);
    vo4 = new TestVO();
    vo4.setFolderCode("L24");
    vo4.setDescription("Angelo Della Nebbia");
    n4 = new OpenSwingTreeNode(vo4);
    vo5 = new TestVO();
    vo5.setFolderCode("L25");
    vo5.setDescription("Ho Messo Via");
    n5 = new OpenSwingTreeNode(vo5);
    vo6 = new TestVO();
    vo6.setFolderCode("L26");
    vo6.setDescription("Baby è Un Mondo Super");
    n6 = new OpenSwingTreeNode(vo6);
    vo7 = new TestVO();
    vo7.setFolderCode("L27");
    vo7.setDescription("Tu Che Conosci Il Cielo");
    n7 = new OpenSwingTreeNode(vo7);
    nb.add(n1);
    nb.add(n2);
    nb.add(n3);
    nb.add(n4);
    nb.add(n5);
    nb.add(n2);
    nb.add(n7);

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
