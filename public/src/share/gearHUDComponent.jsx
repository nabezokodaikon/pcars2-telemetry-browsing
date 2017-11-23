import React from "react";
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

const WHITE = "#FEFEFE";
const BLACK = "#1B1B1B";
const RED = "#C54343";
const GRAY = "#79838D";
const GREEN = "#93C543";
const BLUE = "#B0C1D1";
const YELLOW = "#C5C543";

/*
 * rpm: RPM
 * maxRpm: Max RPM
 * width: 扇の幅
 * fontSize: Font size
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
*/
function createRpmComponent(rpm, maxRpm, width, fontSize, cx, cy, radius) {
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
function createClutchComponent(srcValue, width, cx, cy, radius) {
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
function createHandBrakeComponent(srcValue, width, cx, cy, radius) {
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
function createThrottleComponent(srcValue, width, cx, cy, radius) {
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
function createBrakeComponent(srcValue, width, cx, cy, radius) {
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
function createGearComponent(
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

/*
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * gear: carState.gear
 * speed: carState.speed
 * rpm: carState.rpm
 * maxRpm: carState.maxRpm
 * throttle: carState.throttle
 * brake: carState.brake
 * clutch: carState.clutch
 * handBrake: tyre3.handBrake
 * isMeter: 距離の単位がメートル法かそうでないか
*/
export function createGearHUDComponent(param) {
  const cx = param.cx;
  const cy = param.cy;
  const radius = param.radius;
  const width = radius * 0.025;
  const throttoleAndBrakeRadius = radius * 0.75;

  const gearComponent = createGearComponent(
    param.gear, param.rpm, param.maxRpm, param.speed, param.isMeter,
    radius * 0.6, width, cx, cy, radius);
  const rpmComponent = createRpmComponent(
    param.rpm, param.maxRpm, width, radius * 0.15, cx, cy, radius);
  const throttleComponent = createThrottleComponent(
    param.throttle, width, cx, cy, throttoleAndBrakeRadius);
  const brakeComponent = createBrakeComponent(
    param.brake, width, cx, cy, throttoleAndBrakeRadius);
  const clutchComponent = createClutchComponent(
    param.clutch, width, cx, cy, radius * 0.65);
  const handBrakeComponent = createHandBrakeComponent(
    param.handBrake, width, cx, cy, radius * 0.65);

  return (
    <g>
      {gearComponent}
      {rpmComponent}
      {throttleComponent}
      {brakeComponent}
      {clutchComponent}
      {handBrakeComponent}
    </g>
  );
}
