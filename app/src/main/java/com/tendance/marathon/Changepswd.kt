package com.tendance.marathon

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tendance.marathon.models.changePassword
import com.tendance.marathon.repository.UserRepository
import com.tendance.marathon.utils.SharedPreferenceManager

class Changepswd : AppCompatActivity() {
    
    lateinit var old:EditText
    lateinit var nveau:EditText
    lateinit var confirm:EditText
    lateinit var loading:ProgressBar
    lateinit var btn:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changepswd)

        old = findViewById(R.id.old)
        nveau = findViewById(R.id.nveau)
        confirm = findViewById(R.id.confirm)
        loading = findViewById(R.id.progress)
        btn = findViewById(R.id.btnChange)
        val user = SharedPreferenceManager.getInstance(this)!!.getUserResponse()


        btn.setOnClickListener(){
            if (old.text.isNotEmpty() &&nveau.text.isNotEmpty() &&confirm.text.isNotEmpty() ){
                if (nveau.text.trim().toString().equals(confirm.text.trim().toString())){
                    loading.visibility = View.VISIBLE
                    val model = changePassword(nveau.text.trim().toString(),old.text.trim().toString())
                    UserRepository.getInstance().changePswd(user.token!!,model){
                        isSuccess, response ->
                        if (isSuccess){
                            Toast.makeText(this, "SUCCES", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()

                        }else{
                            loading.visibility = View.GONE
                            Toast.makeText(this, "ECHEC; REESSAYER...", Toast.LENGTH_LONG).show()

                        }
                    }

                }
            }
            }


        }


}