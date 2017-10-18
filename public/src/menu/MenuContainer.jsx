import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../common/contentNames.js";
import { currentContent, toggleMenu } from "../appActionCreators.js";

class Menu extends React.Component {
  constructor(props) {
    super(props);
    this.handleMenuClick = this.handleMenuClick.bind(this);
    this.handleMenuItemClick = this.handleMenuItemClick.bind(this);
    this.handleFilterClick = this.handleFilterClick.bind(this);
  }

  getMenuLeft() {
    if (this.props.isMenuToVisible) {
      return "0";
    } else {
      return "-44vw";
    }
  }

  getMenuStyle() {
    return {
      zIndex: 2,
      position: "fixed",
      top: 0,
      left: this.getMenuLeft(),
      overflow: "hidden",
      width: "30vw",
      height: "100vh",
      padding: "1rem",
      transition: "left 0.5s, right 0.5s",
      backgroundColor: "#868686",
      cursor: "pointer"
    };
  }

  getUlStyle() {
    return {
      margin: 0,
      paddingLeft: 0
    };
  }

  getLiStyle() {
    return {
      listStyle: "none"
    };
  }

  getButtonStyle() {
    return {
      color: "#555555",
      fontSize: "1rem",
      cursor: "pointer"
    };
  }

  getFilterOpacity() {
    if (this.props.isMenuToVisible) {
      return "0.5";
    } else {
      return "0";
    }
  }

  getFilterStyle() {
    return {
      zIndex: 1,
      position: "absolute",
      width: "100vw",
      height: "100vh",
      backgroundColor: "#555555",
      opacity: this.getFilterOpacity(),
      cursor: "pointer"
    };
  };

  getContentItems() {
    return [
      contentNames.PARTICIPANT_INFO_STRINGS,
      contentNames.PARTICIPANT_INFO_STRINGS_ADDITIONAL,
      contentNames.GAMESTATE_DATA,
      contentNames.PARTICIPANT_INFO_DATA,
      contentNames.PARTICIPANT_INFO,
      contentNames.UNFILTEREDINPUT_DATA,
      contentNames.EVENT_INFO_DATA,
      contentNames.TIMING_INFO_DATA,
      contentNames.SECTOR_TIME_DATA,
      contentNames.FLAG_DATA,
      contentNames.PITINFO_DATA,
      contentNames.CAR_STATE_DATA,
      contentNames.CAR_STATE_VECOTRDATA,
      contentNames.TYRE_DATA,
      contentNames.OTHER_UDP_DATA,
      contentNames.CAR_DAMAGE_DATA
    ].map((v, i) =>
      <li style={this.getLiStyle()} key={i.toString()}>
        <button style={this.getButtonStyle()} onClick={evt => this.handleMenuItemClick(evt, v)}>{v}</button>
      </li>
    );
  }

  handleMenuClick() {
    this.props.onMenuClick();
  }

  handleMenuItemClick(evt, selectedContent) {
    this.props.onMenuItemClick(selectedContent);
    evt.stopPropagation();
  }

  handleFilterClick() {
    this.props.onFilterClick();
  }

  renderMenu() {
    return (
      <div style={this.getMenuStyle()} tabIndex="1" onClick={this.handleMenuClick}>
        <nav>
          <ul style={this.getUlStyle()}>
            {this.getContentItems()}
          </ul>
        </nav>
      </div>
    );
  }

  renderFilter() {
    return (
      <div className="filter" style={this.getFilterStyle()} tabIndex="2" onClick={this.handleFilterClick}></div>
    );
  }

  render() {
    return (
      <div>
        {this.renderMenu()}
        {this.renderFilter()}
      </div>
    );
  }
}

Menu.propTypes = {
  isMenuToVisible: PropTypes.bool.isRequired,
  onMenuClick: PropTypes.func.isRequired,
  onMenuItemClick: PropTypes.func.isRequired,
  onFilterClick: PropTypes.func.isRequired
}

const mapStateToProps = state => {
  return {
    isMenuToVisible: state.isMenuToVisible
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onMenuClick: () => {
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
