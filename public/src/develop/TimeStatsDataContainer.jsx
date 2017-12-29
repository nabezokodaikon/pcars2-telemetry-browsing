import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../share/jsUtil.js";

class TimeStatsData extends React.Component {
  constructor(props) {
    super(props);
  }

  createRecords() {
    const createBase = () => {
      const data = this.props.timeStatsData.base;
      return Object.keys(data).map(key => {
        const value = data[key];
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      });
    };

    const createTimeStatsData = () => {
      const data = this.props.timeStatsData;
      return Object.keys(data)
        .filter(valueName => valueName !== "base" && valueName != "stats")
        .map(valueName => {
          const value = data[valueName];
          if (isJson(value)) {
            return Object.keys(value).map(key => {
              return (
                <tr key={key}>
                  <td>{key}</td>
                  <td>{value[key]}</td>
                </tr>
              );
            });
          } else {
            return (
              <tr key={valueName}>
                <td>{valueName}</td>
                <td>{value}</td>
              </tr>
            );
          }
        });
    };

    const createHeader = () => {
      const data = this.props.timeStatsData.stats.formatParticipants;
      const firstData = data[0];
      return Object.keys(firstData).map(key => {
        const value = firstData[key];
        return <td key={key}>{key}</td>;
      });
    };

    const createValueFields = (value, key) => {
      return <td key={key}>{value}</td>;
    };

    const createRecords = () => {
      const data = this.props.timeStatsData.stats.formatParticipants;
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
        <tbody>
          <tr>
            <td>PacketBase</td>
          </tr>
          {createBase()}
          <tr>
            <td>TimeStatsData</td>
          </tr>
          {createTimeStatsData()}
          <tr>
            <td>participants</td>
          </tr>
          <tr>
            <td>index</td>
            {createHeader()}
          </tr>
          {createRecords()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.timeStatsData)) {
      return <div />;
    } else {
      return <div>{this.createRecords()}</div>;
    }
  }
}

TimeStatsData.propTypes = {
  timeStatsData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    timeStatsData: state.currentUdpData.timeStatsData
  };
};

const TimeStatsDataContainer = connect(mapStateToProps)(TimeStatsData);

export default TimeStatsDataContainer;
