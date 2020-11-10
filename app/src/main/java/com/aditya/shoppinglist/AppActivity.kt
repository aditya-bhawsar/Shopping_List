package com.aditya.shoppinglist

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_info_view.view.*

@Suppress("SENSELESS_COMPARISON")
class AppActivity : AppCompatActivity(), ItemAdapter.ItemListener {

    private lateinit var itemViewModel: ItemViewModel
    private lateinit var adapter :ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(7050)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.drawable.ic_paper)
        supportActionBar!!.title = "  Shopping List"

        adapter = ItemAdapter(this,this)
        list_rv.adapter = adapter
        list_rv.layoutManager = LinearLayoutManager(this)

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)
        itemViewModel.allItems.observe(this , Observer { items -> items.let { adapter.setItems(it) } })

        val addBtn = findViewById<FloatingActionButton>(R.id.add_btn)

        addBtn.setOnClickListener {
            val view:View = LayoutInflater.from(this).inflate(R.layout.item_details,null)

            val messageTv  = view.findViewById<MaterialTextView>(R.id.message_tv)
            val nameEt  = view.findViewById<TextInputEditText>(R.id.name_et)
            val amountEt  = view.findViewById<TextInputEditText>(R.id.amount_et)

            messageTv.text = "Add a new item"

            MaterialAlertDialogBuilder(this)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Add"){_,_->
                    if(nameEt.text.toString()==null || nameEt.text.toString().isEmpty()){
                        Snackbar.make(this.findViewById(android.R.id.content), "Name of an item cannot be empty",Snackbar.LENGTH_LONG).show()
                        return@setPositiveButton
                    }
                    val newItem = Item(0,nameEt.text.toString(),amountEt.text.toString())
                    itemViewModel.insert(newItem)
                }
                .setPositiveButtonIcon(resources.getDrawable(R.drawable.ic_add_dialog,this.theme))
                .show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager= getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search_menu).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = "Search item here..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { return false }
            override fun onQueryTextChange(newText: String): Boolean {
                itemViewModel.queryIt(newText)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.info_menu->{
                val view: View = LayoutInflater.from(this).inflate(R.layout.app_info_view,null)
                val versionCode = BuildConfig.VERSION_CODE
                val versionName = BuildConfig.VERSION_NAME
                view.code_show_tv.text = versionCode.toString()
                view.name_show_tv.text = versionName

                MaterialAlertDialogBuilder(this).setView(view).setCancelable(true).show()
            }
            R.id.rate_menu->{ Snackbar.make(this.findViewById(android.R.id.content), "This App is not on Play Store Yet",Snackbar.LENGTH_LONG).show() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun editClick(item: Item) {
        val view:View = LayoutInflater.from(this).inflate(R.layout.item_details,null)

        val messageTv  = view.findViewById<MaterialTextView>(R.id.message_tv)
        val nameEt  = view.findViewById<TextInputEditText>(R.id.name_et)
        val amountEt  = view.findViewById<TextInputEditText>(R.id.amount_et)

        messageTv.text = "Edit the item"
        nameEt.setText(item.name)
        amountEt.setText(item.amount)

        MaterialAlertDialogBuilder(this)
            .setView(view)
            .setCancelable(false)
            .setPositiveButton("Update"){_,_->
                if(nameEt.text.toString()==null || nameEt.text.toString().isEmpty()){
                    Snackbar.make(this.findViewById(android.R.id.content), "Name of an item cannot be empty",Snackbar.LENGTH_LONG).show()
                    return@setPositiveButton
                }
                val newItem = Item(item.id,nameEt.text.toString(),amountEt.text.toString())
                itemViewModel.update(newItem)
            }
            .setPositiveButtonIcon(resources.getDrawable(R.drawable.ic_save , this.theme))
            .setNegativeButton("Delete"){_,_-> itemViewModel.delete(item) }
            .setNegativeButtonIcon(resources.getDrawable(R.drawable.ic_delete , this.theme))
            .setNeutralButton("Cancel"){_,_-> }
            .show()
    }
}