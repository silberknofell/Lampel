package net.geihe.lampel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import net.geihe.lampel.ampel.Ampel
import net.geihe.lampel.ampel.Zustand
import net.geihe.lampel.preferences.AppPreferences

/**
 * TODO: document your custom view class.
 */
class MyAmpelView : View {
    val SKALATEXTGROESSE = 20f
    val LINIENBREITE = 0.003

    internal val clGelb = Color.YELLOW
    internal val clGruen = Color.GREEN
    internal val clRot = Color.RED
    internal val linePaint = Paint()
    internal val paintRed = Paint()
    internal val paintGreen = Paint()
    internal val paintYellow = Paint()
    internal val skalaPaint = Paint()
    internal val backgroundPaint = Paint()

    internal var padding_top: Int = 0
    internal var p_padding_left_right: Int = 100

    public lateinit var ampel: Ampel ;


    //    private var ampel: ReadOnlyAmpel? = null
    private var modGrenzenVerschieben: Boolean = false
    private var p_skalaAnzeigen: Boolean = AppPreferences.skalaAnzeigen

    constructor(ampelViewActivity: AmpelViewActivity) : super(ampelViewActivity) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        setPaints()

        modGrenzenVerschieben = false

        setOnTouchListener { _, event ->
            if (event.y > padding_top && modGrenzenVerschieben) {
                var y = 1.0 - (event.y - padding_top) / (height - padding_top)
                y = Math.round(y * 20) / 20.0 // auf 5%-Schritte normieren
                if (Math.abs(y - ampel.grenzeGelb) > Math.abs(y - ampel.grenzeGruen)) {
                    ampel.grenzeGruen = y
                } else {
                    ampel.grenzeGelb = y
                }
                val mitte = width / 2
                p_padding_left_right = mitte - Math.abs(event.x.toInt() - mitte)
                invalidate()
            }

            if (event.action == MotionEvent.ACTION_UP) {
                modGrenzenVerschieben = false
                //					return true;
            }

            false
        }

        setOnLongClickListener {
            modGrenzenVerschieben = true
            true
        }
    }

    private fun setPaints() {
        paintRed.color = Color.RED
        paintRed.isAntiAlias = true
        paintYellow.color = Color.YELLOW
        paintYellow.isAntiAlias = true
        paintGreen.color = Color.GREEN
        paintGreen.isAntiAlias = true
        linePaint.color = Color.GRAY
        linePaint.isAntiAlias = true
        linePaint.textSize = 20f

        skalaPaint.textAlign = Paint.Align.RIGHT
        skalaPaint.isAntiAlias = true
        skalaPaint.textSize = SKALATEXTGROESSE
        backgroundPaint.color = Color.BLACK
    }

    override fun onSizeChanged(height: Int, width: Int, height_old: Int,
                               width_old: Int) {
        padding_top = Math.round(SKALATEXTGROESSE)
        readPrefs()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility != View.VISIBLE) {
            AppPreferences.paddingLeftRight = p_padding_left_right
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val lautstaerkeAmpel = ampel.lautstaerke
        val grenzeGruen = ampel.grenzeGruen
        val grenzeGelb = ampel.grenzeGelb

        // Balken zeichnen
        when (ampel.zustand) {
            Zustand.GRUEN -> {
                zeichneRundenBalken(0.0, lautstaerkeAmpel, canvas, paintGreen)
            }
            Zustand.GELB -> {
                zeichneRundenBalken(0.0, grenzeGruen, canvas, paintGreen)
                zeichneRundenBalken(grenzeGruen, lautstaerkeAmpel, canvas, paintYellow)
            }
            Zustand.ROT -> {
                zeichneRundenBalken(0.0, grenzeGruen, canvas, paintGreen)
                zeichneRundenBalken(grenzeGruen, grenzeGelb, canvas, paintYellow)
                zeichneRundenBalken(grenzeGelb, lautstaerkeAmpel, canvas, paintRed)
            }
            else -> {
                //nichts zu zeichnen
            }
        } // ende switch


        // Grenzen zeichnen
        linePaint.color = Color.GRAY
        zeichneRundenBalken(grenzeGruen - LINIENBREITE, grenzeGruen + LINIENBREITE, canvas, linePaint)

        canvas.drawText(text(grenzeGruen), 1f,
                y(grenzeGruen + LINIENBREITE * 2).toFloat(), linePaint)

        linePaint.color = Color.LTGRAY
        zeichneRundenBalken(grenzeGelb - LINIENBREITE, grenzeGelb + LINIENBREITE, canvas, linePaint)
        canvas.drawText(text(grenzeGelb), 1f, y(grenzeGelb + LINIENBREITE * 2).toFloat(),
                linePaint)

        // Skala zeichnen
        if (p_skalaAnzeigen) {
            var y = 0.0
            while (y <= 1.0) {
                if (y <= grenzeGruen) {
                    skalaPaint.color = clGruen
                } else if (y <= grenzeGelb) {
                    skalaPaint.color = clGelb
                } else {
                    skalaPaint.color = clRot
                }

                val b = y(y)
                // Log.d("y", Integer.toString(b));

                canvas.drawText(text(y), width.toFloat(), (b - 5).toFloat(), skalaPaint)
                canvas.drawLine((width * 0.85).toFloat(), b.toFloat(), width.toFloat(), b.toFloat(),
                        skalaPaint)
                y += 0.2
            }
        }
    }

    fun readPrefs() {
        p_skalaAnzeigen = AppPreferences.skalaAnzeigen
        p_padding_left_right = AppPreferences.paddingLeftRight
    }

    private fun text(doubleZahl: Double): String {
        return java.lang.Long.toString(Math.round(doubleZahl * 100)) + "%"
    }

    private fun y(wert: Double): Int {
        return Math.round((1 - wert) * (height - padding_top) + padding_top).toInt() - 1
    }

    private fun zeichneBalken(von: Double, bis: Double, canvas: Canvas,
                              paint: Paint) {
        canvas.drawRect(p_padding_left_right.toFloat(), y(bis).toFloat(), (width - p_padding_left_right).toFloat(), y(von).toFloat(), paint)
    }

    private fun zeichneRundenBalken(von: Double, bis: Double, canvas: Canvas,
                                    paint: Paint) {
        val rect = RectF(p_padding_left_right.toFloat(), y(bis).toFloat(), (width - p_padding_left_right).toFloat(), y(von).toFloat())
        canvas.drawRoundRect(rect, 10f, 20f, paint)
    }


}