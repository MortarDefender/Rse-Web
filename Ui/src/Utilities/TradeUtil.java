package Utilities;

import ExceptionsType.ExpType;
import com.google.gson.Gson;
import com.web.Authentication;
import com.web.ContextListener;
import objects.dto.AnswerDto;
import objects.interfaces.CommandAnswer;
import objects.dto.DealDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "Trade", urlPatterns = "/addTrade")
public class TradeUtil extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        if (ContextListener.DEBUG)
            System.out.println("in add Trade");

        String username = gson.fromJson(request.getParameter("username"), String.class);
        if (username.equals("session")) {
            username = Authentication.getUsernameSession(request);
            if (username == null)
                response.sendRedirect("/");
        }

        boolean action = gson.fromJson(request.getParameter("action"), boolean.class);
        String symbol = gson.fromJson(request.getParameter("symbol"), String.class);
        String command = gson.fromJson(request.getParameter("command"), String.class);
        int amount = gson.fromJson(request.getParameter("amount"), int.class);
        int rate = gson.fromJson(request.getParameter("rate"), int.class);

        if (ContextListener.DEBUG)
            System.out.printf("username: %s || action: %s || symbol: %s || command: %s || amount: %s || rate: %s%n", username, action, symbol, command, amount, rate);

        CommandAnswer<List<DealDTO>, String> ans;
        String json;

        switch (command) {
            case "lmt":
                ans = ContextListener.rse.LMT(symbol, action, amount, rate, username);
                break;
            case "mkt":
                ans = ContextListener.rse.MKT(symbol, action, amount, username);
                break;
            case "fok":
                ans = ContextListener.rse.FOK(symbol, action, amount, rate, username);
                break;
            case "ioc":
                ans = ContextListener.rse.IOC(symbol, action, amount, rate, username);
                break;
            default:
                ans = new AnswerDto<>(null, "There is no command " + command, ExpType.Failure);
        }

        if (ans.isSuccessful()) {
            List<DealDTO> approved = ans.getObject();
            json = gson.toJson(approved);
        }
        else {
            Map<String, String> msg = new HashMap<>();
            msg.put("error", ans.getExpMessage());
            json = gson.toJson(msg);
        }

        try (PrintWriter out = response.getWriter()) {
            if (ContextListener.DEBUG)
                System.out.println("trade json: " + json);
            out.println(json);
            out.flush();
        }
    }
}
