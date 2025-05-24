package com.brycepillwein.library.theme

import androidx.compose.ui.graphics.Color

data class AppColorScheme(
  val pink: Color,
  val yellow: Color,
  val blue: Color,

  val hsl5: Color,
  val hsl10: Color,
  val hsl15: Color,
  val hsl20: Color,
  val hsl25: Color,
  val hsl30: Color,
  val hsl40: Color,
  val hsl50: Color,
  val hsl60: Color,
  val hsl70: Color,
  val hsl80: Color,
  val hsl90: Color,
  val hsl95: Color,
  val hsl98: Color,
  val hsl100: Color,
)

val AppColors = AppColorScheme(
  pink = Color(0xFFFF3EB5),
  yellow = Color(0xFFFFE900),
  blue = Color(0xFF2695a3),

  hsl5 = Color(0xFF0d0d0d),
  hsl10 = Color(0xFF1a1a1a),
  hsl15 = Color(0xFF262626),
  hsl20 = Color(0xFF333333),
  hsl25 = Color(0xFF404040),
  hsl30 = Color(0xFF4d4d4d),
  hsl40 = Color(0xFF666666),
  hsl50 = Color(0xFF808080),
  hsl60 = Color(0xFF999999),
  hsl70 = Color(0xFFb3b3b3),
  hsl80 = Color(0xFFcccccc),
  hsl90 = Color(0xFFe6e6e6),
  hsl95 = Color(0xFFf2f2f2),
  hsl98 = Color(0xFFfafafa),
  hsl100 = Color(0xFFffffff),
)
