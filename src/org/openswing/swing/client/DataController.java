package org.openswing.swing.client;

import java.util.HashSet;





/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Interface used by Form and Grid to manage the following operations:
 * insert, edit, delete, reload/cancel, save, copy.
 * It also defines the functionId identifier (optional).</p>
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
public interface DataController {


  /**
   * Method called when used has clicked on reload/cancel button.
   */
  public void reload();


  /**
   * Method called when used has clicked on insert button.
   */
  public void insert();


  /**
   * Method called when used has clicked on copy button.
   */
  public void copy();


  /**
   * Method called when used has clicked on edit button.
   */
  public void edit();


  /**
   * Method called when used has clicked on delete button.
   */
  public void delete();


  /**
   * Method called when used has clicked on save button.
   */
  public boolean save();


  /**
   * Method called when used has clicked on export button.
   */
  public void export();


  /**
   * @return identifier (functionId) associated to the container
   */
  public String getFunctionId();


  /**
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form/grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form/grid method)
   */
  public boolean isButtonDisabled(GenericButton button);


  /**
   * Method called when the user has clicked on filter button.
   */
  public void filterSort();


  /**
   * Method called when used has clicked on import button.
   */
  public void importData();


  /**
   * Set current enabled value of button.
   * @param button generic button that fires this event.
   * @param currentValue current enabled value
   */
  public void setCurrentValue(GenericButton button,boolean currentValue);


  /**
   * @param button generic button that fires this event
   * @return current enabled value
   */
  public boolean getCurrentValue(GenericButton button);


  /**
   * @return collection of buttons binded to grid (InsertButton, EditButton, etc)
   */
  public HashSet getBindedButtons();


}
