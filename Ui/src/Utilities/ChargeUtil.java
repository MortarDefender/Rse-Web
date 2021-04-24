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

@WebServlet(name = "Charge", urlPatterns = "/addCharge")
public class ChargeUtil extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String username = gson.fromJson(request.getParameter("username"), String.class);
        if (username.equals("session")) {
            username = Authentication.getUsernameSession(request);
            if (username == null)
                response.sendRedirect("/");
        }
        int amount = gson.fromJson(request.getParameter("amount"), int.class);
        Map<String, String> msg = new HashMap<>();

        ContextListener.rse.addAccountCharge(username, amount).doAction(item ->
                msg.put("message", "The charge has been successfully added to your account"), exp -> msg.put("error", exp));

        String json = gson.toJson(msg);
        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
    }
}
