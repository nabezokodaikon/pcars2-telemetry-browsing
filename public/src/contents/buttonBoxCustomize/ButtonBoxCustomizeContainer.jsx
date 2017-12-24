import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { 
  currentContent,
  descriptionChange
} from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import SmallSpeedComponent from "../../share/SmallSpeedComponent.jsx";
import SmallFuelComponent from "../../share/SmallFuelComponent.jsx";
import shareStyle from "../../share/smallContent.css";
import style from "./buttonBoxCustomize.css";
import ButtonWithDescription from "./ButtonWithDescription.jsx"

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
    const mappings = props.mappings;
    if (mappings.length < 1) {
      return <div></div>;
    }

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
              <ButtonWithDescription index={1} char={mappings[1].char} description={mappings[1].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChange={this.props.onDescriptionChange}
                onDescriptionChanged={this.props.onDescriptionChanged}
              />
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
  mappings: PropTypes.array.isRequired,
  onCharChanged: PropTypes.func.isRequired,
  onDescriptionChange: PropTypes.func.isRequired,
  onDescriptionChanged: PropTypes.func.isRequired,
  onExitButtonClick: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    isMeter: state.options.isMeter,
    telemetryData: data.telemetryData,
    mappings: state.buttonBox.mappings
  };
};

const mapDispatchToProps = dispatch => {
  return {
    onCharChanged(index, char) {
      // TODO
      console.log(`${index}, ${char}`);
    },
    onDescriptionChange(index, description) {
      dispatch(descriptionChange(index, description));
    },
    onDescriptionChanged(index, description) {
      // TODO
      console.log(`${index}, ${description}`);
    },
    onExitButtonClick: () => {
      dispatch(currentContent(contentNames.BUTTON_BOX));
    }
  };
};

const ButtonBoxCustomizeContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ButtonBoxCustomizeContent);

export default ButtonBoxCustomizeContainer;
