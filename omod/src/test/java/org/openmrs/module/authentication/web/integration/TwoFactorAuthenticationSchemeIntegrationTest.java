/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.authentication.web.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.authentication.web.TwoFactorAuthenticationScheme;
import org.openmrs.web.test.jupiter.BaseModuleWebContextSensitiveTest;
import org.junit.jupiter.api.BeforeAll;
import org.openmrs.api.context.UsernamePasswordAuthenticationScheme;
import org.openmrs.module.authentication.AuthenticationConfig;

import java.util.Properties;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoFactorAuthenticationSchemeIntegrationTest extends BaseModuleWebContextSensitiveTest {
	
	@BeforeAll
	static void resetAuthenticationScheme() throws Exception {
		AuthenticationConfig.setConfig(new Properties());
		Field field = Context.class.getDeclaredField("authenticationScheme");
		field.setAccessible(true);
		field.set(null, new UsernamePasswordAuthenticationScheme());
		Context.clearUserContext();
	}

	private TwoFactorAuthenticationScheme scheme;
	
	@BeforeEach
	void setUp() {
		scheme = new TwoFactorAuthenticationScheme();
	}
	
	@Nested
	@DisplayName("addSecondaryAuthenticationSchemeForUser")
	class AddSecondaryAuthenticationSchemeForUser {
		
		@Test
		@DisplayName("should save the scheme to the database")
		void shouldSaveSchemeToDatabase() {
			User user = Context.getUserService().getUser(1);
			user.getUserProperties().size();
			Context.evictFromSession(user);
			
			scheme.addSecondaryAuthenticationSchemeForUser(user, "totp");
			
			Context.flushSession();
			Context.clearSession();
			
			User savedUser = Context.getUserService().getUser(1);
			
			String savedProperty = savedUser.getUserProperty(TwoFactorAuthenticationScheme.USER_PROPERTY_SECONDARY_TYPE);
			assertEquals("totp", savedProperty);
		}
	}
}
