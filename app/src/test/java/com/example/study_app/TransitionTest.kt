package com.example.study_app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.study_app.composable.MainComposable
import com.google.common.truth.Truth
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransitionTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun `RequestPermissionComposable遷移テスト`() {
        var expectString = ""
        rule.setContent {
            MainComposable(
                onNavigateToRequest = {
                    expectString = "NavigateToRequest"
                }
            )
        }

        rule.onRoot().printToLog("test")

        rule.onNodeWithText("Navigate RequestPermissionComposable")
            .assertIsDisplayed()
            .performClick()

        Truth.assertThat(expectString).isEqualTo("NavigateToRequest")
    }
}