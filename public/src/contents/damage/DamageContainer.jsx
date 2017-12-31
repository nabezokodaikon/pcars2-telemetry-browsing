import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isJson } from "../../share/jsUtil.js";
import { TYRE_FRONT_LEFT, TYRE_FRONT_RIGHT, TYRE_REAR_LEFT, TYRE_REAR_RIGHT } from "../../share/telemetryConst.js";
import LargeGearComponent from "../../share/LargeGearComponent.jsx";
import LargeFuelComponent from "../../share/LargeFuelComponent.jsx";
import shareStyle from "../../share/largeContent.css";
import style from "./damage.css";
import SingleDamageComponent from "./SingleDamageComponent.jsx";
import TyreDamageComponent from "./TyreDamageComponent.jsx";

class DamageContent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div />;
    }

    const carDamage = telemetryData.carDamage;
    const tyre1 = telemetryData.tyre1;
    const brakeDamage = tyre1.brakeDamage;
    const suspensionDamage = tyre1.suspensionDamage;

    return (
      <div className={shareStyle.contents} onClick={props.onContentClick}>
        <div className={shareStyle.topContents}>
          <div className={shareStyle.leftContents}>
            <LargeGearComponent isMeter={props.isMeter} telemetryData={telemetryData} />
          </div>
          <div className={shareStyle.rightContents}>
            <div className={style.carDamage}>
              <SingleDamageComponent className={style.engineDamage} header="ENGINE" value={carDamage.engineDamage} />
              <SingleDamageComponent className={style.aeroDamage} header="AERO" value={carDamage.aeroDamage} />
            </div>
            <div className={style.tyreDamage}>
              <div className={style.frontTyre}>
                <TyreDamageComponent
                  className={style.leftTyre}
                  header="FRONT LEFT"
                  brake={brakeDamage[TYRE_FRONT_LEFT]}
                  suspension={suspensionDamage[TYRE_FRONT_LEFT]}
                />
                <TyreDamageComponent
                  className={style.rightTyre}
                  header="FRONT RIGHT"
                  brake={brakeDamage[TYRE_FRONT_RIGHT]}
                  suspension={suspensionDamage[TYRE_FRONT_RIGHT]}
                />
              </div>
              <div className={style.rearTyre}>
                <TyreDamageComponent
                  className={style.leftTyre}
                  header="REAR LEFT"
                  brake={brakeDamage[TYRE_REAR_LEFT]}
                  suspension={suspensionDamage[TYRE_REAR_LEFT]}
                />
                <TyreDamageComponent
                  className={style.rightTyre}
                  header="REAR RIGHT"
                  brake={brakeDamage[TYRE_REAR_RIGHT]}
                  suspension={suspensionDamage[TYRE_REAR_RIGHT]}
                />
              </div>
            </div>
          </div>
        </div>
        <div className={shareStyle.bottomContents}>
          <LargeFuelComponent telemetryData={telemetryData} fuelData={props.fuelData} />
        </div>
      </div>
    );
  }
}

DamageContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  fuelData: PropTypes.object.isRequired,
  onContentClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData,
    fuelData: data.fuelData
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onContentClick: () => {
      dispatch(currentContent(contentNames.TIME));
    }
  };
};

const DamageContainer = connect(mapStateToProps, mapDispatchToProps)(DamageContent);

export default DamageContainer;
