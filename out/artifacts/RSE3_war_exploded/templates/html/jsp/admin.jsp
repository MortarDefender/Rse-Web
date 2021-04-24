<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.RSE.Deal" %>
<%@page import="com.RSE.Stock" %>
<%@page import="com.RSE.Transaction" %>
<%@page import="com.web.ContextListener" %>
<%
    String stockSym = request.getParameter("stock");
    Stock stock = ContextListener.rse.getStock(stockSym);
%>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>stocks</title>
        <meta content="stocks" property="og:title"/>
        <meta content="stocks" property="twitter:title"/>
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <link href="../../css/admin.css" rel="stylesheet" type="text/css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.26/webfont.js" type="text/javascript"></script>
        <link href="../prov.png" rel="shortcut icon" type="image/x-icon"/>
        <link href="../prov.png" rel="apple-touch-icon"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
        <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
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
                        <a href="/stocks"><li><i class="fa fa-list" aria-hidden="true" style="font-size:24px;"></i>  Stocks</li></a>
                        <a href="/users"><li><i class="fa fa-users" style="font-size:24px;"></i>  Users</li></a>
                        <a href="/settings"><li><i class="fa fa-cog" aria-hidden="true" style="font-size:24px;"></i>  Settings</li></a>
                        <a href="/logout"><li><i class="fa fa-sign-out" aria-hidden="true" style="font-size:24px;"></i>  Logout</li></a>
                    </ul>
                </div>
            </nav>
        </div>
        <div class="table-title">
            <h1><i class="fa fa-file-text-o" aria-hidden="true" style="font-size:50px;"></i> Admin <%= stock.getSymbol() %> Page</h1>
        </div>
        <div class="stock-info">
            <table>
                <tr>
                    <th class="table-row">Company Name:</th>
                    <td class="table-row"> <%= stock.getCompanyName() %> </td>
                </tr>
                <tr>
                    <th class="table-row">Stock Symbol:</th>
                    <td class="table-row"> <%= stock.getSymbol() %> </td>
                </tr>
                <tr>
                    <th class="table-row">Rate Per Share:</th>
                    <td class="table-row"> <%= stock.getRate() %> </td>
                </tr>
                <tr>
                    <th class="table-row">Revolution:</th>
                    <td class="table-row"> <%= stock.getRevolution() %> </td>
                </tr>
            </table>
            <div id="wrapper">
                <div id="chart-area"></div>
                <div id="chart-bar"></div>
            </div>
        </div>
        <h2 style="color: white; text-decoration: underline; padding-left: 10%; padding-top: 2%;"><%= stock.getSymbol() %> Information:</h2>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-left" style="width: 33%;">Time</th>
                    <th class="text-left" style="width: 33%;">Quantity</th>
                    <th class="text-left">Rate Per Share</th>
                </tr>
            </thead>
            <tbody class="table-hover">
                <% for(Transaction transaction : stock.getTransaction()) {%>
                    <tr>
                        <td class="text-left"> <%= transaction.getTime() %> </td>
                        <td class="text-left"> <%= transaction.getTransactionFee() %> </td>
                        <td class="text-left"> <%= transaction.getTransactionFee() %> </td>
                    </tr>
                <%}%>
            </tbody>
        </table>
        <h2 style="color: white; text-decoration: underline; padding-left: 10%; padding-top: 2%;">Buy Table:</h2>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-left">Time</th>
                    <th class="text-left">Deal Type</th>
                    <th class="text-left">Stock Amount</th>
                    <th class="text-left">Stock Rate</th>
                    <th class="text-left">Stock Symbol</th>
                </tr>
            </thead>
            <tbody class="table-hover">
                <% for(Deal deal : stock.getBuyDeals()) {%>
                    <tr>
                        <td class="text-left"> <%= deal.getTime() %> </td>
                        <td class="text-left"> <%= deal.getAction() %> </td>
                        <td class="text-left"> <%= deal.getAmount() %> </td>
                        <td class="text-left"> <%= deal.getRate() %> </td>
                        <td class="text-left"> <%= deal.getSymbol() %> </td>
                    </tr>
                <%}%>
            </tbody>
        </table>
        <h2 style="color: white; text-decoration: underline; padding-left: 10%; padding-top: 2%;">Sell Table:</h2>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-left">Time</th>
                    <th class="text-left">Deal Type</th>
                    <th class="text-left">Stock Amount</th>
                    <th class="text-left">Stock Rate</th>
                    <th class="text-left">Stock Symbol</th>
                </tr>
            </thead>
            <tbody class="table-hover">
                <% for(Deal deal : stock.getSellDeals()) {%>
                    <tr>
                        <td class="text-left"> <%= deal.getTime() %> </td>
                        <td class="text-left"> <%= deal.getAction() %> </td>
                        <td class="text-left"> <%= deal.getAmount() %> </td>
                        <td class="text-left"> <%= deal.getRate() %> </td>
                        <td class="text-left"> <%= deal.getSymbol() %> </td>
                    </tr>
                <%}%>
            </tbody>
        </table>
        <h2 style="color: white; text-decoration: underline; padding-left: 10%; padding-top: 2%;">Approved Table:</h2>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-left">Time</th>
                    <th class="text-left">Deal Type</th>
                    <th class="text-left">Stock Amount</th>
                    <th class="text-left">Stock Rate</th>
                    <th class="text-left">Stock Symbol</th>
                </tr>
            </thead>
            <tbody class="table-hover">
                <% for(Deal deal : stock.getApprovedDeals()) {%>
                    <tr>
                        <td class="text-left"> <%= deal.getTime() %> </td>
                        <td class="text-left"> <%= deal.getAction() %> </td>
                        <td class="text-left"> <%= deal.getAmount() %> </td>
                        <td class="text-left"> <%= deal.getRate() %> </td>
                        <td class="text-left"> <%= deal.getSymbol() %> </td>
                    </tr>
                <%}%>
            </tbody>
        </table>
        <h1 style="color: transparent;">_____</h1>
        <script src="../js/chartAdv.js"></script>
    </body>
</html>
