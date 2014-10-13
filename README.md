# Android开发组件集iFramework开发文档

@[Android|iFramework|SDK|说明文档]

**iFramework**是收集了优秀的常用的Android开源项目，整合而成的Android开发组件集，同时进行了少量的必要的功能调整。目前，该组件集主要包括了以下几个模块：
- **Http**：目前集成了Asynchronous Http Client for Android，该组件是基于回调的异步有Http库，主要功能是HTTP请求，支持使get、post，支持文件上传等多种功能；简单地同步Http请求（需要在线程中执行）
- **Database**:目前集成的是Afinal框架中的finalDb模块，该模块使用简单，入门快
- **UI**:该模块主要收集了一些常用的UI组件或扩展，目前主要有Pull To Refresh Views for Android、Android Touch Gallery、Universal Image Loader for Android、TouchGallery（添加了文件缓存功能）、TagCloud（做了精简处理）、SlidingMenu、pinterestlikeadapterview（图片的流式布局）
- **Logger**:主要是用于将程序信息，记录成日志文件，方便程序发布后的问题跟踪。非Github项目，代码简单，建议直接阅读。
- **Downloader**：Android下载组件，支持暂停、恢复、取消操作
- **Utils**:暂无内容 


该组件集会根据项目使用需要，动态地添加相关组件。
> iFramework的获取地址：https://github.com/xhrong/iFramework


-------------------

## 模块简介

以下是iFramework各模块的简单介绍和使用说明，详细情况请访问各组件的官网或Github链接。 

### Http模块
该模块目前只有**Asynchronous Http Client for Android**
###1.Asynchronous Http Client for Android
Asynchronous Http Client for Android是一个基于回调的异步有Http库，主要功能是HTTP请求，支持使get、post，支持文件上传等多种功能。集成的版本是1.4.5。
> GitHub地址：https://github.com/loopj/android-async-http

####Feature
* 1.Make asynchronous HTTP requests, handle responses in anonymous callbacks
* 2.HTTP requests happen outside the UI thread
* 3.Requests use a threadpool to cap concurrent resource usage
* 4.GET/POST params builder (RequestParams)
* 5.Multipart file uploads with no additional third party libraries
* 6.Tiny size overhead to your application, only 60kb for everything
* 7.Automatic smart request retries optimized for spotty mobile connections
* 8.Automatic gzip response decoding support for super-fast requests
* 9.Optional built-in response parsing into JSON (JsonHttpResponseHandler)
* 10.Optional persistent cookie store, saves cookies into your app's SharedPreferences

####Examples
For inspiration and testing on device we've provided Sample Application.
See individual samples [here](https://github.com/loopj/android-async-http) on Github

####Javadoc
Latest Javadoc for 1.4.5 release are available here (also included in Maven repository):http://loopj.com/android-async-http/doc/
___
### Database模块
Database模块主要是通过ORM机制简化Sqlite的代码编写，目前集成的是Afinal框架中的finalDb模块，该模块使用简单，入门快。
### FinalDb
FinalDb是Android的orm框架，一行代码就可以进行增删改查。支持一对多，多对一等查询。
> Github地址：https://github.com/yangfuhai/afinal

####Examples
```java
FinalDb db = FinalDb.create(this);
User user = new User(); //这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
user.setEmail("mail@tsz.net");
user.setName("michael yang");
db.save(user);
```
具体示例请参考：http://my.oschina.net/yangfuhai/blog/87459
___
### UI模块
该模块主要收集了一些常用的UI组件或扩展，目前主要有**Pull To Refresh Views for Android**、**Android Touch Gallery**
###1.Pull To Refresh Views for Android
Pull To Refresh Views for Android是一个强大的拉动刷新开源项目，支持各种控件下拉刷新，ListView、ViewPager、WevView、ExpandableListView、GridView、ScrollView、Horizontal ScrollView、Fragment上下左右拉动刷新，比下面johannilsson那个只支持ListView的强大的多。并且它实现的下拉刷新ListView在item不足一屏情况下也不会显示刷新提示，体验更好。当前集成的是v2.1.1版本。
> Github地址：https://github.com/chrisbanes/Android-PullToRefresh

####Features
* 1.Supports both Pulling Down from the top, and Pulling Up from the bottom (or even both).
* 2.Animated Scrolling for all devices.
* 3.Over Scroll supports for devices on Android v2.3+.
* 4.Currently works with:ListView、ExpandableListView、GridView、WebView、ScrollView、HorizontalScrollView、ViewPager。
* 5.Integrated End of List Listener for use of detecting when the user has scrolled to the bottom.
* 6.Maven Support.
* 7.Indicators to show the user when a Pull-to-Refresh is available.
* 8.Support for ListFragment!
* 9.Lots of Customisation options!

####Examples
To begin using the library, please see the [Quick Start Guide](https://github.com/chrisbanes/Android-PullToRefresh/wiki/Quick-Start-Guide) page.

###2.Android Touch Gallery
Android Touch Gallery支持双击或双指缩放的Gallery(用ViewPager实现)，在被放大后依然能滑到下一个item，并且支持直接从url和文件中获取图片。
> Github地址：https://github.com/Dreddik/AndroidTouchGallery

####Examples
Include GalleryViewPager in your layout xml or programmatically.
```xml
<ru.truba.touchgallery.GalleryWidget.GalleryViewPager
     android:id="@+id/viewer"
     android:layout_width="fill_parent"
     android:layout_height="fill_parent"
     />
```
Provide one of library adapters: UrlPagerAdapter or FilePagerAdapter For example (an Activity code):
```java
setContentView(R.layout.main);
String[] urls = {
    "http://cs407831.userapi.com/v407831207/18f6/jBaVZFDhXRA.jpg",
    "http://cs407831.userapi.com/v4078f31207/18fe/4Tz8av5Hlvo.jpg", //special url with error
    "http://cs407831.userapi.com/v407831207/1906/oxoP6URjFtA.jpg",
    "http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
    "http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
    "http://cs407831.userapi.com/v407831207/191e/QEQE83Ok0lQ.jpg"
};
List<String> items = new ArrayList<String>();
Collections.addAll(items, urls);
UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);  
GalleryViewPager mViewPager = (GalleryViewPager)findViewById(R.id.viewer);
mViewPager.setOffscreenPageLimit(3);
mViewPager.setAdapter(pagerAdapter);
```


###3.CircleImageView
圆形的ImageView，主要适用于用户头像的显示。
> Github地址：https://github.com/hdodenhof/CircleImageView

####Usage
```xml
<de.hdodenhof.circleimageview.CircleImageView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/profile_image"
    android:layout_width="96dp"
    android:layout_height="96dp"
    android:src="@drawable/profile"
    app:border_width="2dp"
    app:border_color="#FF000000"/>
```
- **Limitations**
The ScaleType is always CENTER_CROP and you'll get an exception if you try to change it. This is (currently) by design as it's perfectly fine for profile images.
If you use Picasso for fetching images, you need to set the noFade() option to avoid messed up images. If you want to keep the fadeIn animation, you have to fetch the image into a Target and apply a custom animation when setting it as source for the CircleImageView in onBitmapLoaded().

###4.Universal Image Loader for Android
This aims to provide a reusable instrument for asynchronous image loading, caching and displaying. It is originally based on Fedor Vlasov's project and has been vastly refactored and improved since then.
> Github地址：https://github.com/nostra13/Android-Universal-Image-Loader/tree/v1.9.2

####Features
* 1.Multithread image loading
* 2.Possibility of wide tuning ImageLoader's configuration (thread executors, downloader,
* 3.decoder, memory and disk cache, display image options, and others)
* 4.Possibility of image caching in memory and/or on device's file system (or SD card)
* 5.Possibility to "listen" loading process
* 6.Possibility to customize every display image call with separated options
* 7.Widget support
* 8.Android 2.0+ support

####Examples
参见：https://github.com/nostra13/Android-Universal-Image-Loader/tree/v1.9.2
___
###Logger模块
Logger模块目前只有Logger组件。
###1.Logger
logger组件主要是用于将程序信息，记录成日志文件，方便程序发布后的问题跟踪。非Github项目，代码简单，建议直接阅读。
####Features
* 1、支持对日志输出等级的控制
* 2、支持日志是否输出到LogCat的控制
* 3、支持日志是否输出地SdCard的文件及日志等级的控制

####Examples
```java
	//debug为true时，日志会输出到LogCat
	public static boolean debug = true;
	//日志等级大于或等于该等级的信息才会被保存到sd卡
	public static int level = Log.ERROR; 
```
建议在程序的Application类中配置两个参数，方便日志的整体控制。

### Downloader模块
该模块通过线程进行文件下载，目前支持下载任务的暂停、恢复和取消，使用方便，扩展性较好。
####Features
* 1、支持断点续传
* 2、支持暂停、恢复、取消
* 3、可通过配置文件指定下载路径，下载数据库路径
* 4、支持携带额外参数，方便下载任务完成后的进一步处理
* 5、支持多任务并发下载，可配置并发数

#### Examples
```java
        IntentFilter intentFilter = new IntentFilter(DownloadManager.DWONLOAD_ACTION);
        //注册广播接收器
        registerReceiver(receiver, intentFilter);
        setContentView(R.layout.downloadlib_main_layout);
        taskList = SourceProvicer.getTaskList();
        adapter = new DownloadTaskAdapter(this, taskList);
        setListAdapter(adapter);
```
```java

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final DownloadTask ddt = ddList.get(position);
        View rowView = convertView;
        // reuse views
        if (rowView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.downloadlib_item_layout, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.textView);
            viewHolder.startBtn = (Button) rowView.findViewById(R.id.btnStart);
            viewHolder.pauseBtn = (Button) rowView.findViewById(R.id.btnPause);
            viewHolder.resumeBtn = (Button) rowView.findViewById(R.id.btnResume);
            viewHolder.cancelBtn = (Button) rowView.findViewById(R.id.btnCancel);
            viewHolder.pBar = (ProgressBar) rowView.findViewById(R.id.progressBar);
            viewHolder.text.setTag(ddt);
            rowView.setTag(viewHolder);
        }


        final ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.text.setText(ddt.getName());
        holder.pBar.setProgress((int) (ddt.getDownloadFinishedSize() * 100 / (ddt.getDownloadTotalSize() > 0 ? ddt.getDownloadTotalSize() : 1)));

        holder.pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadTask ddtask = (DownloadTask) holder.text.getTag();
                DownloadManager.getInstance(getContext()).pauseDownload(ddtask.getId());
            }
        });
        holder.resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadTask ddtask = (DownloadTask) holder.text.getTag();
                DownloadManager.getInstance(getContext()).resumeDownload(ddtask.getId());
            }
        });
        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadTask ddtask = (DownloadTask) holder.text.getTag();
                DownloadManager.getInstance(getContext()).cancelDownload(ddtask.getId());
            }
        });

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadTask d = (DownloadTask) holder.text.getTag();
                DownloadTask ddtask = new DownloadTask();
                ddtask.setName(d.getName());
                ddtask.setUrl(d.getUrl());
                ddtask.setDownloadSavePath(d.getDownloadSavePath());
                ddtask.setId(d.getId());
                String customParam = "{\"fileType\":\"zip\"}";
                ddtask.setCustomParam(customParam);

                if (ddtask.getStatus() == DownloadTask.STATUS_FINISHED || ddtask.getStatus() == DownloadTask.STATUS_ERROR) {//如果结束了，则重新下载
                    ddtask.setStatus(DownloadTask.STATUS_RUNNING);
                }
                DownloadManager.getInstance(getContext()).addDownloadTask(ddtask);
            }
        });
        return rowView;
    }
```

```java
class DownloadBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            DownloadTask task = (DownloadTask) intent.getSerializableExtra("task");
            int msgType = task.getStatus();
            //更新数据源
            for (DownloadTask t : taskList) {
                if (t.getId().equals(task.getId())) {
                    t.setCustomParam(task.getCustomParam());
                    t.setDownloadFinishedSize(task.getDownloadFinishedSize());
                    t.setDownloadSavePath(task.getDownloadSavePath());
                    t.setDownloadSpeed(task.getDownloadSpeed());
                    t.setDownloadTotalSize(task.getDownloadTotalSize());
                    t.setStatus(task.getStatus());
                }
            }
            if (msgType == DownloadTask.STATUS_FINISHED) {
                Toast.makeText(context, task.getId() + "下载成功", Toast.LENGTH_LONG).show();
                if (!task.getCustomParam().isEmpty()) {
                    Log.i(TAG, task.getCustomParam());//这里可以做特殊处理了。比如解压、打开
                }
            } else if (msgType == DownloadTask.STATUS_CANCELED) {
                Toast.makeText(context, task.getId() + "取消下载", Toast.LENGTH_LONG).show();
            }
            synchronized (STOP) {
                adapter.notifyDataSetChanged();
            }
        }
    }
```

详见：https://github.com/xhrong/iFramework/blob/master/iFrameworkDemo/src/com/iflytek/iFrameworkDemo/MainActivity.java

-------
## 反馈与建议
该组件集还在不断完善中，如有好的组件建议集成，或有任何反馈和建议，可能通过如下方式联系，大家一起完善该组件集。
- QQ：564067104


