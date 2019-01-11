package component;

import java.util.List;

import javax.swing.event.ListSelectionEvent;


/**
 * 过滤器接口
 *
 */
public interface Filter {

	List<Message> filter();
}
