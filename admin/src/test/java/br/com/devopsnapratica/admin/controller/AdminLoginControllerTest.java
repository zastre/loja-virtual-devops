package br.com.devopsnapratica.admin.controller;

import org.broadleafcommerce.common.service.GenericResponse;
import org.broadleafcommerce.openadmin.server.security.service.AdminSecurityService;
import org.broadleafcommerce.openadmin.web.form.ResetPasswordForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminLoginControllerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private Model model;
    @Mock
    private AdminSecurityService adminSecurityService;
    private AdminLoginController adminLoginController;

    @Before
    public void setUp() throws Exception {
        adminLoginController = new AdminLoginController(adminSecurityService);
    }

    @Test
    public void shouldProcessResetPasswordWhenTokenIsPresent() {
        // Given
        final ResetPasswordForm resetPasswordForm = new ResetPasswordForm();
        resetPasswordForm.setToken("someToken");

        when(adminSecurityService.resetPasswordUsingToken(anyString(), eq("someToken"), anyString(), anyString())).thenReturn(new GenericResponse());

        // When
        final String response = adminLoginController.processResetPassword(resetPasswordForm, request, this.response, model);

        // Then
        assertThat(response, equalTo("redirect:login?messageCode=passwordReset"));
    }

    @Test
    public void shouldSendPasswordResetNotificationToUserWhenTokenIsNotPresent() {
        // Given
        final ResetPasswordForm resetPasswordForm = new ResetPasswordForm();
        resetPasswordForm.setUsername("username");

        when(adminSecurityService.sendResetPasswordNotification(eq("username"))).thenReturn(new GenericResponse());
        when(request.getSession(true)).thenReturn(session);

        // When
        final String response = adminLoginController.processResetPassword(resetPasswordForm, request, this.response, model);

        // Then
        assertThat(response, equalTo("redirect:resetPassword?messageCode=passwordTokenSent"));
        verify(session).setAttribute(eq("forgot_password_username"), eq("username"));
    }

}
