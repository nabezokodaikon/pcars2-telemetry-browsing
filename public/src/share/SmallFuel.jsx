import React from "react";
import PropTypes from "prop-types";
import { isJson } from "./jsUtil.js";
import style from "./smallContent.css";
import fuelIcon from "../image/fuel-blue.png";

export default class SmallFuel extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div />;
    }

    const carState = telemetryData.carState;

    return (
      <div className={style.fuelBox}>
        <img className={style.fuelIcon} src={fuelIcon} />
        <div className={style.fuelValue}>
          <span>{carState.fuelRemaining}</span>
        </div>
        <div className={style.fuelUnit}>
          <span>L</span>
        </div>
      </div>
    );
  }
}

SmallFuel.propTypes = {
  telemetryData: PropTypes.object.isRequired
};
