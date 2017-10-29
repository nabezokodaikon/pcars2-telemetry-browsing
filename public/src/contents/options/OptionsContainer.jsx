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

  handleTempUnitCheckBoxChanged(evt) {
    this.props.onTempUnitChange(evt.target.checked);
  }

  handleDistanceUnitCheckBoxChanged(evt) {
    this.props.onDistanceUnitChange(evt.target.checked);
  }

  handleAirPressureUnitCheckBoxChanged(evt) {
    this.props.onAirPressureUnitChange(evt.target.checked);
  }

  getFlexContainerStyle() {
    return {
      display: "flex",
      flexDirection: "column",
      alignItems: "left",
      width: "50%",
      marginLeft: "4rem"
    };
  }

  getFlexItemStyle() {
    return {
      display: "flex",
      alignItems: "center",
      marginBottom: "2rem"
    };
  }

  getSpanStyle() {
    return {
      display: "block",
      width: "6rem",
      textAlign: "center"
    }
  }

  createTempChangeContent() {
    return (
      <div style={this.getFlexItemStyle()}>
        <span style={this.getSpanStyle()}>Fahrenheit</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="tempCheckBox"
            type="checkbox"
            checked={this.props.isCelsius}
            onChange={evt => this.handleTempUnitCheckBoxChanged(evt)}
          />
          <label htmlFor="tempCheckBox"></label>
        </div>
        <span style={this.getSpanStyle()}>Celsius</span>
      </div>
    );
  }

  createDistanceChangeContent() {
    return (
      <div style={this.getFlexItemStyle()}>
        <span style={this.getSpanStyle()}>Miles</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="distanceCheckBox"
            type="checkbox"
            checked={this.props.isMeter}
            onChange={evt => this.handleDistanceUnitCheckBoxChanged(evt)}
          />
          <label htmlFor="distanceCheckBox"></label>
        </div>
        <span style={this.getSpanStyle()}>Meter</span>
      </div>
    );
  }

  createAirPressureChangeContent() {
    return (
      <div style={this.getFlexItemStyle()}>
        <span style={this.getSpanStyle()}>psi</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="airPressureCheckBox"
            type="checkbox"
            checked={this.props.isBar}
            onChange={evt => this.handleAirPressureUnitCheckBoxChanged(evt)}
          />
          <label htmlFor="airPressureCheckBox"></label>
        </div>
        <span style={this.getSpanStyle()}>bar</span>
      </div>
    );
  }

  render() {
    return (
      <div style={this.getFlexContainerStyle()}>
        <h1>OPTIONS</h1>
        <h2>Unit</h2>
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
    isBar: options.isBar
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
