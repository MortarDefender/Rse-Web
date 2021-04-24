package com.web;

import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Auth", urlPatterns = "/auth")
public class Authentication extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        if (ContextListener.DEBUG)
            System.out.println("in auth");

        String sessionUsername = getUsernameSession(request);

        if (ContextListener.DEBUG)
            System.out.println("session username: " + sessionUsername);

        Map<String, String> msg = new HashMap<>();
        String json = "";
        if (sessionUsername == null) {
            // redirect to sign up ??
            String username = request.getParameter("username");
            boolean userType = !request.getParameter("userType").equals("Admin");

            if (ContextListener.DEBUG)
                System.out.println("username: " + username + " || kind: " + userType);
            if (ContextListener.um.checkUser(username)) {
                msg.put("error", "There is a username with the name: " + username + " please pick another username");
                if (ContextListener.DEBUG)
                    System.out.println("error " + "There is a username with the name: " + username + " please pick another username");
            }
            else {
                synchronized (this) {
                    if (!ContextListener.rse.checkUser(username))
                        ContextListener.rse.addUser(username, userType);
                    request.getSession(true).setAttribute("username", username);  // base64 encoding
                    msg.put("url", "/stocks");
                    ContextListener.um.addUser(username, userType ? "Stock Broker" : "Admin");  // userType
                    ContextListener.cm.addUserToChat("Everyone", username);
                }
            }
        } else
            msg.put("url", "/stocks");
        json = gson.toJson(msg);

        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
            // response.sendRedirect("/stocks");
    }

    public static String getUsernameSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("username") : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession (HttpServletRequest request) { request.getSession().invalidate(); }
}
