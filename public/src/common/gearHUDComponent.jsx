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
  const degree = 60;
  const value = srcValue * 100;
  const unit = degree / 100; 
  const lBG = 60;
  const rBG = 300;
  const lValue = unit * value + lBG;
  const rValue = rBG - unit * value;

  const lValueShape = createFanShape(cx, cy, radius, lBG, lValue, width, GRAY);
  const rValueShape = createFanShape(cx, cy, radius, rValue, rBG, width, GRAY);

  return (
    <g>
      {lValueShape}
      {rValueShape}
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
  const value = srcValue * 100;
  const unit = degree / 100; 
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
  const value = srcValue * 100;
  const unit = degree / 100; 
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
  const gearColor = (rpm > maxRpm * 0.99 ? RED : WHITE);
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
 * gear: carStateData.gear
 * speed: carStateData.speed
 * rpm: carStateData.rpm
 * maxRpm: carStateData.maxRpm
 * throttle: carStateData.throttle
 * brake: carStateData.brake
 * clutch: carStateData.clutch
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

  return (
    <g>
      {gearComponent}
      {rpmComponent}
      {throttleComponent}
      {brakeComponent}
      {clutchComponent}
    </g>
  );
}
