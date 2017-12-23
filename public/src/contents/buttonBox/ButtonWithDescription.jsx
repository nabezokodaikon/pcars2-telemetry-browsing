import React from "react";
import PropTypes from "prop-types";
import { sleep } from "../../share/jsUtil.js";
import style from "./button.css";

export default class ButtonWithDescription extends React.Component {
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
      <div className={style.buttonWithDescription}>
        <label className={style.labelLeft}>
          <input
            type="checkbox"
            className={style.input}
            ref={input => { this.input = input; }}
            onClick={this.handleClick}
          />
        <span className={[style.span, this.props.color].join(" ")} />
        </label>
        <span className={style.description}>{this.props.description}</span>
      </div>
    );
  }
}

ButtonWithDescription.propTypes = {
  index: PropTypes.number.isRequired,
  onClick: PropTypes.func.isRequired,
  color: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired
};
