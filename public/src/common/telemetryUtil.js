import { isJson, existsKey } from "./jsUtil.js";
import * as telemetryConst from "./telemetryConst.js";

export function isTelemetryDataFrameType(telemetry) {
  return isJson(telemetry)
    && existsKey(telemetry, "frameType")
    && telemetry.frameType == telemetryConst.TELEMETRY_DATA_FRAME_TYPE; 
}

export function isParticipantInfoStringsFrameType(telemetry) {
  return isJson(telemetry)
    && existsKey(telemetry, "frameType")
    && telemetry.frameType == telemetryConst.PARTICIPANT_INFO_STRINGS_FRAME_TYPE; 
}

export function isParticipantInfoStringsAdditionalFrameType(telemetry) {
  return isJson(telemetry)
    && existsKey(telemetry, "frameType")
    && telemetry.frameType == telemetryConst.PARTICIPANT_INFO_STRINGS_ADDITIONAL_FRAME_TYPE; 
}

export function kmhToMIH(kmh) {
  return Math.floor(kmh * 0.625);
}

export function celsiusToFahrenheit(celsius) {
  return Math.floor(celsius * 1.8) + 32;
}

export function barToPSI(bar) {
  return bar * 14.5038;
}

export function getTempUnit(isCelsius) {
  return (isCelsius ? "°C" : "ºF");
}

export function getSpeedUnit(isMeter) {
  return (isMeter ? "KM/H" : "MI/H");
}

export function getAirPressureUnit(isBar) {
  return (isBar ? "bar" : "psi");
}

export function getSpeed(speed, isMeter) {
  return (isMeter ? Math.floor(speed) : kmhToMIH(speed))
}
