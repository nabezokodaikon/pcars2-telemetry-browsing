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

function createGearComponent(
  gear, rpm, maxRpm, srcSpeed, isMeter, x, y, width, height) {
  const cx = width / 2;
  const cy = height / 2;
  const fontSize = 100;

  const gearText =
    <text
      x={cx}
      y={cy}
      fill={WHITE}
      style={{fontSize: fontSize, fontFamily: "'Inconsolata', monospace"}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {gear}
    </text>;

  return (
    <g>
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
  const gearX = 0;
  const gearY = 0;
  const gearWidth = 100;
  const gearHeight = 80;

  const gearComponent = createGearComponent(
    param.gear, param.rpm, param.maxRpm, param.speed, param.isMeter,
    gearX, gearY, gearWidth, gearHeight);

  return (
    <g>
      {gearComponent}
    </g>
  );

}
