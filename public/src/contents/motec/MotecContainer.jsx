import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { isJson } from "../../common/jsUtil.js";
import { createGearHUDComponent } from "../../common/gearHUDComponent.jsx";
import motecStyle from "./motec.css";
import { getTempUnit, getTemp } from "../../common/telemetryUtil.js";
import fuelIcon from "../../image/fuel.png";
import waterIcon from "../../image/water.png";
import oilIcon from "../../image/oil.png";
import engineIcon from "../../image/engine.png";

class MotecContent extends React.Component {
  constructor(props) {
    super(props)
  }

  createGear() {
    const props = this.props;
    const carState = props.telemetryData.carState; 

    const gearHUDComponent = createGearHUDComponent({
      cx: 50,
      cy: 50,
      radius: 50,
      gear: carState.gear,
      speed: carState.speed,
      rpm: carState.rpm,
      maxRpm: carState.maxRpm,
      throttle: carState.throttle,
      brake: carState.brake,
      clutch: carState.clutch,
      isMeter: props.isMeter
    });

    return (
      <svg className={motecStyle.gear} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100">
        {gearHUDComponent}
      </svg>
    );
  }

  createData() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    const carState = telemetryData.carState; 
    const tyre3 = telemetryData.tyre3;
    const fuelData = props.fuelData;
    const isCelsius = props.isCelsius;

    // const telemetryData = props
    return (
      <div className={motecStyle.table}>
        <div className={motecStyle.fuelRecord}>
          <div className={motecStyle.iconCell}>
            <div>
              <img src={fuelIcon} />
            </div>
          </div>
          <div className={[motecStyle.fuelValueCell, motecStyle.valueCell].join(" ")}>
            <div>
              <span>{carState.fuelRemaining}L</span>
            </div>
          </div>
          <div className={motecStyle.fuelDetailsTable}>
            <div className={motecStyle.fuelDetailsRecord1}>
              <div className={motecStyle.fuelDetailsHeaderCell}>
                <div>
                  <span>LATEST</span>
                </div>
              </div>
              <div className={[motecStyle.fuelDetailsValueCell, motecStyle.valueCell].join(" ")}>
                <div>
                  <span>{fuelData.lastConsumption}L</span>
                </div>
              </div>
            </div>
            <div className={motecStyle.fuelDetailsRecord2}>
              <div className={motecStyle.fuelDetailsHeaderCell}>
                <div>
                  <span>AVERAGE</span>
                </div>
              </div>
              <div className={[motecStyle.fuelDetailsValueCell, motecStyle.valueCell].join(" ")}>
                <div>
                  <span>{fuelData.averageConsumption}L</span>
                </div>
              </div>
            </div>
          </div>
        </div> {/* record */}
        <div className={motecStyle.record}>
          <div className={motecStyle.iconCell}>
            <div>
              <img src={waterIcon} />
            </div>
          </div>
          <div className={[motecStyle.value1Cell, motecStyle.valueCell].join(" ")}>
            <div>
              <span>{getTemp(carState.waterTempCelsius, isCelsius) + getTempUnit(isCelsius)}</span>
            </div>
          </div>
          <div className={[motecStyle.value2Cell, motecStyle.valueCell].join(" ")}>
            <div>
              <span>{carState.waterPressureKpa}kPa</span>
            </div>
          </div>
        </div> {/* record */}
        <div className={motecStyle.record}>
          <div className={motecStyle.iconCell}>
            <div>
              <img src={oilIcon} />
            </div>
          </div>
          <div className={[motecStyle.value1Cell, motecStyle.valueCell].join(" ")}>
            <div>
              <span>{getTemp(carState.oilTempCelsius, isCelsius) + getTempUnit(isCelsius)}</span>
            </div>
          </div>
          <div className={[motecStyle.value2Cell, motecStyle.valueCell].join(" ")}>
            <div>
              <span>{carState.oilPressureKPa}kPa</span>
            </div>
          </div>
        </div> {/* record */}
        <div className={motecStyle.record}>
          <div className={motecStyle.iconCell}>
            <div>
              <img src={engineIcon} />
            </div>
          </div>
          <div className={[motecStyle.value1Cell, motecStyle.valueCell].join(" ")}>
            <div>
              <span>{tyre3.enginePower}HP</span>
            </div>
          </div>
          <div className={[motecStyle.value2Cell, motecStyle.valueCell].join(" ")}>
            <div>
              <span>{tyre3.engineTorque}NM</span>
            </div>
          </div>
        </div> {/* record */}
      </div> // table
    );
  }

  render() {
    const props = this.props;
    if (!isJson(props.telemetryData) || !isJson(props.fuelData)) {
      return (
        <div></div>
      );
    }
    
    return (
      <div className={motecStyle.content}>
        {this.createGear()}
        {this.createData()}
      </div>
    );
  }
}

MotecContent.propTypes = {
  isCelsius: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  fuelData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData
  return {
    isCelsius: state.options.isCelsius,
    telemetryData: data.telemetryData,
    fuelData: data.fuelData
  };
};

const MotecContainer = connect(
  mapStateToProps
)(MotecContent);

export default MotecContainer;
