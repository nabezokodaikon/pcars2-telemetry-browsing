import React from "react";
import PropTypes from "prop-types";
import { isJson } from "./jsUtil.js";
import {
  getSpeed,
  getSpeedUnit
} from "./telemetryUtil.js";
import style from "./smallContent.css";

export default class SmallSpeedComponent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div></div>;
    }

    const carState = telemetryData.carState; 
    const isMeter = props.isMeter;
    const speed = getSpeed(carState.speed, isMeter);      
    const unit = getSpeedUnit(isMeter);

    return (
      <div className={style.speedBox}>
        <div className={style.speedValue}>
          <span>{speed}</span>
        </div>
        <div className={style.speedUnit}>
          <span>{unit}</span>
        </div>
      </div>
    );
  }
}

SmallSpeedComponent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired
};
