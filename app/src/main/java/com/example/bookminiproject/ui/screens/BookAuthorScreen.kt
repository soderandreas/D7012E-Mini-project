package com.example.bookminiproject.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookminiproject.R
import com.example.bookminiproject.model.Author
import com.example.bookminiproject.model.AuthorKey
import com.example.bookminiproject.model.AuthorRole
import com.example.bookminiproject.model.Biography
import com.example.bookminiproject.model.Description
import com.example.bookminiproject.model.Work
import com.example.bookminiproject.ui.theme.BookMiniProjectTheme
import com.example.bookminiproject.utils.Constants
import com.example.bookminiproject.viewmodel.BooksDBViewModel
import com.example.bookminiproject.viewmodel.SelectedAuthorUiState


@Composable
fun BookAuthorScreen(
    booksDBViewModel: BooksDBViewModel,
    onWorkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier) {
        when(val selectedAuthorUiState = booksDBViewModel.selectedAuthorUiState) {
            is SelectedAuthorUiState.Success -> {
                Text(
                    text = selectedAuthorUiState.author.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Row (modifier = modifier.padding(vertical = 8.dp)) {
                    if (selectedAuthorUiState.author.photos.isNotEmpty()) {
                        AsyncImage(
                            model = Constants.COVER_IMAGE_BASE_URL + selectedAuthorUiState.author.photos[0] + Constants.COVER_SIZE_M,
                            placeholder = painterResource(R.drawable.no_image_placeholder),
                            contentDescription = "Photo of ${selectedAuthorUiState.author.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .weight(1f)
                                .size(170.dp)
                                .padding(horizontal = 4.dp),
                        )
                    }
                    Column (modifier = Modifier.weight(2f)) {
                        val descriptionValue = when (val bio = selectedAuthorUiState.author.bio) {
                            is Biography.Text -> bio.value
                            is Biography.Object -> bio.value
                            null -> "Description of ${selectedAuthorUiState.author.name} is not available."
                        }
                        Text(
                            text = descriptionValue,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Column (modifier = Modifier.padding(8.dp)) {
                            if (selectedAuthorUiState.author.birthDate != null) {
                                Text(
                                    text = "Born: ${selectedAuthorUiState.author.birthDate}"
                                )
                            }
                            if (selectedAuthorUiState.author.deathDate != null) {
                                Text(
                                    text = "Died: ${selectedAuthorUiState.author.deathDate}"
                                )
                            }
                        }
                    }
                }
                Text(text = "Works")
                LazyRow {
                    items(selectedAuthorUiState.works){work ->
                        WorkListItemCard(
                            work,
                            onWorkClick,
                            Modifier
                                .padding(8.dp)
                                .width(160.dp)
                                .height(200.dp)
                        )
                    }
                }
            }
            is SelectedAuthorUiState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            is SelectedAuthorUiState.Error -> {
                Text(
                    text = "Error: Something went wrong!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkListItemCard(
    work: Work,
    onWorkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = {
            onWorkClick(work.key)
        }
    ) {
        Row {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val cover = work.covers?.get(0)
                if (cover != null) {
                    AsyncImage(
                        model = Constants.COVER_IMAGE_BASE_URL + cover + Constants.COVER_SIZE_M,
                        placeholder = painterResource(R.drawable.no_image_placeholder),
                        contentDescription = work.title,
                        modifier = Modifier
                            .width(90.dp)
                            .height(136.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.no_image_placeholder),
                        contentDescription = work.title,
                        modifier = Modifier
                            .width(90.dp)
                            .height(136.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = work.title,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                val publishDate = work.firstPublishDate
                if (publishDate != null) {
                    Text(
                        text = publishDate,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    Text(
                        text = "Publish date unavailable",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BookAuthorPreview() {
    BookMiniProjectTheme {
        /*BookAuthorScreen(
            Author(
                "George R. R. Martin",
                "George Raymond Richard Martin (born September 20, 1948), sometimes referred to as GRRM, is an American author and screenwriter of fantasy, horror, and science fiction. He is best known for his ongoing *A Song of Ice and Fire* series of epic fantasy novels.\r\n\r\nCritics have described Martin's work as dark and cynical. His first novel, Dying of the Light, set the tone for most of his future work; it is set on a mostly abandoned planet that is slowly becoming uninhabitable as it moves away from its sun. This story, and many of Martin's others, have a strong sense of melancholy. His characters are often unhappy, or at least unsatisfied - trying to stay idealistic in a ruthless world. Many have elements of tragic heroes in them. Reviewer T. M. Wagner writes, \"Let it never be said Martin doesn't share Shakespeare's fondness for the senselessly tragic.\" This gloominess can be an obstacle for some readers. The Inchoatus Group writes, \"If this absence of joy is going to trouble you, or you’re looking for something more affirming, then you should probably seek elsewhere.\"",
                listOf(6387401, 6155669),
                "September 20, 1948",
                "September 20, 2948"
            ),
            listOf(Work(
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
                authors = listOf(AuthorRole(author = AuthorKey( "Ronald Dahl"))),
                firstPublishDate = "1 June, 1970"
            )),
            {}
        )*/
    }
}
