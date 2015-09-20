package com.zkClient;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**   
 * @Title: ZkLoadBalance.java 
 * @Package com.zkClient 
 * @Description: TODO 
 * @author 陈凯 chenkai-ds@petrochina.com.cn   
 * @date 2014-12-13 下午11:28:34 
 * @version V1.0   
 */

public class ZkLoadBalance {

	static Map<String, Integer> map = new HashMap<String, Integer>();
    public static void mian() {
    	map.put("192.168.1.200", 5);
    	map.put("192.168.1.201", 3);
    	map.put("192.168.1.202", 2);
    	map.put("192.168.1.203", 4);
    	map.put("192.168.1.204", 1);
    	testRound();
    	testRandom();
    	testWeight();
    	testHash("192.168.1.104");
    	testWeightRandom();
	}
	
	/**轮询法**/
	public static String testRound(){
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		serverMap.putAll(map);
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> ipList = new ArrayList<String>();
		ipList.addAll(keySet);
		String server = null;
		Integer pos = 0;
		synchronized (pos) {
			if(pos>=keySet.size()){
				pos=0;
			}
			server = ipList.get(pos);
			pos++;
		}
		return server;
	}
	
	
	/**随机法**/
	public static String testRandom(){
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		serverMap.putAll(map);
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> ipList = new ArrayList<String>();
		ipList.addAll(keySet);
	
		Random random = new Random();
		int pos = random.nextInt(ipList.size());
		return ipList.get(pos);
		
	}
	
	/**加权轮询**/
	public static String testWeight(){
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		serverMap.putAll(map);
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> ipList = new ArrayList<String>();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			Integer weight = serverMap.get(iterator.next());
			for (int i = 0; i < weight; i++) {
				ipList.add(iterator.next());
			}
		}
		
		String server = null;
		Integer index=0;
		synchronized (index) {
			if(index>=keySet.size()){
				index=0;
			}
			server = ipList.get(index);
			index++;
		}
		return server;
	}
	
	/**源地址ip hash**/
	public static String testHash(String ip){
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		serverMap.putAll(map);
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> ipList = new ArrayList<String>();
		ipList.addAll(keySet);

		int hashCode = ip.hashCode();
		int size = ipList.size();
		int pos = hashCode%size;
		return ipList.get(pos);
	}
	
	
	/**加权随机**/
	public static String testWeightRandom(){
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		serverMap.putAll(map);
		Set<String> keySet = serverMap.keySet();
		ArrayList<String> ipList = new ArrayList<String>();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			Integer weight = serverMap.get(iterator.next());
			for (int i = 0; i < weight; i++) {
				ipList.add(iterator.next());
			}
		}
		
		Random random = new Random();
		int index = random.nextInt(ipList.size());
		return ipList.get(index);
	}
}
