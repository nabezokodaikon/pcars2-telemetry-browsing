import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isJson } from "../../share/jsUtil.js";
import { getTempUnit, getTemp } from "../../share/telemetryUtil.js";
import LargeGearComponent from "../../share/LargeGearComponent.jsx";
import LargeFuelComponent from "../../share/LargeFuelComponent.jsx";
import shareStyle from "../../share/largeContent.css";
import engineIcon from "../../image/engine.png";
import oilIcon from "../../image/oil.png";
import waterIcon from "../../image/water.png";
import style from "./engine.css";

class EngineContent extends React.Component {
  constructor(props) {
    super(props);
  }

  createEngine() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div></div>;
    }

    const tyre3 = telemetryData.tyre3;

    return (
      <div className={style.engine}>
        <div className={style.icon}>
          <img src={engineIcon} />
        </div>
        <div className={style.values}>
          <div className={style.valueBox}>
            <div className={shareStyle.value}>
              <span>{tyre3.enginePower}</span>
            </div>
            <div className={[style.unit, shareStyle.value].join(" ")}>
              <span>HP</span>
            </div>
          </div>
          <div className={style.valueBox}>
            <div className={shareStyle.value}>
              <span>{tyre3.engineTorque}</span>
            </div>
            <div className={[style.unit, shareStyle.value].join(" ")}>
              <span>NM</span>
            </div>
          </div>
          <div className={style.valueBox}>
            <div className={shareStyle.value}>
              <span>{tyre3.engineSpeed}</span>
            </div>
            <div className={[style.unit, shareStyle.value].join(" ")}>
              <span>rad/s</span>
            </div>
          </div>
        </div>
      </div>
    );
  }

  createOil() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div></div>;
    }

    const carState = telemetryData.carState;
    const isCelsius = props.isCelsius;

    return (
      <div className={style.oil}>
        <div className={style.icon}>
          <img src={oilIcon} />
        </div>
        <div className={style.values}>
          <div className={style.valueBox}>
            <div className={shareStyle.value}>
              <span>{getTemp(carState.oilTempCelsius, isCelsius)}</span>
            </div>
            <div className={[style.unit, shareStyle.value].join(" ")}>
              <span>{getTempUnit(isCelsius)}</span>
            </div>
          </div>
          <div className={style.valueBox}>
            <div className={shareStyle.value}>
              <span>{carState.oilPressureKPa}</span>
            </div>
            <div className={[style.unit, shareStyle.value].join(" ")}>
              <span>kPa</span>
            </div>
          </div>
        </div>
      </div>
    );
  }

  createWater() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div></div>;
    }

    const carState = telemetryData.carState;
    const isCelsius = props.isCelsius;

    return (
      <div className={style.water}>
        <div className={style.icon}>
          <img src={waterIcon} />
        </div>
        <div className={style.values}>
          <div className={style.valueBox}>
            <div className={shareStyle.value}>
              <span>{getTemp(carState.waterTempCelsius, isCelsius)}</span>
            </div>
            <div className={[style.unit, shareStyle.value].join(" ")}>
              <span>{getTempUnit(isCelsius)}</span>
            </div>
          </div>
          <div className={style.valueBox}>
            <div className={shareStyle.value}>
              <span>{carState.waterPressureKPa}</span>
            </div>
            <div className={[style.unit, shareStyle.value].join(" ")}>
              <span>kPa</span>
            </div>
          </div>
        </div>
      </div>
    );
  }

  render() {
    const props = this.props;

    return (
      <div className={shareStyle.contents} onClick={props.onContentClick}>
        <div className={shareStyle.topContents}>
          <div className={shareStyle.leftContents}>
            <LargeGearComponent isMeter={props.isMeter} telemetryData={props.telemetryData} />
          </div>
          <div className={shareStyle.rightContents}>
            {this.createEngine()}
            {this.createOil()}
            {this.createWater()}
          </div>
        </div>
        <div className={shareStyle.bottomContents}>
          <LargeFuelComponent telemetryData={props.telemetryData} fuelData={props.fuelData} />
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
  onContentClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData
  return {
    isMeter: state.options.isMeter,
    isCelsius: state.options.isCelsius,
    telemetryData: data.telemetryData,
    fuelData: data.fuelData
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onContentClick: () => {
      dispatch(currentContent(contentNames.TIME))
    }
  };
};

const EngineContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(EngineContent);

export default EngineContainer;
