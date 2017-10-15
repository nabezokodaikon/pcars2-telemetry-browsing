import { combineReducers } from "redux";
import * as actionTypes from "./appActionTypes.js";

const initialState = {
  testCount: 0,
  telemetry: {}
}

function testCount(state = 0, action) {
  console.log("testCounterReducer");
  switch (action.type) {
    case actionTypes.TEST_COUNTER:
      return state + action.addValue;
    default:
      return state;
  }
}

function telemetry(state = {}, action) {
  console.log("telemetryReducer");
  switch (action.type) {
    case actionTypes.RECEIVED_DATA:
      return action.telemetry;
    default:
      return state;
  }
}

const appReducer = combineReducers({
  testCount,
  telemetry
});

export default appReducer;
