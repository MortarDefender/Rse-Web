package com.web;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Admin", urlPatterns = "/admin")
public class Admin extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (ContextListener.DEBUG)
            System.out.println("admin page");
        response.setContentType("text/html;charset=UTF-8");
        Gson gson = new Gson();
        String symbol = gson.fromJson(request.getParameter("symbol"), String.class);
        String username = Authentication.getUsernameSession(request);
        if (username == null) {
            response.setStatus(403);
            response.sendRedirect("/templates/html/Errors/403_error.html");
        }
        if (!ContextListener.rse.checkStock(symbol)) {
            response.setStatus(404);
            response.sendRedirect("/templates/html/Errors/404_error_user.html");
        }
        else {
            if (ContextListener.rse.getUser(username).getObject().getType().equals("Admin"))
                response.sendRedirect("templates/html/adminPage.jsp?symbol=" + symbol);
            else
                response.sendRedirect("templates/html/stockPage.jsp?symbol=" + symbol);
        }
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
