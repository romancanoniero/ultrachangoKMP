package com.iyr.ultrachango.utils.ui.device

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.utils.ui.elements.ThinButton

@Composable
fun CopyableLink(link: String) {
    val clipboardManager = rememberClipboardManager()
    var copied by remember { mutableStateOf(false) }

    Row(modifier = Modifier.padding(8.dp).height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically

        ) {
        ClickableText(
            text = AnnotatedString(link),
            onClick = { clipboardManager.copyToClipboard(link) },
            modifier = Modifier.weight(1f)
                .fillMaxHeight()
                .border(1.dp, Color.Gray).align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(4.dp))
        ThinButton(
            modifier = Modifier,
            onClick = {
                clipboardManager.copyToClipboard(link)
                copied = true
            },
            text = if (copied) "Copiado" else "Copiar"
        )
    }
}