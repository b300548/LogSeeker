package component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 进程过滤器
 *
 */
public class PidFilter implements Filter {

	// 进程map
	private TreeMap<String, Boolean> pids;
	// 进程映射关系map  主要用于对应进程号 和 带包名的多选框名字
	private HashMap<String, String>  pidMappings;
		
	/**
	 * 构造方法
	 */
	public PidFilter() {
		pids = new TreeMap<String, Boolean>();
		pidMappings = new HashMap<String, String>();
	}
	
	public PidFilter(TreeMap<String, Boolean> map) {
		pids = map;
		pidMappings = new HashMap<String, String>();
	}
	
	
	@Override
	public List<Message> filter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 进程过滤方法
	 * @param messages  要过滤的信息列表
	 * @return  过滤后的信息列表
	 */
	public List<Message> filter(List<Message> messages) {
		ArrayList<Message> mList = new ArrayList<Message>();
		// 遍历信息
		for (Message message : messages) {
			
			String pid = message.getPid();
			String mappingPid = pidMappings.get(pid);
			// 判断多选框是否已选
			if (pids.get(mappingPid)) {
				mList.add(message);
			}
		}
		return mList;
	}
	
	public void setPids(Map<String, Boolean> pids) {
		this.pids = (TreeMap<String, Boolean>) pids;
	}
	
	public TreeMap<String, Boolean> getPids() {
		return pids;
	}
	
	public HashMap<String, String> getPidMappings() {
		return pidMappings;
	}
	
	public void setPidMappings(HashMap<String, String> pidMappings) {
		this.pidMappings = pidMappings;
	}

	

}
