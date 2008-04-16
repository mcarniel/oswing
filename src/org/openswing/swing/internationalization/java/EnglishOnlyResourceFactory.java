package org.openswing.swing.internationalization.java;

import java.util.*;
import java.io.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for retrieve the collection of all internationalization properties:
 * translations, data/numeric/currency formats.
 * No translation is performed, date/numeric/currency formats are based on english formats.</p>
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
public class EnglishOnlyResourceFactory extends ResourcesFactory {

  /** internationalization settings */
  private Resources resources = null;


  /**
   * Constructor.
   * @param currencySymbol currency symbol
   * @param additionalDictionary additional descriptions
   * @param showResourceNotFoundWarning warn when no resource key not found
   */
  public EnglishOnlyResourceFactory(String currencySymbol,Properties additionalDictionary,boolean showResourceNotFoundWarning) {
    Properties dictionary = new Properties();

    dictionary.putAll(additionalDictionary);

    // grid...
    dictionary.setProperty("Remove Filter","Remove Filter");
    dictionary.setProperty("This column is not sorteable","This column is not sorteable");
    dictionary.setProperty("Sorting not allowed","Sorting not allowed");
    dictionary.setProperty("Maximum number of sorted columns","Maximum number of sorted columns");
    dictionary.setProperty("Sorting not applicable","Sorting not applicable");
    dictionary.setProperty("Selected Row","Selected Row");
    dictionary.setProperty("Selected Rows","Selected Rows");
    dictionary.setProperty("Cancel changes and reload data?","Cancel changes and reload data?");
    dictionary.setProperty("Attention","Attention");
    dictionary.setProperty("Loading data...","Loading data...");
    dictionary.setProperty("Error while loading data","Error while loading data");
    dictionary.setProperty("Loading Data Error","Loading Data Error");
    dictionary.setProperty("Delete Rows?","Delete Rows?");
    dictionary.setProperty("Delete Confirmation","Delete Confirmation");
    dictionary.setProperty("Error while deleting rows.","Error while deleting rows.");
    dictionary.setProperty("Deleting Error","Deleting Error");
    dictionary.setProperty("Error while deleting rows.","Error while deleting rows.");
    dictionary.setProperty("Error while saving","Error while saving");
    dictionary.setProperty("Saving Error","Saving Error");
    dictionary.setProperty("A mandatory column is empty.","A mandatory column is empty.");
    dictionary.setProperty("Value not valid","Value not valid");
    dictionary.setProperty("sorting conditions","Sorting conditions");
    dictionary.setProperty("filtering conditions","Filtering conditions");
    dictionary.setProperty("filtering and sorting settings","Filtering and sorting settings");
    dictionary.setProperty("Filtering/Sorting data (CTRL+F)","Filtering/Sorting data (CTRL+F)");

    // export...
    dictionary.setProperty("grid export","Grid Export");
    dictionary.setProperty("export","Export");
    dictionary.setProperty("exportmnemonic","X");
    dictionary.setProperty("column","Column");
    dictionary.setProperty("sel.","Sel.");
    dictionary.setProperty("you must select at least one column","You must select at least one column");
    dictionary.setProperty("columns to export","Columns to export");
    dictionary.setProperty("export type","Export format");

    // quick filter...
    dictionary.setProperty("To value","To value");
    dictionary.setProperty("Filter by","Filter by");
    dictionary.setProperty("From value","From value");
    dictionary.setProperty("equals","equals");
    dictionary.setProperty("contains","contains");
    dictionary.setProperty("starts with","starts with");
    dictionary.setProperty("ends with","ends with");


    // lookup...
    dictionary.setProperty("Code is not correct.","Code is not correct.");
    dictionary.setProperty("Code Validation","Code Validation");
    dictionary.setProperty("Code Selection","Code Selection");

    // form...
    dictionary.setProperty("Confirm deliting data?","Confirm deliting data?");
    dictionary.setProperty("Error while saving: incorrect data.","Error while saving: incorrect data.");
    dictionary.setProperty("Error on deleting:","Error on deleting:");
    dictionary.setProperty("Error on Loading","Error on Loading");
    dictionary.setProperty("Error while loading data:","Error while loading data:");
    dictionary.setProperty("Error on setting value to the input control having the attribute name","Error on setting value to the input control having the attribute name");

    // toolbar buttons...
    dictionary.setProperty("Delete record (CTRL+D)","Delete record (CTRL+D)");
    dictionary.setProperty("Edit record (CTRL+E)","Edit record (CTRL+E)");
    dictionary.setProperty("New record (CTRL+I)","New record (CTRL+I)");
    dictionary.setProperty("Reload record/Cancel current operation (CTRL+Z)","Reload record/Cancel current operation (CTRL+Z)");
    dictionary.setProperty("Save record (CTRL+S)","Save record (CTRL+S)");
    dictionary.setProperty("Copy record (CTRL+C)","Copy record (CTRL+C)");
    dictionary.setProperty("Export record (CTRL+X)","Export record (CTRL+X)");
    dictionary.setProperty("Load the first block of records","Load the first block of records");
    dictionary.setProperty("Select the previous row in grid","Select the previous row in grid");
    dictionary.setProperty("Select the next row in grid","Select the next row in grid");
    dictionary.setProperty("Load the previous block of records","Load the previous block of records");
    dictionary.setProperty("Load the next block of records","Load the next block of records");
    dictionary.setProperty("Load the last block of records","Load the last block of records");

    dictionary.setProperty("Insert","Insert");
    dictionary.setProperty("Edit","Edit");
    dictionary.setProperty("Copy","Copy");
    dictionary.setProperty("Delete","Delete");
    dictionary.setProperty("Save","Save");
    dictionary.setProperty("Reload","Reload");
    dictionary.setProperty("Export","Export");
    dictionary.setProperty("Filter","Filter");

    // binding messages...
    dictionary.setProperty("value must be of type ","Value must be of type ");
    dictionary.setProperty("decimal number","decimal number");
    dictionary.setProperty("value is ","Value is ");
    dictionary.setProperty("optional","optional");
    dictionary.setProperty("minimum value is ","Minimum value is ");
    dictionary.setProperty("required","required");
    dictionary.setProperty("contains a value not valid","contains a value not valid");
    dictionary.setProperty("text","text");
    dictionary.setProperty("Date","date");
    dictionary.setProperty("Value is required","Value is required");
    dictionary.setProperty("maximum value is ","Maximum value is ");
    dictionary.setProperty("yes/no","yes/no");
    dictionary.setProperty("long number","long number");
    dictionary.setProperty("integer number","integer number");
    dictionary.setProperty("Cannot assign component value","Cannot assign component value");

    // ClientUtils...
    dictionary.setProperty("Server Comunication Error","Server Comunication Error");
    dictionary.setProperty("Server Error","Server Error");

    // MDI Frame...
    dictionary.setProperty("file","File");
    dictionary.setProperty("exit","Exit");
    dictionary.setProperty("filemnemonic","F");
    dictionary.setProperty("exitmnemonic","E");
    dictionary.setProperty("change user","Change User");
    dictionary.setProperty("changeusermnemonic","U");
    dictionary.setProperty("changelanguagemnemonic","L");
    dictionary.setProperty("help","Help");
    dictionary.setProperty("about","About");
    dictionary.setProperty("helpmnemonic","H");
    dictionary.setProperty("aboutmnemonic","A");
    dictionary.setProperty("are you sure to quit application?","Are you sure to quit application?");
    dictionary.setProperty("quit application","Quit Application");
    dictionary.setProperty("about","About");
    dictionary.setProperty("forcegcmnemonic","F");
    dictionary.setProperty("Force GC","Force GC");
    dictionary.setProperty("Info","Info");
    dictionary.setProperty("About","About");
    dictionary.setProperty("Java Heap","Java Heap");
    dictionary.setProperty("used","used");
    dictionary.setProperty("allocated","allocated");
    dictionary.setProperty("change language","Change Language");
    dictionary.setProperty("changemnemonic","L");
    dictionary.setProperty("cancelmnemonic","C");
    dictionary.setProperty("cancel","Cancel");
    dictionary.setProperty("Functions","Functions");
    dictionary.setProperty("Error while executing function","Error while executing function");
    dictionary.setProperty("Error","Error");
    dictionary.setProperty("infoPanel","Info");
    dictionary.setProperty("imageButton","About");
    dictionary.setProperty("Window","Window");
    dictionary.setProperty("windowmnemonic","W");
    dictionary.setProperty("Close All","Close All");
    dictionary.setProperty("closeallmnemonic","A");
    dictionary.setProperty("Press ENTER to find function","Press ENTER to find function");
    dictionary.setProperty("Find Function","Find Function");
    dictionary.setProperty("Operation in progress...","Operation in progress...");
    dictionary.setProperty("close window","Close Window");
    dictionary.setProperty("reduce to icon","Reduce to icon");
    dictionary.setProperty("are you sure to close this window?","Are you sure to close this window?\nChanges not yet saved will be lost.");
    dictionary.setProperty("confirm window closing","Confirm window closing");

    // server...
    dictionary.setProperty("Client request not supported","Client request not supported");
    dictionary.setProperty("User disconnected","User disconnected");
    dictionary.setProperty("Updating not performed: the record was previously updated.","Updating not performed: the record was previously updated.");

    // wizard...
    dictionary.setProperty("back","Back");
    dictionary.setProperty("next","Next");
    dictionary.setProperty("finish","Finish");

    // image panel...
    dictionary.setProperty("image selection","Image selection");

    // tip of the day panel...
    dictionary.setProperty("show 'tip of the day' after launching","Show 'tip of the day' after launching");
    dictionary.setProperty("previous tip","Previous tip");
    dictionary.setProperty("next tip","Next tip");
    dictionary.setProperty("close","Close");
    dictionary.setProperty("tip of the day","Tip of the day");

    // progress panel...
    dictionary.setProperty("progress","Progress");

    // licence agreement...
    dictionary.setProperty("i accept the terms in the licence agreement","I accept the terms in the licence agreement");
    dictionary.setProperty("ok","Ok");
    dictionary.setProperty("i do not accept the terms in the licence agreement","I do not accept the terms in the licence agreement");

    // property grid control
    dictionary.setProperty("property name","Name");
    dictionary.setProperty("property value","Value");

    // grid profile
    dictionary.setProperty("grid profile management","Grid profile management");
    dictionary.setProperty("restore default grid profile","Restore default grid profile");
    dictionary.setProperty("create new grid profile","Create new grid profile");
    dictionary.setProperty("profile description","Profile description");
    dictionary.setProperty("remove current grid profile","Remove current grid profile");
    dictionary.setProperty("select grid profile","Select grid profile");
    dictionary.setProperty("default profile","Default profile");

    // search box
    dictionary.setProperty("search","Search");
    dictionary.setProperty("not found","Not found");

    // drag...
    dictionary.setProperty("drag","Drag");
    dictionary.setProperty("drag stopped","Drag stopped");

    resources = new Resources(
      dictionary,
      currencySymbol,
      '.',
      ',',
      Resources.YMD,
      true,
      '/',
      "HH:mm",
      "EN",
      showResourceNotFoundWarning
    );
  }


  /**
   * @return internationalization settings, according with the current language
   */
  public final Resources getResources() {
    return resources;
  }


  /**
   * Load dictionary, according to the specified language id.
   * @param langId language id identifier
   */
  public final void setLanguage(String langId) throws UnsupportedOperationException {
    if (!resources.getLanguageId().equals(langId))
      throw new UnsupportedOperationException("Language identifier not supported.");
  }


  /**
   * @param langId language id identifier
   * @return internationalization settings, according with the language specified
   */
  public final Resources getResources(String langId) throws UnsupportedOperationException {
    if (!resources.getLanguageId().equals(langId))
    throw new UnsupportedOperationException("Language identifier not supported.");
    return resources;
  }



}
