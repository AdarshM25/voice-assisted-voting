let API_BASE = "http://localhost:8080/api";

// ---------------- START SESSION ----------------
function startSession() {
    fetch(`${API_BASE}/start-session`, {
        method: "POST"
    })
    .then(res => res.json())
    .then(data => {
        localStorage.setItem("sessionId", data.session_id);
        localStorage.setItem("ballot", JSON.stringify(data.ballot));
        localStorage.setItem("startTime", new Date().getTime());

        window.location.href = "vote.html";
    })
    .catch(err => alert("Backend not running!"));
}

// ---------------- LOAD BALLOT ----------------
if (window.location.pathname.includes("vote.html")) {

    let ballot = JSON.parse(localStorage.getItem("ballot"));
    let list = document.getElementById("ballotList");

    let selectedOption = null;

    for (let key in ballot) {
        let li = document.createElement("li");
        
        li.innerHTML = `<button class="option-btn" onclick="selectOption(${key})">
    Option ${key}
</button>`;
        list.appendChild(li);
    }

    window.selectOption = function(option) {
        selectedOption = option;
        alert("Selected Option " + option);
    }

    window.submitVote = function() {

        let sessionId = localStorage.getItem("sessionId");

        fetch(`${API_BASE}/submit-vote?sessionId=${sessionId}&option=${selectedOption}`, {
            method: "POST"
        })
        .then(res => res.text())
        .then(data => {

            // 🔥 SESSION EXPIRED HANDLING
            if (data === "Session expired") {
                alert("Session expired!");
                localStorage.clear();
                window.location.href = "index.html";
                return;
            }

            localStorage.setItem("result", data);
            window.location.href = "result.html";
        });
    }

    // 🔥 AUTO TIMEOUT CHECK
    setInterval(() => {
        let startTime = localStorage.getItem("startTime");

        if (!startTime) return;

        let now = new Date().getTime();

        if (now - startTime > 2 * 60 * 1000) {
            alert("Session expired!");
            localStorage.clear();
            window.location.href = "index.html";
        }
    }, 5000);
}

// ---------------- RESULT PAGE ----------------
if (window.location.pathname.includes("result.html")) {

    document.getElementById("resultText").innerText =
        localStorage.getItem("result");

    window.restart = function() {
        localStorage.clear();
        window.location.href = "index.html";
    }
}