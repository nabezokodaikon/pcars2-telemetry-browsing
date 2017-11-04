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
      
### Tyre(Telemetry)
#### 中央
* タイヤの摩耗
  * tyreData.tyreWear
  * 0.00が摩耗無し - 1.00?
  * 緑のメーターが上から縮んでいく。
  * メーターは0.4でなくなるようにする。
#### 上
* タイヤ温度
  * tyreData.tyreTemp
#### 下
* 空気圧
  * tyreData.airPressure
  * bar
#### 左
* BUMP
* TRAEL
  * tyreData.suspensionTravel
  * cm
* HEIGHT
  * tyreData.rideHeight
  * cm
#### 右
* ブレーキ温度
  * tyreData.brakeTempCelsius
  * 摂氏
### 他
* 単位はケルビンと思われる。
  * tyreData.tyreRimTemp
  * tyreData.tyreCarcaseTemp
  * tyreData.tyreLayerTemp
* タイヤ内部空気温度
  * tyreData.tyreInternalAirTemp
  * tyreData.tyreTreadTemp

### 画面右下
* Speed
* POWER
* TORQUE
  * otherUdpData.engineToque
  * NM
  * 小数点以下切り捨て

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
