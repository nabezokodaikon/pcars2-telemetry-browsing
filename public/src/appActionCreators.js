import {
  isTelemetryDataFrameType,
  isParticipantInfoStringsFrameType,
  isParticipantInfoStringsAdditionalFrameType
} from "./common/telemetryUtil.js";
import * as actionTypes from "./appActionTypes.js";

export function receivedParticipantInfoStrings(nextParticipantInfoStrings) {
  return {
    type: actionTypes.RECEIVED_PARTICIPANT_INFO_STRINGS, 
    nextParticipantInfoStrings 
  };
}

export function receivedParticipantInfoStringsAdditional(nextParticipantInfoStringsAdditional) {
  return {
    type: actionTypes.RECEIVED_PARTICIPANT_INFO_STRINGS_ADDITIONAL, 
    nextParticipantInfoStringsAdditional 
  };
}

export function receivedTelemetryData(nextTelemetryData) {
  return {
    type: actionTypes.RECEIVED_TELEMETRY_DATA, 
    nextTelemetryData 
  };
}

export function currentContent(selectedContent) {
  return {
    type: actionTypes.SELECTED_CONTENT,
    selectedContent
  };
}

export function toggleMenu() {
  return {
    type: actionTypes.TOGGLE_MENU
  };
}

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
          try {
            const json = JSON.parse(e.data)
            if (isTelemetryDataFrameType(json)) {
              dispatch(receivedTelemetryData(json));
            } else if (isParticipantInfoStringsFrameType) {
              dispatch(receivedParticipantInfoStrings(json));
            } else if (isParticipantInfoStringsAdditionalFrameType) {
              dispatch(receivedParticipantInfoStringsAdditional(json));
            } else {
              console.log("WebSocket received unknown frame type data.");
            }
          } catch (e) {
            if (e instanceof SyntaxError) {
              console.log("WebSocket received json parse failed.");
            } else {
              console.log(e);
            }
          }
        };
      })
      // TODO .catch(error => dispatch(errorOpeningWebSocket(error.message)))
  }
} 

