package com.iflytek.iFrameworkDemo.image;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.iflytek.iFramework.ui.universalimageloader.core.ImageLoader;
import com.iflytek.iFrameworkDemo.R;

/**
 * Created by xhrong on 2014/9/2.
 */
public class ImageLoaderActivity extends Activity {
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    // 一堆图片链接
    public static final String[] IMAGES = new String[] {
            "http://tabletpcssource.com/wp-content/uploads/2011/05/android-logo.png",
            "http://simpozia.com/pages/images/stories/windows-icon.png",
            "https://si0.twimg.com/profile_images/1135218951/gmail_profile_icon3_normal.png",
            "http://www.krify.net/wp-content/uploads/2011/09/Macromedia_Flash_dock_icon.png",
            "http://radiotray.sourceforge.net/radio.png",
            "http://www.bandwidthblog.com/wp-content/uploads/2011/11/twitter-logo.png",
            "http://weloveicons.s3.amazonaws.com/icons/100907_itunes1.png",
            "http://weloveicons.s3.amazonaws.com/icons/100929_applications.png",
            "http://www.idyllicmusic.com/index_files/get_apple-iphone.png",
            "http://www.frenchrevolutionfood.com/wp-content/uploads/2009/04/Twitter-Bird.png",
            "http://3.bp.blogspot.com/-ka5MiRGJ_S4/TdD9OoF6bmI/AAAAAAAAE8k/7ydKtptUtSg/s1600/Google_Sky%2BMaps_Android.png",
            "http://www.desiredsoft.com/images/icon_webhosting.png",
            "http://goodereader.com/apps/wp-content/uploads/downloads/thumbnails/2012/01/hi-256-0-99dda8c730196ab93c67f0659d5b8489abdeb977.png",
            "http://1.bp.blogspot.com/-mlaJ4p_3rBU/TdD9OWxN8II/AAAAAAAAE8U/xyynWwr3_4Q/s1600/antivitus_free.png",
            "http://cdn3.iconfinder.com/data/icons/transformers/computer.png",
            "http://cdn.geekwire.com/wp-content/uploads/2011/04/firefox.png?7794fe",
            "https://ssl.gstatic.com/android/market/com.rovio.angrybirdsseasons/hi-256-9-347dae230614238a639d21508ae492302340b2ba",
            "http://androidblaze.com/wp-content/uploads/2011/12/tablet-pc-256x256.jpg",
            "http://www.theblaze.com/wp-content/uploads/2011/08/Apple.png",
            "http://1.bp.blogspot.com/-y-HQwQ4Kuu0/TdD9_iKIY7I/AAAAAAAAE88/3G4xiclDZD0/s1600/Twitter_Android.png",
            "http://3.bp.blogspot.com/-nAf4IMJGpc8/TdD9OGNUHHI/AAAAAAAAE8E/VM9yU_lIgZ4/s1600/Adobe%2BReader_Android.png",
            "http://cdn.geekwire.com/wp-content/uploads/2011/05/oovoo-android.png?7794fe",
            "http://icons.iconarchive.com/icons/kocco/ndroid/128/android-market-2-icon.png",
            "http://thecustomizewindows.com/wp-content/uploads/2011/11/Nicest-Android-Live-Wallpapers.png",
            "http://c.wrzuta.pl/wm16596/a32f1a47002ab3a949afeb4f",
            "http://macprovid.vo.llnwd.net/o43/hub/media/1090/6882/01_headline_Muse.jpg",
            // Special cases
            "http://cdn.urbanislandz.com/wp-content/uploads/2011/10/MMSposter-large.jpg", // very large image
            "file:///sdcard/Universal Image Loader @#&=+-_.,!()~'%20.png", // Image from SD card with encoded symbols
            "assets://Living Things @#&=+-_.,!()~'%20.jpg", // Image from assets
            "drawable://" + R.drawable.ic_launcher, // Image from drawables
            "http://upload.wikimedia.org/wikipedia/ru/b/b6/袣邪泻_泻芯褌_褋_屑褘褕邪屑懈_胁芯械胁邪谢.png", // Link with UTF-8
            "https://www.eff.org/sites/default/files/chrome150_0.jpg", // Image from HTTPS
            "http://bit.ly/soBiXr", // Redirect link
            "http://img001.us.expono.com/100001/100001-1bc30-2d736f_m.jpg", // EXIF
            "", // Empty link
            "http://wrong.site.com/corruptedLink", // Wrong link
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_loader_gridview_layout);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter());           // 填充数据
    }


    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                imageView = (ImageView) getLayoutInflater().inflate(R.layout.image_loader_gridview_item_layout, parent, false);
            } else {
                imageView = (ImageView) convertView;
            }


            // 将图片显示任务增加到执行池，图片将被显示到ImageView当轮到此ImageView
            imageLoader.displayImage(IMAGES[position], imageView);

            return imageView;
        }
    }
}
