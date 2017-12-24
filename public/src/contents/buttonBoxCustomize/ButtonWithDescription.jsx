import React from "react";
import PropTypes from "prop-types";
import CharDropDown from "./CharDropDown.jsx";
import style from "./button.css";

export default class ButtonWithDescription extends React.Component {
  constructor(props) {
    super(props);

    this.handleDropDownChange = this.handleDropDownChange.bind(this);
    this.handleInputBlur = this.handleInputBlur.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
  }

  handleDropDownChange(char) {
    this.props.onCharChanged(this.props.index, char);
  }

  handleInputChange() {
    this.props.onDescriptionChange(this.props.index, this.input.value);
  }

  handleInputBlur() {
    this.props.onDescriptionChanged(this.props.index, this.input.value);
  }

  render() {
    return (
      <div className={style.buttonWithDescription}>
        <CharDropDown char={this.props.char} onChange={this.handleDropDownChange} />
        <input type="text" maxLength="12" className={style.description}
          ref={input => { this.input = input; }}
          onChange={this.handleInputChange}
          onBlur={this.handleInputBlur}
          value={this.props.description}
        />
      </div>
    );
  }
}

ButtonWithDescription.propTypes = {
  index: PropTypes.number.isRequired,
  char: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  onCharChanged: PropTypes.func.isRequired,
  onDescriptionChange: PropTypes.func.isRequired,
  onDescriptionChanged: PropTypes.func.isRequired
};
