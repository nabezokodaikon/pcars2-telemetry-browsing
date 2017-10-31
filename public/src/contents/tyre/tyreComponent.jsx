import React from "react";

const WHITE = "#FEFEFE";
const BLACK = "#1B1B1B";
const RED = "#C54343";
const GRAY = "#79838D";
const GREEN = "#93C543";
const BLUE = "#B0C1D1";

/*
 * width: 幅
 * height: 高さ
 * tyreWear: タイヤの摩耗(0.0 - 1.0) * 但し、0.4をMaxとする
 * tyreTemp: タイヤ温度(摂氏)
 * brakeTempCelsius: ブレーキ温度(摂氏)
 * airPressure: 空気圧(bar)
 * isCelsius: 温度の単位が摂氏かそうでないか
 * isBar:空気圧の単位がbarかそうでないか
 */
export function createLeftTyreComponent(param) {
  const width = param.width;
  const height = param.height;

  return (
    <g>
    </g>
  );
}
