package com.iflytek.iFrameworkDemo;

import android.app.ListActivity;
import android.os.Bundle;
import com.iflytek.iFrameworkDemo.download.DownloadTaskAdapter;
import com.iflytek.iFrameworkDemo.download.SourceProvicer;

public class MainActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.downloadlib_main_layout);

        // create an array of Strings, that will be put to our ListActivity
        DownloadTaskAdapter adapter = new DownloadTaskAdapter(this,
                SourceProvicer.getTaskList());
        setListAdapter(adapter);
//        setContentView(R.layout.main);
//        FinalDb db = FinalDb.create(this, Environment.getExternalStorageDirectory()+"/iflytek/","qmy.db");
//
//        User user = new User();
//        user.setEmail("afinal@tsz.net");
//        user.setName("探索者");
//        user.setRegisterDate(new Date());
//
//        db.save(user);
//
//        List<User> userList = db.findAll(User.class);//查询所有的用户
//
//        Logger.e("AfinalOrmDemoActivity", "用户数量："+ (userList!=null?userList.size():0));
    }
}
