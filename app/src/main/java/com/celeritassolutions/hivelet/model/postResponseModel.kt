package com.celeritassolutions.hivelet.model

import com.google.gson.annotations.SerializedName

data class postResponseData(
    @SerializedName("AssessmentID")
    val assessmentID: String = "",
    @SerializedName("QuestionID")
    val QuestionID: String = "",
    @SerializedName("ResponseText")
    val ResponseText: String = "",
    @SerializedName("AssessmentName")
    val AssessmentName: String = "",
    @SerializedName("AssessmentDate")
    val AssessmentDate: String = "",
    @SerializedName("AssessmentQuestion")
    val AssessmentQuestion: String = "",
    @SerializedName("ResponseComment")
    val ResponseComment: String = "",
    @SerializedName("DepartmentID")
    val DepartmentID: String = "",
    @SerializedName("SectionName")
    val SectionName: String = "",
    @SerializedName("ImageData")
    val ImageData: String = "",
    @SerializedName("OrganizationCode")
    val OrganizationCode: String = ""
)