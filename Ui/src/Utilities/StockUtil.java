package Utilities;

import com.google.gson.Gson;
import com.web.Authentication;
import com.web.ContextListener;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AddStock", urlPatterns = "/addStock")
public class StockUtil extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String username = gson.fromJson(request.getParameter("username"), String.class);
        if (username.equals("session")) {
            username = Authentication.getUsernameSession(request);
            if (username == null)
                response.sendRedirect("/");
        }

        String companyName = gson.fromJson(request.getParameter("companyName"), String.class);
        String symbol = gson.fromJson(request.getParameter("symbol"), String.class);
        int quantity = gson.fromJson(request.getParameter("quantity"), int.class);
        int totalValue = gson.fromJson(request.getParameter("totalValue"), int.class);

        if (ContextListener.DEBUG)
            System.out.printf("name: %s || symbol: %s || amount: %s || totalValue: %s%n", companyName, symbol, quantity, totalValue);

        Map<String, String> msg = new HashMap<>();

        ContextListener.rse.addUserStock(companyName, symbol, totalValue, quantity, username).doAction(
                item -> msg.put("message", "The stock has been added successfully to your collection"), exp -> msg.put("error", exp));

        String json = gson.toJson(msg);
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
