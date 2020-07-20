package jp.techacademy.taiyu.autoslideshowapp

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.os.Handler
import android.widget.Button
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE = 100

    private var mTimer: Timer? = null

    private var mTimerSec = 0.0


    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                getContentsInfo()
            } else {

                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        } else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       when(requestCode){
           PERMISSIONS_REQUEST_CODE ->
               if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                   getContentsInfo()
               } else {
                   //アプリを落とす機能？またはアラートダイアログを表示？
               }
       }
    }







    @SuppressLint("Recycle")
    private fun getContentsInfo() {
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )


        cursor!!.moveToFirst()
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            imageView.setImageURI(imageUri)




        moveonbutton.setOnClickListener {
            if (cursor.moveToNext()) {
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)

            } else {
                cursor.moveToFirst()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)

            }

        }


        returnbutton.setOnClickListener {
            if (cursor.moveToPrevious()) {
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)

            } else {
                cursor.moveToLast()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)

            }

        }

        centerbutton.setOnClickListener {

            if (centerbutton.text == "再生") {

                centerbutton.text = "停止"

                moveonbutton.isEnabled = false
                returnbutton.isEnabled = false

                mTimer = Timer()

                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        if (cursor.moveToNext()) {
                            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id = cursor.getLong(fieldIndex)
                            val imageUri = ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                id
                            )

                            mHandler.post {
                                imageView.setImageURI(imageUri)
                            }

                        } else {
                            cursor.moveToFirst()
                            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                            val id = cursor.getLong(fieldIndex)
                            val imageUri =
                                ContentUris.withAppendedId(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    id
                                )

                            mHandler.post {
                                imageView.setImageURI(imageUri)
                            }

                        }
                    }
                }, 2000, 2000)

            } else {

                centerbutton.text = "再生"

                moveonbutton.isEnabled = true
                returnbutton.isEnabled = true

                if (mTimer != null) {
                    mTimer!!.cancel()
                    mTimer = null
                }

            }
        }
    }
}














