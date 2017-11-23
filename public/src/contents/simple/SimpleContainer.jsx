import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isJson } from "../../share/jsUtil.js";
import GearComponent from "../../share/GearComponent.jsx";
import simpleStyle from "./simple.css";
import rankIcon from "../../image/rank.png";
import lapIcon from "../../image/lap.png";
import timeIcon from "../../image/time.png";
import fuelIcon from "../../image/fuel.png";

class SimpleContent extends React.Component {
  constructor(props) {
    super(props)
  }

  createData() {
    const telemetryData = this.props.telemetryData;
    const timingsData = this.props.timingsData;
    const raceData = this.props.raceData;
    if (!isJson(telemetryData) || !isJson(timingsData) || !isJson(raceData)) {
      return <div></div>;
    }

    const carState = telemetryData.carState; 
    const participant = timingsData.formatParticipants[telemetryData.participantInfo.viewedParticipantIndex];

    const eventTimeRemaining = timingsData.eventTimeRemaining;
    const isTimedSessions = (eventTimeRemaining !== "--:--:--.---"); 
    const lapsInEvent = (raceData.lapsInEvent > 0)
      ? participant.currentLap + "/" + raceData.lapsInEvent
      : participant.currentLap; 
    const sessionText = (isTimedSessions)
      ? eventTimeRemaining
      : lapsInEvent

    return (
      <div className={simpleStyle.table}>
        <div className={simpleStyle.record}>
          <div className={simpleStyle.iconCell}>
            <div>
              <img src={rankIcon} />
            </div>
          </div>
          <div className={simpleStyle.valueCell}>
            <div>
              <span>{participant.racePosition}/{timingsData.numParticipants}</span>
            </div>
          </div>
        </div>
        <div className={simpleStyle.record}>
          <div className={simpleStyle.iconCell}>
            <div>
              <img src={lapIcon} />
            </div>
          </div>
          <div className={simpleStyle.valueCell}>
            <div>
              <span>{sessionText}</span>
            </div>
          </div>
        </div>
        <div className={simpleStyle.record}>
          <div className={simpleStyle.iconCell}>
            <div>
              <img src={timeIcon} />
            </div>
          </div>
          <div className={simpleStyle.currentLap}>
            <div>
              <span>{participant.currentLap}</span>
            </div>
          </div>
          <div className={simpleStyle.currentTime}>
            <div>
              <span>{participant.currentTime}</span>
            </div>
          </div>
        </div>
        <div className={simpleStyle.record}>
          <div className={simpleStyle.iconCell}>
            <div>
              <img src={fuelIcon} />
            </div>
          </div>
          <div className={simpleStyle.valueCell}>
            <div>
              <span>{carState.fuelRemaining}L</span>
            </div>
          </div>
        </div>
      </div>
    );
  }

  render() {
    const props = this.props;

    return (
      <div className={simpleStyle.content} onClick={props.onContentsClick}>
        <GearComponent className={simpleStyle.gear} isMeter={props.isMeter} telemetryData={props.telemetryData} />
        {this.createData()}
      </div>
    );
  }
}

SimpleContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  timingsData: PropTypes.object.isRequired,
  raceData: PropTypes.object.isRequired,
  onContentsClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData,
    timingsData: data.timingsData,
    raceData: data.raceData
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onContentsClick: () => {
      dispatch(currentContent(contentNames.MOTEC))
    }
  };
};

const SimpleContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(SimpleContent);

export default SimpleContainer;
