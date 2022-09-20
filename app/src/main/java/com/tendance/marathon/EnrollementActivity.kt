package com.tendance.marathon

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.telpo.tps550.api.TelpoException
import com.telpo.tps550.api.printer.UsbThermalPrinter
import com.tendance.marathon.models.newClientRequest
import com.tendance.marathon.models.paygateRequest
import com.tendance.marathon.repository.ClientRepository
import com.tendance.marathon.repository.EventsRepository
import com.tendance.marathon.utils.SharedPreferenceManager
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
    var startDateFinal = ""
    var eventId = 0
    var model = newClientRequest()

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

        //mdate.isEnabled = false
        mdate.isFocusable = false

        val bundle = intent.extras
        eventId = bundle!!.getInt("eventId")

        val list = arrayListOf("Masculin", "Feminin")
        val adapterObject = ArrayAdapter(
            this@EnrollementActivity,
            android.R.layout.simple_spinner_dropdown_item, list
        )
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
            if (fullName.text.isNotEmpty() && tel.text.isNotEmpty() && pays.text.isNotEmpty()) {
                if (fullName.text.length<3 || fullNameU.text.length<3 ||tel.text.length<8 ||
                    telU.text.length<8 ){

                    fullName.error = "superieur a 3 caracteres"
                    tel.error = "superieur a 3 caracteres"
                    pays.error = "superieur a 3 caracteres"
                }

                model = newClientRequest(
                    fullName.text.toString(),
                    tel.text.toString(),
                    pays.text.toString(),
                    startDateFinal,
                    sexe.selectedItemId.toInt(),
                    fullNameU.text.toString(),
                    telU.text.toString(),
                    eventId,
                    modePayId
                )

                supportFragmentManager.let {
                    OptionsBottomSheetFragment.newInstance(Bundle()).apply {
                        show(it, tag)
                    }
                }

            } else {
                fullName.error = "requis"
                tel.error = "requis"
                pays.error = "requis"
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
            "cash" -> {
                Toast.makeText(this, "Cash", Toast.LENGTH_LONG).show()
                modePayId = 0
                createClient(model)
            }

            "tmoney" -> {
                Toast.makeText(this, "Tmoney", Toast.LENGTH_LONG).show()
                modePayId = 1
                createClient(model)
            }
            "flooz" -> {
                Toast.makeText(this, "Flooz", Toast.LENGTH_LONG).show()
                modePayId = 2
                createClient(model)

            }

            else -> {

            }
        }
    }

    private fun updateLabel() {
        val myFormat = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        mdate.setText(dateFormat.format(myCalendar.time))
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

    private fun createClient(model: newClientRequest) {
        val user = SharedPreferenceManager.getInstance(this)!!.getUserResponse()

        if (modePayId == 0) {
            ClientRepository.getInstance().newClient(model, user.token!!) { isSuccess, response ->
                if (isSuccess) {
                    Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()
                    qrCode = createCode(response!!.number, BarcodeFormat.QR_CODE, 350, 350)

                    generateTicket(
                        response.clientName,
                        response.eventName,
                        response.eventDate,
                        response.amount.toString(),
                        response.dossar,
                        response.clientPhoneNumber,
                        response.number
                    )
                } else {
                    Toast.makeText(this, "ECHEC", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            EventsRepository.getInstance()
                .getOneEvent(user.token!!, eventId) { isSuccess, response ->
                    val payModel = paygateRequest(
                        response!!.eventPrices[0].amount.toInt(),
                        "payement",
                        0,
                        tel.text.toString(),
                        "20c15cd9-639b-45ab-a498-8afa89c0595b",
                        "Tendance Marathon"
                    )

                    ClientRepository.getInstance().payment(payModel) { isSuccess, response ->
                        if (isSuccess) {
                            ClientRepository.getInstance()
                                .newClient(model, user.token) { isSuccess, response ->
                                    if (isSuccess) {
                                        Toast.makeText(this, "OK", Toast.LENGTH_LONG).show()
                                        qrCode = createCode(
                                            response!!.number,
                                            BarcodeFormat.QR_CODE,
                                            350,
                                            350
                                        )

                                        generateTicket(
                                            response.clientName,
                                            response.eventName,
                                            response.eventDate,
                                            response.amount.toString(),
                                            response.dossar,
                                            response.clientPhoneNumber,
                                            response.number
                                        )
                                    } else {
                                        Toast.makeText(this, "ECHEC", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "ECHEC", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }


    }


}