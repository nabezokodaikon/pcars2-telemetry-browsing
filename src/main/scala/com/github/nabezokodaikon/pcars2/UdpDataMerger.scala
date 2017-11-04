package com.github.nabezokodaikon.pcars2

object UdpDataMerger {

  def mergeParticipantsData(
    dataList: List[ParticipantsData]
  ): Option[ParticipantsData] =
    dataList.lastOption match {
      case Some(last) =>
        val name = dataList.flatMap(data => data.name).toArray
        val mergeData = ParticipantsData(
          time = last.time,
          size = last.size,
          base = last.base,
          participantsChangedTimestamp = last.participantsChangedTimestamp,
          name = name
        )
        Some(mergeData)
      case None => None
    }

  def mergeParticipantVehicleNamesData(
    dataList: List[ParticipantVehicleNamesData]
  ): Option[ParticipantVehicleNamesData] =
    dataList.lastOption match {
      case Some(last) =>
        val vehicles = dataList.flatMap(data => data.vehicles).toArray
        val mergeData = ParticipantVehicleNamesData(
          time = last.time,
          size = last.size,
          base = last.base,
          vehicles = vehicles
        )
        Some(mergeData)
      case None => None
    }

  def mergeVehicleClassNamesData(
    dataList: List[VehicleClassNamesData]
  ): Option[VehicleClassNamesData] =
    dataList.lastOption match {
      case Some(last) =>
        val classes = dataList.flatMap(data => data.classes).toArray
        val mergeData = VehicleClassNamesData(
          time = last.time,
          size = last.size,
          base = last.base,
          classes = classes
        )
        Some(mergeData)
      case None => None
    }
}
