package com.bigtime.data.model

import com.google.gson.annotations.SerializedName

data class Order(
        @SerializedName("AllOrders") val allOrders : Int?,
        @SerializedName("activeOrdersTest") val activeOrdersTest : Int?,
        @SerializedName("totalInDelayTest") val totalInDelayTest : Int?,
        @SerializedName("NewOrders") val newOrders : Int?,
        @SerializedName("InProcess") val inProcess : Int?,
        @SerializedName("InPackaging") val inPackaging : Int?,
        @SerializedName("InPackage") val inPackage : Int?,
        @SerializedName("InitiatedPickup") val initiatedPickup : Int?,
        @SerializedName("InDispatch") val inDispatch : Int?,
        @SerializedName("InTransit") val inTransit : Int?,
        @SerializedName("Delivered") val delivered : Int?,
        @SerializedName("RTO") val rTO : Int?,
        @SerializedName("lost") val lost : Int?,
        @SerializedName("cancelled") val cancelled : Int?,
        @SerializedName("completed") val completed : Int?,
        @SerializedName("activeOrders") val activeOrders : Int?,
        @SerializedName("totalInDelay") val totalInDelay : Int?)
