package org.openswing.swing.message.receive.java;

import java.io.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base message returned by the server side: all other messages derive from this one.</p>
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
public abstract class Response implements Serializable {

  /** session id of the current client application */
  private String sessionId = null;


  /**
   * @return <code>true</code> if an error occours, <code>false</code> otherwise
   */
  public abstract boolean isError();


  /**
   * @return error message
   */
  public abstract String getErrorMessage();


  /**
   * @return session id associated to the current client
   */
  public final String getSessionId() {
    return sessionId;
  }


  /**
   * Set the session id associated to the current client.
   * @param sessionId session id associated to the current client
   */
  public final void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }


}
