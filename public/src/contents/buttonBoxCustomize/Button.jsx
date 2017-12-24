import React from "react";
import PropTypes from "prop-types";
import style from "./button.css";

export default class Button extends React.Component {
  constructor(props) {
    super(props);

    // this.handleClick = this.handleClick.bind(this);
  }

  // handleClick() {
    // this.input.disabled = true;
    // sleep(100).then(() => {
      // this.input.checked = false;
      // this.input.disabled = false;
    // });

    // const props = this.props;
    // props.onClick(props.index);
  // }

  render() {
    return (
      <div className={style.label}>
        <select className={style.button}>
          <option value="0">A</option>
          <option value="1">B</option>
          <option value="2">C</option>
          <option value="3">D</option>
        </select>
      </div>
    );
  }
}

Button.propTypes = {
  index: PropTypes.number.isRequired
};
