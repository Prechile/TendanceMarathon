package com.tendance.marathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.tendance.marathon.models.EventsResponse
import com.tendance.marathon.repository.EventsRepository
import com.tendance.marathon.utils.SharedPreferenceManager

class MainActivity2 : AppCompatActivity() {
    lateinit var evenement : Spinner
    lateinit var next : Button
    val listt = arrayListOf<String>()
    var res : ArrayList<EventsResponse> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val user = SharedPreferenceManager.getInstance(this)!!.getUserResponse()
        val list = arrayListOf("Marathon(42km)","Semi-marathon(21km)","10km(Course)","5km(Course)","5km(Marche)")
        evenement = findViewById(R.id.listEvenement)
        next = findViewById(R.id.next)

        EventsRepository.getInstance().getEvents(user.token!!){
                isSuccess, response ->
            if (isSuccess){
                res = response!!
                for (k in response){
                    listt.add(k.name)
                    val adapterObject = ArrayAdapter(this@MainActivity2,
                        android.R.layout.simple_spinner_dropdown_item,listt)
                    evenement.adapter = adapterObject
                }
            }

        }



        next.setOnClickListener{
            var meventId=0
            val name = evenement.selectedItem
            for (i in res){
                if (i.name == name){
                    meventId = i.eventId
                    break
                }
            }

            val got = Intent(this, EnrollementActivity::class.java)
            got.putExtra("eventId", meventId)
            startActivity(got)
        }
    }
}