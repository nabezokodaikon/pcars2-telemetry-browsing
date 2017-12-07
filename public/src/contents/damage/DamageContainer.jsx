import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import { isJson } from "../../share/jsUtil.js";
import LargeGearComponent from "../../share/LargeGearComponent.jsx";
import LargeFuelComponent from "../../share/LargeFuelComponent.jsx";
import shareStyle from "../../share/largeContent.css";
import style from "./damage.css";
// import SingleDamageComponent from "./SingleDamageComponent.jsx";
// import TyreDamageComponent from "./TyreDamageComponent.jsx";

class DamageContent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div></div>;
    }

    return (
      <div className={shareStyle.contents} onClick={props.onContentClick}>
        <div className={shareStyle.topContents}>
          <div className={shareStyle.leftContents}>
            <LargeGearComponent isMeter={props.isMeter} telemetryData={telemetryData} />
          </div>
          <div className={shareStyle.rightContents}>
          </div>
        </div>
        <div className={shareStyle.bottomContents}>
          <LargeFuelComponent telemetryData={telemetryData} fuelData={props.fuelData} />
        </div>
      </div>
    );
  }
}

DamageContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  fuelData: PropTypes.object.isRequired,
  onContentClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData,
    fuelData: data.fuelData
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onContentClick: () => {
      dispatch(currentContent(contentNames.TIME))
    }
  };
};

const DamageContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DamageContent);

export default DamageContainer;
