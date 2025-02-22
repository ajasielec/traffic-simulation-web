document.addEventListener("DOMContentLoaded", function () {
    const outputDiv = document.getElementById("output");
    const uploadBtn = document.getElementById("uploadBtn");
    const randomBtn = document.getElementById("randomBtn");
    const fileInputSection = document.getElementById("fileInputSection");
    const randomInputSection = document.getElementById("randomInputSection");
    const startBtn = document.getElementById("startBtn");
    let isCustomFile = false;
    let isSimulationRunning = false;

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

            if (message.body.includes("Simulation completed") ||
                message.body.includes("Simulation failed") ||
                message.body.includes("Input file not found")) {
                enableStartButton();
            }

            setTimeout(() => {
                outputDiv.scrollTop = outputDiv.scrollHeight;
            }, 10);
        });
    }, function(error) {
        console.error('STOMP error:', error);
        outputDiv.innerText = "Connection error: " + error;
    });

    function scrollToBottom() {
        outputDiv.scrollTop = outputDiv.scrollHeight;
    }

    function disableStartButton() {
        startBtn.disabled = true;
        startBtn.style.opacity = "0.5";
        startBtn.style.cursor = "not-allowed";
        isSimulationRunning = true;
    }

    function enableStartButton() {
        startBtn.disabled = false;
        startBtn.style.opacity = "1";
        startBtn.style.cursor = "pointer";
        isSimulationRunning = false;
    }

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
        if (isSimulationRunning) {
            return;
        }

        disableStartButton();
        scrollToBottom();

        let url;

        if (isCustomFile) {
            const inputFile = document.getElementById("inputFile").value;
            const outputFile = document.getElementById("outputFile").value;
            if (!inputFile || !outputFile) {
                alert("Please enter both input and output file names");
                enableStartButton();
                return;
            }
            url = `http://localhost:8080/start?inputFile=${inputFile}&outputFile=${outputFile}`;
        } else {
            const numberOfCommands = document.getElementById("numberOfCommands").value || 10;
            url = `http://localhost:8080/start-random?numberOfCommands=${numberOfCommands}`;
        }

        fetch(url, { method: "POST" })
            .catch(error => {
                console.error("Error starting simulation:", error);
                outputDiv.innerText += "\nError: " + error.message + "\n";
                scrollToBottom();
                enableStartButton();
            });
    });
});