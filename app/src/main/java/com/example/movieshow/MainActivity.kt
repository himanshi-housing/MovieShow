package com.example.movieshow

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.matchParentSize
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieshow.ui.theme.MovieShowTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
//    lateinit var receiver : InternetOff
    val movieViewModel by viewModels<MovieViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieShowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    receiver = InternetOff()
//                    IntentFilter().also {
//                        registerReceiver(receiver,it)
//                    }
                    movieViewModel.getPopular()
                    movieViewModel.getUpcoming()
                    movieViewModel.getTrending()
                    movieViewModel.getTopRated()
                    val db = MovieDatabase.getInstance(applicationContext).MovieDao()
                    val navControl = rememberNavController()
                    NavHost(navController = navControl, 
                    startDestination = "Landing Page"){
                        composable("Landing Page"){
                            LandingPage(navController = navControl, movieViewModel, db)
                        }
                        composable("Detailed View/{title}/{desc}/{poster}"){
                            val  poster = "https://image.tmdb.org/t/p/w500/"+ (it.arguments?.getString("poster") ?: "")
                            val  title = it.arguments?.getString("title") ?: ""
                            val  descrptn = it.arguments?.getString("desc") ?: ""
                            DetailedView(navController = navControl,poster,title, descrptn)
                        }
                        composable("Watch List"){
                            Watchlist(navController = navControl, movieViewModel,db)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(navController: NavController,  movieViewModel : MovieViewModel, db : MovieDao){
    val movieType = listOf<String>("popular","upcoming","trending","top-rated")
//    var selected by rememberSaveable {
//        mutableStateOf(movieViewModel.pageNo)
//    }
    Scaffold(
        topBar = { TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${movieType[movieViewModel.pageNo]} movies",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.width(330.dp)
                    )
                    Icon(painter = painterResource(R.drawable.baseline_bookmarks_24),
                        contentDescription = "",
                        modifier = Modifier
                            .width(20.dp)
                            .clickable { navController.navigate("Watch List") })
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = colorResource(id = R.color.darkgrey),
                titleContentColor = Color.White
            ),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)),
        )},
        content = { Listview( movieViewModel, navController,db)},
        bottomBar = { BottomAppBar(modifier = Modifier.fillMaxWidth(),
            actions = {
                IconButton(onClick = { movieViewModel.pageNo = 0 }, modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.fire), contentDescription = "", modifier = Modifier.weight(1f))
                        Text(text = "popular", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        }
                    }
                IconButton(onClick = { movieViewModel.pageNo = 1 }, modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.punch), contentDescription = "", modifier = Modifier.weight(1f))
                        Text(text = "upcoming", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    }
                }
                IconButton(onClick = { movieViewModel.pageNo = 2 }, modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.play), contentDescription = "", modifier = Modifier.weight(1f))
                        Text(text = "trending", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    }
                }
                IconButton(onClick = { movieViewModel.pageNo = 3 }, modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.star), contentDescription = "", modifier = Modifier.weight(1f))
                        Text(text = "top-rated", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    }
                }
                })
            }
        )
}

@Composable
fun Listview(movieViewModel: MovieViewModel,navController: NavController, db : MovieDao){
    val movieType = listOf(movieViewModel.popular,movieViewModel.upcoming, movieViewModel.trending,movieViewModel.topRated)
    var title by remember {
        mutableStateOf("")
    }
    var isAddClicked by remember {
        mutableStateOf(false)
    }
    LazyColumn(modifier = Modifier.padding(top = 65.dp)){
        items(items = movieType[movieViewModel.pageNo]){
            Card(modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .padding(top = 10.dp)
                .height(200.dp)
                .shadow(5.dp, shape = RoundedCornerShape(15.dp))
                .clickable {
                    navController.navigate(
                        "Detailed View/${it.originalTitle}/${it.overview}/${
                            it.posterPath?.replace(
                                "/",
                                ""
                            )
                        }"
                    )
                },
            colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    AsyncImage(model = "https://image.tmdb.org/t/p/w500/${it.posterPath}", contentDescription = "", modifier = Modifier
                        .padding(10.dp)
                        .clip(shape = RoundedCornerShape(15.dp)))
                    Column(modifier = Modifier
                        .padding(10.dp)
                        .height(180.dp)) {
                        Column(modifier = Modifier.height(160.dp)) {
                            Text(text = it.originalTitle?:"", 
                                fontWeight = FontWeight.SemiBold, 
                                fontSize = 15.sp,
                                color = colorResource(id = R.color.navy))
                            Text(text = it.overview?:"", fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .wrapContentWidth(),
                                lineHeight = 15.sp,
                                maxLines = 4,
                            overflow = TextOverflow.Ellipsis)
                            Text(text = it.voteAverage.toString()?:"",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(40.dp)
                                    .padding(top = 10.dp)
                                    .clip(shape = RoundedCornerShape(15.dp))
                                    .background(color = colorResource(id = R.color.navy))
                                    .padding(5.dp),
                                overflow = TextOverflow.Clip
                            )

                        }
                        Row {
                            val scope = rememberCoroutineScope()
                            it.releaseDate?.let { it1 ->
                                Text(
                                    text = "release date: ${it1}",
                                    fontSize = 12.sp,
                                    color = colorResource(id = R.color.grey),
                                    modifier = Modifier.width(170.dp)
                                )
                            }
                            IconButton(onClick = {
                                scope.launch {
                                    db.insert(
                                        MovieItem(
                                            System.currentTimeMillis(),
                                            it.backdropPath ?: "",
                                            it.title ?: "",
                                            ""
                                        )
                                    )
                                }
                                title=it.originalTitle?:""
                                isAddClicked = !isAddClicked
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_add_circle_outline_24),
                                    contentDescription = ""
                                )
                            }

                        }
                    }
                }
                if (isAddClicked){
                    Toast.makeText(
                        LocalContext.current,
                        "${title} added to Watchlist",
                        Toast.LENGTH_SHORT
                    ).show()
                    isAddClicked = !isAddClicked
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedView(navController: NavController, poster : String, title : String, descrptn : String){
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(590.dp)
                    .clip(RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp))
                    .shadow(5.dp)
            ) {
                AsyncImage(
                    model = poster,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        item{
            Column(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 30.dp, bottom = 10.dp)
            ) {
                Text(text = title, fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    text = descrptn,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 10.dp),
                    lineHeight = 16.sp
                )
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    navController.navigate("Landing Page")
                })
    }
}

@Composable
fun Watchlist(navController: NavController, movieViewModel: MovieViewModel,db : MovieDao){
    val scope = rememberCoroutineScope()
    val listOfMovies by remember {
        derivedStateOf {
            runBlocking {
                withContext(Dispatchers.IO){
                    db.getAll()
                }
            }
        }
    }

    Column(modifier = Modifier.padding(15.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        navController.navigate("Landing Page")
                    })
            Text(text = "watchlist", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }
        if(listOfMovies.isNotEmpty()){
            LazyColumn(){
                items(items = listOfMovies){
                    Box(modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp)
                        .height(190.dp)
                        .border(2.dp, Color.DarkGray, RoundedCornerShape(10.dp))
                    ) {

                        AsyncImage(model = "https://image.tmdb.org/t/p/w500/${it.url}",
                            contentDescription = "",
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(10.dp))
                                .fillMaxSize()
                        )
                        Box(modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black
                                    )
                                )
                            )
                        ){
                            Icon(painter = painterResource(id = R.drawable.baseline_remove_circle_outline_24), contentDescription = "",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable {
                                        scope.launch {
                                            db.deleteId(it.time)
                                            navController.navigate("Watch List")
                                        }
                                    })
                            Text(text = it.title,  color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 10.dp, top = 160.dp))

                        }
                    }
                }
            }
        }
        else{
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Text(text = "No Movies Added!", textAlign = TextAlign.Center, fontSize = 30.sp, fontWeight = FontWeight.SemiBold)

            }
        }

    }
}