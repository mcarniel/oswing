package org.openswing.swing.logger.server;

import java.io.*;
import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for logging messages through Log4J.</p>
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
public class Log4JLogger implements LoggerMethods {

  /** Log4J log implementation */
  private org.apache.log4j.Logger logger = null;

  public Log4JLogger() {
    logger = logger = org.apache.log4j.Logger.getRootLogger();
  }


  public Log4JLogger(String id) {
    logger = org.apache.log4j.Logger.getLogger(id);
  }


  /**
   * Log an error message.
   * @param username username
   * @param className class when occours the exception
   * @param methodName method when occours the exception
   * @param errorMessage exception message
   * @param exception exception
   */
  public final void error(String username,String className,String methodName,String errorMessage,Throwable exception) {
    logger.error(
        "\n------------------------\n"+
        (username!=null?"["+username+"] ":"[UNDEFINED] ")+new Date()+" [ERROR]\n"+
        "in "+className+"."+methodName+"():\n"+
        errorMessage+"\n"+
        "------------------------",
        exception
    );
  }


  /**
   * Log a debug message.
   * @param username username
   * @param className class when occours the debug message
   * @param methodName method when occours the debug message
   * @param debugMessage debug message
   */
  public final void debug(String username,String className,String methodName,String debugMessage) {
    logger.debug(
        "\n------------------------\n"+
        (username!=null?"["+username+"] ":"[UNDEFINED] ")+new Date()+" [DEBUG]\n"+
        "in "+className+"."+methodName+"():\n"+
        debugMessage+"\n"+
        "------------------------"
    );
  }


  /**
   * Log a warn message.
   * @param username username
   * @param className class when occours the warn message
   * @param methodName method when occours the warn message
   * @param warnMessage warn message
   */
  public final void warn(String username,String className,String methodName,String warnMessage) {
    logger.warn(
        "\n------------------------\n"+
        (username!=null?"["+username+"] ":"[UNDEFINED] ")+new Date()+" [WARN]\n"+
        "in "+className+"."+methodName+"():\n"+
        warnMessage+"\n"+
        "------------------------"
    );
  }


  /**
   * Log an info message.
   * @param username username
   * @param className class when occours the warn message
   * @param methodName method when occours the warn message
   * @param infoMessage info message
   */
  public final void info(String username,String className,String methodName,String infoMessage) {
    logger.info(
        "\n------------------------\n"+
        (username!=null?"["+username+"] ":"[UNDEFINED] ")+new Date()+" [INFO]\n"+
        "in "+className+"."+methodName+"():\n"+
        infoMessage+"\n"+
        "------------------------"
    );
  }


  class StackTraceCollector extends OutputStream {

    private StringBuffer stack = new StringBuffer();


    public void write(int b) throws IOException {
      stack.append((char)b);
    }


    public String toString() {
      return stack.toString();
    }

  }


}
