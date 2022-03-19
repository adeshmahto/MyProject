package com.example.jettipapp.conponent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun InputField(modifier: Modifier=Modifier,
               valueState:MutableState<String>,
               labelId: String,
                enabled:Boolean,
               isSingledLine:Boolean,    // text should be in single line when the time when we put number in the textview
               keyboardType: KeyboardType= KeyboardType.Number,     // keyboard will come when the u enter any number in the app
              imeAction: ImeAction= ImeAction.Next,
             onAction: KeyboardActions = KeyboardActions.Default ){
       OutlinedTextField(value = valueState.value, onValueChange ={valueState.value=it},
       label = { Text(text = labelId)},
       leadingIcon = {Icon(imageVector= Icons.Rounded.AttachMoney     // for this icon we have to add the depedencies in gradle build module
           ,contentDescription= " Money Icon")},
       singleLine = isSingledLine,
       textStyle = TextStyle(fontSize = 18.sp,
       color = MaterialTheme.colors.onBackground)
           ,modifier=modifier.padding(bottom = 10.dp,start = 10.dp,end=10.dp)
               .fillMaxWidth(),
       enabled=enabled,
           keyboardOptions = KeyboardOptions(keyboardType=keyboardType,
            imeAction = imeAction),
           keyboardActions = onAction
       )          // it is like a textview in the main content
}