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
  const rpmBackground = maxRpm * 1.2;
  const rpmUnit = 300 / rpmBackground;
  const rpmValue = rpmUnit * rpm + 30;
  const maxRpmValue = rpmUnit * maxRpm + 30;
  const separateDegree = 300 / rpmBackground * 1000;

  const rpmBackgroundShape = createFanShape(cx, cy, radius, 30, 330, width, "#FF0000");
  const maxRpmShape = createFanShape(cx, cy, radius, 30, maxRpmValue, width, "#000000");
  const rpmShape = createFanShape(cx, cy, radius, 30, rpmValue, width * 1.5, "#FFFFFF");

  const rpmText = Array.from({length: Math.floor(rpmBackground / 1000) + 1}, (v, k) => k).map(i =>
    createFanText(cx, cy, radius - fontSize * 2, 30 + separateDegree * (i - 0.5), 30 + separateDegree * (i + 0.5), fontSize, "#5555AA", i.toString())
  );

  return (
    <g>
      {rpmBackgroundShape}
      {maxRpmShape}
      {rpmShape}
      {rpmText}
    </g>
  ); 
}

export function createClutchComponent(srcValue, cx, cy, radius, width) {
  const value = parseFloat(srcValue) * 100;
  const unit = 60 / 100; 
  const lBG = 60;
  const rBG = 300;
  const lValue = unit * value + lBG;
  const rValue = rBG - unit * value;

  const lBGStroke = createFanStroke(cx, cy, radius, lBG, lBG + 60, width, "#777777", 1);
  const rBGStroke = createFanStroke(cx, cy, radius, rBG - 60, rBG, width, "#777777", 1);
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
