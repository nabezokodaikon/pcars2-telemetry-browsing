import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../common/jsUtil.js";

class GameStateData extends React.Component {
  constructor(props) {
    super(props)
  }

  createRecords() {
    const createGameStateData = () => {
      const data = this.props.gameStateData;
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
          {createGameStateData()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.gameStateData)) {
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

GameStateData.propTypes = {
  gameStateData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    gameStateData: state.currentUdpData.gameStateData
  };
};

const GameStateDataContainer = connect(
  mapStateToProps
)(GameStateData);

export default GameStateDataContainer;
