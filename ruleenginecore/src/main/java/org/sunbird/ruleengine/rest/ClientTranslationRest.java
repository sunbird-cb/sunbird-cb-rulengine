package org.sunbird.ruleengine.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.sunbird.ruleengine.common.util.CommonMessages;
import org.sunbird.ruleengine.model.ClientTranslation;
import org.sunbird.ruleengine.model.TranslationMapper;
import org.sunbird.ruleengine.service.ClientTranslationService;
import org.sunbird.ruleengine.service.TranslatioinMapperService;

import org.sunbird.ruleengine.common.Response;
import org.sunbird.ruleengine.common.RestHelper;
import org.sunbird.ruleengine.common.rest.GenericMultiTenantRoleBasedSecuredRest;
import org.sunbird.ruleengine.service.GenericService;

@RestController
@RequestMapping(value = "{clientCode}/clientTranslation")
public class ClientTranslationRest
		extends GenericMultiTenantRoleBasedSecuredRest<ClientTranslation, ClientTranslation> {

	public ClientTranslationRest() {
		super(ClientTranslation.class, ClientTranslation.class);
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public List<ClientTranslation> getListByCriteria(@PathVariable("clientCode") String clientCode,
			@ModelAttribute ClientTranslation t, @RequestParam(value = "firstResult", required = false) int firstResult,
			@RequestParam(value = "maxResult", required = false) int maxResult, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		return super.getListByCriteria(clientCode, t, firstResult, maxResult, principal);
	}

	@Autowired
	ClientTranslationService clientTranslationService;

	@Autowired
	TranslatioinMapperService translatioinMapperService;

	@Autowired
	org.sunbird.ruleengine.service.ClientService clientService;

	/*
	 * @Autowired ClientTranslationCsvUploadHelper clientTranslationCsvUploadHelper;
	 */

	@Override
	public String rolePrefix() {
		return "clientTranslation";
	}

	@Override
	public GenericService<ClientTranslation, ClientTranslation> getService() {
		return clientTranslationService;
	}

	@Override
	public GenericService<ClientTranslation, ClientTranslation> getUserService() {
		return clientTranslationService;
	}

	@Override
	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody Response<ClientTranslation> save(@PathVariable("clientCode") String clientCode,
			@RequestBody ClientTranslation t, Principal principal) {

		super.validateAuthorization(clientCode, principal);

		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
		RestHelper.setPrincipalDetails(t, principal);
		if (clientTranslationService.codeAndKeyValidation(t)) {
			if (t.isReciprocal()) {
				if (clientTranslationService.uniqueResolutionValidation(t)) {
					ClientTranslation clientTranslation = new ClientTranslation();
					clientTranslation.setKey(t.getValue());
					clientTranslation.setValue(t.getKey());
					clientTranslation.setKeyDescription(t.getValueDescription());
					clientTranslation.setValueDescription(t.getKeyDescription());
					clientTranslation.setReciprocal(false);
					clientTranslation.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
					clientTranslation.setCode(translatioinMapperService
							.getEntityByColumnNameAndValue("code", t.getCode()).getReciprocalCode());
					save(clientCode, clientTranslation, principal);
					return super.save(clientCode, t, principal);
				}

				else {
					return new Response<ClientTranslation>(false, null, CommonMessages.RESOLUTION_VALIDATION);
				}
			} else {
				TranslationMapper translationMapper = translatioinMapperService.getEntityByColumnNameAndValue("code",
						t.getCode());
				if (translationMapper.getReciprocalCode() != null) {
					ClientTranslation criteria = new ClientTranslation();
					criteria.setCode(translationMapper.getReciprocalCode());
					criteria.setKey(t.getValue());
					java.util.List<ClientTranslation> clientTranslationObjectsToBeDeleted = clientTranslationService
							.getListByCriteria(criteria, -1, 0);
					for (ClientTranslation clientTranslation : clientTranslationObjectsToBeDeleted) {
						clientTranslationService.delete(clientTranslation.getId());
					}

				}

				return super.save(clientCode, t, principal);
			}
		} else {
			return new Response<ClientTranslation>(false, null, CommonMessages.KEY_CODE_VALIDATION_ERROR);
		}

	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody Response<ClientTranslation> update(@PathVariable("clientCode") String clientCode,
			@RequestBody ClientTranslation t, Principal principal) {
		t.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));

		RestHelper.setPrincipalDetails(t, principal);
		super.validateAuthorization(clientCode, principal);
		if (clientTranslationService.codeAndKeyValidationExceptThis(t)) {
			if (t.isReciprocal()) {
				if (clientTranslationService.uniqueResolutionValidationExceptThis(t)) {
					// delete the previous reciprocal clientTranslation Data
					TranslationMapper translationMapper = translatioinMapperService
							.getEntityByColumnNameAndValue("code", t.getCode());
					if (translationMapper.getReciprocalCode() != null) {
						ClientTranslation criteria = new ClientTranslation();
						criteria.setCode(translationMapper.getReciprocalCode());
						criteria.setKey(get(clientCode, t.getId(), principal).getCode());
						java.util.List<ClientTranslation> clientTranslationObjectsToBeDeleted = clientTranslationService
								.getListByCriteria(criteria, -1, 0);
						for (ClientTranslation clientTranslation : clientTranslationObjectsToBeDeleted) {
							clientTranslationService.delete(clientTranslation.getId());
						}
					}

					ClientTranslation clientTranslation = new ClientTranslation();
					clientTranslation.setKey(t.getValue());
					clientTranslation.setValue(t.getKey());
					clientTranslation.setKeyDescription(t.getValueDescription());
					clientTranslation.setValueDescription(t.getKeyDescription());
					clientTranslation.setReciprocal(false);
					clientTranslation.setClientId(clientService.getClientCodeById(clientCode, t.getClientId()));
					clientTranslation.setCode(translatioinMapperService
							.getEntityByColumnNameAndValue("code", t.getCode()).getReciprocalCode());
					save(clientCode, clientTranslation, principal);
					return super.update(clientCode, t, principal);
				}

				else {
					return new Response<ClientTranslation>(false, null, CommonMessages.RESOLUTION_VALIDATION);
				}
			} else {
				TranslationMapper translationMapper = translatioinMapperService.getEntityByColumnNameAndValue("code",
						t.getCode());
				if (translationMapper.getReciprocalCode() != null) {
					ClientTranslation criteria = new ClientTranslation();
					criteria.setCode(translationMapper.getReciprocalCode());
					criteria.setKey(t.getValue());
					java.util.List<ClientTranslation> clientTranslationObjectsToBeDeleted = clientTranslationService
							.getListByCriteria(criteria, -1, 0);
					for (ClientTranslation clientTranslation : clientTranslationObjectsToBeDeleted) {
						clientTranslationService.delete(clientTranslation.getId());
					}
				}
				return super.update(clientCode, t, principal);
			}
		} else {
			return new Response<ClientTranslation>(false, null, CommonMessages.KEY_CODE_VALIDATION_ERROR);
		}

	}

	// To save data from csv file of client translation
	/*
	 * @RequestMapping(value = "/translationCsvUpload/{clientId}/{translationCode}",
	 * method = RequestMethod.POST) public @ResponseBody Response<ClientTranslation>
	 * saveCsvUploadData(@PathVariable("clientCode") String clientCode,
	 * 
	 * @RequestBody MultipartFile file, Principal
	 * principal, @PathVariable("clientId") BigInteger clientId,
	 * 
	 * @PathVariable("translationCode") String translationCode) {
	 * 
	 * super.validateAuthorization(clientCode, principal); clientId =
	 * clientService.getClientCodeById(clientCode, clientId);
	 * clientTranslationCsvUploadHelper.translationFromCsv(file, clientId,
	 * translationCode, principal, clientCode); return null;
	 * 
	 * }
	 */

	@RequestMapping(value = "/deleteWithBigInteger", method = RequestMethod.POST)
	public @ResponseBody Response<ClientTranslation> delete(@PathVariable("clientCode") String clientCode,
			@RequestBody ClientTranslation t, Principal principal) {

		super.validateAuthorization(clientCode, principal);

		if (t.isReciprocal()) {
			TranslationMapper translationMapper = translatioinMapperService.getEntityByColumnNameAndValue("code",
					t.getCode());
			if (translationMapper.getReciprocalCode() != null) {
				ClientTranslation criteria = new ClientTranslation();
				criteria.setKey(t.getValue());
				criteria.setCode(translationMapper.getReciprocalCode());
				java.util.List<ClientTranslation> clientTranslationObjectsToBeDeleted = clientTranslationService
						.getListByCriteria(criteria, -1, 0);
				for (ClientTranslation clientTranslation : clientTranslationObjectsToBeDeleted) {

					clientTranslationService.delete(clientTranslation.getId());
				}

			}
		}
		clientTranslationService.delete(t.getId());
		return new Response<ClientTranslation>(true, null);
	}

}
