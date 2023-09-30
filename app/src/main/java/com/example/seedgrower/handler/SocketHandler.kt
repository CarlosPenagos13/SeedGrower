package com.example.seedgrower.handler

import com.example.seedgrower.models.CheckSubTask
import com.example.seedgrower.models.CreateSubTask
import com.example.seedgrower.models.CreateTaskModel
import com.example.seedgrower.models.DetailTaskModel
import com.example.seedgrower.models.LoginResponse
import com.example.seedgrower.models.TaskListModel
import com.example.seedgrower.models.TaskModel
import com.example.seedgrower.models.Token
import com.example.seedgrower.models.UpdateTaskList
import com.example.seedgrower.models.User
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketHandler {
    private var mSocket: Socket? = null

    init {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000/")
            mSocket?.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun initGetSocket(): Socket? {
        return mSocket
    }

    fun emitRegisterUser(user: User) {
        val registerUser = Gson().toJson(user)
        mSocket?.emit(KEYS.REGISTER_USER, true, registerUser)
    }

    fun onRegisterUser(): Boolean {
        var isOnRegister = false

        mSocket?.on(KEYS.REGISTER_USER) { args ->
            if (args[0].toString().toBoolean()) {
                isOnRegister = args[0].toString().toBoolean()
            }
        }
        return isOnRegister
    }

    fun emitLoginUser(user: User) {
        val loginUser = Gson().toJson(user)
        mSocket?.emit(KEYS.LOGIN, loginUser)
    }

    fun onLoginUser(): LoginResponse {
        users = LoginResponse()
        mSocket?.on(KEYS.LOGIN) { args ->
            if (args[0].toString().toBoolean()) {
                users = Gson().fromJson(args.toString(), LoginResponse::class.java)
            }
        }
        return users
    }

    fun emitCreateTaskUser(createTaskModel: CreateTaskModel) {
        val createTaskUser = Gson().toJson(createTaskModel)
        mSocket?.emit(KEYS.CREATE_TASK, createTaskUser)
    }

    fun onCreateTaskUser(): CreateTaskModel {
        createTaskUser = CreateTaskModel()
        mSocket?.on(KEYS.CREATE_TASK) { args ->
            if (args[0].toString().toBoolean()) {
                createTaskUser = Gson().fromJson(args.toString(), createTaskUser::class.java)
            }
        }
        return createTaskUser
    }

    fun emitListTaskUser(token: Token) {
        val tokenUser = Gson().toJson(token)
        mSocket?.emit(KEYS.LIST_TASK, tokenUser)
    }

    fun onListTask(): TaskListModel {
        listTaskModel = TaskListModel()
        mSocket?.on(KEYS.LIST_TASK) { args ->
            if (args[0].toString().toBoolean()) {
                listTaskModel = Gson().fromJson(args.toString(), listTaskModel::class.java)
            }
        }
        return listTaskModel
    }

    fun emitDeleteTaskUser(tokenUser: String, idUser: String?) {
        val deleteTaskUser = TaskModel(token = tokenUser, uuid = idUser)
        val deleteTask = Gson().toJson(deleteTaskUser)
        mSocket?.emit(KEYS.DELETE_TASK, deleteTask)
    }

    fun onDeleteTask(): String {
        mSocket?.on(KEYS.DELETE_TASK) { args ->
            deleteTaskUser = "${args[0]}"
        }
        return deleteTaskUser
    }

    fun emitDetailTaskUser(token: String, taskModl: TaskModel) {
        val detailTaskUser = DetailTaskModel(
            token = token,
            TaskModel(coordinates = taskModl.coordinates, uuid = taskModl.uuid)
        )
        val detailTask = Gson().toJson(detailTaskUser)
        mSocket?.emit(KEYS.DETAIL_TASK, detailTask)
    }

    fun onDetailTaskUser(): TaskModel {
        taskUser = TaskModel()
        mSocket?.on(KEYS.DETAIL_TASK) { args ->
            taskUser = Gson().fromJson(args.toString(), taskUser::class.java)
        }
        return taskUser
    }

    fun emitUpdateTaskUser(updateTaskList: UpdateTaskList) {
        val updateTask = Gson().toJson(updateTaskList)
        mSocket?.emit(KEYS.UPDATE_TASK, updateTask)
    }

    fun onUpdateTask(): String {
        mSocket?.on(KEYS.UPDATE_TASK) { args ->
            updateTaskUser = "${args[0]}"
        }
        return updateTaskUser
    }

    fun emitCheckSubTaskUser(taskCreated: CheckSubTask) {
        val taskCheck = Gson().toJson(taskCreated)
        mSocket?.emit(KEYS.CHECK_SUBTASK, taskCheck)
    }

    fun onCheckTask(): String {
        mSocket?.on(KEYS.UPDATE_TASK) { args ->
            onCheck = "${args[0]}"
        }
        return onCheck
    }

    fun emitFinishedTaskUser(finishedTask: CheckSubTask) {
        val finishTask = Gson().toJson(finishedTask)
        mSocket?.emit(KEYS.FINISHED_TASK, finishTask)
    }

    fun onFinishTask(): String {
        mSocket?.on(KEYS.FINISHED_TASK) { args ->
            onFinish = "${args[0]}"
        }
        return onFinish
    }

    fun emitRegisterSubTaskUser(createSubTask: CreateSubTask) {
        val subTaskRegister = Gson().toJson(createSubTask)
        mSocket?.emit(KEYS.REGISTER_SUBTASK, subTaskRegister)
    }

    fun onRegisterSubTaskUser() : CreateSubTask {
        createSubTask = CreateSubTask()
        mSocket?.on(KEYS.REGISTER_SUBTASK) { args ->
            if (args[0].toString().toBoolean()) {
                createSubTask = Gson().fromJson(args.toString(), createSubTask::class.java)
            }
        }
        return createSubTask
    }

    object KEYS {
        const val REGISTER_USER = "register-user"
        const val LOGIN = "login"
        const val CREATE_TASK = "create-task"
        const val UPDATE_TASK = "update-task"
        const val DELETE_TASK = "delete-task"
        const val LIST_TASK =  "list-task"
        const val DETAIL_TASK = "detail-task"
        const val REGISTER_SUBTASK = "register-subtask"
        const val CHECK_SUBTASK = "check-subtask"
        const val FINISHED_TASK = "finished-task"

    }

    companion object {
        lateinit var users: LoginResponse
        lateinit var listTaskModel: TaskListModel
        lateinit var createTaskUser: CreateTaskModel
        lateinit var deleteTaskUser: String
        lateinit var updateTaskUser: String
        lateinit var onCheck : String
        lateinit var onFinish : String
        lateinit var taskUser : TaskModel
        lateinit var createSubTask : CreateSubTask
    }


}