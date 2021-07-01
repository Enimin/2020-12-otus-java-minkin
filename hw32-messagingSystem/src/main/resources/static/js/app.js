let stompClient = null;

const setConnected = (connected) => {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#chatLine").show();
    } else {
        $("#chatLine").hide();
    }
    $("#message").html("");
}

const connect = () => {
    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        setConnected(true);
        getAllClients();
        stompClient.subscribe('/topic/response', (message) => showMessage(JSON.parse(message.body)));
    });
}

const disconnect = () => {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

const getAllClients = () => stompClient.send("/app/clients", {})

function formStringify(){
    const client = {};
    client["name"] = $("input[name=name]").val();
    client["address"] = $("input[name=address]").val();
    client["phones"] = $("input[name=phones]")
                        .map(function(){
                                return $(this).val()
                             })
                        .get();
    return JSON.stringify(client);
}
const saveClient = () => {stompClient.send("/app/client", {}, formStringify())}

function newRow(table){
    if(document.querySelector("td")){
        let rows = table.rows;
        let last_row = table.rows[table.rows.length - 1];
        var new_row = last_row.cloneNode(true);
    }
    else
    {
        var new_row = table.insertRow(table.rows.length);
        new_row.insertCell(0);
        new_row.insertCell(1);
        new_row.insertCell(2);
    }
    return new_row;
}

function clear(table){
    for(var i = table.rows.length - 1; i > 0; i--){
        table.deleteRow(i);
    }
}

function showMessage (message) {
    let table = document.querySelector("#table_clients");
    clear(table);
    for (let i = 0; i < message.length; i++){
        let row = newRow(table);
        row.cells[0].innerText = message[i].name;
        row.cells[1].innerText = message[i].address;
        row.cells[2].innerHTML = "";
        for (let j = 0; j < message[i].phones.length; j++){
            row.cells[2].innerHTML += message[i].phones[j] + "<br>";
        }
        table.append(row);
    }
}

function newPhone(but){

    let old_name = but.htmlFor;
    let new_name = but.htmlFor + "1";

    let label = document.querySelector("#label");
    let clone_label = label.cloneNode();
    clone_label.innerText = "-";
    clone_label.htmlFor = old_name;
    clone_label.id = "";
    clone_label.onclick =  function() { delPhone(clone_label); }
    but.before(clone_label);

    let br = document.createElement("br");
    but.before(br)

    let input = document.querySelector("#" + old_name);
    let clone_input = input.cloneNode();
    clone_input.id = new_name;
    clone_input.value = "";
    but.htmlFor = new_name;
    but.before(clone_input);
}

function delPhone(but){
    let input = document.querySelector("#" + but.htmlFor);
    let br = but.nextSibling;
    input.remove();
    br.remove();
    but.remove();
}

$(function () {
    connect();
    $("#save").click(saveClient);
});
