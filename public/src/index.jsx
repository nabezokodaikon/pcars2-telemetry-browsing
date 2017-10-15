import React from "react";
import { render } from "react-dom";
import { Provider } from "react-redux";
import { createStore, applyMiddleware } from "redux";
import thunk from "redux-thunk";
import { open, receivedData, fetchReceived } from "./appAction.js"
import appReducer from "./appReducer.js"
import CarStateDataContainer from "./main/carStateData/CarStateDataContainer.js";

const websocketMiddleware = (() => {
  const onMessage = (ws, store) => e => {
    const json = JSON.parse(e.data)
    store.dispatch(receivedData(json));
  }

  return store => next => action => {
    switch (action.type) {
      case "OPEN":
        const ws = new WebSocket("ws://192.168.1.18:9000/pcars1");
        ws.onmessage = onMessage(ws, store);
        break;
      // case "RECEIVED_DATA":
        // store.dispatch(receivedData(action.telemetry));
        // break;
      default:
        return next(action);
    }
  }
})();

const store = createStore(
  appReducer,
  {  
    telemetry: {}
  },
  applyMiddleware(
    thunk,
    websocketMiddleware
  )
);

store.dispatch(open());

// const store = createStore(
  // appReducer,
  // window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
// );

// const ws = new WebSocket("ws://192.168.1.18:9000/pcars1");
// ws.onopen = e => {
  // console.log("Websocket was opened.");
// };

// ws.onclose = e => {
  // console.log("Websocket was closed.");
// }

// ws.onerror = e => {
  // console.log("Error occurred in Websocket.");
// }

// ws.onmessage = e => {
  // const json = JSON.parse(e.data)
  // store.dispatch(receivedData(json));
// }

render(
  <Provider store={store}>
    <div>
      <CarStateDataContainer />
    </div>
  </Provider>,
  document.getElementById("root")
);
