package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.Surface
import android.widget.Toast
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ImageClassifierHelper(
    val context: Context,
    var threshold: Float = 0.1f,
    var maxResults: Int = 1,
    val modelName: String = "cancer_classification.tflite",
    val classifierListener: ClassifierListener?
    ) {
//    private lateinit var interpreter: Interpreter
//    private val modelPath = "cancer_classification.tflite" // Sesuaikan dengan nama model Anda
//    private val imgSize = 224 // Ukuran input gambar yang diharapkan oleh model
private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()

//        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(toBitmap(image)))
//
//        val imageProcessingOptions = ImageProcessingOptions.builder()
//            .setOrientation(getOrientationFromRotation(image.imageInfo.rotationDegrees))
//            .build()

        try {
//            val imageStream = contentResolver.openInputStream(uri)
//            val imageStream = context.contentResolver.openInputStream(imageUri)
//            val bitmap = BitmapFactory.decodeStream(imageStream)


            // Pastikan bitmap tidak null
            var imgBitmap = toBitmap(imageUri)
            if ( imgBitmap!= null) {
//                imageClassifierHelper.classifyImage(bitmap)  // Analisis gambar menggunakan bitmap
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(imgBitmap))


//            val imageProcessingOptions = ImageProcessingOptions.builder()
//            .setOrientation(getOrientationFromRotation(image.imageInfo.rotationDegrees))
//            .build()

//                val results = imageClassifier?.classify(tensorImage, imageProcessingOptions)
                val results = imageClassifier?.classify(tensorImage)
                classifierListener?.onResults(
                    results
                )
            } else {
//                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                Log.d("gagal", "classifyStaticImage: ")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gagal memuat gambar dari galeri: ${e.message}", e)
        }
    }

    private fun toBitmap(imageUri: Uri): Bitmap {
        val imageStream = context.contentResolver.openInputStream(imageUri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        return bitmap
    }

//    private fun bitmapToImageProxy(bitmap: Bitmap): ImageProxy {
//        val width = bitmap.width
//        val height = bitmap.height
//        val imageReader = ImageReader.newInstance(width, height, ImageFormat.YUV_420_888, 1)
//        val image = imageReader.acquireNextImage()
//
//        // Memproses bitmap dan mengisi ImageProxy
//        val planes = image.planes
//        val yPlane: PlaneProxy = planes[0]
//        val uPlane: PlaneProxy = planes[1]
//        val vPlane: PlaneProxy = planes[2]
//
//        // Mengisi data YUV dari bitmap (ini adalah langkah yang tidak langsung)
//        // Anda mungkin perlu menggunakan algoritma untuk mengonversi RGB ke YUV
//
//        // Mengembalikan ImageProxy
//        return image
//    }

//    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
//        return when (rotation) {
//            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
//            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
//            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
//            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
//        }
//    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
//            inferenceTime: Long
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}