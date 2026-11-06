package com.example.cursorfollower

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 60, 60, 60)
        }

        val guideText = TextView(this).apply {
            text = "마우스 커서 팔로워 설정\n\n1. 다른 앱 위에 표시 권한\n2. 접근성 서비스 권한\n두 가지를 모두 켜주세요."
            textSize = 18f
        }
        layout.addView(guideText)

        val overlayButton = Button(this).apply {
            text = "1. 다른 앱 위에 표시 권한 설정"
            setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                }
            }
        }
        layout.addView(overlayButton)

        val accessibilityButton = Button(this).apply {
            text = "2. 접근성 서비스 켜기 (CursorFollower 활성화)"
            setOnClickListener {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                startActivity(intent)
            }
        }
        layout.addView(accessibilityButton)

        setContentView(layout)
    }
}
