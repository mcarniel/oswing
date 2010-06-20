package org.openswing.swing.export.java;

public class ExportToRTFFactory {

  public static byte[] createExportToRTFDocument(ExportOptions opt) throws Throwable {
    Class clazz = null;
    if (System.getProperty("java.version").indexOf("1.4") != -1) {
      clazz = Class.forName("org.openswing.swing.export.java.ExportToRTF14");
    }
    else {
      try {
      clazz = Class.forName("org.openswing.swing.export.java.ExportToRTF15");
      }
      catch (Throwable ex) {
        clazz = Class.forName("org.openswing.swing.export.java.ExportToRTF14");
      }
    }

    return (byte[])clazz.getMethod("getDocument",new Class[]{ExportOptions.class}).invoke(clazz.newInstance(),new Object[]{opt});
  }


}
