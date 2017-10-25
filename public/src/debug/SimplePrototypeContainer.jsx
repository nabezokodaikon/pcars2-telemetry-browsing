import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../common/jsUtil.js";
import { createGearHUDComponent } from "../common/gearHUDComponent.jsx";

class SimpleProtoType extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    }

    const carStateData = this.props.telemetryData.carStateData; 

    const gearHUDComponent = createGearHUDComponent({
      cx: 50,
      cy: 500,
      gear: carStateData.gear,
      speed: carStateData.speed,
      rpm: carStateData.rpm,
      maxRpm: carStateData.maxRpm,
      throttle: carStateData.throttle,
      brake: carStateData.brake,
      clutch: carStateData.clutch,
      oilTempCelsius: carStateData.oilTempCelsius,
      fuelLevel: carStateData.fuelLevel,
      isCelsius: true,
      isMeter: true
    });

    return (
      <svg viewBox="0 0 1000 1000">
        {gearHUDComponent}
      </svg>
    );
  }

  render() {
    return this.getData();
  }
}

SimpleProtoType.propTypes = {
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData
  };
};

const SimpleProtoTypeContainer = connect(
  mapStateToProps
)(SimpleProtoType);

export default SimpleProtoTypeContainer;
