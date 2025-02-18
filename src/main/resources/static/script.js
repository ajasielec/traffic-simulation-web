document.addEventListener("DOMContentLoaded", function () {
    const outputDiv = document.getElementById("output");
    const startBtn = document.getElementById("startBtn");
    const uploadBtn = document.getElementById("uploadBtn");
    const randomBtn = document.getElementById("randomBtn");
    const fileInputSection = document.getElementById("fileInputSection");

    console.log("Attempting to connect to WebSocket...");

    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = Stomp.over(socket);

    stompClient.debug = function(str) {
        console.log(str);
    };

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        outputDiv.innerText = "Connected. Waiting for simulation data...\n";

        stompClient.subscribe('/topic/status', function (message) {
            console.log('Received:', message.body);
            outputDiv.innerText += message.body + "\n";
            outputDiv.scrollTop = outputDiv.scrollHeight;
        });
    }, function(error) {
        console.error('STOMP error:', error);
        outputDiv.innerText = "Connection error: " + error;
    });

    startBtn.addEventListener("click", function () {
        fetch("http://localhost:8080/start?inputFile=input.json&outputFile=output.json", {
            method: "POST"
        })
            .then(response => response.text())
            .then(data => {
                outputDiv.innerText += "\n" + data + "\n";
            })
            .catch(error => console.error("Error starting simulation: ", error));
    });

    uploadBtn.addEventListener("click", function () {

    })

    randomBtn.addEventListener("click", function () {
        fetch("http://localhost:8080/startrandom", {
            method: "POST"
        })
            .then(response => response.text())
            .then(data => {
                outputDiv.innerText += "\n" + data + "\n";
            })
            .catch(error => console.error("Error starting simulation: ", error));
    });

});