package com.tendance.marathon.repository

import com.tendance.marathon.api.ApiClient
import com.tendance.marathon.models.ClientResponse
import com.tendance.marathon.models.EventsResponse
import com.tendance.marathon.models.newClientRequest
import com.tendance.marathon.models.paygateRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientRepository {

    fun newClient(
        model : newClientRequest,
        token:String,
        onResult : (isSuccess :  Boolean, response : ClientResponse?) -> Unit){

        ApiClient.instance.newClient(token,model).enqueue(object: Callback<ClientResponse> {
            override fun onResponse(call: Call<ClientResponse>, response: Response<ClientResponse>){
                if(response.code() == 200){
                    onResult(true, response.body())
                }else{
                    onResult(false, null)
                }
            }
            override fun onFailure(call: Call<ClientResponse>, t: Throwable){
                onResult(false,null)
            }
        })
    }

    fun payment(
        model : paygateRequest,
        onResult : (isSuccess :  Boolean, response : ResponseBody?) -> Unit){

        ApiClient.instance.payement(model).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>){
                if(response.code() == 200){
                    onResult(true, response.body())
                }else{
                    onResult(false, null)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable){
                onResult(false,null)
            }
        })
    }

    companion object {
        private var INSTANCE: ClientRepository? = null
        fun getInstance() = INSTANCE
            ?: ClientRepository().also {
                INSTANCE = it
            }
    }
}