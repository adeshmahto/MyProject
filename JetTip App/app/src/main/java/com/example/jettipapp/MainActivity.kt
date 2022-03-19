package com.example.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.conponent.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.util.calculateTotalPerPerson
import com.example.jettipapp.util.calculateTotalTip
import com.example.jettipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

             MyApp{   // u can add any of composable fn inside this which can shown to the main surface of the app
                MainContent()
             }


        }
    }
}

@Composable
fun MyApp(content :@Composable ()-> Unit){       // creater function which can take any of composable function
    JetTipAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}



//@Preview
@Composable
fun TopHeader(totalPerPerson: Double=0.0){       // it can use to change the money text
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
        .height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp)))
    , color = Color(0xFFD38CD8)
    ) {
        Column(modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
            ,horizontalAlignment = Alignment.CenterHorizontally) {
            val total="%.2f".format(totalPerPerson)
            Text(text = "Total Per Person",
                style = MaterialTheme.typography.h5)
            Text(text = "$$total",style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold)

        }
    }
}       // sec 1

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){
    var splitByState=remember{
        mutableStateOf(1)
    }
    val range=IntRange(1,100)
    val tipAmountState=remember{
        mutableStateOf(0.0)
    }
    val totalPerPersonState=remember{
        mutableStateOf(0.0)
    }
    Column(modifier = Modifier.padding(all=12.dp)) {
        BillForm( splitByState=splitByState,
            range=range,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState){ billAmt->                 // 89-> here we receiving  the amount from the textview
            Log.d("Amit","MainContent: ${billAmt.toInt() * 200}")

        }
    }



}
@ExperimentalComposeUiApi
@Composable
fun BillForm(modifier:Modifier= Modifier,
             range: IntRange=1..100,
             splitByState:MutableState<Int>,
             tipAmountState:MutableState<Double>,
             totalPerPersonState: MutableState<Double>,

             onValChange: (String) ->Unit={}) {


    val totalBillState=remember{ // for the text
        mutableStateOf("")
    }
    val validState =remember(totalBillState.value){     // this value refer when we enter any number in the textview
        totalBillState.value.trim().isNotEmpty()          // trim() is work like giving space btx text or padding , isNotempty return boolean

    }


    val sliderPostionState=remember{            // initialization of slider variable
        mutableStateOf(0f)
    }
    val tipPercentage=(sliderPostionState.value*100).toInt()   // it convert the slider value float to int which mul from 100 so we can see actual % value


    val FocusManager= LocalFocusManager.current
    // we use this becuz when we done with this keyboard , key board should hide after this
    TopHeader(totalPerPerson =totalPerPersonState.value)
    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp,color = Color.LightGray)
    ) {
        Column(modifier=Modifier.padding(6.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment=Alignment.Start) {

            InputField(valueState = totalBillState
                , labelId = "Enter Bill"      // icon at the top will be shown
                , enabled = true
                , isSingledLine = true,
                onAction = KeyboardActions{
                    if(!validState) return@KeyboardActions
                    // Todo-onvaluechanged
                    onValChange(totalBillState.value.trim())
                    FocusManager?.clearFocus()
                })
            if(validState){
                // 1st row
               Row(modifier = Modifier.padding(3.dp),
               horizontalArrangement = Arrangement.Start) {
                   Text(text = "Split", modifier = Modifier.align(
                       alignment =Alignment.CenterVertically
                   ))
                   Spacer(modifier = Modifier.width(120.dp))
                // 2nd row

                Row(modifier = Modifier.padding(horizontal = 3.dp),
                horizontalArrangement = Arrangement.End) {
                    RoundIconButton( imageVector = Icons.Default.Remove ,
                        onClick = { if(splitByState.value> 1) splitByState.value=splitByState.value-1 else 1
                            totalPerPersonState.value=
                                calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = tipPercentage)
                        })




                    Text(text = "${splitByState.value}",modifier =modifier.align(Alignment.CenterVertically))
                    RoundIconButton( imageVector = Icons.Default.Add ,
                        onClick = {
                            if(splitByState.value < range.last){
                                splitByState.value=splitByState.value+1
                            }
                            totalPerPersonState.value=
                                calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = tipPercentage)

                        } )

                }

               }
            Row(modifier = Modifier.padding(3.dp,vertical = 12.dp)) {
                Text(text = "Text",modifier = Modifier.align(alignment = Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(200.dp))
                
                Text(text = "$ ${tipAmountState.value}",modifier = Modifier.align(alignment = Alignment.CenterVertically))

            }
            Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "$tipPercentage%")
                Spacer(modifier = Modifier.height(14.dp))
                // Slider
                Slider(value = sliderPostionState.value, onValueChange ={newVal->
                    sliderPostionState.value=newVal
                    tipAmountState.value=
                        calculateTotalTip( totalBill = totalBillState.value.toDouble(),
                            tipPercentage = tipPercentage)       // creating a function which can calcute the tip




                },modifier=Modifier.padding(start=16.dp,end=16.dp),
                steps = 5,
                onValueChangeFinished = {


                })
            }

            }else{
                Box(){
                    
                }
           }
        }

    }


}




//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {

    }
}