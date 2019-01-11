package component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

/**
 * 时间过滤器
 *
 */
public class DateFilter implements Filter{
	
	// 开始时间
	private Date startDate;
	// 结束时间
	private Date endDate;
	
	// 构造方法
	public DateFilter() {

		// 初始化
		startDate = new Date();
		endDate = new Date();
	}
	
	public DateFilter(Date start, Date end) {
		startDate = start;
		endDate = end;
	}

	
	@Override
	public List<Message> filter() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 时间过滤方法
	 * @param messages  要过滤的信息列表
	 * @return  过滤后的信息列表
	 */
	public List<Message> filter(List<Message> messages) {
		ArrayList<Message> mList = new ArrayList<Message>();
		// 遍历消息
		for (Message message : messages) {
			// 如果消息的时间在开始、结束时间内
			if (message.getDate().after(startDate) && message.getDate().before(endDate)) {
				mList.add(message);
			}
		}
		
		return mList;
		
		
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(Date enDate) {
		this.endDate = enDate;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
}
