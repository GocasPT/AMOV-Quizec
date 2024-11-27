package pt.isec.amov.quizec.ui.viewmodels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainScree(
    viewModel: QuizecViewModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            Text(
                text = "Sou uma batata feliz :)",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(255, 0, 0, 50)
                    )
            )
            viewModel.quizList?.getQuizList()?.forEach {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0, 255, 0, 50)
                        )
                ) {
                    Text(
                        text = it.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0, 255, 0, 50)
                            )
                    )
                    it.questions.forEach {
                        Text(
                            text = it.title + " - " + it.type,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0, 0, 255, 50)
                                )
                        )
                    }
                }
            }
        }
    }
}