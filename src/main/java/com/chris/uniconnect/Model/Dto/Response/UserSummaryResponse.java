package com.chris.uniconnect.Model.Dto.Response;

import java.util.Set;

public record UserSummaryResponse(Integer id, String username, String email, boolean enabled,
                                   boolean accountNonLocked, Set<String> roles) {
}
