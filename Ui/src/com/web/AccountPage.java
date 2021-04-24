package com.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Account", urlPatterns = "/account")
public class AccountPage extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (ContextListener.DEBUG)
            System.out.println("account page");
        response.setContentType("text/html;charset=UTF-8");
        String username = Authentication.getUsernameSession(request);
        if (username == null) {
            response.setStatus(403);
            response.sendRedirect("/templates/html/Errors/403_error.html");
        }
        if (ContextListener.rse.getUser(username).getObject().getType().equals("Admin")) {
            response.setStatus(403);
            response.sendRedirect("/templates/html/Errors/403_error_user.html");
        }
        else
            response.sendRedirect("templates/html/account.html");
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
