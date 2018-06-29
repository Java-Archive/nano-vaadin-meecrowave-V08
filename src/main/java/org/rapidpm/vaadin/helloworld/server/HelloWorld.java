package org.rapidpm.vaadin.helloworld.server;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.apache.meecrowave.Meecrowave;

import javax.servlet.annotation.WebServlet;

public class HelloWorld {

  public static void main(String[] args) {
    new Meecrowave(new Meecrowave.Builder() {
      {
//        randomHttpPort();
        setHttpPort(8080);
        setTomcatScanning(true);
        setTomcatAutoSetup(false);
        setHttp2(true);
      }
    })
        .bake()
        .await();
  }

  @WebServlet("/*")
  @VaadinServletConfiguration(productionMode = false, ui = MyUI.class)
  public static class MyProjectServlet extends VaadinServlet { }

  public static class MyUI extends UI {

    /**
     * Start editing here to create your
     * POC based on a Vaadin App.
     * To start the app, -> start the main Method.
     *
     * You will see in the log´s the randomly used port.
     *
     * @param request that is created by the first request to init the app
     */
    @Override
    protected void init(VaadinRequest request) {

      //create the components you want to use
      // and set the main component with setContent(..)
      final Layout layout = new VerticalLayout();
      layout
          .addComponent(new Button("click me",
                                   event -> layout.addComponents(new Label("clicked again"))
          ));

      //set the main Component
      setContent(layout);
    }

  }
}
