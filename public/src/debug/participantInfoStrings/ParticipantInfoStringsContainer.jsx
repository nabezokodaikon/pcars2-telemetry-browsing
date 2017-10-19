import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../../common/jsUtil.js";

class ParticipantInfoStrings extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isJson(this.props.participantInfoStrings)) {
      return <div></div>;
    }

    const data = this.props.participantInfoStrings;

    const createRecords = () => {
      return Object.keys(data).map(valueName => {
        const value = data[valueName];
        if (isArray(value)) {
          return value.map((childValue, index) => {
            const childValueName = valueName + "[" + index + "]";  
            return (
              <tr key={childValueName}>
                <td>{childValueName}</td>
                <td>{childValue}</td>
              </tr>
            );
          }); 
        } else {
          return (
            <tr key={valueName}>
              <td>{valueName}</td>
              <td>{value}</td>
            </tr>
          );
        }
      });
    }

    return (
      <table>
        <tbody>
          {createRecords()}
        </tbody>
      </table>
    );
  }

  render() {
    return this.getData();
  }
}

ParticipantInfoStrings.propTypes = {
  participantInfoStrings: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    participantInfoStrings: state.participantInfoStrings
  };
};

const ParticipantInfoStringsContainer = connect(
  mapStateToProps
)(ParticipantInfoStrings);

export default ParticipantInfoStringsContainer;
