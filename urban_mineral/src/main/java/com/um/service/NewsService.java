package com.um.service;

import com.um.domain.common.BaseRequest;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.NewsDTO;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:50
 */
public interface NewsService {


    public PaginationSupportDTO<NewsDTO> queryNewsPage(BaseRequest baseRequest);

    public void createNews(NewsDTO newsDTO);

    public void modifyNews(NewsDTO newsDTO);

    public void deleteNews(Integer id,String operator);

    public NewsDTO queryNewsDetail(Integer id);
}
