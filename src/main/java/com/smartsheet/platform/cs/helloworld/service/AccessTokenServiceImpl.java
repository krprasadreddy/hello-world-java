package com.smartsheet.platform.cs.helloworld.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartsheet.platform.cs.helloworld.model.AccessToken;

@Transactional
@Service
/**
 * Implementation for OAuth Token Database service. This implementation uses JPA.
 */
public class AccessTokenServiceImpl implements AccessTokenService {

	@PersistenceContext
	EntityManager em;

	@Override
	public void saveToken(AccessToken oauthtoken) {
		em.merge(oauthtoken);
	}

	@Override
	public AccessToken getToken(String tokenId) {
		AccessToken token = em.find(AccessToken.class, tokenId);
		return token;
	}

}
