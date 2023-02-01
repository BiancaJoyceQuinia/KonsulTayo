package com.example.healthecare.models

data class PatientsModel(

    var key: String? = null,
    var vitrId: String? = null,
    var perId: String? = null,
    var vitrLName: String? = null,
    var vitrFName: String? = null,
    var vitrMName: String? = null,
    var vitrSuffix: String? = null,
    var vitrAge: String? = null,
    var vitrSex: String? = null,
    var vitrFullName: String? = null,
    var vperFullName: String? = null,
    var vitrResAdd: String? = null,
    var vitrModeOfTransac: String? = null,
    var vitrDateOfConsult: String? = null,
    var vitrConsultTime: String? = null,
    var vitrBP: String? = null,
    var vitrTemp: String? = null,
    var vitrHeight: String? = null,
    var vitrWeight: String? = null,
    var vitrTypeOfConsult: String? = null,
    var vitrComplaint: String? = null,
    var vitrDiagnosis: String? = null,
    var vitrSpecificDiagnosis: String? = null,

    var vitrTreatment: String? = null,

    var vitrDateOfReturn: String? = null,

    var MainConsultationsKey: String? = null,
    var MainPatientKey: String? = null,

)



