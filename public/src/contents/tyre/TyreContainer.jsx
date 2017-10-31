import React from "react";
import ReactDom from "react-dom";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { isJson } from "../../common/jsUtil.js";
import { createLeftTyreComponent } from "./tyreComponent.jsx";

class TyreContent extends React.Component {
  constructor(props) {
    super(props)
  }

  createLeftTyre() {
    const tyreData = this.props.telemetryData.tyreData;

    const tyreComponent = createLeftTyreComponent({
      width: 200,
      height: 300,
      tyreWear: tyreData.tyreWear,
      tyreTemp: tyreData.tyreTemp,
      brakeTempCelsius: tyreData.brakeTempCelsius,
      airPressure: tyreData.airPressure,
      isCelsius: this.props.isCelsius,
      isBar: this.props.isBar
    }); 

    return (
      <svg>
        {tyreComponent}
      </svg>
    );
  }

  render() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    } else {
      return (
        <div>
          {this.createLeftTyre()}
        </div>
      );
    }
  }
}

TyreContent.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  isBar: PropTypes.bool.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData,
    isBar: state.options.isBar
  };
};

const TyreContainer = connect(
  mapStateToProps
)(TyreContent);

export default TyreContainer;
