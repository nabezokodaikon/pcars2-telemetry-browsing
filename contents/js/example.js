function sendData() {
  try {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();

    formData.append("name", "taro");

    xhr.addEventListener("load", function(e) {
      console.log(e.target.response);
      alert(e.target.response);
    });

    xhr.addEventListener("error", function(e) {
      console.log(e);
      alert(e);
    });

    xhr.open("POST", "api");
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send(formData);
  } catch(e) {
    console.log(e);
    alert(e);
  }
}

console.log("This is a Load event.");
sendData();
