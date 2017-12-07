import React from "react";
import PropTypes from "prop-types";
import style from "./damage.css";
import brakeIcon from "../../image/brake-blue.png";
import suspensionIcon from "../../image/suspension-blue.png";

export default class TyreDamageComponent extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    return (
      <div className={props.className}>
        <div className={style.header}>
          <span>{props.header}</span>
        </div>
        <div>
          <div className={[style.brake, style.border].join(" ")}>
            <div className={style.icon}>
              <img src={brakeIcon} />
            </div>
            <div className={style.value}>
              <span>{props.brake}%</span>
            </div>
          </div>
          <div className={[style.suspension, style.border].join(" ")}>
            <div className={style.icon}>
              <img src={suspensionIcon} />
            </div>
            <div className={style.value}>
              <span>{props.suspension}%</span>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

TyreDamageComponent.propTypes = {
  className: PropTypes.string.isRequired,
  header: PropTypes.string.isRequired,
  brake: PropTypes.string.isRequired,
  suspension: PropTypes.string.isRequired
}
