package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class SongthaewRoute(
    @PrimaryKey val id: String,
    val color: String, // "red", "yellow", "green", "blue", "white"
    val isFavorite: Boolean = false,
    val isCustom: Boolean = false,
    val badgeTextTh: String,
    val badgeTextEn: String,
    val badgeTextZh: String,
    val badgeTextKo: String,
    val nameTh: String,
    val nameEn: String,
    val nameZh: String,
    val nameKo: String,
    val subTitleTh: String,
    val subTitleEn: String,
    val subTitleZh: String,
    val subTitleKo: String,
    val startTh: String,
    val startEn: String,
    val startZh: String,
    val startKo: String,
    val startDescTh: String,
    val startDescEn: String,
    val startDescZh: String,
    val startDescKo: String,
    val endTh: String,
    val endEn: String,
    val endZh: String,
    val endKo: String,
    val endDescTh: String,
    val endDescEn: String,
    val endDescZh: String,
    val endDescKo: String,
    val pathTh: String,
    val pathEn: String,
    val pathZh: String,
    val pathKo: String,
    val fareTextTh: String,
    val fareTextEn: String,
    val fareTextZh: String,
    val fareTextKo: String,
    val tipsTh: String,
    val tipsEn: String,
    val tipsZh: String,
    val tipsKo: String,
    val waypointsJson: String = "",
    val pathCoordsJson: String = ""
) {
    fun getName(lang: Language): String = when (lang) {
        Language.TH -> nameTh
        Language.EN -> nameEn
        Language.ZH -> nameZh
        Language.KO -> nameKo
    }

    fun getBadgeText(lang: Language): String = when (lang) {
        Language.TH -> badgeTextTh
        Language.EN -> badgeTextEn
        Language.ZH -> badgeTextZh
        Language.KO -> badgeTextKo
    }

    fun getSubTitle(lang: Language): String = when (lang) {
        Language.TH -> subTitleTh
        Language.EN -> subTitleEn
        Language.ZH -> subTitleZh
        Language.KO -> subTitleKo
    }

    fun getStart(lang: Language): String = when (lang) {
        Language.TH -> startTh
        Language.EN -> startEn
        Language.ZH -> startZh
        Language.KO -> startKo
    }

    fun getStartDesc(lang: Language): String = when (lang) {
        Language.TH -> startDescTh
        Language.EN -> startDescEn
        Language.ZH -> startDescZh
        Language.KO -> startDescKo
    }

    fun getEnd(lang: Language): String = when (lang) {
        Language.TH -> endTh
        Language.EN -> endEn
        Language.ZH -> endZh
        Language.KO -> endKo
    }

    fun getEndDesc(lang: Language): String = when (lang) {
        Language.TH -> endDescTh
        Language.EN -> endDescEn
        Language.ZH -> endDescZh
        Language.KO -> endDescKo
    }

    fun getPath(lang: Language): String = when (lang) {
        Language.TH -> pathTh
        Language.EN -> pathEn
        Language.ZH -> pathZh
        Language.KO -> pathKo
    }

    fun getFare(lang: Language): String = when (lang) {
        Language.TH -> fareTextTh
        Language.EN -> fareTextEn
        Language.ZH -> fareTextZh
        Language.KO -> fareTextKo
    }

    fun getTips(lang: Language): String = when (lang) {
        Language.TH -> tipsTh
        Language.EN -> tipsEn
        Language.ZH -> tipsZh
        Language.KO -> tipsKo
    }
}

data class Waypoint(
    val nameTh: String,
    val nameEn: String,
    val nameZh: String,
    val nameKo: String,
    val lat: Double,
    val lng: Double
) {
    fun getName(lang: Language): String = when (lang) {
        Language.TH -> nameTh
        Language.EN -> nameEn
        Language.ZH -> nameZh
        Language.KO -> nameKo
    }
}
