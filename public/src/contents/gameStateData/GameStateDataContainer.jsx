import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { openWebSocket } from "../../appActionCreators.js";
import { isTelemetryDataFrameType } from "../../common/telemetryUtil.js";

class GameStateData extends React.Component {
  constructor(props) {
    super(props)
  }

  componentDidMount() {
    console.log("componentDidMount");
    this.props.onOpenWebSocket();
  }

  getData() {
    if (!isTelemetryDataFrameType(this.props.telemetry)) {
      return <div></div>;
    }

    const createRecords = () => {
      const data = this.props.telemetry.gameStateData;
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
  telemetry: PropTypes.object.isRequired,
  onOpenWebSocket: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  return {
    telemetry: state.telemetry
  };
};

const mapDispatchToProps = dispatch => {
  console.log("mapDispatchToProps");
  return {
    onOpenWebSocket: () => {
      dispatch(openWebSocket());
    }
  };
};

const GameStateDataContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(GameStateData);

export default GameStateDataContainer;
