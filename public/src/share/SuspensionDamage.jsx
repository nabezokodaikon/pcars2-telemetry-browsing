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
        <rect x="15%" y="0.81rem" width="70%" height="2.28rem" fill={getDamageColor(frontDamage)} />
        <image x="0" y="0.8rem" width="100%" height="2.3rem" xlinkHref={icon} />
        <rect x="15%" y="3.21rem" width="70%" height="2.28rem" fill={getDamageColor(rearDamage)} />
        <image x="0" y="3.2rem" width="100%" height="2.3rem" xlinkHref={icon} />
        <text
          style={{ fontSize: "1rem", fontFamily: "'Inconsolata', monospace" }}
          x="50%"
          y="0.5rem"
          textAnchor="middle"
          dominantBaseline="middle"
          fill="#ffffff"
        >
          {frontDamage}%
        </text>
        <text
          style={{ fontSize: "1rem", fontFamily: "'Inconsolata', monospace" }}
          x="50%"
          y="6rem"
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
