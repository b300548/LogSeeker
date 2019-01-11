package component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 标签过滤器
 */
public class TagFilter implements Filter{

	// 标签map
	private HashMap<String, Boolean> tags;
	
	public TagFilter() {
		tags = new HashMap<String, Boolean>();
	}
	
	public TagFilter(HashMap<String, Boolean> map) {
		tags = map;
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
	
	public void setTags(HashMap<String, Boolean> tags) {
		this.tags = tags;
	}
	
	public HashMap<String, Boolean> getTags() {
		return tags;
	}
	
}
