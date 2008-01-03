package org.openswing.swing.util.client;

import javax.swing.JComponent;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Interface implemented by combo control, list control and grid control to search items.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public interface SearchControl {


  /**
   * @return the selected index in the input control
   */
  public int getSelectedIndex();


  /**
   * Set the selected index.
   */
  public void setSelectedIndex(int index);


  /**
   * @return total rows count in the input control
   */
  public int getRowCount();


  /**
   * @return the element at the specified index, converted in String format
   */
  public String getValueAt(int index);


  /**
   * @return combo control or list control or grid control
   */
  public JComponent getComponent();


  /**
   * @return <code>true</code> if the input control is in read only mode (so search is enabled), <code>false</code> otherwise
   */
  public boolean isReadOnly();


}
