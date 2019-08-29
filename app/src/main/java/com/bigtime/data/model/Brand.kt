package com.bigtime.data.model

import android.os.Parcel
import android.os.Parcelable

data class Brand(
	val brandName: String? = null,
	val brandId: Int? = null
): Parcelable {
	var isChecked: Boolean = false

	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int) {
		isChecked = parcel.readByte() != 0.toByte()
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(brandName)
		parcel.writeValue(brandId)
		parcel.writeByte(if (isChecked) 1 else 0)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Brand> {
		override fun createFromParcel(parcel: Parcel): Brand {
			return Brand(parcel)
		}

		override fun newArray(size: Int): Array<Brand?> {
			return arrayOfNulls(size)
		}
	}
}
