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

console.log("This is a Load event.");
sendData();
