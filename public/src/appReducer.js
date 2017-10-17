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

function telemetry(state = {}, action) {
  switch (action.type) {
    case actionTypes.RECEIVED_DATA:
      return action.nextTelemetry;
    default:
      return state;
  }
}

const appReducer = combineReducers({
  currentContent,
  isMenuToVisible,
  telemetry
});

export default appReducer;
