package com.example.data.model

data class TourPackage(
    val id: Int,
    val durationType: String, // "oneday", "halfday"
    val type: String, // "nature", "culture"
    val titleTh: String,
    val titleEn: String,
    val titleZh: String,
    val titleKo: String,
    val durationTh: String,
    val durationEn: String,
    val durationZh: String,
    val durationKo: String,
    val placesTh: String,
    val placesEn: String,
    val placesZh: String,
    val placesKo: String,
    val transportTh: String,
    val transportEn: String,
    val transportZh: String,
    val transportKo: String,
    val highlightTh: String,
    val highlightEn: String,
    val highlightZh: String,
    val highlightKo: String,
    val priceTh: String,
    val priceEn: String,
    val priceZh: String,
    val priceKo: String
) {
    fun getTitle(lang: Language): String = when (lang) {
        Language.TH -> titleTh
        Language.EN -> titleEn
        Language.ZH -> titleZh
        Language.KO -> titleKo
    }

    fun getDuration(lang: Language): String = when (lang) {
        Language.TH -> durationTh
        Language.EN -> durationEn
        Language.ZH -> durationZh
        Language.KO -> durationKo
    }

    fun getPlaces(lang: Language): String = when (lang) {
        Language.TH -> placesTh
        Language.EN -> placesEn
        Language.ZH -> placesZh
        Language.KO -> placesKo
    }

    fun getTransport(lang: Language): String = when (lang) {
        Language.TH -> transportTh
        Language.EN -> transportEn
        Language.ZH -> transportZh
        Language.KO -> transportKo
    }

    fun getHighlight(lang: Language): String = when (lang) {
        Language.TH -> highlightTh
        Language.EN -> highlightEn
        Language.ZH -> highlightZh
        Language.KO -> highlightKo
    }

    fun getPrice(lang: Language): String = when (lang) {
        Language.TH -> priceTh
        Language.EN -> priceEn
        Language.ZH -> priceZh
        Language.KO -> priceKo
    }

    fun getCategoryLabel(lang: Language): String = when (type.lowercase()) {
        "culture" -> when (lang) {
            Language.TH -> "สายวัฒนธรรม"
            Language.EN -> "Culture"
            Language.ZH -> "文化风情"
            Language.KO -> "문화 탐방"
        }
        "nature" -> when (lang) {
            Language.TH -> "สายธรรมชาติ"
            Language.EN -> "Nature"
            Language.ZH -> "自然风光"
            Language.KO -> "자연 힐링"
        }
        "city" -> when (lang) {
            Language.TH -> "สายเมือง"
            Language.EN -> "City Lifestyle"
            Language.ZH -> "城市漫游"
            Language.KO -> "도심 시티"
        }
        else -> when (lang) {
            Language.TH -> "สายวัฒนธรรม"
            Language.EN -> "Culture"
            Language.ZH -> "文化风情"
            Language.KO -> "문화 탐방"
        }
    }
}

data class BusStop(
    val nameTh: String,
    val nameEn: String,
    val nameZh: String,
    val nameKo: String,
    val descTh: String,
    val descEn: String,
    val descZh: String,
    val descKo: String,
    val lat: Double,
    val lng: Double,
    val routes: List<String>
) {
    fun getName(lang: Language): String = when (lang) {
        Language.TH -> nameTh
        Language.EN -> nameEn
        Language.ZH -> nameZh
        Language.KO -> nameKo
    }

    fun getDesc(lang: Language): String = when (lang) {
        Language.TH -> descTh
        Language.EN -> descEn
        Language.ZH -> descZh
        Language.KO -> descKo
    }
}
