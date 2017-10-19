import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray } from "../../common/jsUtil.js";
import { isTelemetryDataFrameType } from "../../common/telemetryUtil.js";
import { TYRE_FRONT_LEFT, TYRE_FRONT_RIGHT, TYRE_REAR_LEFT, TYRE_REAR_RIGHT } from "../../common/telemetryConst.js";

class TyreData extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isTelemetryDataFrameType(this.props.telemetry)) {
      return <div></div>;
    }

    const data1 = this.props.telemetry.tyreData;
    const data2 = this.props.telemetry.tyreUdpData;

    const frontLeft1 = Object.keys(data1).map(key => {
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data1[key][TYRE_FRONT_LEFT]}</td>
        </tr>
      );
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
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data1[key][TYRE_FRONT_RIGHT]}</td>
        </tr>
      );
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
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data1[key][TYRE_REAR_LEFT]}</td>
        </tr>
      );
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
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data1[key][TYRE_REAR_RIGHT]}</td>
        </tr>
      );
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
    return this.getData();
  }
}

TyreData.propTypes = {
  telemetry: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetry: state.telemetry
  };
};

const TyreDataContainer = connect(
  mapStateToProps
)(TyreData);

export default TyreDataContainer;
