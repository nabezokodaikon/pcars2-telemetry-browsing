import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../share/jsUtil.js";

class PicUp extends React.Component {
  constructor(props) {
    super(props)
  }

  render() {
    return (
      <div>PicUp</div>
    );
  }
}

PicUp.propTypes = {
 data : PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    data: state.currentUdpData
  };
};

const PicUpContainer = connect(
  mapStateToProps
)(PicUp);

export default PicUpContainer;
