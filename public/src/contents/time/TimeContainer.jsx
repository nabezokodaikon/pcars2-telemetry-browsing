import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { isArray, isJson } from "../../common/jsUtil.js";
import HeaderRecordComponent from "./HeaderRecordComponent.jsx";
import BodyRecordComponent from "./BodyRecordComponent.jsx";

class TimeContent extends React.Component {
  constructor(props) {
    super(props)
  }

  getContentStyle() {
    return {
      position: "fixed",
      width: "100%",
      height: "100%",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      alignItems: "center"
    };
  }

  getHeaderStyle() {
    return {
      display: "flex",
      flexDirection: "row",
      justifyContent: "spaceAround"
    };
  }

  getTimeTableStyle() {
    return {
      display: "flex",
      flexDirection: "column",
      justifyContent: "center"
    };
  }

  getEmptyRecord() {
    return {
      lap: "---",
      sector1: "--:--.---",
      sector2: "--:--.---",
      sector3: "--:--.---",
      lapTime: "--:--.---"
    }
  }

  getCurrentRecord() {
    const telemetryData = this.props.telemetryData;
    if (!isJson(telemetryData)) {
      return this.getEmptyRecord();
    }

    const participantInfo = this.props.telemetryData.participantInfo;
    if (!isJson(participantInfo)) {
      return this.getEmptyRecord();
    }

    const timingsData = this.props.timingsData;
    if (!isJson(timingsData)) {
      return this.getEmptyRecord();
    }

    const participants = timingsData.formatParticipants;
    if (!isArray(participants)) {
      return this.getEmptyRecord();
    }

    const participant = participants[participantInfo.viewedParticipantIndex];
    if (!isJson(participant)) {
      return this.getEmptyRecord();
    }

    const getCurrentWidthoutLapTime = () => {
      switch (participant.sector) {
        case 1:
          return {
            lap: participant.currentLap,
            sector1: participant.currentSectorTime,
            sector2: "--:--.---",
            sector3: "--:--.---",
            lapTime: participant.currentTime
          }
        case 2:
          return {
            lap: participant.currentLap,
            sector1: "--:--.---",
            sector2: participant.currentSectorTime,
            sector3: "--:--.---",
            lapTime: participant.currentTime
          }
        case 3:
          return {
            lap: participant.currentLap,
            sector1: "--:--.---",
            sector2: "--:--.---",
            sector3: participant.currentSectorTime,
            lapTime: participant.currentTime
          }
        default:
          return {
            lap: participant.currentLap,
            sector1: "--:--.---",
            sector2: "--:--.---",
            sector3: "--:--.---",
            lapTime: participant.currentTime
          }
      }
    };

    const lapTimeDetails = this.props.lapTimeDetails;
    if (isJson(lapTimeDetails)) {
      const current = lapTimeDetails.current; 
      if (isJson(current)) {
        switch (participant.sector) {
          case 1:
            return {
              lap: participant.currentLap,
              sector1: participant.currentSectorTime,
              sector2: "--:--.---",
              sector3: "--:--.---",
              lapTime: participant.currentTime
            }
          case 2:
            return {
              lap: participant.currentLap,
              sector1: current.sector1,
              sector2: participant.currentSectorTime,
              sector3: "--:--.---",
              lapTime: participant.currentTime
            }
          case 3:
            return {
              lap: participant.currentLap,
              sector1: current.sector1,
              sector2: current.sector2,
              sector3: participant.currentSectorTime,
              lapTime: participant.currentTime
            }
          default:
            return {
              lap: participant.currentLap,
              sector1: "--:--.---",
              sector2: "--:--.---",
              sector3: "--:--.---",
              lapTime: participant.currentTime
            }
        }
      } else {
        return getCurrentWidthoutLapTime();
      }
    } else {
      return getCurrentWidthoutLapTime();
    }
  }

  getRecord(record) {
    if (isJson(record)) {
      return record;
    } else {
      return this.getEmptyRecord();
    }
  }

  createGear() {
    const style = {
      width: "6rem",
      height: "4rem",
      marginRight: "4rem",
      backgroundColor: "#FFFF00"
    };

    return (
      <div style={style}></div>
    );
  }

  createFuel() {
    const style = {
      width: "8rem",
      height: "2rem",
      backgroundColor: "#FF0000"
    };

    return (
      <div style={style}></div>
    );
  }

  createHeader() {
    return (
      <div style={this.getHeaderStyle()}>
        {this.createGear()}
        {this.createFuel()}
      </div>
    );
  }

  createTimeTable() {
    if (isJson(this.props.lapTimeDetails)) {
      const lapTimeDetails = this.props.lapTimeDetails;
      return (
      <div style={this.getTimeTableStyle()}>
        <HeaderRecordComponent />
        <BodyRecordComponent name={"CURRENT"} record={this.getCurrentRecord()} />
        <BodyRecordComponent name={"LOG 1"} record={this.getRecord(lapTimeDetails.history[0])} />
        <BodyRecordComponent name={"LOG 2"} record={this.getRecord(lapTimeDetails.history[1])} />
        <BodyRecordComponent name={"LOG 3"} record={this.getRecord(lapTimeDetails.history[2])} />
        <BodyRecordComponent name={"LOG 4"} record={this.getRecord(lapTimeDetails.history[3])} />
        <BodyRecordComponent name={"BEST"} record={this.getRecord(lapTimeDetails.fastest)} />
        <BodyRecordComponent name={"AVERAGE"} record={this.getRecord(lapTimeDetails.average)} />
      </div>
      );
    } else {
      <div></div>
    }
  }

  render() {
    return (
      <div style={this.getContentStyle()}>
        {this.createHeader()}
        {this.createTimeTable()}
      </div>
    );
  }
}

TimeContent.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  timingsData: PropTypes.object.isRequired,
  lapTimeDetails: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    telemetryData: data.telemetryData,
    timingsData: data.timingsData,
    lapTimeDetails: data.lapTimeDetails
  };
};

const TimeContainer = connect(
  mapStateToProps
)(TimeContent);

export default TimeContainer;
