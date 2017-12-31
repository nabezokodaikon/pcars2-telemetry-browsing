import React from "react";
import PropTypes from "prop-types";
import { sleep } from "../../share/jsUtil.js";
import style from "./button.css";

export default class Button extends React.Component {
  constructor(props) {
    super(props);

    this.handleClick = this.handleClick.bind(this);
  }

  handleClick() {
    this.input.disabled = true;
    sleep(100).then(() => {
      this.input.checked = false;
      this.input.disabled = false;
    });

    const props = this.props;
    props.onClick(props.index);
  }

  render() {
    return (
      <label className={style.labelLeft}>
        <input
          type="checkbox"
          className={style.input}
          ref={input => {
            this.input = input;
          }}
          onClick={this.handleClick}
        />
        <span className={[style.span, this.props.color].join(" ")} />
      </label>
    );
  }
}

Button.propTypes = {
  index: PropTypes.number.isRequired,
  onClick: PropTypes.func.isRequired,
  color: PropTypes.string.isRequired
};
