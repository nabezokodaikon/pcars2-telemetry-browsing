import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import {
  requestTempUnitChange,
  requestDistanceUnitChange,
  requestAirPressureUnitChange
} from "../../appActionCreators.js";
import checkBoxStyle from "../../common/slideCheckBox.css";

class OptionsContent extends React.Component {
  constructor(props) {
    super(props);
    this.handleTempUnitCheckBoxChanged = this.handleTempUnitCheckBoxChanged.bind(this);
    this.handleDistanceUnitCheckBoxChanged = this.handleDistanceUnitCheckBoxChanged.bind(this);
    this.handleAirPressureUnitCheckBoxChanged = this.handleAirPressureUnitCheckBoxChanged.bind(this);
  }

  handleTempUnitCheckBoxChanged() {
  }

  handleDistanceUnitCheckBoxChanged() {
  }

  handleAirPressureUnitCheckBoxChanged() {
  }

  createTempChangeContent() {
    return (
      <div>
        <span>Celsius</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input id="tempCheckBox" type="checkbox" />
          <label htmlFor="tempCheckBox"></label>
        </div>
        <span>Fahrenheit</span>
      </div>
    );
  }

  createDistanceChangeContent() {
    return (
      <div>
        <span>Meter</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input id="distanceCheckBox" type="checkbox" />
          <label htmlFor="distanceCheckBox"></label>
        </div>
        <span>Miles</span>
      </div>
    );
  }

  createAirPressureChangeContent() {
    return (
      <div>
        <span>bar</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input id="airPressureCheckBox" type="checkbox" />
          <label htmlFor="airPressureCheckBox"></label>
        </div>
        <span>psi</span>
      </div>
    );
  }

  render() {
    return (
      <div>
        {this.createTempChangeContent()}
        {this.createDistanceChangeContent()}
        {this.createAirPressureChangeContent()}
      </div>
    );
  }
}

OptionsContent.propTypes = {
  isCelsius: PropTypes.bool.isRequired,
  isMeter: PropTypes.bool.isRequired,
  isBar: PropTypes.bool.isRequired,
  onTempUnitChange: PropTypes.func.isRequired,
  onDistanceUnitChange: PropTypes.func.isRequired,
  onAirPressureUnitChange: PropTypes.func.isRequired
}

const mapStateToProps = state => {
  const options = state.options;
  return {
    isCelsius: options.isCelsius,
    isMeter: options.isMeter,
    isBar: options.isBar,
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onTempUnitChange: isCelsius => {
      dispatch(requestTempUnitChange(isCelsius));
    },
    onDistanceUnitChange: isMeter => {
      dispatch(requestDistanceUnitChange(isMeter));
    },
    onAirPressureUnitChange: isBar => {
      dispatch(requestAirPressureUnitChange(isBar));
    }
  };
};

const OptionsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(OptionsContent);

export default OptionsContainer;
