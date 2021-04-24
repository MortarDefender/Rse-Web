<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.RSE.Stock" %>
<%@page import="com.web.ContextListener" %>
<% String kind = request.getParameter("userType"); %>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>Stocks</title>
        <meta content="stocks" property="og:title"/>
        <meta content="stocks" property="twitter:title"/>
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <link href="../../css/stocks.css" rel="stylesheet" type="text/css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.26/webfont.js" type="text/javascript"></script>
        <link href="../prov.png" rel="shortcut icon" type="image/x-icon"/>
        <link href="../prov.png" rel="apple-touch-icon"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
        <script>
            function sideMenu() {
                Swal.fire({
                    title: '<h3 style="text-decoration: underline;">Active Users:</h3>',
                    html: '',
                    position: 'top-end',
                    showClass: {
                        popup: `
                          animate__animated
                          animate__fadeInRight
                          animate__faster
                        `
                    },
                    hideClass: {
                        popup: `
                          animate__animated
                          animate__fadeOutRight
                          animate__faster
                        `
                    },
                    grow: 'column',
                    width: 300,
                    showConfirmButton: false,
                    showCloseButton: true
                })
            }
            function stockPage(stockSym) {
                window.location.replace("/stockPage?stock=" + stockSym);
            }
        </script>
    </head>
    <body class="body-2">
        <div>
            <nav role="navigation">
                <div id="menuToggle">
                    <input type="checkbox" />
                    <span></span>
                    <span></span>
                    <span></span>
                    <ul id="menu">
                        <a href="/"><li><i class="fa fa-home" aria-hidden="true" style="font-size:24px;"></i>  Home</li></a>
                        <a href="/account"><li><i class="fa fa-user-secret" aria-hidden="true" style="font-size:24px;"></i>  Account</li></a>
                        <% if (kind.equals("admin")) {%>
                            <a href="/admin"><li><i class="fa fa-list" aria-hidden="true" style="font-size:24px;"></i>  Admin</li></a>
                        <%}%>
                        <a href="/users"><li><i class="fa fa-users" style="font-size:24px;"></i>  Users</li></a>
                        <a href="/settings"><li><i class="fa fa-cog" aria-hidden="true" style="font-size:24px;"></i>  Settings</li></a>
                        <a href="/logout"><li><i class="fa fa-sign-out" aria-hidden="true" style="font-size:24px;"></i>  Logout</li></a>
                    </ul>
                </div>
            </nav>
            <i class="fa fa-users" aria-hidden="true" onclick="sideMenu()" style="position: fixed; top: 20px; right: 20px; color: white;font-size:24px;"></i>
        </div>
        <div class="table-title">
            <h1><i class="fa fa-file-text-o" aria-hidden="true" style="font-size:50px;"></i> Stock Table</h1>
        </div>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-left">Company Name</th>
                    <th class="text-left">Symbol</th>
                    <th class="text-left">Rate Per Share</th>
                    <th class="text-left">Revolution</th>
                </tr>
            </thead>
            <tbody class="table-hover">
                <% for(Stock stock : ContextListener.rse.getStocks()) {%>
                    <tr onclick="stockPage('<%= stock.getSymbol() %>')">
                        <td class="text-left"><%= stock.getCompanyName() %></td>
                        <td class="text-left"><%= stock.getSymbol() %></td>
                        <td class="text-left"><%= stock.getRate() %></td>
                        <td class="text-left"><%= stock.getRevolution() %></td>
                    </tr>
                <%}%>
            </tbody>
        </table>
    </body>
</html>
