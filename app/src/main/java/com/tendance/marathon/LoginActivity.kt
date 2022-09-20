package com.tendance.marathon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.google.android.material.snackbar.Snackbar
import com.tendance.marathon.models.LoginRequest
import com.tendance.marathon.repository.UserRepository
import com.tendance.marathon.utils.SharedPreferenceManager

class LoginActivity : AppCompatActivity() {

    lateinit var userName:EditText
    lateinit var password:EditText
    lateinit var connexion:Button
    lateinit var loading:ProgressBar
    lateinit var view:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.hide()

        userName = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loading = findViewById(R.id.progressbar)
        connexion = findViewById(R.id.btnConnexion)
        view = findViewById(R.id.global)

        connexion.setOnClickListener {
            connexion.isClickable = false
            loading.visibility = View.VISIBLE
            if (userName.text.isNotEmpty() && password.text.isNotEmpty()){
                val credentials = LoginRequest("1234", password.text.toString(), userName.text.toString())
                UserRepository.getInstance().login(credentials) { isSuccess, response ->
                    if (isSuccess) {
                        val home = Intent(this, MainActivity::class.java)
                        SharedPreferenceManager.getInstance(this)!!.saveUser(response!!)
                        home.putExtra("nom", response.name)
                        home.putExtra("code", response.code)
                        home.putExtra("image", response.image)
                        startActivity(home)
                        finish()
                        loading.visibility = View.GONE
                    } else {
                        connexion.isClickable = true
                        loading.visibility = View.GONE
                        Snackbar.make(view, "Erreur ressayer...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }
                }
            }else{
                connexion.isClickable = true
                loading.visibility = View.GONE
                 Snackbar.make(view, "Saisir les identifiants...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            }


        }

    }
}