package org.openswing.swing.message.receive.java;




/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Message received by the server side: it contains an object.</p>
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
public class VOResponse extends Response {

  /** error flag */
  private boolean error;

  /** erro message */
  private String errorMessage;

  /** object */
  private Object vo = null;


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
   * @return generic (Serializable) object
   */
  public final Object getVo() {
    return vo;
  }


  public void setError(boolean error) {
    this.error = error;
  }


  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }


  public void setVo(Object vo) {
    this.vo = vo;
  }


  /**
   * Costructor.
   * @param vo generic (Serializable) object
   */
  public VOResponse(Object vo) {
    this.vo = vo;
  }


  /**
   * Costructor.
   * @param vo generic (Serializable) object
   */
  public VOResponse() { }

}
