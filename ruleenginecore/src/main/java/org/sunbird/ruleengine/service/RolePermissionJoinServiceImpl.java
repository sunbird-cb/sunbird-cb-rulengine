package org.sunbird.ruleengine.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.RolePermissionJoinDAO;
import org.sunbird.ruleengine.model.RolePermissionJoin;
import org.sunbird.ruleengine.vo.RolePermissionJoinVo;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.AbstractRolePermissionJoinServiceImpl;

@Service
@Transactional
public class RolePermissionJoinServiceImpl extends AbstractRolePermissionJoinServiceImpl<RolePermissionJoin, RolePermissionJoinVo> implements RolePermissionJoinService{

	@Autowired
	RolePermissionJoinDAO rolePermissionJoinDAO;

	@Override
	public AbstractDAO<RolePermissionJoin, RolePermissionJoinVo> getDAO() {

		return rolePermissionJoinDAO;
	}

	@Override
	public void saveRolePermission(RolePermissionJoin t) {

		RolePermissionJoinVo criteria =new RolePermissionJoinVo();
		criteria.setRoleId(t.getRoleId());
		List<RolePermissionJoin> list=rolePermissionJoinDAO.getListByCriteria(criteria, -1, 0, null, true, null);
		//	.getListByCriteria(criteria, "SELECT userRole.id, userRole.USER_ID, userRole.ROLE_ID ,role.ROLE_NAME FROM USER_ROLE_JOIN userRole left join ROLE role on(role.ID=userRole.ROLE_ID)  where 1=1  ", "order by userRole.ID DESC ", -1, 0, new UserRoleJoinMapper());

		for (RolePermissionJoin rolePermissionJoin : list) {
			if(rolePermissionJoin.getRoleId().compareTo(t.getRoleId())==0)
			{
				rolePermissionJoinDAO.delete(rolePermissionJoin.getId());
			}



		}
		List<BigInteger> obj=t.getList();
		for(int i=0;i<obj.size();i++){



			RolePermissionJoin term= new RolePermissionJoin();
			term.setRoleId(t.getRoleId());
			term.setOrgId(t.getOrgId());

			term.setPermissionId(obj.get(i));
			term.setClientId(new BigDecimal("1"));
			term.setCreationDate(new Date());
			term.setLastUpdateDate(new Date());

			super.save(term);

		}



	}

}
