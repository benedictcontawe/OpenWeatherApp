import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.AuthenticationViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify // Ensure this is org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.reflect.Field

@RunWith(MockitoJUnitRunner::class)
class AuthenticationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockActivity: Activity

    @Mock
    private lateinit var mockTaskAuthResult: Task<AuthResult>

    @Mock
    private lateinit var mockAuthResult: AuthResult

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    @Captor
    private lateinit var firebaseUserCaptor: ArgumentCaptor<FirebaseUser?>

    @Captor
    private lateinit var throwableCaptor: ArgumentCaptor<Throwable?>

    // Callbacks for testing - initialized in @Before
    private lateinit var mockOnSuccess: (FirebaseUser?) -> Unit
    private lateinit var mockOnFailure: (Throwable?) -> Unit

    private lateinit var viewModel: AuthenticationViewModel

    @Before
    fun setUp() {
        mockOnSuccess = mock()
        mockOnFailure = mock()
        viewModel = AuthenticationViewModel()
        try {
            val firebaseAuthField: Field = AuthenticationViewModel::class.java.getDeclaredField("firebaseAuth")
            firebaseAuthField.isAccessible = true
            firebaseAuthField.set(viewModel, mockFirebaseAuth)
        } catch (e: Exception) {
            fail("Reflection setup for firebaseAuth failed: ${e.message}")
        }
        // Set default values for LiveData if they are not already set in ViewModel constructor
        // or if you want specific values for most tests.
        // viewModel.setEmail("initial@example.com")
        // viewModel.setPassword("initialPassword")
        // viewModel.setConfirmPassword("initialPassword")
    }

    // --- checkCredential Tests ---

    @Test
    fun `checkCredential with blank email calls onFailure with Exception and correct message`() {
        viewModel.setEmail("")
        viewModel.setPassword("password123")
        viewModel.checkCredential(mockActivity, mockOnSuccess, mockOnFailure)
        verify(mockOnFailure).invoke(throwableCaptor.capture())
        val capturedException = throwableCaptor.value
        assertNotNull("Exception should not be null", capturedException)
        assertTrue("Captured throwable should be an Exception", capturedException is Exception)
        assertEquals("Email or Password is Blank", capturedException?.message)
        verify(mockOnSuccess, times(0)).invoke(any())
        // The ViewModel's checkCredential for blank fields doesn't update _message.value
        // It directly calls onFailure(throw Exception(...))
        // If you want to test _message.value, the ViewModel needs to set it in that path.
        // Based on current ViewModel:
        // assertEquals("Email or Password is Blank", viewModel.message.value) // This would fail as _message is not set here
    }

    @Test
    fun `checkCredential with blank password calls onFailure with Exception and correct message`() {
        viewModel.setEmail("test@example.com")
        viewModel.setPassword("")
        viewModel.checkCredential(mockActivity, mockOnSuccess, mockOnFailure)
        verify(mockOnFailure).invoke(throwableCaptor.capture())
        val capturedException = throwableCaptor.value
        assertNotNull(capturedException)
        assertTrue(capturedException is Exception)
        assertEquals("Email or Password is Blank", capturedException?.message)
        verify(mockOnSuccess, times(0)).invoke(any())
    }

    @Test
    fun `checkCredential successful login calls onSuccess and updates message`() {
        val testEmail = "test@example.com"
        val testPassword = "password123"
        viewModel.setEmail(testEmail)
        viewModel.setPassword(testPassword)
        whenever(mockFirebaseAuth.signInWithEmailAndPassword(testEmail, testPassword))
            .thenReturn(mockTaskAuthResult)
        val onCompleteListenerCaptor = argumentCaptor<OnCompleteListener<AuthResult>>()
        whenever(mockTaskAuthResult.addOnCompleteListener(eq(mockActivity), onCompleteListenerCaptor.capture()))
            .thenAnswer {
                whenever(mockTaskAuthResult.isSuccessful).thenReturn(true)
                whenever(mockTaskAuthResult.result).thenReturn(mockAuthResult)
                whenever(mockAuthResult.user).thenReturn(mockFirebaseUser)
                // whenever(mockFirebaseUser.email).thenReturn(testEmail) // For ViewModel's internal logging
                onCompleteListenerCaptor.firstValue.onComplete(mockTaskAuthResult)
                mockTaskAuthResult
            }
        viewModel.checkCredential(mockActivity, mockOnSuccess, mockOnFailure)
        verify(mockOnSuccess).invoke(firebaseUserCaptor.capture())
        assertEquals(mockFirebaseUser, firebaseUserCaptor.value)
        verify(mockOnFailure, times(0)).invoke(any())
        assertEquals("Login Successful 🎉", viewModel.message.value)
    }

    @Test
    fun `checkCredential failed login calls onFailure and updates message`() {
        val testEmail = "test@example.com"
        val testPassword = "wrongpassword"
        viewModel.setEmail(testEmail)
        viewModel.setPassword(testPassword)
        val mockException = Exception("Firebase auth failed")
        whenever(mockFirebaseAuth.signInWithEmailAndPassword(testEmail, testPassword))
            .thenReturn(mockTaskAuthResult)
        val onCompleteListenerCaptor = argumentCaptor<OnCompleteListener<AuthResult>>()
        whenever(mockTaskAuthResult.addOnCompleteListener(eq(mockActivity), onCompleteListenerCaptor.capture()))
            .thenAnswer {
                whenever(mockTaskAuthResult.isSuccessful).thenReturn(false)
                whenever(mockTaskAuthResult.exception).thenReturn(mockException)
                onCompleteListenerCaptor.firstValue.onComplete(mockTaskAuthResult)
                mockTaskAuthResult
            }
        viewModel.checkCredential(mockActivity, mockOnSuccess, mockOnFailure)
        verify(mockOnFailure).invoke(throwableCaptor.capture())
        assertEquals(mockException, throwableCaptor.value)
        verify(mockOnSuccess, times(0)).invoke(any())
        assertEquals("Invalid credentials ❌", viewModel.message.value)
    }
    // --- registerCredential Tests ---
    @Test
    fun `registerCredential with blank email calls onFailure and updates message`() {
        viewModel.setEmail("")
        viewModel.setPassword("password123")
        viewModel.setConfirmPassword("password123")
        viewModel.registerCredential(mockActivity, mockOnSuccess, mockOnFailure)
        verify
    }
}