package com.tendance.marathon.repository

import com.tendance.marathon.api.ApiClient
import com.tendance.marathon.models.AgentTicket
import com.tendance.marathon.models.EventsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsRepository {

    fun getEvents(
        token : String,
        onResult : (isSuccess :  Boolean, response : ArrayList<EventsResponse>?) -> Unit){

        ApiClient.instance.getEvents(token).enqueue(object: Callback<ArrayList<EventsResponse>> {
            override fun onResponse(call: Call<ArrayList<EventsResponse>>, response: Response<ArrayList<EventsResponse>>){
                if(response.code() == 200){
                    onResult(true, response.body())
                }else{
                    onResult(false, null)
                }
            }
            override fun onFailure(call: Call<ArrayList<EventsResponse>>, t: Throwable){
                onResult(false,null)
            }
        })
    }

    fun getOneEvent(
        token : String, eventId : Int,
        onResult : (isSuccess :  Boolean, response : EventsResponse?) -> Unit){

        ApiClient.instance.getOneEvent(token, eventId).enqueue(object: Callback<EventsResponse> {
            override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>){
                if(response.code() == 200){
                    onResult(true, response.body())
                }else{
                    onResult(false, null)
                }
            }
            override fun onFailure(call: Call<EventsResponse>, t: Throwable){
                onResult(false,null)
            }
        })
    }

    companion object {
        private var INSTANCE: EventsRepository? = null
        fun getInstance() = INSTANCE
            ?: EventsRepository().also {
                INSTANCE = it
            }
    }
}