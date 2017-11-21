import React from "react";
import PropTypes from "prop-types";
import style from "./default.css";

export default class TimeLogRecordComponent extends React.Component {
  constructor(props) {
    super(props);
  }

  getDeltaStyle(delta) {
    const prefix = delta.charAt(0);
    const length = delta.length;
    if (length == 10) {
      if (prefix == "+") {
        return [style.timeLogDelta, style.plusDelta].join(" ");
      } else {
        return [style.timeLogDelta, style.minusDelta].join(" ");
      }
    } else {
      return [style.timeLogDelta, style.evenDelta].join(" ");
    }
  }

  render() {
    const props = this.props;
    const record = props.record;
    const delta = record.delta;

    return (
      <div>
        <div className={[style.timeLogLap, style.header].join(" ")}>
          <span>LAP</span>
        </div>
        <div className={[style.timeLogLapCount, style.lap].join(" ")}>
          <span>{record.lap}</span>
        </div>
        <div className={[style.timeLogTime, style.time].join(" ")}>
          <span>{record.lapTime}</span>
        </div>
        <div className={this.getDeltaStyle(delta)}>
          <span>{delta}</span>
        </div>
      </div>
    );
  }
}

TimeLogRecordComponent.propTypes = {
  record: PropTypes.object.isRequired
};
