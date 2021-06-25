package org.sunbird.ruleengine.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.UserRoleJoinDAO;
import org.sunbird.ruleengine.model.UserRoleJoin;
import org.sunbird.ruleengine.vo.UserRoleJoinVo;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.AbstractUserRoleJoinServiceImpl;

@Service
@Transactional
public class UserRoleJoinServiceImpl extends AbstractUserRoleJoinServiceImpl<UserRoleJoin, UserRoleJoinVo> implements UserRoleJoinService{

	@Autowired
	UserRoleJoinDAO userRoleJoinDAO;


	@Override
	public AbstractDAO<UserRoleJoin, UserRoleJoinVo> getDAO() {

		return userRoleJoinDAO;
	}


	@SuppressWarnings("null")
	@Override
	public void saveUserRole(UserRoleJoin t) {
		UserRoleJoinVo criteria =new UserRoleJoinVo();
		criteria.setUserId(t.getUserId());
		//criteria.setOrgId(t.getOrgId());
		List<UserRoleJoin> list=userRoleJoinDAO.getListByCriteria(criteria,  -1, 0);
		
		
		//List<UserRoleJoin> list = null ;
		for (UserRoleJoin userRoleJoin : list) {
			if(userRoleJoin.getUserId().compareTo(t.getUserId())==0)
			{
				userRoleJoinDAO.delete(userRoleJoin.getId());
			}



		}
		List<BigInteger> obj=t.getList();
		for(int i=0;i<obj.size();i++){
			UserRoleJoin term= new UserRoleJoin();
			term.setUserId(t.getUserId());
			term.setRoleId(obj.get(i));

			super.save(term);

		}

	}



}
