import React from "react";
import ReactDom from "react-dom";
import PropTypes from "prop-types";

export default class CarStateData extends React.Component {
  constructor(props) {
    super(props)
  }

  componentDidMount() {
    console.log("componentDidMount");
    // this.props.onOpen(this.props.telemetry);


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
      // // store.dispatch(receivedData(json));
      // store.dispatch(fetchReceived(json));
    // }

    // this.props.onOpen(ws);


  }

  renderSpeed() {
    if (Object.keys(this.props.telemetry).length > 0 && this.props.telemetry.frameType == 0) {
      return <p>this.props.telemetry.carStateData.speed</p>;
    } else {
      return <p>--:--:--.---</p>;
    }
  }

  render() {
    console.log("render");
    return this.renderSpeed();
  }
}

CarStateData.propTypes = {
  telemetry: PropTypes.object.isRequired
};
