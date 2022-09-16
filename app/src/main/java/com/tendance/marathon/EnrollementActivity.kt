package com.tendance.marathon

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.telpo.tps550.api.TelpoException
import com.telpo.tps550.api.printer.UsbThermalPrinter
import java.text.SimpleDateFormat
import java.util.*

class EnrollementActivity : AppCompatActivity(), OptionsBottomSheetFragment.ItemClickListener {

    private var modePayId: Int? = 0
    lateinit var btnPay: Button
    lateinit var sexe: Spinner
    lateinit var mdate: EditText
    lateinit var fullName: EditText
    lateinit var pays: EditText
    lateinit var tel: EditText
    lateinit var fullNameU: EditText
    lateinit var telU: EditText
    lateinit var loading: ProgressBar

    val mUsbThermalPrinter = UsbThermalPrinter(this)
    lateinit var qrCode: Bitmap
    val myCalendar = Calendar.getInstance()
    lateinit var startDateFinal : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enrollement)

        btnPay = findViewById(R.id.btnPay)
        sexe = findViewById(R.id.sexe)
        mdate = findViewById(R.id.date)
        fullName = findViewById(R.id.fullName)
        fullNameU = findViewById(R.id.fullNameU)
        pays = findViewById(R.id.pays)
        tel = findViewById(R.id.tel)
        telU = findViewById(R.id.telU)
        loading = findViewById(R.id.progressPay)

        val list = arrayListOf("Masculin","Feminin")
        val adapterObject = ArrayAdapter(this@EnrollementActivity,
            android.R.layout.simple_spinner_dropdown_item,list)
        sexe.adapter = adapterObject

        val date = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }


        mdate.setOnClickListener {
            DatePickerDialog(
                this,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnPay.setOnClickListener {
//            if (fullName.text.isNotEmpty() && fullNameU.text.isNotEmpty()
//                &&tel.text.isNotEmpty() && telU.text.isNotEmpty() && pays.text.isNotEmpty()){
//
//            }
            supportFragmentManager.let {
                OptionsBottomSheetFragment.newInstance(Bundle()).apply {
                    show(it, tag)
                }
            }
        }


        Thread(Runnable {
            try {
                this.mUsbThermalPrinter.start(1)

            } catch (e: TelpoException) {
                Log.e("yw", "status  111 $e")
                e.printStackTrace()
            }
        }).start()
    }

    override fun onItemClick(item: String) {

        when (item) {
            "tmoney" -> {
                Toast.makeText(this, "Tmoney", Toast.LENGTH_LONG).show()
                modePayId = 1
                generateTicket(startDateFinal, modePayId.toString(), tel.text.toString(), fullName.toString())
            }
            "flooz" -> {
                Toast.makeText(this, "Flooz", Toast.LENGTH_LONG).show()
                modePayId = 2
                generateTicket(startDateFinal, modePayId.toString(), tel.text.toString(), fullName.toString())

            }

            "cash" -> {
                Toast.makeText(this, "Cash", Toast.LENGTH_LONG).show()
                modePayId = 3
                generateTicket(startDateFinal, modePayId.toString(), tel.text.toString(), fullName.toString())

            }

            else -> {

            }
        }
    }

    private fun updateLabel() {
        val myFormat = "yyyy/MM/dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        mdate.setText(dateFormat.format(myCalendar.time))
        startDateFinal = dateFormat.format(myCalendar.time).toString()

    }

    fun generateTicket(date: String,  montant: String, numero: String, fullName: String) {

        try {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logomarathon)
            if (bitmap != null) {
                mUsbThermalPrinter.printLogo(qrCode, false)
//                mUsbThermalPrinter.setAlgin(5)
            }

            mUsbThermalPrinter.reset()
            mUsbThermalPrinter.setBold(true)
            mUsbThermalPrinter.setLineSpace(1)

            val contentxy = "Recu ticket"
            mUsbThermalPrinter.setTextSize(30)
            mUsbThermalPrinter.addString(contentxy)
            mUsbThermalPrinter.printString()
//            mUsbThermalPrinter.setAlgin(5)

            val contentx = "**************************"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(contentx)
            mUsbThermalPrinter.printString()

            val content1 = date + "\n" +
                    "" + fullName + "\n"
            mUsbThermalPrinter.setTextSize(26)
            mUsbThermalPrinter.setBold(true)
            mUsbThermalPrinter.addString(content1)
            mUsbThermalPrinter.printString()

            val content7 = "Nom complet : $fullName"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(content7)
            mUsbThermalPrinter.printString()

            val content5 = "Téléphone : $numero"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(content5)
            mUsbThermalPrinter.printString()

            val content6 = "Mode paiement: $modePayId"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.setBold(true)
            mUsbThermalPrinter.addString(content6)
            mUsbThermalPrinter.printString()

            mUsbThermalPrinter.printLogo(qrCode, false)

            val content2 = "**************************"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(content2)
            mUsbThermalPrinter.printString()

            val content3 = "Provided by Proconsulting"
            mUsbThermalPrinter.setTextSize(18)
            mUsbThermalPrinter.addString(content3)
            mUsbThermalPrinter.printString()

            val content4 = "***************************\n"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(content4)
            mUsbThermalPrinter.printString()

        } catch (ex: TelpoException) {
            ex.message
            ex.printStackTrace()
        }

    }

}