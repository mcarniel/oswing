package demo48;

import org.openswing.swing.lookup.client.*;
import org.openswing.swing.client.*;
import java.awt.*;
import java.awt.event.*;
import org.openswing.swing.message.send.java.FilterWhereClause;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Filter panel.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TaskFilterPanel extends CustomFilterPanel {
  LabelControl labelCod = new LabelControl();
  TextControl controlCod = new TextControl();
  FlowLayout flowLayout1 = new FlowLayout();

  public TaskFilterPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    labelCod.setLabel("taskCode");
    controlCod.setMaxCharacters(20);
    controlCod.setTrimText(true);
    controlCod.setUpperCase(true);
    controlCod.addFocusListener(new TaskFilterPanel_controlCod_focusAdapter(this));
    this.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    this.add(labelCod, null);
    this.add(controlCod, null);
  }


  void controlCod_focusLost(FocusEvent e) {
    if (controlCod.getValue()!=null && !controlCod.getValue().equals("")) {
      FilterWhereClause[] f = new FilterWhereClause[2];
      f[0] = new FilterWhereClause("taskCode","like","%"+controlCod.getValue()+"%");
      getQuickFilterValues().put("taskCode",f);
    }
    else
      getQuickFilterValues().remove("taskCode");
    reload();
  }

}

class TaskFilterPanel_controlCod_focusAdapter extends java.awt.event.FocusAdapter {
  TaskFilterPanel adaptee;

  TaskFilterPanel_controlCod_focusAdapter(TaskFilterPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlCod_focusLost(e);
  }
}
