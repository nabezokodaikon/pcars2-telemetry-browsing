import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import SmallSpeedComponent from "../../share/SmallSpeedComponent.jsx";
import SmallFuelComponent from "../../share/SmallFuelComponent.jsx";
import shareStyle from "../../share/smallContent.css";

class ButtonBoxContent extends React.Component {
  constructor(props) {
    super(props);

    this.requestButtonAction = this.requestButtonAction.bind(this);
  }

  requestButtonAction(index) {
    const url = "buttonBox/action";
    const json = {
      index: index
    };

    fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(json)
    })
    .then(res => console.log(`success callAction: ${index}`))
    .catch(error => console.log(error.message));
  }

  handleButtonClick(index) {
    this.props.onButtonClick(index);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;

    return (
      <div className={shareStyle.contents}>
        <div className={shareStyle.topContents}>
          <button onClick={() => this.requestButtonAction(0)}>
            <span>Test Button</span>
          </button>
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
