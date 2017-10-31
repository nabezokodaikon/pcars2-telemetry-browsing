import React from "react";
import ReactDom from "react-dom";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { isJson } from "../../common/jsUtil.js";

class TyreContent extends React.Component {
  constructor(props) {
    super(props)
  }

  createView() {
  }

  render() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    } else {
      return (
        <div>
        </div>
      );
    }
  }
}

TyreContent.propTypes = {
  telemetryData: PropTypes.object.isRequired,
  isBar: PropTypes.bool.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData,
    isBar: state.options.isBar
  };
};

const TyreContainer = connect(
  mapStateToProps
)(TyreContent);

export default TyreContainer;
