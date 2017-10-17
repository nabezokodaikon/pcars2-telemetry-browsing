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
