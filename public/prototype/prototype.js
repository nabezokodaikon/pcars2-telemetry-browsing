window.addEventListener("load", (e) => {
  const menu = document.getElementById("menu");

  const input = document.getElementById("input");
  input.addEventListener("click", (e) => {
    console.log("link.");
    menu.style.left = "-340px";
    filter.style.opacity = "0";
  });

  const filter = document.getElementById("filter");
  filter.addEventListener("click", (e) => {
    console.log("filter.");
    if (menu.style.left == "0px") {
      menu.style.left = "-340px";
    } else {
      menu.style.left = "0px";
    }

    if (filter.style.opacity == "0.5") {
      filter.style.opacity = "0";
    } else {
      filter.style.opacity = "0.5";
    }
  });

  console.log("loaded.");
}, false);
