<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.RSE.Transaction" %>
<%@page import="com.web.ContextListener" %>
<%
    String username = request.getParameter("username");
    String kind = request.getParameter("userType");
%>
<html>
    <head>
        <meta charset="utf-8"/>
        <title><%= username %> Account</title>
        <meta content="stocks" property="og:title"/>
        <meta content="stocks" property="twitter:title"/>
        <meta content="width=device-width, initial-scale=1" name="viewport"/>
        <link href="../../css/account.css" rel="stylesheet" type="text/css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.26/webfont.js" type="text/javascript"></script>
        <link href="../prov.png" rel="shortcut icon" type="image/x-icon"/>
        <link href="../prov.png" rel="apple-touch-icon"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>
        <script>
            function alert_add_stock() {
                (async () => {
                    const { value: formValues } = await Swal.fire({
                        title: '<strong style="color: white; text-decoration: underline;">Add New Stock:</strong>',
                        background: "url(https://images.unsplash.com/photo-1565061326832-cd738486b695?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=934&q=80) center, center",
                        html:
                            '<label style="color: white;"> Company Name:</label><input type="text" id="swal-input1" name="companyName" class="swal2-input" style="color: white;" required>' +
                            '<label style="color: white;"> Stock Symbol:</label><input type="text" id="swal-input2" name="symbol" style="color: white;" class="swal2-input" required>' +
                            '<label style="color: white;"> Stock Quantity:</label><input type="text" id="swal-input3" name="quantity" style="color: white;" class="swal2-input" required>' +
                            '<label style="color: white;"> Company Total Value:</label><input type="text" id="swal-input3" name="totalValue" style="color: white;" class="swal2-input" required>',
                        focusConfirm: false,
                        preConfirm: () => {
                            let formValues = [
                                document.getElementById('swal-input1').value,
                                document.getElementById('swal-input2').value,
                                document.getElementById('swal-input3').value,
                                document.getElementById('swal-input4').value
                            ]

                            return $.post('/addStock', {
                                username: <%= username %>,
                                companyName: $(formValues)[0],
                                symbol: $(formValues)[1],
                                quantity: $(formValues)[2],
                                totalValue: $(formValues)[3],
                                    success: function(r) {
                                        alert(r);
                                    }}, function() { location.reload(true); });
                        }
                    })
                })()
            }
            function alert_add_charge() {
                (async () => {
                    const { value: formValues } = await Swal.fire({
                        title: '<strong style="color: white; text-decoration: underline;">Add New Credit Charge:</strong>',
                        background: "url(https://images.unsplash.com/photo-1574322663382-d78bf98abd36?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=934&q=80) center, center",
                        html:
                            '<label style="color: white;"> Amount:</label><input type="text" id="swal-input1" name="companyName" class="swal2-input" style="color: white;" required>',
                        focusConfirm: false,
                        preConfirm: () => {
                            let formValues = [
                                document.getElementById('swal-input1').value
                            ]

                            return $.post('/addCharge', {
                                username: <%= username %>,
                                amount: $(formValues)[0],
                                success: function(r) {
                                    alert(r);
                                }}, function(){location.reload(true);});
                        }
                    })
                })()
            }
            function alert_add_xml_file() {
                (async () => {
                    const { value: formValues } = await Swal.fire({
                        title: '<strong style="color: white; text-decoration: underline;">Add New Credit Charge:</strong>',
                        background: "url(https://images.unsplash.com/photo-1574322663382-d78bf98abd36?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=934&q=80) center, center",
                        html:
                            '<form>' +
                            '<div style="display: flex; margin-bottom: 15px;"><i class="fa fa-file-excel-o icon" style="font-size:30px;"></i>' +
                            '<input name="xmlFile" type="file" id="swal-input1" class="input-field" /></div>' +  // style="padding: 20px; color:white; font-size: 17px;"
                            '</form>',
                        focusConfirm: false,
                        preConfirm: () => {
                            let formValues = [
                                document.getElementById('swal-input1').value
                            ]

                            return $.post('/addXmlFile', {
                                username: <%= username %>,
                                xmlFile: $(formValues)[0],
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
                        <a href="/stocks"><li><i class="fa fa-list" aria-hidden="true" style="font-size:24px;"></i>  Stocks</li></a>
                        <% if (kind.equals("admin")) {%>
                            <a href="/admin"><li><i class="fa fa-list" aria-hidden="true" style="font-size:24px;"></i>  Admin</li></a>
                        <%}%>
                        <a href="/users"><li><i class="fa fa-users" style="font-size:24px;"></i>  Users</li></a>
                        <a href="/settings"><li><i class="fa fa-cog" aria-hidden="true" style="font-size:24px;"></i>  Settings</li></a>
                        <!-- String usernameFromSession = SessionUtils.getUsername(request); -->
                        <a href="/logout"><li><i class="fa fa-sign-out" aria-hidden="true" style="font-size:24px;"></i>  Logout</li></a>
                    </ul>
                </div>
            </nav>
            <!-- <i class="fa fa-file-excel-o" aria-hidden="true" onclick="getXmlFile()" style="position: fixed; top: 20px; right: 20px; color: white;font-size:40px;"></i> -->
            <div class="image-upload">
                <label for="file-input">
                    <i class="fa fa-file-excel-o" aria-hidden="true" style="position: fixed; top: 20px; right: 20px; color: white;font-size:40px;"></i>
                </label>
                <input id="file-input" type="file" />
            </div>
        </div>
        <div class="table-title">
            <h1><i class="fa fa-file-text-o" aria-hidden="true" style="font-size:50px;"></i> <%= username %> Account:</h1>
        </div>
        <div style="display: inline;">
            <div class="table-icon" style="float:left; padding-left: 10%;"><i class="fa fa-plus" aria-hidden="true" onclick="alert_add_stock()"></i></div>
            <div class="table-icon" id="credit"><i class="fa fa-credit-card-alt" aria-hidden="true" onclick="alert_add_charge()"></i></div>
        </div>
        <table class="table-fill">
            <thead>
                <tr>
                    <th class="text-left">Transaction Type</th>
                    <th class="text-left">Time</th>
                    <th class="text-left">Sum</th>
                    <th class="text-left">Account Before</th>
                    <th class="text-left">Account After</th>
                </tr>
            </thead>
            <tbody class="table-hover">
                <% for(Transaction transaction : ContextListener.rse.getUser(username).getTransactions()) {%>
                    <tr>
                        <td class="text-left" > <%= transaction.getType() %> </td >
                        <td class="text-left" > <%= transaction.getTime() %> </td >
                        <td class="text-left" > <%= transaction.getTransactionFee() %> </td >
                        <td class="text-left" > <%= transaction.getAccountBefore() %> </td >
                        <td class="text-left" > <%= transaction.getAccountAfter() %> </td >
                    </tr>
                <%}%>
            </tbody>
        </table>
    </body>
</html>
