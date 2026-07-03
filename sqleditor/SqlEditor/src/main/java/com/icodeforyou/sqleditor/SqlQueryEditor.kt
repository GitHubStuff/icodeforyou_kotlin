package com.icodeforyou.sqleditor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * A full-screen overlay for editing and returning a SQL query.
 *
 * Renders as an opaque black surface that obscures everything beneath it. A
 * centered [title] header sits above the editor, which is inset 24dp on each
 * side with a light-purple border, uses a monospace typeface, shows [minLines]
 * lines at rest, and grows as the user types more. State is hoisted: this
 * composable owns only the in-progress edit buffer and returns the final text
 * through [onAccept]. Validating or executing the query is the caller's job.
 *
 * When [useHaptics] is true, ACCEPT and CANCEL emit semantic haptic feedback
 * (confirm and reject respectively). This is part of the widget's own button
 * behavior and degrades gracefully on devices without haptics.
 *
 * @param initialQuery text the editor opens with.
 * @param onAccept invoked with the current query text when ACCEPT is tapped.
 * @param onCancel invoked when CANCEL is tapped or the back gesture dismisses.
 * @param title header shown above the input box; its size is [fontSize] * 1.25.
 * @param fontSize size of the editor text, in sp.
 * @param minLines minimum visible lines before growth.
 * @param useHaptics whether ACCEPT/CANCEL emit haptic feedback.
 */
@Composable
fun SqlQueryEditor(
    initialQuery: String,
    onAccept: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Raw Sql Statement",
    fontSize: Float = 22f,
    minLines: Int = 5,
    useHaptics: Boolean = true,
) {
    var value by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(initialQuery))
    }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
    ) {
        SqlQueryEditorContent(
            value = value,
            onValueChange = { value = it },
            onAccept = { onAccept(value.text) },
            onCancel = onCancel,
            title = title,
            fontSize = fontSize,
            minLines = minLines,
            useHaptics = useHaptics,
            modifier = modifier,
        )
    }
}

@Composable
private fun SqlQueryEditorContent(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    title: String,
    fontSize: Float,
    minLines: Int,
    useHaptics: Boolean,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(EditorBackground),
        contentAlignment = BiasAlignment(horizontalBias = 0f, verticalBias = -0.3f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Text(
                text = title,
                color = EditorForeground,
                fontFamily = FontFamily.Monospace,
                fontSize = (fontSize * 1.25f).sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = BoxBorder,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .padding(12.dp),
                textStyle = TextStyle(
                    color = EditorForeground,
                    fontFamily = FontFamily.Monospace,
                    fontSize = fontSize.sp,
                ),
                cursorBrush = SolidColor(EditorForeground),
                minLines = minLines,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(100.dp, Alignment.CenterHorizontally),
            ) {
                TextButton(
                    onClick = {
                        if (useHaptics) {
                            haptics.performHapticFeedback(HapticFeedbackType.Reject)
                        }
                        onCancel()
                    },
                    border = BorderStroke(1.dp, ButtonBorder),
                ) {
                    Text(text = "CANCEL", color = EditorForeground)
                }
                TextButton(
                    onClick = {
                        if (useHaptics) {
                            haptics.performHapticFeedback(HapticFeedbackType.Confirm)
                        }
                        onAccept()
                    },
                    border = BorderStroke(1.dp, ButtonBorder),
                ) {
                    Text(text = "ACCEPT", color = EditorForeground)
                }
            }
        }
    }
}

private val EditorBackground = Color(0xFF000000)
private val EditorForeground = Color(0xFFE0E0E0)
private val BoxBorder = Color(0xFFB39DDB)
private val ButtonBorder = Color(0xFFFFEB3B)

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun SqlQueryEditorContentPreview() {
    SqlQueryEditorContent(
        value = TextFieldValue("SELECT *\nFROM users\nWHERE active = 1"),
        onValueChange = {},
        onAccept = {},
        onCancel = {},
        title = "Raw Sql Statement",
        fontSize = 22f,
        minLines = 5,
        useHaptics = true,
    )
}