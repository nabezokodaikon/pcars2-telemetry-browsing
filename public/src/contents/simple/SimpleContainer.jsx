import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
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
      width: "50%",
      height: "100%",
    };
  }

  getDataStyle() {
    return {
      position: "fixed",
      left: "55%",
      width: "45%",
      height: "100%",
      display: "flex",
      flexDirection: "column",
      justifyContent: "space-around"
    };
  }

  getDataItemStyle() {
    return {
      width: "95%",
      height: "6rem",
      borderStyle: "solid",
      borderWidth: "0.1rem",
      borderColor: "#899ba9",
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
      fontSize: "4.25rem",
      fontFamily: "'Inconsolata', monospace",
      transform: "skewX(12deg)"
    };
  }

  getViewStyle() {
    return {
      position: "fixed",
      width: "100%",
      height: "100%"
    };
  }

  createGear() {
    const carStateData = this.props.telemetryData.carStateData; 

    const gearHUDComponent = createGearHUDComponent({
      cx: 50,
      cy: 50,
      radius: 50,
      gear: carStateData.gear,
      speed: carStateData.speed,
      rpm: carStateData.rpm,
      maxRpm: carStateData.maxRpm,
      throttle: carStateData.throttle,
      brake: carStateData.brake,
      clutch: carStateData.clutch,
      isMeter: this.props.isMeter
    });

    return (
      <svg style={this.getGearStyle()} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100">
        {gearHUDComponent}
      </svg>
    );
  }

  createData() {
    const data = this.props.telemetryData;
    const eventInfoData = data.eventInfoData;
    const participantInfo = data.participantInfo;
    const firstParticipantInfo = participantInfo[0];
    
    return (
      <div style={this.getDataStyle()}>

        <div style={this.getDataItemStyle()}>
          <div style={this.getDataIconContainerStyle()}>
            <img style={this.getDataIconStyle()} src={rankIcon} />
          </div>
          <div style={this.getDataValueContainerStyle()}>
            <span style={this.getDataValueStyle()}>{firstParticipantInfo.racePosition}/{eventInfoData.participantsCount}</span>
          </div>
        </div>

        <div style={this.getDataItemStyle()}>
          <div style={this.getDataIconContainerStyle()}>
            <img style={this.getDataIconStyle()} src={lapIcon} />
          </div>
          <div style={this.getDataValueContainerStyle()}>
            <span style={this.getDataValueStyle()}>{firstParticipantInfo.currentLap}/{eventInfoData.lapsInEvent}</span>
          </div>
        </div>
        
        <div style={this.getDataItemStyle()}>
          <div style={this.getDataIconContainerStyle()}>
            <img style={this.getDataIconStyle()} src={timeIcon} />
          </div>
          <div style={this.getDataValueContainerStyle()}>
            <span style={this.getDataValueStyle()}>{data.timingInfoData.currentTime}</span>
          </div>
        </div>

        <div style={this.getDataItemStyle()}>
          <div style={this.getDataIconContainerStyle()}>
            <img style={this.getDataIconStyle()} src={fuelIcon} />
          </div>
          <div style={this.getDataValueContainerStyle()}>
            <span style={this.getDataValueStyle()}>{data.carStateData.fuelLevel}L</span>
          </div>
        </div>

      </div>
    );
  }

  createView() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    } else {
      return (
        <div style={this.getViewStyle()}>
          {this.createGear()}
          {this.createData()}
        </div>
      );
    }
  }

  render() {
    return this.createView();
  }
}

SimpleContent.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  isMeter: PropTypes.bool.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData,
    isMeter: state.options.isMeter
  };
};

const SimpleContainer = connect(
  mapStateToProps
)(SimpleContent);

export default SimpleContainer;
