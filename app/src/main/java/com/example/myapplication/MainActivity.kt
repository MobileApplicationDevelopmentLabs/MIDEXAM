import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import androidx.compose.material3.MaterialTheme as MaterialTheme1
import androidx.compose.ui.res.painterResource as painterResource1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NavHost(navController = rememberNavController(), startDestination = "splash") {
                    composable("splash") {
                        val navController =  rememberNavController()
                        SplashScreen(navController)
                    }
                    composable("input") {
                        val navController = rememberNavController()
                        InputScreen(navController)
                    }
                    composable(
                        "weather/{cityName}",
                        arguments = listOf(navArgument("cityName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
                        val navController = rememberNavController()
                        WeatherScreen(navController, cityName)
                    }
                }
            }
        }
    }
}

data class WeatherInfo(
    val temperature: String,
    val humidity: String,
    val conditions: String
)

fun getWeatherInfo(context: Context, cityName: String): WeatherInfo {
    val resourceId = context.resources.getIdentifier("weather_info_${cityName.lowercase()}", "array", context.packageName)

    if (resourceId != 0) {
        val weatherArray = context.resources.getStringArray(resourceId)
        return WeatherInfo(
            temperature = weatherArray[0],
            humidity = weatherArray[1],
            conditions = weatherArray[2]
        )
    } else {
        // Return default values if the city is not found
        return WeatherInfo(
            temperature = "N/A",
            humidity = "N/A",
            conditions = "N/A"
        )
    }
}



@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {

        delay(3000)
        navController.navigate("input")
    }
}

@Composable
fun InputScreen(navController: NavController) {
    // Get the city name from the user and navigate to the weather screen

    var cityName by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select City", style = MaterialTheme1.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "App icon",
            modifier = Modifier.size(100.dp)
        )

        Image(
            painter = painterResource1(id = R.drawable.map),
            contentDescription = "Map image",
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = cityName,
            onValueChange = { cityName = it },
            label = { Text("Enter city name") }
        )

        Button(
            onClick = {
                navController.navigate("weather/$cityName")
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Get Weather")
        }
    }
}

@Composable
fun WeatherScreen(navController: NavController, cityName: String) {
    // Retrieve the weather information for the city
    val weatherInfo = getWeatherInfo(LocalContext.current, cityName)

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Row {
            Text(text = "City Name: $cityName", style = MaterialTheme1.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row (){
            Column (){
            Text(text = "Temperature");
            Text(text = weatherInfo.temperature);
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (horizontalArrangement = Arrangement.SpaceBetween) {
            Column (){
                Text(text = "Humidity");
                Text(text = weatherInfo.humidity );
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row (horizontalArrangement = Arrangement.SpaceBetween) {
            Column (){
                Text(text = "Conditions" ,);
                Text(text = weatherInfo.conditions);
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MaterialTheme1 {
        WeatherScreen(navController = rememberNavController(), cityName = "Gujranwala" )
    }
}









