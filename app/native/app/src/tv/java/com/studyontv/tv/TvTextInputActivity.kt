package com.studyontv.tv

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.InputType
import android.view.Gravity
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import org.json.JSONObject

/**
 * 电视版填空 / 主观题作答页：网页里的输入框无法用遥控器打字，这里用原生输入框调出系统输入法，
 * 支持遥控器语音键（若电视装有语音识别）。方向键可在输入框和按钮之间移动。
 */
class TvTextInputActivity : Activity() {
    private lateinit var edit: EditText
    private var delivered = false
    private val requestId get() = intent.getStringExtra("requestId") ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val dp = resources.displayMetrics.density
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_HORIZONTAL
            setBackgroundColor(TvUi.background(this@TvTextInputActivity))
            setPadding((64 * dp).toInt(), (36 * dp).toInt(), (64 * dp).toInt(), (24 * dp).toInt())
        }
        root.addView(TextView(this).apply { text = "填写答案"; textSize = 26f; setTextColor(TvUi.foreground(this@TvTextInputActivity)) })
        val prompt = intent.getStringExtra("prompt").orEmpty()
        if (prompt.isNotBlank()) root.addView(TextView(this).apply {
            text = prompt.take(200); textSize = 16f; setTextColor(TvUi.muted(this@TvTextInputActivity)); maxLines = 3
            setPadding(0, (8 * dp).toInt(), 0, (12 * dp).toInt())
        }, LinearLayout.LayoutParams(-1, -2))
        edit = EditText(this).apply {
            hint = "用遥控器或语音输入，按回车或选择“确定”完成"
            // 不允许输入换行符：软键盘的回车键会变成「完成」并触发 IME action，
            // 这样在填空框里按回车就等于点击「确定」，而不会往答案里塞一个换行。
            // minLines 仍保留多行高度，长答案会按宽度自动折行显示。
            inputType = InputType.TYPE_CLASS_TEXT
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_EXTRACT_UI
            minLines = 2; maxLines = 5; gravity = Gravity.TOP or Gravity.START
            TvUi.field(this)
            setText(savedInstanceState?.getString("draft") ?: intent.getStringExtra("draft").orEmpty())
            setSelection(text.length)
            // 软键盘 / 输入法发出的「完成 / 回车」一律当作「确定」提交
            setOnEditorActionListener { _, action, event ->
                val enterKey = event?.keyCode == KeyEvent.KEYCODE_ENTER || event?.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER
                if (action == EditorInfo.IME_ACTION_DONE || action == EditorInfo.IME_ACTION_GO ||
                    action == EditorInfo.IME_ACTION_SEND || action == EditorInfo.IME_ACTION_SEARCH ||
                    action == EditorInfo.IME_ACTION_NEXT || enterKey) {
                    finishWith(true); true
                } else false
            }
        }
        root.addView(edit, LinearLayout.LayoutParams(-1, -2).apply { topMargin = (8 * dp).toInt() })
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER }
        fun add(label: String, primary: Boolean = false, action: () -> Unit) =
            row.addView(TvUi.button(this, label, primary, action), LinearLayout.LayoutParams(-2, -2).apply { leftMargin = (8 * dp).toInt(); rightMargin = (8 * dp).toInt() })
        add("确定", true) { finishWith(true) }
        if (packageManager.queryIntentActivities(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0).isNotEmpty()) add("语音输入") { voice() }
        add("清空") { edit.setText("") }
        add("取消") { finishWith(false) }
        root.addView(row, LinearLayout.LayoutParams(-1, -2).apply { topMargin = (20 * dp).toInt() })
        setContentView(root)
        edit.requestFocus()
        edit.post { (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT) }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        // 遥控器的语音键 / 搜索键直接进入语音输入
        if (event.action == KeyEvent.ACTION_DOWN && (event.keyCode == KeyEvent.KEYCODE_SEARCH || event.keyCode == KeyEvent.KEYCODE_VOICE_ASSIST)) { voice(); return true }
        // 填空框有焦点时，实体键盘 / 外接键盘的回车键 = 点击「确定」。
        // DOWN 与 UP 都消费掉，避免抬起事件在答案里插入换行；组合键（Shift/Ctrl/Alt）不拦截。
        if (::edit.isInitialized && edit.hasFocus() &&
            (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) &&
            !event.isShiftPressed && !event.isCtrlPressed && !event.isAltPressed) {
            if (event.action == KeyEvent.ACTION_DOWN && event.repeatCount == 0) finishWith(true)
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    private fun voice() {
        try {
            startActivityForResult(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-CN"), 7)
        } catch (_: Exception) { android.widget.Toast.makeText(this, "这台电视没有语音识别", android.widget.Toast.LENGTH_SHORT).show() }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val spoken = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull() ?: return
        edit.append(if (edit.text.isEmpty()) spoken else " $spoken")
    }

    private fun finishWith(save: Boolean) {
        if (delivered) return
        delivered = true
        AnswerInput.complete(requestId, if (save) JSONObject().put("text", edit.text.toString()) else null)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("draft", edit.text.toString())
    }

    override fun onDestroy() {
        if (!delivered && !isChangingConfigurations) AnswerInput.complete(requestId, null)
        super.onDestroy()
    }
}
