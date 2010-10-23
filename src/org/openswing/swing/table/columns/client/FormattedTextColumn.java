package org.openswing.swing.table.columns.client;

import java.text.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

import org.openswing.swing.client.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.editors.client.*;
import org.openswing.swing.table.renderers.client.*;


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

  /** formatted text field for cell editor */
  private FormattedTextBox editorTextBox = new FormattedTextBox();

  /** formatted text field for cell renderer */
  private FormattedTextBox renderTextBox = new FormattedTextBox();

  /** formatter controller */
  private FormatterController controller;

  /** component left margin, with respect to component container; defaut value: 2 */
  private int leftMargin = 2;

  /** component right margin, with respect to component container; defaut value: 0 */
  private int rightMargin = 0;

  /** component top margin, with respect to component container; defaut value: 0 */
  private int topMargin = 0;

  /** component bottom margin, with respect to component container; defaut value: 0 */
  private int bottomMargin = 0;


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
      if (editorTextBox != null) {
        editorTextBox.commitEdit();
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
      if (editorTextBox != null) {
        return editorTextBox.getActions();
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
      if (editorTextBox != null) {
        return editorTextBox.getFocusLostBehavior();
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
      if (editorTextBox != null) {
        return editorTextBox.getFormatter();
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
      if (editorTextBox != null) {
        return editorTextBox.getFormatterFactory();
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
//      return editorTextBox.getUIClassID();
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
      if (editorTextBox != null) {
        editorTextBox.invalidEdit();
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
      if (editorTextBox != null) {
        return editorTextBox.isEditValid();
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
      if (editorTextBox != null) {
        editorTextBox.setDocument(doc);
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
      if (editorTextBox != null) {
        editorTextBox.setFocusLostBehavior(behavior);
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
      if (editorTextBox != null) {
        editorTextBox.setFormatter(format);
        renderTextBox.setFormatter(format);
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
      if (editorTextBox != null) {
        editorTextBox.setFormatterFactory(tf);
        renderTextBox.setFormatterFactory(tf);
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
      if (editorTextBox != null) {
        editorTextBox.setInputVerifier(verifier);
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
    return editorTextBox;
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
   * @return component bottom margin, with respect to component container
   */
  public final int getBottomMargin() {
    return bottomMargin;
  }


  /**
   * @return component left margin, with respect to component container
   */
  public final int getLeftMargin() {
    return leftMargin;
  }


  /**
   * @return component right margin, with respect to component container
   */
  public final int getRightMargin() {
    return rightMargin;
  }


  /**
   * @return component top margin, with respect to component container
   */
  public final int getTopMargin() {
    return topMargin;
  }


  /**
   * Set component top margin, with respect to component container.
   * @param topMargin component top margin
   */
  public final void setTopMargin(int topMargin) {
    this.topMargin = topMargin;
  }


  /**
   * Set component right margin, with respect to component container.
   * @param rightMargin component right margin
   */
  public final void setRightMargin(int rightMargin) {
    this.rightMargin = rightMargin;
  }


  /**
   * Set component left margin, with respect to component container.
   * @param leftMargin component left margin
   */
  public final void setLeftMargin(int leftMargin) {
    this.leftMargin = leftMargin;
  }


  /**
   * Set component bottom margin, with respect to component container.
   * @param bottomMargin component bottom margin
   */
  public final void setBottomMargin(int bottomMargin) {
    this.bottomMargin = bottomMargin;
  }


  /**
   * @return TableCellRenderer for this column
   */
  public final TableCellRenderer getCellRenderer(GridController tableContainer,Grids grids) {
    return new FormattedTextTableCellRenderer(
        tableContainer,
        renderTextBox, // create another formatter box, otherwise all renderers and the one cell editor will share the same instance
        leftMargin,
        rightMargin,
        topMargin,
        bottomMargin,
        getTextAlignment(),
        getColumnName()
    );
  }


  /**
   * @return TableCellEditor for this column
   */
  public final TableCellEditor getCellEditor(GridController tableContainer,Grids grids) {
    return new FormattedTextCellEditor(
      getTextBox(),
      isColumnRequired(),
      this
    );
  }


  /**
   * Method invoked by FormattedTextCellEditor, when pressing a key.
   */
  public final void forwardKeyEvent(KeyEvent e) {
    editorTextBox.processKeyEvent(e);
  }


}
