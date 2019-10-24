package com.ibb.common.util.pagination;

/**
 * 分页上下文环境。用于计算Page。
 * 
 * @author kin wong
 *
 * @param <E> 分页中的一条记录对象
 */
public interface IPageContext<E> {
    
    /**
     * 默认设定每页显示记录数为10
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 计算总页数.
     * 
     * @return
     */
    public int getPageCount();
    

    /**
     * 返回 Page 对象.
     * 
     * @param index
     * @return
     */
    public Page<E> getPage(int index);
    
    /**
     * 每页显示的记录数量
     * 
     * @return
     */
    public int getPageSize();
    
    /**
     * 计算总记录数
     * 
     * @return
     */
    public int getTotal();
    
}
