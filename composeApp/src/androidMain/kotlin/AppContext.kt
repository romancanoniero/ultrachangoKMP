import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.iyr.ultrachango.utils.firebase.GoogleAuth

object AppContext {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var handlerSignInResult: (Intent?) -> Any
    lateinit var googleAuth: GoogleAuth
    lateinit var signInLauncher: ActivityResultLauncher<IntentSenderRequest>
    lateinit var context: Context
    lateinit var activity: Activity
}