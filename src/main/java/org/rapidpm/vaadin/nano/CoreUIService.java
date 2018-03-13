package org.rapidpm.vaadin.nano;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.functions.CheckedSupplier;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.function.Supplier;

import static java.lang.Class.forName;
import static org.rapidpm.vaadin.nano.CoreUIService.MyUI.COMPONENT_SUPPLIER_TO_USE;

/**
 *
 */
public class CoreUIService {



  public static class HelloWorldSupplier implements ComponentSupplier {
    @Override
    public Component get() {
      return new Label("Hello World");
    }
  }

  @FunctionalInterface
  public static interface ComponentSupplier extends Supplier<Component> { }

  @PreserveOnRefresh
  @Push
  public static class MyUI extends UI implements HasLogger {
    public static final String COMPONENT_SUPPLIER_TO_USE = "COMPONENT_SUPPLIER_TO_USE";

    @Override
    protected void init(VaadinRequest request) {
      final String className = System.getProperty(COMPONENT_SUPPLIER_TO_USE);
      logger().info("class to load : " + className);
      ((CheckedSupplier<Class<?>>) () -> forName(className))
          .get() //TODO make it fault tolerant
          .flatMap((CheckedFunction<Class<?>, Object>) Class::newInstance)
          .flatMap((CheckedFunction<Object, ComponentSupplier>) ComponentSupplier.class::cast)
          .flatMap((CheckedFunction<ComponentSupplier, Component>) Supplier::get)
          .ifPresentOrElse(this::setContent,
                           failed -> logger().warning(failed)
          );
    }
  }

  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class CoreServlet extends VaadinServlet {
    //customize Servlet if needed


    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
      System.setProperty(COMPONENT_SUPPLIER_TO_USE, HelloWorldSupplier.class.getName());
      super.init(servletConfig);
    }
  }


}
