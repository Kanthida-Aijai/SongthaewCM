package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.data.model.SongthaewRoute

object MapUtils {
    fun getStartCoordinates(route: SongthaewRoute): Pair<Double, Double> {
        return when (route.id) {
            "r1" -> 18.7877 to 98.9934 // ประตูท่าแพ
            "r2" -> 18.7890 to 98.9620 // ตลาดต้นพยอม
            "r3" -> 18.7898 to 99.0003 // ตลาดวโรรส
            "r4" -> 18.7898 to 99.0003 // ตลาดวโรรส
            "r5" -> 18.7997 to 98.9862 // ขนส่งช้างเผือก
            "r6" -> 18.7997 to 98.9862 // ขนส่งช้างเผือก
            "r7" -> 18.7898 to 99.0003 // ตลาดวโรรส
            "r8" -> 18.7898 to 99.0003 // ตลาดวโรรส
            "r9" -> 18.7997 to 98.9862 // ขนส่งช้างเผือก
            "r10" -> 18.8052 to 98.9567 // หน้า มช.
            else -> 18.7898 to 99.0003
        }
    }

    fun openGoogleMaps(context: Context, lat: Double, lng: Double, label: String = "") {
        try {
            val uri = "geo:$lat,$lng?q=$lat,$lng(${Uri.encode(label.ifEmpty { "จุดรอรถ" })})"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
                )
                context.startActivity(webIntent)
            }
        } catch (e: Exception) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lng")
            )
            context.startActivity(webIntent)
        }
    }
}
