import React from "react";
import {
  createFanShape,
  createFanStroke,
  createFanText
} from "./svgComponents.jsx"

/*
 * rpm: RPM
 * maxRpm: Max RPM
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
*/
export function createRpmComponent(rpm, maxRpm, cx, cy, radius, width) {
  const fontSize = width * 2;
  const rpmBG = maxRpm * 1.2;
  const rpmUnit = 300 / rpmBG;
  const rpmValue = rpmUnit * rpm + 30;
  const maxRpmValue = rpmUnit * maxRpm + 30;
  const separateDegree = 300 / rpmBG * 1000;

  const rpmBGShape = createFanShape(cx, cy, radius, 30, 330, width, "#FF0000");
  const maxRpmShape = createFanShape(cx, cy, radius, 30, maxRpmValue, width, "#000000");
  const rpmShape = createFanShape(cx, cy, radius, 30, rpmValue, width * 1.5, "#FFFFFF");

  const rpmText = Array.from({length: Math.floor(rpmBG / 1000) + 1}, (v, k) => k).map(i =>
    createFanText(cx, cy, radius - fontSize * 2, 30 + separateDegree * (i - 0.5), 30 + separateDegree * (i + 0.5), fontSize, "#5555AA", i.toString())
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
  const degree = 60;
  const value = parseFloat(srcValue) * 100;
  const unit = degree / 100; 
  const lBG = degree;
  const rBG = 300;
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
  const degree = 60;
  const value = parseFloat(srcValue) * 100;
  const unit = degree / 100; 
  const bg = degree;
  const destValue = unit * value + bg;

  const bgStroke = createFanStroke(cx, cy, radius, bg, bg + degree, width, "#777777", 1);
  const bgValueShape = createFanShape(cx, cy, radius, bg, destValue, width, "#007700");

  return (
    <g>
      {bgStroke}
      {bgValueShape}
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
  const degree = 60;
  const value = parseFloat(srcValue) * 100;
  const unit = degree / 100; 
  const bg = 360 - degree;
  const destValue = bg - unit * value;

  const bgStroke = createFanStroke(cx, cy, radius, bg - degree, bg, width, "#777777", 1);
  const valueShape = createFanShape(cx, cy, radius, destValue, bg, width, "#770000");

  return (
    <g>
      {bgStroke}
      {valueShape}
    </g>
  );
}

