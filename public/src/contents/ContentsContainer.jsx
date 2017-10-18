import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../common/contentNames.js";
import { openWebSocket } from "../appActionCreators.js";
import GameStateDataContainer from "./gameStateData/GameStateDataContainer.jsx";
import ParticipantInfoDataContainer from "./participantInfoData/ParticipantInfoDataContainer.jsx";
import ParticipantInfoContainer from "./participantInfo/ParticipantInfoContainer.jsx";
import CarStateDataContainer from "./carStateData/CarStateDataContainer.jsx";
import UnfilteredInputDataContainer from "./unfilteredInputData/UnfilteredInputDataContainer.jsx";
import EventInfoDataContainer from "./eventInfoData/EventInfoDataContainer.jsx";
import TimingInfoDataContainer from "./timingInfoData/TimingInfoDataContainer.jsx";
import SectorTimeDataContainer from "./sectorTimeData/SectorTimeDataContainer.jsx";
import FlagDataContainer from "./flagData/FlagDataContainer.jsx";
import PitInfoDataContainer from "./pitInfoData/PitInfoDataContainer.jsx";
import CarStateVecotrDataContainer from "./carStateVecotrData/CarStateVecotrDataContainer.jsx"
import TyreDataContainer from "./tyreData/TyreDataContainer.jsx";
import OtherUdpDataContainer from "./otherUdpData/OtherUdpDataContainer.jsx";
import CarDamageDataContainer from "./carDamageData/CarDamageDataContainer.jsx";
import WeatherDataContainer from "./weatherData/WeatherDataContainer.jsx"

class Contents extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.onOpenWebSocket();
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
          return <ParticipantInfoDataContainer />
        case contentNames.PARTICIPANT_INFO:
          return <ParticipantInfoContainer />;
        case contentNames.UNFILTEREDINPUT_DATA:
          return <UnfilteredInputDataContainer />;
        case contentNames.EVENT_INFO_DATA:
          return <EventInfoDataContainer />;
        case contentNames.TIMING_INFO_DATA:
          return <TimingInfoDataContainer />;
        case contentNames.SECTOR_TIME_DATA:
          return <SectorTimeDataContainer />;
        case contentNames.FLAG_DATA:
          return <FlagDataContainer />;
        case contentNames.PITINFO_DATA:
          return <PitInfoDataContainer />;
        case contentNames.CAR_STATE_DATA:
          return <CarStateDataContainer />;
        case contentNames.CAR_STATE_VECOTRDATA:
          return <CarStateVecotrDataContainer />;
        case contentNames.TYRE_DATA:
          return <TyreDataContainer />;
        case contentNames.OTHER_UDP_DATA:
          return <OtherUdpDataContainer />;
        case contentNames.CAR_DAMAGE_DATA:
          return <CarDamageDataContainer />;
        case contentNames.WEATHER_DATA:
          return <WeatherDataContainer />
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
  currentContent: PropTypes.string.isRequired,
  onOpenWebSocket: PropTypes.func.isRequired
}

const mapStateToProps = state => {
  return {
    currentContent: state.currentContent
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onOpenWebSocket: () => {
      dispatch(openWebSocket());
    }
  };
};

const ContentsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Contents);

export default ContentsContainer;
