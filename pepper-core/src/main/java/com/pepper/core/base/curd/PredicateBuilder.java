package com.pepper.core.base.curd;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.pepper.core.IEnum;
import com.pepper.core.constant.SearchConstant;

/**
 * 
 * @author mrliu
 *
 */
public class PredicateBuilder {

	private static Logger log = LoggerFactory.getLogger(PredicateBuilder.class);
	/**
	 * 构建查询条件
	 * @param root
	 * @param criteriaBuilder
	 * @param searchParameter
	 * @return
	 */
	public static synchronized List<Predicate> builder(final Root<?> root,
			final CriteriaBuilder criteriaBuilder, final Map<String, Object> searchParameter) {
		List<Predicate> predicates = new ArrayList<>();
		Iterator<Entry<String, Object>> iterator = searchParameter.entrySet().iterator();
		Map.Entry<String, Object> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.hasText(key)) {
				
				String searchStr[] = key.split("_");
				if (searchStr.length < 2) {
					continue;
				}
				String searchType = searchStr[0];
				String field = searchStr[1];
				
				List<Path<?>> path = new ArrayList<Path<?>>();
				try{
					for(String str : field.split("&")) {
						path.add(root.get(str));
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				Predicate predicate = criteriaBuilder(path, searchType, value, criteriaBuilder);
				if (predicate != null) {
					predicates.add(predicate);
				}
			}
		}
		return predicates;
	}

	private static synchronized Predicate criteriaBuilder(final List<Path<?>> path, final String searchType,
			final Object value, final CriteriaBuilder criteriaBuilder) {
		if (path == null) {
			return null;
		}
		Predicate predicate = null;
		switch (searchType.toUpperCase()) {
		case SearchConstant.EQUAL:
			if (path.get(0).getJavaType().isEnum()) {
				predicate = predicateEnum(path.get(0),path.get(0).getJavaType(),value,criteriaBuilder);
			}else{
				predicate = criteriaBuilder.equal(path.get(0).as(path.get(0).getJavaType()), value);
			}
			break;
		case SearchConstant.NOTEQUAL:
			if (path.get(0).getJavaType().isEnum()) {
				predicate = predicateEnum(path.get(0),path.get(0).getJavaType(),value,criteriaBuilder);
			}else{
				predicate = criteriaBuilder.notEqual(path.get(0).as(path.get(0).getJavaType()), value);
			}
			break;
		case SearchConstant.ISNULL:
			predicate = criteriaBuilder.isNull(path.get(0).as(path.get(0).getJavaType()));
			break;
		case SearchConstant.ISNOTNULL:
			predicate = criteriaBuilder.isNotNull(path.get(0).as(path.get(0).getJavaType()));
			break;
		case SearchConstant.NOTIN:
			CriteriaBuilder.In<Object> notIn = predicateIn(path.get(0),path.get(0).getJavaType(),value,criteriaBuilder);
			predicate = criteriaBuilder.not(notIn);
			break;
		case SearchConstant.IN:
			predicate = predicateIn(path.get(0),path.get(0).getJavaType(),value,criteriaBuilder);
			break;
		case SearchConstant.LIKE:
			predicate = criteriaBuilder.like(path.get(0).as(String.class), "%" + value + "%");
			break;
		case SearchConstant.NOTLIKE:
			predicate = criteriaBuilder.notLike(path.get(0).as(String.class), "%" + value + "%");
			break;
		case SearchConstant.GE:
			predicate = predicateNumber(path.get(0), searchType, value, criteriaBuilder);
			break;
		case SearchConstant.GT:
			predicate = predicateNumber(path.get(0), searchType, value, criteriaBuilder);
			break;
		case SearchConstant.LE:
			predicate = predicateNumber(path.get(0), searchType, value, criteriaBuilder);
			break;
		case SearchConstant.LT:
			predicate = predicateNumber(path.get(0), searchType, value, criteriaBuilder);
			break;
		case SearchConstant.OR:
			List<Predicate> or = new ArrayList<Predicate>();
			for(Path<?> obj : path) {
				or.add( criteriaBuilder.or(criteriaBuilder.equal(obj.as(obj.getJavaType()), value)));
			}
			predicate = criteriaBuilder.or(or.toArray(new Predicate[or.size()]));
			break;
		case SearchConstant.ORLIKE:
			List<Predicate> orLike = new ArrayList<Predicate>();
			for(Path<?> obj : path) {
				orLike.add( criteriaBuilder.or(criteriaBuilder.like(obj.as(String.class), "%" + value + "%")));
			}
			predicate = criteriaBuilder.or(orLike.toArray(new Predicate[orLike.size()]));
			break;
		default:
			break;
		}
		return predicate;
	}
	
	@SuppressWarnings("unchecked")
	private static synchronized CriteriaBuilder.In<Object> predicateIn(final Path<?> path, final Class<?> classz, final Object value, final CriteriaBuilder criteriaBuilder){
		CriteriaBuilder.In<Object> in = criteriaBuilder.in(path.as(classz));
		if (value instanceof String) {
			for (String v : String.valueOf(value).split(",")) {
				in.value(v);
			}
		} else if (value instanceof Collection) {
			for (Object v : (Collection<Object>) value) {
				in.value(v);
			}
		}else if (value instanceof Object[]) {
			for (Object v : (Object[])value) {
				in.value(v);
			}
		}
		return in;
	}
	
	private static synchronized Predicate predicateEnum(final Path<?> path, final Class<?> classz, final Object value, final CriteriaBuilder criteriaBuilder){
		Predicate predicate = null;
		if (classz.isEnum()) {
			if(value.getClass().isEnum()){
				predicate = criteriaBuilder.equal(path.as(classz),value);
			}else if(value instanceof String){
				try {
					Method method = classz.getMethod("values");
					IEnum inter[] = (IEnum[]) method.invoke(null);
					for (IEnum ienum : inter) {
						if(ienum.toString().equals(value)){
							predicate = criteriaBuilder.equal(path.as(classz),ienum.getKey());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			predicate = criteriaBuilder.equal(path.as(classz), value);
		}
		return predicate;
	}

	@SuppressWarnings("unused")
	private static synchronized Predicate predicateNumber(final Path<?> path, final String searchType, final Object value,
			final CriteriaBuilder criteriaBuilder) {
		Predicate predicate = null;
		Number newValue = null;
		Expression<Number> expression = null;
		if (value instanceof Long) {
			newValue = Long.valueOf(value.toString());
			path.as(Long.class);
		} else if (value instanceof Integer) {
			newValue = Integer.valueOf(value.toString());
			path.as(Integer.class);
		} else if (value instanceof Double) {
			newValue = Double.valueOf(value.toString());
			path.as(Double.class);
		} else if (value instanceof Float) {
			newValue = Float.valueOf(value.toString());
			path.as(Float.class);
		} else if (value instanceof BigDecimal) {
			newValue = new BigDecimal(value.toString());
			path.as(BigDecimal.class);
		}
		if (newValue != null && expression != null) {
			switch (searchType.toUpperCase()) {
			case SearchConstant.GE://大于等于 
				predicate = criteriaBuilder.ge(expression, newValue);
				break;
			case SearchConstant.GT://大于
				predicate = criteriaBuilder.gt(expression, newValue);
				break;
			case SearchConstant.LE://小于
				predicate = criteriaBuilder.le(expression, newValue);
				break;
			case SearchConstant.LT://小于等于
				predicate = criteriaBuilder.lt(expression, newValue);
				break;
			default:
				break;
			}
		}
		return predicate;
	}

}
