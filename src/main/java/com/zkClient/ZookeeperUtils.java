package com.zkClient;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperUtils {

	private static Logger logger = LoggerFactory.getLogger(ZookeeperUtils.class);  
	private static ZooKeeper zooKeeper = null;
	private static int sessionTimeout = 30000;
	private static String connectString = "192.168.1.100:2181";
	private static ZookeeperUtils instance= null;
	private static ReadWriteLock rwl = new ReentrantReadWriteLock();
	
	private ZookeeperUtils(){}
	public static ZookeeperUtils getInstance() throws IOException{
		if(instance==null){
			instance = new ZookeeperUtils();
		}
		return instance;
	}
	public static void connect() throws IOException{
		Lock readLock = rwl.readLock();
		try {
			readLock.lock();
			if (zooKeeper == null) {
				Lock writeLock = rwl.writeLock();
				readLock.unlock();
				try {
					writeLock.lock();
					if (zooKeeper == null) {
						zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
							public void process(WatchedEvent event) {
								if(logger.isDebugEnabled()){
									logger.debug("process :{}", event.getType());
								}
							}
						});
					}
				} finally {
					writeLock.unlock();
				}
				readLock.lock();
			}
		} finally {
			readLock.unlock();
		}
	}
	
	public void close() throws InterruptedException {
		if(zooKeeper!=null){
			zooKeeper.close();
		}
	}
	
	public String createNode(String path,byte[] data) throws KeeperException, InterruptedException{
		String result = zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		return result;
	}
	
	public void deleteNode(String path) throws InterruptedException, KeeperException{
		zooKeeper.delete(path, -1);
	}
	
	public String getData(String path) throws KeeperException, InterruptedException{
		byte[] data = zooKeeper.getData(path, null, null);
		return new String(data);
	}
	
	public void setData(String path,byte[] data) throws KeeperException, InterruptedException{
		Stat stat = zooKeeper.setData(path, data, -1);
		if(logger.isDebugEnabled()){
			logger.debug("set data result :{}",stat.getVersion());
		}
	}
	
	
}
