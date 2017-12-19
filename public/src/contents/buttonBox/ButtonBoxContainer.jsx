import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import SmallSpeedComponent from "../../share/SmallSpeedComponent.jsx";
import SmallFuelComponent from "../../share/SmallFuelComponent.jsx";
import shareStyle from "../../share/smallContent.css";

class ButtonBoxContent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;

    return (
      <div className={shareStyle.contents}>
        <div className={shareStyle.topContents}>
          TODO
        </div>
        <div className={shareStyle.bottomContents} onClick={props.onContentClick}>
          <SmallSpeedComponent isMeter={props.isMeter} telemetryData={telemetryData} />
          <SmallFuelComponent telemetryData={telemetryData} />
        </div>
      </div>
    );
  }
}

ButtonBoxContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData
  };
};

const ButtonBoxContainer = connect(
  mapStateToProps
)(ButtonBoxContent);

export default ButtonBoxContainer;
