package org.openswing.swing.server;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base class to store client session info.</p>
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
public class UserSessionParameters {

  /** username */
  private String username;

  /** language identifier */
  private String languageId;

  /** session identifier */
  private String sessionId;


  public UserSessionParameters(String username) {
    this.username = username;
  }


  public UserSessionParameters() {}


  /**
   * @return username
   */
  public final String getUsername() {
    return username;
  }


  /**
   * Set the username
   * @param usercode username
   */
  public final void setUsername(String username) {
    this.username = username;
  }


  /**
   * @return language identifier
   */
  public final String getLanguageId() {
    return languageId;
  }


  /**
   * Set the language identifier.
   * @param languageId language identifier
   */
  public final void setLanguageId(String languageId) {
    this.languageId = languageId;
  }


  /**
   * @return session identifier
   */
  public final String getSessionId() {
    return sessionId;
  }


  /**
   * Set the session identifier.
   * @param sessionId session identifier
   */
  public final void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }



}
