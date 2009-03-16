package org.openswing.swing.util.client;

import javax.swing.*;

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


  /**
   * @return <code>true</code> to disable key listening on input control (for instance, in case of nested grids), <code>false</code> to listen for key events
   */
  public boolean disableListener();


  /**
   * Method invoked by SearchWindowManager when the specified "textToSeach" pattern has not matchings in the current content
   * of binded control.
   * This callback can be used to retrieve additional data into control and to search inside new data.
   * @param textToSeach patten of text to search
   * @return -1 if no additional data is available, otherwise the row index of data just added that satify the pattern
   */
  public int search(String textToSeach);


}
