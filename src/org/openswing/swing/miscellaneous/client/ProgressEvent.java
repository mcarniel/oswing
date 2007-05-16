package org.openswing.swing.miscellaneous.client;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class used to store a progress bar event, used by the progress dialog to update progress bar showing and messages.</p>
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
public class ProgressEvent {

  /** current progress bar value */
  private double progressValue;

  /** main title to show in bold style */
  private String mainTitle;

  /** main message to show in multiple lines */
  private String mainMessage;

  /** text messages to show above the progress bar */
  private String[] text;


  /**
   * Constructor used to show
   * @param progress current progress bar value
   * @param mainTitle main title to show in bold style
   * @param mainMessage main message to show in multiple lines
   * @param text text messages to show above the progress bar
   */
  public ProgressEvent(double progressValue,String mainTitle,String mainMessage,String[] text) {
    this.progressValue = progressValue;
    this.mainTitle = mainTitle;
    this.mainMessage = mainMessage;
    this.text = text;
  }


  /**
   * @return main message to show in multiple lines
   */
  public final String getMainMessage() {
    return mainMessage;
  }


  /**
   * @return main title to show in bold style
   */
  public final String getMainTitle() {
    return mainTitle;
  }


  /**
   * @return current progress bar value
   */
  public final double getProgressValue() {
    return progressValue;
  }


  /**
   * @return text messages to show above the progress bar
   */
  public final String[] getText() {
    return text;
  }


}