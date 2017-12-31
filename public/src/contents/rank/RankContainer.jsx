import React from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { isJson } from "../../share/jsUtil.js";
import emptyStyle from "../../share/empty.css";

class RankContent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className={emptyStyle.empty}>
        <span>Planned for implementation.</span>
      </div>
    );
  }
}

RankContent.propTypes = {};

const mapStateToProps = state => {
  return {};
};

const RankContainer = connect(mapStateToProps)(RankContent);

export default RankContainer;
