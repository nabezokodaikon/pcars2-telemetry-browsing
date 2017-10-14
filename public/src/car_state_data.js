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
function receivedData(telemetry) {
  return { type: RECEIVED_DATA, telemetry };
}

const initialState = {
  telemetry: {}
}

function telemetry(state = {}, action) {
  switch (action.type) {
    case RECEIVED_DATA:
      return action.telemetry;
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
