import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../common/js/contentNames.js";
import { currentContent, toggleMenu } from "../appActionCreators.js";

class Menu extends React.Component {
  constructor(props) {
    super(props);
    this.handleMenuClick = this.handleMenuClick.bind(this);
    this.handleMenuItemClick = this.handleMenuItemClick.bind(this);
    this.handleFilterClick = this.handleFilterClick.bind(this);
  }

  getMenuStyle() {
    return {
      left: this.getMenuLeft()
    };
  }

  getFilterStyle() {
    return {
      opacity: this.getFilterOpacity()
    };
  };

  getMenuLeft() {
    if (this.props.isMenuToVisible) {
      return "0";
    } else {
      return "-340px";
    }
  }

  getFilterOpacity() {
    if (this.props.isMenuToVisible) {
      return "0.5";
    } else {
      return "0";
    }
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

  // TODO: ループが可能なら、このクラス内にスタイルシートを定義する。
  renderMenu() {
    return (
      <div className="menu" style={this.getMenuStyle()} tabIndex="2" onClick={this.handleMenuClick}>
        <nav>
          <ul>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "ParticipantInfoStrings")}>ParticipantInfoStrings</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "ParticipantInfoStringsAdditional")}>ParticipantInfoStringsAdditional</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "GameStateData")}>GameStateData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "ParticipantInfoData")}>ParticipantInfoData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "ParticipantInfo")}>ParticipantInfo</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "UnfilteredInputData")}>UnfilteredInputData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "EventInfoData")}>EventInfoData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "TimingInfoData")}>TimingInfoData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "SectorTimeData")}>SectorTimeData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "FlagData")}>FlagData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "PitInfoData")}>PitInfoData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "CarStateData")}>CarStateData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "CarStateVecotrData")}>CarStateVecotrData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "TyreData")}>TyreData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "TyreUdpData")}>TyreUdpData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "OtherUdpData")}>OtherUdpData</button></li>
            <li><button onClick={evt => this.handleMenuItemClick(evt, "CarDamageData")}>CarDamageData</button></li>
          </ul>
        </nav>
      </div>
    );
  }

  renderFilter() {
    return (
      <div className="filter" style={this.getFilterStyle()} tabIndex="1" onClick={this.handleFilterClick}></div>
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
