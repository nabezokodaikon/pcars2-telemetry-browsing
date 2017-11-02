import { combineReducers } from "redux";
import * as actionTypes from "./appActionTypes.js";
import * as contentNames from "./common/contentNames";

function currentContent(state = contentNames.TYRE, action) {
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
      return Object.assign({}, state, {
        isCelsius: action.isCelsius
      });
    case actionTypes.CHANGED_DISTANCE_UNIT:
      return Object.assign({}, state, {
        isMeter: action.isMeter
      });
    case actionTypes.CHANGED_AIR_PRESSURE_UNIT:
      return Object.assign({}, state, {
        isBar: action.isBar
      });
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

const initialUdpData = {
  current: {
    carPhysics: {},
    raceDefinition: {},
    participants: {},
    timings: {},
    gameState: {},
    weatherState: {},
    vehicleNames: {},
    participantVehicleNamesData: {},
    vehicleClassNamesData: {}
  }
}

function udpData(state = initialUdpData, action) {
  switch (action.type) {
    case actionTypes.RECEIVED_CAR_PHYSICS:
      return Object.assign({}, state.current, {
        carPhysics: action.nextCarPhysics
      });
    case actionTypes.RECEIVED_RACE_DEFINITION:
      return Object.assign({}, state.current, {
        raceDefinition: action.nextRaceDefinition
      });
    case actionTypes.RECEIVED_PARTICIPANTS:
      return Object.assign({}, state.current, {
        participants: action.nextParticipants
      });
    case actionTypes.RECEIVED_TIMINGS:
      return Object.assign({}, state.current, {
        timings: action.nextTimings
      });
    case actionTypes.RECEIVED_GAME_STATE:
      return Object.assign({}, state.current, {
        gameState: action.nextGameState
      });
    case actionTypes.RECEIVED_WEATHER_STATE:
      return Object.assign({}, state.current, {
        weatherState: action.nextWeatherState
      });
    case actionTypes.RECEIVED_VEHICLE_NAMES:
      return Object.assign({}, state.current, {
        vehicleNames: action.nextVehicleNames
      });
    case actionTypes.RECEIVED_PARTICIPANT_VEHICLE_NAMES_DATA:
      return Object.assign({}, state.current, {
        participantVehicleNamesData: action.nextParticipantVehicleNamesData
      });
    case actionTypes.RECEIVED_VEHICLE_CLASS_NAMES_DATA:
      return Object.assign({}, state.current, {
        vehicleClassNamesData: action.nextVehicleClassNamesData
      });
    default:
      return state;
  }
}

const appReducer = combineReducers({
  connectionInfo,
  isWebSocketOpened,
  options,
  currentContent,
  isMenuVisible,
  udpData
});

export default appReducer;
