// Frame type
// export const TELEMETRY_DATA_FRAME_TYPE = 0;
// export const PARTICIPANT_INFO_STRINGS_FRAME_TYPE = 1;
// export const PARTICIPANT_INFO_STRINGS_ADDITIONAL_FRAME_TYPE = 2;

// PacketHandlerType
export const CAR_PHYSICS = 0
export const RACE_DEFINITION = 1
export const PARTICIPANTS = 2
export const TIMINGS = 3
export const GAME_STATE = 4
export const WEATHER_STATE = 5
export const VEHICLE_NAMES = 6
export const TIME_STATS = 7
export const PARTICIPANT_VEHICLE_NAMES = 8
export const LAP_TIME_DETAILS = 64

// PacketSize
export const TELEMETRY_DATA = 538
export const RACE_DATA = 308
export const PARTICIPANTS_DATA = 1040
export const TIMINGS_DATA = 993
export const GAME_STATE_DATA = 24
export const TIME_STATS_DATA = 784
export const PARTICIPANT_VEHICLE_NAMES_DATA = 1164
export const VEHICLE_CLASS_NAMES_DATA = 1452

// TyreData and TyreUdpData index
export const TYRE_FRONT_LEFT = 0;
export const TYRE_FRONT_RIGHT = 1;
export const TYRE_REAR_LEFT = 2;
export const TYRE_REAR_RIGHT = 3;

// CarStateVecotrData index
export const VEC_X = 0;
export const VEC_Y = 1;
export const VEC_Z = 2;

// Game state
// gameState.gameState
export const GAME_EXITED = 0;
export const GAME_FRONT_END = 1;
export const GAME_INGAME_PLAYING = 2;
export const GAME_INGAME_PAUSED = 3;
export const GAME_INGAME_INMENU_TIME_TICKING = 4;
export const GAME_INGAME_RESTARTING = 5;
export const GAME_INGAME_REPLAY = 6;
export const GAME_FRONT_END_REPLAY = 7;

// Session state
// gameState.sessionState
export const SESSION_INVALID = 0;
export const SESSION_PRACTICE = 1
export const SESSION_TEST = 2;
export const SESSION_QUALIFY = 3;
export const SESSION_FORMATION_LAP = 4;
export const SESSION_RACE = 5;
export const SESSION_TIME_ATTACK = 6;
