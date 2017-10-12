import React from 'react';
import ReactDOM from 'react-dom';
import { writeCommon } from "./common/js/common.js";
import HelloReact from "./react/js/render.jsx";
import css from "./react/css/react.css";

writeCommon("React");

ReactDOM.render(
  <HelloReact />,
  document.getElementById('root')
);
