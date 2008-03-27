package org.openswing.swing.logger.client;

import java.io.*;
import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for logging client messages on console.</p>
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
public class ConsoleLogger implements LoggerMethods {




  /**
   * Log an error message.
   * @param className class when occours the exception
   * @param methodName method when occours the exception
   * @param errorMessage exception message
   * @param exception exception
   */
  public final void error(String className,String methodName,String errorMessage,Throwable exception) {
    StackTraceCollector stack = new StackTraceCollector();
    if (exception!=null)
      exception.printStackTrace(new PrintStream(stack));

    System.err.println(
        "\n------------------------\n"+
        new Date()+" [ERROR]\n"+
        "in "+className+"."+methodName+"():\n"+
        errorMessage+"\n"+
        stack+"\n"+
        "------------------------"
    );
  }


  /**
   * Log a debug message.
   * @param className class when occours the debug message
   * @param methodName method when occours the debug message
   * @param debugMessage debug message
   */
  public final void debug(String className,String methodName,String debugMessage) {
    System.out.println(
        "\n------------------------\n"+
        new Date()+" [DEBUG]\n"+
        "in "+className+"."+methodName+"():\n"+
        debugMessage+"\n"+
        "------------------------"
    );
  }


  /**
   * Log a warn message.
   * @param className class when occours the warn message
   * @param methodName method when occours the warn message
   * @param warnMessage warn message
   */
  public final void warn(String className,String methodName,String warnMessage) {
    System.out.println(
        "\n------------------------\n"+
        new Date()+" [WARN]\n"+
        "in "+className+"."+methodName+"():\n"+
        warnMessage+"\n"+
        "------------------------"
    );
  }


  /**
   * Log an info message.
   * @param className class when occours the warn message
   * @param methodName method when occours the warn message
   * @param infoMessage info message
   */
  public final void info(String className,String methodName,String infoMessage) {
    System.out.println(
        "\n------------------------\n"+
        new Date()+" [INFO]\n"+
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
