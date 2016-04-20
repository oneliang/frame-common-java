package com.oneliang.frame.jdbc;

import java.util.List;

import com.oneliang.Constant;
import com.oneliang.exception.MappingNotFoundException;
import com.oneliang.frame.ConfigurationFactory;
import com.oneliang.util.common.StringUtil;

/**
 * for db mapping file use,through the class can find the table column which
 * field map
 * 
 * @author Dandelion
 * @since 2008-12-29
 */
public final class DatabaseMappingUtil{

	private DatabaseMappingUtil(){}

	/**
	 * regex
	 */
	private static final String REGEX = "\\{[\\w\\.]*\\}";
	private static final String FIRST_REGEX="\\{";
	
	/**
	 * parse sql like:select * from {User}--can find the mapping file where {User.id} and so on
	 * @param sql
	 * @return the parse sql
	 * @throws Exception
	 */
	public static String parseSql(String sql) throws MappingNotFoundException{
		List<String> list = StringUtil.parseStringGroup(sql,REGEX,FIRST_REGEX,StringUtil.BLANK,1);
		if(list!=null){
			for (String string : list) {
				int pos = 0;
				if ((pos = string.lastIndexOf(Constant.Symbol.DOT)) > 0) {
					String className = string.substring(0, pos);
					String fieldName = string.substring(pos + 1, string.length());
					MappingBean mappingBean=ConfigurationFactory.findMappingBean(className);
					if(mappingBean!=null){
						String column=mappingBean.getColumn(fieldName);
						if(column!=null){
							sql = sql.replaceFirst(REGEX, column);
						} else {
							throw new MappingNotFoundException("can not find the mapping field: "+className+Constant.Symbol.DOT+fieldName);
						}
					}else{
						throw new MappingNotFoundException("can not find the mapping bean: "+className);
					}
				} else {
					String className=string;
					MappingBean mappingBean=ConfigurationFactory.findMappingBean(className);
					if(mappingBean!=null){
						String table=mappingBean.getTable();
						if(table!=null){
							sql = sql.replaceFirst(REGEX, table);
						}else{
							throw new MappingNotFoundException("can not find the mapping table of the class:" + className);
						}
					}else{
						throw new MappingNotFoundException("can not find the mapping class:" + className);
					}
				}
			}
		}
		return sql;
	}
}
