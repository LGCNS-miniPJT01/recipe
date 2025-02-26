package recipe.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import recipe.entity.User;

public class CustomUserDetails implements UserDetails {
	
	private User user;
	
	public CustomUserDetails(User user) {
        this.user = user;
    } 
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
            	
            	return "ROLE_" + user.getRole().name();
            }
        });
        return collection;
    }
	
	@Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }


}
