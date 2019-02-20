package com.celeritassolutions.hivelet.fragment

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.celeritassolutions.hivelet.R
import com.celeritassolutions.hivelet.adapter.QuestionnaireAdapter
import com.celeritassolutions.hivelet.database.DatabaseHelper
import com.celeritassolutions.hivelet.model.QuestionnireModel
import com.celeritassolutions.hivelet.utils.SP
import com.celeritassolutions.hivelet.utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_courtesy.view.*
import kotlinx.android.synthetic.main.employee_warning_dialog.view.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class QuestionniareFragment : Fragment(){


    private var questionnaireList: List<QuestionnireModel> = ArrayList()
    private var dbHelper: DatabaseHelper? = null
    private var arrivalDashboard: RecyclerView? = null
    private lateinit var btnProceed: AppCompatButton
    private lateinit var btnHome: AppCompatImageView
    private lateinit var btnAddEmployee: AppCompatImageView
    private lateinit var titleToolbar: AppCompatTextView
    private var extraTitle: String = ""
    private var extraID: String = ""
    private var pExtraTitle: String = ""
    private var pExtraID: String = ""
    private var extraTrianer: String = ""
    private lateinit var txtDate: AppCompatTextView
    private lateinit var txtSurveyID: AppCompatTextView
    private var utils = Utils()
    private lateinit var saveSharedPrefs: SP
    private var surveyID: String = utils.generateSurveyID()
    private var currentDate: String = utils.getCurrentDate()
    private lateinit var pos: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.activity_arrival_service, container, false)
        arrivalDashboard = rootview.findViewById(R.id.arrival_dashboard)
        btnProceed = rootview.findViewById(R.id.btn_proceed)
        btnHome = rootview.findViewById(R.id.home_toolbar)
        btnAddEmployee = rootview.findViewById(R.id.btn_email)
        titleToolbar = rootview.findViewById(R.id.title_toolbar)
        txtDate = rootview.findViewById(R.id.txt_date)
        txtSurveyID = rootview.findViewById(R.id.survey_id)
        dbHelper = DatabaseHelper(this.context!!)
        saveSharedPrefs = SP(this.context!!)
        pos = saveSharedPrefs.getdata("pos")
        extraTitle = arguments?.getString("title")!!.toString()
        extraID = arguments?.getString("id")!!.toString()
        extraTrianer = arguments?.getString("trainer")!!.toString()
        pExtraID = arguments?.getString("extraID")!!.toString()
        pExtraTitle = arguments?.getString("extraTitle")!!.toString()
        titleToolbar.text = extraTitle
        txtDate.text = currentDate
        txtSurveyID.text = surveyID
        Log.d("#Trainer", extraTrianer)
        //loadQuestionnire()

        questionnaireList = dbHelper!!.selectQuestionnire(extraID)
        arrivalDashboard?.layoutManager = LinearLayoutManager(this.context)
        arrivalDashboard?.adapter =
            QuestionnaireAdapter(questionnaireList, this.context!!, object : QuestionnaireAdapter.BtnClickListener {
                override fun onBtnClick(position: Int) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(
                                context as Activity,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                            )
                        } else {
                            selectImage()
                        }
                    } else {
                        selectImage()
                    }
                }
            }, object: QuestionnaireAdapter.BtnCommentListener{
                override fun onCommentClick(position: Int) {
                    showDialog(questionnaireList[position].className, questionnaireList[position].question)
                }

            })

        btnProceed.setOnClickListener {
            saveSharedPrefs.savedata("employeeSelected", "false")
            addFragment(QuestionniareSummaryFragment(), true, extraTitle)
        }

        btnHome.setOnClickListener {
            addFragment(DashboardFragment(), false, "Dashboard")
        }

        btnAddEmployee.setOnClickListener {
            addFragment(EmployeeSetupFragment(), true, "Add Employee Setup")
        }

        for (survey_index in questionnaireList.indices) {
            //survey_index+1+1 because our questionnaire index starts from 1
            dbHelper!!.insertSurveyResponse(
                survey_index + 1,
                surveyID,
                questionnaireList[survey_index].classID,
                questionnaireList[survey_index].q_id,
                questionnaireList[survey_index].className,
                saveSharedPrefs.getdata("assessName"),
                saveSharedPrefs.getdata("deptTitle"),
                questionnaireList[survey_index].question,
                "NA",
                "NA",
                "NA",
                "NA"
            )
        }

        saveSharedPrefs.savedata("surveyID", surveyID)
        saveSharedPrefs.savedata("currentDate", currentDate)
        saveSharedPrefs.savedata("postCurrentDate", utils.postCurrentDate())
        saveSharedPrefs.savedata("lastPosition", dbHelper?.selectLastPosition().toString())
        saveSharedPrefs.savedata("trainerName", extraTrianer)

        return rootview
    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val manager = activity?.supportFragmentManager
        val ft = manager!!.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        val ldf = fragment
        val args = Bundle()
        args.putString("title", extraTitle)
        args.putString("id", extraID)
        args.putString("trainer", extraTrianer)
        args.putString("pExtraID", pExtraID)
        args.putString("pExtraTitle", pExtraTitle)
        ldf.arguments = args
        ft.replace(com.celeritassolutions.hivelet.R.id.fragment_container, fragment, tag)
        ft.commitAllowingStateLoss()
    }

    /* override fun onHandleSelection(position: Int, text: String) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             if (checkSelfPermission(context as Activity ,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                 Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                 ActivityCompat.requestPermissions(
                     context as Activity,
                     arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                 )
             } else {
                 selectImage()
             }
         } else {
             selectImage()
         }
     }*/

    private fun selectImage() {
        CropImage.activity()
            .start(getContext()!!, this);
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                var resultUri: Uri = result.uri
                Log.d("#ResultURI",resultUri.toString())
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error = result.error;
            }
        }
    }*/
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                val imageStream: InputStream = activity!!.contentResolver.openInputStream(resultUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                val encodedImage = encodeImage(selectedImage)
                Toast.makeText(context, "Image added Successfully", Toast.LENGTH_SHORT).show()
                /*final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String encodedImage = encodeImage(selectedImage);
                */
                Log.d("#ResultURI", encodedImage)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error = result.error;
            }
        }
    }


    private fun encodeImage(bm: Bitmap): String {
        var baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var b = baos.toByteArray()
        var encImage = android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);

        return encImage
    }
/*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
          Uri resultUri = result.getUri();
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
          Exception error = result.getError();
        }
      }
    }*/

    /*override fun onCommentClick(position: Int) {
        showDialog(questionnaireList[position].className, questionnaireList[position].question)
    }*/



    private fun showDialog(txtTitle:String, queText: String) {
        // Inflates the dialog with custom view
        val dialogView =
            LayoutInflater.from(activity!!.applicationContext).inflate(R.layout.activity_courtesy, null)
        val builder = AlertDialog.Builder(context!!)
            .setView(dialogView)

        val dialog = builder.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.btn_save.setOnClickListener {
            dialog.dismiss()
        }
        dialogView.txt_question.text = queText
        dialogView.txt_title.text = txtTitle
        dialogView.btn_delete.setOnClickListener {
            dialog.dismiss()
        }
    }


}



