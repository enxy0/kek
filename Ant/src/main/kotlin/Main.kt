/* Copyright 2000-2021 JetBrains s.r.o. and contributors.
 * Use of this source code is governed by the Apache 2.0 license that can
 * be found in the LICENSE file.
 *
 * Программа: Ant
 *
 * Описание программы: построение пути муравья Лэнгтона.
 * Муравей Лэнгтона  -  это  двухмерная  машина Тьюринга с простыми
 * правилами, изобретённая Крисом Лэнгтоном.
 *
 * Муравей движется согласно следующим правилам:
 * (1) на  чёрном квадрате - повернуть на 90° влево,  изменить цвет
 * квадрата на противоположный,  сделать шаг вперёд на следующую клет-
 * ку;
 * (2) на белом квадрате - повернуть на 90° вправо,  изменить  цвет
 * квадрата на противоположный,  сделать шаг вперёд на следующую клет-
 * ку.
 *
 * Язык: Kotlin 1.5.21 (JVM) + Jetpack Compose Desktop (Windows, Linux, macOS)
 * Автор: Бушуев Никита Федорович, 4 курс ИСТ
 * Дата: 23.10.21
 */

import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

private const val INITIAL_TABLE_SIZE = 50
private val WINDOWS_SIZE = 800.dp
private val SETTINGS_PANEL_HEIGHT = 60.dp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ant",
        state = rememberWindowState(width = WINDOWS_SIZE, height = WINDOWS_SIZE)
    ) {
        App()
    }
}

@Composable
@Preview
fun App() {
    val size by remember { mutableStateOf(INITIAL_TABLE_SIZE) }
    val initialPoint by remember { mutableStateOf(Point(size / 2, size / 2)) }
    val scope = rememberCoroutineScope()
    var steps by remember { mutableStateOf(1) }
    var stepsText by remember { mutableStateOf(steps.toString()) }
    var byStep by remember { mutableStateOf(false) }
    var isExecuting by remember { mutableStateOf(false) }
    var speed by remember { mutableStateOf(0) }
    var speedText by remember { mutableStateOf(speed.toString()) }
    var count = 0
    val pointsStateFlow = if (byStep) {
        provideAntPathFlow(size, initialPoint, isExecuting)
            .take(steps)
            .onStart {
                count = 0
            }
            .onEach {
                if (speed > 0 || !byStep) {
                    stepsText = (++count).toString()
                }
                delay(speed.toLong())
            }
            .stateIn(scope, SharingStarted.WhileSubscribed(), State(generateEmptyList(size)))
    } else {
        provideAntPathFlow(size, initialPoint, isExecuting)
            .onStart {
                count = 0
            }
            .onEach {
                if (speed > 0 || !byStep) {
                    stepsText = (++count).toString()
                }
                delay(speed.toLong())
            }
            .stateIn(scope, SharingStarted.WhileSubscribed(), State(generateEmptyList(size)))
    }
    DesktopMaterialTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val pointsState by pointsStateFlow.collectAsState(State())
            Row {
                TextField(
                    value = stepsText,
                    onValueChange = { text ->
                        val value = text.toIntOrNull() ?: 0
                        if (value > 0) {
                            isExecuting = true
                            byStep = true
                            steps = value
                        }
                        stepsText = text
                    },
                    shape = RectangleShape,
                    label = { Text("Шаг") },
                    modifier = Modifier.weight(1f).height(SETTINGS_PANEL_HEIGHT)
                )
                Spacer(modifier = Modifier.size(8.dp))
                TextField(
                    value = speedText,
                    onValueChange = { text ->
                        val value = text.toIntOrNull() ?: 0
                        speed = value
                        speedText = text
                    },
                    shape = RectangleShape,
                    label = { Text("Задержка вывода (в мс)") },
                    modifier = Modifier.weight(1f).height(SETTINGS_PANEL_HEIGHT)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Column(modifier = Modifier.height(SETTINGS_PANEL_HEIGHT).weight(1f)) {
                    Button(
                        onClick = {
                            isExecuting = true
                            byStep = false
                            steps = 1
                        },
                        content = { Text(text = "Выполнить", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        shape = RectangleShape
                    )
                    Spacer(Modifier.height(2.dp))
                    Button(
                        onClick = {
                            byStep = true
                            isExecuting = false
                        },
                        content = { Text(text = "Сбросить", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        shape = RectangleShape
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Table(pointStates = pointsState.data)
        }
    }
}

@Composable
fun ColumnScope.Table(
    pointStates: List<List<PointState>>
) {
    pointStates.forEach { states ->
        Row(Modifier.weight(1f)) {
            states.forEach { state ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(
                            minWidth = 500.dp,
                            minHeight = 500.dp
                        )
                        .background(Color.LightGray)
                        .padding(1.dp)
                        .background(state.color)
                )
            }
        }
    }
}

private fun provideAntPathFlow(size: Int, initialPoint: Point, isExecuting: Boolean) = flow {
    if (!isExecuting) return@flow
    var list = generateEmptyList(size)
    emit(State(list))
    var prevItem = PointState(initialPoint, Color.Black, Direction.BOTTOM)
    list = list.deepCopy(prevItem)
    emit(State((list)))
    while (true) {
        val direction = when (prevItem.color) {
            Color.Black -> when (prevItem.direction) {
                Direction.TOP -> Direction.LEFT
                Direction.BOTTOM -> Direction.RIGHT
                Direction.LEFT -> Direction.BOTTOM
                Direction.RIGHT -> Direction.TOP
                else -> break
            }
            else -> when (prevItem.direction) {
                Direction.TOP -> Direction.RIGHT
                Direction.BOTTOM -> Direction.LEFT
                Direction.LEFT -> Direction.TOP
                Direction.RIGHT -> Direction.BOTTOM
                else -> break
            }
        }
        val point = when (direction) {
            Direction.TOP -> Point(prevItem.point.x, prevItem.point.y - 1)
            Direction.BOTTOM -> Point(prevItem.point.x, prevItem.point.y + 1)
            Direction.LEFT -> Point(prevItem.point.x + 1, prevItem.point.y)
            Direction.RIGHT -> Point(prevItem.point.x - 1, prevItem.point.y)
        }
        val item = if (point.y in 0 until size && point.x in 0 until size) {
            list[point.y][point.x]
        } else {
            break
        }
        val newItem = item.copy(
            color = if (item.color == Color.White) Color.Black else Color.White,
            direction = direction
        )
        prevItem = newItem
        list = list.deepCopy(newItem)
        emit(State(list))
    }
}.flowOn(Dispatchers.Default)

private fun generateEmptyList(size: Int) = MutableList(size) { y ->
    MutableList(size) { x -> PointState(Point(x, y), Color.White) }
}

private fun MutableList<MutableList<PointState>>.deepCopy(item: PointState) = MutableList(size) { y ->
    MutableList(size) { x -> if (item.point.x == x && item.point.y == y) item else this[y][x].copy(direction = null) }
}

data class Point(val x: Int, val y: Int)

enum class Direction {
    TOP, BOTTOM, LEFT, RIGHT;
}

data class State(val data: List<List<PointState>> = emptyList())

data class PointState(
    val point: Point,
    var color: Color,
    var direction: Direction? = null,
) {
    override fun toString(): String {
        return "PointState((${point.x}, ${point.y}), ${if (color == Color.Black) "Black" else "White"}, direction=$direction)"
    }
}
