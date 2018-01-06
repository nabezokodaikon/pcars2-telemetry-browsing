import React from "react";
import PropTypes from "prop-types";
import { isJson } from "./jsUtil.js";
import style from "./largeContent.css";
import fuelIcon from "../image/fuel-blue.png";

export default class LargeFuel extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    const fuelData = props.fuelData;
    if (!isJson(telemetryData) || !isJson(fuelData)) {
      return <div />;
    }

    const carState = telemetryData.carState;

    return (
      <div className={style.fuel}>
        <div className={style.fuelIcon}>
          <img src={fuelIcon} />
        </div>
        <div className={[style.fuelValue, style.value].join(" ")}>
          <span>{carState.fuelRemaining}L</span>
        </div>
        <div className={style.fuelDetails}>
          <div>
            <div className={[style.fuelDetailsHeader, style.header].join(" ")}>
              <span>LATEST</span>
            </div>
            <div className={[style.fuelDetailsValue, style.value].join(" ")}>
              <span>{fuelData.lastConsumption}L</span>
            </div>
          </div>
          <div>
            <div className={[style.fuelDetailsHeader, style.header].join(" ")}>
              <span>AVERAGE</span>
            </div>
            <div className={[style.fuelDetailsValue, style.value].join(" ")}>
              <span>{fuelData.averageConsumption}L</span>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

LargeFuel.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  fuelData: PropTypes.object.isRequired
};
