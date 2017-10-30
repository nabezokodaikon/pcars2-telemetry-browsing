import React from "react";
import {
  createFanShape,
  createFanStroke,
  createFanText
} from "./svgComponents.jsx";
import {
  kmhToMIH
} from "./telemetryUtil.js";
import gasolineIcon from "../image/gasoline.png";
import oilIcon from "../image/oil.png";

/*
 * rpm: RPM
 * maxRpm: Max RPM
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
 * fontSize: Font size
*/
function createRpmComponent(rpm, maxRpm, cx, cy, radius, width, fontSize) {
  const rpmBG = maxRpm * 1.2;
  const rpmUnit = 300 / rpmBG;
  const rpmValue = rpmUnit * rpm + 30;
  const maxRpmValue = rpmUnit * maxRpm + 30;
  const separateDegree = 300 / rpmBG * 1000;
  const bgWidth = width * 0.5;

  const rpmBGShape = createFanShape(cx, cy, radius, 30, 330, bgWidth, "#FF0000");
  const maxRpmShape = createFanShape(cx, cy, radius, 30, maxRpmValue, bgWidth, "#000000");
  const rpmShape = createFanShape(cx, cy, radius, 30, rpmValue, width, "#FFFFFF");

  const textRadius = radius * 0.875;
  const rpmText = Array.from({length: Math.floor(rpmBG / 1000) + 1}, (v, k) => k).map(i =>
    createFanText(cx, cy, textRadius, 30 + separateDegree * (i - 0.5), 30 + separateDegree * (i + 0.5), fontSize, "#7777FF", i.toString())
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
function createClutchComponent(srcValue, cx, cy, radius, width) {
  const degree = 60;
  const value = srcValue * 100;
  const unit = degree / 100; 
  const lBG = 60;
  const rBG = 300;
  const lValue = unit * value + lBG;
  const rValue = rBG - unit * value;

  const lValueShape = createFanShape(cx, cy, radius, lBG, lValue, width, "#777777");
  const rValueShape = createFanShape(cx, cy, radius, rValue, rBG, width, "#777777");

  return (
    <g>
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
function createThrottleComponent(srcValue, cx, cy, radius, width) {
  const degree = 150;
  const value = srcValue * 100;
  const unit = degree / 100; 
  const bg = 330;
  const destValue = bg - unit * value;
  const valueShape = createFanShape(cx, cy, radius, destValue, bg, width, "#00FF00");
  return valueShape;
}

/*
 * srcValue: Brake
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
*/
function createBrakeComponent(srcValue, cx, cy, radius, width) {
  const degree = 150;
  const value = srcValue * 100;
  const unit = degree / 100; 
  const bg = 30;
  const destValue = unit * value + bg;
  const valueShape = createFanShape(cx, cy, radius, bg, destValue, width, "#FF0000");
  return valueShape;
}

/*
 * value: Fuel level
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * iconSize: アイコンサイズ
 * fontSize: フォントサイズ
*/
function createFuelLevelComponent(value, cx, cy, iconSize, fontSize) {
  const x = cx - 235;
  const y = cy - 64;

  const icon =
    <image
      x={x}
      y={y}
      width={iconSize}
      height={iconSize}
      xlinkHref={gasolineIcon}
    />;

  const valueText =
    <text
      x={x + iconSize * 0.5}
      y={y + iconSize * 1.4}
      fill="#FFFFFF"
      style={{fontSize: fontSize}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {value}
    </text>;

  return (
    <g>
      {icon} 
      {valueText}
    </g>
  );
}

/*
 * gear: Gear
 * rpm: RPM
 * maxRpm: Max RPM
 * srcSpeed: Speed(KM/H)
 * isMeter: 距離の単位がメートル法かそうでないか
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * radius: 半径
 * width: 扇の幅
 * gearValueFontSize: ギア値のフォントサイズ
*/
function createGearComponent(gear, rpm, maxRpm, srcSpeed, isMeter, cx, cy, radius, width, gearValueFontSize) {
  const speed = (isMeter ? Math.floor(srcSpeed) : KMHToMIH(srcSpeed)); 
  const speedUnit = (isMeter ? "KM/H" : "MI/H");
  const gearColor = (rpm > maxRpm * 0.99 ? "#FF0000" : "#FFFFFF");
  const rpmValueFontSize = gearValueFontSize * 0.3;
  const rpmUnitFontSize = gearValueFontSize * 0.2;
  const speedUnitFontSize = gearValueFontSize * 0.4;

  const frameShape = createFanShape(cx, cy, radius, 30, 330, width, gearColor);

  const gearText =
    <text
      x={cx}
      y={cy - 10}
      fill={gearColor}
      style={{fontSize: gearValueFontSize}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {gear}
    </text>

  const rpmValueText =
    <text
      x={cx}
      y={cy + 90}
      fill="#7777FF"
      style={{fontSize: rpmValueFontSize}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {rpm}
    </text>

  const rpmUnitText =
    <text
      x={cx}
      y={cy + 130}
      fill="#7777FF"
      style={{fontSize: rpmUnitFontSize}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      rpm
    </text>

  const speedValueText =
    <text
      x={cx}
      y={cy + 280}
      fill="#FFFFFF"
      style={{fontSize: gearValueFontSize}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {speed}
    </text>

  const speedUnitText =
    <text
      x={cx}
      y={cy + 400}
      fill="#FFFFFF"
      style={{fontSize: speedUnitFontSize}}
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
 * srcValue: Oil temperature
 * isCelsius: 温度の単位が摂氏かそうでないか
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * iconSize: アイコンサイズ
 * fontSize: フォントサイズ
*/
function createOilTempComponent(srcValue, isCelsius, cx, cy, iconSize, fontSize) {
  const x = cx - 40;
  const y = cy - 300;
  const unit = (isCelsius ? "°C" : "ºF");
  const value = (isCelsius ? Math.floor(srcValue) : CelsiusToFahrenheit(srcValue)); 
  const text = value + unit;

  const icon =
    <image
      x={x}
      y={y}
      width={iconSize}
      height={iconSize}
      xlinkHref={oilIcon}
    />;

  const valueText =
    <text
      x={x + iconSize * 0.5}
      y={y + iconSize * 1.4}
      fill="#FFFFFF"
      style={{fontSize: fontSize}}
      textAnchor="middle"
      dominantBaseline="middle"
    >
      {text}
    </text>;

  return (
    <g>
      {icon} 
      {valueText}
    </g>
  );
}

/*
 * cx: 円の中心のx座標
 * cy: 円の中心のy座標
 * gear: carStateData.gear
 * speed: carStateData.speed
 * rpm: carStateData.rpm
 * maxRpm: carStateData.maxRpm
 * throttle: carStateData.throttle
 * brake: carStateData.brake
 * clutch: carStateData.clutch
 * oilTempCelsius: carStateData.oilTempCelsius
 * fuelCapacity: carStateData.fuelLevel
 * isCelsius: 温度の単位が摂氏かそうでないか
 * isMeter: 距離の単位がメートル法かそうでないか
*/
export function createGearHUDComponent(param) {
  const radius = 400;
  const cx = param.cx;
  const cy = param.cy;

  const rpmComponent = createRpmComponent(param.rpm, param.maxRpm, cx, cy, radius, 24, 48);
  const throttleComponent = createThrottleComponent(param.throttle, cx, cy, radius * 0.8, 16);
  const brakeComponent = createBrakeComponent(param.brake, cx, cy, radius * 0.8, 16);
  const clutchComponent = createClutchComponent(param.clutch, cx, cy, radius * 0.7, 16);
  const fuelLevelComponent = createFuelLevelComponent(param.fuelLevel, cx, cy, 80, 48);
  const gearComponent = createGearComponent(
    param.gear, param.rpm, param.maxRpm, param.speed, param.isMeter, cx, cy, radius * 0.35, 8, 220);
  const oilTempComponent = createOilTempComponent(param.oilTempCelsius, param.isCelsius, cx, cy, 80, 48);

  return (
    <g>
      {rpmComponent}
      {throttleComponent}
      {brakeComponent}
      {clutchComponent}
      {fuelLevelComponent}
      {oilTempComponent}
      {gearComponent}
    </g>
  );
}
