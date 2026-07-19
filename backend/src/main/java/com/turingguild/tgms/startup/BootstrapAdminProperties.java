package com.turingguild.tgms.startup;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tgms.bootstrap-admin")
public class BootstrapAdminProperties {

    private boolean enabled = false;
    private String fullName = "System Administrator";
    private String email;
    private String password;
}
