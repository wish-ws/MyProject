package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.RecycleOrderDTO;
import com.um.domain.po.RecycleOrderPO;
import com.um.domain.po.UserPO;
import com.um.domain.request.OrderQueryRequeset;
import com.um.mapper.RecycleOrderMapper;
import com.um.mapper.UserMapper;
import com.um.service.RecycleOrderService;
import com.um.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description : 回收订单业务服务
 * @date : 2018/11/14 20:40
 */
@Service
public class RecycleOrderServiceImpl implements RecycleOrderService {

    @Autowired
    private RecycleOrderMapper recycleOrderMapper;

    @Autowired
    private UserMapper userMapper;




    @Override
    public void createRecycleOrder(RecycleOrderDTO recycleOrderDTO) {

        RecycleOrderPO recycleOrderPO = BeanUtil.transformBean(recycleOrderDTO,RecycleOrderPO.class);

        recycleOrderMapper.insert(recycleOrderPO);

    }

    @Override
    public PaginationSupportDTO queryRecycleOrderPage(OrderQueryRequeset orderQueryRequeset) {
        PaginationSupportDTO<RecycleOrderDTO> paginationSupportDTO = new PaginationSupportDTO();
        PageHelper.startPage(orderQueryRequeset.getCurrentPage(), orderQueryRequeset.getPageSize());

        Example example = new Example(RecycleOrderPO.class);
        example.selectProperties("itemType","itemQty","orderStatus","orderCode");
        Example.Criteria criteria = example.createCriteria();

        //pt用户查所有，用户端用户查自己的
        if(null != orderQueryRequeset.getCreatorUserId()){
            criteria.andEqualTo("creatorUserId",orderQueryRequeset.getCreatorUserId());
        }

        //状态条件
        if(null != orderQueryRequeset.getOrderStatus()){
            criteria.andEqualTo("orderStatus",orderQueryRequeset.getOrderStatus());
        }

        example.setOrderByClause("created_time desc");


        List<RecycleOrderPO> recycleOrderPOList = recycleOrderMapper.selectByExample(example);

        PageInfo<RecycleOrderPO> pageInfo = new PageInfo<>(recycleOrderPOList);
        paginationSupportDTO.copyProperties(pageInfo,RecycleOrderDTO.class);
        return paginationSupportDTO;

    }

    @Override
    public RecycleOrderDTO queryRecycleOrderDetail(String orderCode) {

        RecycleOrderPO orderPOSelect = new RecycleOrderPO();
        orderPOSelect.setOrderCode(orderCode);

        RecycleOrderDTO recycleOrderDTO = BeanUtil.transformBean(recycleOrderMapper.selectOne(orderPOSelect),RecycleOrderDTO.class);

        Integer userId = recycleOrderDTO.getCreatorUserId();

        UserPO userPO = userMapper.selectByPrimaryKey(userId);

        if(null != userPO){
            recycleOrderDTO.setCreatorAccountName(userPO.getAccountName());
        }
        return recycleOrderDTO;
    }
}
