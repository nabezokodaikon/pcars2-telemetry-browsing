import React from "react";
import { render } from "react-dom";
import { Provider } from "react-redux";
import { createStore, applyMiddleware } from "redux";
import thunkMiddleware from 'redux-thunk'
import appReducer from "./appReducer.js"
import {
  connectWebSocket,
  requestAllOptions,
  requestConnectionInfo,
  currentContent
} from "./appActionCreators.js"
import DevelopMenuContainer from "./menu/DevelopMenuContainer.jsx";
import DevelopContentsContainer from "./develop/DevelopContentsContainer.jsx";
import * as contentNames from "./common/contentNames.js";

const composeEnhancers =
  typeof window === 'object' && window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({}) : compose;

const enhancer = composeEnhancers(
  applyMiddleware(thunkMiddleware)
);

const store = createStore(appReducer, enhancer);

setInterval(() => {
  const state = store.getState();
  if (state.connectionInfo.isGot && !state.isWebSocketOpened) {
    console.log("Begin reconnect WebSocket.");
    store.dispatch(connectWebSocket(state.connectionInfo));
  }
}, 5000);

store.dispatch(requestAllOptions());
store.dispatch(requestConnectionInfo());
store.dispatch(currentContent(contentNames.TELEMETRY_DATA));

render(
  <Provider store={store}>
    <div>
      <DevelopMenuContainer />
      <DevelopContentsContainer /> 
    </div>
  </Provider>,
  document.getElementById("root")
);
