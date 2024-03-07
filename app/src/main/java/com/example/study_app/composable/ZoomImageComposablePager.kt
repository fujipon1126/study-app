package com.example.study_app.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ZoomImageComposablePager() {
    val imageList = listOf(
        "https://developer.android.com/images/brand/Android_Robot.png",
        "https://1.bp.blogspot.com/-tVeC6En4e_E/X96mhDTzJNI/AAAAAAABdBo/jlD_jvZvMuk3qUcNjA_XORrA4w3lhPkdQCNcBGAsYHQ/s1048/onepiece01_luffy.png",
        "https://1.bp.blogspot.com/-rzRcgoXDqEg/YAOTCKoCpPI/AAAAAAABdOI/5Bl3_zhOxm07TUGzW8_83cXMOT9yy1VJwCNcBGAsYHQ/s1041/onepiece02_zoro_bandana.png",
        "https://1.bp.blogspot.com/-f77JyLzDMeg/X-FcxDMHWxI/AAAAAAABdEw/zSmsi1UHuHwfQTTyw3o9h4yTH_noWumdACNcBGAsYHQ/s981/onepiece15_lucci.png",
        "https://1.bp.blogspot.com/-JWY6R_ha5Uo/X-FcyyDEQyI/AAAAAAABdFI/lYwX7qMA_9wtH4-rWP-_eJT0AGHH4xERgCNcBGAsYHQ/s2048/onepiece20_santaisyou.png"
    )

    HorizontalPager(count = imageList.size) { page ->
        val isVisible by remember {
            derivedStateOf {
                val offset = calculateCurrentOffsetForPage(page)
                (-1.0f < offset) and (offset < 1.0f)
            }
        }
        ZoomImageComposable(imageUrl = imageList[page], isVisible = isVisible)
    }
}