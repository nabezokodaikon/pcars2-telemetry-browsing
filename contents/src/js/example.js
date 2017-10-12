function sendData() {
  try {
    const xhr = new XMLHttpRequest();
    
    xhr.addEventListener("load", function(e) {
      console.log(e.target.response);
      // alert(e.target.response);
    });

    xhr.addEventListener("error", function(e) {
      console.log(e);
      alert(e);
    });

    xhr.open("POST", "api");
    xhr.send();
  } catch(e) {
    console.log(e);
    alert(e);
  }
}

function serverEvent() {
  try {
    const evtSource = new EventSource("tick");
    evtSource.onmessage = function(e) {
      console.log(e.data);
      var newElement = document.createElement("p");
      newElement.innerHTML = "message: " + e.data;
      document.getElementById("root").appendChild(newElement);
    };

  } catch(e) {
    console.log(e);
    alert(e);
  }
}

function connect() {
  const ws = new WebSocket("ws://192.168.1.18:9000/pcars1");
  ws.onopen = function(e) {
    console.log("Websocket was opened.");
  };

  ws.onmessage = function(e) {
    const json = JSON.parse(e.data)
    console.log(json.frameType)
  }

}

console.log("Loaded.");
connect();
