package com.github.nabezokodaikon.udpListener

object UdpDataMerger {

  def mergeParticipantsData(
    dataList: List[ParticipantsData]
  ): Option[ParticipantsData] =
    dataList.lastOption match {
      case Some(last) =>
        val name = dataList.flatMap(data => data.name).toArray
        val nationality = dataList.flatMap(data => data.nationality).toArray
        val index = dataList.flatMap(data => data.index).toArray
        val mergeData = ParticipantsData(
          base = last.base,
          participantsChangedTimestamp = last.participantsChangedTimestamp,
          name = name,
          nationality = nationality,
          index = index
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
          base = last.base,
          classes = classes
        )
        Some(mergeData)
      case None => None
    }
}
