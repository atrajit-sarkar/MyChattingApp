package com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme

import androidx.compose.ui.graphics.Color
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.R

// Define a common base type
interface Theme {
    val ownMessageColor: Color
    val notOwnMessageColor: Color
    val ownBorderColor: Color
    val notOwnBorderColor: Color
    val lockColor: Color
    val viewOnceColor: Color
    val lockOpenColor: Color
    val openedColor: Color
    val chatAppBackGroudImage: Int
}

object DeshiRussianTheme : Theme {
    override val ownMessageColor: Color = Color(0xFF976CE0)
    override val notOwnMessageColor: Color = Color(0xFF4E5050)
    override val ownBorderColor: Color = Color(0xFF512D98)
    override val notOwnBorderColor: Color = Color(0xFF8D9191)
    override val lockColor: Color = Color(0xFFAFA6A6)
    override val viewOnceColor: Color = Color(0xFFFFEB3B)
    override val lockOpenColor: Color = Color(0x92CFDAD8)
    override val openedColor: Color = Color(0x92CFDAD8)
    override val chatAppBackGroudImage: Int = R.drawable.deshirussian

}

object DefaultTheme : Theme {
    override val ownMessageColor: Color = Color(0xFF009688)
    override val notOwnMessageColor: Color = Color(0xFF2D2C2C)
    override val ownBorderColor: Color = Color(0xFF035047)
    override val notOwnBorderColor: Color = Color(0xFF474949)
    override val lockColor: Color = Color(0xFFAFA6A6)
    override val viewOnceColor: Color = Color(0xFFC7C4C2)
    override val lockOpenColor: Color = Color(0x92DFE5E0)
    override val openedColor: Color = Color(0x92CFDAD8)
    override val chatAppBackGroudImage: Int = R.drawable.chatappbackground

}

object LalKhujiTheme : Theme {
    override val ownMessageColor: Color = Color(0xFFEF467E)
    override val notOwnMessageColor: Color = Color(0xFF503030)
    override val ownBorderColor: Color = Color(0xFFAD3B64)
    override val notOwnBorderColor: Color = Color(0xFF2F111A)
    override val lockColor: Color = Color(0xFFC58080)
    override val viewOnceColor: Color = Color(0xFFF5AEC3)
    override val lockOpenColor: Color = Color(0xFFEABED2)
    override val openedColor: Color = Color(0xFFF1CAD4)
    override val chatAppBackGroudImage: Int = R.drawable.lalkhuji

}

object NilJyoniTheme : Theme {
    override val ownMessageColor: Color = Color(0xFF03A9F4)
    override val notOwnMessageColor: Color = Color(0xFF094157)
    override val ownBorderColor: Color = Color(0xFF08608A)
    override val notOwnBorderColor: Color = Color(0xFF042E3F)
    override val lockColor: Color = Color(0xFF248DE1)
    override val viewOnceColor: Color = Color(0xFFC4DFEE)
    override val lockOpenColor: Color = Color(0xFFB2BBEA)
    override val openedColor: Color = Color(0xFFA7D1F8)
    override val chatAppBackGroudImage: Int = R.drawable.niljyoni

}

class CustomTheme(
    viewModel: ChatAppViewModel
) : Theme {
    override val ownMessageColor: Color = viewModel.ownMessageColor.value
    override val notOwnMessageColor: Color = viewModel.notOwnMessageColor.value
    override val ownBorderColor: Color = viewModel.ownBorderColor.value
    override val notOwnBorderColor: Color = viewModel.notOwnBorderColor.value
    override val lockColor: Color = viewModel.lockColor.value
    override val viewOnceColor: Color = viewModel.viewOnceColor.value
    override val lockOpenColor: Color = viewModel.lockOpenColor.value
    override val openedColor: Color = viewModel.openedColor.value
    override val chatAppBackGroudImage: Int = R.drawable.chatappbackground

}

object WallpaperTheme {
    val Mountains = R.drawable.mountains
    val Sunset = R.drawable.sunset
    val Ocean = R.drawable.ocean
    val Beach = R.drawable.beach

}
