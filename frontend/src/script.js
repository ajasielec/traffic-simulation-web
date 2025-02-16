document.addEventListener("DOMContentLoaded", function () {
    const outputDiv = document.getElementById("output");
    const startBtn = document.getElementById("startBtn");

    // connect to WebSocket server
    const socket = new WebSocket("ws://localhost:8080/simulation");

    socket.onopen = function () {
        console.log("Connected to WebSocket server.");
        outputDiv.innerText = "Connected. Waiting for simulation data...\n";
    };

    socket.onmessage = function (event) {
        outputDiv.innerText += event.data + "\n";
        outputDiv.scrollTop = outputDiv.scrollHeight;
    };

    socket.onclose = function () {
        console.log("WebSocket connection closed.");
        outputDiv.innerText += "\nConnection closed.";
    };

    // function to start simulation
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
});


