package org.openswing.swing.permissions.client;

import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Login controller.</p>
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
public interface LoginController {


  /**
   * @return maximum number of failed login
   */
  public int getMaxAttempts();


  /**
   * Method used to destroy application if login is failed.
   */
  public void stopApplication();


  /**
   * Method called by LoginDialog to authenticate the user.
   * @param loginInfo login information, like username, password, ...
   * @return <code>true</code> if user is correcly authenticated, <code>false</code> otherwise
   * NOTE: if this class is used in combination with LoginDialog and CryptUtils classes,
   * than Map argument contains an entry named "password" with associated value tpye byte[] (encrypted password)
   * otherwise Map argument contains an entry named "password" with associated value type String (clear password)
   */
  public boolean authenticateUser(Map loginInfo) throws Exception;


  /**
   * Method called by LoginDialog to notify the sucessful login.
   * @param loginInfo login information, like username, password, ...
   * NOTE: if this class is used in combination with LoginDialog and CryptUtils classes,
   * than Map argument contains an entry named "password" with associated value tpye byte[] (encrypted password)
   * otherwise Map argument contains an entry named "password" with associated value type String (clear password)
   */
  public void loginSuccessful(Map loginInfo);



}
