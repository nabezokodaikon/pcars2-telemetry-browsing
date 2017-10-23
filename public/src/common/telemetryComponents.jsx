import React from "react";
import {
  createFanShape,
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
  const rpmValue = 300 / maxRpm * rpm + 30;
  const separateDegree = 300 / maxRpm * 1000;
  const maxRpmShape = createFanShape(cx, cy, radius, 30, 330, width, "#FF0000");
  const rpmShape = createFanShape(cx, cy, radius, 30, rpmValue, width * 1.5, "#FFFFFF");
  const rpmText = Array.from({length: Math.floor(maxRpm / 1000) + 1}, (v, k) => k).map(i =>
    createFanText(cx, cy, radius - fontSize * 2, 30 + separateDegree * (i - 0.5), 30 + separateDegree * (i + 0.5), fontSize, "#5555AA", i.toString())
  );
  return (
    <g>
      {maxRpmShape}
      {rpmShape}
      {rpmText}
    </g>
  ); 
}

