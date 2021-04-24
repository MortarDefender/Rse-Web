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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserUtil", urlPatterns = "/userUtil")
public class UserUtil extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        boolean info = gson.fromJson(request.getParameter("info"), boolean.class);
        String symbol = gson.fromJson(request.getParameter("symbol"), String.class);
        String username = gson.fromJson(request.getParameter("username"), String.class);
        if (username.equals("session")) {
            username = Authentication.getUsernameSession(request);
            if (username == null)
                response.sendRedirect("/");
        }

        if (ContextListener.DEBUG)
            System.out.printf("username: %s || info: %s\n", username, info);

        Map<String, String> msg = new HashMap<>();
        String[] json = {""};

        if (info) {
            switch (symbol) {
                case "--":
                    Map<String, String> data = ContextListener.um.getUsers();
                    List<Map<String, String>> users = new ArrayList<>(data.size());
                    for (String key : data.keySet()) {
                        Map<String, String> user = new HashMap<>();
                        user.put("username", key);
                        user.put("userType", data.get(key));
                        users.add(user);
                    }
                    json[0] = gson.toJson(users);
                    break;
                case "userType":
                    ContextListener.rse.getUser(username).doAction(item -> json[0] = gson.toJson(item.getType()), exp -> msg.put("error", exp));
                    break;
                case "totalRevolution":
                    ContextListener.rse.getUserRevolution(username).doAction(item -> json[0] = gson.toJson(item), exp -> msg.put("error", exp));
                    break;
                default:
                    ContextListener.rse.getUserStockAmount(username, symbol).doAction(item -> json[0] = gson.toJson(item), exp ->  msg.put("error", exp));
                    break;
            }
        }
        else
            ContextListener.rse.getTransactions(username).doAction(item -> json[0] = gson.toJson(item), exp ->  msg.put("error", exp));

        if (msg.containsKey("error"))
            json[0] = gson.toJson(msg);

        if (ContextListener.DEBUG)
            System.out.println("User Util: " + json[0]);

        try (PrintWriter out = response.getWriter()) {
            out.println(json[0]);
            out.flush();
        }
    }
}
