package com.pepper.core.base.curd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * 
 * @author mrliu
 *
 */
public class SortBuilder {

	/**
	 * 构建排序
	 * @param sortParameter
	 * @return
	 */
	public synchronized static Sort builder(final Map<String, Object> sortParameter){
		if(sortParameter==null){
			return Sort.unsorted();
		}
		List<Order> orders = new ArrayList<Order>();
		Iterator<Entry<String, Object>> iterator = sortParameter.entrySet().iterator();
		Map.Entry<String, Object> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();
			String key = entry.getKey();
			Direction direction = null;
			if(entry.getValue() instanceof Direction ) {
				direction = (Direction) entry.getValue();
			}else {
				String value = (String) entry.getValue();
				direction = Direction.valueOf(value.toUpperCase());
			}
			Order order = new Order(direction, key);
			orders.add(order);
		}
		
		Sort sort = Sort.by(orders);
		return sort;
	}
}
