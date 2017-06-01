package com.bjut.servicedog.servicedog.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple02 on 16/7/11.
 */
public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity)
    {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity)
    {
        activities.remove(activity);
    }
    public static void finishAll()
    {
        for(Activity activity:activities)
        {
            if(!activity.isFinishing())
            {
                activity.finish();
            }
        }
    }

}
