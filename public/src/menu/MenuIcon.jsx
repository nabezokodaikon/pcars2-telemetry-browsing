import React from "react";
import PropTypes from "prop-types";
import style from "./menuIcon.css";

export default class MenuIcon extends React.Component {
  constructor(props) {
    super(props);

    this.handleChange = this.handleChange.bind(this);
  }

  handleChange() {
    this.props.onClick();
  }

  render() {
    return (
      <div className={style.menuIcon} style={this.props.style}>
        <input type="checkbox" checked={this.props.isMenuVisible} onChange={this.handleChange} />
        <span className={style.top} />
        <span className={style.middle} />
        <span className={style.bottom} />
      </div>
    );
  }
}

MenuIcon.propTypes = {
  style: PropTypes.object.isRequired,
  isMenuVisible: PropTypes.bool.isRequired,
  onClick: PropTypes.func.isRequired
};
