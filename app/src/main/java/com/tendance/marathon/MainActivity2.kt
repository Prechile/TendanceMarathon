package com.tendance.marathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class MainActivity2 : AppCompatActivity() {
    lateinit var evenement : Spinner
    lateinit var next : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val list = arrayListOf<String>("Marathon(42km)","Semi-marathon(21km)","10km(Course)","5km(Course)","5km(Marche)")
        evenement = findViewById(R.id.listEvenement)
        next = findViewById(R.id.next)
        val adapterObject = ArrayAdapter(this@MainActivity2,
            android.R.layout.simple_spinner_dropdown_item,list)
        evenement.adapter = adapterObject

        next.setOnClickListener{
            startActivity(Intent(this, EnrollementActivity::class.java))
        }
    }
}