package org.sunbird.ruleengine.oauth2.security;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.sunbird.ruleengine.common.CommonUtil;
import org.sunbird.ruleengine.common.util.PasswordUtil;
import org.sunbird.ruleengine.dao.UserDao;
import org.sunbird.ruleengine.model.User;
import org.sunbird.ruleengine.model.UserEvent;
import org.sunbird.ruleengine.model.UserEvent.Action;
import org.sunbird.ruleengine.service.ClientService;
import org.sunbird.ruleengine.service.UserEventService;
import org.sunbird.ruleengine.vo.UserVo;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
	 private static final Logger logger = LogManager.getLogger(UserAuthenticationProvider.class);
  @Autowired
  UserDao userDao;
  
  @Autowired
  UserEventService userEventService;
  
  @Autowired
  ClientService clientService;
 
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {     
    	UserVo user= new UserVo();
        if(CommonUtil.isBlank(authentication.getPrincipal().toString()))
        {

			throw new BadCredentialsException(
					"USERNAME_REQUIRED");
        }

        
       LinkedHashMap<String, Object> properties =  (LinkedHashMap<String, Object>) authentication.getDetails();
      
        user.setUserName(authentication.getPrincipal().toString());
        user.setClientId(new BigDecimal(clientService.getClientIdByCode(properties.get("clientCode").toString())));
        System.out.println("The Autherities is "+authentication.getAuthorities().toString());
        logger.info("The Autherities is "+authentication.getAuthorities().toString());
        System.out.println("The Name is "+authentication.getName().toString());
        logger.info("The Name is "+authentication.getName().toString());
        System.out.println("The Details is "+authentication.getDetails().toString());
        logger.info("The Details is "+authentication.getDetails().toString());
       System.out.println("The Client Code is "+properties.get("clientCode"));
       logger.info("The Client Code is "+properties.get("clientCode"));
        System.out.println("The Principal is "+authentication.getPrincipal().toString());
        logger.info("The Principal is "+authentication.getPrincipal().toString());
        if(CommonUtil.isBlank(authentication.getCredentials().toString()))
        {
			throw new BadCredentialsException(
					"PASSWORD_REQUIRED");
        }
        //user.setPassword( authentication.getCredentials().toString());
        List<User> users=userDao.getListByCriteria(user, -1, 0, null, false, null);
      
        String password="";
		try {
			password = PasswordUtil.getHash(100, authentication.getCredentials().toString(), users.get(0).getSalt());
		} catch (NoSuchAlgorithmException e) {
			// 
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		} catch (UnsupportedEncodingException e) {
			// 
			e.printStackTrace();
			logger.error(MarkerFactory.getMarker("Exception") , e);
		}
        	   if (!(password.equals(users.get(0).getPassword()))) {
        	    throw new BadCredentialsException(
        	      "PASSWORD_NOT_CORRECT");
        	   }
        	   if (users.size()==0) {
        	       	 throw new BadCredentialsException("BAD_USER_CREDENTIALS");
        	       }
        else if(!users.get(0).isEmailConfirmed())
    	{
    	 throw new BadCredentialsException("EMAIL_NOT_CONFIRMED");
    	}
        else if(!users.get(0).isActive()){
			throw new BadCredentialsException("INACTIVE_USER");
		}
        else {

                List<GrantedAuthority> grantedAuthorities =
    new ArrayList<GrantedAuthority>();
             
               UserAuthenticationToken auth =
           new UserAuthenticationToken(users.get(0).getId(),
                authentication.getCredentials(), grantedAuthorities);
               auth.setAuthenticated(true);
               SecurityContextHolder.getContext().setAuthentication(auth);
               
               UserEvent userEvent= new UserEvent();
               userEvent.setCreationDate(new Date());
               userEvent.setCreatedBy(users.get(0).getId());
               userEvent.setLastUpdatedBy(users.get(0).getId());;
               userEvent.setLastUpdateDate(new Date());
               userEvent.setClientId(new BigInteger("1"));;
               userEvent.setAction(Action.LOGIN);
               userEvent.setUserId(users.get(0).getId());
               userEvent.setUserName(users.get(0).getUserName());
               userEvent.setFullName(users.get(0).getFirstName()+" "+users.get(0).getLastName() );
               userEventService.save(userEvent);
               
                return auth;
           
        }
    }
 
    public boolean supports(Class<?> arg0) {
        return true;
    }

	
}