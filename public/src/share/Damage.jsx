import React from "react";
import PropTypes from "prop-types";
import { TYRE_FRONT_LEFT, TYRE_FRONT_RIGHT, TYRE_REAR_LEFT, TYRE_REAR_RIGHT } from "./telemetryConst.js";
import CarDamage from "./CarDamage.jsx";
import BrakeDamage from "./BrakeDamage.jsx";
import SuspensionDamage from "./SuspensionDamage.jsx";
import style from "./largeContent.css";

export default class Damage extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const brakeDamage = props.brakeDamage;
    const suspensionDamage = props.suspensionDamage;

    return (
      <div className={style.damage}>
        <SuspensionDamage
          frontDamage={suspensionDamage[TYRE_FRONT_LEFT]}
          rearDamage={suspensionDamage[TYRE_REAR_LEFT]}
        />
        <SuspensionDamage
          frontDamage={suspensionDamage[TYRE_FRONT_RIGHT]}
          rearDamage={suspensionDamage[TYRE_REAR_RIGHT]}
        />
        <BrakeDamage frontDamage={brakeDamage[TYRE_FRONT_LEFT]} rearDamage={brakeDamage[TYRE_REAR_LEFT]} />
        <BrakeDamage frontDamage={brakeDamage[TYRE_FRONT_RIGHT]} rearDamage={brakeDamage[TYRE_REAR_RIGHT]} />
        <CarDamage aeroDamage={props.aeroDamage} engineDamage={props.engineDamage} />
      </div>
    );
  }
}

Damage.propTypes = {
  aeroDamage: PropTypes.string.isRequired,
  engineDamage: PropTypes.string.isRequired,
  brakeDamage: PropTypes.array.isRequired,
  suspensionDamage: PropTypes.array.isRequired
};
