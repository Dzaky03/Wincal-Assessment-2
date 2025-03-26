package com.dzaky3022.asesment1.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dzaky3022.asesment1.ui.theme.*

@Composable
fun CustomInput(
    label: String = "",
    hint: String,
    helperText: String? = null,
    initialValue: String = "",
    isEnabled: Boolean = true,
    isDigitOnly: Boolean = false,
    isRequired: Boolean = false,
    maxLength: Int? = null,
    maxLines: Int = 1,
    imeAction: ImeAction = ImeAction.Next,
    onChange: (String) -> Unit = {},
    suffixIcon: @Composable (() -> Unit)? = null,
    prefixIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (label.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontWeight = FontWeight.SemiBold,
                )
                if (isRequired) {
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "*", color = Danger, fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        OutlinedTextField(
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                selectionColors = TextSelectionColors(
                    handleColor = Color.White,
                    backgroundColor = Color.Black.copy(alpha = .5f)
                ),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Gray,
                disabledIndicatorColor = Gray,
            ),
            value = initialValue,
            onValueChange = {
                if (maxLength == null || it.length <= maxLength) {
                    onChange(it)
                }
            },
            enabled = isEnabled,
            singleLine = maxLines == 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = when {
                    isDigitOnly -> KeyboardType.Number
                    else -> KeyboardType.Text
                },
                imeAction = imeAction
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
            placeholder = { Text(text = hint, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            suffix = suffixIcon,
            prefix = prefixIcon
        )

        if (helperText != null) {
            Text(
                text = helperText,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
