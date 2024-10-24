package com.example.lifemaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.lifemaster.ui.theme.LifeMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CustomDialogEx()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun CustomDialogEx() {
    var isDialogOpen by remember { mutableStateOf(false) }
    var todoContent by remember { mutableStateOf("") }
    Column {
        Button(onClick = { isDialogOpen = true }) {
            Text("추가")
        }
    }
    if(isDialogOpen) {
        // 다이얼로그 열기
        Dialog(onDismissRequest = {
            isDialogOpen = false
        }) {
            // slot api 인 content 안에서 다이얼로그 화면을 커스터마이징 하면 된다.
            // 항상 Surface 로 감싸주는 것이 좋다. Surface 로 감싸주지 않으면 무슨 일이 일어날까? 한번 확인해보자.
            // Surface 로 감싸주지 않으면 배경색이 보이지 않는다.
            Surface(
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text ="할일 추가",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text ="제목",
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    TextField(
                        value = todoContent,
                        onValueChange = { todoContent = it },
                        singleLine = true, // 2줄 넘어가면 ui 망가져서 일단 설정함
                        label = {
                            Text(
                                text = "할일을 입력하세요",
                                fontWeight = FontWeight.Light
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFF0F4F5),
                            textColor = Color(0x13131399),
                            unfocusedIndicatorColor = Color.Transparent // underline remove
                        )
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Row {
                        Button(
                            onClick = { isDialogOpen = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(16.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF0F4F5), contentColor = Color.Black) // https://kotlinworld.com/243 왜 elevation 이 적용됐지?
                        ) {
                            Text(
                                text = "취소하기",
                                fontWeight = FontWeight.Light
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(16.dp),
                        ) {
                            Text(
                                text = "추가하기",
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LifeMasterTheme {
        Greeting("Android")
    }
}