package org.sunbird.ruleengine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sunbird.ruleengine.dao.AsyncRequestDao;
import org.sunbird.ruleengine.model.AsyncRequest;

import org.sunbird.ruleengine.dao.AbstractDAO;
import org.sunbird.ruleengine.service.GenericServiceImpl;

@Service
@Transactional
public class AsyncRequestServiceImpl extends GenericServiceImpl<AsyncRequest, AsyncRequest>
		implements AsyncRequestService {

	@Autowired
	AsyncRequestDao asyncRequestDao;

	@Override
	public void save(AsyncRequest asyncRequest) {
		asyncRequestDao.save(asyncRequest);
	}

	@Override
	public AsyncRequest update(AsyncRequest asyncRequest) {
		return asyncRequestDao.update(asyncRequest);

	}

	@Override
	public AbstractDAO<AsyncRequest, AsyncRequest> getDAO() {
		return asyncRequestDao;

	}

}
