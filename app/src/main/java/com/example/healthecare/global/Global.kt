package com.example.healthecare.global

import android.app.Application

public class Global : Application() {
    companion object {
        @JvmField

        var mainPatientKey: String = ""
        var mainConsultationsKey: String = ""
        var key: String = ""
        var mcKey: String = ""

        var vitrFullName: String = ""
        var vitrDateOfReturn: String = ""

        var vperBirthdate: String = ""
        var vperMomBirth: String = ""
        var vperDadBirth: String = ""

        var vitrDateOfConsult: String = ""

        var pos: String = ""
        var username : String = ""
        var userKey : String = ""
        var celno : String = ""
        var firstname : String = ""
        var lastname : String = ""
        var fullname : String = ""

        var pass : String = ""
    }
}