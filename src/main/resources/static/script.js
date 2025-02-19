document.addEventListener("DOMContentLoaded", function () {
    const outputDiv = document.getElementById("output");
    const startBtn = document.getElementById("startBtn");
    const uploadBtn = document.getElementById("uploadBtn");
    const randomBtn = document.getElementById("randomBtn");
    const fileInputSection = document.getElementById("fileInputSection");
    let isCustomFile = false;

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
        const inputFile = document.getElementById("inputFile").value;
        const outputFile = document.getElementById("outputFile").value;

        console.log("Sending request with input file:", inputFile);
        console.log("Sending request with output file:", outputFile);

        if (!inputFile || !outputFile) {
            alert("Please enter both input and output file names");
            return;
        }

        fetch(`http://localhost:8080/start?inputFile=${inputFile}&outputFile=${outputFile}`, {
            method: "POST"
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text();
            })
            .then(data => {
                outputDiv.innerText += "\n" + data + "\n";
            })
            .catch(error => {
                console.error("Error starting simulation: ", error);
                outputDiv.innerText += "\nError: " + error.message + "\n";
            });
    });

    uploadBtn.addEventListener("click", function () {
        fileInputSection.style.display = "block";
        startBtn.style.display = "block";
        isCustomFile = true;
        randomBtn.style.backgroundColor = "#808080";
        uploadBtn.style.backgroundColor = "#4CAF50";
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