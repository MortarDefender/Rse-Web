<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String symbol = request.getParameter("symbol"); %>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>stocks</title>
        <meta content="stocks" property="og:title"/>
        <meta content="stocks" property="twitter:title"/>
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <link href="../css/admin.css" rel="stylesheet" type="text/css"/>
        <link href="../css/chatUi.css" rel="stylesheet" type="text/css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.26/webfont.js" type="text/javascript"></script>
        <link href="prov.png" rel="shortcut icon" type="image/x-icon"/>
        <link href="prov.png" rel="apple-touch-icon"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
        <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-material-design/4.0.2/bootstrap-material-design.css"> <!---->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-alpha.6/css/bootstrap.min.css"> <!---->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-alpha.6/js/bootstrap.min.js" type="text/javascript"></script> <!---->
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"> <!---->
        <script src="../js/loader.js"></script>
        <script src="../js/chatJs.js"></script>
        <script>
            function updateFunction() {
                setAdminStockPage("<%= symbol %>");
            }

            $(document).ready(function() {
                updateFunction();
                startAdvChart();
                setInterval(users, 2000);
                setInterval(updateFunction, 2000);
            });
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
                        <a href="/stocks"><li><i class="fa fa-list" aria-hidden="true" style="font-size:24px;"></i>  Stocks</li></a>
                        <a href="?"><li><i class="fa fa-users" style="font-size:24px;"></i>  Users</li></a>
                        <a href="?"><li><i class="fa fa-cog" aria-hidden="true" style="font-size:24px;"></i>  Settings</li></a>
                        <a href="/logout"><li><i class="fa fa-sign-out" aria-hidden="true" style="font-size:24px;"></i>  Logout</li></a>
                    </ul>
                </div>
            </nav>
        </div>
        <div class="table-title">
            <h1>Admin <%= symbol %> Page</h1>
        </div>
        <div class="stock-info">
            <table>
                <tr>
                    <th class="table-row">Company Name:</th>
                    <td class="table-row" id="companyName">Tesla</td>
                </tr>
                <tr>
                    <th class="table-row">Stock Symbol:</th>
                    <td class="table-row" id="symbol">TS</td>
                </tr>
                <tr>
                    <th class="table-row">Rate Per Share:</th>
                    <td class="table-row" id="rate">120</td>
                </tr>
                <tr>
                    <th class="table-row">Revolution:</th>
                    <td class="table-row" id="revolution">500</td>
                </tr>
            </table>
            <div id="wrapper">
                <div id="chart-area"></div>
                <div id="chart-bar"></div>
            </div>
        </div>
        <h2 style="color: white; text-decoration: underline; padding-left: 10%; padding-top: 2%;">Buy Table:</h2>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-center">Time</th>
                    <th class="text-center">Deal Type</th>
                    <th class="text-center">Stock Amount</th>
                    <th class="text-center">Stock Rate</th>
                    <th class="text-center">Stock Symbol</th>
                </tr>
            </thead>
            <tbody class="table-hover" id="buy-tbody-main">
            </tbody>
        </table>
        <h2 style="color: white; text-decoration: underline; padding-left: 10%; padding-top: 2%;">Sell Table:</h2>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-center">Time</th>
                    <th class="text-center">Deal Type</th>
                    <th class="text-center">Stock Amount</th>
                    <th class="text-center">Stock Rate</th>
                    <th class="text-center">Stock Symbol</th>
                </tr>
            </thead>
            <tbody class="table-hover" id="sell-tbody-main">
            </tbody>
        </table>
        <h2 style="color: white; text-decoration: underline; padding-left: 10%; padding-top: 2%;">Approved Table:</h2>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-center">Time</th>
                    <th class="text-center">Deal Type</th>
                    <th class="text-center">Stock Amount</th>
                    <th class="text-center">Stock Rate</th>
                    <th class="text-center">Stock Symbol</th>
                </tr>
            </thead>
            <tbody class="table-hover" id="approved-tbody-main">
            </tbody>
        </table>
        <h1 style="color: transparent;">_____</h1>
        <div id="chat-circle" class="btn btn-raised">
            <div id="chat-overlay"></div>
            <i class="material-icons">speaker_phone</i>
        </div>
        <div class="chat-box">
            <div class="tab">
                <div class="chat-box-header" id="tab-header">
                    <i class="material-icons" style="cursor:pointer" onclick="addChat()">add</i>
                </div>
            </div>
            <div class="box">
                <div class="chat-box-header">
                    <span id="chat-name">Everyone Chat Room: </span>
                    <span class="chat-box-toggle"><i class="material-icons">close</i></span>
                </div>
                <div class="chat-box-body">
                    <div class="chat-box-overlay">
                    </div>
                    <div class="chat-logs">
                    </div>
                </div>
                <div class="chat-input">
                    <form>
                        <input type="text" id="chat-input" placeholder="Send a message..."/>
                        <button type="submit" class="chat-submit" id="chat-submit"><i class="material-icons">send</i></button>
                    </form>
                </div>
            </div>
        </div>
        <script src="../js/chartAdv.js"></script>
    </body>
</html>
