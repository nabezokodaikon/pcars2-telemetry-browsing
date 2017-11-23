import React from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../share/jsUtil.js";

class ParticipantVehicleNamesData extends React.Component {
  constructor(props) {
    super(props)
  }

  createRecords() {
    const createBase = () => {
      const data = this.props.participantVehicleNamesData.base;
      return Object.keys(data).map(key => {
        const value = data[key];
        return (
          <tr key={key}>
            <td>{key}</td>
            <td>{value}</td>
          </tr>
        );
      });
    }

    const createParticipantVehicleNamesData = () => {
      const data = this.props.participantVehicleNamesData;
      return Object.keys(data).filter(valueName => valueName !== "base" && !isArray(data[valueName])).map(valueName => {
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

    const createHeader = () => {
      const data = this.props.participantVehicleNamesData.vehicles;
      const firstData = data[0];
      return Object.keys(firstData).map(key => {
        const value = firstData[key];
        if (isArray(value)) {
          return value.map((childValue, index) => {
            const indexKey = key + "[" + index + "]";
            return (
              <td key={indexKey}>{indexKey}</td>
            );
          });
        } else {
          return (<td key={key}>{key}</td>);
        }
      });
    };

    const createValueFields = (value, key) => {
      if (isArray(value)) {
        return value.map((childValue, index) => {
          const indexKey = key + "[" + index + "]";
          return (
            <td key={indexKey}>{childValue}</td>
          );
        });
      } else {
        return <td key={key}>{value}</td>;
      }
    };

    const createRecords = () => {
      const data = this.props.participantVehicleNamesData.vehicles;
      return data.map((value, index) => {
        return (
          <tr key={index}>
            <td key={index}>{index}</td>
            {Object.keys(value).map(key => createValueFields(value[key], key))}
          </tr>
        );
      });
    };


    return (
      <table>
        <tbody>
          <tr>
            <td>PacketBase</td>
          </tr>
          {createBase()}
          <tr>
            <td>ParticipantVehicleNamesData</td>
          </tr>
          {createParticipantVehicleNamesData()}
          <tr>
            <td>vehicles</td>
          </tr>
          <tr>
            <td>index</td>
            {createHeader()}
          </tr>
          {createRecords()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.participantVehicleNamesData)) {
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

ParticipantVehicleNamesData.propTypes = {
  participantVehicleNamesData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    participantVehicleNamesData: state.currentUdpData.participantVehicleNamesData
  };
};

const ParticipantVehicleNamesDataContainer = connect(
  mapStateToProps
)(ParticipantVehicleNamesData);

export default ParticipantVehicleNamesDataContainer;
