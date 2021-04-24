package Utilities;

import com.web.Authentication;
import com.web.ContextListener;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@WebServlet(name = "FileUpload", urlPatterns = "/fileUpload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadUtil extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        Collection<Part> parts = request.getParts();
        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            fileContent.append("New Part content:").append("\n");
            fileContent.append(readFromInputStream(part.getInputStream())).append("\n");
        }

        String username = Authentication.getUsernameSession(request);
        if (username == null)
            response.sendRedirect("/");

        InputStream stream = new ByteArrayInputStream(fileContent.toString().getBytes());
        if (ContextListener.DEBUG)
            System.out.println("added successfully");
        try {
            ContextListener.rse.loadXml(stream, username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        response.sendRedirect("templates/html/account.html");
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}
