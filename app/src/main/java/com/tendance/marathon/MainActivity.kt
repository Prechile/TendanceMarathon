package com.tendance.marathon

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.cardview.widget.CardView
import com.tendance.marathon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var info : CardView
    lateinit var btnCon : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        info = findViewById(R.id.cardInfo)
        btnCon = findViewById(R.id.button2)

        info.setOnClickListener{
            startActivity(Intent(this, Website::class.java))
        }

        btnCon.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }


    }