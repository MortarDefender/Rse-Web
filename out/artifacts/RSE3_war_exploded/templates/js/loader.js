let interval = 200;

function getAll(URL, DATA, tableId, columns, title, flag, addOns) {
    $.post({dataType: "json",
        url: URL,
        data: DATA,
        success: function(data) {
            if (data.error !== undefined) {
                alert(data.error);
            } else {
                if (flag)
                    setSmallTable(data);
                else {
                    if (data.length === 0) {
                        createEmpty(tableId, title, columns.length);
                    } else {
                        if (DATA.symbol === "--") {
                            loadStocksDifference(tableId, data, columns, addOns);
                        }
                        else {
                            loadStocks(tableId, data, columns, addOns);
                        }
                    }
                }
            }
        },
        error: function (info) {
            alert("1err: " + JSON.stringify(info));
        }
    });
}

function getAdmin(URL, DATA, tablesId, columns, title) {
    $.post({dataType: "json",
        url: URL,
        data: DATA,
        success: function(data) {
            if (data.error !== undefined){
                alert(data.error);
            } else {
                var keys = ["Buy", "Sell", "Approved"];
                for(var i = 0; i < keys.length; i++) {
                    if (data[keys[i]].length === 0) {
                        createEmpty(tablesId[i], title, columns.length);
                    } else {
                        loadStocks(tablesId[i], data[keys[i]], columns, "");
                    }
                }
            }
        },
        error: function (info) {
            alert("2err: " + info);
        }
    });
}

function users() {
    getUsers("/userUtil", {username: "session", info: true, symbol: "--"});
}

function getUsers(URL, DATA) {
    $.post({dataType: "json",
        url: URL,
        data: DATA,
        success: function(data) {
            sleep(interval);
            if (data.error !== undefined) {
                alert(data.error);
            } else {
                ActiveUsers = [];
                CheckBoxUsers =  "<labe style='color: white; font-size: 30px; text-decoration: underline;'>select users:</label><br>";
                var table = "<table><tr><th>Username:</th><th>User Type</th></tr>";
                for(var i = 0; i < data.length; i++) {
                    ActiveUsers.push(data[i].username);
                    CheckBoxUsers += "<input style='font-size: 25px;' type='checkbox' id='users' name='users' value='" + data[i].username + "'><label style='color: white; font-size: 25px;'>" + data[i].username + "</label><br>";
                    var obj = "<tr>";
                    obj += "<td>" + data[i].username + "</td><td>" + data[i].userType + "</td>";
                    table += obj + "</tr>";
                }
                table += "</table>";
                UsersTable = table;
            }
        },
        error: function (info) {
            alert("3err: " + info);
        }
    });
}

function getGraph(symbol, flag) {
    $.post({dataType: "json",
        url: "/graphUtil",
        data: {
            username: "session",
            symbol: symbol,
            api: false
        },
        success: function(data) {
            if (data["yAxis"].length !== 0) {
                if (flag) {
                    createChart(data["yAxis"], data["xAxis"]);
                } else {
                    createAdvChart(data["yAxis"], data["xAxis"]);
                }
            }
        },
        error: function (info) {
            alert("4err: " + JSON.stringify(info));
        }
    });
}

function getAllStocks() {
    var data = {
        all: true,
        symbol: "--",
        info: false };
    getAll("/getStocks", data, "tbody-main", ["companyName", "symbol", "rate", "revolution"], "stocks", false, "trade");
}

function getAllTransaction() {
    var data = {
        info: false,
        symbol: "- ",
        username: "session"
    };
    getAll("/userUtil", data, "tbody-main", ["actionType", "time", "sum", "accountBefore", "accountAfter"], "transactions", false, "");
}

function stockPageHelper(symbol, tableId) {
    var data = {
        all: false,
        symbol: symbol,
        info: false
    };
    getAll("/getStocks", data, tableId, ["time", "amount", "rate"], "deals", false, "class=\"table-row\"");
    data.info = true;
    getAll("/getStocks", data, tableId, ["time", "quantity", "rate"], "deals", true, "class=\"table-row\"");
}

function setSmallTable(info) {
    $("#companyName").text(info.companyName);
    $("#symbol").text(info.symbol);
    $("#rate").text(info.rate);
    $("#revolution").text(info.revolution);
}

function getUserStockAmount(symbol) {
    $.post({dataType: "json",
        url: "/userUtil",
        data: {
            info: true,
            symbol: symbol,
            username: "session"},
        success: function(data) {
            $("#userAmount").text(data);
        },
        error: function (info) {
            alert("4err: " + JSON.stringify(info));
        }
    });
}

function setStockPage(symbol) {
    stockPageHelper(symbol, "tbody-main");
    getUserStockAmount(symbol);
    getGraph(symbol, true);
    $("#main-title").text(symbol + " Trade Table:");
}

function setAdminStockPage(symbol) {
    getGraph(symbol, false);
    getAll("/getStocks", {all: false, symbol: symbol, info: true}, "", [], "deals", true, "class=\"table-row\"");
    var data = {
        username: "session",
        symbol: symbol,
        listKind: "all"
    };
    var columns = ["time", "action", "amount", "rate", "symbol"];
    var tablesId = ["buy-tbody-main", "sell-tbody-main", "approved-tbody-main"];
    getAdmin("/adminApi", data, tablesId, columns, "deals");
}

function addAccount(hrefId) {
    $.post({
        dataType: "json",
        url: "/userUtil",
        data: {
            info: true,
            symbol: "userType",
            username: "session"
        },
        success: function (data) {
            if (data !== "Admin") {
                $("#" + hrefId).before("<a href=\"/account\"><li><i class=\"fa fa-credit-card-alt\" aria-hidden=\"true\" style=\"font-size:24px;\"></i>  Account</li></a>");
            }
        }
    });
}

function loadStocks(tableId, data, columns, addOns) {
    emptyList(tableId);
    for (var i = 0; i < data.length; i++) {
        var start = "";
        if (addOns === "trade") {
            start = "<tr onclick=\"trade_stock('" + data[i].symbol + "')\">";
        }
        else {
            start = "<tr " + addOns + ">";
        }
        var element_start = "<td class='text-center'>";
        var element_end = "</td>";
        var end = "</tr>";
        var obj = start;
        for (var j = 0; j < columns.length; j++) {
            obj += element_start + data[i][columns[j]] + element_end;
        }
        obj += end;
        $("#" + tableId).append(obj);
    }
}

function loadStocksDifference(tableId, data, columns, addOns) {
    $.post({dataType: "json",
        url: "/getStocks",
        data: {
            all: true,
            symbol: "difference",
            info: false
        },
        success: function(dataDiff) {
            emptyList(tableId);
            for (var i = 0; i < data.length; i++) {
                var start = "";
                if (addOns === "trade") {
                    start = "<tr onclick=\"trade_stock('" + data[i].symbol + "')\">";
                }
                else {
                    start = "<tr " + addOns + ">";
                }
                var element_start = "<td class='text-center'>";
                var element_end = "</td>";
                var end = "</tr>";
                var advElement = "<td class='text-center' style=\"color: black;\">";
                var obj = start;
                for (var j = 0; j < columns.length; j++) {
                    obj += element_start + data[i][columns[j]] + element_end;
                }
                if (dataDiff[i] > 0) {
                    advElement += "<span class=\"material-icons\" style=\"color: green;\">trending_up</span>";
                }
                else if (dataDiff[i] === 0) {
                    advElement += "<span class=\"material-icons\" style=\"color: black;\">trending_flat</span>";
                }
                else {
                    advElement += "<span class=\"material-icons\" style=\"color: red;\">trending_down</span>";
                }
                obj += advElement + Math.abs(dataDiff[i]) + "%" + element_end;
                obj += end;
                $("#" + tableId).append(obj);
            }
        },
        error: function (info) {
            alert("5err: " + JSON.stringify(info));
        }
    });
}

function createEmpty(tableId, title, length) {
    var obj = "<tr><td colspan='" + length + "'><h3 style='text-align: center'> There is no " + title + " to show </h3></td></tr>";
    $("#" + tableId).empty().append(obj);
}

function emptyList(tableId) {
    $("#" + tableId).empty();
    sleep(interval);
}

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}
