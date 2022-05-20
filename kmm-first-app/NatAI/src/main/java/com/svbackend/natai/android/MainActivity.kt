package com.svbackend.natai.android

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.svbackend.natai.android.databinding.ActivityMainBinding
import com.svbackend.natai.android.entity.Note
import com.svbackend.natai.android.model.REMINDER_ID
import com.svbackend.natai.android.service.AlarmReceiver
import com.svbackend.natai.android.service.ApiSyncService
import com.svbackend.natai.android.ui.HorizontalDivider
import com.svbackend.natai.android.ui.NataiTheme
import com.svbackend.natai.android.utils.hasInternetConnection
import com.svbackend.natai.android.viewmodel.NoteViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ScopedActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var account: Auth0
    private lateinit var credsManager: CredentialsManager
    private lateinit var authClient: AuthenticationAPIClient
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var apiSyncService: ApiSyncService

    private val viewModel by viewModels<NoteViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    @Composable
    fun MainScreen() {
        val notes by viewModel.notes.collectAsState(initial = emptyList())

        LazyColumn {
            items(notes) { note ->
                NoteCard(note)
                HorizontalDivider()
            }
        }
    }

    @Composable
    fun NoteCard(note: Note) {
        // card with date and title
        Surface(color = MaterialTheme.colorScheme.surface) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = SimpleDateFormat("dd").format(note.createdAt),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = SimpleDateFormat("MMM").format(note.createdAt),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NataiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        val prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)

        (application as DiaryApplication).let {
            account = it.appContainer.auth0
            credsManager = it.appContainer.credentialsManager
            authClient = it.appContainer.auth0ApiClient
            connectivityManager = it.appContainer.connectivityManager
            apiSyncService = it.appContainer.apiSyncService
        }


        binding.addNoteBtn.setOnClickListener {
            val intent = Intent(this, NewNoteActivity::class.java)
            startActivity(intent)
        }

        binding.homeBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            WebAuthProvider.login(account)
                .withScheme("natai")
                .withScope("openid profile email offline_access")
                // Launch the authentication passing the callback where the results will be received
                .start(this, object : Callback<Credentials, AuthenticationException> {
                    // Called when there is an authentication failure
                    override fun onFailure(error: AuthenticationException) {
                        // Something went wrong!
                        Toast
                            .makeText(
                                this@MainActivity,
                                "Login Error: \n${error.message}",
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }

                    // Called when authentication completed successfully
                    override fun onSuccess(result: Credentials) {

                        val accessToken = result.accessToken
                        val idToken = result.idToken
                        showUserProfile(accessToken)
                        with(prefs.edit()) {
                            putString("access_token", accessToken)
                            putString("id_token", idToken)
                            apply()
                        }

                        credsManager.saveCredentials(result)
                        syncWithApi()
                    }
                })
        }

        binding.logoutBtn.setOnClickListener {
            logout()
        }


        credsManager.getCredentials(object : Callback<Credentials, CredentialsManagerException> {
            override fun onFailure(error: CredentialsManagerException) {

            }

            override fun onSuccess(result: Credentials) {
                showUserProfile(result.accessToken)
                with(prefs.edit()) {
                    putString("access_token", result.accessToken)
                    putString("id_token", result.idToken)
                    apply()
                }
                syncWithApi()
            }
        })

        if (!prefs.getBoolean("is_reminder_enabled", false)) {
            addReminder()
            with(prefs.edit()) {
                putBoolean("is_reminder_enabled", true)
                apply()
            }
        }

        loadNotes()
    }

    private fun syncWithApi() = launch {
        val hasInternet = hasInternetConnection(connectivityManager)
        if (!hasInternet) {
            return@launch
        }

        try {
            apiSyncService.syncNotes()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadNotes() = launch {
        val onClick = OnClickListener<Note> {
            val intent = Intent(this@MainActivity, NoteDetailsActivity::class.java).apply {
                putExtra(PARAM_NOTE_ID, it.id)
            }
            startActivity(intent)
        }

//        viewModel.notes.collect { notes ->
//            viewManager = GridLayoutManager(application, 1)
//            viewAdapter = NoteAdapter(notes, onClick)
//
//            recyclerView = (findViewById<RecyclerView>(R.id.NotesRecyclerView)).apply {
//                // use this setting to improve performance if you know that changes
//                // in content do not change the layout size of the RecyclerView
//                setHasFixedSize(true)
//
//                // use a linear layout manager
//                layoutManager = viewManager
//
//                // specify an viewAdapter (see also next example)
//                adapter = viewAdapter
//            }
//
//            return@collect
//        }
    }

    private fun addReminder() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val reminderReceiverIntent = Intent(this, AlarmReceiver::class.java)

        reminderReceiverIntent.putExtra("reminderId", REMINDER_ID)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                REMINDER_ID.toInt(),
                reminderReceiverIntent,
                FLAG_IMMUTABLE
            )

        val firstNotificationDate: Calendar = Calendar.getInstance()
        firstNotificationDate.set(Calendar.HOUR, 21) // 9pm

        alarmManager.setRepeating(
            AlarmManager.RTC, firstNotificationDate.timeInMillis, 24 * 60 * 60 * 1000, pendingIntent
        )
    }

    private fun formatDate(timeInMillis: Long, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(timeInMillis)
    }

    private fun showUserProfile(accessToken: String) {
        // With the access token, call `userInfo` and get the profile from Auth0.
        authClient.userInfo(accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    // Something went wrong!
                    Toast.makeText(
                        this@MainActivity,
                        "Error getting profile \n${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onSuccess(result: UserProfile) {
                    // We have the user's profile!
                    binding.nameTv.text = result.name
                    binding.emailTv.text = result.email
                    Toast.makeText(
                        this@MainActivity,
                        "Login Successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun logout() {
        WebAuthProvider.logout(account)
            .withScheme("natai")
            .start(
                this,
                object : Callback<Void?, AuthenticationException> {
                    override fun onSuccess(result: Void?) {
                        // The user has been logged out!
                        Toast.makeText(
                            this@MainActivity,
                            "Successfully logged out!",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.nameTv.text = resources.getString(R.string.john_doe)
                        binding.emailTv.text = resources.getString(R.string.email)
                    }

                    override fun onFailure(error: AuthenticationException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Couldn't Logout!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }
}
