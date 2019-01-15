package component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 标签过滤器
 */
public class TagFilter implements Filter{

	// 标签map
	private TreeMap<String, Boolean> tags;
	
	public TagFilter() {
		tags = new TreeMap<String, Boolean>();
	}
	
	public TagFilter(Map<String, Boolean> map) {
		tags = (TreeMap<String, Boolean>) map;
	}
	
	@Override
	public List<Message> filter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 标签过滤方法
	 * @param messages  要过滤的列表
	 * @return  过滤后的列表
	 */
	public List<Message> filter(List<Message> messages){
        ArrayList<Message> mList = new ArrayList<Message>();
		
		for (Message message : messages) {
			String tag = message.getTag();
			if (tags.get(tag)) {
				mList.add(message);
			}
		}
		return mList;
	}
	
	public void setTags(Map<String, Boolean> tags) {
		this.tags = (TreeMap<String, Boolean>) tags;
	}
	
	public TreeMap<String, Boolean> getTags() {
		return tags;
	}
	
}
