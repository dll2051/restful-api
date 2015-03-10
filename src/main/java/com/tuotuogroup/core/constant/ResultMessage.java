/**
 * BaseRestfulServiceController.java
 * 2015 ©版权所有 万达信息股份有限公司
 */
package com.tuotuogroup.core.constant;
/**
 * 
 * <P>
 * <li>描述:接口返回结果信息常量</li>
 * <li>创建人： $Author: hutao $</li>
 * <li>创建时间: 2015年1月14日</li>
 * <li>最后修改人: $Author: hutao $</li>
 * <li>最后修改时间: $Date: 2015-01-19 10:01:40 +0800 (周一, 19 一月 2015) $</li>
 * <li>修改备注:</LI>
 * <li>SVN版本: $Revision: 1689 $</li>
 * 
 * @version 1.0
 *
 */
public interface ResultMessage {
	/**
	 * 成功
	 */
	public static final String SUCCESS="成功";
	/**
	 * 失败
	 */
	public static final String FAIL="失败";
	/**
	 * 无法通过权限验证
	 */
	public static final String AUTH_FAIL="无法通过权限验证";

}
