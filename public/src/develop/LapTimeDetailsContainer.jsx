import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../share/jsUtil.js";

class LapTimeDetails extends React.Component {
  constructor(props) {
    super(props)
  }

  createRecords() {
    const createBase = () => {
      const data = this.props.lapTimeDetails.base;
      return Object.keys(data).map(key => {
        const value = data[key];
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      });
    }

    const createHeader = () => {
      return (
        <tr key="header">
          <td>Type</td>
          <td>Lap</td>
          <td>Sector1</td>
          <td>Sector2</td>
          <td>Sector3</td>
          <td>LapTime</td>
          <td>Delta</td>
        </tr>
      );
    };

    const createRecord = (data, type) => {
      return (
        <tr key={type}>
          <td key="1">{type}</td>
          <td key="2">{data.lap}</td>
          <td key="3">{data.sector1}</td>
          <td key="4">{data.sector2}</td>
          <td key="5">{data.sector3}</td>
          <td key="6">{data.lapTime}</td>
          <td key="7">{data.delta}</td>
        </tr>
      );
    };

    const history = () => {
      return this.props.lapTimeDetails.history.map((v, i) => createRecord(v, "history[" + i + "]"));
    };

    return (
      <table>
        <tbody>
          <tr>
            <td>PacketBase</td>
          </tr>
          {createBase()}
          <tr>
            <td>RaceData</td>
          </tr>
          <tr>
            <td>isTimedSessions</td>
            <td>{this.props.lapTimeDetails.isTimedSessions.toString()}</td>
          </tr>
          <tr>
            <td>lapsInEvent</td>
            <td>{this.props.lapTimeDetails.lapsInEvent}</td>
          </tr>
          <tr>
            <td>totalTime</td>
            <td>this.props.aggregateTime.totalTime</td>
          </tr>
          <tr>
            <td>gapTime</td>
            <td>this.props.aggregateTime.gapTime</td>
          </tr>
          <tr>
            <td>lastConsumption</td>
            <td>{this.props.fuelData.lastConsumption}</td>
          </tr>
          <tr>
            <td>averageConsumption</td>
            <td>{this.props.fuelData.averageConsumption}</td>
          </tr>
          <tr>
            <td>LapTimeDetails</td>
          </tr>
          {createHeader()}
          {createRecord(this.props.lapTimeDetails.current, "current lap")}
          {createRecord(this.props.lapTimeDetails.fastest, "fastest lap")}
          {createRecord(this.props.lapTimeDetails.average, "average")}
          {history()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.lapTimeDetails)
    /* || !isJson(this.props.aggregateTime) */
    || !isJson(this.props.fuelData)) {
      return <div></div>;
    } else {
      return (
        <div>
          {this.createRecords()}
        </div>
      );
    }
  }
}

LapTimeDetails.propTypes = {
  lapTimeDetails: PropTypes.object.isRequired,
  aggregateTime: PropTypes.object.isRequired,
  fuelData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    lapTimeDetails: state.currentUdpData.lapTimeDetails,
    aggregateTime: state.currentUdpData.aggregateTime,
    fuelData: state.currentUdpData.fuelData
  };
};

const LapTimeDetailsContainer = connect(
  mapStateToProps
)(LapTimeDetails);

export default LapTimeDetailsContainer;
