import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import com.eltima.components.ui.DatePicker;
import component.CheckBoxPanel;
import component.Level;
import component.Message;
import javax.swing.JTextField;
import java.awt.Font;

public class Main {
	/**
	 * 控件
	 */
	private LogSeeker logSeeker = new LogSeeker();
	private JFrame mainWindow;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JSplitPane mainPane;
	private JSplitPane leftPane;
	private JSplitPane rightPane;
	private CheckBoxPanel pidPanel;
	private CheckBoxPanel tagPanel;
	private DatePicker startDatePicker ;
	private DatePicker endDatePicker;
	private JTextArea contenTextArea;
	private JSplitPane rightBottomPane;
	private JTable table;
	private DefaultTableModel model;
	
	/**
	 * 多选监听器
	 */
	private PidItemListener pidItemListener = new PidItemListener();
	private TagItemListener tagItemListener = new TagItemListener();
	
	/**
	 * 打开的文件
	 */
	private File mFile;
	
	/**
	 * 构造方法
	 */
	public Main() {
		initWindow();
		initMenu();
		
		// 重新绘制窗口
		mainWindow.revalidate();
	}
	
	/**
	 * main方法
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
	
	/**
	 * 初始化界面
	 */
	private void initWindow() {
		// 主窗口
		mainWindow = new JFrame(); // 创建主窗口
		mainWindow.setSize(1280,720); // 设置主窗口大尺寸
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		mainWindow.setTitle("LogSeeker");  // 设置标题
		
		// 窗口大小改变监听
		mainWindow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				// 重新设置窗口主pane的大小
				mainPane.setSize(mainWindow.getWidth()-20, mainWindow.getHeight());
				mainPane.setDividerLocation(mainWindow.getWidth()/6);
//				leftPane.setSize(mainWindow.getWidth()/6,mainWindow.getHeight());
				//rightPane.setSize((mainWindow.getHeight()*5)/6, mainWindow.getHeight());
//				leftPane.setDividerLocation(mainPane.getHeight()/2);
//				rightPane.setDividerLocation(rightPane.getHeight()/6);
//				rightBottomPane.setDividerLocation((int)(4*rightBottomPane.getHeight()/5));
//				mainWindow.revalidate();
			}
		});
		
		// 添加主分割页面
		mainPane = new JSplitPane();
		mainPane.setDividerSize(2);
		mainWindow.getContentPane().add(mainPane,BorderLayout.CENTER);
		mainPane.setDividerLocation(mainWindow.getWidth()/6);
		
		// 添加左右分割页面
		leftPane = new JSplitPane();
		leftPane.setOrientation(JSplitPane.VERTICAL_SPLIT);  // 纵向分割
		leftPane.setDividerSize(2);  // 分割线大小
		mainPane.setLeftComponent(leftPane);
		rightPane = new JSplitPane();
		rightPane.setDividerSize(2);
		rightPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainPane.setRightComponent(rightPane);
		
		
		// pid框
		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setLayout(new BoxLayout(leftTopPanel, BoxLayout.Y_AXIS));  // 纵向排列
		JLabel pidLabel = new JLabel("PID:");
		JCheckBox allPidCheckBox = new JCheckBox("ALL",true);
		// 添加pid全选框监听器
		allPidCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				JCheckBox cBox = (JCheckBox)e.getItem();
				// 判断多选框状态
				if (cBox.isSelected()) {
					// 遍历多选框
					for (JCheckBox checkBox : pidPanel.getCheckBoxes()) {
						checkBox.removeItemListener(pidItemListener);
						checkBox.setSelected(true);  // 将选项框设为已选
						checkBox.addItemListener(pidItemListener);
					}
					// 将pid map的值全部设为true
					for (String pid : logSeeker.getFilter().getPidFilter().getPids().keySet()) {
						logSeeker.getFilter().getPidFilter().getPids().replace(pid, true);
					}
					
					// 过滤
					logSeeker.getFilter().filter(logSeeker.getMessages());
					// 显示
					showLog(logSeeker.getFilter().getMessages());
				}else {
					// 遍历多选框
					for (JCheckBox checkBox : pidPanel.getCheckBoxes()) {
						checkBox.removeItemListener(pidItemListener);
						checkBox.setSelected(false);  // 将选项框设为未选
						checkBox.addItemListener(pidItemListener);
					}
					// 将pid map 的值全部设为false
					for (String pid : logSeeker.getFilter().getPidFilter().getPids().keySet()) {
						logSeeker.getFilter().getPidFilter().getPids().replace(pid, false);
					}
					// 显示空的消息列表 （与通过过滤后的效果一样，但不需要经过过滤的循环操作，提高性能）
					//logSeeker.getFilter().filter(new ArrayList<Message>());
					showLog(new ArrayList<Message>());
				}
				
				
			}
		});
		pidPanel = new CheckBoxPanel();
		JScrollPane pidScrollPane = new JScrollPane(pidPanel);
		pidScrollPane.setViewportView(pidPanel);
		leftTopPanel.add(pidLabel);
		leftTopPanel.add(allPidCheckBox);
		leftTopPanel.add(pidScrollPane);
		leftPane.setLeftComponent(leftTopPanel);
		
		// tag框
		JPanel leftBottomPanel = new JPanel();
		leftBottomPanel.setLayout(new BoxLayout(leftBottomPanel,BoxLayout.Y_AXIS));
		JLabel tagLabel = new JLabel("Tags:");
		JCheckBox allTagCheckBox = new JCheckBox("ALL",true);
		// 添加tag全选框监听器
		allTagCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				JCheckBox cBox = (JCheckBox)e.getItem();
				// 判断多选框状态
				if (cBox.isSelected()) {
					// 遍历多选框
					for (JCheckBox checkBox : tagPanel.getCheckBoxes()) {
						checkBox.removeItemListener(tagItemListener);
						checkBox.setSelected(true);  // 将多选框设为已选
						checkBox.addItemListener(tagItemListener);
					}
					// 将 tag map 的值全部设为true
					for (String tag : logSeeker.getFilter().getTagFilter().getTags().keySet()) {
						logSeeker.getFilter().getTagFilter().getTags().replace(tag, true);
					}
					// 过滤
					logSeeker.getFilter().filter(logSeeker.getMessages());
					// 显示
					showLog(logSeeker.getFilter().getMessages());
				}else {
					// 遍历多选框
					for (JCheckBox checkBox : tagPanel.getCheckBoxes()) {
						checkBox.removeItemListener(tagItemListener);
						checkBox.setSelected(false);  //  将多选框设为未选
						checkBox.addItemListener(tagItemListener);
					}
					// 将 tag mao 的值全部设为false
					for (String tag : logSeeker.getFilter().getTagFilter().getTags().keySet()) {
						logSeeker.getFilter().getTagFilter().getTags().replace(tag, false);
					}
					// 显示空的消息列表 （与通过过滤后的效果一样，但不需要经过过滤的循环操作，提高性能）
					//logSeeker.getFilter().filter(new ArrayList<Message>());
					showLog(new ArrayList<Message>());
				}
				
			}
		});
		tagPanel = new CheckBoxPanel();
		JScrollPane moduleScrollPane = new JScrollPane(tagPanel);
		moduleScrollPane.setViewportView(tagPanel);
		moduleScrollPane.setVisible(true);
		leftBottomPanel.add(tagLabel);
		leftBottomPanel.add(allTagCheckBox);
		leftBottomPanel.add(moduleScrollPane);
		leftPane.setRightComponent(leftBottomPanel);
		
		
		JPanel rightTopPanel = new JPanel();
		rightTopPanel.setLayout(new BorderLayout());
		
		// 日志级别
		JPanel levelPanel = new JPanel();
		JLabel levelLabel = new JLabel("日志级别：");
		JCheckBox dCheckBox = new JCheckBox("D",true);
		JCheckBox iCheckBox = new JCheckBox("I",true);
		JCheckBox tCheckBox = new JCheckBox("T",true);
		JCheckBox wCheckBox = new JCheckBox("W",true);
		JCheckBox eCheckBox = new JCheckBox("E",true);
		// 给每个多选框都添加监听器
		dCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				JCheckBox cBox = (JCheckBox)e.getItem();
				// 判断多选框状态
				if (cBox.isSelected()) {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.DETAIL, true);
				}else {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.DETAIL, false);
				}
				
				// 过滤
				logSeeker.getFilter().filter(logSeeker.getMessages());
				
				// 显示
				showLog(logSeeker.getFilter().getMessages());
//				System.out.println("-----------");
//				for (Message message : logSeeker.getFilter().getMessages()) {
//					System.out.println(message.toString());
//				}
			}
		});
		iCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				JCheckBox cBox = (JCheckBox)e.getItem();
				if (cBox.isSelected()) {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.INFO, true);
				}else {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.INFO, false);
				}
				
				logSeeker.getFilter().filter(logSeeker.getMessages());
				
				showLog(logSeeker.getFilter().getMessages());
//				System.out.println("-----------");
//				for (Message message : logSeeker.getFilter().getMessages()) {
//					System.out.println(message.toString());
//				}
			}
		});
		tCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				JCheckBox cBox = (JCheckBox)e.getItem();
				if (cBox.isSelected()) {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.TRACE, true);
				}else {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.TRACE, false);
				}
				
				logSeeker.getFilter().filter(logSeeker.getMessages());
				
				showLog(logSeeker.getFilter().getMessages());
//				System.out.println("-----------");
//				for (Message message : logSeeker.getFilter().getMessages()) {
//					System.out.println(message.toString());
//				}
			}
		});
		wCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				JCheckBox cBox = (JCheckBox)e.getItem();
				if (cBox.isSelected()) {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.WARN, true);
				}else {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.WARN, false);
				}
				
				logSeeker.getFilter().filter(logSeeker.getMessages());
				
				showLog(logSeeker.getFilter().getMessages());
//				System.out.println("-----------");
//				for (Message message : logSeeker.getFilter().getMessages()) {
//					System.out.println(message.toString());
//				}
			}
		});
		eCheckBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				JCheckBox cBox = (JCheckBox)e.getItem();
				if (cBox.isSelected()) {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.ERROR, true);
				}else {
					logSeeker.getFilter().getLevelFilter().getLevels().replace(Level.ERROR, false);
				}
				
				logSeeker.getFilter().filter(logSeeker.getMessages());
				
				showLog(logSeeker.getFilter().getMessages());
//				System.out.println("-----------");
//				for (Message message : logSeeker.getFilter().getMessages()) {
//					System.out.println(message.toString());
//				}
			}
		});
		levelPanel.add(levelLabel);
		levelPanel.add(dCheckBox);
		levelPanel.add(iCheckBox);
		levelPanel.add(tCheckBox);
		levelPanel.add(wCheckBox);
		levelPanel.add(eCheckBox);
		rightTopPanel.add(levelPanel,BorderLayout.NORTH);
		rightPane.setLeftComponent(rightTopPanel);
		
		// 时间选择
		JPanel timePanel = new JPanel();
		JLabel startLabel = new JLabel("开始时间: ");
		JLabel endLabel = new JLabel("结束时间: ");
		// 格式
        String DefaultFormat = "yyyy-MM-dd HH:mm:ss";
        // 当前时间
        Date date = new Date();
        // 字体
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        Dimension dimension = new Dimension(177, 24);
        // 初始化时间选择器控件
        startDatePicker = new DatePicker(date, DefaultFormat, font, dimension); 
        // 可以选择时间
        startDatePicker.setTimePanleVisible(true);
        endDatePicker = new DatePicker(date, DefaultFormat, font, dimension);
        endDatePicker.setTimePanleVisible(true);
        JButton timeSerchButton = new JButton("查询");
        // 查询按钮监听器
        timeSerchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取控件的日期
				Date startDate = (Date)startDatePicker.getValue();
				Date endDate = (Date)endDatePicker.getValue();
				// 判断日期大小
				if (startDate.before(endDate)) {
					// 设置过滤器的日期
					logSeeker.getFilter().getDateFilter().setStartDate(startDate);
					logSeeker.getFilter().getDateFilter().setEndDate(endDate);
					
					// 过滤并显示
					showLog(logSeeker.getFilter().getDateFilter().filter(logSeeker.getFilter().getMessages()));
				}
				
			}
		});
        // 重置按钮
        JButton resetButton = new JButton("重置");
        // 重置按钮监听器
        resetButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// 重置过滤器的日期
				Date date = new Date();
				logSeeker.getFilter().getDateFilter().setStartDate(date);
				logSeeker.getFilter().getDateFilter().setEndDate(date);
				
				// 显示信息
				showLog(logSeeker.getFilter().getMessages());
				
			}
		});
        timePanel.add(startLabel);
        timePanel.add(startDatePicker);
        timePanel.add(endLabel);
        timePanel.add(endDatePicker);
        timePanel.add(timeSerchButton);
        timePanel.add(resetButton);
        rightTopPanel.add(timePanel,BorderLayout.CENTER);
        
        // 文字过滤
        JPanel filterPanel = new JPanel();
        JLabel filterLabel = new JLabel("内容筛选: ");
        JTextField filterField = new JTextField(35);
        filterField.setFont(new Font("楷体", Font.PLAIN, 20));
        JButton filterButton = new JButton("搜索");
        // 搜索按钮监听器
        filterButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String content = filterField.getText();
				if (!content.equals("")) {
					logSeeker.getFilter().getContentFilter().setContent(content);
					showLog(logSeeker.getFilter().getContentFilter().filter(logSeeker.getFilter().getMessages()));
				}
				
			}
		});
        JButton clearButton = new JButton("清空");
        clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				filterField.setText("");
				showLog(logSeeker.getFilter().getMessages());
			}
		});
        filterPanel.add(filterLabel);
        filterPanel.add(filterField);
        filterPanel.add(filterButton);
        filterPanel.add(clearButton);
        filterField.setSize(50, 30);
        rightTopPanel.add(filterPanel,BorderLayout.SOUTH);
        
        mainWindow.setVisible(true);
        
        // 右下方内容显示
        rightBottomPane = new JSplitPane();
        rightBottomPane.setDividerSize(2);
        rightBottomPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        rightPane.setRightComponent(rightBottomPane);
        
        
        // 日志显示
        JScrollPane logScrollPane = new JScrollPane();

        table = new JTable() {

			@Override
        	public boolean isCellEditable(int row, int column) {
        		// TODO Auto-generated method stub
        		return false;
        	}
        };
        // 添加鼠标点击监听器
        table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		//在下方的TextArea中显示内容
        		int row = ((JTable)e.getSource()).rowAtPoint(e.getPoint());
        		contenTextArea.setText((String)table.getModel().getValueAt(row, 6));
        	}
		});
        // 设置表格头
        model = new DefaultTableModel();
        model.setDataVector(null, new Object[] {"index","time","pid","tid","level","tag","content"});
        table.setModel(model);
        table.setRowHeight(25);  // 设置表格行高
        table.setFont(new Font("楷体", Font.PLAIN, 20));
        // 设置表格列宽
        setColumnSize(table, 0, 50, 100, 50);
        setColumnSize(table, 1, 100, 200, 100);
        setColumnSize(table, 2, 50, 100, 50);
        setColumnSize(table, 3, 50, 100, 50);
        setColumnSize(table, 4, 50, 100, 50);
        setColumnSize(table, 5, 100, 200, 100);
        logScrollPane.setViewportView(table);
       
        //rightBottomPane.setResizeWeight(0.8);
        rightBottomPane.setLeftComponent(logScrollPane);
        mainWindow.revalidate();
        // 日志内容显示
        contenTextArea = new JTextArea(5, 100);
        contenTextArea.setEnabled(true);
        contenTextArea.setLineWrap(true);
        contenTextArea.setWrapStyleWord(true);
        contenTextArea.setText("日志内容显示");
        contenTextArea.setFont(new Font("楷体",Font.BOLD,24));
        JScrollPane contentScrollPane = new JScrollPane(contenTextArea);
        rightBottomPane.setRightComponent(contentScrollPane);
 
		
		leftPane.setDividerLocation(leftPane.getHeight()/2);
		rightPane.setDividerLocation(rightPane.getHeight()/6);
		rightBottomPane.setDividerLocation((int)(4*rightBottomPane.getHeight()/5));
	}
	
	/**
	 * 初始化菜单栏
	 */
	private void initMenu() {
		menuBar = new JMenuBar();
		mainWindow.setJMenuBar(menuBar);
		
		// 文件菜单
		menuFile = new JMenu();
		menuFile.setText("文件");
		menuBar.add(menuFile);
		
		// 打开文件菜单项
		JMenuItem mi_open = new JMenuItem();
		mi_open.setText("打开");
		// 添加点击监听器
		mi_open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();  // 打开文件
				
			}
		});
		menuFile.add(mi_open);
		
		// 导出文件菜单项
		JMenuItem mi_export = new JMenuItem();
		mi_export.setText("导出");
		// 添加点击监听器
		mi_export.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				exportFile();  // 导出文件
			}
		});
		menuFile.add(mi_export);
		
		// 退出菜单项
		JMenuItem mi_exit = new JMenuItem();
		mi_exit.setText("退出");
		// 添加点击监听器
		mi_exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);  // 退出程序
			}
		});
		menuFile.add(mi_exit);
		mainWindow.getContentPane().setLayout(new BorderLayout(0, 0));
		

	}
	
	
	/**
	 *  打开文件
	 */
	private void openFile() {
		JFileChooser fileChooser = new JFileChooser(logSeeker.getPath()); // 创建文件选择对话框
		int returnValue = fileChooser.showOpenDialog(mainWindow.getContentPane());  // 打开文件选择对话框
		if (returnValue == JFileChooser.APPROVE_OPTION) {  // 判断用户是否选择了文件
			mFile = fileChooser.getSelectedFile();  // 获得文件对象
			logSeeker.setPath(mFile.getParentFile().getAbsolutePath());  // 设置打开的文件的路径
			logSeeker.setMessages(logSeeker.getFileParse().parse(mFile));  // 设置全部的信息
			logSeeker.getFilter().setMessages(logSeeker.cloneMessages());  // 将全部信息复制到要显示的信息列表中
			
			// 显示信息
			showLog(logSeeker.getMessages());
			// 显示pid多选框
			showPids();
			// 显示tag多选框
			showTags();
		}
	}
	
	/**
	 * 导出文件
	 */
	private void exportFile() {
		// 文件选择器对话框
		JFileChooser fileChooser = new JFileChooser(logSeeker.getPath());
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("文本文件(*.txt)", "txt");
		fileChooser.setFileFilter(fileNameExtensionFilter);
		
		int returnValue = fileChooser.showSaveDialog(mainWindow.getContentPane()); // 打开文件保存对话框
		// 判断是否点击了保存
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File saveFile = fileChooser.getSelectedFile();
			
			String fileName = fileChooser.getName(saveFile);
			
			if (fileName.indexOf(".txt") == -1) {
				saveFile = new File(fileChooser.getCurrentDirectory(),fileName+".txt");
			}
			
			try {
				PrintWriter output = new PrintWriter(saveFile);
				
				for (int i=0;i<table.getRowCount();i++) {
					output.print(table.getValueAt(i, 1) + "   ");
					output.print(table.getValueAt(i, 2) + "   ");
					output.print(table.getValueAt(i, 3) + " ");
					output.print(table.getValueAt(i, 4) + " ");
					output.print(table.getValueAt(i, 5) + ": ");
					output.println(table.getValueAt(i, 6));
					
				}
				
				output.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *  显示日志
	 * @param list  要显示的信息列表
	 */
	private void showLog(List<Message> list) {

		model.setRowCount(0); // 情况信息表格数据
		SimpleDateFormat sdFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
		// 遍历消息列表
		for (Message message : list) {
			// 判断pid的值
			if (!message.getPid().equals("0")) {
				ArrayList<Object> msgList = new ArrayList<Object>();
				msgList.add(message.getIndex());
				msgList.add(sdFormat.format(message.getDate()));
				msgList.add(message.getPid());
				msgList.add(message.getTid());
				msgList.add(Message.leveltoString(message.getLevel()));
				msgList.add(message.getTag());
				msgList.add(message.getContent());
				model.addRow(msgList.toArray());
			}else {
				ArrayList<Object> msgList = new ArrayList<Object>();
				msgList.add(message.getIndex());
				msgList.add("------");
				msgList.add("---");
				msgList.add("---");
				msgList.add("---");
				msgList.add("------");
				msgList.add(message.getContent());
				model.addRow(msgList.toArray());
			}
			table.setModel(model);
		}
	}
	
	/**
	 *  显示pid checkbox
	 */
	private void showPids() {
		// 清空多选框
		pidPanel.removeAll();
		// 重新添加多选框
		for (String pid : logSeeker.getFilter().getPidFilter().getPids().keySet()) {
			addCheckBox(pidPanel,pid,pidItemListener);
		}
	}
	
	/**
	 *  显示tag checkbox
	 */
	private void showTags() {
		// 清空多选框
		tagPanel.removeAll();
		// 重新添加多选框
		for (String string : logSeeker.getFilter().getTagFilter().getTags().keySet()) {
			addCheckBox(tagPanel,string, tagItemListener);
		}
	}
	
	/**
	 *  添加多选框
	 * @param panel  将多选框添加到哪个CheckBoxPanel
	 * @param name  CheckBox的名字
	 * @param itemListener  CheckBox的监听器
	 */
	private void addCheckBox(CheckBoxPanel panel,String name,ItemListener itemListener) {
		JCheckBox checkBox = new JCheckBox(name,true);
		checkBox.setFont(new Font("楷体", Font.BOLD, 16));
        checkBox.setBackground(Color.white);
        // 添加多选框监听器
        checkBox.addItemListener(itemListener);
		panel.addNewCheckBox(checkBox);
	}
	
	/**
	 *pid 多选框监听器
	 *
	 */
	class PidItemListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			JCheckBox cBox = (JCheckBox)e.getItem();
			if (cBox.isSelected()) {
				logSeeker.getFilter().getPidFilter().getPids().replace(cBox.getText(), true);
			}else {
				logSeeker.getFilter().getPidFilter().getPids().replace(cBox.getText(), false);
			}
			
			logSeeker.getFilter().filter(logSeeker.getMessages());
			
			showLog(logSeeker.getFilter().getMessages());
			//System.out.println("-----------");
//			for (Message message : logSeeker.getFilter().getMessages()) {
//				System.out.println(message.toString());
//			}
		}
	}
	
	/**
	 * tag 多选框监听器
	 *
	 */
	class TagItemListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			JCheckBox cBox = (JCheckBox)e.getItem();
			if (cBox.isSelected()) {
				logSeeker.getFilter().getTagFilter().getTags().replace(cBox.getText(), true);
			}else {
				logSeeker.getFilter().getTagFilter().getTags().replace(cBox.getText(), false);
			}
			
			logSeeker.getFilter().filter(logSeeker.getMessages());
			
			showLog(logSeeker.getFilter().getMessages());
//			System.out.println("-----------");
//			for (Message message : logSeeker.getFilter().getMessages()) {
//				System.out.println(message.toString());
//			}
		}
		
	}
	

	/**
	 * 设置某一列的宽度
	 * @param table  信息表格
	 * @param i  第几列 
	 * @param preferedWidth  首选宽度
	 * @param maxWidth  最大宽度
	 * @param minWidth  最小宽度
	 */
	public static void setColumnSize(JTable table, int i, int preferedWidth, int maxWidth, int minWidth){
		//表格的列模型
		TableColumnModel cm = table.getColumnModel();
		//得到第i个列对象 
		TableColumn column = cm.getColumn(i);  
		column.setPreferredWidth(preferedWidth);
		column.setMaxWidth(maxWidth);
		column.setMinWidth(minWidth);
		}

}
