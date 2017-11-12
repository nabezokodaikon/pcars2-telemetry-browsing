import React from "react";
import PropTypes from "prop-types";
import timeStyle from "./time.css";

export default class BodyRecordComponent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className={timeStyle.bodyColumn}>
        <div className={timeStyle.headerCell}>
          <div>
            <span>{this.props.name}</span>
          </div>
        </div>
        <div className={timeStyle.lapCountCell}>
          <div>
            <span>{this.props.record.lap}</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>{this.props.record.sector1}</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>{this.props.record.sector2}</span>
          </div>
        </div>
        <div className={timeStyle.sectorCell}>
          <div>
            <span>{this.props.record.sector3}</span>
          </div>
        </div>
        <div className={timeStyle.lapTimeCell}>
          <div>
            <span>{this.props.record.lapTime}</span>
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
