package com.example.wordle.ui.screens.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.ui.theme.getEffectColors


enum class KeyStatus {
    IN_RIGHT_SPOT, IN_WORD, NOT_IN_WORD
}

@Preview
@Composable
fun Keyboard(modifier: Modifier = Modifier, hardMode: Boolean = false,) {
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        Key(text = 'A', hardMode = hardMode)
    }
}

@Composable
fun Key(
    modifier: Modifier = Modifier, text: Char,
    hardMode: Boolean = false, used: Boolean = false,
    status: KeyStatus? = null, contrast: Boolean = false,
    onclick: (Char) -> Unit = {}
) {
    Box(
        modifier = modifier
            .padding(2.5.dp)
            .height(55.dp)
            .width(35.dp)
            .background(
                color = when (status) {
                    KeyStatus.IN_RIGHT_SPOT -> getEffectColors(contrast).isInRightPosition
                    KeyStatus.IN_WORD -> getEffectColors(contrast).isInWord
                    KeyStatus.NOT_IN_WORD -> getEffectColors(contrast).notInWord
                    null -> MaterialTheme.colorScheme.onPrimary
                },
                shape = RoundedCornerShape(corner = CornerSize(4.dp))
            )
            .padding(1.dp)
            .clickable(enabled = if (hardMode) status != KeyStatus.NOT_IN_WORD else true) { onclick(text) }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text.toString().uppercase(),
                color = if (used) Color.White
                else MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


@Composable
fun ActionKey(
    modifier: Modifier = Modifier, text: String? = null,
    resourceId: Int? = null, onclick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .padding(2.5.dp)
            .height(55.dp)
            .width(55.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(corner = CornerSize(4.dp))
            )
            .padding(1.dp)
            .clickable { onclick() }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            text?.let {
                Text(text = it.uppercase(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge, fontSize = 16.sp
                )
            }
            resourceId?.let {
                Image(painter = painterResource(id = it),
                    contentDescription = "back space icon",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground))
            }
        }
    }
}