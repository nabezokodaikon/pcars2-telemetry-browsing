import React from "react";
import ReactDom from "react-dom";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { isJson } from "../common/jsUtil.js";

class SimpleProtoType extends React.Component {
  constructor(props) {
    super(props)
  }

  /*
   * cx: 円の中心のx座標
   * cy: 円の中心のy座標
   * radius: 半径
   * startDegree: 扇形の始まりの中心角(度数法)
   * finishDegree: 扇形の終わりの中心角(度数法)
   * width: 扇の幅
   * strokeColor: 線の色
   * strokeWidth: 線の幅
  */
  createFanStroke(cx, cy, radius, startDegree, finishDegree, width, strokeColor, strokeWidth) {
    const startSinX = Math.sin(startDegree / 180 * Math.PI);
    const startCosY = Math.cos(startDegree / 180 * Math.PI);
    const finishSinX = Math.sin(finishDegree / 180 * Math.PI);
    const finishCosY = Math.cos(finishDegree / 180 * Math.PI);

    const innerRadius = radius - width;
    const innerStartX = cx - innerRadius * startSinX;
    const innerStartY = cy + innerRadius * startCosY;
    const innerFinishX = cx - innerRadius * finishSinX;
    const innerFinishY = cy + innerRadius * finishCosY;

    const arcStartX = cx - radius * startSinX;
    const arcStartY = cy + radius * startCosY;
    const arcFinishX = cx - radius * finishSinX;
    const arcFinishY = cy + radius * finishCosY;

    const largeArcFlag = (finishDegree - startDegree <= 180) ? 0 : 1;

    const startLine = "M" + innerStartX + " " + innerStartY + " L " + arcStartX + " " + arcStartY;
    const arc = "A" + radius + " " + radius + " " + 0 + " " + largeArcFlag + " " + 1 + " " + arcFinishX + " " + arcFinishY;
    const finishLine = "L" + innerFinishX + " " + innerFinishY + " ";

    return (
      <path
        d={startLine + " " + arc + " " + finishLine}
        fill="none"
        stroke={strokeColor}
        strokeWidth={strokeWidth}
      />
    );
  }

  getData() {
    if (!isJson(this.props.telemetryData)) {
      return <div></div>;
    }
      
    const stroke = this.createFanStroke(500, 500, 400, 30, 330, 16, "#FF0000", 1);

    return (
      <svg >
        {stroke}
      </svg>
    );
  }

  render() {
    return this.getData();
  }
}

SimpleProtoType.propTypes = {
  telemetryData: PropTypes.object.isRequired
};

const mapStateToProps = state => {
  return {
    telemetryData: state.telemetryData
  };
};

const SimpleProtoTypeContainer = connect(
  mapStateToProps
)(SimpleProtoType);

export default SimpleProtoTypeContainer;
