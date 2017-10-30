import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../../common/jsUtil.js";
import { createGearHUDComponent } from "../../common/gearHUDComponent.jsx";

class SimpleContent extends React.Component {
  constructor(props) {
    super(props)
  }

  getGearStyle() {
    return {
      position: "fixed",
      width: "50%",
      height: "100%",
    };
  }

  getDataStyle() {
    return {
      position: "fixed",
      left: "50%",
      width: "50%",
      height: "100%"
    };
  }

  getViewStyle() {
    return {
      position: "fixed",
      width: "100%",
      height: "100%"
    };
  }

  createGear() {
    const carStateData = this.props.telemetryData.carStateData; 

    const gearHUDComponent = createGearHUDComponent({
      cx: 50,
      cy: 50,
      radius: 50,
      gear: carStateData.gear,
      speed: carStateData.speed,
      rpm: carStateData.rpm,
      maxRpm: carStateData.maxRpm,
      throttle: carStateData.throttle,
      brake: carStateData.brake,
      clutch: carStateData.clutch,
      isMeter: this.props.isMeter
    });

    return (
      <svg style={this.getGearStyle()} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100">
        {gearHUDComponent}
      </svg>
    );
  }

  createData() {
    return (
      <div style={this.getDataStyle()}>
        <p>hoge</p>
      </div>
    );
  }

  createView() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    } else {
      return (
        <div style={this.getViewStyle()}>
          {this.createData()}
          {this.createGear()}
        </div>
      );
    }
  }

  render() {
    return this.createView();
  }
}

SimpleContent.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  isMeter: PropTypes.bool.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData,
    isMeter: state.options.isMeter
  };
};

const SimpleContainer = connect(
  mapStateToProps
)(SimpleContent);

export default SimpleContainer;
