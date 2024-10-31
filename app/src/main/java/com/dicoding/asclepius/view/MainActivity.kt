package com.dicoding.asclepius.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import kotlin.math.log
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var cropedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener{analyzeImage()}

    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
//            showImage()
            startUCrop()
        }
    }

    private val launcherUCrop = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let {
//                binding.ivCroppedImage.setImageURI(it)
                cropedImageUri = it
                Log.d("cropedImageUri", "this message")
                Log.d("cropedImageUri", cropedImageUri.toString())
                showImage()
            }
        }
    }

    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset[Random.nextInt(charset.length)] }
            .joinToString("")
    }
    private fun startUCrop() {
        var filename = generateRandomString(10)
        var destinationUri = Uri.fromFile(File(cacheDir, "$filename.jpg"))

        var options = UCrop.Options().apply {
            setCompressionQuality(80)
            setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
            setStatusBarColor(ContextCompat.getColor(this@MainActivity, R.color.purple_700))
            setActiveControlsWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.teal_200))
        }

//        val uCrop = UCrop.of(currentImageUri, destinationUri)
//            .withAspectRatio(1f, 1f)
//            .withMaxResultSize(1080, 1080)
//            .withOptions(options)

        var uCrop = currentImageUri?.let {
            UCrop.of(it, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1080, 1080)
                .withOptions(options)
        }

        launcherUCrop.launch(uCrop?.getIntent(this))
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        cropedImageUri?.let {
            
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        moveToResult()
    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        val urei = cropedImageUri.toString()
        Log.d("imageURI", urei)
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, cropedImageUri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}