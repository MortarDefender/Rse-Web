<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.RSE.Stock" %>
<%@page import="com.RSE.Transaction" %>
<%@page import="com.web.ContextListener" %>
<%
    String username = request.getParameter("username"); // change
    String kind = request.getParameter("userType"); // change
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
        <link href="../../css/stockPage.css" rel="stylesheet" type="text/css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.26/webfont.js" type="text/javascript"></script>
        <link href="../prov.png" rel="shortcut icon" type="image/x-icon"/>
        <link href="../prov.png" rel="apple-touch-icon"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
        <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
        <script>
            function trade_stock(symbol) {
                (async () => {
                    const { value: formValues } = await Swal.fire({
                        title: '<strong style="color: white; text-decoration: underline;">Trade Stock:</strong>',
                        background: "url(https://images.unsplash.com/photo-1544679565-73c513036b97?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=934&q=80) center, center",
                        html:
                            '<label style="color: white;"> Action:</label><select id="swal-input4" name="rank" class="swal2-input" style="color: white;"><option value="buy" style="color: black;">Buy</option><option value="sell" style="color: black;">Sell</option></select>' +
                            '<label style="color: white;"> Command:</label><select id="swal-input4" name="command" class="swal2-input" style="color: white;"><option value="lmt" style="color: black;">Limit</option><option value="mkt" style="color: black;">Market</option>' +
                            '<option value="fok" style="color: black;">Fill Or Kill</option><option value="ioc" style="color: black;">Immediate Or Cancel</option></select>' +
                            '<label style="color: white;"> Amount:</label><input type="text" id="swal-input1" name="amount" class="swal2-input" style="color: white;" required>' +
                            '<label style="color: white;"> Rate:</label><input type="text" id="swal-input2" name="rate" style="color: white;" class="swal2-input">',
                        focusConfirm: false,
                        preConfirm: () => {
                            let formValues = [
                                document.getElementById('swal-input1').value,
                                document.getElementById('swal-input2').value,
                                document.getElementById('swal-input3').value,
                                document.getElementById('swal-input4').value
                            ]

                            return $.post('/addTrade', {
                                username: <%= username %>,
                                action: $(formValues)[0],
                                symbol: symbol,
                                command: $(formValues)[1],
                                amount: $(formValues)[2],
                                rate: $(formValues)[3],
                                success: function(r) {
                                    alert(r);
                                }}, function(){location.reload(true);});
                        }
                    })
                })()
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
                        <a href="/stocks"><li><i class="fa fa-user-secret" aria-hidden="true" style="font-size:24px;"></i>  Stocks</li></a>
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
        </div>
        <div class="table-title">
            <h1><span style="text-decoration: none;"><i class="fa fa-file-text-o" aria-hidden="true" style="font-size:50px;"></i>  </span><%= stockSym %> Trade Table</h1>
        </div>
        <div class="stock-info">
            <table>
                <tr class="table-row">
                    <th class="table-row2">Company Name:</th>
                    <td class="table-row"> <%= stock.getCompanyName() %> </td>
                </tr>
                <tr class="table-row">
                    <th class="table-row2">Stock Symbol:</th>
                    <td class="table-row"> <%= stock.getSymbol() %> </td>
                </tr>
                <tr class="table-row">
                    <th class="table-row2">Rate Per Share:</th>
                    <td class="table-row"> <%= stock.getRate() %> </td>
                </tr>
                <tr class="table-row">
                    <th class="table-row2">Revolution:</th>
                    <td class="table-row"> <%= stock.getRevolution() %> </td>
                </tr>
            </table>
            <div id="wrapper">
                <div id="chart"></div>
            </div>
        </div>
        <span style="padding-top: 15px;">___</span>
        <table class="table-fill">
            <thead>
                <tr class="table-row">
                    <th class="text-left" style="width: 33%;">Time</th>
                    <th class="text-left" style="width: 33%;">Quantity</th>
                    <th class="text-left">Rate Per Share</th>
                </tr>
            </thead>
            <tbody class="table-hover">
                <% for(Transaction transaction : stock.getTransactions()) { %>
                    <tr class="table-row">
                        <td class="text-left"> <%= transaction.getTime() %> </td>
                        <td class="text-left"> <%= transaction.getTransactionFee() // amount %> </td>
                        <td class="text-left"> <%= transaction.getTransactionFee() %> </td>
                    </tr>
                <%}%>
            </tbody>
        </table>
        <button class="trade-btn" onclick="trade_stock('symbol')"> Trade </button>
        <h2>___</h2>
        <script src="../js/chartLine.js"></script>
    </body>
</html>
