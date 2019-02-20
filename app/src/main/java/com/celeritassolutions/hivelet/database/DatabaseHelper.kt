package com.celeritassolutions.hivelet.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.celeritassolutions.hivelet.model.*
import com.celeritassolutions.hivelet.utils.SP
import com.celeritassolutions.hivelet.utils.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context,Constants.DATABASE.DB_NAME,null,Constants.DATABASE.DB_VERSION) {

    private var getSharedPrefs = SP(context)
    private var utils = Utils()
    private var startMonth = utils.getStartOfMonth()
    private var today = utils.getCurrentDate()

    override fun onCreate(db: SQLiteDatabase?) {
        myDataBase?.execSQL(Constants.DATABASE.CREATE_ASSESSMENT_TABLE_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_CLASSIFICATION_TABLE_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_QUESTIONNIRE_TABLE_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_DEPARTMENT_TABLE_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_SCORE_REVIEW_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_SURVEY_RESPONSE_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_COUNT_TABLE_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_EMPLOYEE_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_COUNT_MTD_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_CAT_REVIEW_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.CREATE_CAT_SCORE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        myDataBase?.execSQL(Constants.DATABASE.DROP_ASSESSMENT_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.DROP_CLASSIFICATION_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.DROP_QUESTIONNIRE_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.DROP_DEPARTMENT_QUERY)
        myDataBase?.execSQL(Constants.DATABASE.DROP_SCORE_REVIEW_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.DROP_SURVEY_RESPONSE_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.DROP_COUNT_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.DROP_EMPLOYEE_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.DROP_COUNT_MTD_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.DROP_CAT_REVIEW_TABLE)
        myDataBase?.execSQL(Constants.DATABASE.DROP_CAT_SCORE_TABLE)
    }

    fun openDatabase(): SQLiteDatabase {
        val dbFile = context.getDatabasePath(DB_NAME)
        if (!dbFile.exists()) {
            try {
                val checkDB = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE,null)
                checkDB?.close()
                copyDatabase(dbFile)
            } catch (e: IOException) {
                throw RuntimeException("Error creating source database", e)
            }

        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }

    @SuppressLint("WrongConstant")
    private fun copyDatabase(dbFile: File) {
        val `is` = context.assets.open(DB_NAME)
        val os = FileOutputStream(dbFile)

        val buffer = ByteArray(1024)
        while (`is`.read(buffer) > 0) {
            os.write(buffer)
            Log.d("#DB", "writing>>")
        }
        os.flush()
        os.close()
        `is`.close()
        Log.d("#DB", "completed..")
    }

    fun selectDepartments(): List<DepartmentsModel> {
        val records = ArrayList<DepartmentsModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.DEPARTMENT_TABLE}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = DepartmentsModel()
                list.deptID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.DEPARTMENT_ID))
                list.deptName = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.DEPARTMENT_NAME))
                list.assessment_MTD = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.ASSES_MTD))
                list.assessment_YTD = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.ASSES_YTD))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun select_employees(): List<EmployeeModel> {
        val records = ArrayList<EmployeeModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.EMPLOYEE_TABLE} ORDER BY ${Constants.DATABASE.EMPLOYEE_ID}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = EmployeeModel()
                list.id = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.EMPLOYEE_ID))
                list.name = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.EMPLOYEE_NAME))
                list.department = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.DEPARTMENT_NAME))
                list.designation = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.EMPLOYEE_DESIGNATION))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun selectAssessments(id:String): List<AssessmentsModel> {
        val records = ArrayList<AssessmentsModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.ASSESSMENT_TABLE} WHERE ${Constants.DATABASE.DEPARTMENT_ID} = $id ORDER BY ${Constants.DATABASE.ASSESSMENT_NAME}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = AssessmentsModel()
                list.assessmentID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.ASSESSMENT_ID))
                list.deptID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.DEPARTMENT_ID))
                list.assessmentName = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.ASSESSMENT_NAME))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun selectCategories(): List<CategoriesModel> {
        val records = ArrayList<CategoriesModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.CLASSIFICATION_TABLE} ORDER BY ${Constants.DATABASE.CLASSIFICATION_NAME}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = CategoriesModel()
                list.catID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CLASSIFICATION_ID))
                list.catName = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CLASSIFICATION_NAME))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun selectCatScores(): List<CatReviewModel> {
        val records = ArrayList<CatReviewModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.CAT_REVIEW_TABLE}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = CatReviewModel()
                list.CAT_RESPONSE_ID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_ID))
                list.CAT_AMBIANCE = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_AMBIANCE))
                list.CAT_CLEANLINESS = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_CLEANLINESS))
                list.CAT_COURTESY = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_COURTESY))
                list.CAT_EFFECIENCY = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_EFFECIENCY))
                list.CAT_ELEMENT = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_ELEMENT))
                list.CAT_FOOD = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_FOOD))
                list.CAT_GRACIOUS = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_GRACIOUS))
                list.CAT_COMFORT = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_COMFORT))
                list.CAT_CLASSIFICATION = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_CLASSIFICATION))
                list.CAT_SERVICE = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_SERVICE))
                list.CAT_STAFF = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_STAFF))
                list.CAT_TECHNICAL = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_TECHNICAL))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun browseCatScores(responseID: String): List<CategoriesModel> {
        val records = ArrayList<CategoriesModel>()
        val selectQuery = "SELECT * FROM cat_score_table WHERE s_response_id = '$responseID'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = CategoriesModel()
                list.responseID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_ID))
                list.catName = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_NAME))
                list.catScore = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CAT_SCORE))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun selectQuestionnire(id:String): List<QuestionnireModel> {
        val records = ArrayList<QuestionnireModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.QUESTIONNIRE_TABLE} WHERE ${Constants.DATABASE.ASSESSMENT_ID} = $id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = QuestionnireModel()
                list.q_id = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.QUESTIONNIRE_ID))
                list.assessmentID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.ASSESSMENT_ID))
                list.classID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CLASSIFICATION_ID))
                list.className = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CLASSIFICATION_NAME))
                list.question = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.QUESTIONNIRE))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }



    fun selectPastAssessments(deptName: String): List<ResponsesModel> {
        val records = ArrayList<ResponsesModel>()
        val selectQuery = "SELECT * FROM survey_review_table WHERE dept_name = '$deptName' ORDER BY s_response_id DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = ResponsesModel()
                list.respose_id = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_ID))
                list.dept_name = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.DEPARTMENT_NAME))
                list.survey_date = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_DATE))
                list.survey_trainer = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_TRAINER))
                list.total_dept_score = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_SCORE))
                list.survey_status = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_STATUS))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun browseQuestionnaire(response_id: String): List<BrowseQuestionnaireModel> {
        val records = ArrayList<BrowseQuestionnaireModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.SURVEY_RESPONSE_TABLE} WHERE ${Constants.DATABASE.SURVEY_RESPONSE_ID} = '$response_id'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = BrowseQuestionnaireModel()
                list.survey_id = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_ID))
                list.survey_response_id = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_ID))
                list.dept_name = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.DEPARTMENT_NAME))
                list.class_id = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CLASSIFICATION_ID))
                list.class_name = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CLASSIFICATION_NAME))
                list.response_question = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_QUESTION))
                list.response_answer = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE))
                list.survey_date = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_DATE))
                list.survey_comment = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_COMMENT))
                list.survey_photo = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_PHOTO))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun selectResponsesToPost(response_id: String): List<QuestionnireModel> {
        val records = ArrayList<QuestionnireModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.SURVEY_RESPONSE_TABLE} WHERE ${Constants.DATABASE.SURVEY_RESPONSE_ID} = '$response_id'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = QuestionnireModel()
                list.assessmentID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_ID))
                list.classID = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CLASSIFICATION_ID))
                list.q_id = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.QUESTIONNIRE_ID))
                list.className = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.CLASSIFICATION_NAME))
                list.assessmentName = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.ASSESSMENT_NAME))
                list.deptName = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.DEPARTMENT_NAME))
                list.question = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_QUESTION))
                list.queResponse = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE))
                list.isCommentAdded = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_COMMENT))
                list.isPhotoAdded = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_PHOTO))
                list.qDate = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_DATE))

                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun setCatPositiveResponses(responseID: String, catName: String) : List<BrowseQuestionnaireModel>{
        val records = ArrayList<BrowseQuestionnaireModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.SURVEY_RESPONSE_TABLE} WHERE ${Constants.DATABASE.SURVEY_RESPONSE_ID} = '$responseID' AND class_name = '$catName' AND s_response = 'YES'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = BrowseQuestionnaireModel()
                list.response_question = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_QUESTION))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun setCatNegativeResponses(responseID: String, catName: String) : List<BrowseQuestionnaireModel>{
        val records = ArrayList<BrowseQuestionnaireModel>()
        val selectQuery = "SELECT * FROM ${Constants.DATABASE.SURVEY_RESPONSE_TABLE} WHERE ${Constants.DATABASE.SURVEY_RESPONSE_ID} = '$responseID' AND class_name = '$catName' AND s_response = 'NO'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()){
            do {
                val list = BrowseQuestionnaireModel()
                list.response_question = cursor.getString(cursor.getColumnIndex(Constants.DATABASE.SURVEY_RESPONSE_QUESTION))
                records.add(list)
            }while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return records
    }

    fun countYTDSubmissionScore(dept:String): Int{
        val db = this.readableDatabase
        val mCount = db.rawQuery("SELECT COUNT(*) FROM survey_review_table WHERE survey_date like '%${utils.getYear()}%' AND dept_name = '$dept' AND survey_status = 'Submitted'", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        return count
    }

    fun countResponsesByCategory(responseID:String, classID:String, assesName:String, deptName: String):Int {
        val db = this.readableDatabase
        val mCount = db.rawQuery("SELECT COUNT(*) FROM s_response_table WHERE s_response_id = '$responseID' AND class_id = '$classID' AND asses_name = '$assesName' AND dept_name = '$deptName'", null)
        //val mCount = db.rawQuery("SELECT COUNT(*) FROM survey_review_table WHERE survey_date BETWEEN '$startMonth' AND '$today'", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        return count
    }

    fun countYesResponsesByCategory(responseID:String, classID:String, assesName:String, deptName: String):Int {
        val db = this.readableDatabase
        val mCount = db.rawQuery("SELECT COUNT(*) FROM s_response_table WHERE s_response_id = '$responseID' AND class_id = '$classID' AND asses_name = '$assesName' AND dept_name = '$deptName' AND s_response = 'YES'", null)
        //val mCount = db.rawQuery("SELECT COUNT(*) FROM survey_review_table WHERE survey_date BETWEEN '$startMonth' AND '$today'", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        return count
    }

    fun countSubmissionScore(dept:String): Int{
        val db = this.readableDatabase
        val mCount = db.rawQuery("SELECT COUNT(*) FROM survey_review_table WHERE dept_name = '$dept' AND survey_status = 'Submitted' AND survey_date BETWEEN Date('$startMonth') AND Date('$today')", null)
        //val mCount = db.rawQuery("SELECT COUNT(*) FROM survey_review_table WHERE survey_date BETWEEN '$startMonth' AND '$today'", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        return count
    }

    fun sumSubmissionScore(dept:String): Int {
       val db = this.readableDatabase
       val mCount = db.rawQuery("SELECT SUM(survey_score) AS totalSUM FROM survey_review_table WHERE dept_name = '$dept' AND survey_status = 'Submitted' AND survey_date BETWEEN Date('$startMonth') AND Date('$today')", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        return count
   }

    fun sumSubmissionYTDScore(dept:String): Int {
        val db = this.readableDatabase
        val mCount = db.rawQuery("SELECT SUM(survey_score) AS totalSUM FROM survey_review_table WHERE dept_name = '$dept' AND survey_status = 'Submitted' AND survey_date like '%${utils.getYear()}%'", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        return count
    }



    fun countPositiveResponse(s_response_id: String): Int {
        val db = this.readableDatabase
        val table = Constants.DATABASE.SURVEY_RESPONSE_TABLE
        val mCount = db.rawQuery("select count(*) from $table where s_response_id = $s_response_id and s_response = 'YES'", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        return count
    }

    fun countNegativeResponse(s_response_id: String): Int {
        val db = this.readableDatabase
        val table = Constants.DATABASE.SURVEY_RESPONSE_TABLE
        val mCount = db.rawQuery("select count(*) from $table where s_response_id = $s_response_id and s_response = 'NO'", null)
        mCount.moveToFirst()
        val count = mCount.getInt(0)
        mCount.close()
        return count
    }

    fun updateSurveyResponse(survey_id: String, deptName:String, question_response: String, survey_comment: String, survey_photo: String, survey_date: String){
        val db = this.writableDatabase
        //val getSharedPrefs = SP(this.context)
        val values = ContentValues()
        values.put(Constants.DATABASE.DEPARTMENT_NAME, deptName)
        values.put(Constants.DATABASE.SURVEY_RESPONSE, question_response)
        values.put(Constants.DATABASE.SURVEY_COMMENT, survey_comment)
        values.put(Constants.DATABASE.SURVEY_PHOTO, survey_photo)
        values.put(Constants.DATABASE.SURVEY_DATE, survey_date)
        values
        try {
            db.update(Constants.DATABASE.SURVEY_RESPONSE_TABLE,values,"survey_id=$survey_id AND ${Constants.DATABASE.SURVEY_RESPONSE_ID} = ${getSharedPrefs.getdata("surveyID")}", null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    fun updateScores(deptName: String, mtdScore: String, ytdScore: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.DATABASE.DEPARTMENT_NAME, deptName)
        values.put(Constants.DATABASE.ASSES_MTD, mtdScore)
        values.put(Constants.DATABASE.ASSES_YTD, ytdScore)
        values
        try {
            db.update(Constants.DATABASE.DEPARTMENT_TABLE,values,"dept_name = '$deptName'", null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    fun insertSurveyResponse(survey_index:Int ,survey_id: String, class_id:String, qID:String, className:String, assessName: String, deptName:String, survey_question: String, question_response: String, survey_comment: String, survey_photo: String, survey_date: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.DATABASE.SURVEY_ID, survey_index)
        values.put(Constants.DATABASE.SURVEY_RESPONSE_ID, survey_id)
        values.put(Constants.DATABASE.CLASSIFICATION_ID, class_id)
        values.put(Constants.DATABASE.QUESTIONNIRE_ID, qID)
        values.put(Constants.DATABASE.CLASSIFICATION_NAME, className)
        values.put(Constants.DATABASE.ASSESSMENT_NAME, assessName)
        values.put(Constants.DATABASE.DEPARTMENT_NAME, deptName)
        values.put(Constants.DATABASE.SURVEY_RESPONSE_QUESTION, survey_question)
        values.put(Constants.DATABASE.SURVEY_RESPONSE, question_response)
        values.put(Constants.DATABASE.SURVEY_COMMENT, survey_comment)
        values.put(Constants.DATABASE.SURVEY_PHOTO, survey_photo)
        values.put(Constants.DATABASE.SURVEY_DATE, survey_date)
        values
        try {
            db.insertWithOnConflict(Constants.DATABASE.SURVEY_RESPONSE_TABLE,null,values, SQLiteDatabase.CONFLICT_IGNORE)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    fun insertCatScore(responseID: String, catName: String, summaryScore:String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.DATABASE.SURVEY_RESPONSE_ID, responseID)
        values.put(Constants.DATABASE.CAT_NAME, catName)
        values.put(Constants.DATABASE.CAT_SCORE, summaryScore)

        values
        try {
            db.insertWithOnConflict(Constants.DATABASE.CAT_SCORE_TABLE,null,values, SQLiteDatabase.CONFLICT_IGNORE)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    fun updateCatResponse(survey_id: String, catName:String, score: String){
        val db = this.writableDatabase
        //val getSharedPrefs = SP(this.context)
        val values = ContentValues()
        values.put(Constants.DATABASE.CAT_SCORE, score)
        values
        try {
            db.update(Constants.DATABASE.CAT_SCORE_TABLE,values,"s_response_id='$survey_id' AND cat_name='$catName'", null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    fun selectLastPosition(): Int {
        val db = this.readableDatabase
        val table = Constants.DATABASE.COUNT_TABLE
        val mCount = db.rawQuery("select ${Constants.DATABASE.PREVIOUS_POSITION} from $table WHERE ${Constants.DATABASE.COUNT_ID} = 1", null)
        mCount.moveToFirst()
        val count = mCount.getColumnIndex(Constants.DATABASE.PREVIOUS_POSITION)
        mCount.close()
        return count
    }

    fun saveResponses(responseID: String, deptName: String, surveyDate: String, surveyTrainer: String, surveyTotalScore : String, surveyStatus: String){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.DATABASE.SURVEY_RESPONSE_ID, responseID)
        values.put(Constants.DATABASE.DEPARTMENT_NAME, deptName)
        values.put(Constants.DATABASE.SURVEY_DATE, surveyDate)
        values.put(Constants.DATABASE.SURVEY_TRAINER, surveyTrainer)
        values.put(Constants.DATABASE.SURVEY_SCORE, surveyTotalScore)
        values.put(Constants.DATABASE.SURVEY_STATUS, surveyStatus)
        values
        try {
            db.insertWithOnConflict(Constants.DATABASE.SURVEY_REVIEW_SCORE_TABLE,null,values, SQLiteDatabase.CONFLICT_IGNORE)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    companion object {
        var myDataBase: SQLiteDatabase? = null
        private const val DB_NAME = Constants.DATABASE.DB_NAME
    }
}