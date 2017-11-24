import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isArray, isJson } from "../../share/jsUtil.js";
import { createMiniHUDComponent } from "../../share/miniHUDComponent.jsx"
import HeaderRecordComponent from "./HeaderRecordComponent.jsx";
import BodyRecordComponent from "./BodyRecordComponent.jsx";

const emptyRecord = {
  lap: "---",
  sector1: "--:--.---",
  sector2: "--:--.---",
  sector3: "--:--.---",
  lapTime: "--:--.---",
  delta: "--:--.---"
}


class TimeContent extends React.Component {
  constructor(props) {
    super(props)
  }

  getCurrentRecord() {
    const telemetryData = this.props.telemetryData;
    if (!isJson(telemetryData)) {
      return emptyRecord;
    }

    const participantInfo = this.props.telemetryData.participantInfo;
    if (!isJson(participantInfo)) {
      return emptyRecord;
    }

    const timingsData = this.props.timingsData;
    if (!isJson(timingsData)) {
      return emptyRecord;
    }

    const participants = timingsData.formatParticipants;
    if (!isArray(participants)) {
      return emptyRecord;
    }

    const participant = participants[participantInfo.viewedParticipantIndex];
    if (!isJson(participant)) {
      return emptyRecord;
    }

    const getCurrentWidthoutLapTime = () => {
      switch (participant.sector) {
        case 1:
          return {
            lap: participant.currentLap,
            sector1: participant.currentSectorTime,
            sector2: "--:--.---",
            sector3: "--:--.---",
            lapTime: participant.currentTime,
            delta: "--:--.---"
          }
        case 2:
          return {
            lap: participant.currentLap,
            sector1: "--:--.---",
            sector2: participant.currentSectorTime,
            sector3: "--:--.---",
            lapTime: participant.currentTime,
            delta: "--:--.---"
          }
        case 3:
          return {
            lap: participant.currentLap,
            sector1: "--:--.---",
            sector2: "--:--.---",
            sector3: participant.currentSectorTime,
            lapTime: participant.currentTime,
            delta: "--:--.---"
          }
        default:
          return {
            lap: participant.currentLap,
            sector1: "--:--.---",
            sector2: "--:--.---",
            sector3: "--:--.---",
            lapTime: participant.currentTime,
            delta: "--:--.---"
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
              lapTime: participant.currentTime,
              delta: "--:--.---"
            }
          case 2:
            return {
              lap: participant.currentLap,
              sector1: current.sector1,
              sector2: participant.currentSectorTime,
              sector3: "--:--.---",
              lapTime: participant.currentTime,
              delta: "--:--.---"
            }
          case 3:
            return {
              lap: participant.currentLap,
              sector1: current.sector1,
              sector2: current.sector2,
              sector3: participant.currentSectorTime,
              lapTime: participant.currentTime,
              delta: "--:--.---"
            }
          default:
            return {
              lap: participant.currentLap,
              sector1: "--:--.---",
              sector2: "--:--.---",
              sector3: "--:--.---",
              lapTime: participant.currentTime,
              delta: "--:--.---"
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
      return emptyRecord;
    }
  }

  createHeader() {
    const telemetryData = this.props.telemetryData;
    if (!isJson(telemetryData)) {
      return (
        <div></div>
      );
    }

    const style = {
      width: "100%",
      height: "6rem"
    };

    const carState = telemetryData.carState; 
    const miniHUDComponent = createMiniHUDComponent({
      gear: carState.gear,
      speed: carState.speed,
      rpm: carState.rpm,
      maxRpm: carState.maxRpm,
      fuelRemaining: carState.fuelRemaining,
      isMeter: this.props.isMeter
    });

    return (
      <svg style={style} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100">
        {miniHUDComponent}
      </svg>
    );
  }

  createTimeTable() {
    const lapTimeDetails = this.props.lapTimeDetails;
    if (!isJson(lapTimeDetails)) {
      return (
        <div></div>
      );
    }

    const style = {
      display: "flex",
      flexDirection: "column",
      justifyContent: "center"
    };

    const history = lapTimeDetails.history;
    const historyLength = history.length;
    return (
      <div style={style}>
        <HeaderRecordComponent />
        <BodyRecordComponent name={"CURRENT"} record={this.getCurrentRecord()} />
        <BodyRecordComponent name={"BEST"} record={this.getRecord(lapTimeDetails.fastest)} />
        <BodyRecordComponent name={"AVERAGE"} record={this.getRecord(lapTimeDetails.average)} />
        <BodyRecordComponent name={"LATEST"} record={this.getRecord(history[historyLength - 1])} />
        <BodyRecordComponent name={""} record={this.getRecord(history[historyLength - 2])} />
        <BodyRecordComponent name={""} record={this.getRecord(history[historyLength - 3])} />
        <BodyRecordComponent name={""} record={this.getRecord(history[historyLength - 4])} />
      </div>
    );
  }

  render() {
    const style = {
      position: "fixed",
      width: "100%",
      height: "100%",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      alignItems: "center"
    };

    return (
      <div style={style} onClick={this.props.onContentClick}>
        {this.createHeader()}
        {this.createTimeTable()}
      </div>
    );
  }
}

TimeContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  timingsData: PropTypes.object.isRequired,
  lapTimeDetails: PropTypes.object.isRequired,
  onContentClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData,
    timingsData: data.timingsData,
    lapTimeDetails: data.lapTimeDetails
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onContentClick: () => {
      dispatch(currentContent(contentNames.DEFAULT))
    }
  };
};

const TimeContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(TimeContent);

export default TimeContainer;
