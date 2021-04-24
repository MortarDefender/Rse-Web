<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String symbol = request.getParameter("symbol"); %>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>stocks</title>
        <meta content="stocks" property="og:title"/>
        <meta content="stocks" property="twitter:title"/>
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <link href="../css/stockPage.css" rel="stylesheet" type="text/css"/>
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
        <script>
            function tradeAlert(data) {  // problem with a smaller screen size
                var table = "<table><tr>" +
                    "<th>Time</th><th>Action</th><th>Symbol</th><th>Amount</th><th>Rate</th>" +
                    "<th>Revolution</th><th>Publisher</th><th>status</th></tr>"
                for (var i = 0; i < data.length; i++) {
                    var columns = ["time", "action", "symbol", "amount", "rate", "revolution", "publisherName", "status"];
                    var element_start = "<td class='text-left'>";
                    var element_end = "</td>";
                    var end = "</tr>";
                    var obj = "<tr>";
                    for (var j = 0; j < columns.length; j++) {
                        obj += element_start + data[i][columns[j]] + element_end;
                    }
                    obj += end;
                    table += obj
                }
                table += "</table>";
                Swal.fire({
                    icon: 'success',
                    title: 'Deal Results:',
                    width: 1200,
                    html: table,
                    footer: ''
                })
            }
            function trade_stock(symbol) {
                (async () => {
                    const { value: formValues } = await Swal.fire({
                        title: '<strong style="color: white; text-decoration: underline;">Trade Stock:</strong>',
                        background: "url(https://images.unsplash.com/photo-1544679565-73c513036b97?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=934&q=80) center, center",
                        html:
                            '<label style="color: white;"> Action:</label><select id="swal-input1" name="rank" class="swal2-input" style="color: white;"><option value="true" style="color: black;">Buy</option><option value="false" style="color: black;">Sell</option></select>' +
                            '<label style="color: white;"> Command:</label><select id="swal-input2" name="rank" class="swal2-input" style="color: white;"><option value="lmt" style="color: black;">Limit</option><option value="mkt" style="color: black;">Market</option>' +
                            '<option value="fok" style="color: black;">Fill Or Kill</option><option value="ioc" style="color: black;">Immediate Or Cancel</option></select>' +
                            '<label style="color: white;"> Amount:</label><input type="number" id="swal-input3" name="amount" class="swal2-input" style="color: white; min-width: 100%;" required>' +
                            '<label style="color: white;"> Rate:</label><input type="number" id="swal-input4" name="rate" style="color: white; min-width: 100%;" class="swal2-input">',
                        focusConfirm: false,
                        preConfirm: () => {
                            let formValues = [
                                document.getElementById('swal-input1').value,
                                document.getElementById('swal-input2').value,
                                document.getElementById('swal-input3').value,
                                document.getElementById('swal-input4').value
                            ]

                            return $.post({dataType: "json",
                                url: '/addTrade',
                                data: {
                                    username: "session",
                                    action: $(formValues)[0],
                                    symbol: symbol,
                                    command: $(formValues)[1],
                                    amount: $(formValues)[2],
                                    rate: $(formValues)[3]
                                },
                                success: function (data) {
                                    if (data.error !== undefined) {
                                        Swal.fire({
                                            icon: 'error',
                                            title: 'Error In Trade:',
                                            width: 1200,
                                            html: data.error,
                                            footer: ''
                                        })
                                    } else {
                                        tradeAlert(data);
                                    }
                                    // alert(JSON.stringify(data));
                                }
                            });
                        }
                    })
                })()
            }

            function updateFunction() {
                setStockPage("<%= symbol %>");
            }

            $(document).ready(function() {
                updateFunction();
                startChart();
                addAccount("users");
                setInterval(users, 2000);
                setInterval(updateFunction, 2000);
            });
        </script>
        <script src="../js/loader.js"></script>
        <script src="../js/chatJs.js"></script>
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
                        <a href="?" id="users"><li><i class="fa fa-users" style="font-size:24px;"></i>  Users</li></a>
                        <a href="?"><li><i class="fa fa-cog" aria-hidden="true" style="font-size:24px;"></i>  Settings</li></a>
                        <a href="/logout"><li><i class="fa fa-sign-out" aria-hidden="true" style="font-size:24px;"></i>  Logout</li></a>
                    </ul>
                </div>
            </nav>
        </div>
        <div class="table-title">
            <h1 id="main-title">'Stock' Trade Table:</h1>
        </div>
        <div class="stock-info">
            <table>
                <tr class="table-row">
                    <th class="table-row2">Company Name:</th>
                    <td class="table-row" id="companyName">Tesla</td>
                </tr>
                <tr class="table-row">
                    <th class="table-row2">Stock Symbol:</th>
                    <td class="table-row" id="symbol">TS</td>
                </tr>
                <tr class="table-row">
                    <th class="table-row2">Rate Per Share:</th>
                    <td class="table-row" id="rate">120</td>
                </tr>
                <tr class="table-row">
                    <th class="table-row2">Revolution:</th>
                    <td class="table-row" id="revolution">500</td>
                </tr>
                <tr class="table-row">
                    <th class="table-row2">User Amount Stock:</th>
                    <td class="table-row" id="userAmount">50</td>
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
                    <th class="text-center" style="width: 33%;">Time</th>
                    <th class="text-center" style="width: 33%;">Quantity</th>
                    <th class="text-center">Rate Per Share</th>
                </tr>
            </thead>
            <tbody class="table-hover" id="tbody-main">

            </tbody>
        </table>
        <button class="trade-btn" onclick="trade_stock('<%= symbol %>')"> Trade </button>
        <h2>___</h2>
        <script src="../js/chart.js"></script>

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
    </body>
</html>
