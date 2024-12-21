package com.example.romanticyeojido.ui.locker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView
import com.example.romanticyeojido.R
import com.example.romanticyeojido.network.Photo
import com.squareup.picasso.Picasso

class PhotoPagerAdapter(private val photoList: List<Photo>) : RecyclerView.Adapter<PhotoPagerAdapter.PhotoPagerViewHolder>() {

    inner class PhotoPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(photo: Photo) {
            // 여기서 findViewById로 ImageView를 찾습니다.
            val photoImageView = itemView.findViewById<ImageView>(R.id.viewPager2)

            // Picasso로 이미지 로드
            Picasso.get()
                .load(photo.image_url) // 실제 이미지 URL에 맞게 수정
                .into(photoImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoPagerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_pager, parent, false)
        return PhotoPagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PhotoPagerViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}
