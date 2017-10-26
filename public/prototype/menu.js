console.log("Start.");

window.addEventListener("load", evt => {
  const open = document.getElementById("open");
  const menu = document.getElementById("menu");
  const filter = document.getElementById("filter");
  const list = document.getElementById("list");

  open.addEventListener("change", evt => {
    if (open.checked) {
      filter.style.visibility = "visible";
    } else {
      filter.style.visibility = "hidden";
    }
  });

  Array.from(menu.getElementsByTagName("input")).forEach(elm => {
    elm.addEventListener("click", evt => {
      open.checked = false;
      if (open.checked) {
        filter.style.visibility = "visible";
      } else {
        filter.style.visibility = "hidden";
      }
    });
  });

  filter.addEventListener("click", evt => {
    open.checked = false;
    if (open.checked) {
      filter.style.visibility = "visible";
    } else {
      filter.style.visibility = "hidden";
    }
  });
});
