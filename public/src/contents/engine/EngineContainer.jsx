import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isJson } from "../../share/jsUtil.js";
import { getTempUnit, getTemp } from "../../share/telemetryUtil.js";
import LargeGear from "../../share/LargeGear.jsx";
import LargeFuel from "../../share/LargeFuel.jsx";
import Damage from "../../share/Damage.jsx";
import shareStyle from "../../share/largeContent.css";
import engineIcon from "../../image/engine.png";
import oilIcon from "../../image/oil.png";
import waterIcon from "../../image/water.png";
import fuelIcon from "../../image/fuel.png";
import style from "./engine.css";

const Engine = props => {
  const tyre3 = props.tyre3;

  return (
    <div className={style.engine}>
      <div className={style.icon}>
        <img src={engineIcon} />
      </div>

      <div className={style.valueRecords}>
        <div className={style.valueRecord}>
          <div className={style.value}>
            <span>{tyre3.enginePower}</span>
          </div>
          <div className={style.unit}>
            <span>HP</span>
          </div>
        </div>

        <div className={style.valueRecord}>
          <div className={style.value}>
            <span>{tyre3.engineTorque}</span>
          </div>
          <div className={style.unit}>
            <span>NM</span>
          </div>
        </div>

        <div className={style.valueRecord}>
          <div className={style.value}>
            <span>{tyre3.engineSpeed}</span>
          </div>
          <div className={style.unit}>
            <span>rad/s</span>
          </div>
        </div>
      </div>
    </div>
  );
};

const ValueTableHeader = props => {
  return (
    <div className={style.header}>
      <div />
      <div>
        <span>CURRENT</span>
      </div>
      <div>
        <span>MIN</span>
      </div>
      <div>
        <span>MAX</span>
      </div>
    </div>
  );
};

const Oil = props => {
  const carState = props.carState;
  const engine = props.engine;
  const isCelsius = props.isCelsius;

  return (
    <div className={style.oil}>
      <div className={style.icon}>
        <img src={oilIcon} />
      </div>

      <div className={style.valueRecords}>
        <div className={style.valueRecord}>
          <div>
            <div className={style.value}>
              <span>{getTemp(carState.oilTempCelsius, isCelsius)}</span>
            </div>
            <div className={style.unit}>
              <span>{getTempUnit(isCelsius)}</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{getTemp(engine.minOilTempCelsius, isCelsius)}</span>
            </div>
            <div className={style.unit}>
              <span>{getTempUnit(isCelsius)}</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{getTemp(engine.maxOilTempCelsius, isCelsius)}</span>
            </div>
            <div className={style.unit}>
              <span>{getTempUnit(isCelsius)}</span>
            </div>
          </div>
        </div>

        <div className={style.valueRecord}>
          <div>
            <div className={style.value}>
              <span>{carState.oilPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{engine.minOilPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{engine.maxOilPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

const Water = props => {
  const carState = props.carState;
  const engine = props.engine;
  const isCelsius = props.isCelsius;

  return (
    <div className={style.water}>
      <div className={style.icon}>
        <img src={waterIcon} />
      </div>

      <div className={style.valueRecords}>
        <div className={style.valueRecord}>
          <div>
            <div className={style.value}>
              <span>{getTemp(carState.waterTempCelsius, isCelsius)}</span>
            </div>
            <div className={style.unit}>
              <span>{getTempUnit(isCelsius)}</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{getTemp(engine.minWaterTempCelsius, isCelsius)}</span>
            </div>
            <div className={style.unit}>
              <span>{getTempUnit(isCelsius)}</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{getTemp(engine.maxWaterTempCelsius, isCelsius)}</span>
            </div>
            <div className={style.unit}>
              <span>{getTempUnit(isCelsius)}</span>
            </div>
          </div>
        </div>

        <div className={style.valueRecord}>
          <div>
            <div className={style.value}>
              <span>{carState.waterPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{engine.minWaterPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{engine.maxWaterPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

const Fuel = props => {
  const carState = props.carState;
  const engine = props.engine;

  return (
    <div className={style.fuel}>
      <div className={style.icon}>
        <img src={fuelIcon} />
      </div>

      <div className={style.valueRecords}>
        <div className={style.valueRecord}>
          <div>
            <div className={style.value}>
              <span>{carState.fuelPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{engine.minFuelPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
          <div>
            <div className={style.value}>
              <span>{engine.maxFuelPressureKPa}</span>
            </div>
            <div className={style.unit}>
              <span>kPa</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

class EngineContent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    const telemetrySummary = props.telemetrySummary;
    if (!isJson(telemetryData) || !isJson(telemetrySummary)) {
      return <div />;
    }

    const carState = telemetryData.carState;
    const carDamage = telemetryData.carDamage;
    const tyre1 = telemetryData.tyre1;
    const brakeDamage = tyre1.brakeDamage;
    const suspensionDamage = tyre1.suspensionDamage;
    const tyre3 = telemetryData.tyre3;
    const engine = telemetrySummary.engine;

    return (
      <div className={shareStyle.contents} onClick={props.onContentClick}>
        <div className={shareStyle.topContents}>
          <div className={shareStyle.leftContents}>
            <LargeGear isMeter={props.isMeter} telemetryData={props.telemetryData} />
          </div>
          <div className={shareStyle.rightContents}>
            <Engine tyre3={tyre3} />
            <div className={style.valueTable}>
              <ValueTableHeader />
              <Oil carState={carState} engine={engine} isCelsius={props.isCelsius} />
              <Water carState={carState} engine={engine} isCelsius={props.isCelsius} />
              <Fuel carState={carState} engine={engine} />
            </div>
          </div>
        </div>
        <div className={shareStyle.bottomContents}>
          <LargeFuel telemetryData={props.telemetryData} fuelData={props.fuelData} />
          <Damage
            aeroDamage={carDamage.aeroDamage}
            engineDamage={carDamage.engineDamage}
            brakeDamage={brakeDamage}
            suspensionDamage={suspensionDamage}
          />
        </div>
      </div>
    );
  }
}

EngineContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  isCelsius: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  fuelData: PropTypes.object.isRequired,
  telemetrySummary: PropTypes.object.isRequired,
  onContentClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    isMeter: state.options.isMeter,
    isCelsius: state.options.isCelsius,
    telemetryData: data.telemetryData,
    fuelData: data.fuelData,
    telemetrySummary: data.telemetrySummary
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onContentClick: () => {
      dispatch(currentContent(contentNames.TIME));
    }
  };
};

const EngineContainer = connect(mapStateToProps, mapDispatchToProps)(EngineContent);

export default EngineContainer;
