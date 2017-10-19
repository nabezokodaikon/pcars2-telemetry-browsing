import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../../common/jsUtil.js";

class ParticipantInfo extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    }

    const data = this.props.telemetryData.participantInfo;

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
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData
  };
};

const ParticipantInfoContainer = connect(
  mapStateToProps
)(ParticipantInfo);

export default ParticipantInfoContainer;
