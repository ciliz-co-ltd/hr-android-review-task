package com.example.testtask

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testtask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = MainViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        
        viewModel.messageHistory.observeForever { messages ->
            displayMessageHistory(messages)
        }
    }

    private fun displayMessageHistory(messages: List<String>) {
        binding.linearLayout.removeAllViews()

        messages.forEach { message ->
            val textView = TextView(this).apply {
                text = message
                textSize = 14f
                setPadding(8, 4, 8, 4)
                setTextColor(resources.getColor(android.R.color.black, null))
            }
            binding.linearLayout.addView(textView)
        }
    }
}
