package life.majiang.community.community.dto;

import ch.qos.logback.core.joran.conditional.ElseAction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;//true为显示，false为不显示
    private boolean showFirstPage;
    private boolean showEndPage;
    private boolean showNext;
    private Integer page;//当前页码
    private List<Integer> pages = new ArrayList<>();//显示的页列表
    private Integer totalCount;//问题总数
    private Integer totalPage;//页面总数


    public void setPagination(Integer totalCount, Integer page, Integer size,Integer totalPage) {
        this.page=page;
        this.totalPage=totalPage;
        //往pages里赋值
        pages.add(page);
        for(int i=1;i<=3;i++){
            if(page-i>=1){
                pages.add(0,page-i);
            }

            if(page+i<=totalPage){
                pages.add(page+i);
            }
        }

        //是否是首页，以此来判断是否展示上一页
        if (page == 1)
            showPrevious = false;
        else
            showPrevious = true;
        //是否是最后一页
        if (page == totalPage)
            showNext = false;
        else
            showNext = true;
        //页列表是否包含首页
        if (pages.contains(1))
            showFirstPage = false;
        else
            showFirstPage = true;
        //页列表是否包含最后一页
        if (pages.contains(totalPage))
            showEndPage = false;
        else
            showEndPage = true;
    }
}
