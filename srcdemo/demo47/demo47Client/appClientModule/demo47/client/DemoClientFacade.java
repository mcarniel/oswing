package demo47.client;

import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.openswing.swing.mdi.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Client Facade, called by the MDI Tree.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DemoClientFacade implements ClientFacade {

  private InitialContext ic;


  public DemoClientFacade() {
	try {
		Properties p = new Properties();
		p.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		p.setProperty("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
		p.setProperty(Context.PROVIDER_URL,"jnp://localhost:1099");			
		ic = new InitialContext(p);
	} catch (Throwable e) {
		e.printStackTrace();
	}	
  }

  
  
  public Object getRemote(String beanName) throws Exception {
	return ic.lookup("demo47EJBEAR/"+beanName+"/remote");
  } 
  

  public void getArticles() {
    new ArticlesController();
  }


  public void getCustomers() {
    new CustomersController();
  }


  public void getOrders() {
    new OrdersController();
  }
}
