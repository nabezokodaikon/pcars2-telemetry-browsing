import React from "react";
import ReactDom from "react-dom";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { isJson } from "../../common/jsUtil.js";
import { createGearHUDComponent } from "../../common/gearHUDComponent.jsx";
import rankIcon from "../../image/rank.png";
import lapIcon from "../../image/lap.png";
import timeIcon from "../../image/time.png";
import fuelIcon from "../../image/fuel.png";

class SimpleContent extends React.Component {
  constructor(props) {
    super(props)
  }

  getGearStyle() {
    return {
      position: "fixed",
      left: "1rem",
      width: "45%",
      height: "100%"
    };
  }

  getDataStyle() {
    return {
      position: "fixed",
      left: "50%",
      width: "50%",
      height: "100%",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center"
    };
  }

  getDataItemStyle() {
    return {
      width: "95%",
      height: "6rem",
      borderStyle: "solid",
      borderWidth: "0.1rem",
      borderColor: "#899ba9",
      marginBottom: "1rem",
      transform: "skewX(-12deg)",
      display: "flex",
      flexDirection: "row",
      justifyContent: "flex-start",
      alignItems: "center"
    };
  }

  getDataIconContainerStyle() {
    return {
      width: "6rem",
      height: "6rem",
      backgroundColor: "#80adfd",
      display: "flex",
      justifyContent: "center",
      alignItems: "center"
    };
  }

  getDataIconStyle() {
    return {
      width: "80%",
      height: "80%",
      transform: "skewX(12deg)"
    };
  }

  getDataValueContainerStyle() {
    return {
      flexGrow: 1,
      height: "6rem",
      display: "flex",
      justifyContent: "flex-end",
      alignItems: "center"
    };
  }

  getDataValueStyle() {
    return {
      margin: "0.5rem",
      fontSize: "3.25rem",
      fontFamily: "'Inconsolata', monospace",
      transform: "skewX(12deg)"
    };
  }

  getContentStyle() {
    return {
      position: "fixed",
      width: "100%",
      height: "100%"
    };
  }

  createGear() {
    const telemetryData = this.props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div></div>;
    }

    const carState = telemetryData.carState; 

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
      isMeter: this.props.isMeter
    });

    return (
      <svg style={this.getGearStyle()} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100">
        {gearHUDComponent}
      </svg>
    );
  }

  createData() {
    const telemetryData = this.props.telemetryData;
    const timingsData = this.props.timingsData;
    const raceData = this.props.raceData;
    if (!isJson(telemetryData) || !isJson(timingsData)) {
      return <div></div>;
    }

    const carState = telemetryData.carState; 
    const partcipant = timingsData.formatPartcipants[telemetryData.participantinfo.viewedParticipantIndex];

    const eventTimeRemaining = timingsData.eventTimeRemaining;
    const isTimedSessions = (eventTimeRemaining !== "--:--:--.---"); 
    const lapsInEvent = (isJson(raceData) ? raceData.lapsInEvent : 1); 
    const sessionText = (isTimedSessions)
      ? eventTimeRemaining
      : partcipant.currentLap + "/" + lapsInEvent

    return (
      <div style={this.getDataStyle()}>

        <div style={this.getDataItemStyle()}>
          <div style={this.getDataIconContainerStyle()}>
            <img style={this.getDataIconStyle()} src={rankIcon} />
          </div>
          <div style={this.getDataValueContainerStyle()}>
            <span style={this.getDataValueStyle()}>{partcipant.racePosition}/{timingsData.numParticipants}</span>
          </div>
        </div>

        <div style={this.getDataItemStyle()}>
          <div style={this.getDataIconContainerStyle()}>
            <img style={this.getDataIconStyle()} src={lapIcon} />
          </div>
          <div style={this.getDataValueContainerStyle()}>
            <span style={this.getDataValueStyle()}>{sessionText}</span>
          </div>
        </div>
        
        <div style={this.getDataItemStyle()}>
          <div style={this.getDataIconContainerStyle()}>
            <img style={this.getDataIconStyle()} src={timeIcon} />
          </div>
          <div style={this.getDataValueContainerStyle()}>
            <span style={this.getDataValueStyle()}>{partcipant.currentTime}</span>
          </div>
        </div>

        <div style={this.getDataItemStyle()}>
          <div style={this.getDataIconContainerStyle()}>
            <img style={this.getDataIconStyle()} src={fuelIcon} />
          </div>
          <div style={this.getDataValueContainerStyle()}>
            <span style={this.getDataValueStyle()}>{carState.fuelRemaining}L</span>
          </div>
        </div>

      </div>
    );
  }

  render() {
    return (
      <div style={this.getContentStyle()}>
        {this.createGear()}
        {this.createData()}
      </div>
    );
  }
}

SimpleContent.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  isMeter: PropTypes.bool.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData
  return {
    telemetryData: data.telemetryData,
    timingsData: data.timingsData,
    raceData: data.raceData,
    isMeter: state.options.isMeter
  };
};

const SimpleContainer = connect(
  mapStateToProps
)(SimpleContent);

export default SimpleContainer;
