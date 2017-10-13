import { 
  combineReducers,
  createStore
} from "redux";

/*
 * action types
 */
const RECEIVED_DATA = "RECEIVED_DATA";


/*
 * action creators
 */
function receivedData(data) {
  return { type: RECEIVED_DATA, telemetry };
}

const initialState = {
  telemetry: {
    oilTempCelsius: 0,
    oilPressureKPa: 0,
    waterTempCelsius: 0,
    waterPressureKpa: 0,
    fuelPressureKpa: 0,
    carFlags: 0,
    fuelCapacity: 0,
    brake: 0.0,
    throttle: 0.0,
    clutch: 0.0,
    steering: 0.0,
    fuelLevel: 0.0,
    speed: 0.0,
    rpm: 0,
    maxRpm: 0,
    gear: 0,
    numGears: 0,
    boostAmount: 0,
    enforcedPitStopLap: 0,
    odometerKM: -1.0,
    antiLockActive: false,
    boostActive: false
  }
}

function telemetry(state = {}, action) {
  switch (action.type) {
    case RECEIVED_DATA:
      return {
        oilTempCelsius: action.telemetry.carStateData.oilTempCelsius,
        oilPressureKPa: action.telemetry.carStateData.oilPressureKPa,
        waterTempCelsius: action.telemetry.carStateData.waterTempCelsius,
        waterPressureKpa: action.telemetry.carStateData.waterPressureKpa,
        fuelPressureKpa: action.telemetry.carStateData.fuelPressureKpa,
        carFlags: action.telemetry.carStateData.carFlags,
        fuelCapacity: action.telemetry.carStateData.fuelCapacity,
        brake: action.telemetry.carStateData.brake,
        throttle: action.telemetry.carStateData.throttle,
        clutch: action.telemetry.carStateData.clutch,
        steering: action.telemetry.carStateData.steering,
        fuelLevel: action.telemetry.carStateData.fuelLevel,
        speed: action.telemetry.carStateData.speed,
        rpm: action.telemetry.carStateData.rpm,
        maxRpm: action.telemetry.carStateData.maxRpm,
        gear: action.telemetry.carStateData.gear,
        numGears: action.telemetry.carStateData.numGears,
        boostAmount: action.telemetry.carStateData.boostAmount,
        enforcedPitStopLap: action.telemetry.carStateData.enforcedPitStopLap,
        odometerKM: action.telemetry.carStateData.odometerKM,
        antiLockActive: action.telemetry.carStateData.antiLockActive,
        boostActive: action.telemetry.carStateData.boostActive
      };
    default:
      return state;
  }
}

const carStateDataReducer = combineReducers({
  telemetry
});

const store = createStore(
  carStateDataReducer,
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
);

console.log(store.getState());

const ws = new WebSocket("ws://192.168.1.18:9000/pcars1");
ws.onopen = e => {
  console.log("Websocket was opened.");
};

ws.onclose = e => {
  console.log("Websocket was closed.");
}

ws.onerror = e => {
  console.log("Error occurred in Websocket.");
}

ws.onmessage = e => {
  const json = JSON.parse(e.data)
  store.dispatch(receivedData(json));
}
