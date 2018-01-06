import { isJson, existsKey } from "./jsUtil.js";
import * as telemetryConst from "./telemetryConst.js";

export function isCarPhysics(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.CAR_PHYSICS;
}

export function isRaceDefinition(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.RACE_DEFINITION;
}

export function isParticipants(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.PARTICIPANTS;
}

export function isTimings(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.TIMINGS;
}

export function isGameState(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.GAME_STATE;
}

export function isWeatherState(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.WEATHER_STATE;
}

export function isVehicleNames(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.VEHICLE_NAMES;
}

export function isTimeStats(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.TIME_STATS;
}

export function isParticipantVehicleNamesData(json) {
  return (
    isJson(json) &&
    existsKey(json, "base") &&
    json.base.packetType == telemetryConst.PARTICIPANT_VEHICLE_NAMES &&
    json.base.dataSize == telemetryConst.PARTICIPANT_VEHICLE_NAMES_DATA
  );
}

export function isVehicleClassNamesData(json) {
  return (
    isJson(json) &&
    existsKey(json, "base") &&
    json.base.packetType == telemetryConst.PARTICIPANT_VEHICLE_NAMES &&
    json.base.dataSize == telemetryConst.VEHICLE_CLASS_NAMES_DATA
  );
}

export function isLapTimeDetails(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.LAP_TIME_DETAILS;
}

export function isAggregateTime(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.AGGREGATE_TIME;
}

export function isFuelData(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.FUEL_DATA;
}

export function isTelemetrySummary(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.TELEMETRY_SUMMARY;
}

export function isRealTimeGap(json) {
  return isJson(json) && existsKey(json, "base") && json.base.packetType == telemetryConst.REAL_TIME_GAP;
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
  return isCelsius ? "°C" : "ºF";
}

export function getSpeedUnit(isMeter) {
  return isMeter ? "KM/H" : "MI/H";
}

export function getAirPressureUnit(isBar) {
  return isBar ? "bar" : "psi";
}

export function getSpeed(speed, isMeter) {
  return isMeter ? Math.floor(speed) : kmhToMIH(speed);
}

export function getTemp(temp, isCelsius) {
  return isCelsius ? Math.floor(temp) : celsiusToFahrenheit(temp);
}

const safetyColor = "#899ba9";
const warningColor = "#a56754";
const dangerColor = "#c54343";

export function getDamageColor(damage) {
  const value = parseInt(damage);
  if (value >= 20) {
    return dangerColor;
  } else if (value >= 10) {
    return warningColor;
  } else {
    return safetyColor;
  }
}
