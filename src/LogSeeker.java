import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

import component.Level;
import component.Message;

public class LogSeeker {

	// 文件的全部信息列表
	private List<Message> messages;
	// 过滤器
	private MyFilter filter;
	// 文件解析器
	private FileParser fileParser;
	// 打开的文件的路径
	private String path = null;
	
	// 构造方法
	public LogSeeker() {
		// 初始化
		messages = new ArrayList<Message>();
		filter = new MyFilter();
		fileParser = new FileParser();
	}
	
	public MyFilter getFilter() {
		return filter;
	}
	
	public List<Message> getMessages() {
		return messages;
	}
	
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	public void setFilter(MyFilter filter) {
		this.filter = filter;
	}
	
	public void setFileParser(FileParser fileParser) {
		this.fileParser = fileParser;
	}
	
	public FileParser getFileParse() {
		return fileParser;
	}
	
	/**
	 * 文件解析器 
	 * 内部类
	 */
	class FileParser{
		/**
		 * 解析文件
		 * @param file 要解析的文件
		 * @return  解析后的信息
		 */
		public List<Message> parse(File file) {
			
//			System.out.println(file.getParentFile().getAbsolutePath());
			
			
			List<Message> messages = new ArrayList<Message>();
			
			// 文件输入
			FileReader reader;  // 声明字符流
			BufferedReader in;  // 声明字符缓冲流
			Scanner scanner = null;

			// 日期格式
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
			// 每条信息解析成 Message
			try {
				// 初始化输入流
				reader = new FileReader(file);  // 创建字符流
				in = new BufferedReader(reader); // 创建字符缓冲流
				scanner = new Scanner(in);
				// 序号
				int index = 1;
				
				TreeMap<String, Boolean> pidMap= (TreeMap<String, Boolean>) filter.getPidFilter().getPids();
				pidMap.clear();
				TreeMap<String, Boolean> tagMap = (TreeMap<String, Boolean>) filter.getTagFilter().getTags();
				tagMap.clear();
				
				// 遍历信息流
				while(scanner.hasNextLine()) {  //判断是否读到内容
					Message message = new Message();
					// 设置消息序号
					message.setIndex(index++);
					// 解析日期
					StringBuilder dateBuilder = new StringBuilder();
					dateBuilder.append("2018-");
					dateBuilder.append(scanner.next() + " ");
					dateBuilder.append(scanner.next());
					Date date = null;
					try {
						date = sdFormat.parse(dateBuilder.toString());
					} catch (ParseException e) {
						// 加入消息不符合规范
						e.printStackTrace();
						try {
							dateBuilder.append(scanner.nextLine());
						} catch (NoSuchElementException e2) {
							continue;
						}
						message.setContent(dateBuilder.toString());
						message.setPid("0");
						message.setTid(0);
						message.setLevel(Level.DETAIL);
						message.setTag("---");
						messages.add(message);
						
						if (!pidMap.containsKey(message.getPid())) {
							pidMap.put(message.getPid(), true);
							filter.getPidFilter().getPidMappings().put(message.getPid(), message.getPid());
						}
						
						if (!tagMap.containsKey(message.getTag())) {
							tagMap.put(message.getTag(), true);
						}
						continue;
					}
					message.setDate(date);
					
					// 解析消息pid
					message.setPid(scanner.next());
					// 添加到pid map
					if (!pidMap.containsKey(message.getPid())) {
						pidMap.put(message.getPid(), true);
						filter.getPidFilter().getPidMappings().put(message.getPid(), message.getPid());
						
					}
					
					// 解析消息tid
					message.setTid(Integer.parseInt(scanner.next()));
					// 解析消息打印级别
					message.setLevel(Message.stringToLevel(scanner.next()));
					
					String tag = null;
					try {
						// 解析消息标签
						tag= scanner.next();
					} catch (NoSuchElementException e) {
						// TODO: handle exception
						continue;
					}
					
					
					String[] tags = tag.split(":");
					try {
						message.setTag(tags[0]);
					} catch (ArrayIndexOutOfBoundsException e) {
						// TODO: handle exception
//						System.out.println(count++ + "----" + tags.length + "----");
						message.setTag(" ");
					}
					if (!tagMap.containsKey(message.getTag())) {
						tagMap.put(message.getTag(), true);
					}
					
					
					message.setContent(scanner.nextLine());
					
					messages.add(message);
					//System.out.println(index);
					
				}
				in.close(); // 关闭字符缓冲流
				reader.close(); //关闭字符流
			}catch (IOException e) {
				e.printStackTrace();
			}
//			for (int pid : filter.getPidFilter().getPids()) {
//				System.out.println(pid);
//			}
//			for (String tag : filter.getModuleFilter().getModules()) {
//				System.out.println(tag);
//			}
			
			// 解析进程的包名
			String filePath = file.getParentFile().getAbsolutePath();
			
			File file2 = new File(filePath + File.separator +"system.txt");
			System.out.println(file2.getAbsolutePath());
			BufferedReader input = null;
			List<String> msgs = new ArrayList<String>();
			try {
				input = new BufferedReader(new FileReader(file2));
				
				Scanner scanner2 = new Scanner(input);
				while(scanner2.hasNextLine()) {
					String pid;
					String tid;
					String level;
					String tag;
					String content;
					
					StringBuilder dateBuilder = new StringBuilder();
					dateBuilder.append("2018-");
					try {
						dateBuilder.append(scanner2.next() + " ");
						dateBuilder.append(scanner2.next());
					} catch (NoSuchElementException e) {
						// TODO: handle exception
						e.printStackTrace();
						continue;
					}
					Date date = null;
					try {
						date = sdFormat.parse(dateBuilder.toString());
					} catch (ParseException e) {
						e.printStackTrace();
						scanner2.nextLine();
						continue;
						}
					
					try {
						pid = scanner2.next();
						tid = scanner2.next();
						level = scanner2.next();
						
						tag = scanner2.next();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						continue;
					}
					
					
					content = scanner2.nextLine();
					if (!content.contains("Start proc")) {
						continue;
					}
					
					
					String string = content;	
					msgs.add(string);
					
					}
				
				scanner2.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			
			try {
				if (input != null)
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			for (String string : msgs) {
				//System.out.println(string);
				String[] strings = string.split(" ");
				String packageName = strings[3];
				String pid = null;
				for (String s : strings) {
					if(s.contains("pid")) {
						String[] pidNum = s.split("=");
						pid = pidNum[1];
					}
				}
				
				if (filter.getPidFilter().getPids().containsKey(pid)) {
					filter.getPidFilter().getPids().put(packageName + "(" + pid + ")", true);
					filter.getPidFilter().getPids().remove(pid);
					filter.getPidFilter().getPidMappings().replace(pid, packageName + "(" + pid + ")");
				}
				
				System.out.println(packageName + " " + pid);
				
			}
			
			
			return messages;
			
		}
	}
	
	/**
	 * 复制全部消息列表
	 * @return
	 */
	public List<Message> cloneMessages() {
		ArrayList<Message> mList = new ArrayList<Message>();
		for (Message message : messages) {
			mList.add(message);
		}
		
		return mList;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
}
