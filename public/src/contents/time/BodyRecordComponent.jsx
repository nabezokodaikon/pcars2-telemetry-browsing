import React from "react";
import PropTypes from "prop-types";
import timeStyle from "./time.css";

export default class BodyRecordComponent extends React.Component {
  constructor(props) {
    super(props);
  }

  getDeltaFontColor(delta) {
    const prefix = delta.charAt(0);
    const length = delta.length;
    if (length == 10) {
      if (prefix == "+") {
        return {
          color: "#C14544"
        };
      } else {
        return {
          color: "#90C143"
        };
      }
    } else {
      return {
        color: "#E3E4E5"
      };
    }
  }

  render() {
    const props = this.props;
    const record = props.record;
    const delta = record.delta;
    return (
      <div className={timeStyle.bodyColumn}>
        <div className={timeStyle.headerCell}>
          <div>
            <span>{props.name}</span>
          </div>
        </div>
        <div className={timeStyle.lapCountCell}>
          <div>
            <span>{record.lap}</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>{record.sector1}</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>{record.sector2}</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>{record.sector3}</span>
          </div>
        </div>
        <div className={timeStyle.lapTimeCell}>
          <div>
            <span>{record.lapTime}</span>
          </div>
        </div>
        <div className={timeStyle.deltaTimeCell}>
          <div>
            <span style={this.getDeltaFontColor(delta)}>{delta}</span>
          </div>
        </div>
      </div>
    );
  }
}

BodyRecordComponent.propTypes = {
  name: PropTypes.string.isRequired,
  record: PropTypes.object.isRequired
};
