package org.openswing.swing.lookup.client;

import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.tree.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Data Source.</p>
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
public abstract class LookupDataLocator extends TreeDataLocator {

  /** parameters used to retrieve grid data (optional) */
  private Map lookupFrameParams = new HashMap();

  /** parameters used to validate code (optionals) */
  private Map lookupValidationParameters = new HashMap();


  public LookupDataLocator() {}


  /**
   * Method called by lookup controller when validating code.
   * @param code code to validate
   * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
   */
  public abstract Response validateCode(String code);


  /**
   * Method called by lookup controller when user clicks on lookup button.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startIndex current index row on grid to use to start fetching data
   * @param filteredColumns filtered columns
   * @param currentSortedColumns sorted columns
   * @param currentSortedVersusColumns ordering versus of sorted columns
   * @param valueObjectType type of value object associated to the lookup grid
   * @return list of value objects to fill in the lookup grid: VOListResponse if data fetching has success, ErrorResponse otherwise
   */
  public abstract Response loadData(
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Class valueObjectType
  );


  /**
   * @return lookup frame parameters
   */
  public Map getLookupFrameParams() {
    return lookupFrameParams;
  }


  /**
   * @return lookup validation code parameters
   */
  public Map getLookupValidationParameters() {
    return lookupValidationParameters;
  }


  /**
   * Set lookup validation code parameters.
   * @param lookupValidationParameters lookup validation code parameters
   */
  public void setLookupValidationParameters(Map lookupValidationParameters) {
    this.lookupValidationParameters = lookupValidationParameters;
  }


  /**
   * Set lookup frame parameters.
   * @param lookupFrameParams lookup frame parameters
   */
  public void setLookupFrameParams(Map lookupFrameParams) {
    this.lookupFrameParams = lookupFrameParams;
  }






}
