import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../share/contentNames.js";
import DefaultContainer from "../contents/default/DefaultContainer.jsx";
import EngineContainer from "../contents/engine/EngineContainer.jsx";
import TyreContainer from "../contents/tyre/TyreContainer.jsx";
import TimeContainer from "../contents/time/TimeContainer.jsx";
import RankContainer from "../contents/rank/RankContainer.jsx";
import ButtonBoxContainer from "../contents/buttonBox/ButtonBoxContainer.jsx";
import ButtonBoxCustomizeContainer from "../contents/buttonBoxCustomize/ButtonBoxCustomizeContainer.jsx";
import OptionsContainer from "../contents/options/OptionsContainer.jsx";
import CreditsContent from "../contents/credits/CreditsContent.jsx";
// TODO: Plan to delete.
import SimpleContainer from "../contents/simple/SimpleContainer.jsx";
import MotecContainer from "../contents/motec/MotecContainer.jsx";

class Contents extends React.Component {
  constructor(props) {
    super(props);
  }

  getContentStyle() {
    return {
      zIndex: 0,
      position: "fixed",
      width: "100%",
      height: "100%",
      backgroundColor: "#1b1d1e"
    };
  }

  createCurrentContents() {
    switch (this.props.currentContent) {
      case contentNames.DEFAULT:
        return <DefaultContainer />;
      case contentNames.ENGINE:
        return <EngineContainer />;
      case contentNames.TYRE:
        return <TyreContainer />;
      case contentNames.TIME:
        return <TimeContainer />;
      case contentNames.RANK:
        return <RankContainer />;
      case contentNames.BUTTON_BOX:
        return <ButtonBoxContainer />;
      case contentNames.BUTTON_BOX_CUSTOMIZE:
        return <ButtonBoxCustomizeContainer />;
      case contentNames.OPTIONS:
        return <OptionsContainer />;
      case contentNames.CREDITS:
        return <CreditsContent />;
      case contentNames.SIMPLE: // TODO: Plan to delete.
        return <SimpleContainer />;
      case contentNames.MOTEC: // TODO: Plan to delete.
        return <MotecContainer />;
      default:
        return <div />;
    }
  }

  render() {
    return <div style={this.getContentStyle()}>{this.createCurrentContents()}</div>;
  }
}

Contents.propTypes = {
  currentContent: PropTypes.string.isRequired
};

const mapStateToProps = state => {
  return {
    currentContent: state.currentContent
  };
};

const ContentsContainer = connect(mapStateToProps)(Contents);

export default ContentsContainer;
