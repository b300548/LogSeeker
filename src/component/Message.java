package component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 消息类
 *
 */
public class Message {
	// 时间
	private Date date;
	// 序号
	private int index;
	// 进程号
	private String pid;
	// 线程号
	private int tid;
	// 打印级别 
	private Level level;
	// 标签
	private String tag;
	// 内容
	private String content;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
		return String.valueOf(getIndex()) + " " + sdFormat.format(getDate()) + " " + String.valueOf(getPid()) + " "
				+ String.valueOf(getTid()) + " " + Message.leveltoString(getLevel()) + " " + getTag() + " " + getContent();
	}
	
	public static String leveltoString(Level level) {
		String l;
		switch (level) {
		case DETAIL:
			l = "Detail";
			break;
		case INFO:
			l = "Info";
			break;
		case TRACE:
			l = "Trace";
			break;
		case WARN:
			l = "Warn";
			break;
		case ERROR:
			l = "Error";
			break;

		default:
			l = "Detail";
			break;
		}
		
		return l;
	}
	
	public static Level stringToLevel(String l) {
		Level level;
		switch (l) {
		case "D":
			level = Level.DETAIL;
			break;
		case "I":
			level = Level.INFO;
			break;
		case "T":
			level = Level.TRACE;
			break;
		case "W":
			level = Level.WARN;
			break;
		case "E":
			level = Level.ERROR;
			break;

		default:
			level = Level.DETAIL;
			break;
		}
		return level;
	}
	
}
