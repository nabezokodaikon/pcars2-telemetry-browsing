import React from "react";
import PropTypes from "prop-types";
import { isJson } from "./jsUtil.js";
import style from "./smallContent.css";

const WHITE = "#FFFFFF";

export default class SmallSpeedComponent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>
        HOGE
      </div>
    );
  }
}

SmallSpeedComponent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired
};
