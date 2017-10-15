import React from "react";
import { render } from "react-dom";
import { Provider } from "react-redux";
import { createStore, applyMiddleware } from "redux";
import thunk from "redux-thunk";
import appReducer from "./appReducer.js"
import CarStateDataContainer from "./main/carStateData/CarStateDataContainer.jsx";

import { testCounter } from "./appActionCreators.js";

const store = createStore(
  appReducer,
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
);

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

store.dispatch(testCounter(3));

render(
  <Provider store={store}>
    <div>
      <CarStateDataContainer />
    </div>
  </Provider>,
  document.getElementById("root")
);
