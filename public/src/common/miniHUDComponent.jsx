import React from "react";
import {
  createFanShape,
  createFanStroke
} from "./svgComponents.jsx";
import {
  getSpeed,
  getSpeedUnit,
  kmhToMIH
} from "./telemetryUtil.js";

const WHITE = "#FFFFFF";
const BLACK = "#1B1B1B";
const RED = "#C54343";
const BLUE = "#BFD2E3";

function createSpeedComponent(x, y, width, height, speedTextFontSize, srcSpeed, isMeter) {
  const speed = getSpeed(srcSpeed, isMeter); 
  const unit = getSpeedUnit(isMeter);
  const radius = height / 2 * 0.9;
  const cx = x + radius;
  const cy = y + radius;
  const speedTextX = x + radius * 2;
  const speedTextY = y + radius * 1.1;
  const unitTextFontSize = speedTextFontSize * 0.5;
  const unitTextX = speedTextX + width * 0.025;
  const unitTextY = y + (height - speedTextFontSize);

  const speedText =
    <text
      x={speedTextX}
      y={speedTextY}
      fill={WHITE}
      style={{fontSize: speedTextFontSize, fontFamily: "'Inconsolata', monospace"}}
      textAnchor="end"
      dominantBaseline="middle"
    >
      {speed}
    </text>;

  const unitText =
    <text
      x={unitTextX}
      y={unitTextY}
      fill={WHITE}
      style={{fontSize: unitTextFontSize, fontFamily: "'Inconsolata', monospace"}}
      textAnchor="start"
      dominantBaseline="middle"
    >
      {unit}
    </text>;

  return (
    <g>
      {speedText}
      {unitText}
    </g>
  );
}

function createGearComponent(x, y, width, height, fontSize, gear, rpm, maxRpm) {
  const radius = height / 2 * 0.9;
  const cx = x + radius;
  const cy = y + radius;
  const textY = y + radius * 1.1;
  const rpmBarWidth = width * 0.075;
  const rpmBGValue = 300;

  const rpmBGShape = createFanShape(cx, cy, radius, 30, rpmBGValue + 30, rpmBarWidth, RED);

  if (maxRpm <= 0) {
    return (
      <g>
        {rpmBGShape}
      </g>
    );
  }

  const rpmBG = maxRpm * 1.2;
  const rpmUnit = rpmBGValue / rpmBG;
  const maxRpmValue = rpmUnit * maxRpm + 30;
  const rpmValue = rpmUnit * rpm + 30;

  const maxRpmShape = createFanShape(cx, cy, radius, 30, maxRpmValue, rpmBarWidth * 0.9, BLACK);
  const rpmShape = createFanShape(cx, cy, radius, 30, rpmValue, rpmBarWidth, WHITE);

  const gearText =
    <text
      x={cx}
      y={textY}
      fill={WHITE}
      style={{fontSize: fontSize, fontFamily: "'Inconsolata', monospace"}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {gear}
    </text>;

  return (
    <g>
      {rpmBGShape}
      {maxRpmShape}
      {rpmShape}
      {gearText}
    </g>
  );
}

function createFuelComponent(fuelRemaining) {
}

/*
 * gear: carState.gear
 * speed: carState.speed
 * rpm: carState.rpm
 * maxRpm: carState.maxRpm
 * fuelRemaining: carState.fuelRemaining
 * isMeter: 距離の単位がメートル法かそうでないか
 */
export function createMiniHUDComponent(param) {
  const speedX = 40;
  const speedY = 10;
  const speedWidth = 125;
  const speedHeight = 80;
  const speedFontSize = 40;

  const gearX = speedX + speedWidth;
  const gearY = speedY;
  const gearWidth = 80;
  const gearHeight = speedHeight;
  const gearFontSize = 40; 

  const speedComponent = createSpeedComponent(
    speedX, speedY, speedWidth, speedHeight, speedFontSize,
    param.speed, param.isMeter);

  const gearComponent = createGearComponent(
    gearX, gearY, gearWidth, gearHeight, gearFontSize,
    param.gear, param.rpm, param.maxRpm);

  return (
    <g>
      <rect x="0" y="0" width="2000" height="100" fill="blue" fillOpacity="0.5" /> 
      {speedComponent}
      {gearComponent}
    </g>
  );

}
