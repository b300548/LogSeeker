package component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 打印级别过滤器
 *
 */
public class LevelFilter implements Filter{
	// 打印级别列表
	HashMap<Level, Boolean> levels;
	
	// 构造方法
	public LevelFilter() {
		levels = new HashMap<Level, Boolean>();
		levels.put(Level.DETAIL, true);
		levels.put(Level.INFO, true);
		levels.put(Level.TRACE,true);
		levels.put(Level.WARN, true);
		levels.put(Level.ERROR, true);
	}
	
	public LevelFilter(HashMap<Level, Boolean> map) {
		levels = map;
	}

	
	@Override
	public List<Message> filter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 打印级别过滤方法
	 * @param messages  要过滤的信息列表
	 * @return  过滤后的信息列表
	 */
	public List<Message> filter(List<Message> messages) {
		ArrayList<Message> mList = new ArrayList<Message>();
		// 遍历消息
		for (Message message : messages) {
			// 获取消息的打印级别
			Level level = message.getLevel();
			// 判断多选框是否已选
			if (levels.get(level)) {
				mList.add(message);
			}
		}
		return mList;
	}
	
	public void setLevels(HashMap<Level, Boolean> levels) {
		this.levels = levels;
	}
	
	public HashMap<Level, Boolean> getLevels() {
		return levels;
	}
}
