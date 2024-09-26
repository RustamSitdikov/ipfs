package ru.mail.technotrack.ipfs.activities

import android.os.Bundle
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_scrolling.*
import ru.mail.technotrack.ipfs.R
import ru.mail.technotrack.ipfs.api.DTO.FileInfo
import ru.mail.technotrack.ipfs.utils.getTypeFile

class ScrollingActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var type: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item = intent.extras.getSerializable("item") as FileInfo
        setContentView(R.layout.activity_scrolling)

        name = findViewById(R.id.name)
        name.setText(item.name)

        type = findViewById(R.id.type)
        type.setText(item.type?.let { getTypeFile(it) })

        title = item.name

        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}
