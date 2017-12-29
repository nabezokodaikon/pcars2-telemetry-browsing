import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../share/contentNames.js";
import { currentContent, toggleMenu } from "../appActionCreators.js";
import MenuIcon from "./MenuIcon.jsx";
import menuItem from "./menuItem.css";

class Menu extends React.Component {
  constructor(props) {
    super(props);
    this.handleMenuItemClick = this.handleMenuItemClick.bind(this);
  }

  handleMenuItemClick(evt, selectedContent) {
    this.props.onMenuItemClick(selectedContent);
    evt.stopPropagation();
  }

  getMenuIconStyle() {
    return {
      zIndex: 3,
      marginTop: "0.5rem",
      marginLeft: "0.5rem"
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

  createMenu() {
    return (
      <div style={this.getOuterMenuStyle()}>
        <div style={this.getInnerMenuStyle()}>
          <div style={this.getMenuItemListStyle()}>
            <nav>{this.createMenuItems()}</nav>
          </div>
        </div>
      </div>
    );
  }

  createMenuItems() {
    return [
      contentNames.DEFAULT,
      contentNames.ENGINE,
      contentNames.TYRE,
      contentNames.DAMAGE,
      contentNames.TIME,
      contentNames.RANK,
      contentNames.BUTTON_BOX,
      // contentNames.SIMPLE, // TODO: Plan to delete.
      // contentNames.MOTEC, // TODO: Plan to delete.
      contentNames.OPTIONS,
      contentNames.CREDITS
    ].map((v, i) => {
      if (v == this.props.currentContent) {
        return (
          <div key={i} className={menuItem.activeMenuItem} onClick={evt => this.handleMenuItemClick(evt, v)}>
            <p>{v}</p>
          </div>
        );
      } else {
        return (
          <div key={i} className={menuItem.inactiveMenuItem} onClick={evt => this.handleMenuItemClick(evt, v)}>
            <p>{v}</p>
          </div>
        );
      }
    });
  }

  createFilter() {
    return <div style={this.getFilterStyle()} onClick={this.props.onFilterClick} />;
  }

  render() {
    return (
      <div>
        <MenuIcon
          style={this.getMenuIconStyle()}
          isMenuVisible={this.props.isMenuVisible}
          onClick={this.props.onMenuIconClick}
        />
        {this.createMenu()}
        {this.createFilter()}
      </div>
    );
  }
}

Menu.propTypes = {
  isMenuVisible: PropTypes.bool.isRequired,
  currentContent: PropTypes.string.isRequired,
  onMenuIconClick: PropTypes.func.isRequired,
  onMenuItemClick: PropTypes.func.isRequired,
  onFilterClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  return {
    isMenuVisible: state.isMenuVisible,
    currentContent: state.currentContent
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onMenuIconClick: () => {
      dispatch(toggleMenu());
    },
    onMenuItemClick: selectedContent => {
      dispatch(currentContent(selectedContent));
      dispatch(toggleMenu());
    },
    onFilterClick: () => {
      dispatch(toggleMenu());
    }
  };
};

const MenuContainer = connect(mapStateToProps, mapDispatchToProps)(Menu);

export default MenuContainer;
