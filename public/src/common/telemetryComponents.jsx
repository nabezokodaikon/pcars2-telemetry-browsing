import React from "react";
import {
  createFanShape,
  createFanStroke,
  createFanText
} from "./svgComponents.jsx"

/*
 * gear: Gear
 * rpm: RPM
 * speed: Speed
 * speedUnit: Speed unit
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
*/
export function createGearComponent(gear, rpm, speed, speedUnit, cx, cy, radius, width) {
  const frameShape = createFanShape(cx, cy, radius, 30, 330, width, "#FFFFFF");

  const gearText =
    <text
      x={cx}
      y={cy - 20}
      fill="#FFFFFF"
      style={{fontSize: "14rem"}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {gear}
    </text>

  const rpmValueText =
    <text
      x={cx}
      y={cy + 80}
      fill="#7777FF"
      style={{fontSize: "4rem"}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {rpm}
    </text>

  const rpmUnitText =
    <text
      x={cx}
      y={cy + 120}
      fill="#7777FF"
      style={{fontSize: "3rem"}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      rpm
    </text>

  const speedValueText =
    <text
      x={cx}
      y={cy + 200}
      fill="#FFFFFF"
      style={{fontSize: "6rem"}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {speed}
    </text>

  const speedUnitText =
    <text
      x={cx}
      y={cy + 260}
      fill="#FFFFFF"
      style={{fontSize: "3rem"}}
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
 * rpm: RPM
 * maxRpm: Max RPM
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
*/
export function createRpmComponent(rpm, maxRpm, cx, cy, radius, width) {
  const fontSize = width * 4;
  const rpmBG = maxRpm * 1.2;
  const rpmUnit = 300 / rpmBG;
  const rpmValue = rpmUnit * rpm + 30;
  const maxRpmValue = rpmUnit * maxRpm + 30;
  const separateDegree = 300 / rpmBG * 1000;

  const rpmBGShape = createFanShape(cx, cy, radius, 30, 330, width, "#FF0000");
  const maxRpmShape = createFanShape(cx, cy, radius, 30, maxRpmValue, width, "#000000");
  const rpmShape = createFanShape(cx, cy, radius, 30, rpmValue, width * 1.5, "#FFFFFF");

  const rpmText = Array.from({length: Math.floor(rpmBG / 1000) + 1}, (v, k) => k).map(i =>
    createFanText(cx, cy, radius - fontSize, 30 + separateDegree * (i - 0.5), 30 + separateDegree * (i + 0.5), fontSize, "#7777FF", i.toString())
  );

  return (
    <g>
      {rpmBGShape}
      {maxRpmShape}
      {rpmShape}
      {rpmText}
    </g>
  ); 
}

/*
 * srcValue: Cluth
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
*/
export function createClutchComponent(srcValue, cx, cy, radius, width) {
  const degree = 120;
  const value = srcValue * 100;
  const unit = degree / 100; 
  const lBG = 30;
  const rBG = 330;
  const lValue = unit * value + lBG;
  const rValue = rBG - unit * value;

  const lBGStroke = createFanStroke(cx, cy, radius, lBG, lBG + degree, width, "#777777", 1);
  const rBGStroke = createFanStroke(cx, cy, radius, rBG - degree, rBG, width, "#777777", 1);
  const lValueShape = createFanShape(cx, cy, radius, lBG, lValue, width, "#777777");
  const rValueShape = createFanShape(cx, cy, radius, rValue, rBG, width, "#777777");

  return (
    <g>
      {lBGStroke}
      {rBGStroke}
      {lValueShape}
      {rValueShape}
    </g>
  );
}

/*
 * srcValue: Throttle
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
*/
export function createThrottleComponent(srcValue, cx, cy, radius, width) {
  const degree = 120;
  const value = srcValue * 100;
  const unit = degree / 100; 
  const bg = 330;
  const destValue = bg - unit * value;

  const bgStroke = createFanStroke(cx, cy, radius, bg - degree, bg, width, "#00FF00", 1);
  const valueShape = createFanShape(cx, cy, radius, destValue, bg, width, "#00FF00");

  return (
    <g>
      {bgStroke}
      {valueShape}
    </g>
  );
}

/*
 * srcValue: brake
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
*/
export function createBrakeComponent(srcValue, cx, cy, radius, width) {
  const degree = 120;
  const value = srcValue * 100;
  const unit = degree / 100; 
  const bg = 30;
  const destValue = unit * value + bg;

  const bgStroke = createFanStroke(cx, cy, radius, bg, bg + degree, width, "#FF0000", 1);
  const valueShape = createFanShape(cx, cy, radius, bg, destValue, width, "#FF0000");

  return (
    <g>
      {bgStroke}
      {valueShape}
    </g>
  );
}
