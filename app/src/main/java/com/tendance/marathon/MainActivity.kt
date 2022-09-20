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
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.tendance.marathon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var searchBtn : LinearLayout
    lateinit var reportBtn : LinearLayout
    lateinit var stopBtn : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        val bundle = intent.extras
        val name = bundle!!.getInt("name")
        val code = bundle.getInt("code")
        val image = bundle.getString("image")

        searchBtn = findViewById(R.id.rechercherBtn)
        reportBtn = findViewById(R.id.signalerBtn)
        stopBtn = findViewById(R.id.infractionBtn)

        searchBtn.setOnClickListener{
            startActivity(Intent(this, MainActivity2::class.java))
        }

        reportBtn.setOnClickListener{
            startActivity(Intent(this, HistoriqueActivity::class.java))
        }

        stopBtn.setOnClickListener{
            startActivity(Intent(this, Changepswd::class.java))
        }

    }


    }