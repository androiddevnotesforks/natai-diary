package com.svbackend.natai.android.coil


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.max

// taken from https://github.com/Commit451/coil-transformations
// as it is not available on mavenCentral
class CropTransformation(
    private val cropType: CropType = CropType.CENTER
) : Transformation {

    enum class CropType {
        TOP,
        CENTER,
        BOTTOM
    }

    override val cacheKey: String = "${CropTransformation::class.java.name}-$cropType"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val width = input.width
        val height = input.height

        val output = createBitmap(width, height, Bitmap.Config.ARGB_8888)

        output.setHasAlpha(true)

        val scaleX = width.toFloat() / input.width
        val scaleY = height.toFloat() / input.height
        val scale = max(scaleX, scaleY)

        val scaledWidth = scale * input.width
        val scaledHeight = scale * input.height
        val left = (width - scaledWidth) / 2
        val top = getTop(height.toFloat(), scaledHeight)
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        val canvas = Canvas(output)
        canvas.drawBitmap(input, null, targetRect, null)

        return output
    }

    private fun getTop(height: Float, scaledHeight: Float): Float {
        return when (cropType) {
            CropType.TOP -> 0f
            CropType.CENTER -> (height - scaledHeight) / 2
            CropType.BOTTOM -> height - scaledHeight
        }
    }
}