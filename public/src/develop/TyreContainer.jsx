import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../share/jsUtil.js";
import { TYRE_FRONT_LEFT, TYRE_FRONT_RIGHT, TYRE_REAR_LEFT, TYRE_REAR_RIGHT } from "../share/telemetryConst.js";

const Fragment = React.Fragment;

class Tyre extends React.Component {
  constructor(props) {
    super(props);
  }

  createRecords() {
    const createOtherData = () => {
      const data = this.props.telemetryData.tyre3;
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

    const data1 = this.props.telemetryData.tyre1;
    const data2 = this.props.telemetryData.tyre2;
    const frontLeft1 = Object.keys(data1).map(key => {
      const value = data1[key][TYRE_FRONT_LEFT];
      if (isJson(value)) {
        const childValues = Object.keys(value).map(childKey => {
          return (
            <tr key={childKey}>
              <td>{childKey}</td>
              <td>{value[childKey].toString()}</td>
            </tr>
          );
        });
        return <Fragment key={key}>{childValues}</Fragment>;
      } else {
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      }
    });
    const frontLeft2 = Object.keys(data2).map(key => {
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data2[key][TYRE_FRONT_LEFT]}</td>
        </tr>
      );
    });

    const frontRight1 = Object.keys(data1).map(key => {
      const value = data1[key][TYRE_FRONT_RIGHT];
      if (isJson(value)) {
        const childValues = Object.keys(value).map(childKey => {
          return (
            <tr key={childKey}>
              <td>{childKey}</td>
              <td>{value[childKey].toString()}</td>
            </tr>
          );
        });
        return <Fragment key={key}>{childValues}</Fragment>;
      } else {
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      }
    });
    const frontRight2 = Object.keys(data2).map(key => {
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data2[key][TYRE_FRONT_RIGHT]}</td>
        </tr>
      );
    });

    const rearLeft1 = Object.keys(data1).map(key => {
      const value = data1[key][TYRE_REAR_LEFT];
      if (isJson(value)) {
        const childValues = Object.keys(value).map(childKey => {
          return (
            <tr key={childKey}>
              <td>{childKey}</td>
              <td>{value[childKey].toString()}</td>
            </tr>
          );
        });
        return <Fragment key={key}>{childValues}</Fragment>;
      } else {
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      }
    });
    const rearLeft2 = Object.keys(data2).map(key => {
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data2[key][TYRE_REAR_LEFT]}</td>
        </tr>
      );
    });

    const rearRight1 = Object.keys(data1).map(key => {
      const value = data1[key][TYRE_REAR_RIGHT];
      if (isJson(value)) {
        const childValues = Object.keys(value).map(childKey => {
          return (
            <tr key={childKey}>
              <td>{childKey}</td>
              <td>{value[childKey].toString()}</td>
            </tr>
          );
        });
        return <Fragment key={key}>{childValues}</Fragment>;
      } else {
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      }
    });
    const rearRight2 = Object.keys(data2).map(key => {
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data2[key][TYRE_REAR_RIGHT]}</td>
        </tr>
      );
    });

    return (
      <table>
        <tbody>
          <tr>
            <td>TelemetryData.tyre</td>
          </tr>
          {createOtherData()}
          <tr>
            <td>Front Left</td>
          </tr>
          {frontLeft1}
          {frontLeft2}
          <tr>
            <td>Front Right</td>
          </tr>
          {frontRight1}
          {frontRight2}
          <tr>
            <td>Rear Left</td>
          </tr>
          {rearLeft1}
          {rearLeft2}
          <tr>
            <td>Rear Right</td>
          </tr>
          {rearRight1}
          {rearRight2}
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

Tyre.propTypes = {
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.currentUdpData.telemetryData
  };
};

const TyreContainer = connect(mapStateToProps)(Tyre);

export default TyreContainer;
