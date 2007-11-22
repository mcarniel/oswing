package org.openswing.swing.table.columns.client;

import java.text.ParseException;
import org.openswing.swing.client.FormatterController;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.text.Document;
import javax.swing.InputVerifier;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.renderers.client.TextTableCellRenderer;
import org.openswing.swing.table.editors.client.FormattedTextCellEditor;
import org.openswing.swing.table.client.Grids;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Column of type formatted text:
 * it contains a formatted text input field.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class FormattedTextColumn extends Column {

  /** formatted text field */
  private FormattedTextBox textBox = new FormattedTextBox();

  /** formatter controller */
  private FormatterController controller;


  public FormattedTextColumn() { }


  /**
   * @return column type
   */
  public int getColumnType() {
    return TYPE_FORMATTED_TEXT;
  }


// Methods provided by the inner JFormattedTextField


  /**
   * Forces the current value to be taken from the AbstractFormatter and set as the current value.
   */
  public final void commitEdit() throws ParseException {
    try {
      if (textBox != null) {
        textBox.commitEdit();
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return fetches the command list for the editor
   */
  public final Action[] getActions() {
    try {
      if (textBox != null) {
        return textBox.getActions();
      }
      return new Action[0];
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * @return returns the behavior when focus is lost
   */
  public final int getFocusLostBehavior() {
    try {
      if (textBox != null) {
        return textBox.getFocusLostBehavior();
      }
      else {
        return JFormattedTextField.COMMIT_OR_REVERT;
      }
    }
    catch (Exception ex) {
      return JFormattedTextField.COMMIT_OR_REVERT;
    }
  }


  /**
   * @return returns the AbstractFormatter that is used to format and parse the current value
   */
  public final JFormattedTextField.AbstractFormatter getFormatter() {
    try {
      if (textBox != null) {
        return textBox.getFormatter();
      }
      else {
        return null;
      }
    }
    catch (Exception ex) {
      return null;
    }
  }


  /**
   * @return returns the current AbstractFormatterFactory
   */
  public final JFormattedTextField.AbstractFormatterFactory getFormatterFactory() {
    try {
      if (textBox != null) {
        return textBox.getFormatterFactory();
      }
      return null;
    }
    catch (Exception ex) {
      return null;
    }
  }


//  /**
//   * @return gets the class ID for a UI
//   */
//  public final String getUIClassID() {
//    try {
//      return textBox.getUIClassID();
//    }
//    catch (Exception ex) {
//      return null;
//    }
//  }


  /**
   * Invoked when the user inputs an invalid value.
   */
  public final void invalidEdit() {
    try {
      if (textBox != null) {
        textBox.invalidEdit();
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return returns <code>true</code> if the current value being edited is valid
   */
  public final boolean isEditValid() {
    try {
      if (textBox != null) {
        return textBox.isEditValid();
      }
      return false;
    }
    catch (Exception ex) {
      return false;
    }
  }


  /**
   * Associates the editor with a text document.
   * @param doc document linked to this text field
   */
  public final void setDocument(Document doc) {
    try {
      if (textBox != null) {
        textBox.setDocument(doc);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Sets the behavior when focus is lost.
   * @param behavior behavior to set
   */
  public void setFocusLostBehavior(int behavior) {
    try {
      if (textBox != null) {
        textBox.setFocusLostBehavior(behavior);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Sets the current AbstractFormatter.
   * @param format formatter to set
   */
  public final void setFormatter(JFormattedTextField.AbstractFormatter format) {
    try {
      if (textBox != null) {
        textBox.setFormatter(format);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Sets the AbstractFormatterFactory.
   * @param tf formatter factory
   */
  public final void setFormatterFactory(JFormattedTextField.AbstractFormatterFactory tf) {
    try {
      if (textBox != null) {
        textBox.setFormatterFactory(tf);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * Set an input verifier.
   * @param verifier input verifier
   */
  public final void setInputVerifier(InputVerifier verifier) {
    try {
      if (textBox != null) {
        textBox.setInputVerifier(verifier);
      }
    }
    catch (Exception ex) {
    }
  }


  /**
   * @return formatter controller, used to override some JFormatterTextField protected methods
   */
  public final FormatterController getController() {
    return controller;
  }


  /**
   * Set the formatter controller, used to override some JFormatterTextField protected methods.
   * @param controller formatter controller
   */
  public final void setController(FormatterController controller) {
    this.controller = controller;
  }


  /**
   * @return formatted text fiel
   * NOTE: you do NOT have to use this method; it should be used only by cell renderer/editor
   */
  public final FormattedTextBox getTextBox() {
    return textBox;
  }








  /**
   * <p>Title: OpenSwing Framework</p>
   * <p>Description: Inner class used to redirect key event to the inner JFormattedTextField.</p>
   * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class FormattedTextBox extends JFormattedTextField implements FormatterController{


//      public FormattedTextBox() {
//        super();
//        try {
//          setFormatter(new javax.swing.text.MaskFormatter("###"));
//        }
//        catch (ParseException ex) {
//        }
//      }

      public void processKeyEvent(KeyEvent e) {
        try {
          super.processKeyEvent(e);
        }
        catch (Exception ex) {
        }
      }


      /**
       * Invoked when the user inputs an invalid value.
       */
      public void invalidEdit() {
        try {
          if (controller == null) {
            super.invalidEdit();
          }
          else {
            controller.invalidEdit();
          }
        }
        catch (Exception ex) {
        }
      }


      /**
       * Sets the current AbstractFormatter.
       * @param format formatter to set
       */
      public void setFormatter(JFormattedTextField.AbstractFormatter format) {
        try {
          if (controller == null) {
            if (getFormatterFactory()==null)
              super.setFormatterFactory(new DefaultFormatterFactory(format));
            else
              super.setFormatter(format);

          }
          else {
            controller.setFormatter(format);
          }
        }
        catch (Exception ex) {
        }
      }


  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new TextTableCellRenderer(
        tableContainer,
        false,
        getTextAlignment()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new FormattedTextCellEditor(
      getTextBox(),
      isColumnRequired()
    );
  }


}
