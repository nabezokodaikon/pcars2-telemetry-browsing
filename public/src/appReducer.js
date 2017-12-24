import { combineReducers } from "redux";
import * as actionTypes from "./appActionTypes.js";
import * as contentNames from "./share/contentNames";

function currentContent(state = contentNames.DEFAULT, action) {
  switch (action.type) {
      case actionTypes.SELECTED_CONTENT:
        return action.selectedContent;
      default:
        return state;
  }
}

function isMenuVisible(state = false, action) {
  switch (action.type) {
      case actionTypes.TOGGLE_MENU:
        return !state;
      default:
        return state;
  }
}

const initialOptions = {
  isCelsius: true,
  isMeter: true,
  isBar: true
};

function options(state = initialOptions, action) {
  switch (action.type) {
    case actionTypes.GOT_ALL_OPTIONS:
      return {
        isCelsius: action.options.isCelsius,
        isMeter: action.options.isMeter,
        isBar: action.options.isBar
      };
    case actionTypes.CHANGED_TEMP_UNIT:
      return { ...state, isCelsius: action.isCelsius };
    case actionTypes.CHANGED_DISTANCE_UNIT:
      return { ...state, isMeter: action.isMeter };
    case actionTypes.CHANGED_AIR_PRESSURE_UNIT:
      return { ...state, isBar: action.isBar };
    default:
      return state;
  }
}

const initialConnectionInfo = {
  ipAddress: "127.0.0.1",
  port: 9000,
  isGot: false
}

function connectionInfo(state = initialConnectionInfo, action) {
  switch (action.type) {
    case actionTypes.GOT_CONNECTION_INFO:
      return {
        ipAddress: action.connectionInfo.ipAddress,
        port: action.connectionInfo.port,
        isGot: action.connectionInfo.isGot
      };
    default:
      return state;
  }
} 

function isWebSocketOpened(state = false, action) {
  switch (action.type) {
    case actionTypes.OPEN_WEBSOCKET:
      return action.isWebSocketOpened;
    default:
      return state;
  }
}

const initialButtonBox = {
  mappings: []
};

function buttonBox(state = initialButtonBox, action) {
  switch (action.type) {
    case actionTypes.GOT_ALL_BUTTON_BOX_MAPPINGS:
      return {
        mappings: action.mappings
      };
    case actionTypes.DESCRIPTION_CHANGE:
      return {
        mappings: state.mappings.map((v, i) => {
          if (i == action.index) {
            return { ...v, description: action.description };
          } else {
            return v;
          }
        })
      }
    default:
      return state;
  }
}

const initialUdpData = {
  telemetryData: {},
  raceData: {},
  participantsData: {},
  timingsData: {},
  gameStateData: {},
  weatherState: {},
  vehicleNames: {},
  timeStatsData: {},
  participantVehicleNamesData: {},
  vehicleClassNamesData: {},
  lapTimeDetails: {},
  aggregateTime: {},
  fuelData: {},
  telemetrySummary: {}
};

function currentUdpData(state = initialUdpData, action) {
  switch (action.type) {
    case actionTypes.RECEIVED_CAR_PHYSICS:
      return { ...state, telemetryData: action.receivedCarPhysics };
    case actionTypes.RECEIVED_RACE_DEFINITION:
      return { ...state, raceData: action.receivedRaceDefinition };
    case actionTypes.RECEIVED_PARTICIPANTS:
      return { ...state, participantsData: action.receivedParticipants };
    case actionTypes.RECEIVED_TIMINGS:
      return { ...state, timingsData: action.receivedTimings };
    case actionTypes.RECEIVED_GAME_STATE:
      return { ...state, gameStateData: action.receivedGameState };
    case actionTypes.RECEIVED_WEATHER_STATE:
      return { ...state, weatherState: action.receivedWeatherState };
    case actionTypes.RECEIVED_VEHICLE_NAMES:
      return { ...state, vehicleNames: action.receivedVehicleNames };
    case actionTypes.RECEIVED_TIME_STATS:
      return { ...state, timeStatsData: action.receivedTimeStats };
    case actionTypes.RECEIVED_PARTICIPANT_VEHICLE_NAMES_DATA:
      return { ...state, participantVehicleNamesData: action.receivedParticipantVehicleNamesData };
    case actionTypes.RECEIVED_VEHICLE_CLASS_NAMES_DATA:
      return { ...state, vehicleClassNamesData: action.receivedVehicleClassNamesData };
    case actionTypes.RECEIVED_LAP_TIME_DETAILS:
      return { ...state, lapTimeDetails: action.receivedLapTimeDetails };
    case actionTypes.RECEIVED_AGGREGATE_TIME:
      return { ...state, aggregateTime: action.receivedAggregateTime };
    case actionTypes.RECEIVED_FUEL_DATA:
      return { ...state, fuelData: action.receivedFuelData };
    case actionTypes.RECEIVED_TELEMETRY_SUMMARY:
      return { ...state, telemetrySummary: action.receivedTelemetrySummary };
    default:
      return state;
  }
}

const appReducer = combineReducers({
  connectionInfo,
  isWebSocketOpened,
  options,
  buttonBox,
  currentContent,
  isMenuVisible,
  currentUdpData
});

export default appReducer;
