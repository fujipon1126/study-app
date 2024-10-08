package com.example.study_app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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

        rule.onNodeWithText("Navigate RequestPermissionComposable")
            .assertIsDisplayed()
            .performClick()

        Truth.assertThat(expectString).isEqualTo("NavigateToRequest")
    }

    @Test
    fun `PhotoPickerComposable遷移テスト`() {
        var expectString = ""
        rule.setContent {
            MainComposable(
                onNavigateToPhotoPicker = {
                    expectString = "NavigateToPhotoPicker"
                }
            )
        }

        rule.onNodeWithText("Navigate PhotoPickerComposable")
            .assertIsDisplayed()
            .performClick()

        Truth.assertThat(expectString).isEqualTo("NavigateToPhotoPicker")
    }

    @Test
    fun `ZoomImageComposable遷移テスト`() {
        var expectString = ""
        rule.setContent {
            MainComposable(
                onNavigateToZoomImage = {
                    expectString = "NavigateToZoomImage"
                }
            )
        }

        rule.onNodeWithText("Navigate ZoomImageComposable")
            .assertIsDisplayed()
            .performClick()

        Truth.assertThat(expectString).isEqualTo("NavigateToZoomImage")
    }

    @Test
    fun `QiitaListScreen遷移テスト`() {
        var expectString = ""
        rule.setContent {
            MainComposable(
                onQiitaApi = {
                    expectString = "QiitaApi"
                }
            )
        }

        rule.onNodeWithText("Qiita Api Test Composable")
            .assertIsDisplayed()
            .performClick()

        Truth.assertThat(expectString).isEqualTo("QiitaApi")
    }
}