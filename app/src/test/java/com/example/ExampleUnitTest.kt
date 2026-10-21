package com.example

import com.example.data.local.InitialData
import com.example.data.model.AppStrings
import com.example.data.model.Language
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun initialRoutes_hasSixIconicSongthaewLines() {
        val routes = InitialData.initialRoutes
        assertEquals(6, routes.size)

        val colors = routes.map { it.color }
        assertTrue(colors.contains("red"))
        assertTrue(colors.contains("yellow"))
        assertTrue(colors.contains("green"))
        assertTrue(colors.contains("blue"))
        assertTrue(colors.contains("white"))
        assertTrue(colors.contains("orange"))
    }

    @Test
    fun initialTourPackages_hasTenVerifiedPackages() {
        val packages = InitialData.tourPackages
        assertEquals(10, packages.size)
        assertTrue(packages.any { it.titleTh.contains("ดอยอินทนนท์") })
        assertTrue(packages.any { it.titleTh.contains("ดอยสุเทพ") })
        assertTrue(packages.any { it.titleTh.contains("แม่ริม") })
    }

    @Test
    fun translations_supportsFourLanguages() {
        listOf(Language.TH, Language.EN, Language.ZH, Language.KO).forEach { lang ->
            val title = AppStrings.get(lang, "app_title")
            val navHome = AppStrings.get(lang, "nav_home")
            assertEquals("Songthaew CM", title)
            assertNotNull(navHome)
            assertTrue(navHome.isNotBlank())
        }
    }
}
