import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isArray, isJson } from "../../share/jsUtil.js";
import { createMiniHUDComponent } from "../../share/miniHUDComponent.jsx"
import HeaderComponent from "./HeaderComponent.jsx";
import RecordComponent from "./RecordComponent.jsx";
import shareStyle from "../../share/smallContent.css";
import style from "./time.css";

const emptyTime = "--:--.---";

const emptyRecord = {
  lap: "---",
  sector1: emptyTime,
  sector2: emptyTime,
  sector3: emptyTime,
  lapTime: emptyTime,
  delta: emptyTime
}

const indexArray = [...Array(60).keys()];

class TimeContent extends React.Component {
  constructor(props) {
    super(props)
  }

  // TODO
  createHeader() {
    const telemetryData = this.props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div></div>;
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

  getCurrentRecord() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return emptyRecord;
    }

    const participantInfo = telemetryData.participantInfo;
    if (!isJson(participantInfo)) {
      return emptyRecord;
    }

    const timingsData = props.timingsData;
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
            sector2: emptyTime,
            sector3: emptyTime,
            lapTime: participant.currentTime,
            delta: emptyTime
          }
        case 2:
          return {
            lap: participant.currentLap,
            sector1: emptyTime,
            sector2: participant.currentSectorTime,
            sector3: emptyTime,
            lapTime: participant.currentTime,
            delta: emptyTime
          }
        case 3:
          return {
            lap: participant.currentLap,
            sector1: emptyTime,
            sector2: emptyTime,
            sector3: participant.currentSectorTime,
            lapTime: participant.currentTime,
            delta: emptyTime
          }
        default:
          return {
            lap: participant.currentLap,
            sector1: emptyTime,
            sector2: emptyTime,
            sector3: emptyTime,
            lapTime: participant.currentTime,
            delta: emptyTime
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
              sector2: emptyTime,
              sector3: emptyTime,
              lapTime: participant.currentTime,
              delta: emptyTime
            }
          case 2:
            return {
              lap: participant.currentLap,
              sector1: current.sector1,
              sector2: participant.currentSectorTime,
              sector3: emptyTime,
              lapTime: participant.currentTime,
              delta: emptyTime
            }
          case 3:
            return {
              lap: participant.currentLap,
              sector1: current.sector1,
              sector2: current.sector2,
              sector3: participant.currentSectorTime,
              lapTime: participant.currentTime,
              delta: emptyTime
            }
          default:
            return {
              lap: participant.currentLap,
              sector1: emptyTime,
              sector2: emptyTime,
              sector3: emptyTime,
              lapTime: participant.currentTime,
              delta: emptyTime
            }
        }
      } else {
        return getCurrentWidthoutLapTime();
      }
    } else {
      return getCurrentWidthoutLapTime();
    }
  }

  createFastest() {
    const lapTimeDetails = this.props.lapTimeDetails;
    const className = [style.current, style.darkRecord].join(" ");
    if (isJson(lapTimeDetails)) {
      return <RecordComponent className={className} name={"FASTEST"} record={lapTimeDetails.fastest} />;
    } else {
      return <RecordComponent className={className} name={"FASTEST"} record={emptyRecord} />;
    }
  }

  createAverage() {
    const lapTimeDetails = this.props.lapTimeDetails;
    const className = [style.current, style.brightRecord].join(" ");
    if (isJson(lapTimeDetails)) {
      return <RecordComponent className={className} name={"AVERAGE"} record={lapTimeDetails.average} />;
    } else {
      return <RecordComponent className={className} name={"AVERAGE"} record={emptyRecord} />;
    }
  }

  createTimeLogTable() {
    const lapTimeDetails = this.props.lapTimeDetails;
    if (!isJson(lapTimeDetails)) {
      return <div className={style.timeLogTable}></div>;
    }

    const getRecord = record => {
      if (isJson(record)) {
        return record;
      } else {
        return emptyRecord;
      }
    };

    const brightRecord = style.brightRecord;
    const darkRecord = style.darkRecord;
    const history = lapTimeDetails.history;
    const length = history.length;
    
    const records = indexArray.map(index => {
      const className = (index % 2 == 0)? darkRecord: brightRecord; 
      if (index < length) {
        return <RecordComponent key={index} className={className} name={""} record={history[length - (1 + index)]} />;
      } else {
        return <RecordComponent key={index} className={className} name={""} record={emptyRecord} />;
      }
    });

    return (
      <div className={style.timeLogTable}>
        {records}
      </div>
    );
  }

  render() {
    return (
      <div className={shareStyle.contents}>
        <div className={shareStyle.topContents}>
          <HeaderComponent />
          <RecordComponent className={[style.current, style.darkRecord].join(" ")} name={"CURRENT"} record={this.getCurrentRecord()} />
          {this.createTimeLogTable()}
          {this.createFastest()}
          {this.createAverage()}
        </div>
        <div className={shareStyle.bottomContents} onClick={this.props.onContentClick}>
          {this.createHeader()}
        </div>
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
