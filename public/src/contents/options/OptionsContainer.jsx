import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import {
  requestAllOptions,
  requestTempUnitChange,
  requestDistanceUnitChange,
  requestAirPressureUnitChange
} from "../../appActionCreators.js";
import checkBoxStyle from "../../share/slideCheckbox.css";
import style from "./options.css";

class OptionsContent extends React.Component {
  constructor(props) {
    super(props);
    this.handleTempUnitCheckBoxChanged = this.handleTempUnitCheckBoxChanged.bind(this);
    this.handleDistanceUnitCheckBoxChanged = this.handleDistanceUnitCheckBoxChanged.bind(this);
    this.handleAirPressureUnitCheckBoxChanged = this.handleAirPressureUnitCheckBoxChanged.bind(this);

    this.props.onRequestAllOptions();
  }

  handleTempUnitCheckBoxChanged() {
    this.props.onTempUnitChange(!this.props.isCelsius);
  }

  handleDistanceUnitCheckBoxChanged() {
    this.props.onDistanceUnitChange(!this.props.isMeter);
  }

  handleAirPressureUnitCheckBoxChanged() {
    this.props.onAirPressureUnitChange(!this.props.isBar);
  }

  createTempUnitChangeContent() {
    return (
      <div className={style.unitItem} onClick={this.handleTempUnitCheckBoxChanged}>
        <span>Fahrenheit</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="tempCheckBox"
            type="checkbox"
            checked={this.props.isCelsius}
            onChange={this.handleTempUnitCheckBoxChanged}
          />
          <label htmlFor="tempCheckBox" />
        </div>
        <span>Celsius</span>
      </div>
    );
  }

  createDistanceUnitChangeContent() {
    return (
      <div className={style.unitItem} onClick={this.handleDistanceUnitCheckBoxChanged}>
        <span>Miles</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="distanceCheckBox"
            type="checkbox"
            checked={this.props.isMeter}
            onChange={this.handleDistanceUnitCheckBoxChanged}
          />
          <label htmlFor="distanceCheckBox" />
        </div>
        <span>Meter</span>
      </div>
    );
  }

  createAirPressureUnitChangeContent() {
    return (
      <div className={style.unitItem} onClick={this.handleAirPressureUnitCheckBoxChanged}>
        <span>psi</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="airPressureCheckBox"
            type="checkbox"
            checked={this.props.isBar}
            onChange={this.handleAirPressureUnitCheckBoxChanged}
          />
          <label htmlFor="airPressureCheckBox" />
        </div>
        <span>bar</span>
      </div>
    );
  }

  render() {
    return (
      <div className={style.root}>
        <h2>OPTIONS</h2>
        <div>
          <div>
            <h4>Unit</h4>
            {this.createTempUnitChangeContent()}
            {this.createDistanceUnitChangeContent()}
            {this.createAirPressureUnitChangeContent()}
          </div>
        </div>
      </div>
    );
  }
}

OptionsContent.propTypes = {
  isCelsius: PropTypes.bool.isRequired,
  isMeter: PropTypes.bool.isRequired,
  isBar: PropTypes.bool.isRequired,
  onRequestAllOptions: PropTypes.func.isRequired,
  onTempUnitChange: PropTypes.func.isRequired,
  onDistanceUnitChange: PropTypes.func.isRequired,
  onAirPressureUnitChange: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const options = state.options;
  return {
    isCelsius: options.isCelsius,
    isMeter: options.isMeter,
    isBar: options.isBar
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onRequestAllOptions: () => {
      dispatch(requestAllOptions());
    },
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

const OptionsContainer = connect(mapStateToProps, mapDispatchToProps)(OptionsContent);

export default OptionsContainer;
