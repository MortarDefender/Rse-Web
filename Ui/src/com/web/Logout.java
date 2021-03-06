package com.web;

import chat.UsersCommunication;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "logout", urlPatterns = "/logout")
public class Logout extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (ContextListener.DEBUG) {
            System.out.println("logout");
            System.out.println("req: " + request.getSession().getAttribute("username"));
        }
        Gson gson = new Gson();
        String username;
        if (request.getSession().getAttribute("username") != null) {
            if (ContextListener.DEBUG)
                System.out.println("logout -> " + Authentication.getUsernameSession(request));
            username = Authentication.getUsernameSession(request);
            Authentication.clearSession(request);
        } else
            username = gson.fromJson(request.getParameter("username"), String.class);
        UsersCommunication.logoutAction(username);
        response.sendRedirect(request.getContextPath() + "/index.html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
