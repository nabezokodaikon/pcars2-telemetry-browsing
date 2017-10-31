import React from "react";
import ReactDom from "react-dom";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { isJson } from "../../common/jsUtil.js";
import { createLeftTyreComponent } from "./tyreComponent.jsx";

class TyreContent extends React.Component {
  constructor(props) {
    super(props)
  }

  getFrontLeftStyle() {
    return {
      position: "fixed",
      left: 0,
      top: "1rem",
      width: "30%",
      height: "40%"
    };
  }

  getContentStyle() {
    return {
      position: "fixed",
      width: "100%",
      height: "100%"
    };
  }

  createLeftTyre(index, style) {
    const tyreData = this.props.telemetryData.tyreData;
    const tyreUdpData = this.props.telemetryData.tyreUdpData; 

    const tyreComponent = createLeftTyreComponent({
      width: 100,
      height: 100,
      tyreWear: tyreData.tyreWear[index],
      tyreTempCelsius: tyreData.tyreTemp[index],
      brakeTempCelsius: tyreData.brakeTempCelsius[index],
      airPressureBar: tyreUdpData.airPressure[index],
      isCelsius: this.props.isCelsius,
      isBar: this.props.isBar
    }); 

    return (
      <svg style={style} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100">
        {tyreComponent}
      </svg>
    );
  }

  render() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    } else {
      return (
        <div style={this.getContentStyle()}>
          {this.createLeftTyre(0, this.getFrontLeftStyle())}
        </div>
      );
    }
  }
}

TyreContent.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  isCelsius: PropTypes.bool.isRequired,
  isBar: PropTypes.bool.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData,
    isCelsius: state.options.isCelsius,
    isBar: state.options.isBar
  };
};

const TyreContainer = connect(
  mapStateToProps
)(TyreContent);

export default TyreContainer;
