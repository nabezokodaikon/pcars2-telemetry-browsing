import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../share/jsUtil.js";

const Fragment = React.Fragment;

class PicUp extends React.Component {
  constructor(props) {
    super(props)
  }

  createCarState() {
    const data = this.props.data;
    const telemetryData = data.telemetryData; 
    if (!isJson(telemetryData)) {
      return (
        <tr>
          <td>Car State</td>
        </tr>
      );
    }

    const carState = telemetryData.carState;
    const carFlags = carState.carFlags;

    return (
      <Fragment>
        <tr>
          <td>Car State</td>
        </tr>
        <tr>
          <td>crashState</td>
          <td>{carState.crashState}</td>
        </tr>
        <tr>
          <td>boostAmount</td>
          <td>{carState.boostAmount}</td>
        </tr>
        <tr>
          <td>carFlags</td>
        </tr>
        <tr>
          <td>headLight</td>
          <td>{carFlags.headLight.toString()}</td>
        </tr>
        <tr>
          <td>engineActive</td>
          <td>{carFlags.engineActive.toString()}</td>
        </tr>
        <tr>
          <td>engineWarning</td>
          <td>{carFlags.engineWarning.toString()}</td>
        </tr>
        <tr>
          <td>speedLimiter</td>
          <td>{carFlags.speedLimiter.toString()}</td>
        </tr>
        <tr>
          <td>abs</td>
          <td>{carFlags.abs.toString()}</td>
        </tr>
        <tr>
          <td>handbrake</td>
          <td>{carFlags.handbrake.toString()}</td>
        </tr>
      </Fragment>
    );
  }

  createCarDamage() {
    const data = this.props.data;
    const telemetryData = data.telemetryData; 
    if (!isJson(telemetryData)) {
      return (
        <tr>
          <td>Car Damage</td>
        </tr>
      );
    }

    const carDamage = telemetryData.carDamage;
    
    return (
      <Fragment>
        <tr>
          <td>Car Damage</td>
        </tr>
        <tr>
          <td>aeroDamage</td>
          <td>{carDamage.aeroDamage}</td>
        </tr>
        <tr>
          <td>engineDamage</td>
          <td>{carDamage.engineDamage}</td>
        </tr>
      </Fragment>
    );
  }

  createHWState() {
    const data = this.props.data;
    const telemetryData = data.telemetryData; 
    if (!isJson(telemetryData)) {
      return (
        <tr>
          <td>HW State</td>
        </tr>
      );
    }

    const hwState = telemetryData.hwState;
    
    return (
      <Fragment>
        <tr>
          <td>HW State</td>
        </tr>
        <tr>
          <td>brakeBias</td>
          <td>{hwState.brakeBias}</td>
        </tr>
        <tr>
          <td>turboBoostPressure</td>
          <td>{hwState.turboBoostPressure}</td>
        </tr>
      </Fragment>
    );
  }

  createTyre() {
    const data = this.props.data;
    const telemetryData = data.telemetryData; 
    if (!isJson(telemetryData)) {
      return (
        <tr>
          <td>Tyre</td>
        </tr>
      );
    }

    const tyre1 = telemetryData.tyre1;
    
    return (
      <Fragment>
        <tr>
          <td>Front Left</td>
        </tr>
        <tr>
          <td>tyreFlags</td>
        </tr>
        <tr>
          <td>attached</td>
          <td>{tyre1.tyreFlags[0].attached.toString()}</td>
        </tr>
        <tr>
          <td>inflated</td>
          <td>{tyre1.tyreFlags[0].inflated.toString()}</td>
        </tr>
        <tr>
          <td>isOnGround</td>
          <td>{tyre1.tyreFlags[0].isOnGround.toString()}</td>
        </tr>
        <tr>
          <td>brakeDamage</td>
          <td>{tyre1.brakeDamage[0]}</td>
        </tr>
        <tr>
          <td>brakeDamage</td>
          <td>{tyre1.brakeDamage[0]}</td>
        </tr>
        <tr>
          <td>suspensionDamage</td>
          <td>{tyre1.suspensionDamage[0]}</td>
        </tr>
        <tr>
          <td>Front Right</td>
        </tr>
        <tr>
          <td>tyreFlags</td>
        </tr>
        <tr>
          <td>attached</td>
          <td>{tyre1.tyreFlags[1].attached.toString()}</td>
        </tr>
        <tr>
          <td>inflated</td>
          <td>{tyre1.tyreFlags[1].inflated.toString()}</td>
        </tr>
        <tr>
          <td>isOnGround</td>
          <td>{tyre1.tyreFlags[1].isOnGround.toString()}</td>
        </tr>
        <tr>
          <td>brakeDamage</td>
          <td>{tyre1.brakeDamage[1]}</td>
        </tr>
        <tr>
          <td>suspensionDamage</td>
          <td>{tyre1.suspensionDamage[1]}</td>
        </tr>
        <tr>
          <td>Rear Left</td>
        </tr>
        <tr>
          <td>tyreFlags</td>
        </tr>
        <tr>
          <td>attached</td>
          <td>{tyre1.tyreFlags[2].attached.toString()}</td>
        </tr>
        <tr>
          <td>inflated</td>
          <td>{tyre1.tyreFlags[2].inflated.toString()}</td>
        </tr>
        <tr>
          <td>isOnGround</td>
          <td>{tyre1.tyreFlags[2].isOnGround.toString()}</td>
        </tr>
        <tr>
          <td>brakeDamage</td>
          <td>{tyre1.brakeDamage[2]}</td>
        </tr>
        <tr>
          <td>suspensionDamage</td>
          <td>{tyre1.suspensionDamage[2]}</td>
        </tr>
        <tr>
          <td>Rear Right</td>
        </tr>
        <tr>
          <td>tyreFlags</td>
        </tr>
        <tr>
          <td>attached</td>
          <td>{tyre1.tyreFlags[3].attached.toString()}</td>
        </tr>
        <tr>
          <td>inflated</td>
          <td>{tyre1.tyreFlags[3].inflated.toString()}</td>
        </tr>
        <tr>
          <td>isOnGround</td>
          <td>{tyre1.tyreFlags[3].isOnGround.toString()}</td>
        </tr>
        <tr>
          <td>brakeDamage</td>
          <td>{tyre1.brakeDamage[3]}</td>
        </tr>
        <tr>
          <td>suspensionDamage</td>
          <td>{tyre1.suspensionDamage[3]}</td>
        </tr>
      </Fragment>
    );
  }

  createTyreOther() {
    const data = this.props.data;
    const telemetryData = data.telemetryData; 
    if (!isJson(telemetryData)) {
      return (
        <tr>
          <td>Tyre3</td>
        </tr>
      );
    }

    const tyre3 = telemetryData.tyre3;
    
    return (
      <Fragment>
        <tr>
          <td>Tyre Other</td>
        </tr>
        <tr>
          <td>wings[0]</td>
          <td>{tyre3.wings[0]}</td>
        </tr>
        <tr>
          <td>wings[1]</td>
          <td>{tyre3.wings[1]}</td>
        </tr>
      </Fragment>
    );
  }

  createTimingsData() {
    const data = this.props.data;
    const telemetryData = data.telemetryData; 
    const timingsData = data.timingsData;
    if (!isJson(telemetryData) || !isJson(timingsData)) {
      return (
        <tr>
          <td>Timings Data</td>
        </tr>
      );
    }

    const record = timingsData.formatParticipants[telemetryData.participantInfo.viewedParticipantIndex
];

    return (
      <Fragment>
        <tr>
          <td>Timings Data</td>
        </tr>
        <tr>
          <td>splitTime</td>
          <td>{timingsData.splitTime}</td>
        </tr>
        <tr>
          <td>raceState</td>
          <td>{record.raceState}</td>
          <td>{record.raceStateString}</td>
        </tr>
        <tr>
          <td>pitSchedule</td>
          <td>{record.pitSchedule}</td>
          <td>{record.pitScheduleString}</td>
        </tr>
        <tr>
          <td>pitMode</td>
          <td>{record.pitMode}</td>
          <td>{record.pitModeString}</td>
        </tr>
        <tr>
          <td>highestFlag</td>
          <td>{record.highestFlag}</td>
          <td>{record.highestFlagString}</td>
        </tr>
        <tr>
          <td>lapInvalidated</td>
          <td>{record.lapInvalidated.toString()}</td>
        </tr>
      </Fragment>
    );
  }

  render() {
    return (
      <table>
        <tbody>
          {this.createCarState()}
          {this.createCarDamage()}
          {this.createHWState()}
          {this.createTyre()}
          {this.createTyreOther()}
          {this.createTimingsData()}
        </tbody>
      </table>
    );
  }
}

PicUp.propTypes = {
 data : PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    data: state.currentUdpData
  };
};

const PicUpContainer = connect(
  mapStateToProps
)(PicUp);

export default PicUpContainer;
