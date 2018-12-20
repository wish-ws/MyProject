package com.um.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.um.common.enums.ImgBusinessTypeEnum;
import com.um.common.enums.OrderTypeEnum;
import com.um.common.enums.TransactionOrderStatusEnum;
import com.um.common.enums.VerificationStatusEnum;
import com.um.common.exception.ServiceException;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.BusinessImgDTO;
import com.um.domain.dto.TransactionOrderDTO;
import com.um.domain.po.BusinessImgPO;
import com.um.domain.po.TransactionOrderPO;
import com.um.domain.po.UserPO;
import com.um.domain.request.OrderQueryRequeset;
import com.um.mapper.BusinessImgMapper;
import com.um.mapper.TransactionOrderMapper;
import com.um.mapper.UserMapper;
import com.um.service.TransactionOrderService;
import com.um.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:50
 */
@Slf4j
@Service
public class TransactionOrderServiceImpl implements TransactionOrderService {

    @Autowired
    private TransactionOrderMapper transactionOrderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BusinessImgMapper businessImgMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createTransactionOrder(TransactionOrderDTO transactionOrderDTO) {

        //判断行业认证
        UserPO userPO = userMapper.selectByPrimaryKey(transactionOrderDTO.getCreatorUserId());
        if(VerificationStatusEnum.NO.key == userPO.getVerificationStatus()){
            log.error(transactionOrderDTO.getCreator() + "未认证，尚不可下单");
            throw new ServiceException("联系我们，马上认证即可下单");
        }

        TransactionOrderPO transactionOrderPO = BeanUtil.transformBean(transactionOrderDTO,TransactionOrderPO.class);
        transactionOrderMapper.insert(transactionOrderPO);

    }

    @Override
    public PaginationSupportDTO queryTransactionOrderPage(OrderQueryRequeset orderQueryRequeset) {
        PaginationSupportDTO<TransactionOrderDTO> paginationSupportDTO = new PaginationSupportDTO();
        PageHelper.startPage(orderQueryRequeset.getCurrentPage(), orderQueryRequeset.getPageSize());

        List<TransactionOrderDTO> transactionOrderDTOList = transactionOrderMapper.queryTransactionOrderPage(orderQueryRequeset);

        PageInfo<TransactionOrderDTO> pageInfo = new PageInfo<>(transactionOrderDTOList);
        paginationSupportDTO.copyProperties(pageInfo);
        return paginationSupportDTO;
    }

    @Override
    public TransactionOrderDTO queryTransactionOrderDetail(String orderCode) {
        TransactionOrderPO transactionOrderPO = new TransactionOrderPO();
        transactionOrderPO.setOrderCode(orderCode);
        transactionOrderPO = transactionOrderMapper.selectOne(transactionOrderPO);

        if(null == transactionOrderPO){
            log.error("未查询到交易订单");
            throw new ServiceException("未查询到交易订单");
        }

        TransactionOrderDTO transactionOrderDTO = BeanUtil.transformBean(transactionOrderPO,TransactionOrderDTO.class);
        UserPO buyerUser = userMapper.selectByPrimaryKey(transactionOrderDTO.getBuyerUserId());
        UserPO sellerUser = userMapper.selectByPrimaryKey(transactionOrderDTO.getSellerUserId());
        BusinessImgPO imgPO = new BusinessImgPO();
        imgPO.setBusinessCode(orderCode);
        imgPO.setBusinessType(ImgBusinessTypeEnum.STATEMENT.key);
        List<BusinessImgPO> businessImgPOList = businessImgMapper.select(imgPO);


        if(null != buyerUser){
            transactionOrderDTO.setBuyerUserName(buyerUser.getUserName());
            transactionOrderDTO.setBuyerAccountName(buyerUser.getAccountName());
        }

        if(null != sellerUser){
            transactionOrderDTO.setSellerUserName(sellerUser.getUserName());
            transactionOrderDTO.setSellerAccountName(sellerUser.getAccountName());
        }

        transactionOrderDTO.setBusinessImgDTOList(BeanUtil.transformList(businessImgPOList,BusinessImgDTO.class));
        return transactionOrderDTO;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String modifyTransactionOrder(TransactionOrderDTO transactionOrderDTO, Integer modifyType) {
        String msg = null;

        TransactionOrderPO transactionOrderPO = new TransactionOrderPO();
        transactionOrderPO.setOrderCode(transactionOrderDTO.getOrderCode());

        transactionOrderPO = transactionOrderMapper.selectOne(transactionOrderPO);

        TransactionOrderPO orderUpdate = new TransactionOrderPO();
        orderUpdate.setOrderId(transactionOrderPO.getOrderId());
        orderUpdate.setModifier(transactionOrderDTO.getModifier());
        orderUpdate.setModifiedTime(transactionOrderDTO.getModifiedTime());
        List<BusinessImgDTO> businessImgDTOList = transactionOrderDTO.getBusinessImgDTOList();
        List<BusinessImgPO> businessImgPOList = new ArrayList<>();

        switch (modifyType) {
            //接单操作
            case 1:
                if(null != transactionOrderPO.getOrderStatus() && transactionOrderPO.getOrderStatus() != TransactionOrderStatusEnum.WAIT_FOR_RECEIVE.key){
                    msg = "不能接单，订单状态：" + TransactionOrderStatusEnum.getDescByKey(transactionOrderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(TransactionOrderStatusEnum.WAIT_FOR_DELIVER.key);
                orderUpdate.setOrderReceiver(transactionOrderDTO.getModifier());
                orderUpdate.setReceivedTime(transactionOrderDTO.getModifiedTime());
                break;
            //交货操作
            case 2:
                if(null != transactionOrderPO.getOrderStatus() && transactionOrderPO.getOrderStatus() != TransactionOrderStatusEnum.WAIT_FOR_DELIVER.key){
                    msg = "不能回收，订单状态：" + TransactionOrderStatusEnum.getDescByKey(transactionOrderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(TransactionOrderStatusEnum.WAIT_FOR_SETTLE.key);
                break;
            //支付操作
            case 3:
                if(null != transactionOrderPO.getOrderStatus() && transactionOrderPO.getOrderStatus() != TransactionOrderStatusEnum.WAIT_FOR_SETTLE.key){
                    msg = "不能支付，订单状态：" + TransactionOrderStatusEnum.getDescByKey(transactionOrderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(TransactionOrderStatusEnum.WAIT_FOR_AUDIT.key);
                if(CollectionUtils.isNotEmpty(businessImgDTOList)){
                    for (BusinessImgDTO businessImgDTO : businessImgDTOList) {
                        BusinessImgPO businessImgPO = new BusinessImgPO();
                        businessImgPO.setBusinessCode(transactionOrderDTO.getOrderCode());
                        businessImgPO.setBusinessType(ImgBusinessTypeEnum.STATEMENT.key);
                        businessImgPO.setImgPath(businessImgDTO.getImgPath());
                        businessImgPO.setCreator(transactionOrderDTO.getModifier());
                        businessImgPO.setCreatedTime(transactionOrderDTO.getModifiedTime());
                        businessImgPOList.add(businessImgPO);
                    }
                }
                break;
            //审核支付操作
            case 4:
                if(null != transactionOrderPO.getOrderStatus() && transactionOrderPO.getOrderStatus() != TransactionOrderStatusEnum.WAIT_FOR_AUDIT.key){
                    msg = "不能审核支付，订单状态：" + TransactionOrderStatusEnum.getDescByKey(transactionOrderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(TransactionOrderStatusEnum.COMPLETE.key);
                break;
            //取消操作
            case 5:
                if(null != transactionOrderPO.getOrderStatus() && transactionOrderPO.getOrderStatus() != TransactionOrderStatusEnum.WAIT_FOR_RECEIVE.key){
                    msg = "不能取消，订单状态：" + TransactionOrderStatusEnum.getDescByKey(transactionOrderPO.getOrderStatus());
                    return msg;
                }
                orderUpdate.setOrderStatus(TransactionOrderStatusEnum.CANCEL.key);
                break;
            default:
                throw new ServiceException("订单操作类型错误");
        }


        //修改订单状态
        transactionOrderMapper.updateByPrimaryKeySelective(orderUpdate);

        //保存支付图片
        if(CollectionUtils.isNotEmpty(businessImgPOList)){
            businessImgMapper.insertList(businessImgPOList);
        }

        return null;
    }


}
