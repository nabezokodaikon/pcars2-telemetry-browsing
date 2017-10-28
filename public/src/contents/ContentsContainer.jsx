import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../common/contentNames.js";
import OptionsContainer from "../contents/options/OptionsContainer.jsx";

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
      backgroundColor: "#0f192a" 
    };
  }

  createCurrentContents() {
    switch (this.props.currentContent) {
        case contentNames.TIME:
          return <div>TODO</div>;
        case contentNames.OPTIONS:
          return <OptionsContainer />;
        default:
          return <div></div>;
    }
  }

  render() {
    return (
      <div style={this.getContentStyle()}>
        {this.createCurrentContents()}
      </div>
    );
  }
}

Contents.propTypes = {
  currentContent: PropTypes.string.isRequired,
}

const mapStateToProps = state => {
  return {
    currentContent: state.currentContent
  };
};

const ContentsContainer = connect(
  mapStateToProps
)(Contents);

export default ContentsContainer;
