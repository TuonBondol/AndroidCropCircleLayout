package com.tuonbondol.cropcirclelayout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.RelativeLayout

/***
 *
 * @author TUON BONDOL on 8/12/17.
 *
 */

class RoundedCornerLayout : RelativeLayout {

    private var maskBitmap: Bitmap? = null
    private var paint: Paint? = null
    private var maskPaint: Paint? = null
    private var cornerRadius: Float = 0.toFloat()

    /***

     * @param context
     */
    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    /***

     * @param context
     * *
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    /***

     * @param context
     * *
     * @param attrs
     * *
     * @param defStyle
     */
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    /***

     * @param context
     * *
     * @param attrs
     * *
     * @param defStyle
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val metrics = context.getResources().getDisplayMetrics()
        cornerRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1000f, metrics)

        paint = Paint(Paint.ANTI_ALIAS_FLAG)

        maskPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        maskPaint!!.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))

        setWillNotDraw(false)
    }

    /***

     * @param canvas
     */
    override fun draw(canvas: Canvas) {
        val offscreenBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888)
        val offscreenCanvas = Canvas(offscreenBitmap)

        super.draw(offscreenCanvas)

        if (maskBitmap == null) {
            maskBitmap = createMask(canvas.getWidth(), canvas.getHeight())
        }

        offscreenCanvas.drawBitmap(maskBitmap, 0f, 0f, maskPaint)
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint)
    }

    /***

     * @param width
     * *
     * @param height
     * *
     * @return
     */
    private fun createMask(width: Int, height: Int): Bitmap {
        val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(mask)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.setColor(Color.WHITE)

        canvas.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), paint)

        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        canvas.drawRoundRect(RectF(0f, 0f, width.toFloat(), height.toFloat()), cornerRadius, cornerRadius, paint)

        return mask
    }
}
