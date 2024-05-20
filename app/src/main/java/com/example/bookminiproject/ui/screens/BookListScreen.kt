package com.example.bookminiproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bookminiproject.R
import com.example.bookminiproject.model.Works
import com.example.bookminiproject.ui.theme.BookMiniProjectTheme
import com.example.bookminiproject.utils.Constants
import com.example.bookminiproject.viewmodel.BooksDBViewModel
import com.example.bookminiproject.viewmodel.WorkListUiState

@Composable
fun BookListScreen(
    booksDBViewModel: BooksDBViewModel,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when(val workListUiState = booksDBViewModel.workListUiState) {
        is WorkListUiState.Success -> {
            Column (modifier = Modifier.verticalScroll(
                rememberScrollState()
            )) {
                workListUiState.works.forEach {
                    Text(text = it.second)
                    LazyRow(modifier = modifier) {
                        items(it.first) { work ->
                            BookListItemCard(
                                work = work,
                                onBookListItemClicked,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(300.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
        is WorkListUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is WorkListUiState.Error -> {
            Text(
                text = "Error: Something went wrong!",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListItemCard(
    work: Works,
    onBookListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = {
            onBookListItemClicked(work.key)
        }
    ) {
        Row  {
            Box {
                Log.d("IMAGE URL", Constants.COVER_IMAGE_BASE_URL + work.coverImage + Constants.COVER_SIZE_M)
                AsyncImage(
                    model = Constants.COVER_IMAGE_BASE_URL + work.coverImage + Constants.COVER_SIZE_M,
                    placeholder = painterResource(R.drawable.no_image_placeholder),
                    contentDescription = work.title,
                    modifier = Modifier
                        .width(90.dp)
                        .height(136.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = work.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = work.firstPublishYear.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))
                if (work.authorName.isNotEmpty()){
                    Text(
                        text = "by ${work.authorName[0]}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    BookMiniProjectTheme {
        /*BookListItemCard(
            book = Book(
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
                publishDate = "1 June 1970",
                numberOfPages = 123
            ),
            {},
            modifier = Modifier.padding(8.dp)
        )*/
    }
}
