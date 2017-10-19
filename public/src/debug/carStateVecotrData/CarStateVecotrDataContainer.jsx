import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray } from "../../common/jsUtil.js";
import { isTelemetryDataFrameType } from "../../common/telemetryUtil.js";
import { VEC_X, VEC_Y, VEC_Z } from "../../common/telemetryConst.js";

class CarStateVecotrData extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isTelemetryDataFrameType(this.props.telemetry)) {
      return <div></div>;
    }

    const data = this.props.telemetry.carStateVecotrData;

    const vecX = Object.keys(data).map(key => {
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data[key][VEC_X]}</td>
        </tr>
      );
    });

    const vecY = Object.keys(data).map(key => {
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data[key][VEC_Y]}</td>
        </tr>
      );
    });

    const vecZ = Object.keys(data).map(key => {
      return (
        <tr key={key}>
          <td>{key}</td>
          <td>{data[key][VEC_Z]}</td>
        </tr>
      );
    });

    return (
      <table>
        <tbody>
          <tr>
            <td>VEC_X</td>
          </tr>
          {vecX}
          <tr>
            <td>VEC_Y</td>
          </tr>
          {vecY}
          <tr>
            <td>VEC_Z</td>
          </tr>
          {vecZ}
        </tbody>
      </table>
    );
  }

  render() {
    return this.getData();
  }
}

CarStateVecotrData.propTypes = {
  telemetry: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetry: state.telemetry
  };
};

const CarStateVecotrDataContainer = connect(
  mapStateToProps
)(CarStateVecotrData);

export default CarStateVecotrDataContainer;
