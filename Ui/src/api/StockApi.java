package api;

import com.google.gson.Gson;
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

@WebServlet(name = "stocksApi", urlPatterns = "/getStocks")
public class StockApi extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        boolean all = gson.fromJson(request.getParameter("all"), boolean.class);
        String symbol = gson.fromJson(request.getParameter("symbol"), String.class);
        boolean info = gson.fromJson(request.getParameter("info"), boolean.class);

        if (ContextListener.DEBUG)
            System.out.println("in get stocks");

        if (symbol == null)
            symbol = "";
        String[] json = {""};
        Map<String, String> msg = new HashMap<>();

        if (all) {
            if (symbol.equals("difference"))
                json[0] = gson.toJson(ContextListener.rse.getStocksDifference());
            else if (info)
                json[0] = gson.toJson(ContextListener.rse.getStocks(symbol)); // symbol == username
            else
                json[0] = gson.toJson(ContextListener.rse.getStocks());
        }
        else if (info)
            ContextListener.rse.getStock(symbol).doAction(item -> json[0] = gson.toJson(item), exp -> msg.put("error", exp));
        else
            ContextListener.rse.getStockDeals(symbol).doAction(item -> json[0] = gson.toJson(item), exp -> msg.put("error", exp));
        if (msg.containsKey("error"))
            json[0] = gson.toJson(msg);

        try (PrintWriter out = response.getWriter()) {
            out.println(json[0]);
            out.flush();
        }
    }
}
