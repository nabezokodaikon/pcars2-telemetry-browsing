import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../share/contentNames.js";
import PicUpContainer from "./PicUpContainer.jsx";
import TelemetryDataContainer from "./TelemetryDataContainer.jsx";
import VelocityContainer from "./VelocityContainer.jsx";
import TyreContainer from "./TyreContainer.jsx";
import RaceDataContainer from "./RaceDataContainer.jsx";
import ParticipantsDataContainer from "./ParticipantsDataContainer.jsx";
import TimingsDataContainer from "./TimingsDataContainer.jsx";
import GameStateDataContainer from "./GameStateDataContainer.jsx";
import TimeStatsDataContainer from "./TimeStatsDataContainer.jsx";
import ParticipantVehicleNamesDataContainer from "./ParticipantVehicleNamesDataContainer.jsx";
import VehicleClassNamesDataContainer from "./VehicleClassNamesDataContainer.jsx";
import LapTimeDetailsContainer from "./LapTimeDetailsContainer.jsx";

class DevelopContents extends React.Component {
  constructor(props) {
    super(props);
  }

  getContentStyle() {
    return {
      zIndex: 0,
      position: "absolute",
      top: 0,
      left: 0,
      width: "100vw",
      height: "100vh"
    };
  }

  getCurrentContents() {
    switch (this.props.currentContent) {
      case contentNames.PIC_UP:
        return <PicUpContainer />;
      case contentNames.TELEMETRY_DATA:
        return <TelemetryDataContainer />;
      case contentNames.TELEMETRY_DATA_VELOCITY:
        return <VelocityContainer />;
      case contentNames.TELEMETRY_DATA_TYRE:
        return <TyreContainer />;
      case contentNames.RACE_DATA:
        return <RaceDataContainer />;
      case contentNames.PARTICIPANTS_DATA:
        return <ParticipantsDataContainer />;
      case contentNames.TIMINGS_DATA:
        return <TimingsDataContainer />;
      case contentNames.GAMESTATE_DATA:
        return <GameStateDataContainer />;
      case contentNames.TIME_STATS_DATA:
        return <TimeStatsDataContainer />;
      case contentNames.PARTICIPANT_VEHICLE_NAMES_DATA:
        return <ParticipantVehicleNamesDataContainer />;
      case contentNames.VEHICLE_CLASS_NAMES_DATA:
        return <VehicleClassNamesDataContainer />;
      case contentNames.LAP_TIME_DETAILS:
        return <LapTimeDetailsContainer />;
      default:
        return <div />;
    }
  }

  render() {
    return <div style={this.getContentStyle()}>{this.getCurrentContents()}</div>;
  }
}

DevelopContents.propTypes = {
  currentContent: PropTypes.string.isRequired
};

const mapStateToProps = state => {
  return {
    currentContent: state.currentContent
  };
};

const DevelopContentsContainer = connect(mapStateToProps)(DevelopContents);

export default DevelopContentsContainer;
