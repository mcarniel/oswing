package org.openswing.swing.export.java;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: This class is used to instantiate at run time the
 * PDF engine to use for exporting grid data in PDF format.
 * The specific implementation to instantiate depends on the java version;
 * according to java version, a specific version of iText must be used.</p>
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
public class ExportToPDFFactory {


  public static byte[] createExportToPDFDocument(ExportOptions opt) throws Throwable {
    Class clazz = null;
    if (System.getProperty("java.version").indexOf("1.4") != -1) {
      clazz = Class.forName("org.openswing.swing.export.java.ExportToPDF14");
    }
    else {
      try {
      clazz = Class.forName("org.openswing.swing.export.java.ExportToPDF15");
      }
      catch (Throwable ex) {
        clazz = Class.forName("org.openswing.swing.export.java.ExportToPDF14");
      }
    }

    return (byte[])clazz.getMethod("getDocument",new Class[]{ExportOptions.class}).invoke(clazz.newInstance(),new Object[]{opt});
  }


}
