import { combineReducers } from "redux";
import { receivedData, RECEIVED_DATA } from "./appAction.js"

const initialState = {
  telemetry: {}
}

function telemetry(state = {}, action) {
  console.log("appReducer");
  switch (action.type) {
    case RECEIVED_DATA:
      return action.telemetry;
    default:
      return state;
  }
}

const appReducer = combineReducers({
  telemetry
});

export default appReducer;
