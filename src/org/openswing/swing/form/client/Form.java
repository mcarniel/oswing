package org.openswing.swing.form.client;

import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.form.model.client.*;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.Container;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.form.client.*;

import org.openswing.swing.logger.client.Logger;
import java.beans.Beans;
import org.openswing.swing.util.client.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.util.java.*;
import java.awt.EventQueue;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.JTextComponent;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.message.send.java.GridParams;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Panel used to contains input controls.
 * It can set input control values and retrieve from them the values to set the model (the value object).
 * It can also remotely read data and set the v.o. (and the input controls).</p>
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
public class Form extends JPanel implements DataController,ValueChangeListener,GenericButtonController,ActionListener {

  /** insert button */
  private InsertButton insertButton = null;

  /** copy button */
  private CopyButton copyButton = null;

  /** edit button */
  private EditButton editButton = null;

  /** refresh/cancel button */
  private ReloadButton reloadButton = null;

  /** delete button */
  private DeleteButton deleteButton = null;

  /** save button (for insert/update operations) */
  private SaveButton saveButton = null;

  /** form controller */
  private FormController formController = null;

  /** data model associated to the form */
  private VOModel model = null;

  /** current form mode; default value: Consts.READONLY */
  private int mode = Consts.READONLY;

  /** linked panels, related to this (optional); they will receive pull/store events fired to this */
  private ArrayList linkedPanels = new ArrayList();

  /** v.o. class name linked to this Form panel */
  private String voClassName = null;

  /** flag used to inizialize input controls in addNotify method */
  private boolean firstTime = true;

  /** original value object, cloned when the Form has been setted in EDIT mode */
  private ValueObject previousVO = null;

  /** identifier (functionId) associated to the container */
  private String functionId = null;

  /** Form panel that has currently received focus (used to listen acceleration keys events to dispatch to toolbar buttons of the form */
  private static Form currentFocusedForm = null;

  /** Form border, when it has not the focus */
  private Border notFocusedBorder = null;

  /** generic buttons, that inherit abilitation state of the other form buttons */
  private ArrayList genericButtons = new ArrayList();

  /** collection of input controls attribute names to disable when the specified attribute will be setted to the specified value; pairs of type (attribute name, List of AttributeCouple objects) */
  private Hashtable inputControlsToDisable = new Hashtable();

  /** collection of GenericButton objects to disable when the specified attribute will be setted to the specified value; pairs of type (GenericButton object, List of GenericButtonController objects) */
  private Hashtable buttonsToDisable = new Hashtable();

  /** collection of input controls linked to this form; couples of type: attribute name, List of InputControl objects */
  private Hashtable bindings = new Hashtable();

  /** grid control linked to the current Form (optional) */
  private GridControl grid = null;

  /** navigation bar linked to the current Form (optional); if this property is defined then Form data loading is automatically performed when using navigator bar; also grid selection row is updated */
  private NavigatorBar navBar = null;

  /** attributes that must be defined both on grid v.o. and on the Form v.o. used to select on grid the (first) row that matches pk values */
  private HashSet pkAttributes = null;

  /** flag used to enable Form data reloading when clicking with the left mouse button onto the grid */
  private boolean reloadModelWhenClickingWithMouse;

  /** flag used to enable Form data reloading when pressing up/down keys onto the grid */
  private boolean reloadModelWhenPressingKey;

  /** flag used to define if an inner v.o. must be automatically instantiated when a setter method is invoked; default value: <code>true</code> */
  private boolean createInnerVO = true;


  public Form() {
  }


  /**
   * Static initializer, used to listen accelerator key events to dispatch to the toolbar buttons.
   */
  static {
    if (!Beans.isDesignTime())
      ApplicationEventQueue.getInstance().addKeyListener(new KeyAdapter() {

        public void keyPressed(KeyEvent e) {
          if (currentFocusedForm!=null) {
            if (((KeyEvent)e).getKeyCode() == KeyEvent.VK_Z && ((KeyEvent)e).isControlDown() && (currentFocusedForm.getReloadButton() != null) &&
                currentFocusedForm.getReloadButton().isEnabled()) {
              currentFocusedForm.getReloadButton().requestFocus();
              currentFocusedForm.reload();
            }
            else if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown() & (currentFocusedForm.getSaveButton() != null) &&
                     currentFocusedForm.getSaveButton().isEnabled()) {
              currentFocusedForm.getSaveButton().requestFocus();
              currentFocusedForm.save();
            }
            else if (e.getKeyCode() == KeyEvent.VK_I && e.isControlDown() &&
                     (currentFocusedForm.getInsertButton() != null) && currentFocusedForm.getInsertButton().isEnabled()) {
              currentFocusedForm.getInsertButton().requestFocus();
              currentFocusedForm.insert();
            }
            else if (e.getKeyCode() == KeyEvent.VK_C && e.isControlDown() &&
                     (currentFocusedForm.getCopyButton() != null) && currentFocusedForm.getCopyButton().isEnabled()) {
              currentFocusedForm.getCopyButton().requestFocus();
              currentFocusedForm.copy();
            }
            else if (e.getKeyCode() == KeyEvent.VK_E && e.isControlDown() && (currentFocusedForm.getEditButton() != null) &&
                     currentFocusedForm.getEditButton().isEnabled()) {
              currentFocusedForm.getEditButton().requestFocus();
              currentFocusedForm.edit();
            }
            else if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown() &&
                     (currentFocusedForm.getDeleteButton() != null) && currentFocusedForm.getDeleteButton().isEnabled())
              currentFocusedForm.delete();
          }
        }

      });
  }


  /**
   * Set the current focused form that will receive key events..
   * @param form currently focused form; may be null (if no form is currently focused)
   */
  public static void setCurrentFocusedForm(Form form) {
    if (Beans.isDesignTime())
      return;
    if (form!=null && !form.isShowing())
      return;
    if (currentFocusedForm!=null && !currentFocusedForm.equals(form))
      currentFocusedForm.disableFocusedForm();
    currentFocusedForm = form;
    if (currentFocusedForm!=null)
      currentFocusedForm.setBorder(BorderFactory.createCompoundBorder(
        currentFocusedForm.getNotFocusedBorder(),
        BorderFactory.createLineBorder(ClientSettings.FORM_FOCUS_BORDER,1)
      ));
  }


  /**
   * Set the current focused form that will receive key events..
   * @param form currently focused form; may be null (if no form is currently focused)
   */
  public void disableFocusedForm() {
    if (Beans.isDesignTime())
      return;
    this.setBorder(BorderFactory.createCompoundBorder(
      currentFocusedForm.getNotFocusedBorder(),
      BorderFactory.createLineBorder(this.getBackground(),1)
    ));
  }


  /**
   * Method overridden to initialize input controls.
   */
  public void addNotify() {
    try {
      super.addNotify();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    if (!Beans.isDesignTime() && firstTime) {
      firstTime = false;
      linkInputControls(Form.this.getComponents(),true);

      notFocusedBorder = this.getBorder();
      addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent e) {
          setCurrentFocusedForm(Form.this);
        }
      });

      JInternalFrame parentInternalFrame = getParentJInternalFrame(this);
      if (parentInternalFrame!=null)
          parentInternalFrame.addInternalFrameListener(new InternalFrameAdapter() {

            public void internalFrameActivated(InternalFrameEvent e) {
              if (currentFocusedForm==null)
                setCurrentFocusedForm(Form.this);
            }

            public void internalFrameDeactivated(InternalFrameEvent e) {
              if (Form.this.equals(currentFocusedForm))
                setCurrentFocusedForm(null);
            }

          });
      else {
        JFrame parentFrame = getParentJFrame(this);
        if (parentFrame!=null)
            parentFrame.addWindowListener(new WindowAdapter() {

              public void windowActivated(WindowEvent e) {
                if (currentFocusedForm==null)
                  setCurrentFocusedForm(Form.this);
              }

              public void windowDeactivated(WindowEvent e) {
                if (Form.this.equals(currentFocusedForm))
                  setCurrentFocusedForm(null);
              }

            });
      }

    }
  }


  /**
   * @param comp form which may be contained into a JFrame
   * @return JFrame object which contains the graphic component; null if comp is not inside a JFrame
   */
  private JFrame getParentJFrame(Form comp) {
    Container parentFrame = comp.getParent();
    while(parentFrame!=null) {
      if (parentFrame instanceof JFrame) {
        return (JFrame)parentFrame;
      }
      parentFrame = parentFrame.getParent();
    }
    return null;
  }


  /**
   * @param comp form which may be contained into a JInternalFrame
   * @return JInternalFrame object which contains the graphic component; null if comp is not inside a JInternalFrame
   */
  private JInternalFrame getParentJInternalFrame(Form comp) {
    Container parentFrame = comp.getParent();
    while(parentFrame!=null) {
      if (parentFrame instanceof JInternalFrame) {
        return (JInternalFrame)parentFrame;
      }
      parentFrame = parentFrame.getParent();
    }
    return null;
  }



  /**
   * @return insert button
   */
  public final InsertButton getInsertButton() {
    return insertButton;
  }


  /**
   * @return copy button
   */
  public final CopyButton getCopyButton() {
    return copyButton;
  }



  /**
   * @return edit button
   */
  public final EditButton getEditButton() {
    return editButton;
  }


  /**
   * Set edit button linked to this form.
   * @param editButton edit button
   */
  public final void setEditButton(EditButton editButton) {
    // remove a previous listener...
    if (this.editButton != null)
      this.editButton.removeDataController(this);
    this.editButton = editButton;
    if (editButton != null)
      // add a new listener...
      editButton.addDataController(this);
  }


  /**
   * Set insert button linked to this form.
   * @param insertButton insert button
   */
  public final void setInsertButton(InsertButton insertButton) {
    // remove a previous listener...
    if (this.insertButton != null)
      this.insertButton.removeDataController(this);
    this.insertButton = insertButton;
    if (insertButton != null)
      // add a new listener...
      insertButton.addDataController(this);
  }


  /**
   * Set copy button linked to this form.
   * @param copyButton insert button
   */
  public final void setCopyButton(CopyButton copyButton) {
    // remove a previous listener...
    if (this.copyButton != null)
      this.copyButton.removeDataController(this);
    this.copyButton = copyButton;
    if (copyButton != null)
      // add a new listener...
      copyButton.addDataController(this);
  }


  /**
   * @return refresh/cancel button
   */
  public final ReloadButton getReloadButton() {
    return reloadButton;
  }


  /**
   * Set refresh/cancel button linked to this form. It allows to cancel insert/edit operation and reloads the previous data in the form and set it in READONLY mode.
   * @param ReloadButton refresh/cancel button
   */
  public final void setReloadButton(ReloadButton reloadButton) {
    // remove the previous listener...
    if (this.reloadButton != null)
      this.reloadButton.removeDataController(this);
    this.reloadButton = reloadButton;
    if (reloadButton != null)
      // add a new listener...
      reloadButton.addDataController(this);
  }


  /**
   * @return delete button
   */
  public final DeleteButton getDeleteButton() {
    return deleteButton;
  }


  /**
   * Set delete button linked to this form.
   * @param deleteButton delete button
   */
  public final void setDeleteButton(DeleteButton deleteButton) {
    // remove the previous listener...
    if (this.deleteButton != null)
      this.deleteButton.removeDataController(this);
    this.deleteButton = deleteButton;
    if (deleteButton != null)
      // add a new listener...
      deleteButton.addDataController(this);
  }

  /**
   * @return save button
   */
  public final SaveButton getSaveButton() {
    return saveButton;
  }


  /**
   * Set save button linked to this form. It allows to create an insert/edit sql instruction
   * @param saveButton save button
   */
  public final void setSaveButton(SaveButton saveButton) {
    // remove the previous listener...
    if (this.saveButton != null)
      this.saveButton.removeDataController(this);
    this.saveButton = saveButton;
    if (saveButton != null)
      // add a new listener...
      saveButton.addDataController(this);
  }


  /**
   * Method called from refresh/cancel button: it reload the v.o. (in another thread).
   */
  private void reloadData() {
    new Thread() {
      public void run() {
        boolean errorOnLoad = true;
        // it will be used another thread to do not block the rest of the application (the mani graphical thread)...
        if (reloadButton!=null)
          reloadButton.setEnabled(false);
        if (insertButton!=null)
          insertButton.setEnabled(false);
        if (copyButton!=null)
          copyButton.setEnabled(false);
        if (editButton!=null)
          editButton.setEnabled(false);
        if (deleteButton!=null)
          deleteButton.setEnabled(false);
        if (saveButton!=null)
          saveButton.setEnabled(false);

        for(int i=0;i<genericButtons.size();i++)
          ((GenericButton)genericButtons.get(i)).setEnabled(false);

        // reload data...
        errorOnLoad = ! loadData();
        if (reloadButton!=null)
          reloadButton.setEnabled(reloadButton.getOldValue());
        if (saveButton!=null)
          saveButton.setEnabled(saveButton.getOldValue());

        resetButtonsState();

      }
    }.start();
  }


  /**
   * Reload data and set the input controls.
   */
  private boolean loadData() {
    try {
      // data loading...
      Response answer = formController.loadData(model.getValueObjectType());

      // set the form model with data just loaded...
      if (!answer.isError()) {
        model.setValueObject( (ValueObject)((VOResponse)answer).getVo() );
        previousVO = getVOModel().getValueObject();
       }
      else
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource("Error while loading data:")+"\n"+
            ClientSettings.getInstance().getResources().getResource(answer.getErrorMessage()),
            ClientSettings.getInstance().getResources().getResource("Error on Loading"),
            JOptionPane.WARNING_MESSAGE
        );


      // toolbar updating...
      if (insertButton!=null)
        insertButton.setEnabled(true);
      if (copyButton!=null)
        copyButton.setEnabled(true);
      if (editButton!=null)
        editButton.setEnabled(true);
      if (deleteButton!=null)
        deleteButton.setEnabled(true);

      for(int i=0;i<genericButtons.size();i++)
        ((GenericButton)genericButtons.get(i)).setEnabled(true);

      // set input control values...
      pull();

      // events firing (for example "load data completed" event)...
      if (!answer.isError()) {
        formController.loadDataCompleted(false);
        resetButtonsState();
        return true;
      }
      else {
        formController.loadDataCompleted(true);
        resetButtonsState();
        return false;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
//      MDIFrame.setStatusBar("");
//      MDIFrame.setBusy(false);
    }
    return false;
  }


  /**
   * Set current form mode and consequently the input controls editing state.
   * @param mode form mode; possible values: READONLY, INSERT, EDIT
   */
  public final void setMode(int mode) {
    if (Beans.isDesignTime())
      return;
    if (mode!=Consts.READONLY && mode!=Consts.EDIT && mode!=Consts.INSERT)
      throw new UnsupportedOperationException("Mode not supported");
    this.mode = mode;
    if (mode==Consts.INSERT) {
      try {
        if (!this.formController.beforeInsertData(this))
          return;

        ValueObject vo = (ValueObject)model.getValueObjectType().newInstance();
        model.setValueObject( vo );

        // callback used to set default values in input controls...
        formController.createPersistentObject(vo);
        pull();

        Component[] c = this.getComponents();
        setEnabled(c,true);

        // set input controls edit state in linked panels...
        Container container = null;
        for(int i=0;i<linkedPanels.size();i++) {
          container = (Container)linkedPanels.get(i);
          setEnabled(container.getComponents(),true);
        }

        // set focus to the first editable input control...
        int i=0;
        while(i<c.length && !c[i].isEnabled())
          i++;
        if (i<c.length)
          c[i].requestFocus();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      catch (Error er) {
        er.printStackTrace();
      }

      this.formController.afterInsertData(this);

      if (insertButton!=null)
        insertButton.setEnabled(false);
      if (copyButton!=null)
        copyButton.setEnabled(false);
      if (deleteButton!=null)
        deleteButton.setEnabled(false);
      if (editButton!=null)
        editButton.setEnabled(false);

      for(int i=0;i<genericButtons.size();i++)
        ((GenericButton)genericButtons.get(i)).setEnabled(false);

      if (reloadButton!=null)
        reloadButton.setEnabled(true);
      if (saveButton!=null)
        saveButton.setEnabled(true);

      resetButtonsState();
    } else if (mode==Consts.READONLY) {
      Component[] c = this.getComponents();
      setEnabled(c,false);

      // set input controls edit state in linked panels...
      Container container = null;
      for(int i=0;i<linkedPanels.size();i++) {
        container = (Container)linkedPanels.get(i);
        setEnabled(container.getComponents(),false);
      }

      if (insertButton!=null)
        insertButton.setEnabled(insertButton.getOldValue());
      if (copyButton!=null)
        copyButton.setEnabled(copyButton.getOldValue());
      if (deleteButton!=null)
        deleteButton.setEnabled(deleteButton.getOldValue());
      if (editButton!=null)
        editButton.setEnabled(editButton.getOldValue());

      for(int i=0;i<genericButtons.size();i++)
        ((GenericButton)genericButtons.get(i)).setEnabled(((GenericButton)genericButtons.get(i)).getOldValue());

      if (reloadButton!=null)
        reloadButton.setEnabled(true);
      if (saveButton!=null)
        saveButton.setEnabled(false);

      resetButtonsState();
    } else if (mode==Consts.EDIT) {
      if (!this.formController.beforeEditData(this))
        return;

      Component[] c = this.getComponents();
      setEnabled(c,true);

      // set input controls edit state in linked forms...
      Container container = null;
      for(int i=0;i<linkedPanels.size();i++) {
        container = (Container)linkedPanels.get(i);
        setEnabled(container.getComponents(),true);
      }

      try {
        previousVO = (ValueObject)( (ValueObject) model.getValueObject()).clone();
      }
      catch (CloneNotSupportedException ex1) {
        Logger.error(this.getClass().getName(), "setMode", "Error while duplicating the value object",ex1);
      }

      // set focus to the first editable input control...
      int i=0;
      while(i<c.length && !c[i].isEnabled())
        i++;
      if (i<c.length)
        c[i].requestFocus();

      this.formController.afterEditData(this);

      if (insertButton!=null)
        insertButton.setEnabled(false);
      if (copyButton!=null)
        copyButton.setEnabled(false);
      if (deleteButton!=null)
        deleteButton.setEnabled(false);
      if (editButton!=null)
        editButton.setEnabled(false);

      for(i=0;i<genericButtons.size();i++)
        ((GenericButton)genericButtons.get(i)).setEnabled(false);

      if (reloadButton!=null)
        reloadButton.setEnabled(true);
      if (saveButton!=null)
        saveButton.setEnabled(true);

      resetButtonsState();

    }

    // reset changed property for all linked input controls...
    Enumeration en = bindings.keys();
    String attrName = null;
    ArrayList list = null;
    while(en.hasMoreElements()) {
      attrName = en.nextElement().toString();
      list = (ArrayList)bindings.get(attrName);
      for(int i=0;i<list.size();i++)
        ((InputControl)list.get(i)).setChanged(false);
    }

    if (formController!=null)
      formController.modeChanged(mode);

  }


  /**
   * Set input controls edit state.
   * @param c components whose edit state must be set
   * @param enabled edit state
   */
  private void setEnabled(Component[] c,boolean enabled) {
    boolean canEdit;
    for(int i=0;i<c.length;i++) {
      if (c[i] instanceof JScrollPane) {
        setEnabled( ( (JScrollPane) c[i]).getViewport().getComponents(), enabled);
        continue;
      }
      if (c[i] instanceof JTabbedPane) {
        setEnabled( ( (Container) c[i]).getComponents(), enabled);
        continue;
      }
      if (c[i] instanceof Container)
        setEnabled( ( (Container) c[i]).getComponents(), enabled);
      if (! (c[i] instanceof JLabel || c[i] instanceof JScrollPane)) {
        if (c[i] instanceof InputControl && enabled) {
          if (((InputControl)c[i]).isEnabledOnInsert() && getMode()==Consts.INSERT) {
            canEdit = isInputControlEnabled(enabled,(InputControl)c[i]);
            c[i].setEnabled( canEdit );
            if (canEdit &&
                c[i] instanceof ComboBoxControl &&
                !((ComboBoxControl)c[i]).isNullAsDefaultValue() &&
                ((ComboBoxControl)c[i]).getComboBox().getSelectedIndex()==-1)
              ((ComboBoxControl)c[i]).getComboBox().setSelectedIndex(0);
          }
          else if (((InputControl)c[i]).isEnabledOnEdit() && getMode()==Consts.EDIT)
            c[i].setEnabled( isInputControlEnabled(enabled,(InputControl)c[i]) );
          else if(enabled && (getMode()==Consts.INSERT || getMode()==Consts.EDIT))
            c[i].setEnabled(false);
        }
        else
          c[i].setEnabled(enabled);
      }
    }
  }


  /**
   * Method called by setEnabled method to check if input control must be disabled,
   * independently from enabledOnInsert/Edit settings.
   * @return <code>true</code> if input control does not match any input controls disabilitation policies, <code>false</code> if there exist a policy that matches
   */
  private boolean isInputControlEnabled(boolean enabled,InputControl control) {
    if (!enabled)
      return enabled;
    if (control.getAttributeName()==null)
      return enabled;
    ArrayList list = (ArrayList)inputControlsToDisable.get(control.getAttributeName());
    AttributeCouple ac = null;
    Object stateValue = null;
    if (list!=null)
      for(int i=0;i<list.size();i++) {
        ac = (AttributeCouple)list.get(i);
        stateValue = model.getValue(ac.getAttributeName());
        if (stateValue!=null && ac.getAttributeValue()!=null && stateValue.equals(ac.getAttributeValue()) ||
            stateValue==null && ac.getAttributeValue()==null)
          return false;
      }

    return enabled;
  }


  /**
   * Define input controls attribute names that will be disabled (independently from enabledOnInsert/Edit settings)
   * when the specified attribute will be set to the specified value.
   * @param attributeNames collections of attribute names related to input controls to disable
   * @param stateAttributeName attribute name in the form value object to test
   * @param state attribute value in the form value object to test: if the value is this one, then input controls will be disabled (in INSERT/EDIT modes)
   */
  public final void addInputControlAttributesNotEditableOnState(HashSet attributeNames,String stateAttributeName,Object state) {
    AttributeCouple ac = new AttributeCouple(stateAttributeName,state);
    Iterator it = attributeNames.iterator();
    String aName = null;
    ArrayList list = null;
    while(it.hasNext()) {
      aName = it.next().toString();
      list = (ArrayList)inputControlsToDisable.get(aName);
      if (list==null) {
        list = new ArrayList();
        inputControlsToDisable.put(aName,list);
      }
      list.add(ac);
    }
  }


  /**
   * Define GenericButton objects linked to this that will be disabled (independently from the form mode)
   * when the specified attribute will be set to the specified value.
   * @param buttons collections GenericButton objects linked to this that have to be disabled
   * @param buttonController interface that defines button disabilitation, according to some custom policy
   */
  public final void addButtonsNotEnabled(HashSet buttons,GenericButtonController buttonController) {
    Iterator it = buttons.iterator();
    GenericButton genericButton = null;
    ArrayList list = null;
    while(it.hasNext()) {
      genericButton = (GenericButton)it.next();
      list = (ArrayList)buttonsToDisable.get(genericButton);
      if (list==null) {
        list = new ArrayList();
        buttonsToDisable.put(genericButton,list);
      }
      list.add(buttonController);
    }
  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form method)
   */
  public final boolean isButtonDisabled(GenericButton button) {
    ArrayList list = (ArrayList)buttonsToDisable.get(button);
    if (list!=null) {
      GenericButtonController buttonController = null;
      for(int i=0;i<list.size();i++) {
        buttonController = (GenericButtonController)list.get(i);
        if (buttonController.isButtonDisabled(button))
          return true;
      }
    }
    return false;
  }


  /**
   * Method automatically called by the Form to check buttons disabilitation.
   */
  public final void resetButtonsState() {
    if (insertButton!=null && isButtonDisabled(insertButton))
      insertButton.setEnabled(false);
    if (copyButton!=null && isButtonDisabled(copyButton))
      copyButton.setEnabled(false);
    if (editButton!=null && isButtonDisabled(editButton))
      editButton.setEnabled(false);
    if (reloadButton!=null && isButtonDisabled(reloadButton))
      reloadButton.setEnabled(false);
    if (saveButton!=null && isButtonDisabled(saveButton))
      saveButton.setEnabled(false);
    if (deleteButton!=null && isButtonDisabled(deleteButton))
      deleteButton.setEnabled(false);
  }



  /**
   * @return current form mode
   */
  public final int getMode() {
    return mode;
  }


  /**
   * Method called by refresh/cancel button.
   */
  public final void reload() {
    if (getMode()!=Consts.READONLY) {
      // show message dialog to confirm the refresh/cancel operation...
      if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                    ClientSettings.getInstance().getResources().getResource("Cancel changes and reload data?"),
                                    ClientSettings.getInstance().getResources().getResource("Attention"),
                                    JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
        executeReload();
    } else if (getMode()==Consts.READONLY) {
      reloadData();
    }
  }


  /**
   * This method is called by reload method to reload data.
   * Developer can explicity call this method to force data model reloading.
   * @return <code>true</code> if data loading is succesfully completed, <code>false</code> if an error occours
   */
  public final boolean executeReload() {
    try {
      setMode(Consts.READONLY);
      reloadData();
      super.repaint();
      if (saveButton!=null)
        saveButton.setEnabled(false);

      // set toolbar buttons state in the state previous to the insert/edit.
      if (editButton!=null)
        editButton.setEnabled(editButton.getOldValue());
      if (deleteButton!=null)
        deleteButton.setEnabled(deleteButton.getOldValue());
      if (insertButton!=null)
        insertButton.setEnabled(insertButton.getOldValue());
      if (copyButton!=null)
        copyButton.setEnabled(copyButton.getOldValue());

      for(int i=0;i<genericButtons.size();i++)
        ((GenericButton)genericButtons.get(i)).setEnabled(((GenericButton)genericButtons.get(i)).getOldValue());

      // fire event...
      formController.afterReloadData();

      resetButtonsState();
      return true;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }


  /**
   * Method called by insert button.
   */
  public final void insert() {
    if (getMode()==Consts.READONLY) {
      if (!this.formController.beforeInsertData(this))
        return;

      setMode(Consts.INSERT);
//      if (insertButton!=null)
//        insertButton.setEnabled(false);
//      if (copyButton!=null)
//        copyButton.setEnabled(false);
//      if (deleteButton!=null)
//        deleteButton.setEnabled(false);
//      if (editButton!=null)
//        editButton.setEnabled(false);
//
//      for(int i=0;i<genericButtons.size();i++)
//        ((GenericButton)genericButtons.get(i)).setEnabled(false);
//
//      if (reloadButton!=null)
//        reloadButton.setEnabled(true);
//      if (saveButton!=null)
//        saveButton.setEnabled(true);
//
//      resetButtonsState();
    }
    else
      Logger.error(this.getClass().getName(), "insert", "The form is not in READONLY mode: operation not allowed.",null);
  }


  /**
   * Method called by copy button.
   */
  public final void copy() {
    if (getMode()==Consts.READONLY) {
      try {
        // duplicate the current v.o...
        ValueObject vo = (ValueObject) ( (ValueObject) model.getValueObject()).clone();

        // create a new empty v.o...
        setMode(Consts.INSERT);
        if (insertButton != null) {
          insertButton.setEnabled(false);
        }
        if (copyButton != null) {
          copyButton.setEnabled(false);
        }
        if (deleteButton != null) {
          deleteButton.setEnabled(false);
        }
        if (editButton != null) {
          editButton.setEnabled(false);
        }

        for(int i=0;i<genericButtons.size();i++)
          ((GenericButton)genericButtons.get(i)).setEnabled(false);

        if (reloadButton != null) {
          reloadButton.setEnabled(true);
        }
        if (saveButton != null) {
          saveButton.setEnabled(true);
        }

        // fill in the new v.o. with the duplicable attributes of the cloned v.o...
        copyInputControlValues(vo,this,this.getComponents(),true);
        pull();

        resetButtonsState();
      }
      catch (Throwable ex) {
        Logger.error(this.getClass().getName(), "copy", "Error while duplicating the Form content",ex);
      }
    }
    else
      Logger.error(this.getClass().getName(), "copy", "The form is not in READONLY mode: operation not allowed.",null);
  }


  /**
   * Copy the value contained in all duplicable input controls.
   * @param form form that contains all input controls
   * @param c components added to the container
   */
  private void copyInputControlValues(ValueObject vo,Form form,Component[] c,boolean evalLinkedForm) throws Throwable {
    String attributeName = null;
    Object attributeValue = null;
    for(int i=0;i<c.length;i++)
      if (c[i] instanceof InputControl) {
          attributeName = ((InputControl)c[i]).getAttributeName();
          if (attributeName==null)
            continue;
          if (((InputControl)c[i]).isCanCopy()) {
            try {
            attributeValue = vo.getClass().getMethod("get"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]).invoke(vo,new Object[0]);
            }
            catch (NoSuchMethodException ex) {
              attributeValue = vo.getClass().getMethod("is"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]).invoke(vo,new Object[0]);
            }
            model.setValue(attributeName,attributeValue);
          }
      }
      else if (c[i] instanceof Container)
        copyInputControlValues(vo,form,((Container)c[i]).getComponents(),false);

    Container container = null;
    if (evalLinkedForm)
      for(int i=0;i<linkedPanels.size();i++) {
        container = (Container)linkedPanels.get(i);
        copyInputControlValues(vo,form,container.getComponents(),false);
      }
  }


  /**
   * Method called by edit button.
   */
  public final void edit() {
    if (getMode()==Consts.READONLY) {
      if (!this.formController.beforeEditData(this))
        return;

      setMode(Consts.EDIT);
//      if (insertButton!=null)
//        insertButton.setEnabled(false);
//      if (copyButton!=null)
//        copyButton.setEnabled(false);
//      if (deleteButton!=null)
//        deleteButton.setEnabled(false);
//      if (editButton!=null)
//        editButton.setEnabled(false);
//
//      for(int i=0;i<genericButtons.size();i++)
//        ((GenericButton)genericButtons.get(i)).setEnabled(false);
//
//      if (reloadButton!=null)
//        reloadButton.setEnabled(true);
//      if (saveButton!=null)
//        saveButton.setEnabled(true);
//
//      resetButtonsState();
    }
    else
      Logger.error(this.getClass().getName(), "edit", "The form is not in READONLY mode: operation not allowed.",null);
  }


  /**
   * Method called by delete button.
   */
  public final void delete() {
    if (getMode()==Consts.READONLY) {
      if (!this.formController.beforeDeleteData(this))
        return;

      if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                    ClientSettings.getInstance().getResources().getResource("Confirm deliting data?"),
                                    ClientSettings.getInstance().getResources().getResource("Attention"),
                                    JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
        try {
          Response response = this.formController.deleteRecord((ValueObject)model.getValueObject());
          if (!response.isError()) {

            int gridRowIndex = -1;
            if (grid!=null && pkAttributes!=null) {
              gridRowIndex = getRowIndexInGrid();
            }

            insert();
            //funzione call back avvenuta cancellazione dati
            this.formController.afterDeleteData();

            // if a grid has been linked to this Form then update automatically its content:
            // the removed record will be dropped from the grid...
            if (grid!=null && pkAttributes!=null) {
              if (gridRowIndex!=-1)
                grid.getVOListTableModel().removeObjectAt(gridRowIndex);
            }

          }
          else JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              ClientSettings.getInstance().getResources().getResource("Error on deleting:")+"\n"+
              ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Deleting Error"),
              JOptionPane.WARNING_MESSAGE
          );

        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    else
      Logger.error(this.getClass().getName(), "delete", "The form is not in READONLY mode: operation not allowed.",null);
  }


  /**
   *
   * @return boolean value indicating whether or not all fields in this form have a valid state
   * i.e. if an input control is mandatory and has a null value then is not in a valid state
   */
  public final ArrayList getInputControlsNotValid() {
    ArrayList inputControlsNotValid = new ArrayList();
    ArrayList list = null;
    String attrName = null;
    InputControl comp = null;
    Enumeration en = bindings.keys();
    ArrayList models = new ArrayList();
    Object value = null;
    while(en.hasMoreElements()) {
      attrName = en.nextElement().toString();
      list = (ArrayList)bindings.get(attrName);
      if (list!=null)
        for(int i=0;i<list.size();i++) {
          comp = (InputControl)list.get(i);
          if (comp instanceof FormattedTextControl &&
              !((FormattedTextControl)comp).isEditValid()) {
            // input control is a formatted text control and its context is invalid...
            if (comp.getLinkLabel()!=null)
              inputControlsNotValid.add( comp.getLinkLabel().getText() );
            else
              inputControlsNotValid.add( comp.getAttributeName() );
          }
          else {
            value = comp.getValue();
            if ((value==null || value.equals("")) && comp.isRequired()) {
              if (comp.getLinkLabel()!=null)
                inputControlsNotValid.add( comp.getLinkLabel().getText() );
              else
                inputControlsNotValid.add( comp.getAttributeName() );
            }
          }
        }
    }
    return inputControlsNotValid;
  }


  /**
   * If all input controls linked to the form are in a valid state, then push each value to its associated data model.
   * @return boolean indicating whether or not the input control values were successfully pushed to the data model
   */
  public final boolean push() {
    ArrayList inputControlsNotValid = getInputControlsNotValid();
    if (inputControlsNotValid.size()>0) {
      String list = "";
      for(int i=0;i<inputControlsNotValid.size();i++)
        list += inputControlsNotValid.get(i)+", ";
      list = list.substring(0,list.length()-2);
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("Error while saving: incorrect data.")+"\n"+list,
          ClientSettings.getInstance().getResources().getResource("Saving Error"),
          JOptionPane.WARNING_MESSAGE
      );
      return false;
    }

    // retrieve the value from each linked input control...
    boolean result = true;
    ArrayList list = null;
    String attrName = null;
    InputControl comp = null;
    Enumeration en = bindings.keys();
    ArrayList models = new ArrayList();
    while(en.hasMoreElements()) {
      attrName = en.nextElement().toString();
      list = (ArrayList)bindings.get(attrName);
      if (list!=null)
        for(int i=0;i<list.size();i++) {
          comp = (InputControl)list.get(i);
          try {
            model.setValue(comp.getAttributeName(), comp.getValue());
          }
          catch (Exception ex) {
            JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(this),
                ClientSettings.getInstance().getResources().getResource("Error while saving: incorrect data.")+"\n"+comp.getAttributeName(),
                ClientSettings.getInstance().getResources().getResource("Saving Error"),
                JOptionPane.WARNING_MESSAGE
            );
            result = false;
          }
        }
    }

    return result;
  }


  /**
   * Set the content for each linked input control, based on value object content.
   * @return boolean indicating whether or not the input controls are all correctly setted
   */
  public final boolean pull() {
    boolean result = true;
    String attrName = null;
    Enumeration en = bindings.keys();
    while(en.hasMoreElements()) {
      attrName = en.nextElement().toString();
      result = result && pull(attrName);
    }
    return result;
  }


  /**
   * Set the content for the specified linked input control, based on value object content.
   * @return boolean indicating whether or not the input control has been correctly setted, according to the specified attribute name
   */
  public final boolean pull(String attributeName) {
    boolean result = true;
    ArrayList list = null;
    String attrName = null;
    InputControl comp = null;

    Class clazz = model.getAttributeType(attributeName);
    if (clazz!=null && ValueObject.class.isAssignableFrom(clazz)) {
      // if the value just changed is an inner value object, then all its attributes are managed as changed:
      // check if there are input controls binded to these changed attributes and for each one "pull" it...
      Enumeration en = bindings.keys();
      while(en.hasMoreElements()) {
        attrName = en.nextElement().toString();
        if(attrName.indexOf(attributeName+'.')!=-1)
          pull(attrName);
      }
    }


    list = (ArrayList)bindings.get(attributeName);
    if (list!=null) {
      for(int i=0;i<list.size();i++) {
        comp = (InputControl)list.get(i);
        try {
          comp.setValue( model.getValue(comp.getAttributeName()) );
        }
        catch (Exception ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              ClientSettings.getInstance().getResources().getResource("Error on setting value to the input control having the attribute name")+ " '"+comp.getAttributeName()+"'\n"+ex.getMessage(),
              ClientSettings.getInstance().getResources().getResource("Saving Error"),
              JOptionPane.WARNING_MESSAGE
          );
          result = false;
        }
      }
      return result;
    }
    else
      return false;
  }



  /**
   * Method called by save button.
   * If an error occours while saving data, an error message is showed.
   * @return <code>true</code> if data saving is successfully completed, <code>false</code> if an error occours
   */
  public final boolean save() {
    int previousMode; // previous form mode (before saving data)...
    if (getMode()!=Consts.READONLY) {
      try {
        // input controls data validation...
        if ( !push() ) {
          return false;
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      try {
        //execute insert/edit operation...
        previousMode = getMode();
        Response response = null;
        if (getMode()==Consts.INSERT)
          response = formController.insertRecord((ValueObject)model.getValueObject());
        else if (getMode()==Consts.EDIT)
          response = formController.updateRecord(previousVO,(ValueObject)model.getValueObject());
        if (!response.isError()) {
          model.setValueObject( (ValueObject)((VOResponse)response).getVo() );
          if (getMode()==Consts.INSERT)
            previousVO = getVOModel().getValueObject();

          // reset input controls value according to value object content (which could be filled on the server side)
          pull();

          setMode(Consts.READONLY);
          if (saveButton!=null)
            saveButton.setEnabled(false);

          // set toolbar buttons to the previous state...
          if (editButton!=null)
            editButton.setEnabled(true);
          if (deleteButton!=null)
            deleteButton.setEnabled(true);
          if (insertButton!=null)
            insertButton.setEnabled(true);
          if (copyButton!=null)
            copyButton.setEnabled(true);

          for(int i=0;i<genericButtons.size();i++)
            ((GenericButton)genericButtons.get(i)).setEnabled(true);

          // fire event...
          switch (previousMode) {
            case Consts.INSERT:
              this.formController.afterInsertData();
              break;
            case Consts.EDIT:
              this.formController.afterEditData();
              break;
          }

          // reset changed property for all linked input controls...
          Enumeration en = bindings.keys();
          String attrName = null;
          ArrayList list = null;
          while(en.hasMoreElements()) {
            attrName = en.nextElement().toString();
            list = (ArrayList)bindings.get(attrName);
            for(int i=0;i<list.size();i++)
              ((InputControl)list.get(i)).setChanged(false);
          }

          resetButtonsState();

          // if a grid has been linked to this Form then update automatically its content:
          // the new/updated row will be reloaded and set into the grid...
          if (grid!=null && pkAttributes!=null) {
            Map filters = new HashMap();
            String pkAttrName = null;
            Iterator it = pkAttributes.iterator();
            FilterWhereClause[] filter = null;
            while(it.hasNext()) {
              pkAttrName = it.next().toString();
              filter = new FilterWhereClause[2];

              filter[0] = new FilterWhereClause(pkAttrName,Consts.EQ,model.getValue(pkAttrName));
              filters.put(pkAttrName,filter);
            }
            Response res = grid.getGridDataLocator().loadData(
                GridParams.NEXT_BLOCK_ACTION,
                0,
                filters,
                new ArrayList(),
                new ArrayList(),
                Class.forName(grid.getValueObjectClassName()),
                grid.getOtherGridParams()
            );
            if (res.isError())
              Logger.error(this.getClass().getName(), "save", "Error while loading new row for grid:\n"+res.getErrorMessage(),null);
            else {
              ArrayList rows = ((VOListResponse)res).getRows();
              if (rows.size()==1) {
                final int gridRowIndex = getRowIndexInGrid();
                if (gridRowIndex==-1) {
                  grid.getVOListTableModel().addObject((ValueObject)rows.get(0));
                  grid.repaint();
                  SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                      grid.setRowSelectionInterval(grid.getVOListTableModel().getRowCount()-1,grid.getVOListTableModel().getRowCount()-1);
                    }
                  });
                }
                else {
                  grid.getVOListTableModel().updateObjectAt((ValueObject)rows.get(0),gridRowIndex);
                  grid.repaint();
                  SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                      grid.setRowSelectionInterval(gridRowIndex,gridRowIndex);
                    }
                  });
                }
              }
            }

          }
          previousVO = getVOModel().getValueObject();
          return true;
        } else {
          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              ClientSettings.getInstance().getResources().getResource("Error while saving: incorrect data.")+"\n"+
              ClientSettings.getInstance().getResources().getResource(response.getErrorMessage()),
              ClientSettings.getInstance().getResources().getResource("Saving Error"),
              JOptionPane.WARNING_MESSAGE
          );
          return false;
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }
    }
    else {
      Logger.error(this.getClass().getName(), "save", "The form is in READONLY mode: operation not allowed.",null);
      return false;
    }
  }


  /**
   * @return form controller
   */
  public final FormController getFormController() {
    return formController;
  }


  /**
   * Set form controller.
   * @param formController form controller
   */
  public final void setFormController(FormController formController) {
    this.formController = formController;
  }


  /**
   * @return data model linked to the form
   */
  public final String getVOClassName() {
    try {
      if (Beans.isDesignTime())
        AttributeNameEditor.setDesignVOClass(Class.forName(voClassName));
    }
    catch (Throwable ex) {
    }
    return this.voClassName;
  }


  /**
   * Set data model to link to the form.
   * @param model data model
   */
  public final void setVOClassName(String voClassName) {
    this.voClassName = voClassName;
    try {
      if (Beans.isDesignTime())
        AttributeNameEditor.setDesignVOClass(Class.forName(voClassName));
    }
    catch (Throwable ex) {
    }

    try {
      this.model = new VOModel(Class.forName(voClassName),createInnerVO,this);
    }
    catch (Throwable ex) {
      Logger.error(this.getClass().getName(), "setVOClassName", "Error on setting Form data model:\n"+ex.toString(),null);
    }
  }


//  /**
//   * Set data model to link to the form.
//   * This method is ONLY called by setLinkedForm.
//   * @param model data model
//   */
//  public final void setVOModel(VOModel model) {
//    this.voClassName = model.getValueObjectType().getName();
//    try {
//      this.model = model;
//      if (linkedForm!=null) {
//        linkedForm.setVOModel(model);
//      }
//    }
//    catch (Exception ex) {
//      Logger.error(this.getClass().getName(), "setVOModel", "Error on setting Form data model:\n"+ex.toString(),null);
//    }
//  }


  /**
   * @return model associated to this Form panel
   */
  public final VOModel getVOModel() {
    return model;
  }


  /**
   * Bind input controls added to the specified container:
   * only input controls of type InputControl that have an attribute name defined are linked.
   * @param form form to use to link the input controls
   * @param c components added to the container
   */
  private void linkInputControls(Component[] c,boolean evalLinkedForm) {
    for(int i=0;i<c.length;i++)
      if (c[i] instanceof InputControl)
        try {
          if (((InputControl)c[i]).getAttributeName()==null)
            // input control not linkable: it has not defined the attribute name property...
            continue;
          try {
            // bind input control...
            bind((InputControl)c[i]);
          }
          catch (Exception ex) {
            Logger.error(this.getClass().getName(), "linkInputControls", "Error while linking the input control having attribute name '"+((InputControl)c[i]).getAttributeName()+"'",ex);
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      else if (c[i] instanceof Container)
        linkInputControls(((Container)c[i]).getComponents(),false);

      if (evalLinkedForm) {
        Container container = null;
        for(int i=0;i<linkedPanels.size();i++) {
          container = (Container)linkedPanels.get(i);
          linkInputControls(container.getComponents(),false);
        }
      }

      revalidate();
      repaint();
  }



  /**
   * Remove an added linked panel.
   */
  public final void removeLinkedPanel(Container c) {
    linkedPanels.remove(c);

    Enumeration en = bindings.keys();
    String attrName = null;
    ArrayList list = null;
    while(en.hasMoreElements()) {
      attrName = en.nextElement().toString();
      list = (ArrayList)bindings.get(attrName);
      if (list!=null)
        for(int i=0;i<list.size();i++) {
          try {
            unbind( (InputControl) list.get(i) );
          }
          catch (Exception ex) {
            Logger.error(this.getClass().getName(), "removeLinkedPanel", "Error while unbinding the input control having attribute name '"+((InputControl)list.get(i)).getAttributeName()+"'",ex);
          }
        }
    }
    linkInputControls(this.getComponents(),true);
  }


  /**
   * Add a linked panel, that will receive the same events of this form (in pull/store operations).
   * @param c container to link
   */
  public final void addLinkedPanel(Container c) {
    if (linkedPanels.contains(c))
      return;
    linkedPanels.add(c);
    if (!firstTime)
      linkInputControls(c.getComponents(),false);
  }


  /**
   * @param c panel to check
   * @return <code>true</code> if "panel" is a linked panel, <code>false</code> otherwise
   */
  public final boolean containsLinkedPanel(Container c) {
    return linkedPanels.contains(c);
  }


  /**
   * Create a binding between the specified input control and this data model
   * @param comp input control
   * @throws Exception if an error occours
   */
  public final void bind(InputControl comp) throws Exception {
    if (comp.getAttributeName()==null)
      throw new Exception("No attribute name defined for the specified input control");

    ArrayList list = (ArrayList)bindings.get(comp.getAttributeName());
    if (list==null) {
      list = new ArrayList();
      bindings.put(comp.getAttributeName(),list);
    }
    if (!list.contains(comp))
      // input control NOT yet linked...
      list.add(comp);

    comp.addValueChangedListener(this);
  }


  /**
   * Remove an existing binding between the specified input control and this data model
   * @param comp input control to unbind
   * @throws Exception if an error occours
   */
  public final void unbind(InputControl comp) throws Exception {
    if (comp.getAttributeName()==null)
      throw new Exception("No attribute name defined for the specified input control");

    ArrayList list = (ArrayList)bindings.get(comp.getAttributeName());
    if (list==null)
      return;

    list.remove(comp);
    comp.removeValueChangedListener(this);
  }


  /**
   * Invoked when the value of a linked input control changes.
   * @param e ValueChangeEvent describing the event
   */
  public final void valueChanged(ValueChangeEvent e) {
    if (Beans.isDesignTime())
      return;
    model.setValue(e.getAttributeName(),e.getNewValue());
  }


  /**
   * Method called when user has clicked on export button: not supported.
   */
  public final void export() {}


  /**
   * Method called when used has clicked on filter button: not supported.
   */
  public void filterSort() {}



  /**
   * Set the functionId identifier, associated to the container
   * @param functionId identifier associated to the container
   */
  public final void setFunctionId(String functionId) {
    this.functionId = functionId;
  }


  /**
   * @return identifier (functionId) associated to the container
   */
  public final String getFunctionId() {
    return functionId;
  }


  /**
   * @return not focused border
   */
  public final Border getNotFocusedBorder() {
    return notFocusedBorder;
  }


  /**
   * Add a generic button, that inherits abilitation state of the other form buttons.
   * @param b generic button, that inherits abilitation state of the other form buttons
   */
  public final void addGenericButton(GenericButton b) {
    genericButtons.add(b);
  }


  /**
   * Remove a generic button, that inherits abilitation state of the other form buttons.
   * @param b generic button, that inherits abilitation state of the other form buttons
   */
  public final void removeGenericButton(GenericButton b) {
    genericButtons.remove(b);
  }


  /**
   * Link the specified grid control to the current Form, so that:
   * - row selection event (fired by grid navigator bar) will force the Form data loading (ONLY IF loadModelWhenSelectingOnGrid is set to <code>true</code>)
   * - insert new data on the Form will refresh grid by adding a new row
   * - update data on Form will refresh grid for the related changed row
   * - delete existing data on the Form will refresh grid content by removing the related row
   * To correctly identify the row on grid to change a matching beetween attributes (specified in "pkAttributes" argument)
   * of grid v.o. and Form v.o. is performed: it will be identified the first row on grid that matches this filter criteria, based on attribute values.
   *
   * NOTE: "reloadModelWhenSelectingOnGrid" is set to <code>true</code> then Form data reloading is performed also when clicking with the left mouse button onto the grid and when pressing up/down keys onto the grid.
   *
   * @param grid grid control linked to the current Form, to update grid content
   * @param pkAttributes attributes that must be defined both on grid v.o. and on the Form v.o. used to select on grid the (first) row that matches pk values; if the grid v.o. is the same class or a super-class of Form v.o. then this HashSet could
   * @param reloadModelWhenSelectingOnGrid <code>true</code> to force data Form reloading when changing row selection on grid by grid navigator bar
   */
  public final void linkGrid(GridControl grid,HashSet pkAttributes,boolean reloadModelWhenSelectingOnGrid) {
    linkGrid(grid,pkAttributes,reloadModelWhenSelectingOnGrid,reloadModelWhenSelectingOnGrid,reloadModelWhenSelectingOnGrid,null);
  }


  /**
   * Link the specified grid control to the current Form, so that:
   * - grid navigator bar event (i.e. pressing one of its buttons) on grid will force the Form data loading (e.g. when using grid navigator bar)
   * - insert new data on the Form will refresh grid by adding a new row
   * - update data on Form will refresh grid for the related changed row
   * - delete existing data on the Form will refresh grid content by removing the related row
   * To correctly identify the row on grid to change a matching beetween attributes (specified in "pkAttributes" argument)
   * of grid v.o. and Form v.o. is performed: it will be identified the first row on grid that matches this filter criteria, based on attribute values.
   * NOTE: if more than one Form has been opened, then all Forms will be reloaded to the same row when a grid navigator button is being pressed.
   *
   * In addition, Form data loading is automatically performed when using the specified navigator bar; also grid selection row is updated.
   * NOTE: "reloadModelWhenSelectingOnGrid" is set to <code>true</code> then Form data reloading is performed also when clicking with the left mouse button onto the grid and when pressing up/down keys onto the grid.
   *
   * @param grid grid control linked to the current Form, to update grid content
   * @param pkAttributes attributes that must be defined both on grid v.o. and on the Form v.o. used to select on grid the (first) row that matches pk values; if the grid v.o. is the same class or a super-class of Form v.o. then this HashSet could
   * @param reloadModelWhenSelectingOnGrid <code>true</code> to force data Form reloading when changing row selection on grid by grid navigator bar
   * @param navBar navigation bar linked to the current Form, used to load data on Form according to the selected row on grid, whose selection has been changed when using this navigator bar
   */
  public final void linkGrid(GridControl grid,HashSet pkAttributes,boolean reloadModelWhenSelectingOnGrid,NavigatorBar navBar) {
    linkGrid(grid,pkAttributes,reloadModelWhenSelectingOnGrid,reloadModelWhenSelectingOnGrid,reloadModelWhenSelectingOnGrid,navBar);
  }


  /**
   * Link the specified grid control to the current Form, so that:
   * - grid navigator bar event (i.e. pressing one of its buttons) on grid will force the Form data loading (e.g. when using grid navigator bar)
   * - insert new data on the Form will refresh grid by adding a new row
   * - update data on Form will refresh grid for the related changed row
   * - delete existing data on the Form will refresh grid content by removing the related row
   * To correctly identify the row on grid to change a matching beetween attributes (specified in "pkAttributes" argument)
   * of grid v.o. and Form v.o. is performed: it will be identified the first row on grid that matches this filter criteria, based on attribute values.
   * NOTE: if more than one Form has been opened, then all Forms will be reloaded to the same row when a grid navigator button is being pressed.
   *
   * In addition, Form data loading is automatically performed when using the specified navigator bar; also grid selection row is updated.
   *
   * @param grid grid control linked to the current Form, to update grid content
   * @param pkAttributes attributes that must be defined both on grid v.o. and on the Form v.o. used to select on grid the (first) row that matches pk values; if the grid v.o. is the same class or a super-class of Form v.o. then this HashSet could
   * @param reloadModelWhenSelectingOnGrid <code>true</code> to force data Form reloading when changing row selection on grid by grid navigator bar
   * @param reloadModelWhenClickingWithMouse <code>true</code> to enable Form data reloading when clicking with the left mouse button onto the grid
   * @param reloadModelWhenPressingKey <code>true</code> to enable Form data reloading when pressing up/down keys onto the grid
   * @param navBar navigation bar linked to the current Form, used to load data on Form according to the selected row on grid, whose selection has been changed when using this navigator bar
   */
  public final void linkGrid(GridControl grid,HashSet pkAttributes,boolean reloadModelWhenSelectingOnGrid,boolean reloadModelWhenClickingWithMouse,boolean reloadModelWhenPressingKey,NavigatorBar navBar) {
    this.grid = grid;
    this.pkAttributes = pkAttributes;
    this.navBar = navBar;
    this.reloadModelWhenClickingWithMouse = reloadModelWhenClickingWithMouse;
    this.reloadModelWhenPressingKey = reloadModelWhenPressingKey;

    if (Beans.isDesignTime())
      return;

    if (grid!=null && pkAttributes!=null) {
      if (grid.getNavBar()!=null && reloadModelWhenSelectingOnGrid) {
        // if there exist a navigator bar linked to the grid, then listen events from it...
        grid.getNavBar().addAfterActionListener(this);

        // remove that listener when this Form is destroyed...
        this.addAncestorListener(new AncestorListener() {

             public void ancestorAdded(AncestorEvent event) {
             }

             public void ancestorMoved(AncestorEvent event) {
             }

             public void ancestorRemoved(AncestorEvent event) {
               if (Form.this.grid!=null && Form.this.grid.getNavBar()!=null) {
                 Form.this.grid.getNavBar().removeAfterActionListener(Form.this);
               }
             }
        });

      }

      if (navBar!=null) {
        navBar.initNavigator(grid.getTable());
        navBar.addAfterActionListener(this);

        // used to correctly set the row in grid related to the current v.o. in the Form...
        navBar.addBeforeActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            int rowInGrid = getRowIndexInGrid();
            if (rowInGrid!=-1)
              Form.this.grid.setRowSelectionInterval(rowInGrid,rowInGrid);
          }
        });
      }
    }
  }


  /**
   * @return row index in grid related to the linked grid's v.o. having the same pk of the current Form v.o.; -1 if that row does not exist in grid or when the grid has not been linked to this Form (see linkGrid method))
   */
  public final int getRowIndexInGrid() {
    if (grid!=null && pkAttributes!=null) {
      ValueObject gridVO = null;
      Iterator it = null;
      String pkAttrName = null;
      int colIndex;
      Object o1,o2;
      boolean rowFound;
      for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
        gridVO = grid.getVOListTableModel().getObjectForRow(i);
        it = pkAttributes.iterator();
        rowFound = true;
        while(it.hasNext()) {
          pkAttrName = it.next().toString();
          colIndex = grid.getVOListTableModel().findColumn(pkAttrName);
          if (colIndex==-1) {
            rowFound = false;
            break;
          }
          o1 = grid.getVOListTableModel().getValueAt(i,colIndex);
          o2 = model.getValue(pkAttrName,previousVO);
          if (o1==null && o2!=null ||
              o1!=null && o2==null ||
              o1!=null && o2!=null && !o1.equals(o2)) {
            rowFound = false;
            break;
          }
        }
        if (rowFound)
          return i;
      }
    }
    return -1;
  }


  /**
   * Callback method invoked by the navigator bar of the grid when the user has pressed a button on it.
   */
  public final void actionPerformed(ActionEvent e) {
    // a row on the linked grid has been just selected: reload data model for this Form...
    if (e.getSource().equals(navBar) ||
        (
          grid.getNavBar()!=null && e.getSource().equals(grid.getNavBar()) &&
          !(!reloadModelWhenClickingWithMouse && e.getActionCommand().equals(NavigatorBar.LEFT_MOUSE_BUTTON)) &&
          !(!reloadModelWhenPressingKey && e.getActionCommand().equals(NavigatorBar.UP_KEY)) &&
          !(!reloadModelWhenPressingKey && e.getActionCommand().equals(NavigatorBar.DOWN_KEY))
        )
    )
      reload();
  }


  /**
   * @return define if an inner v.o. must be automatically instantiated when a setter method is invoked
   */
  public final boolean isCreateInnerVO() {
    return createInnerVO;
  }


  /**
   * Define if an inner v.o. must be automatically instantiated when a setter method is invoked.
   * @param createInnerVO define if an inner v.o. must be automatically instantiated when a setter method is invoked
   */
  public final void setCreateInnerVO(boolean createInnerVO) {
    this.createInnerVO = createInnerVO;
    if (model!=null)
      model.setCreateInnerVO(createInnerVO);
  }


}


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Inner class that stores a couple (attribute name, attribute value).</p>
 * @author Mauro Carniel
 * @version 1.0
 */
class AttributeCouple {

  private String attributeName = null;
  private Object attributeValue = null;

  public AttributeCouple(String attributeName,Object attributeValue) {
    this.attributeName = attributeName;
    this.attributeValue = attributeValue;

  }


  public final String getAttributeName() {
    return attributeName;
  }


  public final Object getAttributeValue() {
    return attributeValue;
  }

}

