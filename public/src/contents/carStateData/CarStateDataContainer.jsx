import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { openWebSocket } from "../../appActionCreators.js";

class CarStateData extends React.Component {
  constructor(props) {
    super(props)
  }

  componentDidMount() {
    console.log("componentDidMount");
    this.props.onOpenWebSocket();
  }

  renderSpeed() {
    if (Object.keys(this.props.telemetry).length > 0 && this.props.telemetry.frameType == 0) {
      return <p>{this.props.telemetry.carStateData.speed}</p>;
    } else {
      return <p>--:--:--.---</p>;
    }
  }

  render() {
    return (
      <div>
        {this.renderSpeed()}
      </div>
    );
  }
}

CarStateData.propTypes = {
  telemetry: PropTypes.object.isRequired,
  onOpenWebSocket: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  return {
    telemetry: state.telemetry
  };
};

const mapDispatchToProps = dispatch => {
  console.log("mapDispatchToProps");
  return {
    onOpenWebSocket: () => {
      dispatch(openWebSocket());
    }
  };
};

const CarStateDataContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(CarStateData);

export default CarStateDataContainer;
