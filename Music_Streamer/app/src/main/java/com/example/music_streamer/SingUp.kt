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
import com.example.music_streamer.databinding.ActivitySingUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SingUp : AppCompatActivity() {

    lateinit var binding: ActivitySingUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountButton.setOnClickListener{
            val email = binding.emailEditText.text.toString()
            val pass = binding.passEditText.text.toString()
            val cpass = binding.confPassEditText.text.toString()

            //validation
          if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),email)){
              binding.emailEditText.setError("Invalid Email")
          return@setOnClickListener
          }
            if(pass.length < 8){
                binding.passEditText.setError("Length should be atleast 8 characters")
                return@setOnClickListener
            }
            if(!pass.equals(cpass)){
                binding.confPassEditText.setError("Password not matched")
                return@setOnClickListener
            }
            createAcoount(email,pass)

        }

        binding.gotoLogin.setOnClickListener{
            finish()
        }

    }


    fun createAcoount(email: String, password: String){
    setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                setInProgress(false)
                Toast.makeText(applicationContext,"User created successfully",Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener{
                setInProgress(false)
                Toast.makeText(applicationContext,"No account created",Toast.LENGTH_SHORT).show()
            }

    }

    fun setInProgress(inProgress: Boolean){
        if(inProgress){
            binding.createAccountButton.visibility= View.GONE
            binding.progressBar.visibility=View.VISIBLE
        }else{
            binding.createAccountButton.visibility= View.VISIBLE
            binding.progressBar.visibility=View.GONE
        }
    }
}