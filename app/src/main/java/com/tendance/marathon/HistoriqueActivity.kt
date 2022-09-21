package com.tendance.marathon

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.telpo.tps550.api.TelpoException
import com.telpo.tps550.api.printer.UsbThermalPrinter
import com.tendance.marathon.adapter.HistoriqueAdapter
import com.tendance.marathon.models.AgentTicket
import com.tendance.marathon.repository.EventsRepository
import com.tendance.marathon.repository.UserRepository
import com.tendance.marathon.utils.SharedPreferenceManager
import java.text.SimpleDateFormat
import java.util.*

class HistoriqueActivity : AppCompatActivity() {

    lateinit var loading: ProgressBar
    lateinit var mListView: ListView
    lateinit var dateS: TextView
    lateinit var btn: Button
    val myCalendar = Calendar.getInstance()
    var startDateFinal = ""
    val mUsbThermalPrinter = UsbThermalPrinter(this)
    lateinit var qrCode: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historique)

        val user = SharedPreferenceManager.getInstance(this)!!.getUserResponse()
        val auth = user.token
        dateS= findViewById(R.id.selectDate)
        btn= findViewById(R.id.btnok)
        loading = findViewById(R.id.loading)
        mListView = findViewById(R.id.OperationlistViewId)

        val date = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLabel()
        }

        dateS.setOnClickListener {
            DatePickerDialog(
                this,
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btn.setOnClickListener{

        }


        try {
            UserRepository.getInstance().getAgentTickets(user.token!!,startDateFinal) { isSuccess, response ->
                    if (isSuccess) {
                        loading.visibility = View.GONE
                        mListView.adapter = HistoriqueAdapter(this,response!! as ArrayList<AgentTicket>)
                        mListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                                val ope = mListView.getItemAtPosition(position) as AgentTicket
                                val alertDialog = AlertDialog.Builder(this)
                                alertDialog.setMessage("Impression...")
                                alertDialog.setNegativeButton("Annuler"){dialog, _ -> dialog.cancel()}
                                alertDialog.setNegativeButton("OK"){dialog, _ ->
                                    qrCode = createCode(
                                        ope.number,
                                        BarcodeFormat.QR_CODE,
                                        350,
                                        350
                                    )
                                    generateTicket(
                                        ope.clientName,
                                        ope.eventName,
                                        ope.eventDate,
                                        ope.amount.toString(),
                                        ope.dossar,
                                        ope.clientPhoneNumber,
                                        ope.number
                                    )
                                }
                                alertDialog.show()

                            }

                    } else {
                        Toast.makeText(this, "Echec de connexion", Toast.LENGTH_LONG)
                            .show()
                    }

                }

        } catch (e: Exception) {
            e.message
            e.printStackTrace()
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

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        dateS.setText(dateFormat.format(myCalendar.time))
        startDateFinal = dateFormat.format(myCalendar.time).toString()

    }

    @Throws(WriterException::class)
    fun createCode(str: String, type: BarcodeFormat, bmpWidth: Int, bmpHeight: Int): Bitmap {
        // ??????,????????,??????????????,??????????
        val matrix = MultiFormatWriter().encode(str, type, bmpWidth, bmpHeight)
        val width = matrix.width
        val height = matrix.height
        // ????????????(?????)
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = -0x1000000
                } else {
                    pixels[y * width + x] = -0x1
                }
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        // ????????bitmap,????api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun generateTicket(
        fullName: String, eventName: String, eventDate: String,
        amount: String, dossard: String, numero: String, ref: String
    ) {

        try {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.logomarathon)
            if (bitmap != null) {
                mUsbThermalPrinter.printLogo(bitmap, false)
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

            val content1 = fullName
            mUsbThermalPrinter.setTextSize(32)
            mUsbThermalPrinter.setBold(true)
            mUsbThermalPrinter.addString(content1)
            mUsbThermalPrinter.printString()


            val content88 = "date: $eventDate"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(content88)
            mUsbThermalPrinter.printString()

            val content89 = "Montant: $amount xof"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(content89)
            mUsbThermalPrinter.printString()


            val content7 = "Ref: $ref"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(content7)
            mUsbThermalPrinter.printString()

            val content5 = "Téléphone : $numero"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.addString(content5)
            mUsbThermalPrinter.printString()

            val content6 = "Dossard: $dossard"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.setBold(true)
            mUsbThermalPrinter.addString(content6)
            mUsbThermalPrinter.printString()


            val content20 = "Evenement: $eventName"
            mUsbThermalPrinter.setTextSize(24)
            mUsbThermalPrinter.setBold(true)
            mUsbThermalPrinter.addString(content20)
            mUsbThermalPrinter.printString()

            mUsbThermalPrinter.printLogo(qrCode, false)

            val content2 = "**************************"
            mUsbThermalPrinter.setTextSize(18)
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