import React from "react";
import { render } from "react-dom";
import { Provider } from "react-redux";
import { createStore, applyMiddleware } from "redux";
import thunkMiddleware from 'redux-thunk'
import appReducer from "./appReducer.js"
import {
  connectWebSocket,
  requestAllOptions,
  requestConnectionInfo
} from "./appActionCreators.js"
import DebugMenuContainer from "./menu/DebugMenuContainer.jsx";
import ContentsContainer from "./debug/ContentsContainer.jsx";

const composeEnhancers =
  typeof window === 'object' && window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({}) : compose;

const enhancer = composeEnhancers(
  applyMiddleware(thunkMiddleware)
);

const store = createStore(appReducer, enhancer);

// For mobile.
// const store = createStore(appReducer, applyMiddleware(thunkMiddleware));

setInterval(() => {
  const state = store.getState();
  if (state.connectionInfo.isGot && !state.isWebSocketOpened) {
    console.log("Begin reconnect WebSocket.");
    store.dispatch(connectWebSocket(state.connectionInfo));
  }
}, 5000);

store.dispatch(requestAllOptions());
store.dispatch(requestConnectionInfo());

render(
  <Provider store={store}>
    <div>
      <DebugMenuContainer />
      <ContentsContainer /> 
    </div>
  </Provider>,
  document.getElementById("root")
);
