import React from "react";
import PropTypes from "prop-types";

class Menu extends React.Component {
  constructor(props) {
    super(props);
    this.handleMenuClick = this.handleMenuClick(this);
    this.handleMenuItemClick = this.handleMenuItemClick(this);
    this.handleFilterClick = this.handleFilterClick(this);
  }

  const menuStyle = {
    left: getMenuLest()
  }

  const filterStyle = {
    opacity: getFilterOpacity()
  }

  getMenuLest() {
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

  handleMenuItemClick(menuItemName) {
    this.props.onMenuItemClick(menuItemName);
  }

  handleFilterClick() {
    this.props.onFilterClick();
  }

  renderFilter() {
    return (
      <div className="menu" style={menuStyle} tabIndex="2" onClick={this.handleMenuClick()}>
        <nav>
          <ul>
            <li><button onClick={this.handleMenuItemClick("ParticipantInfoStrings")}>ParticipantInfoStrings</button></li>
            <li><button onClick={this.handleMenuItemClick("ParticipantInfoStringsAdditional")}>ParticipantInfoStringsAdditional</button></li>
            <li><button onClick={this.handleMenuItemClick("GameStateData")}>GameStateData</button></li>
            <li><button onClick={this.handleMenuItemClick("ParticipantInfoData")}>ParticipantInfoData</button></li>
            <li><button onClick={this.handleMenuItemClick("ParticipantInfo")}>ParticipantInfo</button></li>
            <li><button onClick={this.handleMenuItemClick("UnfilteredInputData")}>UnfilteredInputData</button></li>
            <li><button onClick={this.handleMenuItemClick("EventInfoData")}>EventInfoData</button></li>
            <li><button onClick={this.handleMenuItemClick("TimingInfoData")}>TimingInfoData</button></li>
            <li><button onClick={this.handleMenuItemClick("SectorTimeData")}>SectorTimeData</button></li>
            <li><button onClick={this.handleMenuItemClick("FlagData")}>FlagData</button></li>
            <li><button onClick={this.handleMenuItemClick("PitInfoData")}>PitInfoData</button></li>
            <li><button onClick={this.handleMenuItemClick("CarStateData")}>CarStateData</button></li>
            <li><button onClick={this.handleMenuItemClick("CarStateVecotrData")}>CarStateVecotrData</button></li>
            <li><button onClick={this.handleMenuItemClick("TyreData")}>TyreData</button></li>
            <li><button onClick={this.handleMenuItemClick("TyreUdpData")}>TyreUdpData</button></li>
            <li><button onClick={this.handleMenuItemClick("OtherUdpData")}>OtherUdpData</button></li>
            <li><button onClick={this.handleMenuItemClick("CarDamageData")}>CarDamageData</button></li>
          </ul>
        </nav>
      </div>
      <div className="filter" style={filterStyle} tabIndex="1" onClick={this.handleFilterClick()}></div>
    );
  }
}

Menu.propTypes = {
  isMenuToVisible: PropTypes.bool.isRequired,
  onMenuClick: PropTypes.func.isRequired,
  onMenuItemClick: PropTypes.func.isRequired,
  onFilterClick: PropTypes.func.isRequired
}
