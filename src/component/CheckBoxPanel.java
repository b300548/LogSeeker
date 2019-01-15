package component;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BoxLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;


/**
 * 自定义多选框面板
 *
 */
public class CheckBoxPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 多选框列表
	  ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
	  
	  /**
	   * 构造方法
	   */
	  public CheckBoxPanel() {
		  // 设置背景为白色
		  setBackground(Color.white);
		  // 设置垂直排列
		  setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		  }
	  
	  
	    /**
	     * 添加多选框
	     * @param checkBox  要添加的多选框
	     */
	    public void addNewCheckBox(JCheckBox checkBox) {
	    	checkBoxes.add(checkBox);
	        add(checkBox);
	        revalidate();
	      }
	    
	    public ArrayList<JCheckBox> getCheckBoxes() {
			return checkBoxes;
		}
}
