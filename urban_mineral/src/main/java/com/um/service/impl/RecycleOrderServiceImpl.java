package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.common.enums.RecycleOrderStatusEnum;
import com.um.common.exception.ServiceException;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.RecycleOrderDTO;
import com.um.domain.po.RecycleOrderPO;
import com.um.domain.po.UserPO;
import com.um.domain.request.OrderQueryRequeset;
import com.um.mapper.RecycleOrderMapper;
import com.um.mapper.UserMapper;
import com.um.service.RecycleOrderService;
import com.um.util.BeanUtil;
import com.um.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description : 回收订单业务服务
 * @date : 2018/11/14 20:40
 */
@Slf4j
@Service
public class RecycleOrderServiceImpl implements RecycleOrderService {

    @Autowired
    private RecycleOrderMapper recycleOrderMapper;

    @Autowired
    private UserMapper userMapper;



    @Transactional
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

        //订单号
        if(StringUtils.isNotEmpty(orderQueryRequeset.getOrderCode())){
            criteria.andEqualTo("orderCode",orderQueryRequeset.getOrderCode());
        }


        example.setOrderByClause("created_time desc");


        List<RecycleOrderPO> recycleOrderPOList = recycleOrderMapper.selectByExample(example);

        PageInfo<RecycleOrderPO> pageInfo = new PageInfo<>(recycleOrderPOList);
        paginationSupportDTO.copyProperties(pageInfo,RecycleOrderDTO.class);
        return paginationSupportDTO;

    }

    @Override
    public RecycleOrderDTO queryRecycleOrderDetail(String orderCode) {

        RecycleOrderPO orderPO = new RecycleOrderPO();
        orderPO.setOrderCode(orderCode);

        orderPO = recycleOrderMapper.selectOne(orderPO);
        RecycleOrderDTO recycleOrderDTO = BeanUtil.transformBean(orderPO,RecycleOrderDTO.class);

        Integer userId = recycleOrderDTO.getCreatorUserId();

        UserPO userPO = userMapper.selectByPrimaryKey(userId);

        if(null != userPO){
            recycleOrderDTO.setCreatorAccountName(userPO.getAccountName());
        }
        return recycleOrderDTO;
    }

    @Transactional
    @Override
    public String modifyRecycleOrder(RecycleOrderDTO recycleOrderDTO, Integer modifyType) {
        String msg = null;

        RecycleOrderPO orderPO = new RecycleOrderPO();
        orderPO.setOrderCode(recycleOrderDTO.getOrderCode());
        orderPO = recycleOrderMapper.selectOne(orderPO);

        RecycleOrderPO orderUpdate = new RecycleOrderPO();
        orderUpdate.setOrderId(orderPO.getOrderId());
        orderUpdate.setModifier(recycleOrderDTO.getModifier());
        orderUpdate.setModifiedTime(recycleOrderDTO.getModifiedTime());

        switch (modifyType) {
            //接单操作
            case 1:
                if (null != orderPO.getOrderStatus() && orderPO.getOrderStatus() != RecycleOrderStatusEnum.WAIT_FOR_RECEIVE.key) {
                    msg = "不能接单，订单状态：" + RecycleOrderStatusEnum.getDescByKey(orderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(RecycleOrderStatusEnum.WAIT_FOR_RECYCLE.key);

                orderUpdate.setOrderReceiver(recycleOrderDTO.getModifier());
                orderUpdate.setReceivedTime(recycleOrderDTO.getModifiedTime());
                break;
            //回收操作
            case 2:
                if (null != orderPO.getOrderStatus() && orderPO.getOrderStatus() != RecycleOrderStatusEnum.WAIT_FOR_RECYCLE.key) {
                    msg = "不能回收，订单状态：" + RecycleOrderStatusEnum.getDescByKey(orderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(RecycleOrderStatusEnum.WAIT_FOR_SETTLE.key);
                break;
            //结算操作
            case 3:
                if (null != orderPO.getOrderStatus() && orderPO.getOrderStatus() != RecycleOrderStatusEnum.WAIT_FOR_SETTLE.key) {
                    msg = "不能结算，订单状态：" + RecycleOrderStatusEnum.getDescByKey(orderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(RecycleOrderStatusEnum.COMPLETE.key);
                break;
            //取消操作
            case 4:
                if (null != orderPO.getOrderStatus() && orderPO.getOrderStatus() != RecycleOrderStatusEnum.WAIT_FOR_RECEIVE.key) {
                    msg = "不能取消，订单状态：" + RecycleOrderStatusEnum.getDescByKey(orderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(RecycleOrderStatusEnum.CANCEL.key);
                break;
            default:
                throw new ServiceException("订单操作类型错误");

        }

        //修改订单状态
        recycleOrderMapper.updateByPrimaryKeySelective(orderUpdate);

        return null;
    }




}
