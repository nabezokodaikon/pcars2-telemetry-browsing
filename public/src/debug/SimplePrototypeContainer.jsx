import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../common/jsUtil.js";
import { createRpmComponent } from "../common/telemetryComponents.jsx";

class SimpleProtoType extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    }

    const carStateData = this.props.telemetryData.carStateData; 
    const maxRpm = carStateData.maxRpm;
    const rpm = carStateData.rpm;

    const rpmComponent = createRpmComponent(rpm, maxRpm, 500, 500, 400, 16);

    return (
      <svg>
        {rpmComponent}
      </svg>
    );
  }

  render() {
    return this.getData();
  }
}

SimpleProtoType.propTypes = {
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData
  };
};

const SimpleProtoTypeContainer = connect(
  mapStateToProps
)(SimpleProtoType);

export default SimpleProtoTypeContainer;
