package com.example.w7

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_drawer_layout.*
import kotlinx.android.synthetic.main.item_nav_header.*

class DrawerLayoutActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 111
        private const val REQUEST_GET_CONTENT_IMAGE = 222
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_layout)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                container.translationX = drawerView.width * slideOffset
            }
        }
        drawerToggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val navItems = mutableListOf<NavItem?>()
        navItems.add(null)
        navItems.add(NavItem(R.drawable.ic_inbox, resources.getString(R.string.text_view_inbox)))
        navItems.add(NavItem(R.drawable.ic_send, resources.getString(R.string.text_view_send)))
        navItems.add(NavItem(R.drawable.ic_trash, resources.getString(R.string.text_view_trash)))
        navItems.add(NavItem(R.drawable.ic_spam, resources.getString(R.string.text_view_spam)))
        recyclerView.adapter = NavItemAdapter(navItems)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> if (resultCode == RESULT_OK) {
                imgAvatar.setImageBitmap(data?.extras?.get("data") as Bitmap)
            }
            REQUEST_GET_CONTENT_IMAGE -> if (resultCode == RESULT_OK) {
                imgAvatar.setImageURI(data?.data)
            }
        }
    }
}