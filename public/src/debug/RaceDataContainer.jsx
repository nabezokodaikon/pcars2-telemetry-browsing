import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../common/jsUtil.js";

class RaceData extends React.Component {
  constructor(props) {
    super(props)
  }

  createRecords() {
    const createBase = () => {
      const data = this.props.raceData.base;
      return Object.keys(data).map(key => {
        const value = data[key];
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      });
    }

    const createRaceData = () => {
      const data = this.props.raceData;
      return Object.keys(data).filter(key => key !== "base").map(valueName => {
        const value = data[valueName];
        if (isJson(value)) {
          return Object.keys(value).map(key => {
            return (
              <tr key={key}>
                <td>{key}</td>
                <td>{value[key].toString()}</td>
              </tr>
            );
          });
        } else {
          return (
            <tr key={valueName}>
              <td>{valueName}</td>
              <td>{value.toString()}</td>
            </tr>
          );
        }
      });
    }

    return (
      <table>
        <tbody>
          <tr>
            <td>PacketBase</td>
          </tr>
          {createBase()}
          <tr>
            <td>RaceData</td>
          </tr>
          {createRaceData()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.raceData)) {
      return <div></div>;
    } else {
      return (
        <div>
          {this.createRecords()}
        </div>
      );
    }
  }
}

RaceData.propTypes = {
  raceData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    raceData: state.currentUdpData.raceData
  };
};

const RaceDataContainer = connect(
  mapStateToProps
)(RaceData);

export default RaceDataContainer;
