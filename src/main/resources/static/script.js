document.addEventListener("DOMContentLoaded", function () {
    const outputDiv = document.getElementById("output");
    const uploadBtn = document.getElementById("uploadBtn");
    const randomBtn = document.getElementById("randomBtn");
    const fileInputSection = document.getElementById("fileInputSection");
    const randomInputSection = document.getElementById("randomInputSection");
    const startBtn = document.getElementById("startBtn");
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

    uploadBtn.addEventListener("click", function () {
        fileInputSection.style.display = "block";
        randomInputSection.style.display = "none";
        startBtn.style.display = "block";
        isCustomFile = true;
        randomBtn.style.backgroundColor = "#808080";
        uploadBtn.style.backgroundColor = "#cfb46f";
    });

    randomBtn.addEventListener("click", function () {
        fileInputSection.style.display = "none";
        randomInputSection.style.display = "block";
        startBtn.style.display = "block";
        isCustomFile = false;
        uploadBtn.style.backgroundColor = "#808080";
        randomBtn.style.backgroundColor = "#cfb46f";
    });

    startBtn.addEventListener("click", function () {
        if (isCustomFile) {
            const inputFile = document.getElementById("inputFile").value;
            const outputFile = document.getElementById("outputFile").value;
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
        } else {
            const numberOfCommands = document.getElementById("numberOfCommands").value || 10;

            fetch(`http://localhost:8080/start-random?numberOfCommands=${numberOfCommands}`, {
                method: "POST"
            })
                .then(response => response.text())
                .then(data => {
                    outputDiv.innerText += "\n" + data + "\n";
                })
                .catch(error => console.error("Error starting simulation: ", error));
        }
    });
});