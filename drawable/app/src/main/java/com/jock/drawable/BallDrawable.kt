package com.jock.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Description: 自定义篮球 Drawable
 * Author: lxc
 * Date: 2022/3/7 10:37
 */
class BallDrawable : Drawable() {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#D2691E")
    }

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1f
        color = Color.BLACK
    }

    /**
     * 在边界内进行绘制（通过setBounds()），受alpha与colorFilter所影响。
     */
    override fun draw(canvas: Canvas) {
        val radius = bounds.width().toFloat() / 2
        canvas.drawCircle(
            bounds.width().toFloat() / 2,
            bounds.height().toFloat() / 2,
            radius,
            paint
        )

        //the vertical line of the ball
        canvas.drawLine(
            bounds.width().toFloat() / 2,
            0f,
            bounds.width().toFloat() / 2,
            bounds.height().toFloat(),
            linePaint
        )
        //the transverse line of the ball
        canvas.drawLine(
            0f,
            bounds.height().toFloat() / 2,
            bounds.width().toFloat(),
            bounds.height().toFloat() / 2,
            linePaint
        )

        val path = Path()
        val sinValue = kotlin.math.sin(Math.toRadians(45.0)).toFloat()
        //left curve
        path.moveTo(radius - sinValue * radius,
            radius - sinValue * radius
        )
        path.cubicTo(radius - sinValue * radius,
            radius - sinValue * radius,
            radius,
            radius,
            radius - sinValue * radius,
            radius + sinValue * radius
        )
        //right curve
        path.moveTo(radius + sinValue * radius,
            radius - sinValue * radius
        )
        path.cubicTo(radius + sinValue * radius,
            radius - sinValue * radius,
            radius,
            radius,
            radius + sinValue * radius,
            radius + sinValue * radius
        )
        canvas.drawPath(path, linePaint)
    }

    /**
     * 为Drawable指定一个alpha值，0 表示完全透明，255 表示完全不透明
     */
    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    /**
     * 为Drawable指定可选的颜色过滤器。Drawable的draw绘图内容的每个输出像素
     * 在混合到 Canvas 的渲染目标之前将被颜色过滤器修改。传递 null 会删除任何现有的颜色过滤器。
     */
    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    /**
     * 返回Drawable的透明度，如下所示：
     */
    override fun getOpacity(): Int {
        return when (paint.alpha) {
            0xff -> PixelFormat.OPAQUE
            0x00 -> PixelFormat.TRANSPARENT
            else -> PixelFormat.TRANSLUCENT
        }
    }
}