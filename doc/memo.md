# MEMO
## Game State
* 起動時 - メニュー画面
  * GameStateData.gameState = 1
  * GameStateData.sessionState = 0
  * GameStateData.raceStateFlags = 0
* ブリフィーグ画面
  * GameStateData.gameState = 2
  * GameStateData.sessionState = 5
  * GameStateData.raceStateFlags = 1
* 発進時
  * GameStateData.gameState = 2
  * GameStateData.sessionState = 5
  * GameStateData.raceStateFlags = 2
* ゴール後
  * GameStateData.gameState = 2
  * GameStateData.sessionState = 5
  * GameStateData.raceStateFlags = 3
* リプレイ
  * GameStateData.gameState = 5
  * GameStateData.sessionState = 5
  * GameStateData.raceStateFlags = 2


## Contents
### Common
* 車両名
  * ParticipantInfoStrings.carName
* 車のクラス
  * ParticipantInfoStrings.carClassName
* コース名
  * ParticipantInfoStrings.trackLocation
    * 最後の5文字がおかしい。
### Simple
#### ラップ
* ラップタイム
  * TimingInfoData.currentTime(秒)
* 現在のラップ
  * ParticipantInfo[0].currentLap
* トータルラップ数
  * EventInfoData.lapsInEvent
* 順位
  * ParticipantInfo[0].racePosition
* 参加台数
  * ParticipantInfo.racePositionの最大値
* ギア
  * CarStateData.gear
* 速度(KM/H)
  * CarStateData.speed
    * 小数点以下を四捨五入
* 残り燃料(リットル)
  * CarStateData.fuelLevel
    * 小数点第2を四捨五入
    * 最後のラップでの使用量
    * 平均使用量
* エンジン回転数(rpm)
  * CarStateData.rpm
* 最大エンジン回転数(rpm)
  * CarStateData.maxRpm
* 油温(摂氏)
  * CarStateData.oilTempCelsius
    * 小数点以下を四捨五入
* 油圧(KPa)
  * CarStateData.oilPressureKPa
* 水温(摂氏)
  * CarStateData.waterTempCelsius
    * 小数点以下を四捨五入
* 水圧
  * CarStateData.waterPressureKpa
    * 画面表示が固定のまま。RSDashとは値が同じっぽい。
* ペダル
  * 0 - 1
  * 小数点第二位を四捨五入
      * CarStateData.throttle
      * CarStateData.brake
      * CarStateData.clutch
* ブレーキ温度(摂氏)
  * TyreData.brakeTempCelsius
    * 小数点以下を四捨五入
* タイヤ温度(摂氏)
  * TyreData.tyreTemp
* タイヤ圧(bar)
  * TyreData.airPressure
    * 小数点第三位を四捨五入
* タイヤの消耗
  * TyreData.tyreWear
    * 0 - 1
* タイヤ横の高さ
  * TyreUdpData.rideHeight
* タイヤ横の走行距離
  * TyreUdpData.suspensionTravel
      
### Tyre
#### 中央
* タイヤの摩耗
  * telemetryData.tyre1.tyreWear
  * 現在は値がよくわからない。
  * 緑のメーターが上から縮んでいく。
#### 上
* タイヤ温度
  * telemetryData.tyre1.tyreTempLeft
  * telemetryData.tyre1.tyreTempCenter
  * telemetryData.tyre1.tyreTempRight
#### 下
* 空気圧
  * telemetryData.tyre2.airPressure の100分の1
  * bar
#### 左
* BUMP
* TRAEL
  * telemetryData.tyre1.suspensionTravel (メートル単位)
  * cm
* HEIGHT
  * telemetryData.tyre1.rideHeight (メートル単位)
  * cm
#### 右
* ブレーキ温度
  * telemetryData.tyre1.brakeTempCelsius / 255
  * 摂氏
### 他
* 単位はケルビンと思われる。
  * telemetryData.tyre1.tyreRimTemp
  * telemetryData.tyre1.tyreCarcaseTemp
  * telemetryData.tyre1.tyreLayerTemp
* タイヤ内部空気温度
  * telemetryData.tyre1.tyreInternalAirTemp
  * telemetryData.tyre1.tyreTreadTemp

### 画面右下
* Speed
* POWER
* TORQUE
  * telemetryData.tyre3.engineToque
  * NM
  * 小数点以下切り捨て


## Time
* GAP(1位マシンとの差)(1位の場合は空白)
* Fastest lap
  * BestLAPTimeとの差(BESTLAP DELTA +00:00.000)
  * timeStatsData.participants[0].fastestLapTime(きれいに走らないと記録されない。)
* splitTime
  * timingsData.splitTime
* LAP
  * timeStatsData.participants[0].lastLapTime
  * timingsData.particpants[0].currentTime
  * timingsData.particpants[0].currentLap
* S1, S2, S3
  * timeStatsData.participants[0].lastSectorTime
  * timingsData.particpants[0].currentSectorTime
  * timingsData.particpants[0].sector
* current情報のsectorとlapを監視して、timeStatsDataが来たときにsectorとlapを変更する。
* 複数周保持したい。


## Motec LCD
#### 1
* Water Temp
* Water Press
* Oil Temp
* Oil Press
* RPM
* Speed
* Fuel
* (Current) Lap Time
#### 2
* Fuel(Current)小数点第一位
* Fuel Average
* Fuel Last
* Lap Time
* Best Lap Time
* Split Time
* Race poticon(pos)
* Laps
#### 3
* Tire Temp
* Brace Temp
* Brake % F (?)

### Detail view
### Tyre state chart
### Tyre state chart
### Other chart

## Unit
### 距離
* メートル法
* ヤード・ポンド法
### 温度
* 摂氏
* 華氏


## キーワード
* 設定
  * OPTIONS
* 単位
  * Display Units
    * Metric
    * Imperial

## 保持情報(storage)
* RaceData
* ParticipantsData
* ParticipantVehicleNamesData
* VehicleClassNamesData

# GameState
* ピットボックス
  * `GAME_INGAME_INMENU_TIME_TICKING`
* 走行中
  * `GAME_INGAME_PLAYING`
* リプレイ
  * `GAME_INGAME_REPLAY`


## Bug
### sParticipantVehicleNamesDataのPacketBaseのmPartialPacketIndexがmPartialPacketNumberの値まで送信されていない。
* 報告済み。

### sVehicleClassNamesDataのPacketBaseのmPartialPacketIndexとmPartialPacketNumberが、常に同じ値で送信されており、データの結合ができない。
* 報告済み。

### sTelemetryDataのsTyreCompoundが、空文字(全て0値)で送信されている。
* 報告済み。
* まれに正しく取得できる。

### sRaceDataのsLapsTimeInEventでtimed sessionの場合に、時間の値が常に32767になっている。
* 報告済み。

### Tire data of sTelemetryData
* 報告済み。
#### FrontLeft
* sTyreTempRight = Fixed 0
* sTyreTempCenter = Fixed 0
* sTyreTempLeft = Fixed 0
* sAirPressure = Fixed 65467
* sBrakeTempCelsius = Unknown value 
* sTyreWear = Unknown value
* sRideHeight = Unknown value
* sSuspensionRideHeight = Unknown value
### FrontRight
* sTyreWear = Increasing or decreasing
### RearLeft
* sTyreWear = It is unknown whether the value is correct when compared with RearRight.
### RearRight
* sTyreWear = It is unknown whether the value is correct when compared with RearLeft.

## ダメージ
```
private float trivialEngineDamageThreshold = 0.05f;
private float minorEngineDamageThreshold = 0.20f;
private float severeEngineDamageThreshold = 0.45f;
private float destroyedEngineDamageThreshold = 0.90f;

private float trivialSuspensionDamageThreshold = 0.01f;
private float minorSuspensionDamageThreshold = 0.05f;
private float severeSuspensionDamageThreshold = 0.15f;
private float destroyedSuspensionDamageThreshold = 0.60f;

private float trivialBrakeDamageThreshold = 0.15f;
private float minorBrakeDamageThreshold = 0.3f;
private float severeBrakeDamageThreshold = 0.6f;
private float destroyedBrakeDamageThreshold = 0.90f;

// TODO: have separate thresholds for tin-tops and open wheelers with wings here
private float trivialAeroDamageThreshold = 0.1f;
private float minorAeroDamageThreshold = 0.25f;
private float severeAeroDamageThreshold = 0.6f;
private float destroyedAeroDamageThreshold = 0.90f;

// tyres in PCars are worn out when the wear level is > ?
private float wornOutTyreWearLevel = 0.50f;

private float scrubbedTyreWearPercent = 1f;
private float minorTyreWearPercent = 20f;
private float majorTyreWearPercent = 40f;
private float wornOutTyreWearPercent = 80f;
```


## TODO
* DEFAULT content の順位を3桁まで表示できるようにする。(特にiPhoneでレイアウトが崩れる。)
* ENGINE content のスクリーンショットに水圧が表示されていないので、撮り直す。
* TIME content に Small gear, Small fuel を実装する。
* TIME content の テーブルの斜め表示をやめる。
* 時間履歴を増やし、TIME content をスクロール可能にする。
* ダメージの表示を追加する。
  * エンジン
    * aeroDamege
    * engineDamage
  * タイヤ
    * brakeDamage
    * suspensionDamage
