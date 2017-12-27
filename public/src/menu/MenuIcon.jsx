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
        <input id="toggle" type="checkbox" checked={this.props.isMenuVisible} className={style.toggle} onChange={this.handleChange} />
        <label htmlFor="toggle" className={style.hamburger}>
          <div className={style.topBun} />
          <div className={style.meat} />
          <div className={style.bottomBun} />
        </label>
      </div>
    );
  }
}

MenuIcon.propTypes = {
  style: PropTypes.object.isRequired,
  isMenuVisible: PropTypes.bool.isRequired,
  onClick: PropTypes.func.isRequired
};
