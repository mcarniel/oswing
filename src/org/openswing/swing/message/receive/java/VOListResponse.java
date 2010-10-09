package org.openswing.swing.message.receive.java;

import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Message received by the server side: it contains a list of value objects needed by a grid.
 * It also contains the total number of records related to the (partial) list and a flag indicating if there are other value objects to read, after the currents.</p>
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
public class VOListResponse extends Response {

  /** error flag */
  private boolean error;

  /** erro message */
  private String errorMessage;

  /** list of value objects */
  private List rows = null;

  /** there are other rows in result set not yet loaded */
  private boolean moreRows;

  /** result set length */
  private int resultSetLength;

  /** total amount of rows; usually not filled */
  private int totalAmountOfRows = -1;


  public VOListResponse() {
     this.rows = new ArrayList();
     this.moreRows = false;
     this.resultSetLength = 0;
   }


   public VOListResponse(String errorMessage) {
      this.rows = new ArrayList();
      this.moreRows = false;
      this.resultSetLength = 0;
      this.errorMessage = errorMessage;
      this.error = true;
    }


  /**
   * @param rows list of value objects
   * @param moreRows there are other rows in result set not yet loaded
   * @param resultSetLength result set length
   */
  public VOListResponse(List rows,boolean moreRows,int resultSetLength) {
    this.rows = rows;
    this.moreRows = moreRows;
    this.resultSetLength = resultSetLength;
  }


  /**
   * @return <code>true</code> if an error occours, <code>false</code> otherwise
   */
  public final boolean isError() {
    return error;
  }


  /**
   * @return error message
   */
  public final String getErrorMessage() {
    return errorMessage;
  }


  /**
   * @return rows list of value objects
   */
  public final List getRows() {
    return rows;
  }


  /**
   * @return there are other rows in result set not yet loaded
   */
  public final boolean isMoreRows() {
    return moreRows;
  }


  /**
   * @return result set length
   */
  public final int getResultSetLength() {
    return resultSetLength;
  }


  /**
   * @return total amount of rows in ResultSet; -1 if not determined
   */
  public final int getTotalAmountOfRows() {
    return totalAmountOfRows;
  }


  /**
   * Set the total amount of rows in ResultSet
   * @param totalAmountOfRows total amount of rows in ResultSet
   */
  public final void setTotalAmountOfRows(int totalAmountOfRows) {
    this.totalAmountOfRows = totalAmountOfRows;
  }


  public void setRows(List rows) {
    this.rows = rows;
  }


  public void setResultSetLength(int resultSetLength) {
    this.resultSetLength = resultSetLength;
  }


  public void setMoreRows(boolean moreRows) {
    this.moreRows = moreRows;
  }


  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }


  public void setError(boolean error) {
    this.error = error;
  }


}
