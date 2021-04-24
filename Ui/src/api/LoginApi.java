package api;

import com.google.gson.Gson;
import com.web.Authentication;
import com.web.ContextListener;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@WebServlet(name = "loginApi", urlPatterns = "/loginApi")
public class LoginApi extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();

        if (ContextListener.DEBUG)
            System.out.println("in auth");

        String sessionUsername = Authentication.getUsernameSession(request);

        if (ContextListener.DEBUG)
            System.out.println("session username: " + sessionUsername);

        Map<String, String> msg = new HashMap<>();
        String json = "";
        if (sessionUsername == null) {
            String username = request.getParameter("username");
            boolean userType = !request.getParameter("userType").equals("Admin");
            if (ContextListener.DEBUG)
                System.out.println("username: " + username + " || kind: " + userType);
            if (ContextListener.um.checkUser(username)) {
                msg.put("error", "There is a username with the name: " + username + " please pick another username");
                if (ContextListener.DEBUG)
                    System.out.println("error " + "There is a username with the name: " + username + " please pick another username");
            }
            else {
                synchronized (this) {
                    if (!ContextListener.rse.checkUser(username))
                        ContextListener.rse.addUser(username, userType);
                    // ContextListener.rse.addUser(username, userType).ifFailure( exp -> msg.put("error", exp));

                    request.getSession(true).setAttribute("username", username);  // base64 encoding
                    msg.put("key", getApiKey());
                    msg.put("message", "you have entered the system");
                    ContextListener.um.addUser(username, userType ? "Stock Broker" : "Admin");  // userType
                    ContextListener.cm.addUserToChat("Everyone", username);
                }
            }
        } else {
            msg.put("key", getApiKey());
            msg.put("message", "you have entered the system");
        }
        json = gson.toJson(msg);

        try (PrintWriter out = response.getWriter()) {
            out.println(json);
            out.flush();
        }
    }

    private String getApiKey() {  // dlc
        int length = 32;
        String res = "";
        String dictionary = "abcdefghijklmnopqrstuvwxyzABCDEEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&";
        Random rand = new Random();
        for (int i = 0; i < length; i++)
            res = res.concat("" + dictionary.charAt(rand.nextInt(dictionary.length())));
        return res;
    }
}

/*System.out.println("loginAPI");
        System.out.println("username: " + request.getParameter("username"));
        System.out.println("type: " + request.getParameter("userType"));
        Gson gson = new Gson();
        Map<String, String> res = new HashMap<>();
        HttpSession session = request.getSession(false);
        String username = gson.fromJson(request.getParameter("username"), String.class);
        if (username.equals("session")) {
            username = Authentication.getUsernameSession(request);
            if (username == null)
                res.put("error", "The username is none existent. please enter a username and user type");
        }
        boolean userType = !(request.getParameter("userType")).equals("Admin");
        String answer = "yes";

        System.out.println("2 username: " + username + " || type: " + userType);

        if (session == null && username != null) {
            if (ContextListener.um.checkUser(username)) {
                res.put("error", "There is a username with the name: " + username + " please pick another username");
                System.out.println("error " + "There is a username with the name: " + username + " please pick another username");
            } else {
                try {
                    ContextListener.rse.addUser(username, userType);
                    request.getSession(true).setAttribute("username", username);
                    ContextListener.um.addUser(username, userType ? "Stock Broker" : "Admin");
                } catch (InvalidParameterException e) {
                    answer = e.getMessage();
                }
            }
        }
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {

            if (answer.equals("yes")) {
                res.put("key", getApiKey());
                res.put("message", answer);
            }
            else
                res.put("error", answer);
            System.out.println(res.get("key"));
            String json = gson.toJson(res);
            out.println(json);
            out.flush();
        }*/