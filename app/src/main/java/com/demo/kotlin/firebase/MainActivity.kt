package com.demo.kotlin.firebase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.net.URL

class MainActivity : AppCompatActivity () {

    override fun onSaveInstanceState (outState: Bundle) {
        Allo.i ("onSaveInstanceState $javaClass")
        super.onSaveInstanceState (outState)
    }

    override fun onRestoreInstanceState (savedInstanceState: Bundle) {
        Allo.i ("onRestoreInstanceState $javaClass")
        super.onRestoreInstanceState (savedInstanceState)
    }

    override fun onCreate (savedInstanceState: Bundle?) {
        Allo.i ("onCreate $javaClass")
        super.onCreate (savedInstanceState)
        
        try {
            setContentView (R.layout.activity_main)
            rotateFirebase ()
            enableNotificationPermission ()
        } catch (e: Exception) { e.printStackTrace () }
    }

    public override fun onStart () {
        Allo.i ("onStart $javaClass")
        super.onStart ()
    }

    public override fun onRestart () {
        Allo.i ("onRestart $javaClass")
        super.onRestart ()
    }

    override fun onResume () {
        Allo.i ("onResume $javaClass")
        super.onResume ()

        try {
            rotateNotification ()
        } catch (e: Exception) { e.printStackTrace () }
    }

    public override fun onPause () {
        Allo.i ("onPause $javaClass")
        super.onPause ()
    }

    public override fun onStop () {
        Allo.i ("onStop $javaClass")
        super.onStop ()
    }

    public override fun onDestroy () {
        Allo.i ("onDestroy $javaClass")
        super.onDestroy ()
    }

    // 푸시 알림의 데이터를 활용하기 위해 반드시 override 해야함 (특히 카카오톡과 같은 다중 알림)
    override fun onNewIntent (intent: Intent) {
        Allo.i ("onNewIntent $javaClass")
        super.onNewIntent (intent)

        try {
            setIntent (intent)
        } catch (e: Exception) { e.printStackTrace () }
    }

    private fun enableNotificationPermission () {
        Allo.i ("enableNotificationPermission $javaClass")
        
        try {
            var enabled = false
            val managedPackages = NotificationManagerCompat.getEnabledListenerPackages (this)
            val packageName = packageName
            for (managedPackage in managedPackages) {
                if (packageName == managedPackage) {
                    enabled = true
                    break
                }
            }
            if (!enabled) {
                startActivity (Intent ("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
            }
        } catch (e: Exception) { e.printStackTrace () }
    }

    private fun rotateNotification () {
        Allo.i ("rotateNotification $javaClass")

        try {
            val params = intent
            if (null != params) {
                if (null != params.getStringExtra (Allo.CUBE_LINK)) {
                    val link = params.getStringExtra (Allo.CUBE_LINK)
                    Allo.i ("Check link [$link]")
                    try {
                        URL (link) // 유효하지 않은 경우엔 오류서 스킵함
                        startActivity (Intent(Intent.ACTION_VIEW).setData (Uri.parse (link)))
                    } catch (x: Exception) { x.printStackTrace () }
                    // 푸시 알림 패러미터 재실행 방지를 위해 데이터 삭제요
                    // 예 : (데이터 삭제를 안하면) 띄워진 외부 링크 확인후 앱으로 넘어오면 다시 외부 링크를 띄움 (무한 반복)
                    params.removeExtra (Allo.CUBE_LINK)
                }
            }
        } catch (e: Exception) { e.printStackTrace () }
    }

    private fun rotateFirebase () {
        Allo.i ("rotateFirebase $javaClass")

        try {
            FirebaseMessaging.getInstance ().token
                .addOnCompleteListener (OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Allo.i ("getInstanceId failed " + task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result
                    Handler ().postDelayed ({ rotateToken (token) }, 100)
                })
        } catch (e: Exception) { e.printStackTrace () }
    }

    private fun rotateToken(token: String) {
        Allo.i ("rotateToken $javaClass")

        try {
            registDevice (token)
            val sharedPreferences = getPreferences (MODE_PRIVATE)
            val editor = sharedPreferences.edit ()
            editor.putString (Allo.CUBE_TOKEN, token)
            editor.commit ()
        } catch (e: Exception) { e.printStackTrace () }
    }

    private fun registDevice(token: String) {
        Allo.i ("registDevice $javaClass")

        try {
            object : Thread() {
                override fun run () {
                    // 필요시 로컬 및 리모트 서버 연동하여 저장함
                    Allo.i ("Check token [$token]")
                }
            }.start ()
        } catch (e: Exception) { e.printStackTrace () }
    }
}