package com.github.nabezokodaikon.pcars2

object UdpDataMerger {

  def merge(dataList: List[ParticipantsData]): ParticipantsData = {
    dataList.head
  }

  def merge(dataList: List[ParticipantVehicleNamesData]): ParticipantVehicleNamesData = {
    dataList.head
  }

  def merge(dataList: List[VehicleClassNamesData]): VehicleClassNamesData = {
    dataList.head
  }
}
