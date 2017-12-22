import React from "react";
import PropTypes from "prop-types";
import style from "./button.css";


function sleep(time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}

export default class Button extends React.Component {
  constructor(props) {
    super(props);

    this.handleClick = this.handleClick.bind(this);
  }

  handleClick() {
    console.log("click");
    this.input.disabled = true;
    sleep(100).then(() => {
      this.input.checked = false;
      this.input.disabled = false;
    });
  }

  render() {
    return (
      <label>
        <input
          type="checkbox"
          className={style.input}
          ref={input => { this.input = input; }}
          onClick={this.handleClick} />
        <span className={style.span}></span>
      </label>
    );
  }
}
