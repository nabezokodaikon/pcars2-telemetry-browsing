import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isArray, isJson } from "../common/jsUtil.js";

class VehicleClassNamesData extends React.Component {
  constructor(props) {
    super(props)
  }

  createRecords() {
    const createBase = () => {
      const data = this.props.vehicleClassNamesData.base;
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

    const createVehicleClassNamesData = () => {
      const data = this.props.vehicleClassNamesData;
      return Object.keys(data).filter(key => key !== "base" && !isArray(data[key])).map(key => {
        const value = data[key];
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
            <tr key={key}>
              <td>{key}</td>
              <td>{value}</td>
            </tr>
          );
        }
      });
    }

    const createClassesField = (value, key) => {
      return <td key={key}>{value}</td>;
    };

    const createClasses = () => {
      const data = this.props.vehicleClassNamesData.classes;
      return data.map((value, index) => {
        const valueName = "classes[" + index + "]";
        return (
          <tr key={index}>
            <td key={index}>{valueName}</td>
            {Object.keys(value).map(key => createClassesField(value[key], key))}
          </tr>
        );
      }); 
    }

    return (
      <table>
        <tbody>
          <tr>
            <td>PacketBase</td>
          </tr>
          {createBase()}
          <tr>
            <td>VehicleClassNamesData</td>
          </tr>
          {createVehicleClassNamesData()}
          {createClasses()}
        </tbody>
      </table>
    );
  }

  render() {
    if (!isJson(this.props.vehicleClassNamesData)) {
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

VehicleClassNamesData.propTypes = {
  vehicleClassNamesData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    vehicleClassNamesData: state.currentUdpData.vehicleClassNamesData
  };
};

const VehicleClassNamesDataContainer = connect(
  mapStateToProps
)(VehicleClassNamesData);

export default VehicleClassNamesDataContainer;
