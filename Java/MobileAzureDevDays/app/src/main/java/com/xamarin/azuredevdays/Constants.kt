package com.xamarin.azuredevdays

// colors
const val DEFAULT_BACKGROUND_COLOR = "#FF6F69"
const val EMOTION_COLOR_9 = "#29EB94"
const val EMOTION_COLOR_8 = "#31C774"
const val EMOTION_COLOR_7 = "#2E9C5F"
const val EMOTION_COLOR_6 = "#208946"
const val EMOTION_COLOR_5 = "#3D81DF"
const val EMOTION_COLOR_4 = "#CC2C66"
const val EMOTION_COLOR_3 = "#CB3359"
const val EMOTION_COLOR_2 = "#B01C41"
const val EMOTION_COLOR_1 = "#7F1437"

// emojis
val SAD_FACE_EMOJI = getEmojiByUnicode(0x1F61E)
val NEUTRAL_FACE_EMOJI = getEmojiByUnicode(0x1F610)
val HAPPY_FACE_EMOJI = getEmojiByUnicode(0x1F604)

private fun getEmojiByUnicode(unicode: Int): String {
    return String(Character.toChars(unicode))
}
