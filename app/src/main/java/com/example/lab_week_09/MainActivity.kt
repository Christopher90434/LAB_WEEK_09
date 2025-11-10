package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

//Here, we create a composable function called App
//This will be the root composable of the app
@Composable
fun App(navController: NavHostController) {
    //Here, we use NavHost to create a navigation graph
    //We pass the navController as a parameter
    //We also set the startDestination to "home"
    //This means that the app will start with the Home composable
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        //Here, we create a route called "home"
        //We pass the Home composable as a parameter
        //This means that when the app navigates to "home",
        //the Home composable will be displayed
        composable("home") {
            //Here, we pass a lambda function that navigates to "resultContent"
            //and pass the listData as a parameter
            Home { navController.navigate("resultContent/?listData=$it") }
        }
        //Here, we create a route called "resultContent"
        //We pass the ResultContent composable as a parameter
        //This means that when the app navigates to "resultContent",
        //the ResultContent composable will be displayed
        //You can also define arguments for the route
        //Here, we define a String argument called "listData"
        //We use navArgument to define the argument
        //We use NavType.StringType to define the type of the argument
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) {
            //Here, we pass the value of the argument to the ResultContent composable
            ResultContent(
                it.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

//Declare a data class called Student
data class Student(
    var name: String
)

@Composable
fun Home(
    navigateFromHomeToResult: (String) -> Unit
) {
    //Here, we create a mutable state list of Student
    //We use remember to make the list remember its value
    //This is so that the list won't be recreated when the composable recomposes
    //We use mutableStateListOf to make the list mutable
    //This is so that we can add or remove items from the list
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    //Here, we create a mutable state of Student
    //This is so that we can get the value of the input field
    var inputField = remember { mutableStateOf(Student("")) }

    //We call the HomeContent composable
    //Here, we pass:
    //listData to show the list of items inside HomeContent
    //inputField to show the input field value inside HomeContent
    //A lambda function to update the value of the inputField
    //A lambda function to add the inputField to the listData
    HomeContent(
        listData,
        inputField.value,
        { input -> inputField.value = inputField.value.copy(name = input) },
        {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        },
        { navigateFromHomeToResult(listData.toList().toString()) }
    )
}

//Here, we create a composable function called HomeContent
//HomeContent is used to display the content of the Home composable
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    //Here, we call the OnBackgroundTitleText UI Element
                    OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))
                }

                TextField(
                    //Set the value of the input field
                    value = inputField.name,
                    //Set the keyboard type of the input field
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    //Set what happens when the value of the input field changes
                    onValueChange = {
                        //Here, we call the onInputValueChange lambda function
                        //and pass the value of the input field as a parameter
                        //This is so that we can update the value of the inputField
                        onInputValueChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row {
                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_click)
                    ) {
                        onButtonClick()
                    }
                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_navigate)
                    ) {
                        navigateFromHomeToResult()
                    }
                }
            }
        }

        //Here, we use items to display a list of items inside the LazyColumn
        //This is the RecyclerView replacement
        //We pass the listData as a parameter
        items(listData) { item ->
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                //Here, we call the OnBackgroundItemText UI Element
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

//Here, we create a composable function called ResultContent
//ResultContent accepts a String parameter called listData from the Home composable
//then displays the value of listData to the screen
@Composable
fun ResultContent(listData: String) {
    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        //Here, we call the OnBackgroundItemText UI Element
        OnBackgroundItemText(text = listData)
    }
}
