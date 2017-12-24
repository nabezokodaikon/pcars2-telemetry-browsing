import React from "react";
import PropTypes from "prop-types";
import CharDropDown from "./CharDropDown.jsx";
import style from "./button.css";

export default class Button extends React.Component {
  constructor(props) {
    super(props);

    this.handleDropDownChange = this.handleDropDownChange.bind(this);
  }

  handleDropDownChange(char) {
    this.props.onCharChanged(this.props.index, char);
  }

  render() {
    return (
      <div className={style.label}>
        <CharDropDown char={this.props.char} onChange={this.handleDropDownChange} />
      </div>
    );
  }
}

Button.propTypes = {
  index: PropTypes.number.isRequired,
  char: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  onCharChanged: PropTypes.func.isRequired,
  onDescriptionChange: PropTypes.func.isRequired,
  onDescriptionChanged: PropTypes.func.isRequired
};
