package com.bigtime.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import com.bigtime.R

/**
 * Created by Shijil Kadambath on 11/9/17
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */


class LoadingButton : View {
    var mText: String = ""
    private var mAnimationEndListener: AnimationEndListener? = null
    private var mColorPrimary: Int = 0
    private var mTextColor: Int = 0
    private var mRippleColor: Int = 0
    private var mRippleAlpha: Float = 0.toFloat()
    var isResetAfterFailed: Boolean = false //when loading data failed, whether reset view
    private var mPadding: Float = 0.toFloat()
    private var mPaint: Paint? = null
    private var ripplePaint: Paint? = null
    private var mStrokePaint: Paint? = null
    private var mTextPaint: Paint? = null
    private var mPathEffectPaint: Paint? = null
    private var mPathEffectPaint2: Paint? = null
    private var mPath: Path? = null
    private var mScaleWidth: Int = 0
    private var mScaleHeight: Int = 0
    private var mDegree: Int = 0
    private var mAngle: Int = 0
    private var mEndAngle: Int = 0
    var currentState: Int = 0
        private set
    private var mDensity: Float = 0.toFloat()
    private var mButtonCorner: Float = 0.toFloat()
    private var mRadius: Int = 0
    private var width: Float = 0.toFloat()
    private var height: Float = 0.toFloat()
    private val mMatrix = Matrix()
    private var mTextWidth: Float = 0.toFloat()
    private var mTextHeight: Float = 0.toFloat()
    private var mSuccessPath: Path? = null
    private var mSuccessPathLength: Float = 0.toFloat()
    private var mSuccessPathIntervals: FloatArray? = null
    private var mFailedPath: Path? = null
    private var mFailedPath2: Path? = null
    private var mFailedPathLength: Float = 0.toFloat()
    private var mFailedPathIntervals: FloatArray? = null
    private var mTouchX: Float = 0.toFloat()
    private var mTouchY: Float = 0.toFloat()
    private var mRippleRadius: Float = 0.toFloat()
    private var mButtonRectF: RectF? = null
    private var mArcRectF: RectF? = null
    private var mLoadingAnimatorSet: AnimatorSet? = null
    private var mContext: Context? = null
    private var capzText = true

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        init(context, attrs)
    }

    @SuppressLint("Recycle")
    private fun init(context: Context, attrs: AttributeSet?) {

        mContext = context
        var boldText = true

        if (attrs != null) {
            val defaultColor = Color.BLUE
            val ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
            mColorPrimary = ta.getInt(R.styleable.LoadingButton_lb_btnColor, defaultColor)
            val text = ta.getString(R.styleable.LoadingButton_lb_btnText)
            mText = text ?: ""
            boldText = ta.getInt(R.styleable.LoadingButton_lb_textStyle, 1) == 1
            capzText = ta.getInt(R.styleable.LoadingButton_lb_setAllCapz, 1) == 1

            mTextColor = ta.getColor(R.styleable.LoadingButton_lb_textColor, Color.WHITE)
            isResetAfterFailed = ta.getBoolean(R.styleable.LoadingButton_lb_resetAfterFailed, true)
            mRippleColor = ta.getColor(R.styleable.LoadingButton_lb_btnRippleColor, Color.BLACK)
            mRippleAlpha = ta.getFloat(R.styleable.LoadingButton_lb_btnRippleAlpha, 0.3f)
            ta.recycle()
        }

        currentState = STATE_BUTTON
        mScaleWidth = 0
        mScaleHeight = 0
        mDegree = 0
        mAngle = 0
        mDensity = resources.displayMetrics.density
        mButtonCorner = 2 * mDensity
        mPadding = 6 * mDensity

        mPaint = Paint()
        setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint)
        mPaint!!.isAntiAlias = true
        mPaint!!.color = mColorPrimary
        mPaint!!.style = Paint.Style.FILL
        setShadowDepth1()

        ripplePaint = Paint()
        ripplePaint!!.isAntiAlias = true
        ripplePaint!!.color = mRippleColor
        ripplePaint!!.alpha = (mRippleAlpha * 255).toInt()
        ripplePaint!!.style = Paint.Style.FILL

        mStrokePaint = Paint()
        mStrokePaint!!.isAntiAlias = true
        mStrokePaint!!.color = mColorPrimary
        mStrokePaint!!.style = Paint.Style.STROKE
        mStrokePaint!!.strokeWidth = 2 * mDensity

        mTextPaint = Paint()
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.color = mTextColor
        mTextPaint!!.textSize = 14 * mDensity

        if (boldText) {
            mTextPaint!!.typeface = ResourcesCompat.getFont(context, R.font.bold)
            mTextPaint!!.isFakeBoldText = true
        } else {
            mTextPaint!!.typeface = ResourcesCompat.getFont(context, R.font.medium)
            mTextPaint!!.isFakeBoldText = false
        }

        if (capzText) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
            val text = ta.getString(R.styleable.LoadingButton_lb_btnText)
            mText = text?.toUpperCase() ?: ""
        } else {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
            val text = ta.getString(R.styleable.LoadingButton_lb_btnText)
            mText = text ?: ""

        }

        mTextWidth = mTextPaint!!.measureText(mText)
        val bounds = Rect()
        mTextPaint!!.getTextBounds(mText, 0, mText.length, bounds)
        mTextHeight = bounds.height().toFloat()

        mPathEffectPaint = Paint()
        mPathEffectPaint!!.isAntiAlias = true
        mPathEffectPaint!!.color = mColorPrimary
        mPathEffectPaint!!.style = Paint.Style.STROKE
        mPathEffectPaint!!.strokeWidth = 2 * mDensity

        mPathEffectPaint2 = Paint()
        mPathEffectPaint2!!.isAntiAlias = true
        mPathEffectPaint2!!.color = mColorPrimary
        mPathEffectPaint2!!.style = Paint.Style.STROKE
        mPathEffectPaint2!!.strokeWidth = 2 * mDensity

        mPath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measureDimension((88 * mDensity).toInt(), widthMeasureSpec)
        val height = measureDimension((56 * mDensity).toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = w.toFloat()
        height = h.toFloat()
        mRadius = (height - mPadding * 2).toInt() / 2

        if (mButtonRectF == null) {
            mButtonRectF = RectF()
            mButtonRectF!!.top = mPadding
            mButtonRectF!!.bottom = height - mPadding
            mArcRectF =
                RectF((width / 2 - mRadius), mPadding, (width / 2 + mRadius), height - mPadding)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mTouchX = event.x
                mTouchY = event.y
                playRippleAnimation(true)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                playRippleAnimation(false)
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    fun setStrokeColor(color: Int) {
        mStrokePaint!!.color = color
        invalidate()
    }

    fun setShapeColor(color: Int) {
        mPathEffectPaint!!.color = color
        mPaint!!.color = color
        mPathEffectPaint2!!.color = color
        invalidate()
    }

    fun setTextColor(color: Int) {
        mTextColor = color
        mTextPaint!!.color = color
        invalidate()
    }

    private fun measureDimension(defaultSize: Int, measureSpec: Int): Int {

        val result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize)
        } else {
            result = defaultSize
        }
        return result
    }

    /**
     * start loading,play animation
     */
    fun startLoading() {

        if (currentState == STATE_ANIMATION_FAILED && !isResetAfterFailed) {
            scaleFailedPath()
            return
        }

        if (currentState == STATE_BUTTON) {
            currentState = STATE_ANIMATION_STEP1
            removeShadow()
            playStartAnimation(false)
        }

    }

    /**
     * loading data successful
     */
    fun loadingSuccessful() {

        Handler().postDelayed({
            //do something

            if (mLoadingAnimatorSet != null && mLoadingAnimatorSet!!.isStarted) {
                mLoadingAnimatorSet!!.end()
                currentState = STATE_STOP_LOADING
                playSuccessAnimation()
            }
        }, (1 * 1000).toLong())


    }

    /**
     * loading data failed
     */
    fun loadingFailed() {

        Handler().postDelayed({
            //do something

            if (mLoadingAnimatorSet != null && mLoadingAnimatorSet!!.isStarted) {
                mLoadingAnimatorSet!!.end()
                currentState = STATE_STOP_LOADING
                playFailedAnimation()
            }
        }, (1 * 1000).toLong())


    }

    fun cancelLoading() {
        Handler().postDelayed({
            //do something

            if (currentState != STATE_ANIMATION_LOADING) {
                return@postDelayed
            }
            cancel()
        }, (1 * 1000).toLong())


    }

    /**
     * reset view to Button with animation
     */
    fun reset() {

        if (currentState == STATE_ANIMATION_SUCCESS) {
            scaleSuccessPath()
        }

        if (currentState == STATE_ANIMATION_FAILED) {
            scaleFailedPath()
        }
    }

    fun setTypeface(typeface: Typeface?) {
        if (typeface != null) {
            mTextPaint!!.typeface = typeface
            invalidate()
        }
    }

    fun setStyle(textSize: Int, @FontRes font: Int) {

        mTextPaint!!.textSize = textSize * mDensity
        mTextPaint!!.typeface = ResourcesCompat.getFont(mContext!!, font)

        mTextWidth = mTextPaint!!.measureText(mText)
        val bounds = Rect()
        mTextPaint!!.getTextBounds(mText, 0, mText.length, bounds)
        mTextHeight = bounds.height().toFloat()
        invalidate()
    }

    fun setText(text: String) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        this.mText = text
        if (capzText) mText = text.toUpperCase()
        mTextWidth = mTextPaint!!.measureText(mText)
        val bounds = Rect()
        mTextPaint!!.getTextBounds(mText, 0, mText.length, bounds)
        mTextHeight = bounds.height().toFloat()
        invalidate()
    }

    fun setAnimationEndListener(animationEndListener: AnimationEndListener) {
        this.mAnimationEndListener = animationEndListener
    }

    private fun createSuccessPath() {

        if (mSuccessPath != null) {
            mSuccessPath!!.reset()
        } else {
            mSuccessPath = Path()
        }

        val mLineWith = 2 * mDensity

        val left = (width / 2 - mRadius) + (mRadius / 3).toFloat() + mLineWith
        val top = mPadding + (mRadius / 2).toFloat() + mLineWith
        val right = (width / 2 + mRadius) - mLineWith - (mRadius / 3).toFloat()
        val bottom = (mLineWith + mRadius) * 1.5f + mPadding / 2
        val xPoint = (width / 2 - mRadius / 6)

        mSuccessPath = Path()
        mSuccessPath!!.moveTo(left, mPadding + mRadius.toFloat() + mLineWith)
        mSuccessPath!!.lineTo(xPoint, bottom)
        mSuccessPath!!.lineTo(right, top)

        val measure = PathMeasure(mSuccessPath, false)
        mSuccessPathLength = measure.length
        mSuccessPathIntervals = floatArrayOf(mSuccessPathLength, mSuccessPathLength)
    }

    private fun createFailedPath() {

        if (mFailedPath != null) {
            mFailedPath!!.reset()
            mFailedPath2!!.reset()
        } else {
            mFailedPath = Path()
            mFailedPath2 = Path()
        }

        val left = (width / 2 - mRadius + mRadius / 2)
        val top = mRadius / 2 + mPadding

        mFailedPath!!.moveTo(left, top)
        mFailedPath!!.lineTo(left + mRadius, top + mRadius)

        mFailedPath2!!.moveTo((width / 2 + mRadius / 2), top)
        mFailedPath2!!.lineTo((width / 2 - mRadius + mRadius / 2), top + mRadius)

        val measure = PathMeasure(mFailedPath, false)
        mFailedPathLength = measure.length
        mFailedPathIntervals = floatArrayOf(mFailedPathLength, mFailedPathLength)

        val PathEffect = DashPathEffect(mFailedPathIntervals, mFailedPathLength)
        mPathEffectPaint2!!.pathEffect = PathEffect
    }

    private fun setShadowDepth1() {
        mPaint!!.setShadowLayer(1 * mDensity, 0f, 1 * mDensity, 0x1F000000)
    }

    private fun setShadowDepth2() {
        mPaint!!.setShadowLayer(2 * mDensity, 0f, 2 * mDensity, 0x1F000000)
    }

    private fun removeShadow() {
        mPaint!!.reset()
        mPaint!!.isAntiAlias = true
        mPaint!!.color = mColorPrimary
        mPaint!!.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (currentState) {
            STATE_BUTTON,


                /* mPaint.setColor(Color.BLUE);
                int offset = 50;
                RectF rectF = new RectF(
                        offset, // left
                        offset, // top
                        canvas.getWidth() - offset, // right
                        canvas.getHeight() - offset // bottom
                );
                int radius = 250;
                canvas.drawCircle(10, 10, 20, mPaint);


                canvas.drawRoundRect(mButtonRectF,100,100,mPaint);
                invalidate();*/
            STATE_ANIMATION_STEP1 -> {
                val cornerRadius =
                    (mRadius - mButtonCorner) * (mScaleWidth / (width / 2 - height / 2)) + mButtonCorner
                mButtonRectF!!.left = mScaleWidth.toFloat()
                mButtonRectF!!.right = (width - mScaleWidth)
                canvas.drawRoundRect(mButtonRectF!!, cornerRadius, cornerRadius, mPaint!!)
                if (currentState == STATE_BUTTON) {

                    // mTextPaint.setTextAlign(Paint.Align.CENTER);
                    //int xPos = (canvas.getWidth() / 2);
                    val xPos = (width - mTextWidth) / 2
                    val yPos = (height / 2 - (mTextPaint!!.descent() + mTextPaint!!.ascent()) / 2).toInt()
                    //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.
                    canvas.drawText(mText, xPos, yPos.toFloat(), mTextPaint!!)

                    //canvas.drawText(mText,(width-mTextWidth)/2,(height-mTextHeight)/2+mPadding*2,mTextPaint);

                    if (mTouchX > 0 || mTouchY > 0) {
                        canvas.clipRect(0f, mPadding, width, height - mPadding)
                        canvas.drawCircle(mTouchX, mTouchY, mRippleRadius, ripplePaint!!)
                    }
                }
            }
            STATE_ANIMATION_STEP2 -> {
                canvas.drawCircle(
                    (width / 2),
                    (height / 2),
                    (mRadius - mScaleHeight).toFloat(),
                    mPaint!!
                )
                canvas.drawCircle((width / 2), (height / 2), mRadius - mDensity, mStrokePaint!!)
            }
            STATE_ANIMATION_LOADING -> {
                mPath!!.reset()
                mPath!!.addArc(mArcRectF, (270 + mAngle / 2).toFloat(), (360 - mAngle).toFloat())
                if (mAngle != 0) {
                    mMatrix.setRotate(mDegree.toFloat(), (width / 2), (height / 2))
                    mPath!!.transform(mMatrix)
                    mDegree += 10
                }
                canvas.drawPath(mPath!!, mStrokePaint!!)
            }
            STATE_STOP_LOADING -> {
                mPath!!.reset()
                mPath!!.addArc(mArcRectF, (270 + mAngle / 2).toFloat(), mEndAngle.toFloat())
                if (mEndAngle != 360) {
                    mMatrix.setRotate(mDegree.toFloat(), (width / 2), (height / 2))
                    mPath!!.transform(mMatrix)
                    mDegree += 10
                }
                canvas.drawPath(mPath!!, mStrokePaint!!)
            }
            STATE_ANIMATION_SUCCESS -> {
                canvas.drawPath(mSuccessPath!!, mPathEffectPaint!!)
                canvas.drawCircle((width / 2), (height / 2), mRadius - mDensity, mStrokePaint!!)
            }
            STATE_ANIMATION_FAILED -> {
                canvas.drawPath(mFailedPath!!, mPathEffectPaint!!)
                canvas.drawPath(mFailedPath2!!, mPathEffectPaint2!!)
                canvas.drawCircle((width / 2), (height / 2), mRadius - mDensity, mStrokePaint!!)
            }
        }
    }

    private fun playStartAnimation(isReverse: Boolean) {

        val animator = ValueAnimator.ofInt(
            if (isReverse) (width / 2 - height / 2).toInt() else 0,
            if (isReverse) 0 else (width / 2 - height / 2).toInt()
        )
        animator.addUpdateListener { valueAnimator ->
            mScaleWidth = valueAnimator.animatedValue as Int
            invalidate()
        }
        animator.duration = 400
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.startDelay = 100
        animator.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {
                currentState = if (isReverse) STATE_BUTTON else STATE_ANIMATION_STEP2
                if (currentState == STATE_BUTTON) {
                    setShadowDepth1()
                    invalidate()
                }
            }
        })

        val animator2 = ValueAnimator.ofInt(if (isReverse) mRadius else 0, if (isReverse) 0 else mRadius)
        animator2.addUpdateListener { valueAnimator ->
            mScaleHeight = valueAnimator.animatedValue as Int
            invalidate()
        }
        animator2.duration = 240
        animator2.interpolator = AccelerateDecelerateInterpolator()
        animator2.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {
                currentState = if (isReverse) STATE_ANIMATION_STEP1 else STATE_ANIMATION_LOADING
            }
        })

        val loadingAnimator = ValueAnimator.ofInt(30, 300)
        loadingAnimator.addUpdateListener { valueAnimator ->
            mAngle = valueAnimator.animatedValue as Int
            invalidate()
        }
        loadingAnimator.duration = 1000
        loadingAnimator.repeatCount = ValueAnimator.INFINITE
        loadingAnimator.repeatMode = ValueAnimator.REVERSE
        loadingAnimator.interpolator = AccelerateDecelerateInterpolator()
        if (mLoadingAnimatorSet != null) {
            mLoadingAnimatorSet!!.cancel()
        }
        mLoadingAnimatorSet = AnimatorSet()
        if (isReverse) {
            mLoadingAnimatorSet!!.playSequentially(animator2, animator)
        } else {
            mLoadingAnimatorSet!!.playSequentially(animator, animator2, loadingAnimator)
        }
        mLoadingAnimatorSet!!.start()
    }

    private fun playSuccessAnimation() {

        createSuccessPath()

        val animator = ValueAnimator.ofInt(360 - mAngle, 360)
        animator.addUpdateListener { valueAnimator ->
            mEndAngle = valueAnimator.animatedValue as Int
            invalidate()
        }
        animator.duration = 240
        animator.interpolator = DecelerateInterpolator()
        animator.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {
                currentState = STATE_ANIMATION_SUCCESS
            }
        })

        val successAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        successAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            val PathEffect = DashPathEffect(mSuccessPathIntervals, mSuccessPathLength - mSuccessPathLength * value)
            mPathEffectPaint!!.pathEffect = PathEffect
            invalidate()
        }
        successAnimator.duration = 500
        successAnimator.interpolator = AccelerateDecelerateInterpolator()
        val set = AnimatorSet()
        set.playSequentially(animator, successAnimator)
        set.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {

                if (isResetAfterFailed) {
                    postDelayed({ scaleFailedPath() }, 1000)
                    return
                }

                if (mAnimationEndListener != null) {
                    mAnimationEndListener!!.onAnimationEnd(AnimationType.SUCCESSFUL)
                }
            }
        })
        set.start()
    }

    private fun playFailedAnimation() {

        createFailedPath()

        val animator = ValueAnimator.ofInt(360 - mAngle, 360)
        animator.addUpdateListener { valueAnimator ->
            mEndAngle = valueAnimator.animatedValue as Int
            invalidate()
        }
        animator.duration = 240
        animator.interpolator = DecelerateInterpolator()
        animator.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {
                currentState = STATE_ANIMATION_FAILED
            }
        })

        val failedAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        failedAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            val PathEffect = DashPathEffect(mFailedPathIntervals, mFailedPathLength - mFailedPathLength * value)
            mPathEffectPaint!!.pathEffect = PathEffect
            invalidate()
        }
        failedAnimator.duration = 300
        failedAnimator.interpolator = AccelerateDecelerateInterpolator()

        val failedAnimator2 = ValueAnimator.ofFloat(0.0f, 1.0f)
        failedAnimator2.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            val PathEffect = DashPathEffect(mFailedPathIntervals, mFailedPathLength - mFailedPathLength * value)
            mPathEffectPaint2!!.pathEffect = PathEffect
            invalidate()
        }
        failedAnimator2.duration = 300
        failedAnimator2.interpolator = AccelerateDecelerateInterpolator()

        val set = AnimatorSet()
        set.playSequentially(animator, failedAnimator, failedAnimator2)
        set.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {
                if (isResetAfterFailed) {
                    postDelayed({ scaleFailedPath() }, 1000)
                    return
                }

                if (mAnimationEndListener != null) {
                    mAnimationEndListener!!.onAnimationEnd(AnimationType.FAILED)
                }
            }
        })
        set.start()
    }

    private fun cancel() {
        currentState = STATE_STOP_LOADING
        val animator = ValueAnimator.ofInt(360 - mAngle, 360)
        animator.addUpdateListener { valueAnimator ->
            mEndAngle = valueAnimator.animatedValue as Int
            invalidate()
        }
        animator.duration = 240
        animator.interpolator = DecelerateInterpolator()
        animator.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {
                currentState = STATE_ANIMATION_STEP2
                playStartAnimation(true)
            }
        })
        animator.start()
    }

    private fun scaleSuccessPath() {
        val scaleMatrix = Matrix()
        val scaleAnimator = ValueAnimator.ofFloat(1.0f, 0.0f)
        scaleAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            scaleMatrix.setScale(value, value, (width / 2), (height / 2))
            mSuccessPath!!.transform(scaleMatrix)
            invalidate()
        }
        scaleAnimator.duration = 300
        scaleAnimator.interpolator = AccelerateDecelerateInterpolator()
        scaleAnimator.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {
                currentState = STATE_ANIMATION_STEP2
                playStartAnimation(true)
            }
        })
        scaleAnimator.start()
    }

    private fun scaleFailedPath() {
        val scaleMatrix = Matrix()
        val scaleAnimator = ValueAnimator.ofFloat(1.0f, 0.0f)
        scaleAnimator.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
            try {
                val value = valueAnimator.animatedValue as Float
                scaleMatrix.setScale(value, value, (width / 2), (height / 2))

                if (mFailedPath == null || mFailedPath2 == null) {
                    //ca
                    return@AnimatorUpdateListener
                }

                mFailedPath!!.transform(scaleMatrix)
                mFailedPath2!!.transform(scaleMatrix)
                invalidate()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        scaleAnimator.duration = 300
        scaleAnimator.interpolator = AccelerateDecelerateInterpolator()
        scaleAnimator.addListener(object : AnimatorEndListener() {
            override fun onAnimationEnd(animator: Animator) {
                currentState = STATE_ANIMATION_STEP2
                playStartAnimation(true)
            }
        })
        scaleAnimator.start()
    }

    private fun playRippleAnimation(isTouchDown: Boolean) {
        setShadowDepth2()
        val rippleAnimator =
            ValueAnimator.ofFloat(
                if (isTouchDown) 0f else (width / 2),
                if (isTouchDown) (width / 2) else width
            )
        rippleAnimator.addUpdateListener { valueAnimator ->
            mRippleRadius = valueAnimator.animatedValue as Float
            invalidate()
        }
        rippleAnimator.duration = 240
        rippleAnimator.interpolator = AccelerateDecelerateInterpolator()
        if (!isTouchDown) {
            rippleAnimator.addListener(object : AnimatorEndListener() {
                override fun onAnimationEnd(animator: Animator) {
                    performClick()
                    mTouchX = 0f
                    mTouchY = 0f
                    mRippleRadius = 0f
                    invalidate()
                }
            })
        }
        rippleAnimator.start()
    }

    enum class AnimationType {
        SUCCESSFUL, FAILED
    }

    interface AnimationEndListener {
        fun onAnimationEnd(animationType: AnimationType)
    }

    private abstract inner class AnimatorEndListener : Animator.AnimatorListener {

        override fun onAnimationStart(animator: Animator) {}

        override fun onAnimationCancel(animator: Animator) {}

        override fun onAnimationRepeat(animator: Animator) {}
    }

    companion object {

        private const val STATE_BUTTON = 0
        private const val STATE_ANIMATION_STEP1 = 1
        private const val STATE_ANIMATION_STEP2 = 2
        private const val STATE_ANIMATION_LOADING = 3
        private const val STATE_STOP_LOADING = 4
        private const val STATE_ANIMATION_SUCCESS = 5
        private const val STATE_ANIMATION_FAILED = 6
    }

}
