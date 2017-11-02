import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../common/jsUtil.js";

class RaceData extends React.Component {
  constructor(props) {
    super(props)
  }

  createRecords() {
    const createRaceData = () => {
      const data = this.props.raceData;
      return Object.keys(data).map(valueName => {
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
    }

    return (
      <table>
        <tbody>
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
