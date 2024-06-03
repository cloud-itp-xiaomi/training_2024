package com.example.mi1.common.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用分页数据封装类
 * Created by macro on 2019/4/19.
 */
public class CommonPage<T> {
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPageNum;
    /**
     * 总条数
     */
    private Integer totalContentNum;
    /**
     * 分页数据
     */
    private List<T> content;


    protected CommonPage() {
    }

    protected CommonPage(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(Integer totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Integer getTotalContentNum() {
        return totalContentNum;
    }

    public void setTotalContentNum(Integer totalContentNum) {
        this.totalContentNum = totalContentNum;
    }

    public static <T> CommonPage<T> page(Integer currentPage, Integer pageSize, List<T> data) {
        CommonPage<T> pageData = new CommonPage<>(currentPage, pageSize);
        // 用 stream 方式转换
        // List<T> content = data.stream().skip((long) (currentPage - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        int fromIndex = Math.min(Math.max(data.size() - pageSize, 0), (currentPage - 1) * pageSize);//防止出现 fromIndex 大于 doc 的 size 这种情况返回最后一页
        int toIndex = Math.min(data.size(), fromIndex + pageSize);
        List<T> content = data.subList(fromIndex, toIndex);
        // 设置总页数
        pageData.setTotalPageNum((int) Math.ceil((double) content.size() / pageSize));
        // 设置总条数
        pageData.setTotalContentNum(content.size());
        // 数据
        pageData.setContent(content);
        return pageData;
    }

    public Map<String, Object> format( Map<PagePara, String> customParaMap) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (customParaMap.containsKey(PagePara.currentPage)) {
            result.put(customParaMap.get(PagePara.currentPage), this.getCurrentPage());
        }
        if (customParaMap.containsKey(PagePara.pageSize)) {
            result.put(customParaMap.get(PagePara.pageSize), this.getPageSize());
        }
        if (customParaMap.containsKey(PagePara.totalPageNum)) {
            result.put(customParaMap.get(PagePara.totalPageNum), this.getTotalPageNum());
        }
        if (customParaMap.containsKey(PagePara.totalContentNum)) {
            result.put(customParaMap.get(PagePara.totalContentNum), this.getTotalContentNum());
        }
        if (customParaMap.containsKey(PagePara.content)) {
            result.put(customParaMap.get(PagePara.content), this.getContent());
        }
        return result;
    }

    /**
     * 分页使用的参数
     */
    public enum PagePara {
        currentPage("当前页", "current_page", "currentPage"),
        pageSize("每页数量", "page_size", "pageSize"),
        totalPageNum("总页数", "total_page_num", "totalPageNum"),
        totalContentNum("总条数", "total_content_num", "totalContentNum"),
        content("分页后的数据", "content", "content");

        private final String description;
        private final String responseKey;
        private final String fieldName;

        /**
         * @param description 描述信息
         * @param responseKey 返回给前端时序列化后的 key
         * @param fieldName   CommonPage 中使用的 field 名字
         */
        PagePara(String description, String responseKey, String fieldName) {
            this.description = description;
            this.responseKey = responseKey;
            this.fieldName = fieldName;
        }

        public String getDescription() {
            return description;
        }

        public String getResponseKey() {
            return responseKey;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

}
