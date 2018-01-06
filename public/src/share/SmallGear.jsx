import React from "react";
import PropTypes from "prop-types";
import { isJson } from "./jsUtil.js";
import { createFanShape, createFanStroke } from "./svgDrawings.jsx";
import style from "./smallContent.css";

const WHITE = "#FFFFFF";
const GRAY = "#AAAAAA";
const BLACK = "#1B1B1B";
const RED = "#C54343";

export default class SmallGear extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <svg className={props.className} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100" />;
    }

    const carState = telemetryData.carState;
    const rpm = carState.rpm;
    const maxRpm = carState.maxRpm;
    const gear = carState.gear;

    const cx = 50;
    const cy = 50;
    const radius = 40;
    const fontSize = 60;
    const width = radius * 0.2;

    const startDegree = 30;
    const finishDegree = 330;
    const bgShape = createFanShape(cx, cy, radius, startDegree, finishDegree, width, BLACK);
    const bgStroke = createFanStroke(cx, cy, radius, startDegree, finishDegree, width, GRAY, width * 0.2);

    if (maxRpm < 1) {
      return (
        <svg className={props.className} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100">
          <g>
            {bgShape}
            {bgStroke}
          </g>
        </svg>
      );
    }

    const gearColor = rpm > maxRpm * 0.97 ? RED : WHITE;
    const rpmBG = maxRpm * 1.2;
    const rpmUnit = (finishDegree - startDegree) / rpmBG;
    const rpmOverStartDegree = finishDegree - (rpmUnit * rpmBG - rpmUnit * maxRpm);
    const rpmOverShape = createFanShape(cx, cy, radius, rpmOverStartDegree, finishDegree, width, RED);
    const rpmValue = rpmUnit * rpm + startDegree;
    const rpmShape = createFanShape(cx, cy, radius, startDegree, rpmValue, width, gearColor);
    const gearText = (
      <text
        x={cx}
        y={cy + fontSize * 0.1}
        fill={gearColor}
        style={{ fontSize: fontSize, fontWeight: "bold", fontFamily: "'Inconsolata', monospace" }}
        textAnchor="middle"
        dominantBaseline="middle"
      >
        {gear}
      </text>
    );

    return (
      <svg className={style.gear} preserveAspectRatio="xMidYMid meet" viewBox="0 0 100 100">
        <g>
          {bgShape}
          {bgStroke}
          {rpmOverShape}
          {rpmShape}
          {gearText}
        </g>
      </svg>
    );
  }
}

SmallGear.propTypes = {
  telemetryData: PropTypes.object.isRequired
};
