package org.openswing.swing.logger.server;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Base class for logging messages.</p>
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
public interface LoggerMethods {


  /**
   * Log an error message.
   * @param username username
   * @param className class when occours the exception
   * @param methodName method when occours the exception
   * @param errorMessage exception message
   * @param exception exception
   */
  public void error(String username,String className,String methodName,String errorMessage,Throwable exception);


  /**
   * Log a debug message.
   * @param username username
   * @param className class when occours the debug message
   * @param methodName method when occours the debug message
   * @param debugMessage debug message
   */
  public void debug(String username,String className,String methodName,String debugMessage);


  /**
   * Log a warn message.
   * @param username username
   * @param className class when occours the warn message
   * @param methodName method when occours the warn message
   * @param warnMessage warn message
   */
  public void warn(String username,String className,String methodName,String warnMessage);


  /**
   * Log an info message.
   * @param username username
   * @param className class when occours the warn message
   * @param methodName method when occours the warn message
   * @param infoMessage info message
   */
  public void info(String username,String className,String methodName,String infoMessage);

}
