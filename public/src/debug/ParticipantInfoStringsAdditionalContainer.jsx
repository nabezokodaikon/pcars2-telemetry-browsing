import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../common/jsUtil.js";

class ParticipantInfoStringsAdditional extends React.Component {
  constructor(props) {
    super(props)
  }

  getData() {
    if (!isJson(this.props.participantInfoStringsAdditional)) {
      return <div></div>;
    }

    const createRecords = () => {
      const data = this.props.participantInfoStringsAdditional;

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

ParticipantInfoStringsAdditional.propTypes = {
  participantInfoStringsAdditional: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    participantInfoStringsAdditional: state.participantInfoStringsAdditional
  };
};

const ParticipantInfoStringsAdditionalContainer = connect(
  mapStateToProps
)(ParticipantInfoStringsAdditional);

export default ParticipantInfoStringsAdditionalContainer;
