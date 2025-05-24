package com.brycepillwein.library.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.brycepillwein.library.theme.LocalAppColors

@Composable
fun Tt(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = LocalAppColors.current.hsl5, // default colour
  style: TextStyle = TextStyle.Default,
  fontSize: TextUnit = TextUnit.Unspecified,
  fontWeight: FontWeight? = null,
  lineHeight: TextUnit = TextUnit.Unspecified,
  maxLines: Int = Int.MAX_VALUE,
  overflow: TextOverflow = TextOverflow.Clip,
  softWrap: Boolean = true
) {
  Text(
    text = text,
    modifier = modifier,
    color = color,
    style = style,
    fontSize = fontSize,
    fontWeight = fontWeight,
    lineHeight = lineHeight,
    maxLines = maxLines,
    overflow = overflow,
    softWrap = softWrap
  )
}