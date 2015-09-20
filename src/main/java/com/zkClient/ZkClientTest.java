package com.zkClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;

import com.github.zkclient.IZkChildListener;
import com.github.zkclient.ZkClient;


public class ZkClientTest {
	private static List<String> serverList = new ArrayList<String>();
	private static String zkServerList = "192.168.1.100:2181";
	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		ZookeeperUtils.getInstance().connect();
		ZookeeperUtils.getInstance().createNode("/zk001","zk001data".getBytes());
		//ZookeeperUtils.getInstance().deleteNode("/zk001");
		
		/*createService("service-a", "/config");
		getService("service-a", "/config");
		for (int i = 0; i < serverList.size(); i++) {
			System.out.println(serverList.get(i));
		}*/
	}
	
	/**
	 * 
	 * @Title: createService 
	 * @Description: 通过服务名称和服务路径创建服务节点
	 * @param @param serviceName
	 * @param @param servicePath
	 * @param @throws UnknownHostException
	 * @return void
	 * @throws
	 */
	public static void createService(String serviceName, String servicePath) throws UnknownHostException{
		ZkClient zkClient = new ZkClient(zkServerList);
		if(!zkClient.exists(servicePath)){//不存在，则创建路径
			zkClient.createPersistent(servicePath);
		}
		boolean serviceExist = zkClient.exists(servicePath+"/"+serviceName);
		if(!serviceExist){//不存在，则创建服务节点
			zkClient.createPersistent(servicePath+"/"+serviceName);
		}
		//获取本机IP地址
		InetAddress localHost = InetAddress.getLocalHost();
		String ip = localHost.getHostAddress();
		zkClient.createEphemeral(servicePath+"/"+serviceName+"/"+ip);
		
	}
	
	/**
	 * @Title: getService 
	 * @Description: 根据服务名称和路径获取服务地址
	 * @param @param serviceName 服务名称
	 * @param @param servicePath 服务路径
	 * @return void
	 * @throws
	 */
	public static void getService(String serviceName, String servicePath){
		servicePath = servicePath +"/"+ serviceName;
		ZkClient zkClient = new ZkClient(zkServerList);
		if(zkClient.exists(servicePath)){
			serverList = zkClient.getChildren(servicePath);
		}
		zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
			public void handleChildChange(String path, List<String> currentChilds)
					throws Exception {
				serverList = currentChilds;
			}
		});
	} 
	
}
