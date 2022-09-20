package com.tendance.marathon.api

import com.tendance.marathon.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @POST("/api/Agent/Connexion")
    fun login(
        @Body credentials: LoginRequest
    ): Call<UserResponse>

    @POST("/api/Agent/ChangePassWord")
    fun changePswd(
        @Query("token")token: String,
        @Body model: changePassword
    ): Call<ResponseBody>

    @POST("/api/Client/NewClient")
    fun newClient(
        @Query("token")token: String,
        @Body model: newClientRequest
    ): Call<ClientResponse>

    @POST("https://app.ebounam.com:1026/api/PayGate/Pay")
    fun payement(
        @Body model: paygateRequest
    ): Call<ResponseBody>

    @GET("/api/Event/GetEvents")
    fun getEvents(
        @Query("token") token: String
    ) : Call<ArrayList<EventsResponse>>

    @GET("/api/Event/GetOneEvent")
    fun getOneEvent(
        @Query("token")token:String,
        @Query("eventId")eventId:Int
    ) : Call<EventsResponse>

    @GET("/api/Ticket/GetAgentTicket")
    fun getAgentTickets(
        @Query("token")token:String,
        @Query("date")date:String,
    ) : Call<List<AgentTicket>>



}