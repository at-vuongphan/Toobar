package com.example.w7


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class NavItemAdapter(private val navItems: MutableList<NavItem?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
        private const val REQUEST_IMAGE_CAPTURE = 111
        private const val REQUEST_GET_CONTENT_IMAGE = 222
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_nav_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nav, parent, false)
            NavItemViewHolder(view)
        }
    }

    override fun getItemCount() = navItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NavItemViewHolder) {
            holder.onBind(position)
        } else if (holder is HeaderViewHolder) {
            holder.onBind()
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind() {
            itemView.findViewById<ImageView>(R.id.imgAvatar).setOnClickListener {
                AlertDialog.Builder(itemView.context).apply {
                    setTitle("Choose Action ")
                    setMessage("Take photo from camera or gallery?")

                    setPositiveButton("Camera") { _, _ ->
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                            takePictureIntent.resolveActivity(context.packageManager)?.also {
                                (itemView.context as Activity)?.startActivityForResult(
                                    Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                                    REQUEST_IMAGE_CAPTURE
                                )
                            }
                        }
                    }

                    setNegativeButton("Gallery") { _, _ ->
                        val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        intent.type = "image/*"
                        intent.resolveActivity(context.packageManager)?.also {
                            (itemView.context as Activity)?.startActivityForResult(
                                intent,
                                REQUEST_GET_CONTENT_IMAGE
                            )
                        }
                    }

                    setNeutralButton("Cancel") { _, _ ->
                    }
                }.create().show()
            }
        }
    }

    inner class NavItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(position: Int) {
            val navItem = navItems[position]

            val icNavItem = itemView.findViewById<ImageView>(R.id.icNavItem)
            val tvNavItem = itemView.findViewById<TextView>(R.id.tvNavItem)

            navItem?.also {
                icNavItem.setImageResource(it.icon)
                tvNavItem.text = it.title
            }

            itemView.findViewById<ConstraintLayout>(R.id.llNavItem).setOnClickListener {
                Toast.makeText(itemView.context, "${navItem?.title} is clicked", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER
        else VIEW_TYPE_ITEM
    }
}