package com.example.roomsqlapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.example.roomsqlapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)


        val db = MainDb.getDb(this) // создает (если нет ее) и подключается к бд

        db.getDao().getAllItems().asLiveData().observe(this){ list ->
            bind.DataTV.text = ""
            list.forEach{
                val text = "Id: ${it.id}; Name: ${it.name}; Price ${it.price}\n"
                bind.DataTV.append(text)
            }
        }

        bind.SaveBTN.setOnClickListener{
            val item = Item(null,
            bind.NamePT.text.toString(), // передаем введенный текст в таблицу бд
            bind.PricePT.text.toString()
            )
            Thread {
                db.getDao().insertItem(item)
            }.start()
            Toast.makeText(this, "Запись добавлена в базу", Toast.LENGTH_SHORT).show()
            bind.NamePT.setText("")
            bind.PricePT.setText("")
        }

    }
}