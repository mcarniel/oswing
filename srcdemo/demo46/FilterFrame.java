package demo46;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.customvo.client.*;
import org.openswing.swing.customvo.java.*;
import org.openswing.swing.customvo.server.*;
import java.util.ArrayList;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Frame that contains a filter used to open a grid defined at run time.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class FilterFrame extends JFrame {

  JPanel topPanel = new JPanel();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  private Connection conn = null;
  LabelControl labelSQL = new LabelControl();
  TextAreaControl controlSQL = new TextAreaControl();
  JButton buttonExecute = new JButton();
  private GridFrame frame = null;


  public FilterFrame(Connection conn) {
    super.setDefaultCloseOperation(super.EXIT_ON_CLOSE);
    this.conn = conn;
    try {
      jbInit();
      setSize(750,120);
      setTitle(ClientSettings.getInstance().getResources().getResource("filter panel"));
      controlSQL.setValue("select YEAR,ORDER_NR,CUSTOMER,ORDER_DATE,TOTAL_AMOUNT from ORDERS");
      setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().width/2-350,100);
      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    topPanel.setLayout(gridBagLayout1);
    filterPanel.setLayout(gridBagLayout1);
    buttonExecute.addActionListener(new FilterFrame_buttonExecute_actionAdapter(this));
    topPanel.add(filterPanel,               new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));

    filterPanel.add(labelSQL,                  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(controlSQL,                    new GridBagConstraints(1, 0, 1, 2, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(buttonExecute,                   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonExecute.setText(ClientSettings.getInstance().getResources().getResource("load data"));

    this.getContentPane().add(topPanel,  BorderLayout.CENTER);
  }


  void buttonExecute_actionPerformed(ActionEvent e) {
    String sql = (String)controlSQL.getValue();
    try {
      if (sql!=null && !sql.equals("")) {
        if (frame!=null) {
          frame.setVisible(false);
        }

        // show frame...
        frame = new GridFrame(conn,sql);
        frame.setLocation(this.getLocation().x,this.getLocation().y+this.getHeight());
      }
    }
    catch (Throwable ex) {
      ex.printStackTrace();
    }
  }





}

class FilterFrame_buttonExecute_actionAdapter implements java.awt.event.ActionListener {
  FilterFrame adaptee;

  FilterFrame_buttonExecute_actionAdapter(FilterFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonExecute_actionPerformed(e);
  }
}

