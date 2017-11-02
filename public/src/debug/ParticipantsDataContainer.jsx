import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../common/jsUtil.js";

class ParticipantsData extends React.Component {
  constructor(props) {
    super(props)
  }

  createRecords() {
    const createParticipantsData = () => {
      const data = this.props.participantsData;
      return Object.keys(data).map(valueName => {
        const value = data[valueName];
        if (isJson(value)) {
          return Object.keys(value).map(key => {
            return (
              <tr key={key}>
                <td>{key}</td>
                <td>{value[key]}</td>
              </tr>
            );
          });
        } else if (isArray(value)) {
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
          <tr>
            <td>ParticipantsData</td>
          </tr>
          {createParticipantsData()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.participantsData)) {
      return <div></div>;
    } else {
      return (
        <div>
          {this.createRecords()}
        </div>
      );
    }
  }
}

ParticipantsData.propTypes = {
  participantsData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    participantsData: state.currentUdpData.participantsData
  };
};

const ParticipantsDataContainer = connect(
  mapStateToProps
)(ParticipantsData);

export default ParticipantsDataContainer;
