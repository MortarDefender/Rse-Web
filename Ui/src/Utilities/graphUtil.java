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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Graph", urlPatterns = "/graphUtil")
public class graphUtil extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String username = gson.fromJson(request.getParameter("username"), String.class);
        if (username.equals("session")) {
            username = Authentication.getUsernameSession(request);
            if (username == null)
                response.sendRedirect("/");
        }
        boolean api = gson.fromJson(request.getParameter("api"), boolean.class);
        String symbol = gson.fromJson(request.getParameter("symbol"), String.class);
        String[] json = {""};
        Map<String, String> msg = new HashMap<>();

        if (api)
            ContextListener.rse.apiGraph(symbol).doAction(item -> json[0] = gson.toJson(item), exp -> msg.put("error", exp));
        else
            ContextListener.rse.siteGraph(symbol).doAction(item -> json[0] = gson.toJson(item), exp -> msg.put("error", exp));

        if (msg.containsKey("error"))
            json[0] = gson.toJson(msg);

        if (ContextListener.DEBUG)
            System.out.println(json[0]);
        try (PrintWriter out = response.getWriter()) {
            out.println(json[0]);
            out.flush();
        }
    }
}
