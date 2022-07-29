package com.esaudev.pruebatecnicayoutube.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.esaudev.pruebatecnicayoutube.R
import com.esaudev.pruebatecnicayoutube.databinding.ActivityMainBinding
import com.esaudev.pruebatecnicayoutube.domain.extension.load
import com.esaudev.pruebatecnicayoutube.domain.model.RandomUser
import com.esaudev.pruebatecnicayoutube.domain.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initListeners()
        subscribeViewModel()
    }

    private fun initListeners() {
        with(binding) {
            buttonUpdate.setOnClickListener {
                viewModel.getRandomUser()
            }
        }
    }

    private fun subscribeViewModel() {
        viewModel.randomUserState.observe(this) { result ->
            when(result) {
                is Resource.Success -> result.data?.let { bindUserData(it) }
                is Resource.Error -> handleError()
            }
        }

        viewModel.isLoading.observe(this) {
            handleLoadingState(it)
        }
    }

    private fun bindUserData(randomUser: RandomUser) {
        with(binding) {
            userName.text = randomUser.name
            userImage.load(randomUser.photo)
        }
    }

    private fun handleLoadingState(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                userData.visibility = View.GONE
                progressIndicator.visibility = View.VISIBLE
            } else {
                progressIndicator.visibility = View.GONE
                userData.visibility = View.VISIBLE
            }
        }
    }

    private fun handleError() {
        Toast.makeText(this, "An Unknown error occurred", Toast.LENGTH_SHORT).show()
    }
}