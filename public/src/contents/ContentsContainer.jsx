import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../common/contentNames.js";
import GameStateDataContainer from "./gameStateData/GameStateDataContainer.jsx";
import CarStateDataContainer from "./carStateData/CarStateDataContainer.jsx";

class Contents extends React.Component {
  constructor(props) {
    super(props);
  }

  getContentStyle() {
    return {
      zIndex: 0,
      position: "absolute",
      width: "100vw",
      height: "100vh",
      backgroundColor: "#EEEEEE"
    };
  }

  getCurrentContents() {
    switch (this.props.currentContent) {
        case contentNames.PARTICIPANT_INFO_STRINGS:
          return <div></div>;
        case contentNames.PARTICIPANT_INFO_STRINGS_ADDITIONAL:
          return <div></div>;
        case contentNames.GAMESTATE_DATA:
          return <GameStateDataContainer />;
        case contentNames.PARTICIPANT_INFO_DATA:
          return <div></div>;
        case contentNames.PARTICIPANT_INFO:
          return <div></div>;
        case contentNames.UNFILTEREDINPUT_DATA:
          return <div></div>;
        case contentNames.EVENT_INFO_DATA:
          return <div></div>;
        case contentNames.TIMING_INFO_DATA:
          return <div></div>;
        case contentNames.SECTOR_TIME_DATA:
          return <div></div>;
        case contentNames.FLAG_DATA:
          return <div></div>;
        case contentNames.PITINFO_DATA:
          return <div></div>;
        case contentNames.CAR_STATE_DATA:
          return <CarStateDataContainer />;
        case contentNames.CAR_STATE_VECOTRDATA:
          return <div></div>;
        case contentNames.TYRE_DATA:
          return <div></div>;
        case contentNames.TYRE_UDP_DATA:
          return <div></div>;
        case contentNames.OTHER_UDP_DATA:
          return <div></div>;
        case contentNames.CAR_DAMAGE_DATA:
          return <div></div>;
        default:
          return <div></div>;
    }
  }

  render() {
    return (
      <div style={this.getContentStyle()}>
        {this.getCurrentContents()}
      </div>
    );
  }
}

Contents.propTypes = {
  currentContent: PropTypes.string.isRequired
}

const mapStateToProps = state => {
  return {
    currentContent: state.currentContent
  };
};

const ContentsContainer = connect(
  mapStateToProps
)(Contents);

export default ContentsContainer;
