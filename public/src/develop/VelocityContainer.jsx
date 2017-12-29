import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../share/jsUtil.js";

class Velocity extends React.Component {
  constructor(props) {
    super(props);
  }

  createRecords() {
    const createVelocity = () => {
      const data = this.props.telemetryData.velocity;
      return Object.keys(data).map(valueName => {
        const value = data[valueName];
        return value.map((childValue, index) => {
          const childValueName = valueName + "[" + index + "]";
          return (
            <tr key={childValueName}>
              <td>{childValueName}</td>
              <td>{childValue}</td>
            </tr>
          );
        });
      });
    };

    return (
      <table>
        <tbody>
          <tr>
            <td>TelemetryData.velocity</td>
          </tr>
          {createVelocity()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.telemetryData.velocity)) {
      return <div />;
    } else {
      return <div>{this.createRecords()}</div>;
    }
  }
}

Velocity.propTypes = {
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.currentUdpData.telemetryData
  };
};

const VelocityContainer = connect(mapStateToProps)(Velocity);

export default VelocityContainer;
