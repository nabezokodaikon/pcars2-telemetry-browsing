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

    const createHeader = () => {
      const firstData = data[0];
      return Object.keys(firstData).map(key => {
        const value = firstData[key];
        if (isArray(value)) {
          return value.map((childValue, index) => {
            const indexKey = key + "[" + index + "]";
            return (
              <td key={indexKey}>{indexKey}</td>
            );
          });
        } else {
          return (<td key={key}>{key}</td>);
        }
      });
    };

    const createValueFields = (value, key) => {
      if (isArray(value)) {
        return value.map((childValue, index) => {
          const indexKey = key + "[" + index + "]";
          return (
            <td key={indexKey}>{childValue}</td>
          );
        });
      } else {
        return <td key={key}>{value}</td>;
      }
    };

    const createRecords = () => {
      return data.map((value, index) => {
        return (
          <tr key={index}>
            <td key={index}>{index}</td>
            {Object.keys(value).map(key => createValueFields(value[key], key))}
          </tr>
        );
      });
    };

    return (
      <table>
        <thead>
          <tr>
            <td>Index</td>
            {createHeader()}
          </tr>
        </thead>
        <tbody>
          {createRecords()}
        </tbody>
      </table>
    );
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
