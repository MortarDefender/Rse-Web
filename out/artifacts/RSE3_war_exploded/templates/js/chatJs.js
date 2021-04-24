var INDEX = 0;
var TIME = "";
var ActiveUsers = [];
var CheckBoxUsers = "";
var ChatInterval;
var ChatRoomsList = [];
var CurrChatRoom = "Everyone";

$(document).ready(function() {
    setInterval(getChatRooms, 2000);
    chatAction("info", "first", CurrChatRoom);
    ChatInterval = setInterval(function () { chatAction("info", "", CurrChatRoom);}, 2000);

    $("#chat-submit").click(function(e) {
        e.preventDefault();
        var msg = $("#chat-input").val();
        if(msg.trim() === '') {
            return false;
        }
        chatAction("new_message", msg, CurrChatRoom);
    })

    $(document).delegate(".chat-btn", "click", function() {
        var value = $(this).attr("chat-value");
        var name = $(this).html();
        $("#chat-input").attr("disabled", false);
        generate_message(name, 'self');
    })

    $("#chat-circle").click(function() {
        $("#chat-circle").toggle('scale');
        $(".chat-box").toggle('scale');
    })

    $(".chat-box-toggle").click(function() {
        $("#chat-circle").toggle('scale');
        $(".chat-box").toggle('scale');
    })

});

function chatAction(action, message, chatName) {  // http://localhost:8080/
    if (action === "new_message") {
        generate_message(message, 'self');
    }
    $.post({ // dataType: "json",
        cache: false,
        url: "/chatUtil",
        data: {
            action: action,
            message :  message,
            chatName:  chatName, // "Everyone",
            username: "session"
        },
        success: function(info) {
            var data = JSON.parse(info);
            if (data.error !== undefined) {
                alert("chat error 1: " +  data.error);
            } else {
                if ((action === "info") && (data.length !== 0)) {
                    var flag = false;
                    var userType = "user";
                    var chatMessage = "";
                    if (TIME === "" || message === "first") {
                        flag = true;
                    }
                    for(var i = 0; i < data.length; i++) {
                        if ((flag === true) || (data[i].time > TIME)) {
                            if ((data[i].username === "session") && (message === "first")) {
                                userType = "self";
                                chatMessage = data[i].message;
                            } else {
                                userType = "user";
                                chatMessage = data[i].username + ": " + data[i].message;
                            }
                            generate_message( chatMessage, userType);
                            TIME = data[i].time;
                        }
                    }
                }
            }
        },
        error: function (info) {
            alert("chat err: " + JSON.stringify(info));
        }
    });
}

function generate_message(msg, type) {
    INDEX++;
    var str="";
    str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
    str += "          <span class=\"msg-avatar\">";
    str += "            <span class=\"material-icons\">person</span>";
    str += "          <\/span>";
    str += "          <div class=\"cm-msg-text\">";
    str += msg;
    str += "          <\/div>";
    str += "        <\/div>";
    $(".chat-logs").append(str);
    $("#cm-msg-" + INDEX).hide().fadeIn(300);
    if(type === 'self') {
        $("#chat-input").val('');
    }
    $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight}, 1000);
}

function getChatRooms() {
    $.post({
        cache: false,
        url: "/chatUtil",
        data: {
            action: "rooms_info",
            username: "session",
            message :  "",
            chatName:  ""
        },
        success: function(info) {
            var data = JSON.parse(info);
            console.log(data);
            if (data.error !== undefined) {
                alert("chat error 1: " +  data.error);
            } else {
                for (var i = 0; i < data.length; i++) {
                    var found = false;
                    for (var j = 0; j < ChatRoomsList.length; j++) {
                        if (ChatRoomsList[j] === data[i]) {
                            found = true;
                        }
                    }
                    if (!found) {
                        ChatRoomsList.push(data[i]);
                        addChatIcon(data[i]);
                    }
                }
            }
        },
        error: function (info) {
            alert("chat err: " + JSON.stringify(info));
        }
    });
}

function addChatIcon(chatName) {
    $("#tab-header").append("<div><h6 style='color: black; text-align: center; padding-top: 20px;' onclick='getChatRoom(\"" + chatName + "\")'>" + chatName + "</h6></div>");
}

function getChatRoom(chatName) {
    CurrChatRoom = chatName;
    $("#chat-name").text(chatName + " Chat Room:");
    $(".chat-logs").empty(); // clear
    sleep(interval);
    chatAction("info", "first", CurrChatRoom);
    clearInterval(ChatInterval);
    ChatInterval = setInterval(function () { chatAction("info", "", CurrChatRoom);}, 2000);
}

function generate_button_message(msg, buttons){
    /* Buttons should be object array
      [
        {
          name: 'Existing User',
          value: 'existing'
        },
        {
          name: 'New User',
          value: 'new'
        }
      ]
    */
    INDEX++;
    var btn_obj = buttons.map(function(button) {
        return  "              <li class=\"button\"><a href=\"javascript:;\" class=\"btn btn-primary chat-btn\" chat-value=\""+button.value+"\">"+button.name+"<\/a><\/li>";
    }).join('');
    var str="";
    str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg user\">";
    str += "          <span class=\"msg-avatar\">";
    str += "            <img src=\"https:\/\/image.crisp.im\/avatar\/operator\/196af8cc-f6ad-4ef7-afd1-c45d5231387c\/240\/?1483361727745\">";
    str += "          <\/span>";
    str += "          <div class=\"cm-msg-text\">";
    str += msg;
    str += "          <\/div>";
    str += "          <div class=\"cm-msg-button\">";
    str += "            <ul>";
    str += btn_obj;
    str += "            <\/ul>";
    str += "          <\/div>";
    str += "        <\/div>";
    $(".chat-logs").append(str);
    $("#cm-msg-"+INDEX).hide().fadeIn(300);
    $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight}, 1000);
    $("#chat-input").attr("disabled", true);
}

function addChat() {
    Swal.fire({
        title: '<span style="color: white; text-decoration: underline; font-weight: bold;"> Select Users: </span>',
        width: 400,
        background: "url(https://images.unsplash.com/photo-1540144965158-050c0b7769ed?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1951&q=80) center, center",
        html: '<label style="color: white; font-size: 30px; text-decoration: underline;"> chat name:</label><br>' +
            '<input type="text" id="chatName" name="chat_name" style="color: white; font-size: 25px;" class="swal2-input" required><br>' + CheckBoxUsers,
        showConfirmButton: true,
        preConfirm: () => {
            let formValues = [
                document.getElementById('chatName').value,
                document.getElementsByName("users")
            ]

            console.log("users length: " + formValues[1].length);
            var selectedUsers = [];
            for(var i = 0; i < formValues[1].length; i++) {
                if (formValues[1][i].checked === true) {
                    selectedUsers.push(formValues[1][i].value);
                }
            }
            console.log("users selected: " + selectedUsers);

            return $.post({
                dataType: "json",
                data: {
                    message: "",
                    username: "session",
                    action: "new_chat",
                    chatName: formValues[0],
                    users:  JSON.stringify(selectedUsers).substring(1, JSON.stringify(selectedUsers).length - 1)
                },
                url: "/chatUtil",
                timeout: 2000,
                error: function(errorObject) {
                    console.error("Failed to select !");
                    alert("error  " + JSON.stringify(errorObject.responseText));
                },
                success: function(info) {
                    if (info.error !== undefined) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error In Creating New Chat:',
                            width: 400,
                            html: info.error
                        })
                    } else {
                        Swal.fire({
                            icon: 'success',
                            title: 'new chat:',
                            width: 400,
                            html: info.message
                        })
                        ChatRoomsList.push(formValues[0]);
                        addChatIcon(formValues[0]);
                    }
                }
            });
        }
    })
}