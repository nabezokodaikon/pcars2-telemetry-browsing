import React from "react";
import { render } from "react-dom";
import { Provider } from "react-redux";
import { createStore, applyMiddleware } from "redux";
import thunkMiddleware from 'redux-thunk'
import appReducer from "./appReducer.js"
import CarStateDataContainer from "./main/carStateData/CarStateDataContainer.jsx";

const composeEnhancers =
  typeof window === 'object' && window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
    ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({}) : compose;

const enhancer = composeEnhancers(
  applyMiddleware(thunkMiddleware)
);

const store = createStore(appReducer, enhancer);

// For mobile.
// const store = createStore(appReducer, applyMiddleware(thunkMiddleware));

render(
  <Provider store={store}>
    <div>
      <CarStateDataContainer />
    </div>
  </Provider>,
  document.getElementById("root")
);
