package com.spider.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author calf
 * 定时任务抽象类
 */
public abstract class AbstractTask {
    public final static Log log = LogFactory.getLog(AbstractTask.class);
    
    public abstract void runTask();
}
