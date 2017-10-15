import * as actionTypes from "./appActionTypes.js";

export function receivedData(telemetry) {
  return { type: actionTypes.RECEIVED_DATA, telemetry };
}

export function open() {
  return { type: actionTypes.OPEN };
}

export function testCounter(addValue) {
    return {
      type: actionTypes.TEST_COUNTER,
      addValue
    };
};
