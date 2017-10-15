import { connect } from "react-redux";
import CarStateData from "./CarStateData.jsx";
// import { open, fetchReceived } from "../../appAction.js"

const mapStateToProps = state => {
  console.log("mapStateToProps");
  return {
    telemetry: state.telemetry
  };
};

// const mapDispatchToProps = dispatch => {
  // console.log("mapDispatchToProps");
  // return {
    // onOpen: ws => {
      // dispatch(fetchReceived(ws));
    // }
  // }
// };

const CarStateDataContainer = connect(
  mapStateToProps
)(CarStateData);

export default CarStateDataContainer;
