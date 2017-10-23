import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../common/jsUtil.js";
import { 
  createBrakeComponent,
  createClutchComponent,
  createRpmComponent,
  createThrottleComponent
} from "../common/telemetryComponents.jsx";

class SimpleProtoType extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    }

    const carStateData = this.props.telemetryData.carStateData; 
    const maxRpm = carStateData.maxRpm;
    const rpm = carStateData.rpm;
    const clutch = carStateData.clutch;
    const throttle = carStateData.throttle;
    const brake = carStateData.brake;

    const cx = 500;
    const cy = 500;
    const rpmComponent = createRpmComponent(rpm, maxRpm, cx, cy, 200, 8);
    const clutchComponent = createClutchComponent(clutch, cx, cy, 232, 16);
    const throttleComponent = createThrottleComponent(throttle, cx, cy, 264, 16);
    const brakeComponent = createBrakeComponent(brake, cx, cy, 264, 16);

    return (
      <svg viewBox="0 0 1000 1000">
        {rpmComponent}
        {clutchComponent}
        {throttleComponent}
        {brakeComponent}
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
