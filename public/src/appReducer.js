import { combineReducers } from "redux";
import * as actionTypes from "./appActionTypes.js";
import { CAR_STATE_DATA } from "./common/contentNames";

function currentContent(state = CAR_STATE_DATA, action) {
  switch (action.type) {
      case actionTypes.SELECTED_CONTENT:
        return action.selectedContent;
      default:
        return state;
  }
}

function isMenuToVisible(state = false, action) {
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

const appReducer = combineReducers({
  currentContent,
  isMenuToVisible,
  participantInfoStrings,
  participantInfoStringsAdditional,
  telemetryData
});

export default appReducer;
