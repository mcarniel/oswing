package org.openswing.swing.internationalization.java;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for retrieving the collection of all internationalization properties:
 * translations, data/numeric/currency formats.
 * Based on XML files.</p>
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
public class XMLResourcesFactory extends ResourcesFactory {

  /** internationalization settings */
  private Resources resources = null;

  /** collection of pairs: language identifier, Resources object */
  private Hashtable allResources = new Hashtable();

  /** collection of pairs: language identifier (e.g. "en","it") or language+country (e.g. "en_UK","it_IT"), xml file path (relative to classpath or absolute) */
  private Hashtable xmlFiles = null;

  /** warn when no resource key not found */
  private boolean showResourceNotFoundWarning = true;


  /**
   * Costructor.
   * @param xmlFiles collection of pairs: language identifier, xml file path (relative to classpath or absolute)
   */
  public XMLResourcesFactory(Hashtable xmlFiles,boolean showResourceNotFoundWarning) {
    this.xmlFiles = xmlFiles;
    this.showResourceNotFoundWarning = showResourceNotFoundWarning;
  }


  /**
   * @return internationalization settings, according with the current language
   */
  public final Resources getResources() {
    return resources;
  }


  /**
   * Load dictionary, according to the specified language identifier.
   * @param langId language identifier
   */
  public final void setLanguage(String langId) throws UnsupportedOperationException {
    resources = getResources(langId);
  }


  /**
   * @param langId language id identifier
   * @return internationalization settings, according with the language specified
   */
  public final Resources getResources(String langId) throws UnsupportedOperationException {
    Resources resources = (Resources)allResources.get(langId);
    if (resources!=null)
      return resources;

    String xmlFile = (String)xmlFiles.get(langId);
    if (xmlFile==null) {
      throw new UnsupportedOperationException("Language unknown '"+langId+"'");
    }
    else {
      try {
        InputStream in = null;
        try {
          in = this.getClass().getClassLoader().getResourceAsStream(xmlFile.replaceAll("%20"," "));
          if (in==null)
            in = new FileInputStream(xmlFile.replaceAll("%20"," "));
        }
        catch (Exception ex1) {
          in = new FileInputStream(xmlFile.replaceAll("%20"," "));
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(in);
        NodeList nodes = doc.getElementsByTagName("object");
        Properties p = new Properties();
        for(int i=0;i<nodes.getLength();i++) {
          p.setProperty(
            ((Element)nodes.item(i)).getAttribute("key"),
            ((Element)nodes.item(i)).getAttribute("value")
          );
        }

        nodes = doc.getElementsByTagName("currencySymbol");
        String currencySymbol = nodes.item(0).getChildNodes().item(0).getNodeValue();

        nodes = doc.getElementsByTagName("decimalSymbol");
        char decimalSymbol = nodes.item(0).getChildNodes().item(0).getNodeValue().charAt(0);

        nodes = doc.getElementsByTagName("groupingSymbol");
        char groupingSymbol = nodes.item(0).getChildNodes().item(0).getNodeValue().charAt(0);

        nodes = doc.getElementsByTagName("dateFormat");
        String dateFormatStr = nodes.item(0).getChildNodes().item(0).getNodeValue();
        int dateFormat = -1;
        if (dateFormatStr.equals("DMY"))
          dateFormat = Resources.DMY;
        else if (dateFormatStr.equals("MDY"))
          dateFormat = Resources.MDY;
        else if (dateFormatStr.equals("YDM"))
          dateFormat = Resources.YDM;
        else if (dateFormatStr.equals("YMD"))
          dateFormat = Resources.YMD;

        nodes = doc.getElementsByTagName("showCenturyInDateFormat");
        boolean showCenturyInDateFormat = nodes.item(0).getChildNodes().item(0).getNodeValue().toLowerCase().equals("true");

        nodes = doc.getElementsByTagName("dateFormatSeparator");
        char dateFormatSeparator = nodes.item(0).getChildNodes().item(0).getNodeValue().charAt(0);

        nodes = doc.getElementsByTagName("timeFormat");
        String timeFormat = nodes.item(0).getChildNodes().item(0).getNodeValue();

        in.close();

        resources = new Resources(
          p,
          currencySymbol,
          decimalSymbol,
          groupingSymbol,
          dateFormat,
          showCenturyInDateFormat,
          dateFormatSeparator,
          timeFormat,
          langId,
          showResourceNotFoundWarning
        );
        allResources.put(langId,resources);
        return resources;
      }
      catch (Throwable ex) {
        String msg =
          "Error while loading internationalization settings for language '"+langId+"'"+
          "\nin file: "+new File(xmlFile).getAbsolutePath();
        System.err.println(msg);
        ex.printStackTrace();
        return this.resources;
      }
    }
  }



}
