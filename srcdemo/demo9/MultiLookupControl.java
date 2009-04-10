package demo9;

import java.awt.*;
import org.openswing.swing.client.*;
import javax.swing.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.ValueObject;
import java.util.Collection;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.logger.client.Logger;
import java.util.ArrayList;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel that contains a lookup that allows to select more than one code and stores
 * a list of codes, in order to filter data according to a list of codes.</p>
 * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class MultiLookupControl extends JPanel {


  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private CodLookupControl codLookupControl1 = new CodLookupControl();
  private JButton jButton1 = new JButton() {

    public void paint(Graphics g) {
      super.paint(g);
      int width = g.getFontMetrics().stringWidth("C");
      g.drawString("C",(this.getWidth()-width+1)/2, this.getHeight()/2+4);
    }

  };
  private JScrollPane scrollPaneList = new JScrollPane();
  private DefaultListModel model = new DefaultListModel();
  private JList codesList = new JList(model);
  private LookupController controller = null;
  private String[] lookupAttributeNames = null;
  private String sep = null;
  private ArrayList selectedVOs = new ArrayList();


  public MultiLookupControl() {
    this(new LookupController(),new String[]{""}," - ");
  }


  public MultiLookupControl(LookupController controller,String lookupAttributeName) {
    this(controller,new String[]{lookupAttributeName}," - ");
  }


  public MultiLookupControl(final LookupController controller,String[] lookupAttributeNames,String sep) {
    this.lookupAttributeNames = lookupAttributeNames;
    this.sep = sep;
    try {
      jbInit();
      setController(controller);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }




  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    jButton1.setPreferredSize(new Dimension(20, 20));
    jButton1.setMnemonic('C');
    jButton1.setText("");
    jButton1.addActionListener(new MultiLookupControl_jButton1_actionAdapter(this));
    scrollPaneList.setPreferredSize(new Dimension(100, 100));
    this.add(codLookupControl1,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jButton1,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    this.add(scrollPaneList,      new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    scrollPaneList.getViewport().add(codesList, null);
  }


  void jButton1_actionPerformed(ActionEvent e) {
    selectedVOs.clear();
    model.removeAllElements();
    codesList.revalidate();
    codesList.repaint();
  }


  /**
   * @return list of ValueObjects, related to selected codes
   */
  public ArrayList getSelectedVOs() {
    return selectedVOs;
  }


  public LookupController getController() {
    return controller;
  }


  public void setController(final LookupController controller) {
    this.codLookupControl1.setLookupController(controller);
    this.controller = controller;
    this.controller.setDisableFrameClosing(true);
    this.controller.addLookupListener(new LookupListener() {

      public void beforeLookupAction(ValueObject parentVO) {
      }

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        try {
          StringBuffer sb = new StringBuffer();
          Object value = null;
          for(int i=0;i<lookupAttributeNames.length;i++) {
            value= ClientUtils.getValue(controller.getLookupVO(),lookupAttributeNames[i]);
            sb.append(value).append(sep);
          }
          sb.delete(sb.length()-sep.length(),sb.length());
          model.addElement(sb.toString());
          selectedVOs.add(controller.getLookupVO());
          codesList.revalidate();
          codesList.repaint();
        }
        catch (Exception ex) {
          Logger.error(this.getClass().getName(),"codeChanged",ex.getMessage(),ex);
        }
      }

      public void codeValidated(boolean validated) {
      }

      public void forceValidate() {
      }

    });
  }


}

class MultiLookupControl_jButton1_actionAdapter implements java.awt.event.ActionListener {
  MultiLookupControl adaptee;

  MultiLookupControl_jButton1_actionAdapter(MultiLookupControl adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}
