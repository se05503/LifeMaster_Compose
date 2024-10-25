package com.example.lifemaster

import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    color = MaterialTheme.colors.background,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    TopLevel()
                }
            }
        }
    }
}

class ToDoViewModel : ViewModel() {
    // 다이얼로그 열렸는 지 유무
    var isDialogOpen by mutableStateOf(false) // delegated property

    // 할일 목록
    var todoList = mutableStateListOf<ToDoItem>()

    // 다이얼로그 내용
    var todoContent by mutableStateOf("")

    // 다이얼로그에서 추가 버튼 클릭 시 이벤트
    val onSubmit: (String) -> Unit = { content ->
        val key = (todoList.lastOrNull()?.key ?: 0) + 1 // key 는 0 이 아닌 1부터 시작한다.
        todoList.add(ToDoItem(key, content)) // recomposition 시점
        todoContent = ""
    }

    // 할일 목록에서 체크박스 눌렀을 때 이벤트
    val onToggle: (Int, Boolean) -> Unit = { key, toggle ->
        val i = todoList.indexOfFirst {
            it.key == key
        }
        todoList[i] =
            todoList[i].copy(isFinished = toggle) // list 에서는 원소의 프로퍼티가 아닌 원소 자체를 대체해야 recomposition 이 일어남
    }
}

@Composable
fun TopLevel(toDoViewModel: ToDoViewModel = viewModel()) {
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
            Button(
                onClick = { toDoViewModel.isDialogOpen = true },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "추가")
            }
        }
        Spacer(modifier = Modifier.size(8.dp))
        LazyColumn {
            items(toDoViewModel.todoList) { todoItem ->
                ToDoItem(todoItem, toDoViewModel.onToggle)
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
        CustomDialog(
            toDoViewModel.isDialogOpen,
            { toDoViewModel.isDialogOpen = it },
            toDoViewModel.todoContent,
            { toDoViewModel.todoContent = it },
            toDoViewModel.onSubmit
        )
    }

}

@Composable
fun CustomDialog(
    isDialogOpen: Boolean,
    onOpenDialog: (Boolean) -> Unit,
    toDoContent: String,
    onValueChanged: (String) -> Unit,
    onSubmit: (String) -> Unit
) {
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
                        value = toDoContent,
                        onValueChange = onValueChanged,
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
                            onClick = {
                                onSubmit(toDoContent)
                                onOpenDialog(false)
                            },
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
fun ToDoItem(
    todoItem: ToDoItem,
    onToggle: (Int, Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0x195C76C3),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            Checkbox(
                checked = todoItem.isFinished,
                onCheckedChange = { onToggle ->
                    onToggle(todoItem.key, onToggle)
                },
                colors = CheckboxDefaults.colors(
                    uncheckedColor = Color(0xFF5C76C3),
                    checkmarkColor = Color(0x4D5C76C3),
                    checkedColor = Color(0x4D5C76C3) // 박스 경계와 박스 자체의 색깔을 둘 다 포함하는 파라미터임 -> 박스를 배경색과 동일하게 만들려면 커스터마이징 필요
                )
            )
            Text(
                text = todoItem.text,
                modifier = Modifier.weight(1f),
                color = Color(0xFF131313)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_timer),
                contentDescription = "pomodoro timer"
            )
            Image(
                painter = painterResource(id = R.drawable.ic_show_more),
                contentDescription = "화면 이동",
                modifier = Modifier.padding(end = 4.dp)
            )
        }
    }
}

data class ToDoItem(
    val key: Int, // 몇번째 항목 리스트인지 식별
    val text: String, // 해당 항목의 내용
    val isFinished: Boolean = false // 체크 박스 → 기본값은 false 로 설정
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LifeMasterTheme {
        TopLevel()
    }
}