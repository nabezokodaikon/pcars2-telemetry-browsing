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
      <div className={style.menu} style={this.props.style}>
        <input id="menuOpen" type="checkbox" className={style.menuOpen} checked={this.props.isMenuVisible} onChange={this.handleChange} />
        <label  htmlFor="menuOpen" className={style.menuOpenButton}>
          <span className={[style.hamburger, style.hamburger1].join(" ")} />
          <span className={[style.hamburger, style.hamburger2].join(" ")} />
          <span className={[style.hamburger, style.hamburger3].join(" ")} />
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
