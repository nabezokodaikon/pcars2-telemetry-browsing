import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../share/jsUtil.js";

const Fragment = React.Fragment;

class TelemetryData extends React.Component {
  constructor(props) {
    super(props);
  }

  createRecords() {
    const createBase = () => {
      const data = this.props.telemetryData.base;
      return Object.keys(data).map(key => {
        const value = data[key];
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      });
    };

    const createTelemetryParticipantInfo = () => {
      const data = this.props.telemetryData.participantInfo;
      return Object.keys(data).map(key => {
        const value = data[key];
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      });
    };

    const createUnfilteredInput = () => {
      const data = this.props.telemetryData.unfilteredInput;
      return Object.keys(data).map(key => {
        const value = data[key];
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      });
    };

    const createCarState = () => {
      const data = this.props.telemetryData.carState;
      return Object.keys(data).map(key => {
        const value = data[key];
        if (isJson(value)) {
          const flags = Object.keys(value).map(name => {
            return (
              <tr key={key + name}>
                <td>{key + "." + name}</td>
                <td>{value[name].toString()}</td>
              </tr>
            );
          });
          return <Fragment key={key}>{flags}</Fragment>;
        } else {
          return (
            <tr key={key}>
              <td>{key}</td>
              <td>{value}</td>
            </tr>
          );
        }
      });
    };

    const createCarDamage = () => {
      const data = this.props.telemetryData.carDamage;
      return Object.keys(data).map(key => {
        const value = data[key];
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      });
    };

    const createHWState = () => {
      const data = this.props.telemetryData.hwState;
      return Object.keys(data).map(valueName => {
        const value = data[valueName];
        if (isArray(value)) {
          return value.map((childValue, index) => {
            const childValueName = valueName + "[" + index + "]";
            return (
              <tr key={childValueName}>
                <td>{childValueName}</td>
                <td>{childValue}</td>
              </tr>
            );
          });
        } else {
          return (
            <tr key={valueName}>
              <td>{valueName}</td>
              <td>{value}</td>
            </tr>
          );
        }
      });
    };

    return (
      <table>
        <tbody>
          <tr>
            <td>PacketBase</td>
          </tr>
          {createBase()}
          <tr>
            <td>ParticipantInfo</td>
          </tr>
          {createTelemetryParticipantInfo()}
          <tr>
            <td>UnfilteredInput</td>
          </tr>
          {createUnfilteredInput()}
          <tr>
            <td>CarState</td>
          </tr>
          {createCarState()}
          <tr>
            <td>CarDamage</td>
          </tr>
          {createCarDamage()}
          <tr>
            <td>HWState</td>
          </tr>
          {createHWState()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.telemetryData)) {
      return <div />;
    } else {
      return <div>{this.createRecords()}</div>;
    }
  }
}

TelemetryData.propTypes = {
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.currentUdpData.telemetryData
  };
};

const TelemetryDataContainer = connect(mapStateToProps)(TelemetryData);

export default TelemetryDataContainer;
