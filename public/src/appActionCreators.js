import {
  isTelemetryDataFrameType,
  isParticipantInfoStringsFrameType,
  isParticipantInfoStringsAdditionalFrameType
} from "./common/telemetryUtil.js";
import * as actionTypes from "./appActionTypes.js";

function xmlHttpRequestByJSON(json, url) {
  return new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest();
      xhr.open("POST", url);
      xhr.setRequestHeader("Content-type", "application/json;charset=UTF-8");
      xhr.responseType = "json";

      xhr.onload = evt => {
        resolve(xhr.response)
      };

      xhr.onerror = () => {
        reject("Failed temp unit change request.")
      }

      xhr.send(JSON.stringify(json));
  });
}

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

function currentContent(selectedContent) {
  return {
    type: actionTypes.SELECTED_CONTENT,
    selectedContent
  };
}

export function requestCurrentContentChange(selectedContent) {
  return dispatch => {
    const json = {
      key: "state/currentContent",
      value: selectedContent
    };
    xmlHttpRequestByJSON(json, "state/current-content")
      .then(res => {
        dispatch(currentContent(res.value));
      })
      .catch(errMsg => {
        console.log(errMsg);
      });
  }
}

export function toggleMenu() {
  return {
    type: actionTypes.TOGGLE_MENU
  };
}

function changedTempUnit(isCelsius) {
  return {
    type: actionTypes.CHANGED_TEMP_UNIT,
    isCelsius
  };
}

export function requestTempUnitChange(isCelsius) {
  return dispatch => {
    const json = {
      key: "option/isCelsius",
      value: isCelsius
    };
    xmlHttpRequestByJSON(json, "option/unit")
      .then(res => {
        dispatch(changedTempUnit(res.value));
      })
      .catch(errMsg => {
        console.log(errMsg);
      });
  }
}  

function changedDistanceUnit(isMeter) {
  return {
    type: actionTypes.CHANGED_DISTANCE_UNIT,
    isMeter
  };
}

export function requestDistanceUnitChange(isMeter) {
  return dispatch => {
    const json = {
      key: "option/isMeter",
      value: isMeter
    };
    xmlHttpRequestByJSON(json, "option/unit")
      .then(res => {
        dispatch(changedDistanceUnit(res.value));
      })
      .catch(errMsg => {
        console.log(errMsg);
      });
  }
}

function changedAirPressureUnit(isBar) {
  return {
    type: actionTypes.CHANGED_AIR_PRESSURE_UNIT,
    isBar
  };
}

export function requestAirPressureUnitChange(isBar) {
  return dispatch => {
    const json = {
      key: "option/isBar",
      value: isBar
    };
    xmlHttpRequestByJSON(json, "option/unit")
      .then(res => {
        dispatch(changedAirPressureUnit(res.value));
      })
      .catch(errMsg => {
        console.log(errMsg);
      });
  }
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
          // See http://tools.ietf.org/html/rfc6455#section-7.4.1
          if (event.code == 1000) {
            console.log("WebSocket was closed. Status code: " + e.code + ": A normal closure, meaning that the purpose for which the connection was established has been fulfilled.");
          }
          else if(event.code == 1001) {
            console.log("WebSocket was closed. Status code: " + e.code + ": An endpoint is 'going away', such as a server going down or a browser having navigated away from a page.");
          }
          else if(event.code == 1002) {
            console.log("WebSocket was closed. Status code: " + e.code + ": An endpoint is terminating the connection due to a protocol error.");
          }
          else if(event.code == 1003) {
            console.log("WebSocket was closed. Status code: " + e.code + ": An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).");
          }
          else if(event.code == 1004) {
            console.log("WebSocket was closed. Status code: " + e.code + ": Reserved. The specific meaning might be defined in the future.");
          }
          else if(event.code == 1005) {
            console.log("WebSocket was closed. Status code: " + e.code + ": A reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint.  It is designated for use in applications expecting a status code to indicate that no status code was actually present.");
          }
          else if(event.code == 1006) {
            console.log("WebSocket was closed. Status code: " + e.code + ": A reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint.  It is designated for use in applications expecting a status code to indicate that the connection was closed abnormally, e.g., without sending or receiving a Close control frame.");
          }
          else if(event.code == 1007) {
            console.log("WebSocket was closed. Status code: " + e.code + ": An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [RFC3629] data within a text message).");
          }
          else if(event.code == 1008) {
            console.log("WebSocket was closed. Status code: " + e.code + ": An endpoint is terminating the connection because it has received a message that violates its policy.  This is a generic status code that can be returned when there is no other more suitable status code (e.g., 1003 or 1009) or if there is a need to hide specific details about the policy.");
          }
          else if(event.code == 1009) {
            console.log("WebSocket was closed. Status code: " + e.code + ": An endpoint is terminating the connection because it has received a message that is too big for it to process.");
          }
          else if(event.code == 1010) {
            console.log("WebSocket was closed. Status code: " + e.code + ": An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake.  The list of extensions that are needed SHOULD appear in the /reason/ part of the Close frame. Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.");
          }
          else if(event.code == 1011) {
            console.log("WebSocket was closed. Status code: " + e.code + ": A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.");
          }
          else if(event.code == 1015) {
            console.log("WebSocket was closed. Status code: " + e.code + ": A reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint.  It is designated for use in applications expecting a status code to indicate that the connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).");
          } else {
            console.log("WebSocket was closed. Received unknown status code: " + e.code);
          }

          dispatch(openWebSocket());
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

