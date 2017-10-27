import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../common/contentNames.js";
import { requestConnectionInfo } from "../appActionCreators.js";
import ParticipantInfoStringsContainer from "./ParticipantInfoStringsContainer.jsx";
import ParticipantInfoStringsAdditionalContainer from "./ParticipantInfoStringsAdditionalContainer.jsx";
import GameStateDataContainer from "./GameStateDataContainer.jsx";
import ParticipantInfoDataContainer from "./ParticipantInfoDataContainer.jsx";
import ParticipantInfoContainer from "./ParticipantInfoContainer.jsx";
import CarStateDataContainer from "./CarStateDataContainer.jsx";
import UnfilteredInputDataContainer from "./UnfilteredInputDataContainer.jsx";
import EventInfoDataContainer from "./EventInfoDataContainer.jsx";
import TimingInfoDataContainer from "./TimingInfoDataContainer.jsx";
import SectorTimeDataContainer from "./SectorTimeDataContainer.jsx";
import FlagDataContainer from "./FlagDataContainer.jsx";
import PitInfoDataContainer from "./PitInfoDataContainer.jsx";
import CarStateVecotrDataContainer from "./CarStateVecotrDataContainer.jsx"
import TyreDataContainer from "./TyreDataContainer.jsx";
import OtherUdpDataContainer from "./OtherUdpDataContainer.jsx";
import CarDamageDataContainer from "./CarDamageDataContainer.jsx";
import WeatherDataContainer from "./WeatherDataContainer.jsx"
import SimpleProtoTypeContainer from "./SimpleProtoTypeContainer.jsx";

class Contents extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount() {
    this.props.onRequestConnectionInfo();
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
          return <ParticipantInfoStringsContainer />;
        case contentNames.PARTICIPANT_INFO_STRINGS_ADDITIONAL:
          return <ParticipantInfoStringsAdditionalContainer />;
        case contentNames.GAMESTATE_DATA:
          return <GameStateDataContainer />;
        case contentNames.PARTICIPANT_INFO_DATA:
          return <ParticipantInfoDataContainer />;
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
          return <WeatherDataContainer />;
        case contentNames.SIMPLE:
          return <SimpleProtoTypeContainer />;
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
  onRequestConnectionInfo: PropTypes.func.isRequired
}

const mapStateToProps = state => {
  return {
    currentContent: state.currentContent
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onRequestConnectionInfo: () => {
      dispatch(requestConnectionInfo());
    }
  };
};

const ContentsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(Contents);

export default ContentsContainer;
