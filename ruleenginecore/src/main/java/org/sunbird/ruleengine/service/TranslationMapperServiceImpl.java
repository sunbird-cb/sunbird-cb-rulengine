package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.TranslationMapperDAO;
import org.sunbird.ruleengine.model.TranslationMapper;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class TranslationMapperServiceImpl extends GenericServiceImpl<TranslationMapper, TranslationMapper> implements TranslatioinMapperService {

	@Autowired
	TranslationMapperDAO translationMapperDao;
	
	@Override
	public AbstractDAO<TranslationMapper, TranslationMapper> getDAO() {
		// TODO Auto-generated method stub
		return translationMapperDao;
	}

}
