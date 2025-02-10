package com.iyr.ultrachango.utils.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.iyr.ultrachango.ui.theme.SFProMediumFontFamily
import com.iyr.ultrachango.ui.theme.frameBorder
import com.iyr.ultrachango.utils.ui.LoadingIndicator
import com.iyr.ultrachango.utils.ui.ShowKeyboard
import com.iyr.ultrachango.utils.ui.triggerHapticFeedback
import org.jetbrains.compose.resources.vectorResource
import ultrachango2.composeapp.generated.resources.Res
import ultrachango2.composeapp.generated.resources.barcode_scanner


/***
 * Text Field Standard con un placeholder como texto
 * @see TextField
 * @param modifier Modificador de la composable
 * @param value Valor del campo de texto
 * @param placeHolder Texto del placeholder
 * @param onValueChange Función que se ejecuta cuando cambia el valor del campo de texto
 * @sample MyTexField(modifier = Modifier, value = "", placeHolder = "Nombre", onValueChange = {})
 * @since 1.0
 */
@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
fun MyTexField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: @Composable Placeholder? = null,
    placeHolderText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions? = KeyboardOptions.Default,
    keyboardActions: KeyboardActions? = KeyboardActions.Default,
    colors: TextFieldColors? = OutlinedTextFieldDefaults.colors(),

    ) {

    var focusState by remember { mutableStateOf(false) }
    val requester = FocusRequester()
    val inputModeManager = LocalInputModeManager.current
    OutlinedTextField(
        modifier = modifier
            .background(Color.White, shape = customShape)
            .onFocusEvent {

            }
            .onFocusChanged { focusState = it.isFocused }
            .focusRequester(requester)
            .clickable {
                requester.requestFocus()
            },

        shape = customShape,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            if (placeholder != null) {
                placeholder
            } else {
                Text(
                    modifier = Modifier.wrapContentHeight(),
                    text = placeHolderText.toString(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontFamily = SFProMediumFontFamily(),
                        fontSize = textSize14,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    )
                )
            }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        //  colors = colors ?: TextFieldDefaults.colors(),
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        keyboardActions = keyboardActions ?: KeyboardActions.Default
    )
}



@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
fun MyTexField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable Placeholder? = null,
    placeHolderText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions? = KeyboardOptions.Default,
    keyboardActions: KeyboardActions? = KeyboardActions.Default,
    colors: TextFieldColors? = OutlinedTextFieldDefaults.colors(),

    ) {

    var focusState by remember { mutableStateOf(false) }
    val requester = FocusRequester()
    val inputModeManager = LocalInputModeManager.current
    OutlinedTextField(
        modifier = modifier
            .background(Color.White, shape = customShape)
            .onFocusEvent {

            }
            .onFocusChanged { focusState = it.isFocused }
            .focusRequester(requester)
            .clickable {
                requester.requestFocus()
            },

        shape = customShape,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            if (placeholder != null) {
                placeholder
            } else {
                Text(
                    modifier = Modifier.wrapContentHeight(),
                    text = placeHolderText.toString(),
                    style = TextStyle(
                        color = Color.Gray,
                        fontFamily = SFProMediumFontFamily(),
                        fontSize = textSize14,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    )
                )
            }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        //  colors = colors ?: TextFieldDefaults.colors(),
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        keyboardActions = keyboardActions ?: KeyboardActions.Default
    )
}

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
fun MySearchTexField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    placeHolderText: String? = "Buscar",
    placeholder: @Composable Placeholder? = null,
    keyboardOptions: KeyboardOptions? = KeyboardOptions.Default,
    keyboardActions: KeyboardActions? = KeyboardActions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors? = TextFieldDefaults.colors()
) {

    MyTexField(
        modifier = modifier,
        value = value,
        onValueChange = {
            println(it.text)
            onValueChange(it)
        },
        placeholder = placeholder,
        placeHolderText = placeHolderText,
        leadingIcon = leadingIcon ?: {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors
    )
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReusableSearchTextField(
    text: String,
    onTextChange: (String) -> Unit,
    loadingProducts: Boolean = false,
    showScannerIcon: Boolean = true,
    onScannerClick: () -> Unit,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean, FocusRequester) -> Unit,
    onScannerButtonVisibilityChange : (Boolean) -> Unit = {}, // Cuando se dispone que cambie la visiiliad del boton de Scanner
    onTextSearchRequested: (String) -> Unit = {}, // Cuando cambia el texto de busqueda
) {

    var showScannerIcon by remember { mutableStateOf(true) }

    TextField(
        value = text,
        placeholder = { Text("Nombre del producto") },
        onValueChange = {
            onTextChange(it)
         //   buttonScannerVisibilityCallback(it.isEmpty())
            onScannerButtonVisibilityChange(it.isEmpty())
            showScannerIcon = it.isEmpty()
        },
        modifier = Modifier.fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .border(1.dp, frameBorder, shape = RoundedCornerShape(12.dp))
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.hasFocus, focusRequester)
            },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
         //   vm.onProductTextInput(text)
            onTextSearchRequested(text)
        }),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Buscar",
                tint = Color.LightGray
            )
        },
        trailingIcon = {

            if ( loadingProducts == false)
            {
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            triggerHapticFeedback()
                            onTextChange("")
                            showScannerIcon = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Clear text",
                            tint = Color.Gray
                        )
                    }
                } else if (showScannerIcon) {
                    IconButton(
                        onClick = onScannerClick
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.barcode_scanner),
                            contentDescription = "Escáner",
                            tint = Color.LightGray
                        )
                    }
                }

            }
            else
            {
                LoadingIndicator(
                    modifier = Modifier.size(24.dp),
                    enabled = true
                )
            }



        /*
            if (vm.state.searchResults.size > 1) {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = vm.state.searchResultsExpanded
                )
            } else {

            */
            /*
                if (showScannerIcon) {
                    IconButton(
                        onClick = onScannerClick
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.barcode_scanner),
                            contentDescription = "Escáner",
                            tint = Color.LightGray
                        )
                    }

                    */
          //      }
          //  }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedPlaceholderColor = Color.Blue,
            focusedPlaceholderColor = Color.Red,
            focusedContainerColor = Color.White
        )
    )
}