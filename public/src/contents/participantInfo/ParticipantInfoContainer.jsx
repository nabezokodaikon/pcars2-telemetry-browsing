import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray } from "../../common/jsUtil.js";
import { isTelemetryDataFrameType } from "../../common/telemetryUtil.js";

class ParticipantInfo extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isTelemetryDataFrameType(this.props.telemetry)) {
      return <div></div>;
    }

    const data = this.props.telemetry.participantInfo;

    const map = {};
    const firstData = data[0];
    Object.keys(firstData).forEach(valueName => {
      const value = firstData[valueName];
      if (isArray(value)) {
        value.forEach((v, i) => map[valueName + "[" + i + "]"] = Array(data.length)); 
      } else {
        map[valueName] = Array(data.length);
      }
    });

    data.forEach((record, index) => {
      Object.keys(record).forEach(valueName => {
        const value = record[valueName];
        if (isArray(value)) {
          value.forEach((v, i) => map[valueName + "[" + i + "]"][index] = v); 
        } else {
          map[valueName][index] = value;
        }
      });
    });

    return Object.keys(map).map(valueName => {
      const valueArray = map[valueName];
      const valueNameField = <td>{valueName}</td>;
      const valuesFieldArray = valueArray.map(value => <td>{value}</td>);
      return (
        <tr key={valueName}>
          {valueNameField}
          {valuesFieldArray}
        </tr>
      );
    });
  }

  render() {
    return this.getData();
  }
}

ParticipantInfo.propTypes = {
  telemetry: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetry: state.telemetry
  };
};

const ParticipantInfoContainer = connect(
  mapStateToProps
)(ParticipantInfo);

export default ParticipantInfoContainer;
