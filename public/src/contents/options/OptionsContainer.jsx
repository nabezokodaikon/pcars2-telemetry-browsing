import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import {
  requestAllOptions,
  requestTempUnitChange,
  requestDistanceUnitChange,
  requestAirPressureUnitChange
} from "../../appActionCreators.js";
import checkBoxStyle from "../../share/slideCheckBox.css";

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
      <div style={this.getFlexItemStyle()} onClick={this.handleTempUnitCheckBoxChanged}>
        <span style={this.getSpanStyle()}>Fahrenheit</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="tempCheckBox"
            type="checkbox"
            checked={this.props.isCelsius}
            onChange={this.handleTempUnitCheckBoxChanged}
          />
          <label htmlFor="tempCheckBox"></label>
        </div>
        <span style={this.getSpanStyle()}>Celsius</span>
      </div>
    );
  }

  createDistanceChangeContent() {
    return (
      <div style={this.getFlexItemStyle()} onClick={this.handleDistanceUnitCheckBoxChanged}>
        <span style={this.getSpanStyle()}>Miles</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="distanceCheckBox"
            type="checkbox"
            checked={this.props.isMeter}
            onChange={this.handleDistanceUnitCheckBoxChanged}
          />
          <label htmlFor="distanceCheckBox"></label>
        </div>
        <span style={this.getSpanStyle()}>Meter</span>
      </div>
    );
  }

  createAirPressureChangeContent() {
    return (
      <div style={this.getFlexItemStyle()} onClick={this.handleAirPressureUnitCheckBoxChanged}>
        <span style={this.getSpanStyle()}>psi</span>
        <div className={checkBoxStyle.slideCheckBox}>
          <input
            id="airPressureCheckBox"
            type="checkbox"
            checked={this.props.isBar}
            onChange={this.handleAirPressureUnitCheckBoxChanged}
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
        <h2>OPTIONS</h2>
        <h3>Unit</h3>
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
  onRequestAllOptions: PropTypes.func.isRequired,
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

const OptionsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(OptionsContent);

export default OptionsContainer;
