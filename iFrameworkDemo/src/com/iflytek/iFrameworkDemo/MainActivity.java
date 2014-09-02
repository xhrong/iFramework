package com.iflytek.iFrameworkDemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import com.iflytek.iFramework.database.finaldb.FinalDb;
import com.iflytek.iFramework.logger.Logger;
import com.iflytek.iFrameworkDemo.db.User;

import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FinalDb db = FinalDb.create(this, Environment.getExternalStorageDirectory()+"/iflytek/","qmy.db");

        User user = new User();
        user.setEmail("afinal@tsz.net");
        user.setName("探索者");
        user.setRegisterDate(new Date());

        db.save(user);

        List<User> userList = db.findAll(User.class);//查询所有的用户

        Logger.e("AfinalOrmDemoActivity", "用户数量："+ (userList!=null?userList.size():0));
    }
}
