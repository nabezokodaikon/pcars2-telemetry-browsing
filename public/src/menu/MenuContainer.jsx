import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../common/contentNames.js";
import { currentContent, toggleMenu } from "../appActionCreators.js";
import menuOpenIcon from "./menuOpenIcon.css";
import menuItem from "./menuItem.css";

class Menu extends React.Component {
  constructor(props) {
    super(props);
    this.handleMenuOpenIconClick = this.handleMenuOpenIconClick.bind(this);
    this.handleMenuItemClick = this.handleMenuItemClick.bind(this);
    this.handleFilterClick = this.handleFilterClick.bind(this);
  }

  handleMenuOpenIconClick() {
    this.props.onMenuOpenIconClick();
  }

  handleMenuItemClick(evt, selectedContent) {
    this.props.onMenuItemClick(selectedContent);
    evt.stopPropagation();
  }

  handleFilterClick() {
    this.props.onFilterClick();
  }

  getMenuOpenIconStyle() {
    return {
      zIndex: 3,
      position: "absolute",
      margin: "1rem"
    };
  }

  getOuterMenuLeft() {
    if (this.props.isMenuVisible) {
      return "0";
    } else {
      return "-50%";
    }
  }

  getOuterMenuStyle() {
    return {
      zIndex: 2,
      position: "fixed",
      left: this.getOuterMenuLeft(),
      width: "40%",
      height: "100%",
      fontSize: "75%",
      backgroundColor: "#485867",
      transition: "left .3s, right .3s",
      transform: "skewX(-12deg)"
    };
  }

  getInnerMenuStyle() {
    return {
      position: "fixed",
      left: "-18%",
      width: "100%",
      height: "50%",
      padding: "2.5%",
      backgroundColor: "#485867",
      transform: "skewX(12deg)"
    };
  }

  getMenuItemListStyle() {
    return {
      borderStyle: "solid",
      margin: "3rem 0rem 0rem 4rem"
    };
  }

  getFilterVisibility() {
    if (this.props.isMenuVisible) {
      return "visible";
    } else {
      return "hidden";
    }
  }

  getFilterStyle() {
    return {
      zIndex: 1,
      position: "fixed",
      width: "100%",
      height: "100%",
      backgroundColor: "#000000",
      opacity: 0.5,
      visibility: this.getFilterVisibility()
    };
  }

  getMenuOpenIconChecked() {
    if (this.props.isMenuVisible) {
      return "checked";
    } else {
      return "";
    }
  }

  createMenuOpenIcon() {
    return (
      <input
        type="checkbox"
        checked={this.getMenuOpenIconChecked()}
        className={menuOpenIcon.menuOpen}
        style={this.getMenuOpenIconStyle()}
        onChange={this.handleMenuOpenIconClick}   
      />
    );
  }

  createMenu() {
    return (
      <div style={this.getOuterMenuStyle()}>
        <div style={this.getInnerMenuStyle()}>
          <div style={this.getMenuItemListStyle()}>
            <nav>
              {this.createMenuItems()}
            </nav>
          </div>
        </div>
      </div>
    );
  }

  createMenuItems() {
    return [
      contentNames.TIME,
      contentNames.OPTIONS
    ].map((v, i) => {
      if (v == this.props.currentContent) {
        return (
          <div
            key={i}
            className={menuItem.activeMenuItem}
            onClick={evt => this.handleMenuItemClick(evt, v)}>
            <p>{v}</p>
          </div>
        );
      } else {
        return (
          <div
            key={i}
            className={menuItem.inactiveMenuItem}
            onClick={evt => this.handleMenuItemClick(evt, v)}>
            <p>{v}</p>
          </div>
        );
      }
    });
  }

  createFilter() {
    return (
      <div
        style={this.getFilterStyle()}
        onClick={this.handleFilterClick}
      >
      </div>
    );
  }

  render() {
    return (
      <div>
        {this.createMenuOpenIcon()}
        {this.createMenu()}
        {this.createFilter()}
      </div>
    );
  }
}

Menu.propTypes = {
  isMenuVisible: PropTypes.bool.isRequired,
  currentContent: PropTypes.string.isRequired,
  onMenuOpenIconClick: PropTypes.func.isRequired,
  onMenuItemClick: PropTypes.func.isRequired,
  onFilterClick: PropTypes.func.isRequired
}

const mapStateToProps = state => {
  return {
    isMenuVisible: state.isMenuVisible,
    currentContent: state.currentContent
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onMenuOpenIconClick: () => {
      dispatch(toggleMenu());
    },
    onMenuItemClick: (selectedContent) => {
      dispatch(currentContent(selectedContent));
      dispatch(toggleMenu());
    },
    onFilterClick: () => {
      dispatch(toggleMenu());
    }
  };
};

const MenuContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Menu);

export default MenuContainer;
