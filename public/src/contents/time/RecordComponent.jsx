import React from "react";
import PropTypes from "prop-types";
import style from "./time.css";

export default class RecordComponent extends React.Component {
  constructor(props) {
    super(props);
  }

  getDeltaStyle(delta) {
    const prefix = delta.charAt(0);
    const length = delta.length;
    if (length == 10) {
      if (prefix == "+") {
        return style.plusDeltaCell;
      } else {
        return style.minusDeltaCell;
      }
    } else {
      return style.evenDeltaCell;
    }
  }

  render() {
    const props = this.props;
    const record = props.record;
    const delta = record.delta;

    return (
      <div className={props.className}>
        <div className={style.titleCell}>
          <span>{props.name}</span>
        </div>
        <div className={style.lapCell}>
          <span>{record.lap}</span>
        </div>
        <div className={style.timeCell}>
          <span>{record.sector1}</span>
        </div>
        <div className={style.timeCell}>
          <span>{record.sector2}</span>
        </div>
        <div className={style.timeCell}>
          <span>{record.sector3}</span>
        </div>
        <div className={style.timeCell}>
          <span>{record.lapTime}</span>
        </div>
        <div className={this.getDeltaStyle(delta)}>
          <span>{delta}</span>
        </div>
      </div>
    );
  }
}

RecordComponent.propTypes = {
  name: PropTypes.string.isRequired,
  record: PropTypes.object.isRequired,
  className: PropTypes.string.isRequired
};
