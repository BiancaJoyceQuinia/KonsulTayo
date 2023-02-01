package com.example.healthecare.QR

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.healthecare.global.Global
import com.example.healthecare.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

class QRGenerate : AppCompatActivity() {

    private lateinit var idIVQrcode : ImageView
    private lateinit var vitrFullName : TextView
    private lateinit var vitrDateOfReturn : TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrgenerate)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "KonsulTayo QR Generator"

        idIVQrcode = findViewById(R.id.idIVQrcode)
        vitrFullName = findViewById(R.id.vitrFullName)
        vitrDateOfReturn = findViewById(R.id.vitrDateOfReturn)
        //idEdt = findViewById(R.id.idEdt)
        //idBtnGenerateQR = findViewById(R.id.idBtnGenerateQR)

        vitrFullName.text = "Name: " + Global.vitrFullName
        vitrDateOfReturn.text = "Date of Return: " + Global.vitrDateOfReturn

        val writer = QRCodeWriter()

        try{

            val mainPatientKey = Global.mainPatientKey
            val mainConsultationsKey = Global.mainConsultationsKey

            //key=-NC3FOLqkGQme1NK9YfH&mcKey=-NCEKrnO8ShQM3aq3O24
            val data = "key=${mainPatientKey}&mcKey=${mainConsultationsKey}"

            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width){
                for (y in 0 until height){
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            idIVQrcode.setImageBitmap(bmp)

        }catch (e: WriterException){
            e.printStackTrace()
        }



    }
}