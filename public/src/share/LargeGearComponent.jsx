import React from "react";
import PropTypes from "prop-types";
import { isJson } from "./jsUtil.js";
import {
  createFanShape,
  createFanStroke,
  createFanText
} from "./svgComponents.jsx";
import {
  getSpeed,
  getSpeedUnit,
  kmhToMIH
} from "./telemetryUtil.js";
import style from "./largeContent.css";

const WHITE = "#FEFEFE";
const BLACK = "#1B1B1B";
const RED = "#C54343";
const GRAY = "#79838D";
const GREEN = "#93C543";
const BLUE = "#B0C1D1";
const YELLOW = "#C5C543";

export default class LargeGearComponent extends React.Component {
  constructor(props) {
    super(props)
  }

  /*
   * rpm: RPM
   * maxRpm: Max RPM
   * width: 扇の幅
   * fontSize: Font size
   * cx: 円の中心のx座標
   * cy: 円の中心のy座標
   * radius: 半径
  */
  createRpm(rpm, maxRpm, width, fontSize, cx, cy, radius) {
    const rpmBG = maxRpm * 1.2;
    const rpmUnit = 300 / rpmBG;
    const bgWidth = width * 0.75;
    const rpmBGShape = createFanShape(cx, cy, radius, 30, 330, bgWidth, RED);

    if (maxRpm > 0) {
      const rpmValue = rpmUnit * rpm + 30;
      const maxRpmValue = rpmUnit * maxRpm + 30;
      const separateDegree = 300 / rpmBG * 1000;
      const maxRpmShape = createFanShape(cx, cy, radius, 30, maxRpmValue, bgWidth, BLACK);
      const rpmShape = createFanShape(cx, cy, radius, 30, rpmValue, width, WHITE);
      const textRadius = radius * 0.875;
      const rpmText = Array.from({length: Math.floor(rpmBG / 1000) + 1}, (v, k) => k).map(i =>
        createFanText(cx, cy, textRadius, 30 + separateDegree * (i - 0.5), 30 + separateDegree * (i + 0.5), fontSize, BLUE, i.toString())
      );

      return (
        <g>
          {rpmBGShape}
          {maxRpmShape}
          {rpmShape}
          {rpmText}
        </g>
      ); 
    } else {
      return (
        <g>
          {rpmBGShape}
        </g>
      );
    }
  }

  /*
   * srcValue: Cluth
   * width: 扇の幅
   * cx: 円の中心のx座標
   * cy: 円の中心のy座標
   * radius: 半径
  */
  createClutch(srcValue, width, cx, cy, radius) {
    const degree = 90;
    const unit = degree / 255; 
    const bg = 45;
    const value = unit * srcValue + bg;

    const valueShape = createFanShape(cx, cy, radius, bg, value, width, GRAY);

    return (
      <g>
        {valueShape}
      </g>
    );
  }

  /*
   * srcValue: Handbrake
   * width: 扇の幅
   * cx: 円の中心のx座標
   * cy: 円の中心のy座標
   * radius: 半径
  */
  createHandBrake(srcValue, width, cx, cy, radius) {
    const degree = 90;
    const unit = degree / 255; 
    const bg = 315;
    const value = bg - unit * srcValue;

    const valueShape = createFanShape(cx, cy, radius, value, bg, width, YELLOW);

    return (
      <g>
        {valueShape}
      </g>
    );
  }

  /*
   * srcValue: Throttle
   * width: 扇の幅
   * cx: 円の中心のx座標
   * cy: 円の中心のy座標
   * radius: 半径
  */
  createThrottle(srcValue, width, cx, cy, radius) {
    const degree = 150;
    const value = srcValue;
    const unit = degree / 255; 
    const bg = 330;
    const destValue = bg - unit * value;
    const valueShape = createFanShape(cx, cy, radius, destValue, bg, width, GREEN);
    return valueShape;
  }

  /*
   * srcValue: Brake
   * width: 扇の幅
   * cx: 円の中心のx座標
   * cy: 円の中心のy座標
   * radius: 半径
  */
  createBrake(srcValue, width, cx, cy, radius) {
    const degree = 150;
    const value = srcValue;
    const unit = degree / 255; 
    const bg = 30;
    const destValue = unit * value + bg;
    const valueShape = createFanShape(cx, cy, radius, bg, destValue, width, RED);
    return valueShape;
  }

  /*
   * gear: Gear
   * rpm: RPM
   * maxRpm: Max RPM
   * srcSpeed: Speed(KM/H)
   * isMeter: 距離の単位がメートル法かそうでないか
   * gearValueFontSize: ギア値のフォントサイズ
   * width: 扇の幅
   * cx: 円の中心のx座標
   * cy: 円の中心のy座標
   * radius: 半径
  */
  createGear(
    gear, rpm, maxRpm, srcSpeed, isMeter, gearValueFontSize, width, cx, cy, radius) {
    const speed = getSpeed(srcSpeed, isMeter); 
    const speedUnit = getSpeedUnit(isMeter);
    const gearColor = (rpm > maxRpm * 0.97 ? RED : WHITE);
    const rpmValueFontSize = gearValueFontSize * 0.3;
    const rpmUnitFontSize = gearValueFontSize * 0.2;
    const speedFontSize = gearValueFontSize * 0.75;
    const speedUnitFontSize = gearValueFontSize * 0.3;

    const frameShape = createFanShape(cx, cy, radius * 0.5, 30, 330, width, gearColor);

    const gearText =
      <text
        x={cx}
        y={cy}
        fill={gearColor}
        style={{fontSize: gearValueFontSize, fontFamily: "'Inconsolata', monospace"}}
        textAnchor="middle"
        dominantBaseline="middle"
      >
        {gear}
      </text>

    const rpmValueText =
      <text
        x={cx}
        y={cy * 1.275}
        fill={BLUE}
        style={{fontSize: rpmValueFontSize, fontFamily: "'Inconsolata', monospace"}}
        textAnchor="middle"
        dominantBaseline="middle"
      >
        {rpm}
      </text>

    const rpmUnitText =
      <text
        x={cx}
        y={cy * 1.375}
        fill={BLUE}
        style={{fontSize: rpmUnitFontSize, fontFamily: "'Inconsolata', monospace"}}
        textAnchor="middle"
        dominantBaseline="middle"
      >
        rpm
      </text>

    const speedValueText =
      <text
        x={cx}
        y={cy * 1.7}
        fill={WHITE}
        style={{fontSize: speedFontSize, fontFamily: "'Inconsolata', monospace"}}
        textAnchor="middle"
        dominantBaseline="middle"
      >
        {speed}
      </text>

    const speedUnitText =
      <text
        x={cx}
        y={cy * 1.92}
        fill={WHITE}
        style={{fontSize: speedUnitFontSize, fontFamily: "'Inconsolata', monospace"}}
        textAnchor="middle"
        dominantBaseline="middle"
      >
        {speedUnit}
      </text>

    return (
      <g>
        {frameShape}
        {gearText}
        {rpmValueText}
        {rpmUnitText}
        {speedValueText}
        {speedUnitText}
      </g>
    );
  }

  render() {
    const props = this.props;
    const telemetryData = props.telemetryData;
    if (!isJson(telemetryData)) {
      return <div></div>;
    }

    const carState = telemetryData.carState; 
    const tyre3 = telemetryData.tyre3;

    const cx = 50;
    const cy = 50;
    const radius = 50;
    const width = radius * 0.025;
    const throttoleAndBrakeRadius = radius * 0.75;

    const gear = this.createGear(
      carState.gear, carState.rpm, carState.maxRpm, carState.speed, props.isMeter,
      radius * 0.6, width, cx, cy, radius);

    const rpm = this.createRpm(
      carState.rpm, carState.maxRpm,
      width, radius * 0.15, cx, cy, radius);

    const throttle = this.createThrottle(
      carState.throttle,
      width, cx, cy, throttoleAndBrakeRadius);

    const brake = this.createBrake(
      carState.brake,
      width, cx, cy, throttoleAndBrakeRadius);

    const clutch = this.createClutch(
      carState.clutch,
      width, cx, cy, radius * 0.65);
    
    const handBrake = this.createHandBrake(
      tyre3.handBrake,
      width, cx, cy, radius * 0.65);

    return (
      <svg className={style.gear} preserveAspectRatio="xMidYMin meet" viewBox="0 0 100 100">
        <g>
          {gear}
          {rpm}
          {throttle}
          {brake}
          {clutch}
          {handBrake}
        </g>
      </svg>
    );
  }
}

LargeGearComponent.propTypes = {
  isMeter: PropTypes.bool.isRequired,
  telemetryData: PropTypes.object.isRequired
};
