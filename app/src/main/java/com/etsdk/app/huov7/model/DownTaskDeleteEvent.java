package com.etsdk.app.huov7.model;

import com.etsdk.app.huov7.down.TasksManagerModel;

/**
 * Created by LiuHongLiang on 2016/10/9.
 */

public class DownTaskDeleteEvent {
    public TasksManagerModel tasksManagerModel;

    public DownTaskDeleteEvent(TasksManagerModel tasksManagerModel) {
        this.tasksManagerModel = tasksManagerModel;
    }
}
