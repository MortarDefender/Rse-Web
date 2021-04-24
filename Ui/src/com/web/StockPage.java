package com.web;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "StockPage", urlPatterns = "/stockPage")
public class StockPage extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (ContextListener.DEBUG)
            System.out.println("stock page");
        Gson gson = new Gson();
        String symbol = gson.fromJson(request.getParameter("symbol"), String.class);
        String username = Authentication.getUsernameSession(request);  // get username only from session
        if (username == null) {
            response.setStatus(403);
            response.sendRedirect("/templates/html/Errors/403_error.html");
        }
        if (!ContextListener.rse.checkStock(symbol)) {
            response.setStatus(404);
            response.sendRedirect("/templates/html/Errors/404_error_user.html");
        }
        else {
            request.setAttribute("symbol", symbol);
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
