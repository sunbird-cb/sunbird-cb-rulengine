package org.sunbird.ruleengine.service;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.ClientDao;
import org.sunbird.ruleengine.model.Client;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class ClientServiceImpl extends GenericServiceImpl<Client, Client> implements ClientService {

	@Autowired
	ClientDao clientDao;

	@Override
	public AbstractDAO<Client, Client> getDAO() {
		return clientDao;
	}

	@Override
	public BigInteger getClientIdByCode(String clientCode) {
		if(clientDao.getListByColumnNameAndValue("code", clientCode).size()>0 )
				return clientDao.getListByColumnNameAndValue("code", clientCode).get(0).getId();
		else 
			return null;
	}

	@Override
	public BigInteger getClientCodeById(String clientCode,BigInteger clientId) {
		if(clientDao.getListByColumnNameAndValue("code", clientCode).size()>0 )
		{
			if(!clientDao.getListByColumnNameAndValue("code", clientCode).get(0).isAdmin())
				return clientDao.getListByColumnNameAndValue("code", clientCode).get(0).getId();
			else
				return clientId;
		}
			
		else 
			return null;
	}

}
