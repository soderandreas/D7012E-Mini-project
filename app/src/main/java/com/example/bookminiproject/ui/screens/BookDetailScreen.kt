package com.example.bookminiproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookminiproject.ui.theme.BookMiniProjectTheme
import coil.compose.AsyncImage
import com.example.bookminiproject.R
import com.example.bookminiproject.model.AuthorKey
import com.example.bookminiproject.model.Description
import com.example.bookminiproject.utils.Constants
import com.example.bookminiproject.viewmodel.BooksDBViewModel
import com.example.bookminiproject.viewmodel.SelectedWorkUiState

@Composable
fun BookDetailScreen(
    booksDBViewModel: BooksDBViewModel,
    onAuthorClick: (AuthorKey) -> Unit,
    onSubjectClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when(val selectedWorkUiState = booksDBViewModel.selectedWorkUiState) {
        is SelectedWorkUiState.Success -> {
            Column (modifier = modifier) {
                Row {
                    val cover = selectedWorkUiState.work.covers?.get(0)
                    if (cover != null) {
                        AsyncImage(
                            model = Constants.COVER_IMAGE_BASE_URL + cover + Constants.COVER_SIZE_M,
                            placeholder = painterResource(R.drawable.no_image_placeholder),
                            contentDescription = selectedWorkUiState.work.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .weight(2f)
                                .height(270.dp)
                                .padding(horizontal = 4.dp, vertical = 8.dp),
                        )
                    }
                    Column (modifier = Modifier.weight(3f)) {
                        Text(
                            text = selectedWorkUiState.work.title,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        BookAuthorText(selectedWorkUiState.author, selectedWorkUiState.work.authors[0].author, onAuthorClick)
                        Spacer(modifier = Modifier.size(8.dp))
                        val descriptionValue = when (val description = selectedWorkUiState.work.description) {
                            is Description.Text -> description.value
                            is Description.Object -> description.value
                            null -> "Description of ${selectedWorkUiState.work.title} is not available."
                        }
                        Text(
                            text = descriptionValue,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Column  {
                    Text(
                        text = "Subjects: ",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    BookSubjects(
                        selectedWorkUiState.work.subjects,
                        onSubjectClick,
                        Modifier
                            .padding(horizontal = 2.dp)
                    )
                }
                Row {
                    Text(
                        text = "Favorite",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    Switch(checked = selectedWorkUiState.favorite, onCheckedChange = {
                        if (it)
                            booksDBViewModel.saveWork(selectedWorkUiState.work, selectedWorkUiState.author)
                        else
                            booksDBViewModel.deleteWork(selectedWorkUiState.work, selectedWorkUiState.author)
                    })
                }
            }
        }
        is SelectedWorkUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is SelectedWorkUiState.Error -> {
            Text(
                text = "Error: Something went wrong!",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun BookAuthorText(author: String, authorId: AuthorKey, onAuthorClick: (AuthorKey) -> Unit) {
    val annotatedText = buildAnnotatedString {
        append("by ")

        pushStringAnnotation(
            tag = "name",
            annotation = author
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue, fontWeight = FontWeight.Bold
            )
        ) {
            append(author)
        }

        pop()
    }

    ClickableText(text = annotatedText, onClick = { offset ->
        annotatedText.getStringAnnotations(
            tag = "name", start = offset, end = offset
        ).firstOrNull()?.let { annotation ->
            Log.d("Clicked Author", annotation.item)
            onAuthorClick(authorId)
        }
    }, modifier = Modifier.padding(top = 24.dp))
}

@Composable
fun BookSubjects(
    subjects: List<String>,
    onSubjectClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow {
        items(subjects) {subject ->
            Button(
                onClick = {
                    onSubjectClick(subject)
                    Log.d("BookSubject", "Clicked on subject $subject")
                },
                shape = RoundedCornerShape(10),
                modifier = modifier
            ){
                Text(
                    subject
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookDetailPreview() {
    BookMiniProjectTheme {
        /*BookDetailScreen(
            work = Work(
                key = "/works/OL45804W",
                title = "Fantastic Mr Fox",
                covers = listOf(6498519, 8904777, 108274, 233884, 1119236, -1, 10222599, 10482837, 3216657, 10519563, 10835922, 10835924, 10861366, 10883671, 8760472, 12583098, 10482548, 10831929, 10835926, 12333895, 12498647, 7682784, 12143357, 12781739, 3077458, 13200133, 13215770, 13269612),
                description = "The main character of Fantastic Mr. Fox is an extremely clever anthropomorphized fox named Mr. Fox. He lives with his wife and four little foxes. In order to feed his family, he steals food from the cruel, brutish farmers named Boggis, Bunce, and Bean every night.\r\n\r\nFinally tired of being constantly outwitted by Mr. Fox, the farmers attempt to capture and kill him. The foxes escape in time by burrowing deep into the ground. The farmers decide to wait outside the hole for the foxes to emerge. Unable to leave the hole and steal food, Mr. Fox and his family begin to starve. Mr. Fox devises a plan to steal food from the farmers by tunneling into the ground and borrowing into the farmer's houses.\r\n\r\nAided by a friendly Badger, the animals bring the stolen food back and Mrs. Fox prepares a great celebratory banquet attended by the other starving animals and their families. Mr. Fox invites all the animals to live with him underground and says that he will provide food for them daily thanks to his underground passages. All the animals live happily and safely, while the farmers remain waiting outside in vain for Mr. Fox to show up.",
                subjects = listOf("Animals",
                    "Hunger",
                    "Open Library Staff Picks",
                    "Juvenile fiction",
                    "Children's stories, English",
                    "Foxes",
                    "Fiction",
                    "Zorros",
                    "Ficción juvenil",
                    "Tunnels",
                    "Interviews",
                    "Farmers",
                    "Children's stories",
                    "Rats",
                    "Welsh Authors",
                    "English Authors",
                    "Thieves",
                    "Tricksters",
                    "Badgers",
                    "Children's fiction",
                    "Foxes, fiction",
                    "Underground",
                    "Renards",
                    "Romans, nouvelles, etc. pour la jeunesse",
                    "Children's literature",
                    "Plays",
                    "Children's plays",
                    "Children's stories, Welsh",
                    "Agriculteurs",
                    "Fantasy fiction",
                    "Children's plays, English"),
                authors = listOf(AuthorRole(author = AuthorKey( "Ronald Dahl")))
            ),
            {}
        )*/
    }
}
