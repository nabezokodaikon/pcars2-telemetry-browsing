import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";

class TimeContent extends React.Component {
  constructor(props) {
    super(props)
  }

  getTimeTableStyle() {
    return {
      position: "fixed",
      left: "0%",
      width: "100%",
      height: "75",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center"
    };
  }

  getContentStyle() {
    return {
      position: "fixed",
      width: "100%",
      height: "100%"
    };
  }

  createTimeTable() {
    return (
    <div style={this.getTimeTableStyle()}>
    </div>
    );
  }

  render() {
    return (
      <div style={this.getContentStyle()}>
        {this.createTimeTable()}
      </div>
    );
  }
}

TimeContent.propTypes = {
  timingsData: PropTypes.object.isRequired,
  lapTimeDetails: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  const data = state.currentUdpData;
  return {
    timingsData: data.timingsData,
    lapTimeDetails: data.lapTimeDetails
  };
};

const TimeContainer = connect(
  mapStateToProps
)(TimeContent);

export default TimeContainer;
