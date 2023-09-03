package com.example.movieshow

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import com.example.movieshow.database.MovieDatabase
import com.example.movieshow.database.MovieItem
import com.example.movieshow.network.MovieApi
import com.example.movieshow.ui.theme.MovieShowTheme
import com.example.movieshow.viewModels.MovieViewModel
import kotlinx.coroutines.launch
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.movieshow.Observer.ConnectivityObserver
import com.example.movieshow.Observer.NetworkConnectivityObserver
import com.example.movieshow.models.Movie
import com.example.movieshow.repository.MovieRepo
import com.example.movieshow.viewModels.PopularViewModel
import com.example.movieshow.viewModels.TopRatedViewModel
import com.example.movieshow.viewModels.TrendingViewModel
import com.example.movieshow.viewModels.UpcompingViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class MainActivity : ComponentActivity() {
    lateinit var movieViewModel : MovieViewModel
    lateinit var popularViewModel: PopularViewModel
    lateinit var upcompingViewModel: UpcompingViewModel
    lateinit var trendingViewModel: TrendingViewModel
    lateinit var topRatedViewModel: TopRatedViewModel
    lateinit var connectivityObserver: ConnectivityObserver
    @SuppressLint("UnrememberedMutableState", "FlowOperatorInvokedInComposition",
        "CoroutineCreationDuringComposition"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityObserver  = NetworkConnectivityObserver(applicationContext)

        val movieDao = MovieDatabase.getInstance(this).movieDao()
        val movieApi = MovieApi.getMovieApi(MovieApi.getRetrofit())
        val repo = MovieRepo(movieDao, movieApi)
        val movieFactory = MovieViewModelFactory(repo)
        val popularFactory = PopularViewModelFactory(repo)
        val upcomingFactory = UpcomingViewModeFactory(repo)
        val trendingFactory = TrendingViewModelFactory(repo)
        val topRatedFactory = TopRatedViewModelFactory(repo)
        movieViewModel = ViewModelProvider(this, movieFactory)[MovieViewModel::class.java]
        popularViewModel = ViewModelProvider(this, popularFactory)[PopularViewModel::class.java]
        upcompingViewModel = ViewModelProvider(this, upcomingFactory)[UpcompingViewModel::class.java]
        trendingViewModel = ViewModelProvider(this, trendingFactory)[TrendingViewModel::class.java]
        topRatedViewModel = ViewModelProvider(this, topRatedFactory)[TopRatedViewModel::class.java]

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val lastVisitedScreen = sharedPreferences.getString("lastVisitedScreen", "") ?: "Landing Page"

        setContent() {
                MovieShowTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val status by connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Unavailable)

                        if(status  == ConnectivityObserver.Status.Available){
                            popularViewModel.getMovie()
                            upcompingViewModel.getMovie()
                            trendingViewModel.getMovie()
                            topRatedViewModel.getMovie()

                            if(lastVisitedScreen=="") {
                                movieViewModel.currentSreen = "Landing Page"
                                movieViewModel.lastScreen = "Landing Page"
                            }
                            else {
                                movieViewModel.currentSreen = lastVisitedScreen
                                movieViewModel.lastScreen = lastVisitedScreen
                            }
                        }
                        else if(status == ConnectivityObserver.Status.Unavailable || status == ConnectivityObserver.Status.Lost)
                                movieViewModel.currentSreen  = "Watch List"

                        val popular = popularViewModel.popular.collectAsLazyPagingItems()
                        val upcoming = upcompingViewModel.upcoming.collectAsLazyPagingItems()
                        val trending = trendingViewModel.trending.collectAsLazyPagingItems()
                        val topRated = topRatedViewModel.topRated.collectAsLazyPagingItems()
                        val stateList = listOf<LazyListState>(rememberLazyListState(), rememberLazyListState(), rememberLazyListState(), rememberLazyListState())
                        val movieType = listOf<String>("popular","upcoming","trending","top-rated")

                        val navControl = rememberNavController()
                        NavHost(navController = navControl,
                        startDestination = movieViewModel.currentSreen){
                            composable("Landing Page"){
                                LandingPage(navController = navControl,movieViewModel, movieType[0], popular, stateList[0])
                            }
                            composable("Upcoming"){
                                LandingPage(navController = navControl,movieViewModel, movieType[1], upcoming, stateList[1])
                            }
                            composable("Trending"){
                                LandingPage(navController = navControl,movieViewModel, movieType[2], trending, stateList[2])
                            }
                            composable("Top-Rated"){
                                LandingPage(navController = navControl,movieViewModel, movieType[3], topRated, stateList[3])
                            }
                            composable("Detailed View/{lastscreen}/{title}/{desc}/{poster}"){
                                val lastScreen = it.arguments?.getString("lastscreen") ?: "Landing Page"
                                val  poster = "https://image.tmdb.org/t/p/w500/"+ (it.arguments?.getString("poster") ?: "")
                                val  title = it.arguments?.getString("title") ?: ""
                                val  descrptn = it.arguments?.getString("desc") ?: ""
                                DetailedView(navController = navControl,poster,title, descrptn, lastScreen, status)
                            }
                            composable("Watch List"){
                                Watchlist(navController = navControl, movieViewModel)
                            }
                        }
                    }
                }
            }
        }

        override fun onStop() {
            super.onStop()
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("lastVisitedScreen", movieViewModel.lastScreen)
            editor.apply()
        }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType",
    "CoroutineCreationDuringComposition"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(navController: NavController, movieViewModel: MovieViewModel ,movieType : String, movieList : LazyPagingItems<Movie>,colState : LazyListState){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isClicked by remember {
        mutableStateOf(false)
    }
    var title by remember {
        mutableStateOf("")
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        topBar = { TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${movieType} movies",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.width(300.dp)
                    )
                    Icon(painter = painterResource(R.drawable.baseline_bookmarks_24),
                        contentDescription = "",
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                movieViewModel.lastScreen = "Watch List"
                                navController.navigate("Watch List")
                            })
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = colorResource(id = R.color.darkgrey),
                titleContentColor = Color.White
            ),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)),
        )},
        content = { margin -> Listview( movieViewModel, navController, movieList, colState, margin) {
            isClicked = true
            title = it
        }
        },
        bottomBar = { BottomAppBar(modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
            actions = {
                IconButton(onClick = {
                    navController.navigate("Landing Page")
                    movieViewModel.pageNo = 0 },
                    modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.fire), contentDescription = "", modifier = Modifier.weight(1f))
                        Text(text = "popular", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        }
                    }
                IconButton(onClick = {
                    navController.navigate("Upcoming")
                    movieViewModel.pageNo = 1 },
                    modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.punch), contentDescription = "", modifier = Modifier.weight(1f))
                        Text(text = "upcoming", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    }
                }
                IconButton(onClick = {
                    navController.navigate("Trending")
                    movieViewModel.pageNo = 2 },
                    modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.play), contentDescription = "", modifier = Modifier.weight(1f))
                        Text(text = "trending", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    }
                }
                IconButton(onClick = {
                    navController.navigate("Top-Rated")
                    movieViewModel.pageNo = 3 },
                    modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.star), contentDescription = "", modifier = Modifier.weight(1f))
                        Text(text = "top-rated", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    }
                }
                if(isClicked){
                    scope.launch {
                        snackbarHostState.showSnackbar("${title} added to the watchlist")
                        isClicked = false
                    }
                }
                },
            containerColor = colorResource(id = R.color.yellow))
            }
        )
}

@Composable
fun Listview(movieViewModel: MovieViewModel, navController: NavController, movieList : LazyPagingItems<Movie>, colState: LazyListState, margin : PaddingValues, onClick : (title : String)-> Unit) {

    LazyColumn(modifier = Modifier.padding(margin), state = colState) {
        items(items = movieList) {
                item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .padding(top = 10.dp)
                    .height(200.dp)
                    .shadow(5.dp, shape = RoundedCornerShape(15.dp))
                    .clickable {
                        navController.navigate(
                            "Detailed View/Landing Page/${item?.originalTitle}/${item?.overview}/${
                                item?.posterPath?.replace(
                                    "/",
                                    ""
                                )
                            }"
                        )
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w500/${item?.posterPath}",
                        contentDescription = "",
                        modifier = Modifier
                            .padding(10.dp)
                            .clip(shape = RoundedCornerShape(15.dp)),
                        error = painterResource(id = R.drawable.baseline_image_24)
                    )
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(180.dp)
                    ) {
                        Column(modifier = Modifier.height(160.dp)) {
                            Text(
                                text = item?.originalTitle ?: "",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = colorResource(id = R.color.navy)
                            )
                            Text(
                                text = item?.overview ?: "", fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .wrapContentWidth(),
                                lineHeight = 15.sp,
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (item?.voteAverage!! > 0) {
                                Text(
                                    text = item?.voteAverage.toString() ?: "",
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
                        }
                        Row {
                            val scope = rememberCoroutineScope()
                            item?.releaseDate?.let { it1 ->
                                Text(
                                    text = "release date: ${it1}",
                                    fontSize = 12.sp,
                                    color = colorResource(id = R.color.grey),
                                    modifier = Modifier.width(160.dp)
                                )
                            }
                            IconButton(onClick = {
                                movieViewModel.addMovie(
                                    MovieItem(
                                        item?.id ?: 0,
                                        System.currentTimeMillis(),
                                        item?.posterPath ?: "",
                                        item?.backdropPath ?: "",
                                        item?.originalTitle ?: "",
                                        item?.overview ?: ""
                                    ))
                                onClick(item?.originalTitle?:"")
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_add_circle_outline_24),
                                    contentDescription = ""
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnusedMaterialScaffoldPaddingParameter"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedView(navController: NavController, poster : String, title : String, descrptn : String, lastScreen : String, status: ConnectivityObserver.Status){
    var isClicked by remember {
        mutableStateOf(false)
    }
    var count by remember {
        mutableStateOf(0)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(590.dp)
                    .clip(RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp))
                    .shadow(5.dp)
            ) {
                if (status != ConnectivityObserver.Status.Available) {
                    val painter = rememberAsyncImagePainter(model = poster)
                    if (painter.state is AsyncImagePainter.State.Error) {
                        Text(text = "Image no loaded")
                    } else if (painter.state is AsyncImagePainter.State.Success) {
                        Image(
                            painter = rememberAsyncImagePainter(model = poster),
                            contentDescription = ""
                        )
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (count <= 5) {
                                Button(modifier = Modifier,
                                    onClick = {
                                        isClicked = true
                                        count += 1
                                    }) {
                                    Text(text = "Retry")
                                }
                                if (isClicked) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(64.dp),
                                        color = Color.DarkGray,
                                        strokeWidth = 6.dp
                                    )
                                }
                            }
                            else{
                                Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState)}, topBar = {}, content = {})
                                scope.launch {
                                    snackbarHostState.showSnackbar("Error!")
                                }
                            }

                        }
                    }
                } else {
                    AsyncImage(
                        model = poster,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
        item {
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
    Box(modifier = Modifier.padding(bottom = 20.dp)) {
        Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
            contentDescription = "",
            tint = if(status == ConnectivityObserver.Status.Available) Color.White else Color.Black,
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    navController.popBackStack()
                })
    }
}

@Composable
fun Watchlist(navController: NavController, movieViewModel: MovieViewModel){
    val listOfMovies by movieViewModel.watchList.observeAsState(listOf())

    Column(modifier = Modifier.padding(15.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable {
                        if (movieViewModel.lastScreen == "" || movieViewModel.lastScreen == "Watch List") {
                            movieViewModel.lastScreen = "Landing Page"
                            navController.navigate("Landing Page")
                        } else
                            navController.popBackStack()
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

                        AsyncImage(model = "https://image.tmdb.org/t/p/w500/${it.shortPoster}",
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape = RoundedCornerShape(10.dp))
                                .clickable {
                                    movieViewModel.lastScreen = "Watch List"
                                    navController.navigate(
                                        "Detailed View/Watch List/${it.title}/${it.descrptn}/${
                                            it.longPoster?.replace(
                                                "/",
                                                ""
                                            )
                                        }"
                                    )
                                },
                            contentScale = ContentScale.FillBounds
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
                                        movieViewModel.deleteMovie(it.id)
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
