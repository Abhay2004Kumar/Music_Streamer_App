package com.example.music_streamer

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.music_streamer.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            val pass = binding.passEditText.text.toString()


            //validation
            if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),email)){
                binding.emailEditText.setError("Invalid Email")
                return@setOnClickListener
            }
            if(pass.length < 8){
                binding.passEditText.setError("Length should be atleast 8 characters")
                return@setOnClickListener
            }

            login(email,pass)
        }

        binding.gotoSignup.setOnClickListener{
            startActivity(Intent(this,SingUp::class.java))
        }

    }

    fun login(email: String, password: String){
        setInProgress(true)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                setInProgress(false)
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                finish()
            }.addOnFailureListener{
                setInProgress(false)
                Toast.makeText(applicationContext,"Login account failed", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onResume() {
        super.onResume()
        FirebaseAuth.getInstance().currentUser?.apply {
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
        }
    }

    fun setInProgress(inProgress: Boolean){
        if(inProgress){
            binding.loginButton.visibility= View.GONE
            binding.progressBar.visibility= View.VISIBLE
        }else{
            binding.loginButton.visibility= View.VISIBLE
            binding.progressBar.visibility= View.GONE
        }
    }
}