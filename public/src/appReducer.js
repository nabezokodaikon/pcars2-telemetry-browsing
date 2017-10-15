import { combineReducers } from "redux";
import * as actionTypes from "./appActionTypes.js";

const initialState = {
  testCount: 0,
  telemetry: {}
}

function testCount(state = 0, action) {
  switch (action.type) {
    case actionTypes.TEST_COUNTER:
      return state + action.addValue;
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
  testCount,
  telemetry
});

export default appReducer;
