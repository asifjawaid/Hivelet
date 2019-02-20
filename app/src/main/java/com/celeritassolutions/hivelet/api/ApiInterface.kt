package com.celeritassolutions.hivelet.api

import com.celeritassolutions.hivelet.model.postResponseData
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*


interface ApiInterface {
    @GET("setResponsesDemoRC.php")
    fun responseDemoRC(
        @Query("AssessmentID") assessmentID: String,
        @Query("QuestionID") QuestionID: String,
        @Query("ResponseText") ResponseText: String,
        @Query("AssessmentName") AssessmentName: String,
        @Query("AssessmentDate") AssessmentDate: String,
        @Query("AssessmentQuestion") AssessmentQuestion: String,
        @Query("ResponseComment") ResponseComment: String,
        @Query("DepartmentID") DepartmentID: String,
        @Query("SectionName") SectionName: String,
        @Query("ImageData") ImageData: String,
        @Query("OrganizationCode") OrganizationCode: String
    ): Call<ResponseBody>

    @GET("AWSassessmentListRDemoRC.php")
    fun assessmentListRDemoRC(
        @Query("DepartmentID") DepartmentID: String,
        @Query("AssessmentID") AssessmentID: String,
        @Query("AssessmentLaunchedDateTime") AssessmentLaunchedDateTime: String,
        @Query("AssessmentSubmittedDateTime") AssessmentSubmittedDateTime: String,
        @Query("AssessmentTrainer") AssessmentTrainer: String,
        @Query("AssessmentScore") AssessmentScore: String,
        @Query("AssessmentCurrentStatus") AssessmentCurrentStatus: String,
        @Query("SummaryComments") SummaryComments: String,
        @Query("AssessmentNameID") AssessmentNameID: String,
        @Query("OrganizationCode") OrganizationCode: String
    ): Call<ResponseBody>

    @GET("setGeneralDemoRC.php")
    fun generalDemoRC(
        @Query("AssessmentID") AssessmentID: String,
        @Query("DepartmentID") DepartmentID: String,
        @Query("AssessmentNameID") AssessmentNameID: String,
        @Query("RoomNumber") RoomNumber: String,
        @Query("MealDuration") MealDuration: String,
        @Query("OrganizationCode") OrganizationCode: String
    ): Call<ResponseBody>




}

/*
AssessmentID=%@&DepartmentID=%@&AssessmentNameID=%@&RoomNumber=%@&MealDuration=%@&OrganizationCode=%@






http://ec2-52-4-106-227.compute-1.amazonaws.com/demoassessment/AWSassessmentListRDemoRC.php?
DepartmentID=%@&AssessmentID=%@&AssessmentLaunchedDateTime=%@&AssessmentSubmittedDateTime=%@&AssessmentTrainer=%@&AssessmentScore=%@&AssessmentCurrentStatus=%@&SummaryComments=%@&AssessmentNameID=%@&OrganizationCode=%@

object ApiUtils {

    val BASE_URL = "http://ec2-52-4-106-227.compute-1.amazonaws.com"

    val apiService: ApiInterface
        get() = ApiClient.getClient(BASE_URL)!!.create(ApiInterface::class.java)

}*/
