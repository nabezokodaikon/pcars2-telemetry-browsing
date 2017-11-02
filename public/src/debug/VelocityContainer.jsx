import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../common/jsUtil.js";

class Velocity extends React.Component {
  constructor(props) {
    super(props)
  }

  createRecords() {
    const createVelocity = () => {
      const data = this.props.velocity;
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
    }

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
    if (!isJson(this.props.velocity)) {
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

Velocity.propTypes = {
  velocity: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    velocity: state.currentUdpData.telemetryData.velocity
  };
};

const VelocityContainer = connect(
  mapStateToProps
)(Velocity);

export default VelocityContainer;
