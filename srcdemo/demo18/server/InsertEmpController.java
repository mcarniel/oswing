package demo18.server;

import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.openswing.springframework.web.servlet.view.OpenSwingViewResolver;
import org.openswing.swing.message.send.java.Command;
import org.openswing.springframework.web.servlet.handler.OpenSwingHandlerMapping;
import demo18.java.EmpVO;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Spring Controller used to insert an emp.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class InsertEmpController implements Controller {

  private DemoFacade facade;


  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mav = new ModelAndView();
    Command command = (Command)request.getAttribute(OpenSwingHandlerMapping.COMMAND_ATTRIBUTE_NAME);
    mav.addObject(
      OpenSwingViewResolver.RESPONSE_PROPERTY_NAME,
      facade.insertEmp((EmpVO)command.getInputParam())
    );

    return mav;

  }


  public DemoFacade getFacade() {
    return facade;
  }
  public void setFacade(DemoFacade facade) {
    this.facade = facade;
  }


}
