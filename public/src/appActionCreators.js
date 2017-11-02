import {
  isCarPhysics,
  isRaceDefinition,
  isParticipants,
  isTimings,
  isGameState,
  isWeatherState,
  isVehicleNames,
  isTimeStats,
  isParticipantVehicleNamesData,
  isVehicleClassNamesData
} from "./common/telemetryUtil.js";
import * as actionTypes from "./appActionTypes.js";

function fetchPostByJson(json, url) {
  return fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(json)
  });
}

function fetchGet(url) {
  return fetch(url, {
    method: "GET",
  });
}

export function receivedCarPhysics(receivedCarPhysics) {
  return {
    type: actionTypes.RECEIVED_CAR_PHYSICS,
    receivedCarPhysics
  }
}

export function receivedRaceDefinition(receivedRaceDefinition) {
  return {
    type: actionTypes.RECEIVED_RACE_DEFINITION,
    receivedRaceDefinition
  }
}

export function receivedParticipants(receivedParticipants) {
  return {
    type: actionTypes.RECEIVED_PARTICIPANTS,
    receivedParticipants
  }
}

export function receivedTimings(receivedTimings) {
  return {
    type: actionTypes.RECEIVED_TIMINGS,
    receivedTimings
  }
}

export function receivedGameState(receivedGameState) {
  return {
    type: actionTypes.RECEIVED_GAME_STATE,
    receivedGameState
  }
}

export function receivedWeatherState(receivedWeatherState) {
  return {
    type: actionTypes.RECEIVED_WEATHER_STATE,
    receivedWeatherState
  }
}

export function receivedVehicleNames(receivedVehicleNames) {
  return {
    type: actionTypes.RECEIVED_VEHICLE_NAMES,
    receivedVehicleNames
  }
}

export function receivedTimeStats(receivedTimeStats) {
  return {
    type: actionTypes.RECEIVED_TIME_STATS,
    receivedTimeStats
  }
}

export function receivedParticipantVehicleNamesData(receivedParticipantVehicleNamesData) {
  return {
    type: actionTypes.RECEIVED_PARTICIPANT_VEHICLE_NAMES_DATA,
    receivedParticipantVehicleNamesData
  }
}

export function receivedVehicleClassNamesData(receivedVehicleClassNamesData) {
  return {
    type: actionTypes.RECEIVED_VEHICLE_CLASS_NAMES_DATA,
    receivedVehicleClassNamesData
  }
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

function gotAllOptions(options) {
  return {
    type: actionTypes.GOT_ALL_OPTIONS,
    options: {
      isCelsius: options.isCelsius.value,
      isMeter: options.isMeter.value,
      isBar: options.isBar.value
    }
  };
}

export function requestAllOptions() {
  return dispatch => {
    fetchGet("option/all")
      .then(res => res.json())
      .then(json => dispatch(gotAllOptions(json)))
      .catch(error => {
        console.log(error.message);
      });
  };
}

function gotConnectionInfo(connectionInfo) {
  return {
    type: actionTypes.GOT_CONNECTION_INFO,
    connectionInfo: {
      ipAddress: connectionInfo.ipAddress,
      port: connectionInfo.port,
      isGot: true
    }
  };
}

export function requestConnectionInfo() {
  return dispatch => {
    fetchGet("config/connection-info")
      .then(res => res.json())
      .then(json => {
        dispatch(gotConnectionInfo(json));
        dispatch(connectWebSocket(json));
      })
      .catch(error => {
        console.log(error.message);
      });
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
    fetchPostByJson(json, "option/unit")
      .then(res => res.json())
      .then(json => dispatch(changedTempUnit(json.value)))
      .catch(error => {
        console.log(error.message);
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
    fetchPostByJson(json, "option/unit")
      .then(res => res.json())
      .then(json => dispatch(changedDistanceUnit(json.value)))
      .catch(error => {
        console.log(error.message);
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
    fetchPostByJson(json, "option/unit")
      .then(res => res.json())
      .then(json => dispatch(changedAirPressureUnit(json.value)))
      .catch(error => {
        console.log(error.message);
      });
  }
}

function openWebSocket(isWebSocketOpened) {
  return {
    type: actionTypes.OPEN_WEBSOCKET,
    isWebSocketOpened
  }
}

async function beginConnectWebSocket(connectionInfo) {
  const url = "ws://" + connectionInfo.ipAddress + ":" + connectionInfo.port + "/pcars2";
  const ws = new WebSocket(url);
  return ws;
}

export function connectWebSocket(connectionInfo) {
  return dispatch => {
    return beginConnectWebSocket(connectionInfo)
      .then(ws => {
        ws.onopen = e => {
          console.log("WebSocket was opened.");
          dispatch(openWebSocket(true));
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

          dispatch(openWebSocket(false));
        };

        ws.onerror = e => {
          console.log("WebSocket was error occurred.");
          dispatch(openWebSocket(false));
        }

        ws.onmessage = e => {
          try {
            const json = JSON.parse(e.data)
            if (isCarPhysics(json)) {
              dispatch(receivedCarPhysics(json));
            } else if (isRaceDefinition(json)) {
              dispatch(receivedRaceDefinition(json));
            } else if (isParticipants(json)) {
              dispatch(receivedParticipants(json));
            } else if (isTimings(json)) {
              dispatch(receivedTimings(json));
            } else if (isGameState(json)) {
              dispatch(receivedGameState(json));
            } else if (isWeatherState(json)) {
              dispatch(receivedWeatherState(json));
            } else if (isVehicleNames(json)) {
              dispatch(receivedVehicleNames(json));
            } else if (isTimeStats(json)) {
              dispatch(receivedTimeStats(json));
            } else if (isParticipantVehicleNamesData(json)) {
              dispatch(receivedParticipantVehicleNamesData(json));
            } else if (isVehicleClassNamesData(json)) {
              dispatch(receivedVehicleClassNamesData(json));
            } else {
              console.log("WebSocket received unknown packet type data.");
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
      .catch(error => {
        console.log(error.message);
        dispatch(openWebSocket(false));
      })
  }
} 
