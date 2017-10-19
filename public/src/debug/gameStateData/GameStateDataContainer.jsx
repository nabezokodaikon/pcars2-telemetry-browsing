import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../../common/jsUtil.js";

class GameStateData extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    }

    const createRecords = () => {
      const data = this.props.telemetryData.gameStateData;
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

    return (
      <table>
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

GameStateData.propTypes = {
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData
  };
};

const GameStateDataContainer = connect(
  mapStateToProps
)(GameStateData);

export default GameStateDataContainer;
