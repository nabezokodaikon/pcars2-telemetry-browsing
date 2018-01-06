import React from "react";
import PropTypes from "prop-types";
import { getDamageColor } from "./telemetryUtil.js";
import icon from "../image/suspension2.png";

export default class SuspensionDamage extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const frontDamage = props.frontDamage;
    const rearDamage = props.rearDamage;

    return (
      <svg style={{ width: "4rem", height: "90%" }} preserveAspectRatio="xMidYMid meet" viewBox="0 0 50 100">
        <rect x="20%" y="15.5%" width="60%" height="34%" fill={getDamageColor(frontDamage)} />
        <image x="0" y="15%" width="100%" height="35%" xlinkHref={icon} />
        <rect x="20%" y="50.5%" width="60%" height="34%" fill={getDamageColor(rearDamage)} />
        <image x="0" y="50%" width="100%" height="35%" xlinkHref={icon} />
        <text
          style={{ fontSize: "1rem", fontFamily: "'Inconsolata', monospace" }}
          x="50%"
          y="10%"
          textAnchor="middle"
          dominantBaseline="middle"
          fill="#ffffff"
        >
          {frontDamage}%
        </text>
        <text
          style={{ fontSize: "1rem", fontFamily: "'Inconsolata', monospace" }}
          x="50%"
          y="95%"
          textAnchor="middle"
          dominantBaseline="middle"
          fill="#ffffff"
        >
          {rearDamage}%
        </text>
      </svg>
    );
  }
}

SuspensionDamage.propTypes = {
  frontDamage: PropTypes.string.isRequired,
  rearDamage: PropTypes.string.isRequired
};
