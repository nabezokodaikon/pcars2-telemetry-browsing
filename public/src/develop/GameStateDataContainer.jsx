import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../share/jsUtil.js";

class GameStateData extends React.Component {
  constructor(props) {
    super(props);
  }

  createRecords() {
    const createBase = () => {
      const data = this.props.gameStateData.base;
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

    const createGameStateData = () => {
      const data = this.props.gameStateData;
      return Object.keys(data)
        .filter(key => key !== "base")
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

    return (
      <table>
        <tbody>
          <tr>
            <td>PacketBase</td>
          </tr>
          {createBase()}
          <tr>
            <td>GameStateData</td>
          </tr>
          {createGameStateData()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.gameStateData)) {
      return <div />;
    } else {
      return <div>{this.createRecords()}</div>;
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

const GameStateDataContainer = connect(mapStateToProps)(GameStateData);

export default GameStateDataContainer;
