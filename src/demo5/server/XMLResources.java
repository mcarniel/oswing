package demo5.server;

import org.openswing.swing.internationalization.java.XMLResourcesFactory;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import java.util.Hashtable;
import org.openswing.swing.internationalization.java.Resources;
import javax.servlet.ServletContext;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Internalization settings factory: used server side.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class XMLResources extends ServerResourcesFactory {

  private XMLResourcesFactory factory = null;

  public XMLResources() {
  }


  /**
   * Method called by the server controller (Controller object) to initialize the factory.
   * @param context
   */
  public void init(ServletContext context) {
    Hashtable xmlFiles = new Hashtable();
    xmlFiles.put("EN",this.getClass().getResource("/").getPath()+"Resources_en.xml");
    xmlFiles.put("IT",this.getClass().getResource("/").getPath()+"Resources_it.xml");
    factory = new XMLResourcesFactory(xmlFiles,true);
  }


  /**
   * Load dictionary, according to the specified language id.
   * @param langId language id identifier
   */
  public final void setLanguage(String langId) throws UnsupportedOperationException {
    factory.setLanguage(langId);
  }


  /**
   * @return internationalization settings, according with the current language
   */
  public final Resources getResources() {
    return factory.getResources();
  }


  /**
   * @param langId language id identifier
   * @return internationalization settings, according with the language specified
   */
  public final Resources getResources(String langId) throws UnsupportedOperationException {
    return factory.getResources(langId);
  }


}