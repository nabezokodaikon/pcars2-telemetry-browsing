import React from "react";
import PropTypes from "prop-types";
import style from "./damage.css";

export default class SingleDamageComponent extends React.Component {
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
        <div className={style.border}>
          <div className={style.value}>
            <span>{props.value}%</span>
          </div>
        </div>
      </div>
    );
  }
}

SingleDamageComponent.propTypes = {
  className: PropTypes.string.isRequired,
  header: PropTypes.string.isRequired,
  value: PropTypes.string.isRequired
}
