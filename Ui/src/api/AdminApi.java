package api;

import com.google.gson.Gson;
import com.web.Authentication;
import com.web.ContextListener;
import objects.DealDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminApi", urlPatterns = "/adminApi")
public class AdminApi extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String username = gson.fromJson(request.getParameter("username"), String.class);
        if (username.equals("session")) {
            username = Authentication.getUsernameSession(request);
            if (username == null)
                response.sendRedirect("/");
        }

        String symbol = gson.fromJson(request.getParameter("symbol"), String.class);
        String listKind = gson.fromJson(request.getParameter("listKind"), String.class).toLowerCase();
        String json = "--";
        String res = "";
        if (ContextListener.DEBUG)
            System.out.println("in admin api " + listKind);
        if (!ContextListener.rse.checkUser(username))
            res = "There is no user with that name";
        if (res.equals("") && !ContextListener.rse.getUser(username).getObject().getType().equals("Admin"))
            res = username + " is not an admin";
        if (!ContextListener.rse.checkStock(symbol))
            res = "There is no stock with the symbol " + symbol;
        else {
            Map<String, List<DealDTO>> adminLists = new HashMap<>();
            switch (listKind) {
                case "all":
                    adminLists.put("Buy", ContextListener.rse.getAdminList(symbol, "Buy").getObject());
                    adminLists.put("Sell", ContextListener.rse.getAdminList(symbol, "Sell").getObject());
                    adminLists.put("Approved", ContextListener.rse.getAdminList(symbol, "Approved").getObject());
                    break;
                case "buy":
                    adminLists.put("Buy", ContextListener.rse.getAdminList(symbol, "Buy").getObject());
                    break;
                case "sell":
                    adminLists.put("Sell", ContextListener.rse.getAdminList(symbol, "Sell").getObject());
                    break;
                case "approved":
                    adminLists.put("Approved", ContextListener.rse.getAdminList(symbol, "Approved").getObject());
                    break;
            }
            json = gson.toJson(adminLists);
        }
        try (PrintWriter out = response.getWriter()) {
            if (json.equals("--")) {
                Map<String, String> msg = new HashMap<>();
                msg.put("error", res);
                json = gson.toJson(msg);
            }
            if (ContextListener.DEBUG)
                System.out.println(json);
            out.println(json);
            out.flush();
        }
    }
}
