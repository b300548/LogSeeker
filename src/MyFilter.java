import java.util.ArrayList;
import java.util.List;

import component.ContentFilter;
import component.DateFilter;
import component.Filter;
import component.LevelFilter;
import component.Message;
import component.TagFilter;
import component.PidFilter;

/**
 * 过滤器
 */
public class MyFilter {

	// 要显示的消息
	private List<Message> messages;
	// 进程过滤器
	private PidFilter pidFilter;
	// 打印级别过滤器
	private LevelFilter levelFilter;
	// 标签过滤器
	private TagFilter tagFilter;
	// 内容过滤器
	private ContentFilter contentFilter;
	// 日期过滤器
	private DateFilter dateFilter;
	
	/**
	 * 构造方法
	 */
	public MyFilter() {
		// 初始化
		messages = new ArrayList<Message>();
		pidFilter = new PidFilter();
		levelFilter = new LevelFilter();
		tagFilter = new TagFilter();
		contentFilter = new ContentFilter();
		dateFilter = new DateFilter();
	}
	
	
	public MyFilter(List<Message> list) {
		messages = list;
		pidFilter = new PidFilter();
		levelFilter = new LevelFilter();
		tagFilter = new TagFilter();
		contentFilter = new ContentFilter();
		dateFilter = new DateFilter();
	}
	
	/**
	 * 过滤 方法
	 * @param mList  要过滤的信息列表
	 */
	public void filter(List<Message> mList) {
		setMessages(getPidFilter().filter(mList));
		setMessages(getTagFilter().filter(getMessages()));
		setMessages(getLevelFilter().filter(getMessages()));
		
	}
	
	public static void filter(Filter filter) {
		
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public PidFilter getPidFilter() {
		return pidFilter;
	}

	public void setPidFilter(PidFilter pidFilter) {
		this.pidFilter = pidFilter;
	}

	public LevelFilter getLevelFilter() {
		return levelFilter;
	}

	public void setLevelFilter(LevelFilter levelFilter) {
		this.levelFilter = levelFilter;
	}

	public TagFilter getTagFilter() {
		return tagFilter;
	}

	public void setTagFilter(TagFilter moduleFilter) {
		this.tagFilter = moduleFilter;
	}

	public ContentFilter getContentFilter() {
		return contentFilter;
	}

	public void setContentFilter(ContentFilter contentFilter) {
		this.contentFilter = contentFilter;
	}

	public DateFilter getDateFilter() {
		return dateFilter;
	}

	public void setDateFilter(DateFilter dateFilter) {
		this.dateFilter = dateFilter;
	}
	
	
}
