import React from "react";
import PropTypes from "prop-types";
import { getDamageColor } from "./telemetryUtil.js";
import icon from "../image/car.png";

export default class CarDamage extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const aeroDamage = props.aeroDamage;
    const engineDamage = props.engineDamage;

    return (
      <svg style={{ width: "4rem", height: "90%" }} preserveAspectRatio="xMidYMid meet" viewBox="0 0 50 100">
        <rect x="5%" y="0" width="90%" height="30%" fill={getDamageColor(aeroDamage)} />
        <rect x="5%" y="30%" width="90%" height="50%" fill={getDamageColor(engineDamage)} />
        <image x="0" y="0" width="100%" height="100%" xlinkHref={icon} />
        <text
          style={{ fontSize: "1rem", fontFamily: "'Inconsolata', monospace" }}
          x="50%"
          y="15%"
          textAnchor="middle"
          dominantBaseline="middle"
          fill="#ffffff"
        >
          {aeroDamage}%
        </text>
        <text
          style={{ fontSize: "1rem", fontFamily: "'Inconsolata', monospace" }}
          x="50%"
          y="75%"
          textAnchor="middle"
          dominantBaseline="middle"
          fill="#ffffff"
        >
          {engineDamage}%
        </text>
      </svg>
    );
  }
}

CarDamage.propTypes = {
  aeroDamage: PropTypes.string.isRequired,
  engineDamage: PropTypes.string.isRequired
};
