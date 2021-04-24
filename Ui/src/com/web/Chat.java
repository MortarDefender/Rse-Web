package com.web;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Chat", urlPatterns = "/chat")
public class Chat extends HttpServlet {

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("text/html;charset=UTF-8");
        //response.sendRedirect("templates/html/chat.html");  // https://codepen.io/shivapandey/pen/dWdRYM  // https://codepen.io/Alca/pen/bWGMoz
        if (ContextListener.DEBUG)
            System.out.println("!!! Chat !!!");
        String[] users = request.getParameterValues("users");
        if (ContextListener.DEBUG) {
            for (String name : users)
                System.out.println("user selected: " + name);
        }
        Gson gson = new Gson();
        try (PrintWriter out = response.getWriter()) {
            out.println(gson.toJson("chat"));
            out.flush();
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

