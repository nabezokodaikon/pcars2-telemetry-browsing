import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import * as contentNames from "../common/contentNames.js";
import TelemetryDataContainer from "./TelemetryDataContainer.jsx";

class Contents extends React.Component {
  constructor(props) {
    super(props);
  }

  getContentStyle() {
    return {
      zIndex: 0,
      position: "absolute",
      width: "100vw",
      height: "100vh",
      backgroundColor: "#EEEEEE"
    };
  }

  getCurrentContents() {
    switch (this.props.currentContent) {
        case contentNames.TELEMETRY_DATA:
          return <TelemetryDataContainer />;
        default:
          return <div></div>;
    }
  }

  render() {
    return (
      <div style={this.getContentStyle()}>
        {this.getCurrentContents()}
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
