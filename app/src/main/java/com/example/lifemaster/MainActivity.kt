package com.example.lifemaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lifemaster.ui.theme.LifeMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeCacheDir.setReadOnly()
        setContent {
            LifeMasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TopLevel()
                }
            }
        }
    }
}

class ToDoViewModel : ViewModel() {
    // viewmodel 내에서는 destruction 이 불가능하다.
    var isDialogOpen by mutableStateOf(false) // delegated property
    val onOpenDialog: (Boolean) -> Unit = {
        isDialogOpen = it
    }
}

@Composable
fun TopLevel(toDoViewModel: ToDoViewModel = viewModel()) {
    Scaffold { // Scaffold 를 왜 쓰는거지?
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_todo),
                    contentDescription = "todo 아이콘"
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "할일",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
                Button(onClick = {
                    toDoViewModel.isDialogOpen = true
                }) {
                    Text(text = "추가")
                }
            }
            CustomDialog(
                toDoViewModel.isDialogOpen,
                toDoViewModel.onOpenDialog
            )
        }
    }
}

@Composable
fun CustomDialog(
    isDialogOpen: Boolean,
    onOpenDialog: (Boolean) -> Unit
) {
    var todoContent by remember { mutableStateOf("") }
    if (isDialogOpen) {
        // 다이얼로그 열기
        Dialog(onDismissRequest = {
            onOpenDialog(false)
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
                        text = "할일 추가",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text = "제목",
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
                            onClick = { onOpenDialog(false) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFFF0F4F5),
                                contentColor = Color.Black
                            ) // 참고: https://kotlinworld.com/243 + 왜 elevation 이 적용됐지?
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

@Composable
fun ToDoItem() {
    var isChecked by remember { mutableStateOf(false) }
    var todoContent by remember { mutableStateOf("") }
    Surface {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
            Text(text = todoContent)
            Image(
                painter = painterResource(id = R.drawable.ic_timer),
                contentDescription = "pomodoro timer"
            )
            Image(
                painter = painterResource(id = R.drawable.ic_show_more),
                contentDescription = "화면 이동"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LifeMasterTheme {
        TopLevel()
    }
}