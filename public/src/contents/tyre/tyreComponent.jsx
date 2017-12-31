import React from "react";
import { barToPSI, celsiusToFahrenheit, getAirPressureUnit, getTempUnit } from "../../share/telemetryUtil.js";
import discBrakeIcon from "../../image/disc-brake.png";

const WHITE = "#FEFEFE";
const BLACK = "#0f192a";
const RED = "#C54343";
const GRAY = "#79838D";
const GREEN = "#93C543";
const BLUE = "#B0C1D1";

function createTyreComponent(x, y, width, height, srcTyreWear, tyreTempCelsius, airPressureBar, isCelsius, isBar) {
  const tyreTemp = isCelsius
    ? tyreTempCelsius + getTempUnit(isCelsius)
    : celsiusToFahrenheit(tyreTempCelsius) + getTempUnit(isCelsius);
  const airPressure = isBar
    ? airPressureBar + getAirPressureUnit(isBar)
    : barToPSI(airPressure) + getAirPressureUnit(isBar);
  const tyreWear = Math.min(srcTyreWear * 100, 40);

  const tyreWidth = width * 0.5;
  const tyreHeight = height * 0.6;
  const tyreX = (width - tyreWidth) / 2;
  const tyreY = (height - tyreHeight) / 2;
  const tyreRadius = width * 0.1;

  const tyreLineLeft = tyreX + tyreWidth * 0.3;
  const tyreLineCneter = tyreX + tyreWidth * 0.5;
  const tyreLineRight = tyreX + tyreWidth * 0.7;
  const tyreLineY2 = tyreY + tyreHeight;

  const tyreWearHeight = tyreHeight / 40 * tyreWear;

  const valueWidth = width;
  const valueHeight = (height - tyreHeight) / 4;

  const tyreTempX = x;
  const tyreTempY = y;

  const airPressureX = x;
  const airPressureY = height - valueHeight;

  const tyre = (
    <g>
      <rect x={tyreX} y={tyreY} width={tyreWidth} height={tyreHeight} rx={tyreRadius} ry={tyreRadius} fill={GREEN} />
      <rect x={tyreX} y={tyreY} width={tyreWidth} height={tyreWearHeight} fill={BLACK} />
      <rect
        x={tyreX}
        y={tyreY}
        width={tyreWidth}
        height={tyreHeight}
        rx={tyreRadius}
        ry={tyreRadius}
        stroke={GRAY}
        strokeWidth={1}
        fill="none"
      />
      <line x1={tyreLineLeft} y1={tyreY} x2={tyreLineLeft} y2={tyreLineY2} stroke={GRAY} />
      <line x1={tyreLineCneter} y1={tyreY} x2={tyreLineCneter} y2={tyreLineY2} stroke={GRAY} />
      <line x1={tyreLineRight} y1={tyreY} x2={tyreLineRight} y2={tyreLineY2} stroke={GRAY} />
    </g>
  );

  return <g>{tyre}</g>;
}

/*
 * width: 幅
 * height: 高さ
 * tyreWear: タイヤの摩耗(0.0 - 1.0) * 但し、0.4をMaxとする
 * tyreTempCelsius: タイヤ温度(摂氏)
 * brakeTempCelsius: ブレーキ温度(摂氏)
 * airPressureBar: 空気圧(bar)
 * isCelsius: 温度の単位が摂氏かそうでないか
 * isBar:空気圧の単位がbarかそうでないか
 */
export function createLeftTyreComponent(param) {
  const width = param.width;
  const height = param.height;

  const tyre = createTyreComponent(
    0,
    0,
    width / 2,
    height,
    param.tyreWear,
    param.tyreTemp,
    param.airPressure,
    param.isCelsius,
    param.isBar
  );

  return <g>{tyre}</g>;
}
