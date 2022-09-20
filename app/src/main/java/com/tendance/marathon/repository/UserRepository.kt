package com.tendance.marathon.repository

import com.tendance.marathon.api.ApiClient
import com.tendance.marathon.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

class UserRepository {
    fun login(
        credentials : LoginRequest,
        onResult : (isSuccess :  Boolean, response : UserResponse?) -> Unit){

        ApiClient.instance.login(credentials).enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>){
                if(response.code() == 200){
                    onResult(true, response.body())
                }else{
                    onResult(false, null)
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable?){
                onResult(false,null)
            }
        })
    }

    fun getAgentTickets(
        token : String, date: String,
        onResult : (isSuccess :  Boolean, response : List<AgentTicket>?) -> Unit){

        ApiClient.instance.getAgentTickets(token,date).enqueue(object: Callback<List<AgentTicket>> {
            override fun onResponse(call: Call<List<AgentTicket>>, response: Response<List<AgentTicket>>){
                if(response.code() == 200){
                    onResult(true, response.body())
                }else{
                    onResult(false, null)
                }
            }
            override fun onFailure(call: Call<List<AgentTicket>>, t: Throwable?){
                onResult(false,null)
            }
        })
    }

    fun changePswd(
        token : String, model: changePassword,
        onResult : (isSuccess :  Boolean, response : ResponseBody?) -> Unit){

        ApiClient.instance.changePswd(token,model).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if(response.code() == 200){
                    onResult(true, response.body())
                }else{
                    onResult(false, null)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable?){
                onResult(false,null)
            }
        })
    }


    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance() = INSTANCE
            ?: UserRepository().also {
                INSTANCE = it
            }
    }
}

