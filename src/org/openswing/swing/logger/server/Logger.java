package org.openswing.swing.logger.server;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for logging server messages.</p>
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
public class Logger {

  public static final int LOG_ALL = 0;
  public static final int LOG_ERROR_ONLY = 1;
  public static final int LOG_ERROR_WARN_ONLY = 2;
  public static final int LOG_ERROR_WARN_INFO_ONLY = 3;
  public static LoggerMethods loggerImpl = null;
  public static int logLevelImpl;


  static {
    init(new ConsoleLogger(),LOG_ALL);
  }


  /**
   * Initialize logger.
   * @param username username
   * @param logger logger implementation
   * @param logLevel log level
   */
  public static final void init(LoggerMethods logger,int logLevel) {
    loggerImpl = logger;
    logLevelImpl = logLevel;
  }


  /**
   * Log an error message.
   * @param username username
   * @param className class when occours the exception
   * @param methodName method when occours the exception
   * @param errorMessage exception message
   * @param exception exception
   */
  public static final void error(String username,String className,String methodName,String errorMessage,Throwable exception) {
    loggerImpl.error(username,className,methodName,errorMessage,exception);
  }


  /**
   * Log a debug message.
   * @param username username
   * @param className class when occours the debug message
   * @param methodName method when occours the debug message
   * @param debugMessage debug message
   */
  public static final void debug(String username,String className,String methodName,String debugMessage) {
    if (logLevelImpl==LOG_ALL)
      loggerImpl.debug(username,className,methodName,debugMessage);
  }


  /**
   * Log a warn message.
   * @param username username
   * @param className class when occours the warn message
   * @param methodName method when occours the warn message
   * @param warnMessage warn message
   */
  public static final void warn(String username,String className,String methodName,String warnMessage) {
    if (logLevelImpl==LOG_ALL || logLevelImpl==LOG_ERROR_WARN_INFO_ONLY || logLevelImpl==LOG_ERROR_WARN_ONLY)
      loggerImpl.warn(username,className,methodName,warnMessage);
  }


  /**
   * Log an info message.
   * @param username username
   * @param className class when occours the warn message
   * @param methodName method when occours the warn message
   * @param infoMessage info message
   */
  public static final void info(String username,String className,String methodName,String infoMessage) {
    if (logLevelImpl==LOG_ALL || logLevelImpl==LOG_ERROR_WARN_INFO_ONLY)
    loggerImpl.info(username,className,methodName,infoMessage);
  }

}
