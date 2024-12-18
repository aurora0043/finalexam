package com.example.a1111

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a1111.ui.theme._1111Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _1111Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var isHomeScreen by remember { mutableStateOf(true) }
    var isKnowledgeCardScreen by remember { mutableStateOf(false) }
    var isTestScreen by remember { mutableStateOf(false) }

    if (isHomeScreen) {
        HomeScreen(
            onNavigateToKnowledgeCard = { isKnowledgeCardScreen = true; isHomeScreen = false },
            onNavigateToTest = { isTestScreen = true; isHomeScreen = false }
        )
    } else if (isKnowledgeCardScreen) {
        KnowledgeCardScreen(
            onBackToHome = { isHomeScreen = true; isKnowledgeCardScreen = false }
        )
    } else if (isTestScreen) {
        TestScreen(onBackToHome = { isHomeScreen = true; isTestScreen = false })
    }
}

@Composable
fun HomeScreen(onNavigateToKnowledgeCard: () -> Unit, onNavigateToTest: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8DC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "生活知識王",
                color = Color.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onNavigateToKnowledgeCard,
                    modifier = Modifier.size(width = 120.dp, height = 60.dp)
                ) {
                    Text(text = "知識卡", fontSize = 18.sp)
                }

                Button(
                    onClick = onNavigateToTest,
                    modifier = Modifier.size(width = 120.dp, height = 60.dp)
                ) {
                    Text(text = "開始測驗", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun KnowledgeCardScreen(onBackToHome: () -> Unit) {
    val images = listOf(
        R.drawable.brushteethphoto,
        R.drawable.firephoto,
        R.drawable.machinephoto,
        R.drawable.playingphoto,
        R.drawable.swimmingphoto
    )

    val soundFiles = listOf(
        R.raw.brushteeth,
        R.raw.fire2,
        R.raw.machine,
        R.raw.playing,
        R.raw.swimming
    )

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = currentIndex) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, soundFiles[currentIndex])
            mediaPlayer?.start()
        } catch (e: Exception) {
            println("音檔載入或播放錯誤: ${e.message}")
        }
    }

    DisposableEffect(key1 = currentIndex) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8DC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = images[currentIndex]),
                contentDescription = "Knowledge Card Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex -= 1
                        }
                    },
                    modifier = Modifier.size(width = 120.dp, height = 60.dp)
                ) {
                    Text(text = "上一頁", fontSize = 18.sp)
                }

                Button(
                    onClick = {
                        if (currentIndex < images.size - 1) {
                            currentIndex += 1
                        }
                    },
                    modifier = Modifier.size(width = 120.dp, height = 60.dp)
                ) {
                    Text(text = "下一頁", fontSize = 18.sp)
                }
            }
        }

        Button(
            onClick = onBackToHome,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("返回首頁")
        }
    }
}

@Composable
fun TestScreen(onBackToHome: () -> Unit) {
    val images = listOf(
        R.drawable.brushqa,
        R.drawable.fireqa,
        R.drawable.mechineqa,
        R.drawable.playqa,
        R.drawable.swimmingqa
    )

    val soundFiles = listOf(
        R.raw.brush,
        R.raw.fire,
        R.raw.elementary,
        R.raw.play,
        R.raw.swim
    )

    // 正確答案
    val correctAnswers = listOf(true, false, false, false, false) // O為true，X為false

    // 記錄分數
    var score by remember { mutableStateOf(0) }
    var answerHistory = remember { mutableStateListOf<String>() }

    // 當前題數
    var currentIndex by remember { mutableStateOf(0) }

    // 標誌，標記是否停止計分
    var isTestCompleted by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(key1 = currentIndex) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, soundFiles[currentIndex])
            mediaPlayer?.start()
        } catch (e: Exception) {
            println("音檔載入或播放錯誤: ${e.message}")
        }
    }

    DisposableEffect(key1 = currentIndex) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8DC))
    ) {
        if (!isTestCompleted) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 70.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = images[currentIndex]),
                    contentDescription = "Test Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (correctAnswers[currentIndex]) {
                                score += 20
                                answerHistory.add("第${currentIndex + 1}題: 正確")
                            } else {
                                answerHistory.add("第${currentIndex + 1}題: 錯誤")
                            }

                            if (currentIndex < images.size - 1) {
                                currentIndex += 1
                            } else {
                                isTestCompleted = true
                            }
                        },
                        modifier = Modifier.size(width = 120.dp, height = 60.dp)
                    ) {
                        Text(text = "O", fontSize = 18.sp)
                    }

                    Button(
                        onClick = {
                            if (!correctAnswers[currentIndex]) {
                                score += 20
                                answerHistory.add("第${currentIndex + 1}題: 正確")
                            } else {
                                answerHistory.add("第${currentIndex + 1}題: 錯誤")
                            }

                            if (currentIndex < images.size - 1) {
                                currentIndex += 1
                            } else {
                                isTestCompleted = true
                            }
                        },
                        modifier = Modifier.size(width = 120.dp, height = 60.dp)
                    ) {
                        Text(text = "X", fontSize = 18.sp)
                    }
                }
            }
        }

        // 顯示計分表並置中
        if (isTestCompleted) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFFFFF))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    answerHistory.forEach {
                        Text(
                            text = it,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Text(
                        text = "總得分: $score",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }

        Button(
            onClick = onBackToHome,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("返回首頁")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    _1111Theme {
        HomeScreen(onNavigateToKnowledgeCard = {}, onNavigateToTest = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKnowledgeCardScreen() {
    _1111Theme {
        KnowledgeCardScreen(onBackToHome = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTestScreen() {
    _1111Theme {
        TestScreen(onBackToHome = {})
    }
}
