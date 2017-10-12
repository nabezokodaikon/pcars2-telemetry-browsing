import React from "react";
import ReactDOM from "react-dom";

export default class HelloReact extends React.Component {
  render() {
    ReactDOM.render(
      <h1>Hello React!</h1>,
      document.getElementById("root")
    );
  }
}
