import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { isJson } from "../../share/jsUtil.js";
import emptyStyle from "../../share/empty.css";

class TyreContent extends React.Component {
  constructor(props) {
    super(props)
  }

  render() {
    return (
      <div className={emptyStyle.empty}>
        <span>Planned for implementation.</span>
      </div>
    );
  }
}

TyreContent.propTypes = {
};

const mapStateToProps = state => {
  return {
  };
};

const TyreContainer = connect(
  mapStateToProps
)(TyreContent);

export default TyreContainer;
