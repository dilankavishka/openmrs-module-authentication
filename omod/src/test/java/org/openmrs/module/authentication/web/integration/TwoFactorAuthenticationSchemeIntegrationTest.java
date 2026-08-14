package org.openmrs.module.authentication.web.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.authentication.web.TwoFactorAuthenticationScheme;
import org.openmrs.web.test.jupiter.BaseModuleWebContextSensitiveTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoFactorAuthenticationSchemeIntegrationTest extends BaseModuleWebContextSensitiveTest {
	
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
			
			scheme.addSecondaryAuthenticationSchemeForUser(user, "totp");
			
			Context.flushSession();
			Context.clearSession();
			
			User savedUser = Context.getUserService().getUser(1);
			
			String savedProperty = savedUser.getUserProperty(TwoFactorAuthenticationScheme.USER_PROPERTY_SECONDARY_TYPE);
			assertEquals("totp", savedProperty);
		}
	}
}
