package com.maho_ya.tell_me_your_dpi

import android.R.attr.label
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val disp = wm.defaultDisplay
        val realSize = Point()
        disp.getRealSize(realSize)


        val androidCodeName= when(Build.VERSION.SDK_INT){
            Build.VERSION_CODES.BASE, Build.VERSION_CODES.BASE_1_1  -> "Base"
            Build.VERSION_CODES.CUPCAKE -> "Cupcake"
            Build.VERSION_CODES.DONUT -> "Donut"
            Build.VERSION_CODES.ECLAIR, Build.VERSION_CODES.ECLAIR_0_1, Build.VERSION_CODES.ECLAIR_MR1  -> "Eclair"
            Build.VERSION_CODES.FROYO -> "Froyo"
            Build.VERSION_CODES.GINGERBREAD, Build.VERSION_CODES.GINGERBREAD_MR1 -> "Gingerbread"
            Build.VERSION_CODES.HONEYCOMB, Build.VERSION_CODES.HONEYCOMB_MR1, Build.VERSION_CODES.HONEYCOMB_MR2 -> "Honeycomb"
            Build.VERSION_CODES.ICE_CREAM_SANDWICH, Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 -> "Ice Cream Sandwich"
            Build.VERSION_CODES.JELLY_BEAN, Build.VERSION_CODES.JELLY_BEAN_MR1, Build.VERSION_CODES.JELLY_BEAN_MR2 -> "Jelly Bean"
            Build.VERSION_CODES.KITKAT, Build.VERSION_CODES.KITKAT_WATCH -> "KitKat/4.4W"
            Build.VERSION_CODES.LOLLIPOP, Build.VERSION_CODES.LOLLIPOP_MR1 -> "Lollipop"
            Build.VERSION_CODES.M -> "Marshmallow"
            Build.VERSION_CODES.N, Build.VERSION_CODES.N_MR1 -> "Nougat"
            Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1 -> "Oreo"
            Build.VERSION_CODES.P -> "Pie"
            Build.VERSION_CODES.Q -> "Android 10 (Q)"
            30 -> "Android 11 (R)"
            else -> "UnKnown"
        }


        // /android-29/android/content/res/Configuration.java
        val dpi2 = when (resources.displayMetrics.densityDpi) {
            Configuration.DENSITY_DPI_UNDEFINED -> "UnDefined"
            120 -> "ldpi"
            160 -> "mdpi"
            213 -> "tvdpi"
            240 -> "hdpi"
            320 -> "xhdpi"
            480 -> "xxhdpi"
            640 -> "xxxhdpi"
            0xfffe -> "anydpi"
            0xffff-> "nodpi"
            else -> resources.displayMetrics.densityDpi.toString() + "dpi"
        }

        val dpi = when (resources.displayMetrics.densityDpi) {
            0 -> "UnDefined"
            in 1..120 -> "ldpi"
            in 121..160 -> "mdpi"
            in 161..240 -> "hdpi"
            in 241..320 -> "xhdpi"
            in 321..480 -> "xxhdpi"
            in 481..640 -> "xxxhdpi"
            else -> resources.displayMetrics.densityDpi.toString() + "dpi"
        }





        val activityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val totalMemory = (memoryInfo.totalMem / 1024 / 1024).toInt()
        val availableMemory = (memoryInfo.availMem / 1024 / 1024).toInt()


        val dpiWidth = (realSize.x / resources.displayMetrics.xdpi).toInt()
        val dpiHeight = (realSize.y / resources.displayMetrics.density).toInt()

        val mutableList = mutableListOf<MyData>()
        mutableList.add(MyData("Density qualifier", dpi))
        mutableList.add(MyData("Density DPI", "${resources.displayMetrics.densityDpi}"))
        mutableList.add(MyData("Real Display Size - Width", "${realSize.x} px"))
        mutableList.add(MyData("Real Display Size - Height",  "${realSize.y} px"))
        mutableList.add(MyData("デバイス",  Build.DEVICE))
        mutableList.add(MyData("ブランド",  Build.BRAND))
        mutableList.add(MyData("モデル",  Build.MODEL))
        mutableList.add(MyData("API Level",  "${Build.VERSION.SDK_INT}"))
        mutableList.add(MyData("Android バージョン",  Build.VERSION.RELEASE))
        mutableList.add(MyData("コードネーム",  androidCodeName))
        mutableList.add(MyData("メモリ / 利用可能 （参考値）",
            "${convertMemorySizeToMB(getMemoryInfo().totalMem)} MB" +
            " / ${convertMemorySizeToMB(getMemoryInfo().availMem)} MB"))


        viewManager = LinearLayoutManager(this)
        viewAdapter = DpiInfoAdapter(mutableList)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }


        val fab: com.google.android.material.floatingactionbutton.FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            throw RuntimeException("Test Crash Release")

            val clipboardManager: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            var clip = "";
            mutableList.forEach {
                clip += it.key + ":" + it.value + "\n"
            }

            clipboardManager.setPrimaryClip(ClipData.newPlainText("", clip))
            //fab.setImageResource(R.drawable.ic_check)
            //fab.setRippleColor(ContextCompat.getColor(this, R.color.colorPrimary))
            Snackbar.make(view, "情報をコピーしました。", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }
    }

    private fun convertMemorySizeToMB(memorySize: Long): Int {
        return (memorySize / 1024 / 1024).toInt()
    }

    // Get a MemoryInfo object for the device's current memory status.
    private fun getMemoryInfo(): ActivityManager.MemoryInfo {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return ActivityManager.MemoryInfo().also { memoryInfo ->
            activityManager.getMemoryInfo(memoryInfo)
        }
    }
}
