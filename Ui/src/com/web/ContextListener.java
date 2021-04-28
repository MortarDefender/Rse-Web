package com.web;

import chat.ChatManager;
import chat.UserManager;
import chat.UsersCommunication;
import objects.interfaces.RSE;
import com.RSE.Engine;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;

public class ContextListener implements ServletContextListener {
    public static final boolean DEBUG = false;
    public static final RSE rse = new Engine();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("My web app is being initialized :)");
        UsersCommunication.addNewChat("Everyone", new HashSet<>()).ifFailure(System.out::println);
        try {
            rse.loadXml("C:\\Users\\Tamir\\Downloads\\ex2-small.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("My web app is being destroyed :(");
    }
}
