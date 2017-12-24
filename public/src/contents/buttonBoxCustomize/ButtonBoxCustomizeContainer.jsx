import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { 
  currentContent,
  requestAllButtonBoxMappings,
  requestButtonBoxCharChage,
  requestButtonBoxDescriptionChage,
  chageButtonBoxDescription
} from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import SmallSpeedComponent from "../../share/SmallSpeedComponent.jsx";
import SmallFuelComponent from "../../share/SmallFuelComponent.jsx";
import shareStyle from "../../share/smallContent.css";
import style from "./buttonBoxCustomize.css";
import Button from "./Button.jsx"
import ButtonWithDescription from "./ButtonWithDescription.jsx"

class ButtonBoxCustomizeContent extends React.Component {
  constructor(props) {
    super(props);

    this.handleExitButtonClick = this.handleExitButtonClick.bind(this);

    this.props.onRequestAllButtonBoxMappings();
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
                  <Button index={1} char={mappings[1].char} onCharChanged={this.props.onCharChanged} />
                </div>
                <div className={style.crossButtons}>
                  <Button index={0} char={mappings[0].char} onCharChanged={this.props.onCharChanged} />
                  <Button index={2} char={mappings[2].char} onCharChanged={this.props.onCharChanged} />
                  <Button index={4} char={mappings[4].char} onCharChanged={this.props.onCharChanged} />
                </div>
                <div className={style.crossButtons}>
                  <Button index={3} char={mappings[3].char} onCharChanged={this.props.onCharChanged} />
                </div>
              </div>
            </div>

            <div className={style.leftBottomButtons}>
              <div className={style.exitButton} onClick={this.handleExitButtonClick}>
                <span>EXIT</span>
              </div>
            </div>

          </div> 

          <div className={style.rightButtons}>
            <div className={style.rightColumn}>
              <ButtonWithDescription index={5} char={mappings[5].char} description={mappings[5].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={6} char={mappings[6].char} description={mappings[6].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={7} char={mappings[7].char} description={mappings[7].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={8} char={mappings[8].char} description={mappings[8].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
            </div>
            <div className={style.rightColumn}>
              <ButtonWithDescription index={9} char={mappings[9].char} description={mappings[9].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={10} char={mappings[10].char} description={mappings[10].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={11} char={mappings[11].char} description={mappings[11].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={12} char={mappings[12].char} description={mappings[12].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
            </div>
            <div className={style.rightColumn}>
              <ButtonWithDescription index={13} char={mappings[13].char} description={mappings[13].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={14} char={mappings[14].char} description={mappings[14].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={15} char={mappings[15].char} description={mappings[15].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={16} char={mappings[16].char} description={mappings[16].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
            </div>
            <div className={style.rightColumn}>
              <ButtonWithDescription index={17} char={mappings[17].char} description={mappings[17].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={18} char={mappings[18].char} description={mappings[18].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={19} char={mappings[19].char} description={mappings[19].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
              />
              <ButtonWithDescription index={20} char={mappings[20].char} description={mappings[20].description}
                onCharChanged={this.props.onCharChanged}
                onDescriptionChanged={this.props.onDescriptionChanged}
                onDescriptionChange={this.props.onDescriptionChange}
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
  onRequestAllButtonBoxMappings: PropTypes.func.isRequired,
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
    onRequestAllButtonBoxMappings() {
      dispatch(requestAllButtonBoxMappings());
    },
    onCharChanged(index, char) {
      dispatch(requestButtonBoxCharChage(index, char));
    },
    onDescriptionChanged(index, description) {
      dispatch(requestButtonBoxDescriptionChage(index, description));
    },
    onDescriptionChange(index, description) {
      dispatch(chageButtonBoxDescription(index, description));
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
