package com.celeritassolutions.hivelet.database

class Constants {
    object DATABASE {
        const val DB_PATH = "/databases/"
        const val DB_NAME = "HiveletDB.db"
        const val DB_VERSION = 14
        var ASSESSMENT_ID = "asses_id"
        var DEPARTMENT_ID = "dept_id"
        var DEPARTMENT_NAME = "dept_name"
        var ASSES_MTD = "asses_mtd"
        var ASSES_YTD = "asses_ytd"
        var ASSES_SCORE = "asses_score"
        var ASSESSMENT_NAME = "asses_name"
        var CLASSIFICATION_ID = "class_id"
        var CLASSIFICATION_NAME = "class_name"
        var QUESTIONNIRE_ID = "q_id"
        var QUESTIONNIRE = "question"
        var QUESTIONNIRE_TABLE = "Questionnire"
        var CLASSIFICATION_TABLE = "Classification"
        var ASSESSMENT_TABLE = "Assessments"
        var DEPARTMENT_TABLE = "Department"

        var SURVEY_REVIEW_SCORE_TABLE = "survey_review_table"
        var SURVEY_ID = "survey_id"
        var SURVEY_DATE = "survey_date"
        var SURVEY_TRAINER = "survey_trainer"
        var SURVEY_MTD_SCORE = "mtd_score"
        var SURVEY_YTD_SCORE = "ytd_score"
        var SURVEY_SCORE = "survey_score"
        var SURVEY_STATUS = "survey_status"

        var SURVEY_RESPONSE_TABLE = "s_response_table"
        var SURVEY_RESPONSE_ID = "s_response_id"
        var SURVEY_RESPONSE_QUESTION = "s_response_question"
        var SURVEY_RESPONSE = "s_response"
        var SURVEY_COMMENT = "s_comment"
        var SURVEY_PHOTO = "s_photo"

        var COUNT_TABLE = "c_table"
        var COUNT_ID = "c_id"
        var PREVIOUS_POSITION = "c_pos"

        var EMPLOYEE_TABLE = "Employee"
        var EMPLOYEE_ID = "emp_id"
        var EMPLOYEE_NAME = "emp_name"
        var EMPLOYEE_DESIGNATION = "emp_designation"
        var EMPLOYEE_STATUS = "emp_status"
        var LAST_UPDATE = "last_update"

        var COUNT_MTD_TABLE = "Counts_MTD"
        var COUNT_HOUSEKEEPING = "housekeeping_count"
        var COUNT_RESERVATION = "reservation_count"
        var COUNT_FRONTDESK = "frontdesk_count"
        var COUNT_LOSS = "loss_count"
        var COUNT_SPA = "spa_count"
        var COUNT_HOTELBAR = "hotelbar_count"
        var COUNT_ROOMSERVICE = "roomservice_count"
        var COUNT_PUBLICSPACE = "publicspace_count"

        var CAT_REVIEW_TABLE = "cat_review_table"
        var CAT_ASSES = "cat_asses"
        var CAT_CLEANLINESS = "cleanliness_score"
        var CAT_AMBIANCE = "ambiance_score"
        var CAT_COURTESY = "courtesy_score"
        var CAT_EFFECIENCY = "effeciency_score"
        var CAT_ELEMENT = "elements_score"
        var CAT_FOOD = "food_score"
        var CAT_GRACIOUS = "gracious_score"
        var CAT_COMFORT = "comfort_score"
        var CAT_CLASSIFICATION = "noclassification_score"
        var CAT_SERVICE = "service_score"
        var CAT_STAFF = "staff_score"
        var CAT_TECHNICAL = "technical_score"
        var CAT_SUMMARY_SCORE = "summary_score"

        var CAT_SCORE_TABLE = "cat_score_table"
        var CAT_NAME = "cat_name"
        var CAT_SCORE = "cat_score"

        val CREATE_CAT_SCORE_TABLE = "CREATE TABLE" + CAT_SCORE_TABLE + "" +
                "(" + SURVEY_RESPONSE_ID + "INTEGER" +
                CAT_NAME +"TEXT"+
                CAT_SCORE +"INTEGER )"



        val CREATE_CAT_REVIEW_TABLE = "CREATE TABLE" + CAT_REVIEW_TABLE + "" +
                "(" + SURVEY_RESPONSE_ID + "INTEGER" +
                CAT_ASSES +"TEXT"+
                CAT_AMBIANCE +"INTEGER"+
                CAT_CLEANLINESS +"INTEGER"+
                CAT_COURTESY +"INTEGER"+
                CAT_EFFECIENCY +"INTEGER"+
                CAT_ELEMENT +"INTEGER"+
                CAT_FOOD +"INTEGER"+
                CAT_GRACIOUS +"INTEGER"+
                CAT_COMFORT +"INTEGER"+
                CAT_CLASSIFICATION +"INTEGER"+
                CAT_SERVICE +"INTEGER"+
                CAT_STAFF +"INTEGER"+
                CAT_TECHNICAL +"INTEGER"+
                CAT_SUMMARY_SCORE +"INTEGER )"

        val CREATE_COUNT_MTD_TABLE = "CREATE TABLE" + COUNT_MTD_TABLE + "" +
                "(" + COUNT_HOUSEKEEPING + "INTEGER" +
                COUNT_RESERVATION +"INTEGER"+
                COUNT_FRONTDESK +"INTEGER"+
                COUNT_LOSS +"INTEGER"+
                COUNT_SPA +"INTEGER"+
                COUNT_HOTELBAR +"INTEGER"+
                COUNT_ROOMSERVICE +"INTEGER"+
                COUNT_PUBLICSPACE +"INTEGER )"

        val CREATE_EMPLOYEE_TABLE = "CREATE TABLE" + EMPLOYEE_TABLE + "" +
                "(" + EMPLOYEE_ID + "INTEGER PRIMARY KEY" +
                EMPLOYEE_NAME +"TEXT NULL"+
                DEPARTMENT_NAME +"TEXT NULL"+
                EMPLOYEE_DESIGNATION +"TEXT NULL"+
                EMPLOYEE_STATUS +"TEXT NULL"+
                LAST_UPDATE +"TEXT NULL )"

        val CREATE_SURVEY_RESPONSE_TABLE = "CREATE TABLE " + SURVEY_RESPONSE_TABLE + "" +
                "(" + SURVEY_ID + "INTEGER NULL" +
                SURVEY_RESPONSE_ID +"INTEGER NULL"+
                CLASSIFICATION_ID +"INTEGER NULL"+
                QUESTIONNIRE_ID +"INTEGER NULL"+
                CLASSIFICATION_NAME +"TEXT NULL"+
                ASSESSMENT_NAME +"TEXT NULL"+
                DEPARTMENT_NAME + "TEXT NULL" +
                SURVEY_DATE + "DATE NULL" +
                SURVEY_RESPONSE_QUESTION + "TEXT NULL" +
                SURVEY_RESPONSE + "TEXT NULL" +
                SURVEY_COMMENT + "TEXT NULL" +
                SURVEY_PHOTO + "TEXT NULL )"

        val CREATE_SCORE_REVIEW_TABLE = "CREATE TABLE " + SURVEY_REVIEW_SCORE_TABLE + "" +
                "(" + SURVEY_RESPONSE_ID + "INTEGER PRIMARY KEY AUTO INCREMENT" +
                DEPARTMENT_NAME + "TEXT NULL" +
                SURVEY_DATE + "DATE NULL" +
                SURVEY_TRAINER + "TEXT NULL" +
                SURVEY_SCORE + "TEXT NULL" +
                SURVEY_STATUS + "TEXT NULL" +
                ")"

        val CREATE_ASSESSMENT_TABLE_QUERY = "CREATE TABLE " + ASSESSMENT_TABLE + "" +
                "(" + ASSESSMENT_ID + " INTEGER PRIMARY KEY not null UNIQUE," +
                DEPARTMENT_ID + " INTEGER not null," +
                ASSESSMENT_NAME + " TEXT null)"


        val CREATE_CLASSIFICATION_TABLE_QUERY = "CREATE TABLE " + CLASSIFICATION_TABLE + "" +
                "(" + CLASSIFICATION_ID + " INTEGER PRIMARY KEY not null UNIQUE," +
                CLASSIFICATION_NAME + " TEXT null)"

        val CREATE_DEPARTMENT_TABLE_QUERY = "CREATE TABLE " + DEPARTMENT_TABLE + "" +
                "(" + DEPARTMENT_ID + " INTEGER PRIMARY KEY not null UNIQUE," +
                DEPARTMENT_NAME + " TEXT null," +
                ASSES_MTD + " INTEGER null," +
                ASSES_YTD + " INTEGER null," +
                ASSES_SCORE + " INTEGER null)"


        val CREATE_QUESTIONNIRE_TABLE_QUERY = "CREATE TABLE " + QUESTIONNIRE_TABLE + "" +
                "(" + QUESTIONNIRE_ID + " INTEGER PRIMARY KEY not null UNIQUE," +
                ASSESSMENT_ID + " INTEGER not null," +
                CLASSIFICATION_ID + " INTEGER not null," +
                QUESTIONNIRE + "TEXT NULL)"

        val CREATE_COUNT_TABLE_QUERY = "CREATE TABLE " + COUNT_TABLE + "" +
                "(" + COUNT_ID + " INTEGER PRIMARY KEY not null UNIQUE," +
                PREVIOUS_POSITION + "INTEGER NOT NULL)"

        val DROP_ASSESSMENT_QUERY = "DROP TABLE IF EXISTS $ASSESSMENT_TABLE "
        val DROP_DEPARTMENT_QUERY = "DROP TABLE IF EXISTS $DEPARTMENT_TABLE "
        val DROP_CLASSIFICATION_QUERY = "DROP TABLE IF EXISTS $CLASSIFICATION_TABLE"
        val DROP_QUESTIONNIRE_QUERY = "DROP TABLE IF EXISTS $QUESTIONNIRE_TABLE "
        val DROP_SCORE_REVIEW_TABLE = "DROP TABLE IF EXISTS $SURVEY_REVIEW_SCORE_TABLE "
        val DROP_SURVEY_RESPONSE_TABLE = "DROP TABLE IF EXISTS $SURVEY_RESPONSE_TABLE"
        val DROP_COUNT_TABLE = "DROP TABLE IF EXISTS $COUNT_TABLE"
        val DROP_EMPLOYEE_TABLE = "DROP TABLE IF EXISTS $EMPLOYEE_TABLE"
        val DROP_COUNT_MTD_TABLE = "DROP TABLE IF EXISTS $COUNT_MTD_TABLE"
        val DROP_CAT_REVIEW_TABLE = "DROP TABLE IF EXISTS $CAT_REVIEW_TABLE"
        val DROP_CAT_SCORE_TABLE = "DROP TABLE IF EXISTS $CAT_REVIEW_TABLE"
    }
}