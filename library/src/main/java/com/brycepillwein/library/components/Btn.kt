package com.brycepillwein.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brycepillwein.library.theme.LocalAppColors

@Composable
fun Btn(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  shape: Shape = RoundedCornerShape(6.dp),
  borderWidth: Dp = 1.dp,

  backgroundColor: Color = LocalAppColors.current.pink,
  borderColor: Color = LocalAppColors.current.pink,

  disabledBorderColor: Color = LocalAppColors.current.hsl70,
  disabledColor: Color = LocalAppColors.current.hsl70,

  contentPadding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
  content: @Composable () -> Unit
) {
  val bgColor = if (enabled) backgroundColor else disabledColor
  val brdColor = if (enabled) borderColor else disabledBorderColor

  Box(
    modifier = modifier
      .clip(shape)
      .background(bgColor, shape)
      .border(borderWidth, brdColor, shape)
      .clickable(enabled = enabled, onClick = onClick)
      .padding(contentPadding),
    contentAlignment = Alignment.Center
  ) {
    content()
  }
}
