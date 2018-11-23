package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.domain.common.BaseRequest;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.NewsDTO;
import com.um.domain.po.NewsPO;
import com.um.domain.po.SystemLogPO;
import com.um.mapper.NewsMapper;
import com.um.mapper.SystemLogMapper;
import com.um.service.NewsService;
import com.um.util.BeanUtil;
import com.um.util.DateUtil;
import com.um.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:50
 */
@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Override
    public PaginationSupportDTO<NewsDTO> queryNewsPage(BaseRequest baseRequest) {
        PaginationSupportDTO<NewsDTO> paginationSupportDTO = new PaginationSupportDTO();
        PageHelper.startPage(baseRequest.getCurrentPage(), baseRequest.getPageSize());

        Example example = new Example(NewsPO.class);
        example.selectProperties("id","title","creator","createdTime");
        Example.Criteria criteria = example.createCriteria();
        example.setOrderByClause("created_time desc");

        List<NewsPO> newsPOList = newsMapper.selectByExample(example);

        PageInfo<NewsPO> pageInfo = new PageInfo<>(newsPOList);
        paginationSupportDTO.copyProperties(pageInfo,NewsDTO.class);
        return paginationSupportDTO;
    }

    @Transactional
    @Override
    public void createNews(NewsDTO newsDTO) {

        NewsPO newsPO = BeanUtil.transformBean(newsDTO,NewsPO.class);
        newsMapper.insert(newsPO);

        //记录操作日志
        SystemLogPO systemLogPO = new SystemLogPO();
        systemLogPO.setCreatorAccountName(newsDTO.getCreator().split("_")[1]);
        systemLogPO.setCreator(newsDTO.getCreator());
        systemLogPO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
        systemLogPO.setLogContent("新增了资讯 \"" + newsPO.getTitle() + "\"");
        systemLogMapper.insertSelective(systemLogPO);
    }

    @Transactional
    @Override
    public void modifyNews(NewsDTO newsDTO) {

        NewsPO newsPO = new NewsPO();
        newsPO.setId(newsDTO.getId());
        newsPO.setTitle(newsDTO.getTitle());
        newsPO.setContent(newsDTO.getContent());
        newsPO.setModifier(newsDTO.getModifier());
        newsPO.setModifiedTime(newsDTO.getModifiedTime());

        newsMapper.updateByPrimaryKeySelective(newsPO);
    }

    @Transactional
    @Override
    public void deleteNews(Integer id,String operator) {

        NewsPO newsPO = newsMapper.selectByPrimaryKey(id);

        newsMapper.deleteByPrimaryKey(id);

        //记录操作日志
        SystemLogPO systemLogPO = new SystemLogPO();
        systemLogPO.setCreatorAccountName(operator.split("_")[1]);
        systemLogPO.setCreator(operator);
        systemLogPO.setCreatedTime(DateUtil.getCurrentDateTimeStr());
        systemLogPO.setLogContent("删除了资讯 \"" + newsPO.getTitle() + "\"");
        systemLogMapper.insertSelective(systemLogPO);
    }


    @Override
    public NewsDTO queryNewsDetail(Integer id) {
        NewsPO newsPO = newsMapper.selectByPrimaryKey(id);
        NewsDTO newsDTO = BeanUtil.transformBean(newsPO,NewsDTO.class);
        //处理返回内容中，图片的样式
        newsDTO.setContent(StringUtil.replaceImgStyleWidthHeight(newsDTO.getContent()));
        return newsDTO;
    }
}
