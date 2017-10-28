import { combineReducers } from "redux";
import * as actionTypes from "./appActionTypes.js";
import { TIME } from "./common/contentNames";

function currentContent(state = TIME, action) {
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

function participantInfoStrings(state = {}, action) {
  switch (action.type) {
    case actionTypes.RECEIVED_PARTICIPANT_INFO_STRINGS:
      return action.nextParticipantInfoStrings;
    default:
      return state;
  }
}

function participantInfoStringsAdditional(state = {}, action) {
  switch (action.type) {
    case actionTypes.RECEIVED_PARTICIPANT_INFO_STRINGS_ADDITIONAL:
      return action.nextParticipantInfoStringsAdditional;
    default:
      return state;
  }
}

function telemetryData(state = {}, action) {
  switch (action.type) {
    case actionTypes.RECEIVED_TELEMETRY_DATA:
      return action.nextTelemetryData;
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

const appReducer = combineReducers({
  connectionInfo,
  isWebSocketOpened,
  options,
  currentContent,
  isMenuVisible,
  participantInfoStrings,
  participantInfoStringsAdditional,
  telemetryData
});

export default appReducer;
