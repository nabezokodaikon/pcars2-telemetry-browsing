import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import {
  currentContent,
  requestAllButtonBoxMappings
} from "../../appActionCreators.js";
import * as contentNames from "../../share/contentNames.js";
import style from "./buttonBox.css";
import buttonStyle from "./button.css";
import Button from "./Button.jsx";
import ButtonWithDescription from "./ButtonWithDescription.jsx";

class ButtonBoxContent extends React.Component {
  constructor(props) {
    super(props);

    this.handleButtonClick = this.handleButtonClick.bind(this);
    this.handleCustomizeButtonClick = this.handleCustomizeButtonClick.bind(this);

    this.props.onRequestAllButtonBoxMappings();
  }

  handleButtonClick(index) {
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

  handleCustomizeButtonClick() {
    this.props.onCustomizeButtonClick();
  }

  render() {
    const props = this.props;

    const mappings = props.mappings;
    if (mappings.length < 1) {
      return <div></div>;
    }

    const red = buttonStyle.red;
    const yellow = buttonStyle.yellow;
    const green = buttonStyle.green;
    const blue = buttonStyle.blue;

    return (
      <div className={style.topContents}>

        <div className={style.leftButtons}>

          <div className={style.leftTopButtons}>
            <div>
              <div className={style.crossButtons}>
                <Button index={1} onClick={this.handleButtonClick} color={red} />
              </div>
              <div className={style.crossButtons}>
                <Button index={0} onClick={this.handleButtonClick} color={red} />
                <Button index={2} onClick={this.handleButtonClick} color={red} />
                <Button index={4} onClick={this.handleButtonClick} color={red} />
              </div>
              <div className={style.crossButtons}>
                <Button index={3} onClick={this.handleButtonClick} color={red} />
              </div>
            </div>
          </div>

          <div className={style.leftBottomButtons}>
            <div className={style.customizeButton} onClick={this.handleCustomizeButtonClick}>
              <span>CUSTOMIZE</span>
            </div>
          </div>

        </div> 

        <div className={style.rightButtons}>
          <div className={style.rightColumn}>
            <ButtonWithDescription index={5} onClick={this.handleButtonClick} color={red} description={mappings[5].description} />
            <ButtonWithDescription index={6} onClick={this.handleButtonClick} color={red} description={mappings[6].description} />
            <ButtonWithDescription index={7} onClick={this.handleButtonClick} color={red} description={mappings[7].description} />
            <ButtonWithDescription index={8} onClick={this.handleButtonClick} color={red} description={mappings[8].description} />
          </div>
          <div className={style.rightColumn}>
            <ButtonWithDescription index={9} onClick={this.handleButtonClick} color={yellow} description={mappings[9].description} />
            <ButtonWithDescription index={10} onClick={this.handleButtonClick} color={yellow} description={mappings[10].description} />
            <ButtonWithDescription index={11} onClick={this.handleButtonClick} color={yellow} description={mappings[11].description} />
            <ButtonWithDescription index={12} onClick={this.handleButtonClick} color={yellow} description={mappings[12].description} />
          </div>
          <div className={style.rightColumn}>
            <ButtonWithDescription index={13} onClick={this.handleButtonClick} color={green} description={mappings[13].description} />
            <ButtonWithDescription index={14} onClick={this.handleButtonClick} color={green} description={mappings[14].description} />
            <ButtonWithDescription index={15} onClick={this.handleButtonClick} color={green} description={mappings[15].description} />
            <ButtonWithDescription index={16} onClick={this.handleButtonClick} color={green} description={mappings[16].description} />
          </div>
          <div className={style.rightColumn}>
            <ButtonWithDescription index={17} onClick={this.handleButtonClick} color={blue} description={mappings[17].description} />
            <ButtonWithDescription index={18} onClick={this.handleButtonClick} color={blue} description={mappings[18].description} />
            <ButtonWithDescription index={19} onClick={this.handleButtonClick} color={blue} description={mappings[19].description} />
            <ButtonWithDescription index={20} onClick={this.handleButtonClick} color={blue} description={mappings[20].description} />
          </div>
        </div>

      </div>
    );
  }
}

ButtonBoxContent.propTypes = {
  mappings: PropTypes.array.isRequired,
  onRequestAllButtonBoxMappings: PropTypes.func.isRequired,
  onCustomizeButtonClick: PropTypes.func.isRequired
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
    onCustomizeButtonClick: () => {
      dispatch(currentContent(contentNames.BUTTON_BOX_CUSTOMIZE))
    }
  };
};

const ButtonBoxContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ButtonBoxContent);

export default ButtonBoxContainer;
