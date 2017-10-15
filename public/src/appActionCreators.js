/*
 * action types
 */
const OPEN = "OPEN";
const RECEIVED_DATA = "RECEIVED_DATA";


/*
 * action creators
 */
export function receivedData(telemetry) {
  return { type: RECEIVED_DATA, telemetry };
}

export function open() {
  return { type: OPEN };
}

// function receivedCallback(ws) {
  // return dispatch => {
    // return ws.onmessage = e => {
      // const json = JSON.parse(e.data)
      // dispatch(receivedData(json));
    // }
  // }
// }

// export function fetchReceived(ws) {
  // return (dispatch, getState) => {
    // return dispatch(receivedCallback(ws));
  // }
// }
