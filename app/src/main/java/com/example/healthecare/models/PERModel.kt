package com.example.healthecare.models

data class PERModel(
//    25

    var key: String? = null,
    var vperLName: String? = null,
    var vperFName: String? = null,
    var vperMName: String? = null,
    var vperSuffix: String? = null,
    var vperFullName: String? = null,
    var vperAge: String? = null,
    var vperSex: String? = null,
    var vperBirthdate: String? = null,
    var vperBirthplace: String? = null,
    var vperCivilStatus: String? = null,
    var vperSpouse: String? = null,
    var vperEducAttain: String? = null,
    var vperEmpStatus: String? = null,
    var vperFamMem: String? = null,
    var vperMomName: String? = null,
    var vperMomBirth: String? = null,
    var vperResAdd: String? = null,
    var vperBrgy: String? = null,
    var vperDadName: String? = null,
    var vperDadBirth: String? = null,
    var vperContactNum: String? = null,
    var vper4psMem: String? = null,
    var vperPhilHealthMem: String? = null,
    var vperStatusType: String? = null,
    var vperPhilHealthNo: String? = null,

    var userKey1: String? = null,
    var userKey2: String = "x",

    var zdateTime: String? = null,
    var zupDateTime: String? = null,
    )


data class UsersModel(

    var key: String? = null,
    var pass: String? = null,
    var pos: String? = null,
    var usernamepass: String? = null,
    var username: String = "",
    var celno: String = "",
    var otp: String = "",
    var status: String = "",
    var firstname: String = "",
    var lastname: String = "",
    var fullname: String? = null

)

data class SpecificDiagnosis(

    var key: String? = null,
    var vitrSpecificDiagnosis: String? = null

)
