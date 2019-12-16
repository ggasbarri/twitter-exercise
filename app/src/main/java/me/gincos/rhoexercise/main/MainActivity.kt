package me.gincos.rhoexercise.main

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import me.gincos.rhoexercise.R
import org.koin.android.ext.android.inject
import android.widget.Toast
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import android.view.ViewGroup
import android.widget.FrameLayout
import me.gincos.rhoexercise.utils.dpToPx


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel(name = MainViewModel.mainVmName)
    private val adapter: MainAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeData()
        setupRecyclerView()

        showInitDialog()

        mainViewModel.initNetworkListening(this)

    }

    private fun showInitDialog() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(getString(R.string.dialog_title))
        alert.setMessage(getString(R.string.dialog_message))

        val input = EditText(this)
        input.isSingleLine = true
        input.requestFocus()

        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val leftMargin = dpToPx(20f, this.resources)
        val topMargin = dpToPx(10f, this.resources)
        val rightMargin = dpToPx(20f, this.resources)
        val bottomMargin = dpToPx(4f, this.resources)
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)

        input.layoutParams = params

        container.addView(input)

        alert.setView(container)

        alert.setPositiveButton(
            getString(R.string.dialog_positive)
        ) { _, _ ->
            val value = input.text.toString()
            if (value.isNotEmpty()) {
                mainViewModel.search(value)
            } else {
                Toast.makeText(
                    applicationContext, getString(R.string.empty_query),
                    Toast.LENGTH_LONG
                ).show()
                showInitDialog()
            }
        }

        alert.show()
    }

    private fun observeData() {
        mainViewModel.searchResults.observe(this, Observer {
            adapter.submitList(it.toList().asReversed())
            when {
                it == null ||
                        // If there are no current tweets
                        it.isEmpty() -> {
                    progressBar.visibility = VISIBLE
                }
                else -> {
                    progressBar.visibility = GONE
                }
            }
        })
    }

    private fun setupRecyclerView() {

        val layoutManager = if (
            resources.configuration.orientation == ORIENTATION_LANDSCAPE
        ) {
            GridLayoutManager(
                this, 2
            )
        } else LinearLayoutManager(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recyclerView.layoutManager = if (
            resources.configuration.orientation == ORIENTATION_LANDSCAPE
        ) {
            GridLayoutManager(
                this, 2
            )
        } else LinearLayoutManager(this)
    }
}
