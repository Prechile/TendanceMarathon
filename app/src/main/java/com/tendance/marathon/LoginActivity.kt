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
                val home = Intent(this, MainActivity2::class.java)
                startActivity(home)
                finish()
            }else{
                 Snackbar.make(view, "Saisir les identifiants...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            }

            //val credentials = LoginRequest(username.text.toString(), password.text.toString())
//            UserRepository.getInstance().login(credentials) { isSuccess, response ->
//                if (isSuccess) {
//                    val home = Intent(this, MainActivity2::class.java)
//                    SharedPreferenceManager.getInstance(this)!!.userResponse(response!!)
//                    home.putExtra("directionId", response.directionId)
//                    home.putExtra("userId", response.id)
//                    home.putExtra("auth", response.token)
//                    startActivity(home)
//                    finish()
//                    loading.visibility = View.GONE
//                } else {
//                    connexion.isClickable = true
//                    loading.visibility = View.GONE
//                    val alertDialog: AlertDialog = AlertDialog.Builder(this@LoginActivity)
//                        .create()
//                    alertDialog.setCancelable(false)
//                    alertDialog.setMessage("Echec de connexion, veuillez réessayer")
//                    alertDialog.setButton(
//                        AlertDialog.BUTTON_NEUTRAL, "OK",
//                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
//                    alertDialog.show()
//                }
//            }
        }

    }
}