package com.example.healthecare.QR

import android.content.Intent
import android.content.pm.PackageManager
import android.net.UrlQuerySanitizer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.healthecare.global.Global
import com.example.healthecare.ITR.DetailsITR
import com.example.healthecare.R

class QRScanner : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private val requestCodeCameraPermission = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "KonsulTayo QR Scanner"

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        if (ContextCompat.checkSelfPermission(this@QRScanner, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this@QRScanner, arrayOf(android.Manifest.permission.CAMERA), requestCodeCameraPermission)
        }

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()

                val sanitizer = UrlQuerySanitizer()
                sanitizer.allowUnregisteredParamaters = true;
                sanitizer.parseUrl("?${it.text}");
                val key = sanitizer.getValue("key")
                val mcKey = sanitizer.getValue("mcKey")

                Global.key = key.toString()
                Global.mcKey = mcKey.toString()

                Global.mainPatientKey = key.toString()
                Global.mainConsultationsKey = mcKey.toString()

                val intent = Intent(this, DetailsITR::class.java)
                finish()
                startActivity(intent)


            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}