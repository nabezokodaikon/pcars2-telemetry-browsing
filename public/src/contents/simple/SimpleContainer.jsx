import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../../common/jsUtil.js";
import { createGearHUDComponent } from "../../common/gearHUDComponent.jsx";

class SimpleContent extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    }

    const width = document.documentElement.clientWidth;
    const height = document.documentElement.clientHeight;
    const viewBox = "0" + " " + "0" + " " + width + " " + height;

    const carStateData = this.props.telemetryData.carStateData; 

    const gearHUDComponent = createGearHUDComponent({
      cx: 50,
      cy: 50,
      radius: 50,
      gear: carStateData.gear,
      speed: carStateData.speed,
      rpm: carStateData.rpm,
      maxRpm: carStateData.maxRpm,
      throttle: carStateData.throttle,
      brake: carStateData.brake,
      clutch: carStateData.clutch,
      isMeter: this.props.isMeter
    });

    return (
      <svg preserveAspectRatio="xMinYMin meet" width="100%" height="100%" viewBox="0 0 100 100">
        {gearHUDComponent}
      </svg>
    );
  }

  render() {
    return this.getData();
  }
}

SimpleContent.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  isMeter: PropTypes.bool.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData,
    isMeter: state.options.isMeter
  };
};

const SimpleContainer = connect(
  mapStateToProps
)(SimpleContent);

export default SimpleContainer;
