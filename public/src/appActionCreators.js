import * as actionTypes from "./appActionTypes.js";

export function receivedData(nextTelemetry) {
  return {
    type: actionTypes.RECEIVED_DATA, 
    nextTelemetry 
  };
}

export function open() {
  return { type: actionTypes.OPEN_WEBSOCKET };
}

export function currentContent(selectedContent) {
  return {
    type: actionTypes.SELECTED_CONTENT,
    selectedContent
  }
}

export function toggleMenu() {
  return {
    type: actionTypes.TOGGLE_MENU
  }
}

export function testCounter(addValue) {
  return {
    type: actionTypes.TEST_COUNTER,
    addValue
  };
};

function startWebSocketConnection() {
  return new Promise((resolve, reject) => {
    try {
      const ws = new WebSocket("ws://192.168.1.18:9000/pcars1");
      return resolve(ws);
    } catch(error) {
      return reject(error);
    }
  });
}

export function openWebSocket() {
  return dispatch => {
    return startWebSocketConnection()
      .then(ws => {
        ws.onopen = e => {
          console.log("WebSocket was opened.");
        };

        ws.onclose = e => {
          console.log("WebSocket was closed.");
        };

        ws.onerror = e => {
          console.log("WebSocket was error occurred.");
        }

        ws.onmessage = e => {
          const json = JSON.parse(e.data)
          dispatch(receivedData(json));
        };
      })
      // TODO .catch(error => dispatch(errorOpeningWebSocket(error.message)))
  }
} 

