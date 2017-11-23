import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isJson } from "../../share/jsUtil.js";
import LargeGearComponent from "../../share/LargeGearComponent.jsx";
import LargeFuelComponent from "../../share/LargeFuelComponent.jsx";
import shareStyle from "../../share/largeContent.css";
import style from "./default.css";
import TimeLogRecordComponent from "./TimeLogRecordComponent.jsx";

class DefaultContent extends React.Component {
  constructor(props) {
    super(props);
  }

  createSession() {
    const timingsData = this.props.timingsData;
    if (!isJson(timingsData)) {
      return <div></div>;
    }

    return (
      <div className={style.session}>
        <div className={[style.sessionHeader, shareStyle.header].join(" ")}>
          <span>SESSION</span>
        </div>
        <div className={[style.sessionValue, shareStyle.value].join(" ")}>
          <span>{timingsData.eventTimeRemaining}</span>
        </div>
      </div>
    );
  }

  createPosition() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    const timingsData = props.timingsData;
    if (!isJson(telemetryData) || !isJson(timingsData)) {
      return <div></div>;
    }

    const participant = timingsData.formatParticipants[telemetryData.participantInfo.viewedParticipantIndex];

    return (
      <div className={[style.position, style.border].join(" ")}>
        <div className={[style.positionHeader, shareStyle.header].join(" ")}>
          <span>POSITION</span>
        </div>
        <div className={[style.positionValue, shareStyle.value].join(" ")}>
          <span>{participant.racePosition}/{timingsData.numParticipants}</span>
        </div>
      </div>
    );
  }

  createCurrent() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    const timingsData = props.timingsData;
    const raceData = props.raceData;
    if (!isJson(telemetryData) || !isJson(timingsData) || !isJson(raceData)) {
      return <div></div>;
    }

    const participant = timingsData.formatParticipants[telemetryData.participantInfo.viewedParticipantIndex];
    const lapCount = (raceData.isTimedSessions)
      ? participant.currentLap
      : participant.currentLap + "/" + raceData.lapsInEvent;

    return (
      <div className={[style.current, style.border].join(" ")}>
        <div className={[style.currentLap, shareStyle.header].join(" ")}>
          <span>CURRENT</span>
        </div>
        <div className={[style.currentLapCount, style.lap].join(" ")}>
          <span>{lapCount}</span>
        </div>
        <div className={[style.currentLapTime, style.time].join(" ")}>
          <span>{participant.currentTime}</span>
        </div>
      </div>
    );
  }

  createTimeLog() {
    const props = this.props;
    const lapTimeDetails = props.lapTimeDetails;
    if (!isJson(lapTimeDetails)) {
      return (
        <div></div>
      );
    }

    const getRecord = record => {
      if (isJson(record)) {
        return record;
      } else {
        return {
          lap: "---",
          sector1: "--:--.---",
          sector2: "--:--.---",
          sector3: "--:--.---",
          lapTime: "--:--.---",
          delta: "--:--.---"
        };
      }
    };

    const history = lapTimeDetails.history;
    const historyLength = history.length;

    return (
      <div className={style.timeLogs}>
        <TimeLogRecordComponent record={getRecord(history[historyLength - 1])} />
        <TimeLogRecordComponent record={getRecord(history[historyLength - 2])} />
        <TimeLogRecordComponent record={getRecord(history[historyLength - 3])} />
        <TimeLogRecordComponent record={getRecord(history[historyLength - 4])} />
      </div>
    );
  }

  createFastest() {
    const props = this.props;
    const lapTimeDetails = props.lapTimeDetails;
    if (!isJson(lapTimeDetails)) {
      return <div></div>;
    }

    const fastest = lapTimeDetails.fastest;

    return (
      <div className={[style.otherTime, style.border].join(" ")}>
        <div className={[style.otherTimeHeader, shareStyle.header].join(" ")}>
          <span>FASTEST</span>
        </div>
        <div className={[style.otherTimeLap, shareStyle.header].join(" ")}>
          <span>LAP</span>
        </div>
        <div className={[style.otherTimeLapCount, style.lap].join(" ")}>
          <span>{fastest.lap}</span>
        </div>
        <div className={[style.otherTimeTime, style.time].join(" ")}>
          <span>{fastest.lapTime}</span>
        </div>
      </div>
    );
  }

  createAverage() {
    const props = this.props;
    const lapTimeDetails = props.lapTimeDetails;
    if (!isJson(lapTimeDetails)) {
      return <div></div>;
    }

    const average = lapTimeDetails.average;

    return (
      <div className={[style.otherTime, style.border].join(" ")}>
        <div className={[style.otherTimeHeader, shareStyle.header].join(" ")}>
          <span>AVERAGE</span>
        </div>
        <div className={[style.otherTimeLap, shareStyle.header].join(" ")}>
          <span>LAP</span>
        </div>
        <div className={[style.otherTimeLapCount, style.lap].join(" ")}>
          <span>{average.lap}</span>
        </div>
        <div className={[style.otherTimeTime, style.time].join(" ")}>
          <span>{average.lapTime}</span>
        </div>
      </div>
    );
  }

  render() {
    const props = this.props;

    return (
      <div className={shareStyle.contents}>
        <div className={shareStyle.topContents}>
          <div className={shareStyle.leftContents}>
            <LargeGearComponent isMeter={props.isMeter} telemetryData={props.telemetryData} />
          </div>
          <div className={shareStyle.rightContents}>
            {this.createSession()}
            {this.createPosition()}
            {this.createCurrent()}
            {this.createTimeLog()}
            {this.createFastest()}
            {this.createAverage()}
          </div>
        </div>
        <div className={shareStyle.bottomContents}>
          <LargeFuelComponent telemetryData={props.telemetryData} fuelData={props.fuelData} />
        </div>
      </div>
    );
  }
}

DefaultContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  timingsData: PropTypes.object.isRequired,
  raceData: PropTypes.object.isRequired,
  lapTimeDetails: PropTypes.object.isRequired,
  fuelData: PropTypes.object.isRequired,
  onContentsClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData,
    timingsData: data.timingsData,
    raceData: data.raceData,
    lapTimeDetails: data.lapTimeDetails,
    fuelData: data.fuelData
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onContentsClick: () => {
      // dispatch(currentContent(contentNames.TIME))
    }
  };
};

const DefaultContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DefaultContent);

export default DefaultContainer;