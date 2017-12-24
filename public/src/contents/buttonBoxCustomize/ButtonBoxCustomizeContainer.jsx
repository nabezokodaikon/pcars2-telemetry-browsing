import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { currentContent } from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import SmallSpeedComponent from "../../share/SmallSpeedComponent.jsx";
import SmallFuelComponent from "../../share/SmallFuelComponent.jsx";
import shareStyle from "../../share/smallContent.css";
import style from "./buttonBoxCustomize.css";

class ButtonBoxCustomizeContent extends React.Component {
  constructor(props) {
    super(props);

    this.handleExitButtonClick = this.handleExitButtonClick.bind(this);
  }

  handleExitButtonClick() {
    this.props.onExitButtonClick();
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;

    return (
      <div className={shareStyle.contents}>
        <div className={style.topContents}>

          <div className={style.leftButtons}>

            <div className={style.leftTopButtons}>
              <div>
                <div className={style.crossButtons}>
                </div>
                <div className={style.crossButtons}>
                </div>
                <div className={style.crossButtons}>
                </div>
              </div>
            </div>

            <div className={style.leftBottomButtons}>
              <div className={style.customButton} onClick={this.handleExitButtonClick}>
                <span>EXIT</span>
              </div>
            </div>

          </div> 

          <div className={style.rightButtons}>
            <div className={style.rightColumn}>
            </div>
            <div className={style.rightColumn}>
            </div>
            <div className={style.rightColumn}>
            </div>
            <div className={style.rightColumn}>
            </div>
          </div>

        </div>
        <div className={shareStyle.bottomContents} onClick={props.onContentClick}>
          <SmallSpeedComponent isMeter={props.isMeter} telemetryData={telemetryData} />
          <SmallFuelComponent telemetryData={telemetryData} />
        </div>
      </div>
    );
  }

}

ButtonBoxCustomizeContent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired,
  onExitButtonClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onExitButtonClick: () => {
      dispatch(currentContent(contentNames.BUTTON_BOX))
    }
  };
};

const ButtonBoxCustomizeContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ButtonBoxCustomizeContent);

export default ButtonBoxCustomizeContainer;
