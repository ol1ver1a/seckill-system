package com.seckill.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于ZooKeeper的分布式锁实现
 * 使用Curator框架的InterProcessMutex（可重入互斥锁）
 *
 * 原理：
 * 1. 在ZooKeeper上创建临时有序节点
 * 2. 判断自己是否为最小节点，如果是则获取锁
 * 3. 如果不是，则监听前一个节点的删除事件
 * 4. 释放锁时删除节点
 *
 * 优势：
 * - 公平锁：按请求顺序获取
 * - 可重入：同一线程可多次获取
 * - 自动释放：客户端断开连接时临时节点自动删除
 */
@Component
public class ZooKeeperDistributedLock {

    private static final Logger log = LoggerFactory.getLogger(ZooKeeperDistributedLock.class);

    private final CuratorFramework curatorFramework;
    private final ConcurrentHashMap<String, InterProcessMutex> lockMap = new ConcurrentHashMap<>();

    public ZooKeeperDistributedLock(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    /**
     * 获取分布式锁
     * @param lockPath 锁路径，如 /seckill/lock/activity/1
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 是否获取成功
     */
    public boolean acquire(String lockPath, long timeout, TimeUnit unit) {
        try {
            InterProcessMutex lock = lockMap.computeIfAbsent(lockPath, k -> new InterProcessMutex(curatorFramework, k));
            boolean acquired = lock.acquire(timeout, unit);
            if (acquired) {
                log.debug("获取ZooKeeper分布式锁成功: path={}", lockPath);
            } else {
                log.warn("获取ZooKeeper分布式锁超时: path={}", lockPath);
            }
            return acquired;
        } catch (Exception e) {
            log.error("获取ZooKeeper分布式锁异常: path={}", lockPath, e);
            return false;
        }
    }

    /**
     * 释放分布式锁
     * @param lockPath 锁路径
     */
    public void release(String lockPath) {
        try {
            InterProcessMutex lock = lockMap.get(lockPath);
            if (lock != null && lock.isAcquiredInThisProcess()) {
                lock.release();
                log.debug("释放ZooKeeper分布式锁成功: path={}", lockPath);
            }
        } catch (Exception e) {
            log.error("释放ZooKeeper分布式锁异常: path={}", lockPath, e);
        }
    }
}
