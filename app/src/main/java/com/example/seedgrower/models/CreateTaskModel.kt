package com.example.seedgrower.models

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CreateTaskModel (
    @SerializedName("ok") var ok: String? = null,
    @SerializedName("token") var token : String? = null,
    @SerializedName("task") var taskModel : TaskModel? = null

)

@Suppress("DEPRECATION")
data class  TaskModel (
    @SerializedName("coordinates") var coordinates : Location? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("uuid") var uuid : String? = null,
    @SerializedName("token") var token : String? = null,
    @SerializedName("subTasks") var subTasks : ArrayList<SubTaskModel>? = null
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Location::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(SubTaskModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(coordinates, flags)
        parcel.writeString(description)
        parcel.writeString(title)
        parcel.writeString(uuid)
        parcel.writeString(token)
        parcel.writeList(subTasks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskModel> {
        override fun createFromParcel(parcel: Parcel): TaskModel {
            return TaskModel(parcel)
        }

        override fun newArray(size: Int): Array<TaskModel?> {
            return arrayOfNulls(size)
        }
    }

}

data class TaskListModel(
    @SerializedName("ok") var ok: String? = null,
    @SerializedName("tasks") var taskObtainUser: ArrayList<TaskModel>? = null
)


data class DetailTaskModel(
    @SerializedName("token") var token: String? = null,
    @SerializedName("data") var data: TaskModel? = null
)


data class SubTaskModel(
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("uuidSubtask") var uuidSubtask: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubTaskModel> {
        override fun createFromParcel(parcel: Parcel): SubTaskModel {
            return SubTaskModel(parcel)
        }

        override fun newArray(size: Int): Array<SubTaskModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class CheckSubTask(
    @SerializedName("token") var token: String? = null,
    @SerializedName("uuid") var uuid: String? = null,
    @SerializedName("uuidSubtask") var uuidSubtask: String? = null

)

data class UpdateTaskList(
    @SerializedName("token") var token: String? = null,
    @SerializedName("uuid") var uuid: String? = null,
    @SerializedName("subTasks") var subTasks: SubTaskModel = null
)

data class CreateSubTask(
    @SerializedName("token") var token: String? = null,
    @SerializedName("data") var data: UpdateTaskList? = null
)