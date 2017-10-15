import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { testCounter, openWebSocket } from "../../appActionCreators.js";

class CarStateData extends React.Component {
  constructor(props) {
    super(props)
    this.handleTestClick = this.handleTestClick.bind(this);
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

  handleTestClick() {
    this.props.onTestClick(2);
  }

  render() {
    return (
      <div>
        {this.renderSpeed()}
        <button onClick={this.handleTestClick}>Click me</button>
        <p>{this.props.testCount}</p>
      </div>
    );
  }
}

CarStateData.propTypes = {
  telemetry: PropTypes.object.isRequired,
  testCount: PropTypes.number.isRequired,
  onTestClick: PropTypes.func.isRequired,
  onOpenWebSocket: PropTypes.func.isRequired
};

const mapStateToProps = state => {
  return {
    testCount: state.testCount,
    telemetry: state.telemetry
  };
};

const mapDispatchToProps = dispatch => {
  console.log("mapDispatchToProps");
  return {
    onTestClick: addValue => {
      dispatch(testCounter(addValue));
    },
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
