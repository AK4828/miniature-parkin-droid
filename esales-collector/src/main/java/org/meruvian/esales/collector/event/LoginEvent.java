package org.meruvian.esales.collector.event;

import org.meruvian.esales.collector.entity.Authentication;

/**
 * Created by meruvian on 29/07/15.
 */
public class LoginEvent {
    public static class DoLogin {}

    public static class LoginSuccess {
        private Authentication authentication;

        public LoginSuccess(Authentication authentication) {
            this.authentication = authentication;
        }

        public Authentication getAuthentication() {
            return authentication;
        }
    }

    public static class LoginFailed {
        private final int statusCode;

        public LoginFailed(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}
