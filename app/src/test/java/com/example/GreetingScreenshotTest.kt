package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.ui.screens.PeronChatScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.PeronChatViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PeronChatRobolectricTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testChatScreenInitialState() {
    val viewModel = PeronChatViewModel()
    composeTestRule.setContent {
      MyApplicationTheme {
        PeronChatScreen(viewModel = viewModel)
      }
    }

    composeTestRule.onNodeWithTag("peron_chat_screen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("october_17_banner_card").assertIsDisplayed()
    composeTestRule.onNodeWithTag("chat_input_text_field").assertIsDisplayed()
  }
}
