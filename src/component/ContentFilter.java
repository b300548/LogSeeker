package component;

import java.util.ArrayList;
import java.util.List;


/**
 * 内容过滤器
 *
 */
public class ContentFilter implements Filter{
	// 要检索的关键词
	private String content;
	
	/**
	 * 构造方法
	 */
	public ContentFilter() {
		
	}
	
	public ContentFilter(String s) {
		content = s;
	}

	@Override
	public List<Message> filter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 内容过滤方法
	 * @param messages  过滤的信息列表
	 * @return  过滤后的信息列表
	 */
	public List<Message> filter(List<Message> messages){
		ArrayList<Message> mList = new ArrayList<Message>();
		
		// 遍历信息列表
		for (Message message : messages) {
			// 如果包含关键词
			if (message.getContent().toUpperCase().contains(content.toUpperCase()) || message.getTag().toUpperCase().contains(content.toUpperCase())) {
				mList.add(message);
			}
		}
		return mList;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
}
