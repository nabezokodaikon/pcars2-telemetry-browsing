console.log("This is a Load event.")

function sendData() {
  const xhr = new XMLHttpRequest();
  const formData = new FormData();

  formData.append("name", "taro");

  xhr.addEventListener("load", e => {
    console.log(e.target.response);
  });

  xhr.addEventListener("error", e => {
    console.log(e);
  });

  xhr.open("POST", "api");
  xhr.send(formData);
}

sendData();
