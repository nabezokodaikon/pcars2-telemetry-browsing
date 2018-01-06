import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isJson } from "../../share/jsUtil.js";
import LargeGear from "../../share/LargeGear.jsx";
import LargeFuel from "../../share/LargeFuel.jsx";
import Damage from "../../share/Damage.jsx";
import shareStyle from "../../share/largeContent.css";
import style from "./default.css";
import TimeLogRecord from "./TimeLogRecord.jsx";

class DefaultContent extends React.Component {
  constructor(props) {
    super(props);
  }

  createSession() {
    const timingsData = this.props.timingsData;
    if (!isJson(timingsData)) {
      return <div />;
    }

    return (
      <div className={[style.session, style.border].join(" ")}>
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
      return <div />;
    }

    const participant = timingsData.formatParticipants[telemetryData.participantInfo.viewedParticipantIndex];

    return (
      <div className={[style.position, style.border].join(" ")}>
        <div className={[style.positionHeader, shareStyle.header].join(" ")}>
          <span>POSITION</span>
        </div>
        <div className={[style.positionValue, shareStyle.value].join(" ")}>
          <span>
            {participant.racePosition}/{timingsData.numParticipants}
          </span>
        </div>
      </div>
    );
  }

  createCurrent() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    const timingsData = props.timingsData;
    const raceData = props.raceData;
    const realTimeGap = props.realTimeGap;
    if (!isJson(telemetryData) || !isJson(timingsData) || !isJson(raceData) || !isJson(realTimeGap)) {
      return <div />;
    }

    const participant = timingsData.formatParticipants[telemetryData.participantInfo.viewedParticipantIndex];
    const lapCount = raceData.isTimedSessions
      ? participant.currentLap
      : participant.currentLap + "/" + raceData.lapsInEvent;

    const gapTime = realTimeGap.gapTime;

    const getGapStyle = time => {
      const prefix = time.charAt(0);
      const length = time.length;
      if (length == 10) {
        if (prefix == "+") {
          return [style.currentLapGap, style.plusGap].join(" ");
        } else {
          return [style.currentLapGap, style.minusGap].join(" ");
        }
      } else {
        return [style.currentLapGap, style.evenGap].join(" ");
      }
    };

    return (
      <div className={[style.current, style.border].join(" ")}>
        <div className={[style.currentLapHeader, shareStyle.header].join(" ")}>
          <span>CURRENT</span>
        </div>
        <div className={[style.currentLapCount, style.lap].join(" ")}>
          <span>{lapCount}</span>
        </div>
        <div className={style.currentLapTimeBox}>
          <div className={getGapStyle(gapTime)}>
            <span>{gapTime}</span>
          </div>
          <div className={[style.currentLapTime, style.time].join(" ")}>
            <span>{participant.currentTime}</span>
          </div>
        </div>
      </div>
    );
  }

  createTimeLog() {
    const props = this.props;
    const lapTimeDetails = props.lapTimeDetails;
    if (!isJson(lapTimeDetails)) {
      return <div />;
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
      <div className={[style.timeLogs, style.border].join(" ")}>
        <TimeLogRecord record={getRecord(history[historyLength - 1])} />
        <TimeLogRecord record={getRecord(history[historyLength - 2])} />
        <TimeLogRecord record={getRecord(history[historyLength - 3])} />
      </div>
    );
  }

  createFastest() {
    const props = this.props;
    const lapTimeDetails = props.lapTimeDetails;
    if (!isJson(lapTimeDetails)) {
      return <div />;
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
      return <div />;
    }

    const average = lapTimeDetails.average;

    return (
      <div className={style.otherTime}>
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
            <LargeGear isMeter={props.isMeter} telemetryData={telemetryData} />
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
          <LargeFuel telemetryData={telemetryData} fuelData={props.fuelData} />
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

DefaultContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  timingsData: PropTypes.object.isRequired,
  raceData: PropTypes.object.isRequired,
  lapTimeDetails: PropTypes.object.isRequired,
  realTimeGap: PropTypes.object.isRequired,
  fuelData: PropTypes.object.isRequired,
  onContentClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData,
    timingsData: data.timingsData,
    raceData: data.raceData,
    lapTimeDetails: data.lapTimeDetails,
    realTimeGap: data.realTimeGap,
    fuelData: data.fuelData
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onContentClick: () => {
      dispatch(currentContent(contentNames.ENGINE));
    }
  };
};

const DefaultContainer = connect(mapStateToProps, mapDispatchToProps)(DefaultContent);

export default DefaultContainer;
